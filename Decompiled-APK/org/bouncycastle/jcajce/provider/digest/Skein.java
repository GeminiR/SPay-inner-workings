package org.bouncycastle.jcajce.provider.digest;

import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.crypto.digests.SkeinDigest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseKeyGenerator;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseMac;
import org.bouncycastle.jce.X509KeyUsage;

public class Skein {

    public static class DigestSkein1024 extends BCMessageDigest implements Cloneable {
        public DigestSkein1024(int i) {
            super(new SkeinDigest(SkeinMac.SKEIN_1024, i));
        }

        public Object clone() {
            BCMessageDigest bCMessageDigest = (BCMessageDigest) super.clone();
            bCMessageDigest.digest = new SkeinDigest((SkeinDigest) this.digest);
            return bCMessageDigest;
        }
    }

    public static class DigestSkein256 extends BCMessageDigest implements Cloneable {
        public DigestSkein256(int i) {
            super(new SkeinDigest(SkeinMac.SKEIN_256, i));
        }

        public Object clone() {
            BCMessageDigest bCMessageDigest = (BCMessageDigest) super.clone();
            bCMessageDigest.digest = new SkeinDigest((SkeinDigest) this.digest);
            return bCMessageDigest;
        }
    }

    public static class DigestSkein512 extends BCMessageDigest implements Cloneable {
        public DigestSkein512(int i) {
            super(new SkeinDigest(SkeinMac.SKEIN_512, i));
        }

        public Object clone() {
            BCMessageDigest bCMessageDigest = (BCMessageDigest) super.clone();
            bCMessageDigest.digest = new SkeinDigest((SkeinDigest) this.digest);
            return bCMessageDigest;
        }
    }

    public static class Digest_1024_1024 extends DigestSkein1024 {
        public Digest_1024_1024() {
            super(SkeinMac.SKEIN_1024);
        }
    }

    public static class Digest_1024_384 extends DigestSkein1024 {
        public Digest_1024_384() {
            super(384);
        }
    }

    public static class Digest_1024_512 extends DigestSkein1024 {
        public Digest_1024_512() {
            super(SkeinMac.SKEIN_512);
        }
    }

    public static class Digest_256_128 extends DigestSkein256 {
        public Digest_256_128() {
            super(X509KeyUsage.digitalSignature);
        }
    }

    public static class Digest_256_160 extends DigestSkein256 {
        public Digest_256_160() {
            super(CipherSuite.TLS_DH_RSA_WITH_AES_128_GCM_SHA256);
        }
    }

    public static class Digest_256_224 extends DigestSkein256 {
        public Digest_256_224() {
            super(224);
        }
    }

    public static class Digest_256_256 extends DigestSkein256 {
        public Digest_256_256() {
            super(SkeinMac.SKEIN_256);
        }
    }

    public static class Digest_512_128 extends DigestSkein512 {
        public Digest_512_128() {
            super(X509KeyUsage.digitalSignature);
        }
    }

    public static class Digest_512_160 extends DigestSkein512 {
        public Digest_512_160() {
            super(CipherSuite.TLS_DH_RSA_WITH_AES_128_GCM_SHA256);
        }
    }

    public static class Digest_512_224 extends DigestSkein512 {
        public Digest_512_224() {
            super(224);
        }
    }

    public static class Digest_512_256 extends DigestSkein512 {
        public Digest_512_256() {
            super(SkeinMac.SKEIN_256);
        }
    }

    public static class Digest_512_384 extends DigestSkein512 {
        public Digest_512_384() {
            super(384);
        }
    }

    public static class Digest_512_512 extends DigestSkein512 {
        public Digest_512_512() {
            super(SkeinMac.SKEIN_512);
        }
    }

    public static class HMacKeyGenerator_1024_1024 extends BaseKeyGenerator {
        public HMacKeyGenerator_1024_1024() {
            super("HMACSkein-1024-1024", SkeinMac.SKEIN_1024, new CipherKeyGenerator());
        }
    }

    public static class HMacKeyGenerator_1024_384 extends BaseKeyGenerator {
        public HMacKeyGenerator_1024_384() {
            super("HMACSkein-1024-384", 384, new CipherKeyGenerator());
        }
    }

    public static class HMacKeyGenerator_1024_512 extends BaseKeyGenerator {
        public HMacKeyGenerator_1024_512() {
            super("HMACSkein-1024-512", SkeinMac.SKEIN_512, new CipherKeyGenerator());
        }
    }

    public static class HMacKeyGenerator_256_128 extends BaseKeyGenerator {
        public HMacKeyGenerator_256_128() {
            super("HMACSkein-256-128", X509KeyUsage.digitalSignature, new CipherKeyGenerator());
        }
    }

