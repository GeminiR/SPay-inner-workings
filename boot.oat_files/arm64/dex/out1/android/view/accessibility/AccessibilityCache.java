package android.view.accessibility;

import android.os.Build;
import android.util.ArraySet;
import android.util.Log;
import android.util.LongArray;
import android.util.LongSparseArray;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.List;

final class AccessibilityCache {
    private static final boolean CHECK_INTEGRITY = "eng".equals(Build.TYPE);
    private static final boolean DEBUG = false;
    private static final String LOG_TAG = "AccessibilityCache";
    private long mAccessibilityFocus = 2147483647L;
    private long mInputFocus = 2147483647L;
    private final Object mLock = new Object();
    private final SparseArray<LongSparseArray<AccessibilityNodeInfo>> mNodeCache = new SparseArray();
    private final SparseArray<AccessibilityWindowInfo> mTempWindowArray = new SparseArray();
    private final SparseArray<AccessibilityWindowInfo> mWindowCache = new SparseArray();

    AccessibilityCache() {
    }

    public void addWindow(AccessibilityWindowInfo window) {
        synchronized (this.mLock) {
            int windowId = window.getId();
            AccessibilityWindowInfo oldWindow = (AccessibilityWindowInfo) this.mWindowCache.get(windowId);
            if (oldWindow != null) {
                oldWindow.recycle();
            }
            this.mWindowCache.put(windowId, AccessibilityWindowInfo.obtain(window));
        }
    }

    public void onAccessibilityEvent(AccessibilityEvent event) {
        synchronized (this.mLock) {
            switch (event.getEventType()) {
                case 1:
                case 4:
                case 16:
                case 8192:
                    refreshCachedNodeLocked(event.getWindowId(), event.getSourceNodeId());
                    break;
                case 8:
                    if (this.mInputFocus != 2147483647L) {
                        refreshCachedNodeLocked(event.getWindowId(), this.mInputFocus);
                    }
                    this.mInputFocus = event.getSourceNodeId();
                    refreshCachedNodeLocked(event.getWindowId(), this.mInputFocus);
                    break;
                case 32:
                case 4194304:
                    clear();
                    break;
                case 2048:
                    synchronized (this.mLock) {
                        int windowId = event.getWindowId();
                        long sourceId = event.getSourceNodeId();
                        if ((event.getContentChangeTypes() & 1) != 0) {
                            clearSubTreeLocked(windowId, sourceId);
                        } else {
                            refreshCachedNodeLocked(windowId, sourceId);
                        }
                    }
                    break;
                case 4096:
                    clearSubTreeLocked(event.getWindowId(), event.getSourceNodeId());
                    break;
                case 32768:
                    if (this.mAccessibilityFocus != 2147483647L) {
                        refreshCachedNodeLocked(event.getWindowId(), this.mAccessibilityFocus);
                    }
                    this.mAccessibilityFocus = event.getSourceNodeId();
                    refreshCachedNodeLocked(event.getWindowId(), this.mAccessibilityFocus);
                    break;
                case 65536:
                    if (this.mAccessibilityFocus == event.getSourceNodeId()) {
                        refreshCachedNodeLocked(event.getWindowId(), this.mAccessibilityFocus);
                        this.mAccessibilityFocus = 2147483647L;
                        break;
                    }
                    break;
            }
        }
        if (CHECK_INTEGRITY) {
            checkIntegrity();
        }
    }

    private void refreshCachedNodeLocked(int windowId, long sourceId) {
        LongSparseArray<AccessibilityNodeInfo> nodes = (LongSparseArray) this.mNodeCache.get(windowId);
        if (nodes != null) {
            AccessibilityNodeInfo cachedInfo = (AccessibilityNodeInfo) nodes.get(sourceId);
            if (cachedInfo != null && !cachedInfo.refresh(true)) {
                clearSubTreeLocked(windowId, sourceId);
            }
        }
    }

    public AccessibilityNodeInfo getNode(int windowId, long accessibilityNodeId) {
        AccessibilityNodeInfo accessibilityNodeInfo;
        synchronized (this.mLock) {
            LongSparseArray<AccessibilityNodeInfo> nodes = (LongSparseArray) this.mNodeCache.get(windowId);
            if (nodes == null) {
                accessibilityNodeInfo = null;
            } else {
                accessibilityNodeInfo = (AccessibilityNodeInfo) nodes.get(accessibilityNodeId);
                if (accessibilityNodeInfo != null) {
                    accessibilityNodeInfo = AccessibilityNodeInfo.obtain(accessibilityNodeInfo);
                }
            }
        }
        return accessibilityNodeInfo;
    }

