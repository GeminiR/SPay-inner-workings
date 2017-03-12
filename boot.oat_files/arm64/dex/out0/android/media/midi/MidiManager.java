package android.media.midi;

import android.bluetooth.BluetoothDevice;
import android.media.midi.IMidiDeviceListener.Stub;
import android.media.midi.MidiDeviceServer.Callback;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import java.util.concurrent.ConcurrentHashMap;

public final class MidiManager {
    public static final String BLUETOOTH_MIDI_SERVICE_CLASS = "com.android.bluetoothmidiservice.BluetoothMidiService";
    public static final String BLUETOOTH_MIDI_SERVICE_INTENT = "android.media.midi.BluetoothMidiService";
    public static final String BLUETOOTH_MIDI_SERVICE_PACKAGE = "com.android.bluetoothmidiservice";
    private static final String TAG = "MidiManager";
    private ConcurrentHashMap<DeviceCallback, DeviceListener> mDeviceListeners = new ConcurrentHashMap();
    private final IMidiManager mService;
    private final IBinder mToken = new Binder();

    public static class DeviceCallback {
        public void onDeviceAdded(MidiDeviceInfo device) {
        }

        public void onDeviceRemoved(MidiDeviceInfo device) {
        }

        public void onDeviceStatusChanged(MidiDeviceStatus status) {
        }
    }

    private class DeviceListener extends Stub {
        private final DeviceCallback mCallback;
        private final Handler mHandler;

        public DeviceListener(DeviceCallback callback, Handler handler) {
            this.mCallback = callback;
            this.mHandler = handler;
        }

        public void onDeviceAdded(MidiDeviceInfo device) {
            if (this.mHandler != null) {
                final MidiDeviceInfo deviceF = device;
                this.mHandler.post(new Runnable() {
                    public void run() {
                        DeviceListener.this.mCallback.onDeviceAdded(deviceF);
                    }
                });
                return;
            }
            this.mCallback.onDeviceAdded(device);
        }

        public void onDeviceRemoved(MidiDeviceInfo device) {
            if (this.mHandler != null) {
                final MidiDeviceInfo deviceF = device;
                this.mHandler.post(new Runnable() {
                    public void run() {
                        DeviceListener.this.mCallback.onDeviceRemoved(deviceF);
                    }
                });
                return;
            }
            this.mCallback.onDeviceRemoved(device);
        }

        public void onDeviceStatusChanged(MidiDeviceStatus status) {
            if (this.mHandler != null) {
                final MidiDeviceStatus statusF = status;
                this.mHandler.post(new Runnable() {
                    public void run() {
                        DeviceListener.this.mCallback.onDeviceStatusChanged(statusF);
                    }
                });
                return;
            }
            this.mCallback.onDeviceStatusChanged(status);
        }
    }

    public interface OnDeviceOpenedListener {
        void onDeviceOpened(MidiDevice midiDevice);
    }

    public MidiManager(IMidiManager service) {
        this.mService = service;
    }

    public void registerDeviceCallback(DeviceCallback callback, Handler handler) {
        DeviceListener deviceListener = new DeviceListener(callback, handler);
        try {
            this.mService.registerListener(this.mToken, deviceListener);
            this.mDeviceListeners.put(callback, deviceListener);
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in registerDeviceListener");
        }
    }

    public void unregisterDeviceCallback(DeviceCallback callback) {
        DeviceListener deviceListener = (DeviceListener) this.mDeviceListeners.remove(callback);
        if (deviceListener != null) {
            try {
                this.mService.unregisterListener(this.mToken, deviceListener);
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in unregisterDeviceListener");
            }
        }
    }

    public MidiDeviceInfo[] getDevices() {
        try {
            return this.mService.getDevices();
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in getDevices");
            return new MidiDeviceInfo[0];
        }
    }

    private void sendOpenDeviceResponse(final MidiDevice device, final OnDeviceOpenedListener listener, Handler handler) {
        if (handler != null) {
            handler.post(new Runnable() {
                public void run() {
                    listener.onDeviceOpened(device);
                }
            });
        } else {
            listener.onDeviceOpened(device);
        }
    }

    public void openDevice(MidiDeviceInfo deviceInfo, OnDeviceOpenedListener listener, Handler handler) {
        final MidiDeviceInfo deviceInfoF = deviceInfo;
        final OnDeviceOpenedListener listenerF = listener;
        final Handler handlerF = handler;
        try {
            this.mService.openDevice(this.mToken, deviceInfo, new IMidiDeviceOpenCallback.Stub() {
                public void onDeviceOpened(IMidiDeviceServer server, IBinder deviceToken) {
                    MidiDevice device;
                    if (server != null) {
                        device = new MidiDevice(deviceInfoF, server, MidiManager.this.mService, MidiManager.this.mToken, deviceToken);
                    } else {
                        device = null;
                    }
                    MidiManager.this.sendOpenDeviceResponse(device, listenerF, handlerF);
                }
            });
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in openDevice");
        }
    }

    public void openBluetoothDevice(BluetoothDevice bluetoothDevice, OnDeviceOpenedListener listener, Handler handler) {
        final OnDeviceOpenedListener listenerF = listener;
        final Handler handlerF = handler;
        try {
            this.mService.openBluetoothDevice(this.mToken, bluetoothDevice, new IMidiDeviceOpenCallback.Stub() {
                public void onDeviceOpened(IMidiDeviceServer server, IBinder deviceToken) {
                    MidiDevice device;
                    if (server != null) {
                        try {
                            device = new MidiDevice(server.getDeviceInfo(), server, MidiManager.this.mService, MidiManager.this.mToken, deviceToken);
                        } catch (RemoteException e) {
                            Log.e(MidiManager.TAG, "remote exception in getDeviceInfo()");
                        }
                        MidiManager.this.sendOpenDeviceResponse(device, listenerF, handlerF);
                    }
                    device = null;
                    MidiManager.this.sendOpenDeviceResponse(device, listenerF, handlerF);
                }
            });
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in openDevice");
        }
    }

    public MidiDeviceServer createDeviceServer(MidiReceiver[] inputPortReceivers, int numOutputPorts, String[] inputPortNames, String[] outputPortNames, Bundle properties, int type, Callback callback) {
        try {
            MidiDeviceServer server = new MidiDeviceServer(this.mService, inputPortReceivers, numOutputPorts, callback);
            if (this.mService.registerDeviceServer(server.getBinderInterface(), inputPortReceivers.length, numOutputPorts, inputPortNames, outputPortNames, properties, type) != null) {
                return server;
            }
            Log.e(TAG, "registerVirtualDevice failed");
            return null;
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in createVirtualDevice");
            return null;
        }
    }
}
