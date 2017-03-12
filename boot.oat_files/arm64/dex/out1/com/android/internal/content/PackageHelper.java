package com.android.internal.content;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageParser.PackageLite;
import android.os.Environment;
import android.os.FileUtils;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.storage.IMountService;
import android.os.storage.IMountService.Stub;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.os.storage.VolumeInfo;
import android.util.ArraySet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Slog;
import com.android.internal.content.NativeLibraryHelper.Handle;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import libcore.io.IoUtils;

public class PackageHelper {
    public static final int APP_INSTALL_AUTO = 0;
    public static final int APP_INSTALL_EXTERNAL = 2;
    public static final int APP_INSTALL_INTERNAL = 1;
    private static boolean DEBUG_SD_PATH = false;
    public static final int RECOMMEND_FAILED_ALREADY_EXISTS = -4;
    public static final int RECOMMEND_FAILED_INSUFFICIENT_STORAGE = -1;
    public static final int RECOMMEND_FAILED_INVALID_APK = -2;
    public static final int RECOMMEND_FAILED_INVALID_LOCATION = -3;
    public static final int RECOMMEND_FAILED_INVALID_URI = -6;
    public static final int RECOMMEND_FAILED_VERSION_DOWNGRADE = -7;
    public static final int RECOMMEND_INSTALL_EXTERNAL = 2;
    public static final int RECOMMEND_INSTALL_INTERNAL = 1;
    public static final int RECOMMEND_MEDIA_UNAVAILABLE = -5;
    private static final String TAG = "PackageHelper";
    private static final boolean localLOGV = false;

    public static IMountService getMountService() throws RemoteException {
        IBinder service = ServiceManager.getService("mount");
        if (service != null) {
            return Stub.asInterface(service);
        }
        Log.e(TAG, "Can't get mount service");
        throw new RemoteException("Could not contact mount service");
    }

    public static String createSdDir(long sizeBytes, String cid, String sdEncKey, int uid, boolean isExternal) {
        int sizeMb = ((int) (((((double) sizeBytes) * 1.03d) + 1048576.0d) / 1048576.0d)) + 1;
        try {
            int rc;
            IMountService mountService = getMountService();
            if (Environment.isExternalStorageEmulated() || !isExternal) {
                Log.i(TAG, "createSdDir with ext4");
                rc = mountService.createSecureContainer(cid, sizeMb, "ext4", sdEncKey, uid, isExternal);
            } else {
                Log.i(TAG, "createSdDir with fat");
                rc = mountService.createSecureContainer(cid, sizeMb, "fat", sdEncKey, uid, isExternal);
            }
            if (rc == 0) {
                return mountService.getSecureContainerPath(cid);
            }
            Log.e(TAG, "Failed to create secure container " + cid);
            return null;
        } catch (RemoteException e) {
            Log.e(TAG, "MountService running?");
            return null;
        }
    }

    public static boolean resizeSdDir(long sizeBytes, String cid, String sdEncKey) {
        try {
            if (getMountService().resizeSecureContainer(cid, ((int) ((sizeBytes + 1048576) / 1048576)) + 1, sdEncKey) == 0) {
                return true;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "MountService running?");
        }
        Log.e(TAG, "Failed to create secure container " + cid);
        return false;
    }

    public static String mountSdDir(String cid, String key, int ownerUid) {
        return mountSdDir(cid, key, ownerUid, true);
    }

    public static String mountSdDir(String cid, String key, int ownerUid, boolean readOnly) {
        String str = null;
        try {
            int rc = getMountService().mountSecureContainer(cid, key, ownerUid, readOnly);
            if (rc != 0) {
                Log.i(TAG, "Failed to mount container " + cid + " rc : " + rc);
            } else {
                str = getMountService().getSecureContainerPath(cid);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "MountService running?");
        }
        return str;
    }

    public static boolean unMountSdDir(String cid) {
        try {
            int rc = getMountService().unmountSecureContainer(cid, true);
            if (rc == 0) {
                return true;
            }
            Log.e(TAG, "Failed to unmount " + cid + " with rc " + rc);
            return false;
        } catch (RemoteException e) {
            Log.e(TAG, "MountService running?");
            return false;
        }
    }