    public List<AccessibilityWindowInfo> getWindows() {
        List<AccessibilityWindowInfo> windows;
        synchronized (this.mLock) {
            int windowCount = this.mWindowCache.size();
            if (windowCount > 0) {
                int i;
                SparseArray<AccessibilityWindowInfo> sortedWindows = this.mTempWindowArray;
                sortedWindows.clear();
                for (i = 0; i < windowCount; i++) {
                    AccessibilityWindowInfo window = (AccessibilityWindowInfo) this.mWindowCache.valueAt(i);
                    sortedWindows.put(window.getLayer(), window);
                }
                windows = new ArrayList(windowCount);
                for (i = windowCount - 1; i >= 0; i--) {
                    windows.add(AccessibilityWindowInfo.obtain((AccessibilityWindowInfo) sortedWindows.valueAt(i)));
                    sortedWindows.removeAt(i);
                }
            } else {
                windows = null;
            }
        }
        return windows;
    }

    public AccessibilityWindowInfo getWindow(int windowId) {
        AccessibilityWindowInfo obtain;
        synchronized (this.mLock) {
            AccessibilityWindowInfo window = (AccessibilityWindowInfo) this.mWindowCache.get(windowId);
            if (window != null) {
                obtain = AccessibilityWindowInfo.obtain(window);
            } else {
                obtain = null;
            }
        }
        return obtain;
    }

    public void add(AccessibilityNodeInfo info) {
        synchronized (this.mLock) {
            int windowId = info.getWindowId();
            LongSparseArray<AccessibilityNodeInfo> nodes = (LongSparseArray) this.mNodeCache.get(windowId);
            if (nodes == null) {
                nodes = new LongSparseArray();
                this.mNodeCache.put(windowId, nodes);
            }
            long sourceId = info.getSourceNodeId();
            AccessibilityNodeInfo oldInfo = (AccessibilityNodeInfo) nodes.get(sourceId);
            if (oldInfo != null) {
                LongArray newChildrenIds = info.getChildNodeIds();
                int oldChildCount = oldInfo.getChildCount();
                for (int i = 0; i < oldChildCount; i++) {
                    long oldChildId = oldInfo.getChildId(i);
                    if (newChildrenIds == null || newChildrenIds.indexOf(oldChildId) < 0) {
                        clearSubTreeLocked(windowId, oldChildId);
                    }
                }
                long oldParentId = oldInfo.getParentNodeId();
                if (info.getParentNodeId() != oldParentId) {
                    clearSubTreeLocked(windowId, oldParentId);
                }
            }
            nodes.put(sourceId, AccessibilityNodeInfo.obtain(info));
        }
    }

    public void clear() {
        synchronized (this.mLock) {
            int i;
            for (i = this.mWindowCache.size() - 1; i >= 0; i--) {
                ((AccessibilityWindowInfo) this.mWindowCache.valueAt(i)).recycle();
                this.mWindowCache.removeAt(i);
            }
            int nodesForWindowCount = this.mNodeCache.size();
            for (i = 0; i < nodesForWindowCount; i++) {
                clearNodesForWindowLocked(this.mNodeCache.keyAt(i));
            }
            this.mAccessibilityFocus = 2147483647L;
            this.mInputFocus = 2147483647L;
        }
    }

    private void clearNodesForWindowLocked(int windowId) {
        LongSparseArray<AccessibilityNodeInfo> nodes = (LongSparseArray) this.mNodeCache.get(windowId);
        if (nodes != null) {
            for (int i = nodes.size() - 1; i >= 0; i--) {
                AccessibilityNodeInfo info = (AccessibilityNodeInfo) nodes.valueAt(i);
                nodes.removeAt(i);
                info.recycle();
            }
            this.mNodeCache.remove(windowId);
        }
    }

