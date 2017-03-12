package com.android.internal.policy;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.widget.RemoteViews;

public interface IKeyguardService extends IInterface {

    public static abstract class Stub extends Binder implements IKeyguardService {
        private static final String DESCRIPTOR = "com.android.internal.policy.IKeyguardService";
        static final int TRANSACTION_addStateMonitorCallback = 2;
        static final int TRANSACTION_changeLidState = 19;
        static final int TRANSACTION_dismiss = 5;
        static final int TRANSACTION_doKeyguardTimeout = 16;
        static final int TRANSACTION_getScreenOrientation = 27;
        static final int TRANSACTION_keyguardDone = 4;
        static final int TRANSACTION_onActivityDrawn = 21;
        static final int TRANSACTION_onBootCompleted = 18;
        static final int TRANSACTION_onDreamingStarted = 6;
        static final int TRANSACTION_onDreamingStopped = 7;
        static final int TRANSACTION_onFinishedGoingToSleep = 9;
        static final int TRANSACTION_onScreenTurnedOff = 13;
        static final int TRANSACTION_onScreenTurnedOn = 12;
        static final int TRANSACTION_onScreenTurningOn = 11;
        static final int TRANSACTION_onStartedGoingToSleep = 8;
        static final int TRANSACTION_onStartedWakingUp = 10;
        static final int TRANSACTION_onSystemReady = 15;
        static final int TRANSACTION_removeAdaptiveEvent = 23;
        static final int TRANSACTION_setAdaptiveEvent = 22;
        static final int TRANSACTION_setBendedPendingIntent = 25;
        static final int TRANSACTION_setBendedPendingIntentInSecure = 26;
        static final int TRANSACTION_setCurrentUser = 17;
        static final int TRANSACTION_setKeyguardEnabled = 14;
        static final int TRANSACTION_setOccluded = 1;
        static final int TRANSACTION_startKeyguardExitAnimation = 20;
        static final int TRANSACTION_updateAdaptiveEvent = 24;
        static final int TRANSACTION_verifyUnlock = 3;

        private static class Proxy implements IKeyguardService {
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