    public static boolean renameSdDir(String oldId, String newId) {
        try {
            int rc = getMountService().renameSecureContainer(oldId, newId);
            if (rc == 0) {
                return true;
            }
            Log.e(TAG, "Failed to rename " + oldId + " to " + newId + "with rc " + rc);
            return false;
        } catch (RemoteException e) {
            Log.i(TAG, "Failed ot rename  " + oldId + " to " + newId + " with exception : " + e);
            return false;
        }
    }

    public static String getSdDir(String cid) {
        try {
            return getMountService().getSecureContainerPath(cid);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to get container path for " + cid + " with exception " + e);
            return null;
        }
    }

    public static String getSdFilesystem(String cid) {
        try {
            return getMountService().getSecureContainerFilesystemPath(cid);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to get container path for " + cid + " with exception " + e);
            return null;
        }
    }

    public static boolean finalizeSdDir(String cid) {
        try {
            if (getMountService().finalizeSecureContainer(cid) == 0) {
                return true;
            }
            Log.i(TAG, "Failed to finalize container " + cid);
            return false;
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to finalize container " + cid + " with exception " + e);
            return false;
        }
    }

    public static boolean destroySdDir(String cid) {
        try {
            if (getMountService().destroySecureContainer(cid, true) == 0) {
                return true;
            }
            Log.i(TAG, "Failed to destroy container " + cid);
            return false;
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to destroy container " + cid + " with exception " + e);
            return false;
        }
    }

    public static String[] getSecureContainerList() {
        try {
            return getMountService().getSecureContainerList();
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to get secure container list with exception" + e);
            return null;
        }
    }

    public static boolean isContainerMounted(String cid) {
        try {
            return getMountService().isSecureContainerMounted(cid);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to find out if container " + cid + " mounted");
            return false;
        }
    }

    public static long extractPublicFiles(File apkFile, File publicZipFile) throws IOException {
        FileOutputStream fstr;
        ZipOutputStream publicZipOutStream;
        if (publicZipFile == null) {
            fstr = null;
            publicZipOutStream = null;
        } else {
            fstr = new FileOutputStream(publicZipFile);
            publicZipOutStream = new ZipOutputStream(fstr);
            Log.d(TAG, "Extracting " + apkFile + " to " + publicZipFile);
        }
        long size = 0;
        ZipFile privateZip;
        try {
            privateZip = new ZipFile(apkFile.getAbsolutePath());
            Iterator i$ = Collections.list(privateZip.entries()).iterator();
            while (i$.hasNext()) {
                ZipEntry zipEntry = (ZipEntry) i$.next();
                String zipEntryName = zipEntry.getName();
                if ("AndroidManifest.xml".equals(zipEntryName) || "resources.arsc".equals(zipEntryName) || zipEntryName.startsWith("res/")) {
                    size += zipEntry.getSize();
                    if (publicZipFile != null) {
                        copyZipEntry(zipEntry, privateZip, publicZipOutStream);
                    }
                }
            }
            try {
                privateZip.close();
            } catch (IOException e) {
            }
            if (publicZipFile != null) {
                publicZipOutStream.finish();
                publicZipOutStream.flush();
                FileUtils.sync(fstr);
                publicZipOutStream.close();
                FileUtils.setPermissions(publicZipFile.getAbsolutePath(), DisplayMetrics.DENSITY_420, -1, -1);
            }
            IoUtils.closeQuietly(publicZipOutStream);
            return size;
        } catch (Throwable th) {
            IoUtils.closeQuietly(publicZipOutStream);
        }
    }

    private static void copyZipEntry(ZipEntry zipEntry, ZipFile inZipFile, ZipOutputStream outZipStream) throws IOException {
        ZipEntry newEntry;
        byte[] buffer = new byte[4096];
        if (zipEntry.getMethod() == 0) {
            newEntry = new ZipEntry(zipEntry);
        } else {
            newEntry = new ZipEntry(zipEntry.getName());
        }
        outZipStream.putNextEntry(newEntry);
        InputStream data = inZipFile.getInputStream(zipEntry);
        while (true) {
            try {
                int num = data.read(buffer);
                if (num <= 0) {
                    break;
                }
                outZipStream.write(buffer, 0, num);
            } finally {
                IoUtils.closeQuietly(data);
            }
        }
        outZipStream.flush();
    }

    public static boolean fixSdPermissions(String cid, int gid, String filename) {
        try {
            if (getMountService().fixPermissionsSecureContainer(cid, gid, filename) == 0) {
                return true;
            }
            Log.i(TAG, "Failed to fixperms container " + cid);
            return false;
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to fixperms container " + cid + " with exception " + e);
            return false;
        }
    }