    private void clearSubTreeLocked(int windowId, long rootNodeId) {
        LongSparseArray<AccessibilityNodeInfo> nodes = (LongSparseArray) this.mNodeCache.get(windowId);
        if (nodes != null) {
            clearSubTreeRecursiveLocked(nodes, rootNodeId);
        }
    }

    private void clearSubTreeRecursiveLocked(LongSparseArray<AccessibilityNodeInfo> nodes, long rootNodeId) {
        AccessibilityNodeInfo current = (AccessibilityNodeInfo) nodes.get(rootNodeId);
        if (current != null) {
            nodes.remove(rootNodeId);
            int childCount = current.getChildCount();
            for (int i = 0; i < childCount; i++) {
                clearSubTreeRecursiveLocked(nodes, current.getChildId(i));
            }
        }
    }

    public void checkIntegrity() {
        synchronized (this.mLock) {
            if (this.mWindowCache.size() > 0 || this.mNodeCache.size() != 0) {
                int i;
                AccessibilityWindowInfo focusedWindow = null;
                AccessibilityWindowInfo activeWindow = null;
                int windowCount = this.mWindowCache.size();
                for (i = 0; i < windowCount; i++) {
                    AccessibilityWindowInfo window = (AccessibilityWindowInfo) this.mWindowCache.valueAt(i);
                    if (window.isActive()) {
                        if (activeWindow != null) {
                            Log.e(LOG_TAG, "Duplicate active window:" + window);
                        } else {
                            activeWindow = window;
                        }
                    }
                    if (window.isFocused()) {
                        if (focusedWindow != null) {
                            Log.e(LOG_TAG, "Duplicate focused window:" + window);
                        } else {
                            focusedWindow = window;
                        }
                    }
                }
                AccessibilityNodeInfo accessFocus = null;
                AccessibilityNodeInfo inputFocus = null;
                int nodesForWindowCount = this.mNodeCache.size();
                for (i = 0; i < nodesForWindowCount; i++) {
                    LongSparseArray<AccessibilityNodeInfo> nodes = (LongSparseArray) this.mNodeCache.valueAt(i);
                    if (nodes.size() > 0) {
                        ArraySet<AccessibilityNodeInfo> seen = new ArraySet();
                        int windowId = this.mNodeCache.keyAt(i);
                        int nodeCount = nodes.size();
                        for (int j = 0; j < nodeCount; j++) {
                            AccessibilityNodeInfo node = (AccessibilityNodeInfo) nodes.valueAt(j);
                            if (seen.add(node)) {
                                int childCount;
                                int k;
                                if (node.isAccessibilityFocused()) {
                                    if (accessFocus != null) {
                                        Log.e(LOG_TAG, "Duplicate accessibility focus:" + node + " in window:" + windowId);
                                    } else {
                                        accessFocus = node;
                                    }
                                }
                                if (node.isFocused()) {
                                    if (inputFocus != null) {
                                        Log.e(LOG_TAG, "Duplicate input focus: " + node + " in window:" + windowId);
                                    } else {
                                        inputFocus = node;
                                    }
                                }
                                AccessibilityNodeInfo nodeParent = (AccessibilityNodeInfo) nodes.get(node.getParentNodeId());
                                if (nodeParent != null) {
                                    boolean childOfItsParent = false;
                                    childCount = nodeParent.getChildCount();
                                    for (k = 0; k < childCount; k++) {
                                        if (((AccessibilityNodeInfo) nodes.get(nodeParent.getChildId(k))) == node) {
                                            childOfItsParent = true;
                                            break;
                                        }
                                    }
                                    if (!childOfItsParent) {
                                        Log.e(LOG_TAG, "Invalid parent-child relation between parent: " + nodeParent + " and child: " + node);
                                    }
                                }
                                childCount = node.getChildCount();
                                for (k = 0; k < childCount; k++) {
                                    AccessibilityNodeInfo child = (AccessibilityNodeInfo) nodes.get(node.getChildId(k));
                                    if (!(child == null || ((AccessibilityNodeInfo) nodes.get(child.getParentNodeId())) == node)) {
                                        Log.e(LOG_TAG, "Invalid child-parent relation between child: " + node + " and parent: " + nodeParent);
                                    }
                                }
                            } else {
                                Log.e(LOG_TAG, "Duplicate node: " + node + " in window:" + windowId);
                            }
                        }
                    }
                }
                return;
            }
        }
    }
}
