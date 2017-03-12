package com.android.internal.view;

import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.view.DragEvent;
import android.view.IWindow.Stub;
import android.view.IWindowSession;
import com.samsung.android.smartclip.SmartClipRemoteRequestInfo;

public class BaseIWindow extends Stub {
    public int mSeq;
    private IWindowSession mSession;

    public void setSession(IWindowSession session) {
        this.mSession = session;
    }

    public void resized(Rect frame, Rect overscanInsets, Rect contentInsets, Rect visibleInsets, Rect stableInsets, Rect outsets, boolean reportDraw, Configuration newConfig, Rect cocktailBarFrame) {
        if (reportDraw) {
            try {
                this.mSession.finishDrawing(this);
            } catch (RemoteException e) {
            }
        }
    }

    public void moved(int newX, int newY) {
    }

    public void dispatchAttachedDisplayChanged(int displayId) {
    }

    public void dispatchAppVisibility(boolean visible) {
    }

    public void dispatchGetNewSurface() {
    }

    public void windowFocusChanged(boolean hasFocus, boolean touchEnabled, boolean focusedAppChanged) {
    }

    public void executeCommand(String command, String parameters, ParcelFileDescriptor out) {
    }

    public void closeSystemDialogs(String reason) {
    }

    public void dispatchWallpaperOffsets(float x, float y, float xStep, float yStep, boolean sync) {
        if (sync) {
            try {
                this.mSession.wallpaperOffsetsComplete(asBinder());
            } catch (RemoteException e) {
            }
        }
    }

    public void dispatchDragEvent(DragEvent event) {
    }

    public void dispatchSystemUiVisibilityChanged(int seq, int globalUi, int localValue, int localChanges) {
        this.mSeq = seq;
    }

    public void dispatchWallpaperCommand(String action, int x, int y, int z, Bundle extras, boolean sync) {
        if (sync) {
            try {
                this.mSession.wallpaperCommandComplete(asBinder(), null);
            } catch (RemoteException e) {
            }
        }
    }

    public void onAnimationStarted(int remainingFrameCount) {
    }

    public void onAnimationStopped() {
    }

    public void dispatchWindowShown() {
    }

    public void dispatchSmartClipRemoteRequest(SmartClipRemoteRequestInfo request) {
    }

    public void dispatchCoverStateChanged(boolean isOpen) {
    }

    public void onSurfaceDestroyDeferred() {
    }

    public void dispatchMultiWindowStateChanged(int state) {
    }
}
