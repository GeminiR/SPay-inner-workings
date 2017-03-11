package org.bouncycastle.crypto.tls;

public class HeartbeatMessageType {
    public static final short heartbeat_request = (short) 1;
    public static final short heartbeat_response = (short) 2;

    public static boolean isValid(short s) {
        return s >= heartbeat_request && s <= heartbeat_response;
    }
}
