package org.bouncycastle.crypto.generators;

import java.math.BigInteger;
import java.util.Random;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.crypto.params.GOST3410KeyGenerationParameters;
import org.bouncycastle.crypto.params.GOST3410Parameters;
import org.bouncycastle.crypto.params.GOST3410PrivateKeyParameters;
import org.bouncycastle.crypto.params.GOST3410PublicKeyParameters;
import org.bouncycastle.math.ec.WNafUtil;

public class GOST3410KeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
    private GOST3410KeyGenerationParameters param;

    public AsymmetricCipherKeyPair generateKeyPair() {
        GOST3410Parameters parameters = this.param.getParameters();
        Random random = this.param.getRandom();
        BigInteger q = parameters.getQ();
        BigInteger p = parameters.getP();
        BigInteger a = parameters.getA();
        while (true) {
            BigInteger bigInteger = new BigInteger(SkeinMac.SKEIN_256, random);
            if (bigInteger.signum() >= 1 && bigInteger.compareTo(q) < 0 && WNafUtil.getNafWeight(bigInteger) >= 64) {
                return new AsymmetricCipherKeyPair(new GOST3410PublicKeyParameters(a.modPow(bigInteger, p), parameters), new GOST3410PrivateKeyParameters(bigInteger, parameters));
            }
        }
    }

    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.param = (GOST3410KeyGenerationParameters) keyGenerationParameters;
    }
}