    public static class HMacKeyGenerator_256_160 extends BaseKeyGenerator {
        public HMacKeyGenerator_256_160() {
            super("HMACSkein-256-160", CipherSuite.TLS_DH_RSA_WITH_AES_128_GCM_SHA256, new CipherKeyGenerator());
        }
    }

    public static class HMacKeyGenerator_256_224 extends BaseKeyGenerator {
        public HMacKeyGenerator_256_224() {
            super("HMACSkein-256-224", 224, new CipherKeyGenerator());
        }
    }

    public static class HMacKeyGenerator_256_256 extends BaseKeyGenerator {
        public HMacKeyGenerator_256_256() {
            super("HMACSkein-256-256", SkeinMac.SKEIN_256, new CipherKeyGenerator());
        }
    }

    public static class HMacKeyGenerator_512_128 extends BaseKeyGenerator {
        public HMacKeyGenerator_512_128() {
            super("HMACSkein-512-128", X509KeyUsage.digitalSignature, new CipherKeyGenerator());
        }
    }

    public static class HMacKeyGenerator_512_160 extends BaseKeyGenerator {
        public HMacKeyGenerator_512_160() {
            super("HMACSkein-512-160", CipherSuite.TLS_DH_RSA_WITH_AES_128_GCM_SHA256, new CipherKeyGenerator());
        }
    }

    public static class HMacKeyGenerator_512_224 extends BaseKeyGenerator {
        public HMacKeyGenerator_512_224() {
            super("HMACSkein-512-224", 224, new CipherKeyGenerator());
        }
    }

    public static class HMacKeyGenerator_512_256 extends BaseKeyGenerator {
        public HMacKeyGenerator_512_256() {
            super("HMACSkein-512-256", SkeinMac.SKEIN_256, new CipherKeyGenerator());
        }
    }

    public static class HMacKeyGenerator_512_384 extends BaseKeyGenerator {
        public HMacKeyGenerator_512_384() {
            super("HMACSkein-512-384", 384, new CipherKeyGenerator());
        }
    }

    public static class HMacKeyGenerator_512_512 extends BaseKeyGenerator {
        public HMacKeyGenerator_512_512() {
            super("HMACSkein-512-512", SkeinMac.SKEIN_512, new CipherKeyGenerator());
        }
    }

    public static class HashMac_1024_1024 extends BaseMac {
        public HashMac_1024_1024() {
            super(new HMac(new SkeinDigest(SkeinMac.SKEIN_1024, SkeinMac.SKEIN_1024)));
        }
    }

    public static class HashMac_1024_384 extends BaseMac {
        public HashMac_1024_384() {
            super(new HMac(new SkeinDigest(SkeinMac.SKEIN_1024, 384)));
        }
    }

    public static class HashMac_1024_512 extends BaseMac {
        public HashMac_1024_512() {
            super(new HMac(new SkeinDigest(SkeinMac.SKEIN_1024, SkeinMac.SKEIN_512)));
        }
    }

    public static class HashMac_256_128 extends BaseMac {
        public HashMac_256_128() {
            super(new HMac(new SkeinDigest(SkeinMac.SKEIN_256, X509KeyUsage.digitalSignature)));
        }
    }

    public static class HashMac_256_160 extends BaseMac {
        public HashMac_256_160() {
            super(new HMac(new SkeinDigest(SkeinMac.SKEIN_256, CipherSuite.TLS_DH_RSA_WITH_AES_128_GCM_SHA256)));
        }
    }

    public static class HashMac_256_224 extends BaseMac {
        public HashMac_256_224() {
            super(new HMac(new SkeinDigest(SkeinMac.SKEIN_256, 224)));
        }
    }

    public static class HashMac_256_256 extends BaseMac {
        public HashMac_256_256() {
            super(new HMac(new SkeinDigest(SkeinMac.SKEIN_256, SkeinMac.SKEIN_256)));
        }
    }

    public static class HashMac_512_128 extends BaseMac {
        public HashMac_512_128() {
            super(new HMac(new SkeinDigest(SkeinMac.SKEIN_512, X509KeyUsage.digitalSignature)));
        }
    }

    public static class HashMac_512_160 extends BaseMac {
        public HashMac_512_160() {
            super(new HMac(new SkeinDigest(SkeinMac.SKEIN_512, CipherSuite.TLS_DH_RSA_WITH_AES_128_GCM_SHA256)));
        }
    }

    public static class HashMac_512_224 extends BaseMac {
        public HashMac_512_224() {
            super(new HMac(new SkeinDigest(SkeinMac.SKEIN_512, 224)));
        }
    }

