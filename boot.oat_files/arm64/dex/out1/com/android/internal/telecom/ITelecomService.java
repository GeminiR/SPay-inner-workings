package com.android.internal.telecom;

import android.content.ComponentName;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import java.util.List;

public interface ITelecomService extends IInterface {

    public static abstract class Stub extends Binder implements ITelecomService {
        private static final String DESCRIPTOR = "com.android.internal.telecom.ITelecomService";
        static final int TRANSACTION_acceptRingingCall = 28;
        static final int TRANSACTION_addNewIncomingCall = 35;
        static final int TRANSACTION_addNewUnknownCall = 36;
        static final int TRANSACTION_cancelMissedCallsNotification = 29;
        static final int TRANSACTION_clearAccounts = 16;
        static final int TRANSACTION_enablePhoneAccount = 38;
        static final int TRANSACTION_endCall = 27;
        static final int TRANSACTION_getAdnUriForPhoneAccount = 32;
        static final int TRANSACTION_getAllPhoneAccountHandles = 11;
        static final int TRANSACTION_getAllPhoneAccounts = 10;
        static final int TRANSACTION_getAllPhoneAccountsCount = 9;
        static final int TRANSACTION_getCallCapablePhoneAccounts = 5;
        static final int TRANSACTION_getCallState = 26;
        static final int TRANSACTION_getCurrentTtyMode = 34;
        static final int TRANSACTION_getDefaultDialerPackage = 21;
        static final int TRANSACTION_getDefaultOutgoingPhoneAccount = 2;
        static final int TRANSACTION_getDefaultPhoneApp = 20;
        static final int TRANSACTION_getLine1Number = 19;
        static final int TRANSACTION_getPhoneAccount = 8;
        static final int TRANSACTION_getPhoneAccountsForPackage = 7;
        static final int TRANSACTION_getPhoneAccountsSupportingScheme = 6;
        static final int TRANSACTION_getSimCallManager = 12;
        static final int TRANSACTION_getSimCallManagerForUser = 13;
        static final int TRANSACTION_getSystemDialerPackage = 22;
        static final int TRANSACTION_getUserSelectedOutgoingPhoneAccount = 3;
        static final int TRANSACTION_getVoiceMailNumber = 18;
        static final int TRANSACTION_handlePinMmi = 30;
        static final int TRANSACTION_handlePinMmiForPhoneAccount = 31;
        static final int TRANSACTION_isInCall = 24;
        static final int TRANSACTION_isRinging = 25;
        static final int TRANSACTION_isTtySupported = 33;
        static final int TRANSACTION_isVoiceMailNumber = 17;
        static final int TRANSACTION_placeCall = 37;
        static final int TRANSACTION_registerPhoneAccount = 14;
        static final int TRANSACTION_setDefaultDialer = 39;
        static final int TRANSACTION_setUserSelectedOutgoingPhoneAccount = 4;
        static final int TRANSACTION_showInCallScreen = 1;
        static final int TRANSACTION_silenceRinger = 23;
        static final int TRANSACTION_unregisterPhoneAccount = 15;

        private static class Proxy implements ITelecomService {
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

