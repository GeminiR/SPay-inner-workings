package org.bouncycastle.crypto.tls;

import java.io.InputStream;
import java.io.OutputStream;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.util.Strings;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class URLAndHash {
    protected byte[] sha1Hash;
    protected String url;

    public URLAndHash(String str, byte[] bArr) {
        if (str == null || str.length() < 1 || str.length() >= PKIFailureInfo.notAuthorized) {
            throw new IllegalArgumentException("'url' must have length from 1 to (2^16 - 1)");
        } else if (bArr == null || bArr.length == 20) {
            this.url = str;
            this.sha1Hash = bArr;
        } else {
            throw new IllegalArgumentException("'sha1Hash' must have length == 20, if present");
        }
    }

    public static URLAndHash parse(TlsContext tlsContext, InputStream inputStream) {
        byte[] readOpaque16 = TlsUtils.readOpaque16(inputStream);
        if (readOpaque16.length < 1) {
            throw new TlsFatalAlert((short) 47);
        }
        String fromByteArray = Strings.fromByteArray(readOpaque16);
        readOpaque16 = null;
        switch (TlsUtils.readUint8(inputStream)) {
            case ECCurve.COORD_AFFINE /*0*/:
                if (TlsUtils.isTLSv12(tlsContext)) {
                    throw new TlsFatalAlert((short) 47);
                }
                break;
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                readOpaque16 = TlsUtils.readFully(20, inputStream);
                break;
            default:
                throw new TlsFatalAlert((short) 47);
        }
        return new URLAndHash(fromByteArray, readOpaque16);
    }

    public void encode(OutputStream outputStream) {
        TlsUtils.writeOpaque16(Strings.toByteArray(this.url), outputStream);
        if (this.sha1Hash == null) {
            TlsUtils.writeUint8(0, outputStream);
            return;
        }
        TlsUtils.writeUint8(1, outputStream);
        outputStream.write(this.sha1Hash);
    }

    public byte[] getSHA1Hash() {
        return this.sha1Hash;
    }

    public String getURL() {
        return this.url;
    }
}
