package org.bouncycastle.crypto.paddings;

import java.security.SecureRandom;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class PKCS7Padding implements BlockCipherPadding {
    public int addPadding(byte[] bArr, int i) {
        byte length = (byte) (bArr.length - i);
        while (i < bArr.length) {
            bArr[i] = length;
            i++;
        }
        return length;
    }

    public String getPaddingName() {
        return "PKCS7";
    }

    public void init(SecureRandom secureRandom) {
    }

    public int padCount(byte[] bArr) {
        int i = bArr[bArr.length - 1] & GF2Field.MASK;
        byte b = (byte) i;
        int i2 = (i == 0 ? 1 : 0) | (i > bArr.length ? 1 : 0);
        for (int i3 = 0; i3 < bArr.length; i3++) {
            i2 |= (bArr.length - i3 <= i ? 1 : 0) & (bArr[i3] != b ? 1 : 0);
        }
        if (i2 == 0) {
            return i;
        }
        throw new InvalidCipherTextException("pad block corrupted");
    }
}