            public void showInCallScreen(boolean showDialpad, String callingPackage) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!showDialpad) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public PhoneAccountHandle getDefaultOutgoingPhoneAccount(String uriScheme, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    PhoneAccountHandle _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(uriScheme);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(_reply);
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

            public PhoneAccountHandle getUserSelectedOutgoingPhoneAccount() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    PhoneAccountHandle _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(_reply);
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

            public void setUserSelectedOutgoingPhoneAccount(PhoneAccountHandle account) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<PhoneAccountHandle> getCallCapablePhoneAccounts(boolean includeDisabledAccounts, String callingPackage) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (includeDisabledAccounts) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    List<PhoneAccountHandle> _result = _reply.createTypedArrayList(PhoneAccountHandle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<PhoneAccountHandle> getPhoneAccountsSupportingScheme(String uriScheme, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(uriScheme);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    List<PhoneAccountHandle> _result = _reply.createTypedArrayList(PhoneAccountHandle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<PhoneAccountHandle> getPhoneAccountsForPackage(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    List<PhoneAccountHandle> _result = _reply.createTypedArrayList(PhoneAccountHandle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public PhoneAccount getPhoneAccount(PhoneAccountHandle account) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    PhoneAccount _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (PhoneAccount) PhoneAccount.CREATOR.createFromParcel(_reply);
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

            public int getAllPhoneAccountsCount() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<PhoneAccount> getAllPhoneAccounts() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    List<PhoneAccount> _result = _reply.createTypedArrayList(PhoneAccount.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<PhoneAccountHandle> getAllPhoneAccountHandles() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    List<PhoneAccountHandle> _result = _reply.createTypedArrayList(PhoneAccountHandle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public PhoneAccountHandle getSimCallManager() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    PhoneAccountHandle _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(_reply);
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

            public PhoneAccountHandle getSimCallManagerForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    PhoneAccountHandle _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(_reply);
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

            public void registerPhoneAccount(PhoneAccount metadata) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (metadata != null) {
                        _data.writeInt(1);
                        metadata.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterPhoneAccount(PhoneAccountHandle account) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearAccounts(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isVoiceMailNumber(PhoneAccountHandle accountHandle, String number, String callingPackage) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (accountHandle != null) {
                        _data.writeInt(1);
                        accountHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(number);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getVoiceMailNumber(PhoneAccountHandle accountHandle, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (accountHandle != null) {
                        _data.writeInt(1);
                        accountHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getLine1Number(PhoneAccountHandle accountHandle, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (accountHandle != null) {
                        _data.writeInt(1);
                        accountHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ComponentName getDefaultPhoneApp() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    ComponentName _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (ComponentName) ComponentName.CREATOR.createFromParcel(_reply);
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

            public String getDefaultDialerPackage() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getSystemDialerPackage() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void silenceRinger(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isInCall(String callingPackage) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isRinging(String callingPackage) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCallState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean endCall() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void acceptRingingCall() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void cancelMissedCallsNotification(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean handlePinMmi(String dialString, String callingPackage) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(dialString);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean handlePinMmiForPhoneAccount(PhoneAccountHandle accountHandle, String dialString, String callingPackage) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (accountHandle != null) {
                        _data.writeInt(1);
                        accountHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(dialString);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Uri getAdnUriForPhoneAccount(PhoneAccountHandle accountHandle, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    Uri _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (accountHandle != null) {
                        _data.writeInt(1);
                        accountHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Uri) Uri.CREATOR.createFromParcel(_reply);
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

            public boolean isTtySupported(String callingPackage) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCurrentTtyMode(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addNewIncomingCall(PhoneAccountHandle phoneAccount, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (phoneAccount != null) {
                        _data.writeInt(1);
                        phoneAccount.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addNewUnknownCall(PhoneAccountHandle phoneAccount, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (phoneAccount != null) {
                        _data.writeInt(1);
                        phoneAccount.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void placeCall(Uri handle, Bundle extras, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (handle != null) {
                        _data.writeInt(1);
                        handle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean enablePhoneAccount(PhoneAccountHandle accountHandle, boolean isEnabled) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (accountHandle != null) {
                        _data.writeInt(1);
                        accountHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(isEnabled ? 1 : 0);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setDefaultDialer(String packageName) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ITelecomService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ITelecomService)) {
                return new Proxy(obj);
            }
            return (ITelecomService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = 0;
            boolean _arg0;
            PhoneAccountHandle _result;
            PhoneAccountHandle _arg02;
            List<PhoneAccountHandle> _result2;
            int _result3;
            boolean _result4;
            String _result5;
            Bundle _arg1;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = true;
                    } else {
                        _arg0 = false;
                    }
                    showInCallScreen(_arg0, data.readString());
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getDefaultOutgoingPhoneAccount(data.readString(), data.readString());
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getUserSelectedOutgoingPhoneAccount();
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    setUserSelectedOutgoingPhoneAccount(_arg02);
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = true;
                    } else {
                        _arg0 = false;
                    }
                    _result2 = getCallCapablePhoneAccounts(_arg0, data.readString());
                    reply.writeNoException();
                    reply.writeTypedList(_result2);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getPhoneAccountsSupportingScheme(data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeTypedList(_result2);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getPhoneAccountsForPackage(data.readString());
                    reply.writeNoException();
                    reply.writeTypedList(_result2);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    PhoneAccount _result6 = getPhoneAccount(_arg02);
                    reply.writeNoException();
                    if (_result6 != null) {
                        reply.writeInt(1);
                        _result6.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getAllPhoneAccountsCount();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    List<PhoneAccount> _result7 = getAllPhoneAccounts();
                    reply.writeNoException();
                    reply.writeTypedList(_result7);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getAllPhoneAccountHandles();
                    reply.writeNoException();
                    reply.writeTypedList(_result2);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getSimCallManager();
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getSimCallManagerForUser(data.readInt());
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 14:
                    PhoneAccount _arg03;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = (PhoneAccount) PhoneAccount.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    registerPhoneAccount(_arg03);
                    reply.writeNoException();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    unregisterPhoneAccount(_arg02);
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    clearAccounts(data.readString());
                    reply.writeNoException();
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    _result4 = isVoiceMailNumber(_arg02, data.readString(), data.readString());
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    _result5 = getVoiceMailNumber(_arg02, data.readString());
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    _result5 = getLine1Number(_arg02, data.readString());
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _result8 = getDefaultPhoneApp();
                    reply.writeNoException();
                    if (_result8 != null) {
                        reply.writeInt(1);
                        _result8.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getDefaultDialerPackage();
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    _result5 = getSystemDialerPackage();
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    silenceRinger(data.readString());
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = isInCall(data.readString());
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = isRinging(data.readString());
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getCallState();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = endCall();
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    acceptRingingCall();
                    reply.writeNoException();
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    cancelMissedCallsNotification(data.readString());
                    reply.writeNoException();
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = handlePinMmi(data.readString(), data.readString());
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    _result4 = handlePinMmiForPhoneAccount(_arg02, data.readString(), data.readString());
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    Uri _result9 = getAdnUriForPhoneAccount(_arg02, data.readString());
                    reply.writeNoException();
                    if (_result9 != null) {
                        reply.writeInt(1);
                        _result9.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = isTtySupported(data.readString());
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getCurrentTtyMode(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg1 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    addNewIncomingCall(_arg02, _arg1);
                    reply.writeNoException();
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg1 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    addNewUnknownCall(_arg02, _arg1);
                    reply.writeNoException();
                    return true;
                case 37:
                    Uri _arg04;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = (Uri) Uri.CREATOR.createFromParcel(data);
                    } else {
                        _arg04 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg1 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    placeCall(_arg04, _arg1, data.readString());
                    reply.writeNoException();
                    return true;
                case 38:
                    boolean _arg12;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg12 = true;
                    } else {
                        _arg12 = false;
                    }
                    _result4 = enablePhoneAccount(_arg02, _arg12);
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = setDefaultDialer(data.readString());
                    reply.writeNoException();
                    if (_result4) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void acceptRingingCall() throws RemoteException;

    void addNewIncomingCall(PhoneAccountHandle phoneAccountHandle, Bundle bundle) throws RemoteException;

    void addNewUnknownCall(PhoneAccountHandle phoneAccountHandle, Bundle bundle) throws RemoteException;

    void cancelMissedCallsNotification(String str) throws RemoteException;

    void clearAccounts(String str) throws RemoteException;

    boolean enablePhoneAccount(PhoneAccountHandle phoneAccountHandle, boolean z) throws RemoteException;

    boolean endCall() throws RemoteException;

    Uri getAdnUriForPhoneAccount(PhoneAccountHandle phoneAccountHandle, String str) throws RemoteException;

    List<PhoneAccountHandle> getAllPhoneAccountHandles() throws RemoteException;

    List<PhoneAccount> getAllPhoneAccounts() throws RemoteException;

    int getAllPhoneAccountsCount() throws RemoteException;

    List<PhoneAccountHandle> getCallCapablePhoneAccounts(boolean z, String str) throws RemoteException;

    int getCallState() throws RemoteException;

    int getCurrentTtyMode(String str) throws RemoteException;

    String getDefaultDialerPackage() throws RemoteException;

    PhoneAccountHandle getDefaultOutgoingPhoneAccount(String str, String str2) throws RemoteException;

    ComponentName getDefaultPhoneApp() throws RemoteException;

    String getLine1Number(PhoneAccountHandle phoneAccountHandle, String str) throws RemoteException;

    PhoneAccount getPhoneAccount(PhoneAccountHandle phoneAccountHandle) throws RemoteException;

    List<PhoneAccountHandle> getPhoneAccountsForPackage(String str) throws RemoteException;

    List<PhoneAccountHandle> getPhoneAccountsSupportingScheme(String str, String str2) throws RemoteException;

    PhoneAccountHandle getSimCallManager() throws RemoteException;

    PhoneAccountHandle getSimCallManagerForUser(int i) throws RemoteException;

    String getSystemDialerPackage() throws RemoteException;

    PhoneAccountHandle getUserSelectedOutgoingPhoneAccount() throws RemoteException;

    String getVoiceMailNumber(PhoneAccountHandle phoneAccountHandle, String str) throws RemoteException;

    boolean handlePinMmi(String str, String str2) throws RemoteException;

    boolean handlePinMmiForPhoneAccount(PhoneAccountHandle phoneAccountHandle, String str, String str2) throws RemoteException;

    boolean isInCall(String str) throws RemoteException;

    boolean isRinging(String str) throws RemoteException;

    boolean isTtySupported(String str) throws RemoteException;

    boolean isVoiceMailNumber(PhoneAccountHandle phoneAccountHandle, String str, String str2) throws RemoteException;

    void placeCall(Uri uri, Bundle bundle, String str) throws RemoteException;

    void registerPhoneAccount(PhoneAccount phoneAccount) throws RemoteException;

    boolean setDefaultDialer(String str) throws RemoteException;

    void setUserSelectedOutgoingPhoneAccount(PhoneAccountHandle phoneAccountHandle) throws RemoteException;

    void showInCallScreen(boolean z, String str) throws RemoteException;

    void silenceRinger(String str) throws RemoteException;

    void unregisterPhoneAccount(PhoneAccountHandle phoneAccountHandle) throws RemoteException;
}