    public static class HashMac_512_256 extends BaseMac {
        public HashMac_512_256() {
            super(new HMac(new SkeinDigest(SkeinMac.SKEIN_512, SkeinMac.SKEIN_256)));
        }
    }

    public static class HashMac_512_384 extends BaseMac {
        public HashMac_512_384() {
            super(new HMac(new SkeinDigest(SkeinMac.SKEIN_512, 384)));
        }
    }

    public static class HashMac_512_512 extends BaseMac {
        public HashMac_512_512() {
            super(new HMac(new SkeinDigest(SkeinMac.SKEIN_512, SkeinMac.SKEIN_512)));
        }
    }

    public static class Mappings extends DigestAlgorithmProvider {
        private static final String PREFIX;

        static {
            PREFIX = Skein.class.getName();
        }

        private void addSkeinMacAlgorithm(ConfigurableProvider configurableProvider, int i, int i2) {
            String str = "Skein-MAC-" + i + HCEClientConstants.TAG_KEY_SEPARATOR + i2;
            String str2 = PREFIX + "$SkeinMac_" + i + "_" + i2;
            String str3 = PREFIX + "$SkeinMacKeyGenerator_" + i + "_" + i2;
            configurableProvider.addAlgorithm("Mac." + str, str2);
            configurableProvider.addAlgorithm("Alg.Alias.Mac.Skein-MAC" + i + "/" + i2, str);
            configurableProvider.addAlgorithm("KeyGenerator." + str, str3);
            configurableProvider.addAlgorithm("Alg.Alias.KeyGenerator.Skein-MAC" + i + "/" + i2, str);
        }

        public void configure(ConfigurableProvider configurableProvider) {
            configurableProvider.addAlgorithm("MessageDigest.Skein-256-128", PREFIX + "$Digest_256_128");
            configurableProvider.addAlgorithm("MessageDigest.Skein-256-160", PREFIX + "$Digest_256_160");
            configurableProvider.addAlgorithm("MessageDigest.Skein-256-224", PREFIX + "$Digest_256_224");
            configurableProvider.addAlgorithm("MessageDigest.Skein-256-256", PREFIX + "$Digest_256_256");
            configurableProvider.addAlgorithm("MessageDigest.Skein-512-128", PREFIX + "$Digest_512_128");
            configurableProvider.addAlgorithm("MessageDigest.Skein-512-160", PREFIX + "$Digest_512_160");
            configurableProvider.addAlgorithm("MessageDigest.Skein-512-224", PREFIX + "$Digest_512_224");
            configurableProvider.addAlgorithm("MessageDigest.Skein-512-256", PREFIX + "$Digest_512_256");
            configurableProvider.addAlgorithm("MessageDigest.Skein-512-384", PREFIX + "$Digest_512_384");
            configurableProvider.addAlgorithm("MessageDigest.Skein-512-512", PREFIX + "$Digest_512_512");
            configurableProvider.addAlgorithm("MessageDigest.Skein-1024-384", PREFIX + "$Digest_1024_384");
            configurableProvider.addAlgorithm("MessageDigest.Skein-1024-512", PREFIX + "$Digest_1024_512");
            configurableProvider.addAlgorithm("MessageDigest.Skein-1024-1024", PREFIX + "$Digest_1024_1024");
            addHMACAlgorithm(configurableProvider, "Skein-256-128", PREFIX + "$HashMac_256_128", PREFIX + "$HMacKeyGenerator_256_128");
            addHMACAlgorithm(configurableProvider, "Skein-256-160", PREFIX + "$HashMac_256_160", PREFIX + "$HMacKeyGenerator_256_160");
            addHMACAlgorithm(configurableProvider, "Skein-256-224", PREFIX + "$HashMac_256_224", PREFIX + "$HMacKeyGenerator_256_224");
            addHMACAlgorithm(configurableProvider, "Skein-256-256", PREFIX + "$HashMac_256_256", PREFIX + "$HMacKeyGenerator_256_256");
            addHMACAlgorithm(configurableProvider, "Skein-512-128", PREFIX + "$HashMac_512_128", PREFIX + "$HMacKeyGenerator_512_128");
            addHMACAlgorithm(configurableProvider, "Skein-512-160", PREFIX + "$HashMac_512_160", PREFIX + "$HMacKeyGenerator_512_160");
            addHMACAlgorithm(configurableProvider, "Skein-512-224", PREFIX + "$HashMac_512_224", PREFIX + "$HMacKeyGenerator_512_224");
            addHMACAlgorithm(configurableProvider, "Skein-512-256", PREFIX + "$HashMac_512_256", PREFIX + "$HMacKeyGenerator_512_256");
            addHMACAlgorithm(configurableProvider, "Skein-512-384", PREFIX + "$HashMac_512_384", PREFIX + "$HMacKeyGenerator_512_384");
            addHMACAlgorithm(configurableProvider, "Skein-512-512", PREFIX + "$HashMac_512_512", PREFIX + "$HMacKeyGenerator_512_512");
            addHMACAlgorithm(configurableProvider, "Skein-1024-384", PREFIX + "$HashMac_1024_384", PREFIX + "$HMacKeyGenerator_1024_384");
            addHMACAlgorithm(configurableProvider, "Skein-1024-512", PREFIX + "$HashMac_1024_512", PREFIX + "$HMacKeyGenerator_1024_512");
            addHMACAlgorithm(configurableProvider, "Skein-1024-1024", PREFIX + "$HashMac_1024_1024", PREFIX + "$HMacKeyGenerator_1024_1024");
            addSkeinMacAlgorithm(configurableProvider, SkeinMac.SKEIN_256, X509KeyUsage.digitalSignature);
            addSkeinMacAlgorithm(configurableProvider, SkeinMac.SKEIN_256, CipherSuite.TLS_DH_RSA_WITH_AES_128_GCM_SHA256);
            addSkeinMacAlgorithm(configurableProvider, SkeinMac.SKEIN_256, 224);
            addSkeinMacAlgorithm(configurableProvider, SkeinMac.SKEIN_256, SkeinMac.SKEIN_256);
            addSkeinMacAlgorithm(configurableProvider, SkeinMac.SKEIN_512, X509KeyUsage.digitalSignature);
            addSkeinMacAlgorithm(configurableProvider, SkeinMac.SKEIN_512, CipherSuite.TLS_DH_RSA_WITH_AES_128_GCM_SHA256);
            addSkeinMacAlgorithm(configurableProvider, SkeinMac.SKEIN_512, 224);
            addSkeinMacAlgorithm(configurableProvider, SkeinMac.SKEIN_512, SkeinMac.SKEIN_256);
            addSkeinMacAlgorithm(configurableProvider, SkeinMac.SKEIN_512, 384);
            addSkeinMacAlgorithm(configurableProvider, SkeinMac.SKEIN_512, SkeinMac.SKEIN_512);
            addSkeinMacAlgorithm(configurableProvider, SkeinMac.SKEIN_1024, 384);
            addSkeinMacAlgorithm(configurableProvider, SkeinMac.SKEIN_1024, SkeinMac.SKEIN_512);
            addSkeinMacAlgorithm(configurableProvider, SkeinMac.SKEIN_1024, SkeinMac.SKEIN_1024);
        }
    }