    public static String resolveInstallVolume(Context context, String packageName, int installLocation, long sizeBytes) throws IOException {
        ApplicationInfo existingInfo = null;
        try {
            existingInfo = context.getPackageManager().getApplicationInfo(packageName, 8192);
        } catch (NameNotFoundException e) {
        }
        StorageManager storageManager = (StorageManager) context.getSystemService(StorageManager.class);
        boolean fitsOnInternal = fitsOnInternal(context, sizeBytes);
        ArraySet<String> allCandidates = new ArraySet();
        VolumeInfo bestCandidate = null;
        long bestCandidateAvailBytes = Long.MIN_VALUE;
        for (VolumeInfo vol : storageManager.getVolumes()) {
            if (vol.type == 1 && vol.isMountedWritable()) {
                long availBytes = storageManager.getStorageBytesUntilLow(new File(vol.path));
                if (availBytes >= sizeBytes) {
                    allCandidates.add(vol.fsUuid);
                }
                if (availBytes >= bestCandidateAvailBytes) {
                    bestCandidate = vol;
                    bestCandidateAvailBytes = availBytes;
                }
            }
        }
        if (existingInfo != null && existingInfo.isSystemApp()) {
            installLocation = 1;
        }
        if (installLocation != 1) {
            if (existingInfo != null) {
                if (existingInfo.volumeUuid == null && fitsOnInternal) {
                    return null;
                }
                if (allCandidates.contains(existingInfo.volumeUuid)) {
                    return existingInfo.volumeUuid;
                }
            }
            if (bestCandidate != null) {
                return bestCandidate.fsUuid;
            }
            if (fitsOnInternal) {
                return null;
            }
            throw new IOException("No special requests, but no room anywhere");
        } else if (fitsOnInternal) {
            return null;
        } else {
            throw new IOException("Requested internal only, but not enough space");
        }
    }

    public static boolean fitsOnInternal(Context context, long sizeBytes) {
        return sizeBytes <= ((StorageManager) context.getSystemService(StorageManager.class)).getStorageBytesUntilLow(Environment.getDataDirectory());
    }

    public static boolean fitsOnExternal(Context context, long sizeBytes) {
        StorageManager storage = (StorageManager) context.getSystemService(StorageManager.class);
        StorageVolume primary = storage.getPrimaryVolume();
        boolean emulated;
        if (hasActiveContainer(context)) {
            emulated = false;
        } else {
            emulated = true;
        }
        if (sizeBytes <= 0 || emulated || sizeBytes > storage.getStorageBytesUntilLow(primary.getPathFile())) {
            return false;
        }
        return true;
    }

    public static int resolveInstallLocation(Context context, String packageName, int installLocation, long sizeBytes, int installFlags) {
        int prefer;
        boolean checkBoth;
        ApplicationInfo existingInfo = null;
        try {
            existingInfo = context.getPackageManager().getApplicationInfo(packageName, 8192);
        } catch (NameNotFoundException e) {
        }
        if ((installFlags & 16) != 0) {
            prefer = 1;
            checkBoth = false;
        } else if ((installFlags & 8) != 0) {
            prefer = 2;
            checkBoth = false;
        } else if (installLocation == 1) {
            prefer = 1;
            checkBoth = false;
        } else if (installLocation == 2) {
            prefer = 2;
            checkBoth = true;
        } else if (installLocation == 0) {
            if (existingInfo == null) {
                prefer = 1;
            } else if ((existingInfo.flags & 262144) != 0) {
                prefer = 2;
            } else {
                prefer = 1;
            }
            checkBoth = true;
        } else {
            prefer = 1;
            checkBoth = false;
        }
        boolean fitsOnInternal = false;
        if (checkBoth || prefer == 1) {
            fitsOnInternal = fitsOnInternal(context, sizeBytes);
        }
        boolean fitsOnExternal = false;
        if (checkBoth || prefer == 2) {
            fitsOnExternal = fitsOnExternal(context, sizeBytes);
        }
        if (prefer == 1) {
            if (fitsOnInternal) {
                return 1;
            }
        } else if (prefer == 2 && fitsOnExternal) {
            return 2;
        }
        if (checkBoth) {
            if (fitsOnInternal) {
                return 1;
            }
            if (fitsOnExternal) {
                return 2;
            }
        }
        return -1;
    }

