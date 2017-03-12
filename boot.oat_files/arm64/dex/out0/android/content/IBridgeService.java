package android.content;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface IBridgeService extends IInterface {

    public static abstract class Stub extends Binder implements IBridgeService {
        private static final String DESCRIPTOR = "android.content.IBridgeService";
        static final int TRANSACTION_queryAllProviders = 2;
        static final int TRANSACTION_queryProvider = 1;
        static final int TRANSACTION_registerProvider = 3;

        private static class Proxy implements IBridgeService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public CustomCursor queryProvider(String providerName, String resource, int containerId, String[] projection, String selection, String[] selectionArgs, String sortOrder) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    CustomCursor _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(providerName);
                    _data.writeString(resource);
                    _data.writeInt(containerId);
                    _data.writeStringArray(projection);
                    _data.writeString(selection);
                    _data.writeStringArray(selectionArgs);
                    _data.writeString(sortOrder);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (CustomCursor) CustomCursor.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<CustomCursor> queryAllProviders(String providerName, String resource, int containerId, String[] projection, String selection, String[] selectionArgs, String sortOrder) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(providerName);
                    _data.writeString(resource);
                    _data.writeInt(containerId);
                    _data.writeStringArray(projection);
                    _data.writeString(selection);
                    _data.writeStringArray(selectionArgs);
                    _data.writeString(sortOrder);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    List<CustomCursor> _result = _reply.createTypedArrayList(CustomCursor.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerProvider(String providerName, IProviderCallBack mProvider) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(providerName);
                    _data.writeStrongBinder(mProvider != null ? mProvider.asBinder() : null);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IBridgeService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IBridgeService)) {
                return new Proxy(obj);
            }
            return (IBridgeService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    CustomCursor _result = queryProvider(data.readString(), data.readString(), data.readInt(), data.createStringArray(), data.readString(), data.createStringArray(), data.readString());
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    List<CustomCursor> _result2 = queryAllProviders(data.readString(), data.readString(), data.readInt(), data.createStringArray(), data.readString(), data.createStringArray(), data.readString());
                    reply.writeNoException();
                    reply.writeTypedList(_result2);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    registerProvider(data.readString(), android.content.IProviderCallBack.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case IBinder.INTERFACE_TRANSACTION /*1598968902*/:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    List<CustomCursor> queryAllProviders(String str, String str2, int i, String[] strArr, String str3, String[] strArr2, String str4) throws RemoteException;

    CustomCursor queryProvider(String str, String str2, int i, String[] strArr, String str3, String[] strArr2, String str4) throws RemoteException;

    void registerProvider(String str, IProviderCallBack iProviderCallBack) throws RemoteException;
}