    public static class SkeinMacKeyGenerator_1024_1024 extends BaseKeyGenerator {
        public SkeinMacKeyGenerator_1024_1024() {
            super("Skein-MAC-1024-1024", SkeinMac.SKEIN_1024, new CipherKeyGenerator());
        }
    }

    public static class SkeinMacKeyGenerator_1024_384 extends BaseKeyGenerator {
        public SkeinMacKeyGenerator_1024_384() {
            super("Skein-MAC-1024-384", 384, new CipherKeyGenerator());
        }
    }

    public static class SkeinMacKeyGenerator_1024_512 extends BaseKeyGenerator {
        public SkeinMacKeyGenerator_1024_512() {
            super("Skein-MAC-1024-512", SkeinMac.SKEIN_512, new CipherKeyGenerator());
        }
    }

    public static class SkeinMacKeyGenerator_256_128 extends BaseKeyGenerator {
        public SkeinMacKeyGenerator_256_128() {
            super("Skein-MAC-256-128", X509KeyUsage.digitalSignature, new CipherKeyGenerator());
        }
    }

    public static class SkeinMacKeyGenerator_256_160 extends BaseKeyGenerator {
        public SkeinMacKeyGenerator_256_160() {
            super("Skein-MAC-256-160", CipherSuite.TLS_DH_RSA_WITH_AES_128_GCM_SHA256, new CipherKeyGenerator());
        }
    }

    public static class SkeinMacKeyGenerator_256_224 extends BaseKeyGenerator {
        public SkeinMacKeyGenerator_256_224() {
            super("Skein-MAC-256-224", 224, new CipherKeyGenerator());
        }
    }

    public static class SkeinMacKeyGenerator_256_256 extends BaseKeyGenerator {
        public SkeinMacKeyGenerator_256_256() {
            super("Skein-MAC-256-256", SkeinMac.SKEIN_256, new CipherKeyGenerator());
        }
    }