    public static long calculateInstalledSize(PackageLite pkg, boolean isForwardLocked, String abiOverride) throws IOException {
        Handle handle = null;
        try {
            handle = Handle.create(pkg);
            long calculateInstalledSize = calculateInstalledSize(pkg, handle, isForwardLocked, abiOverride);
            return calculateInstalledSize;
        } finally {
            IoUtils.closeQuietly(handle);
        }
    }

    public static long calculateInstalledSize(PackageLite pkg, Handle handle, boolean isForwardLocked, String abiOverride) throws IOException {
        long sizeBytes = 0;
        for (String codePath : pkg.getAllCodePaths()) {
            File codeFile = new File(codePath);
            sizeBytes += codeFile.length();
            if (isForwardLocked) {
                sizeBytes += extractPublicFiles(codeFile, null);
            }
        }
        return sizeBytes + NativeLibraryHelper.sumNativeBinariesWithOverride(handle, abiOverride);
    }

    public static String replaceEnd(String str, String before, String after) {
        if (str.endsWith(before)) {
            return str.substring(0, str.length() - before.length()) + after;
        }
        throw new IllegalArgumentException("Expected " + str + " to end with " + before);
    }

    public static boolean isExternalStorageMounted(Context context) {
        StorageManager sm = StorageManager.from(context);
        if (sm == null) {
            Slog.e(TAG, "get storage service failed");
            return false;
        }
        if (DEBUG_SD_PATH) {
            Slog.d(TAG, "get storage service success");
        }
        StorageVolume[] storageVolumes = sm.getVolumeList();
        for (StorageVolume storageVolume : storageVolumes) {
            String subSystem = storageVolume.getSubSystem();
            if (DEBUG_SD_PATH) {
                Slog.d(TAG, "subSystem : " + subSystem);
            }
            if (subSystem != null && "sd".equals(subSystem)) {
                String state = sm.getVolumeState(storageVolume.getPath());
                if (DEBUG_SD_PATH) {
                    Slog.d(TAG, "sd state : " + state);
                }
                if ("mounted".equals(state)) {
                    if (DEBUG_SD_PATH) {
                        Slog.d(TAG, "It is mounted(" + state + ")");
                    }
                    if (storageVolume.getActivitySecureContainer()) {
                        if (DEBUG_SD_PATH) {
                            Slog.d(TAG, "It is mounted(" + state + ")");
                        }
                        return true;
                    }
                } else {
                    continue;
                }
            }
        }
        return false;
    }

    public static String getExternalStorageDirectory(Context context) {
        StorageManager sm = StorageManager.from(context);
        if (sm == null) {
            Slog.e(TAG, "get storage service failed");
            return null;
        }
        if (DEBUG_SD_PATH) {
            Slog.d(TAG, "get storage service success");
        }
        StorageVolume[] storageVolumes = sm.getVolumeList();
        String result = null;
        for (StorageVolume storageVolume : storageVolumes) {
            String subSystem = storageVolume.getSubSystem();
            if (DEBUG_SD_PATH) {
                Slog.d(TAG, "subSystem : " + subSystem);
            }
            if (subSystem != null && "sd".equals(subSystem)) {
                String state = sm.getVolumeState(storageVolume.getPath());
                if (DEBUG_SD_PATH) {
                    Slog.d(TAG, "sd state : " + state);
                }
                if ("mounted".equals(state)) {
                    if (DEBUG_SD_PATH) {
                        Slog.d(TAG, "It is mounted(" + state + ")");
                    }
                    if (storageVolume.getActivitySecureContainer()) {
                        if (DEBUG_SD_PATH) {
                            Slog.d(TAG, "It is mounted(" + state + ")");
                        }
                        result = storageVolume.getPath();
                    }
                }
            }
        }
        return result;
    }

    public static boolean hasActiveContainer(Context context) {
        StorageManager sm = StorageManager.from(context);
        if (sm == null) {
            Slog.e(TAG, "get storage service failed");
            return false;
        }
        if (DEBUG_SD_PATH) {
            Slog.d(TAG, "get storage service success");
        }
        for (StorageVolume storageVolume : sm.getVolumeList()) {
            if (storageVolume.getActivitySecureContainer()) {
                return true;
            }
        }
        return false;
    }
}
