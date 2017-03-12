package org.bouncycastle.crypto.tls;

import java.io.IOException;

public class DTLSTransport implements DatagramTransport {
    private final DTLSRecordLayer recordLayer;

    DTLSTransport(DTLSRecordLayer dTLSRecordLayer) {
        this.recordLayer = dTLSRecordLayer;
    }

    public void close() {
        this.recordLayer.close();
    }

    public int getReceiveLimit() {
        return this.recordLayer.getReceiveLimit();
    }

    public int getSendLimit() {
        return this.recordLayer.getSendLimit();
    }

    public int receive(byte[] bArr, int i, int i2, int i3) {
        try {
            return this.recordLayer.receive(bArr, i, i2, i3);
        } catch (TlsFatalAlert e) {
            this.recordLayer.fail(e.getAlertDescription());
            throw e;
        } catch (IOException e2) {
            this.recordLayer.fail((short) 80);
            throw e2;
        } catch (Throwable e3) {
            this.recordLayer.fail((short) 80);
            throw new TlsFatalAlert((short) 80, e3);
        }
    }

    public void send(byte[] bArr, int i, int i2) {
        try {
            this.recordLayer.send(bArr, i, i2);
        } catch (TlsFatalAlert e) {
            this.recordLayer.fail(e.getAlertDescription());
            throw e;
        } catch (IOException e2) {
            this.recordLayer.fail((short) 80);
            throw e2;
        } catch (Throwable e3) {
            this.recordLayer.fail((short) 80);
            throw new TlsFatalAlert((short) 80, e3);
        }
    }
}
