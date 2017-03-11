package org.bouncycastle.pqc.crypto.gmss;

import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.pqc.crypto.gmss.util.GMSSRandom;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;

public class GMSSLeaf {
    private byte[] concHashs;
    private GMSSRandom gmssRandom;
    private int f396i;
    private int f397j;
    private int keysize;
    private byte[] leaf;
    private int mdsize;
    private Digest messDigestOTS;
    byte[] privateKeyOTS;
    private byte[] seed;
    private int steps;
    private int two_power_w;
    private int f398w;

    GMSSLeaf(Digest digest, int i, int i2) {
        this.f398w = i;
        this.messDigestOTS = digest;
        this.gmssRandom = new GMSSRandom(this.messDigestOTS);
        this.mdsize = this.messDigestOTS.getDigestSize();
        int ceil = (int) Math.ceil(((double) (this.mdsize << 3)) / ((double) i));
        this.keysize = ceil + ((int) Math.ceil(((double) getLog((ceil << i) + 1)) / ((double) i)));
        this.two_power_w = 1 << i;
        this.steps = (int) Math.ceil(((double) (((((1 << i) - 1) * this.keysize) + 1) + this.keysize)) / ((double) i2));
        this.seed = new byte[this.mdsize];
        this.leaf = new byte[this.mdsize];
        this.privateKeyOTS = new byte[this.mdsize];
        this.concHashs = new byte[(this.mdsize * this.keysize)];
    }

    public GMSSLeaf(Digest digest, int i, int i2, byte[] bArr) {
        this.f398w = i;
        this.messDigestOTS = digest;
        this.gmssRandom = new GMSSRandom(this.messDigestOTS);
        this.mdsize = this.messDigestOTS.getDigestSize();
        int ceil = (int) Math.ceil(((double) (this.mdsize << 3)) / ((double) i));
        this.keysize = ceil + ((int) Math.ceil(((double) getLog((ceil << i) + 1)) / ((double) i)));
        this.two_power_w = 1 << i;
        this.steps = (int) Math.ceil(((double) (((((1 << i) - 1) * this.keysize) + 1) + this.keysize)) / ((double) i2));
        this.seed = new byte[this.mdsize];
        this.leaf = new byte[this.mdsize];
        this.privateKeyOTS = new byte[this.mdsize];
        this.concHashs = new byte[(this.mdsize * this.keysize)];
        initLeafCalc(bArr);
    }

    public GMSSLeaf(Digest digest, byte[][] bArr, int[] iArr) {
        this.f396i = iArr[0];
        this.f397j = iArr[1];
        this.steps = iArr[2];
        this.f398w = iArr[3];
        this.messDigestOTS = digest;
        this.gmssRandom = new GMSSRandom(this.messDigestOTS);
        this.mdsize = this.messDigestOTS.getDigestSize();
        int ceil = (int) Math.ceil(((double) (this.mdsize << 3)) / ((double) this.f398w));
        this.keysize = ceil + ((int) Math.ceil(((double) getLog((ceil << this.f398w) + 1)) / ((double) this.f398w)));
        this.two_power_w = 1 << this.f398w;
        this.privateKeyOTS = bArr[0];
        this.seed = bArr[1];
        this.concHashs = bArr[2];
        this.leaf = bArr[3];
    }

    private GMSSLeaf(GMSSLeaf gMSSLeaf) {
        this.messDigestOTS = gMSSLeaf.messDigestOTS;
        this.mdsize = gMSSLeaf.mdsize;
        this.keysize = gMSSLeaf.keysize;
        this.gmssRandom = gMSSLeaf.gmssRandom;
        this.leaf = Arrays.clone(gMSSLeaf.leaf);
        this.concHashs = Arrays.clone(gMSSLeaf.concHashs);
        this.f396i = gMSSLeaf.f396i;
        this.f397j = gMSSLeaf.f397j;
        this.two_power_w = gMSSLeaf.two_power_w;
        this.f398w = gMSSLeaf.f398w;
        this.steps = gMSSLeaf.steps;
        this.seed = Arrays.clone(gMSSLeaf.seed);
        this.privateKeyOTS = Arrays.clone(gMSSLeaf.privateKeyOTS);
    }

    private int getLog(int i) {
        int i2 = 1;
        int i3 = 2;
        while (i3 < i) {
            i3 <<= 1;
            i2++;
        }
        return i2;
    }

    private void updateLeafCalc() {
        byte[] bArr = new byte[this.messDigestOTS.getDigestSize()];
        for (int i = 0; i < this.steps + 10000; i++) {
            if (this.f396i == this.keysize && this.f397j == this.two_power_w - 1) {
                this.messDigestOTS.update(this.concHashs, 0, this.concHashs.length);
                this.leaf = new byte[this.messDigestOTS.getDigestSize()];
                this.messDigestOTS.doFinal(this.leaf, 0);
                return;
            }
            if (this.f396i == 0 || this.f397j == this.two_power_w - 1) {
                this.f396i++;
                this.f397j = 0;
                this.privateKeyOTS = this.gmssRandom.nextSeed(this.seed);
            } else {
                this.messDigestOTS.update(this.privateKeyOTS, 0, this.privateKeyOTS.length);
                this.privateKeyOTS = bArr;
                this.messDigestOTS.doFinal(this.privateKeyOTS, 0);
                this.f397j++;
                if (this.f397j == this.two_power_w - 1) {
                    System.arraycopy(this.privateKeyOTS, 0, this.concHashs, this.mdsize * (this.f396i - 1), this.mdsize);
                }
            }
        }
        throw new IllegalStateException("unable to updateLeaf in steps: " + this.steps + " " + this.f396i + " " + this.f397j);
    }

    public byte[] getLeaf() {
        return Arrays.clone(this.leaf);
    }

    public byte[][] getStatByte() {
        byte[][] bArr = new byte[][]{new byte[this.mdsize], new byte[this.mdsize], new byte[(this.mdsize * this.keysize)], new byte[this.mdsize]};
        bArr[0] = this.privateKeyOTS;
        bArr[1] = this.seed;
        bArr[2] = this.concHashs;
        bArr[3] = this.leaf;
        return bArr;
    }

    public int[] getStatInt() {
        return new int[]{this.f396i, this.f397j, this.steps, this.f398w};
    }

    void initLeafCalc(byte[] bArr) {
        this.f396i = 0;
        this.f397j = 0;
        Object obj = new byte[this.mdsize];
        System.arraycopy(bArr, 0, obj, 0, this.seed.length);
        this.seed = this.gmssRandom.nextSeed(obj);
    }

    GMSSLeaf nextLeaf() {
        GMSSLeaf gMSSLeaf = new GMSSLeaf(this);
        gMSSLeaf.updateLeafCalc();
        return gMSSLeaf;
    }

    public String toString() {
        int i;
        String str = BuildConfig.FLAVOR;
        for (i = 0; i < 4; i++) {
            str = str + getStatInt()[i] + " ";
        }
        String str2 = str + " " + this.mdsize + " " + this.keysize + " " + this.two_power_w + " ";
        byte[][] statByte = getStatByte();
        String str3 = str2;
        for (i = 0; i < 4; i++) {
            str3 = statByte[i] != null ? str3 + new String(Hex.encode(statByte[i])) + " " : str3 + "null ";
        }
        return str3;
    }
}