            public void setOccluded(boolean isOccluded, int displayId) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!isOccluded) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeInt(displayId);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void addStateMonitorCallback(IKeyguardStateCallback callback) throws RemoteException {
                IBinder iBinder = null;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (callback != null) {
                        iBinder = callback.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void verifyUnlock(IKeyguardExitCallback callback) throws RemoteException {
                IBinder iBinder = null;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (callback != null) {
                        iBinder = callback.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void keyguardDone(boolean authenticated, boolean wakeup) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(authenticated ? 1 : 0);
                    if (!wakeup) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void dismiss() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onDreamingStarted() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onDreamingStopped() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onStartedGoingToSleep(int reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(reason);
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onFinishedGoingToSleep(int reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(reason);
                    this.mRemote.transact(9, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onStartedWakingUp() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onScreenTurningOn(IKeyguardDrawnCallback callback) throws RemoteException {
                IBinder iBinder = null;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (callback != null) {
                        iBinder = callback.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    this.mRemote.transact(11, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onScreenTurnedOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onScreenTurnedOff() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(13, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void setKeyguardEnabled(boolean enabled) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!enabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(14, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onSystemReady() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(15, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void doKeyguardTimeout(Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(16, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void setCurrentUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(17, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onBootCompleted() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(18, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void changeLidState(boolean lidOpen) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!lidOpen) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(19, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void startKeyguardExitAnimation(long startTime, long fadeoutDuration) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(startTime);
                    _data.writeLong(fadeoutDuration);
                    this.mRemote.transact(20, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onActivityDrawn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(21, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void setAdaptiveEvent(String requestClass, RemoteViews smallView, RemoteViews bigView) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(requestClass);
                    if (smallView != null) {
                        _data.writeInt(1);
                        smallView.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (bigView != null) {
                        _data.writeInt(1);
                        bigView.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(22, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void removeAdaptiveEvent(String requestClass) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(requestClass);
                    this.mRemote.transact(23, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void updateAdaptiveEvent(String requestClass, RemoteViews smallView, RemoteViews bigView) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(requestClass);
                    if (smallView != null) {
                        _data.writeInt(1);
                        smallView.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (bigView != null) {
                        _data.writeInt(1);
                        bigView.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(24, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void setBendedPendingIntent(PendingIntent p, Intent fillInIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (p != null) {
                        _data.writeInt(1);
                        p.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (fillInIntent != null) {
                        _data.writeInt(1);
                        fillInIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(25, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void setBendedPendingIntentInSecure(PendingIntent p, Intent fillInIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (p != null) {
                        _data.writeInt(1);
                        p.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (fillInIntent != null) {
                        _data.writeInt(1);
                        fillInIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(26, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public int getScreenOrientation(boolean isOccluded) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (isOccluded) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IKeyguardService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IKeyguardService)) {
                return new Proxy(obj);
            }
            return (IKeyguardService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _arg0;
            String _arg02;
            RemoteViews _arg1;
            RemoteViews _arg2;
            PendingIntent _arg03;
            Intent _arg12;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = true;
                    } else {
                        _arg0 = false;
                    }
                    setOccluded(_arg0, data.readInt());
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    addStateMonitorCallback(com.android.internal.policy.IKeyguardStateCallback.Stub.asInterface(data.readStrongBinder()));
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    verifyUnlock(com.android.internal.policy.IKeyguardExitCallback.Stub.asInterface(data.readStrongBinder()));
                    return true;
                case 4:
                    boolean _arg13;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = true;
                    } else {
                        _arg0 = false;
                    }
                    if (data.readInt() != 0) {
                        _arg13 = true;
                    } else {
                        _arg13 = false;
                    }
                    keyguardDone(_arg0, _arg13);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    dismiss();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    onDreamingStarted();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    onDreamingStopped();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    onStartedGoingToSleep(data.readInt());
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    onFinishedGoingToSleep(data.readInt());
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    onStartedWakingUp();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    onScreenTurningOn(com.android.internal.policy.IKeyguardDrawnCallback.Stub.asInterface(data.readStrongBinder()));
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    onScreenTurnedOn();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    onScreenTurnedOff();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = true;
                    } else {
                        _arg0 = false;
                    }
                    setKeyguardEnabled(_arg0);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    onSystemReady();
                    return true;
                case 16:
                    Bundle _arg04;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg04 = null;
                    }
                    doKeyguardTimeout(_arg04);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    setCurrentUser(data.readInt());
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    onBootCompleted();
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = true;
                    } else {
                        _arg0 = false;
                    }
                    changeLidState(_arg0);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    startKeyguardExitAnimation(data.readLong(), data.readLong());
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    onActivityDrawn();
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readString();
                    if (data.readInt() != 0) {
                        _arg1 = (RemoteViews) RemoteViews.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg2 = (RemoteViews) RemoteViews.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    setAdaptiveEvent(_arg02, _arg1, _arg2);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    removeAdaptiveEvent(data.readString());
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readString();
                    if (data.readInt() != 0) {
                        _arg1 = (RemoteViews) RemoteViews.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg2 = (RemoteViews) RemoteViews.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    updateAdaptiveEvent(_arg02, _arg1, _arg2);
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg12 = (Intent) Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    setBendedPendingIntent(_arg03, _arg12);
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg12 = (Intent) Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    setBendedPendingIntentInSecure(_arg03, _arg12);
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = true;
                    } else {
                        _arg0 = false;
                    }
                    int _result = getScreenOrientation(_arg0);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void addStateMonitorCallback(IKeyguardStateCallback iKeyguardStateCallback) throws RemoteException;

    void changeLidState(boolean z) throws RemoteException;

    void dismiss() throws RemoteException;

    void doKeyguardTimeout(Bundle bundle) throws RemoteException;

    int getScreenOrientation(boolean z) throws RemoteException;

    void keyguardDone(boolean z, boolean z2) throws RemoteException;

    void onActivityDrawn() throws RemoteException;

    void onBootCompleted() throws RemoteException;

    void onDreamingStarted() throws RemoteException;

    void onDreamingStopped() throws RemoteException;

    void onFinishedGoingToSleep(int i) throws RemoteException;

    void onScreenTurnedOff() throws RemoteException;

    void onScreenTurnedOn() throws RemoteException;

    void onScreenTurningOn(IKeyguardDrawnCallback iKeyguardDrawnCallback) throws RemoteException;

    void onStartedGoingToSleep(int i) throws RemoteException;

    void onStartedWakingUp() throws RemoteException;

    void onSystemReady() throws RemoteException;

    void removeAdaptiveEvent(String str) throws RemoteException;

    void setAdaptiveEvent(String str, RemoteViews remoteViews, RemoteViews remoteViews2) throws RemoteException;

    void setBendedPendingIntent(PendingIntent pendingIntent, Intent intent) throws RemoteException;

    void setBendedPendingIntentInSecure(PendingIntent pendingIntent, Intent intent) throws RemoteException;

    void setCurrentUser(int i) throws RemoteException;

    void setKeyguardEnabled(boolean z) throws RemoteException;

    void setOccluded(boolean z, int i) throws RemoteException;

    void startKeyguardExitAnimation(long j, long j2) throws RemoteException;

    void updateAdaptiveEvent(String str, RemoteViews remoteViews, RemoteViews remoteViews2) throws RemoteException;

    void verifyUnlock(IKeyguardExitCallback iKeyguardExitCallback) throws RemoteException;
}