    public static class SkeinMacKeyGenerator_512_128 extends BaseKeyGenerator {
        public SkeinMacKeyGenerator_512_128() {
            super("Skein-MAC-512-128", X509KeyUsage.digitalSignature, new CipherKeyGenerator());
        }
    }

    public static class SkeinMacKeyGenerator_512_160 extends BaseKeyGenerator {
        public SkeinMacKeyGenerator_512_160() {
            super("Skein-MAC-512-160", CipherSuite.TLS_DH_RSA_WITH_AES_128_GCM_SHA256, new CipherKeyGenerator());
        }
    }

    public static class SkeinMacKeyGenerator_512_224 extends BaseKeyGenerator {
        public SkeinMacKeyGenerator_512_224() {
            super("Skein-MAC-512-224", 224, new CipherKeyGenerator());
        }
    }

    public static class SkeinMacKeyGenerator_512_256 extends BaseKeyGenerator {
        public SkeinMacKeyGenerator_512_256() {
            super("Skein-MAC-512-256", SkeinMac.SKEIN_256, new CipherKeyGenerator());
        }
    }

    public static class SkeinMacKeyGenerator_512_384 extends BaseKeyGenerator {
        public SkeinMacKeyGenerator_512_384() {
            super("Skein-MAC-512-384", 384, new CipherKeyGenerator());
        }
    }

    public static class SkeinMacKeyGenerator_512_512 extends BaseKeyGenerator {
        public SkeinMacKeyGenerator_512_512() {
            super("Skein-MAC-512-512", SkeinMac.SKEIN_512, new CipherKeyGenerator());
        }
    }

    public static class SkeinMac_1024_1024 extends BaseMac {
        public SkeinMac_1024_1024() {
            super(new SkeinMac(SkeinMac.SKEIN_1024, SkeinMac.SKEIN_1024));
        }
    }

    public static class SkeinMac_1024_384 extends BaseMac {
        public SkeinMac_1024_384() {
            super(new SkeinMac(SkeinMac.SKEIN_1024, 384));
        }
    }

    public static class SkeinMac_1024_512 extends BaseMac {
        public SkeinMac_1024_512() {
            super(new SkeinMac(SkeinMac.SKEIN_1024, SkeinMac.SKEIN_512));
        }
    }

    public static class SkeinMac_256_128 extends BaseMac {
        public SkeinMac_256_128() {
            super(new SkeinMac(SkeinMac.SKEIN_256, X509KeyUsage.digitalSignature));
        }
    }

    public static class SkeinMac_256_160 extends BaseMac {
        public SkeinMac_256_160() {
            super(new SkeinMac(SkeinMac.SKEIN_256, CipherSuite.TLS_DH_RSA_WITH_AES_128_GCM_SHA256));
        }
    }

    public static class SkeinMac_256_224 extends BaseMac {
        public SkeinMac_256_224() {
            super(new SkeinMac(SkeinMac.SKEIN_256, 224));
        }
    }

    public static class SkeinMac_256_256 extends BaseMac {
        public SkeinMac_256_256() {
            super(new SkeinMac(SkeinMac.SKEIN_256, SkeinMac.SKEIN_256));
        }
    }

    public static class SkeinMac_512_128 extends BaseMac {
        public SkeinMac_512_128() {
            super(new SkeinMac(SkeinMac.SKEIN_512, X509KeyUsage.digitalSignature));
        }
    }

    public static class SkeinMac_512_160 extends BaseMac {
        public SkeinMac_512_160() {
            super(new SkeinMac(SkeinMac.SKEIN_512, CipherSuite.TLS_DH_RSA_WITH_AES_128_GCM_SHA256));
        }
    }

    public static class SkeinMac_512_224 extends BaseMac {
        public SkeinMac_512_224() {
            super(new SkeinMac(SkeinMac.SKEIN_512, 224));
        }
    }

    public static class SkeinMac_512_256 extends BaseMac {
        public SkeinMac_512_256() {
            super(new SkeinMac(SkeinMac.SKEIN_512, SkeinMac.SKEIN_256));
        }
    }

    public static class SkeinMac_512_384 extends BaseMac {
        public SkeinMac_512_384() {
            super(new SkeinMac(SkeinMac.SKEIN_512, 384));
        }
    }

    public static class SkeinMac_512_512 extends BaseMac {
        public SkeinMac_512_512() {
            super(new SkeinMac(SkeinMac.SKEIN_512, SkeinMac.SKEIN_512));
        }
    }

    private Skein() {
    }
}
