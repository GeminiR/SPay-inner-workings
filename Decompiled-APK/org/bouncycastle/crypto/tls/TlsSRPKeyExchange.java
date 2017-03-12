package org.bouncycastle.crypto.tls;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Vector;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.agreement.srp.SRP6Client;
import org.bouncycastle.crypto.agreement.srp.SRP6Server;
import org.bouncycastle.crypto.agreement.srp.SRP6Util;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.SRP6GroupParameters;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.BigIntegers;
import org.bouncycastle.util.io.TeeInputStream;

public class TlsSRPKeyExchange extends AbstractTlsKeyExchange {
    protected TlsSRPGroupVerifier groupVerifier;
    protected byte[] identity;
    protected byte[] password;
    protected TlsSignerCredentials serverCredentials;
    protected AsymmetricKeyParameter serverPublicKey;
    protected SRP6Client srpClient;
    protected SRP6GroupParameters srpGroup;
    protected BigInteger srpPeerCredentials;
    protected byte[] srpSalt;
    protected SRP6Server srpServer;
    protected BigInteger srpVerifier;
    protected TlsSigner tlsSigner;

    public TlsSRPKeyExchange(int i, Vector vector, TlsSRPGroupVerifier tlsSRPGroupVerifier, byte[] bArr, byte[] bArr2) {
        super(i, vector);
        this.serverPublicKey = null;
        this.srpGroup = null;
        this.srpClient = null;
        this.srpServer = null;
        this.srpPeerCredentials = null;
        this.srpVerifier = null;
        this.srpSalt = null;
        this.serverCredentials = null;
        this.tlsSigner = createSigner(i);
        this.groupVerifier = tlsSRPGroupVerifier;
        this.identity = bArr;
        this.password = bArr2;
        this.srpClient = new SRP6Client();
    }

    public TlsSRPKeyExchange(int i, Vector vector, byte[] bArr, TlsSRPLoginParameters tlsSRPLoginParameters) {
        super(i, vector);
        this.serverPublicKey = null;
        this.srpGroup = null;
        this.srpClient = null;
        this.srpServer = null;
        this.srpPeerCredentials = null;
        this.srpVerifier = null;
        this.srpSalt = null;
        this.serverCredentials = null;
        this.tlsSigner = createSigner(i);
        this.identity = bArr;
        this.srpServer = new SRP6Server();
        this.srpGroup = tlsSRPLoginParameters.getGroup();
        this.srpVerifier = tlsSRPLoginParameters.getVerifier();
        this.srpSalt = tlsSRPLoginParameters.getSalt();
    }

    public TlsSRPKeyExchange(int i, Vector vector, byte[] bArr, byte[] bArr2) {
        this(i, vector, new DefaultTlsSRPGroupVerifier(), bArr, bArr2);
    }

    protected static TlsSigner createSigner(int i) {
        switch (i) {
            case NamedCurve.secp224r1 /*21*/:
                return null;
            case NamedCurve.secp256k1 /*22*/:
                return new TlsDSSSigner();
            case NamedCurve.secp256r1 /*23*/:
                return new TlsRSASigner();
            default:
                throw new IllegalArgumentException("unsupported key exchange algorithm");
        }
    }

    public void generateClientKeyExchange(OutputStream outputStream) {
        TlsSRPUtils.writeSRPParameter(this.srpClient.generateClientCredentials(this.srpSalt, this.identity, this.password), outputStream);
        this.context.getSecurityParameters().srpIdentity = Arrays.clone(this.identity);
    }

    public byte[] generatePremasterSecret() {
        try {
            return BigIntegers.asUnsignedByteArray(this.srpServer != null ? this.srpServer.calculateSecret(this.srpPeerCredentials) : this.srpClient.calculateSecret(this.srpPeerCredentials));
        } catch (Throwable e) {
            throw new TlsFatalAlert((short) 47, e);
        }
    }

    public byte[] generateServerKeyExchange() {
        this.srpServer.init(this.srpGroup, this.srpVerifier, TlsUtils.createHash((short) 2), this.context.getSecureRandom());
        ServerSRPParams serverSRPParams = new ServerSRPParams(this.srpGroup.getN(), this.srpGroup.getG(), this.srpSalt, this.srpServer.generateServerCredentials());
        OutputStream digestInputBuffer = new DigestInputBuffer();
        serverSRPParams.encode(digestInputBuffer);
        if (this.serverCredentials != null) {
            SignatureAndHashAlgorithm signatureAndHashAlgorithm = TlsUtils.getSignatureAndHashAlgorithm(this.context, this.serverCredentials);
            Digest createHash = TlsUtils.createHash(signatureAndHashAlgorithm);
            SecurityParameters securityParameters = this.context.getSecurityParameters();
            createHash.update(securityParameters.clientRandom, 0, securityParameters.clientRandom.length);
            createHash.update(securityParameters.serverRandom, 0, securityParameters.serverRandom.length);
            digestInputBuffer.updateDigest(createHash);
            byte[] bArr = new byte[createHash.getDigestSize()];
            createHash.doFinal(bArr, 0);
            new DigitallySigned(signatureAndHashAlgorithm, this.serverCredentials.generateCertificateSignature(bArr)).encode(digestInputBuffer);
        }
        return digestInputBuffer.toByteArray();
    }

    public void init(TlsContext tlsContext) {
        super.init(tlsContext);
        if (this.tlsSigner != null) {
            this.tlsSigner.init(tlsContext);
        }
    }

    protected Signer initVerifyer(TlsSigner tlsSigner, SignatureAndHashAlgorithm signatureAndHashAlgorithm, SecurityParameters securityParameters) {
        Signer createVerifyer = tlsSigner.createVerifyer(signatureAndHashAlgorithm, this.serverPublicKey);
        createVerifyer.update(securityParameters.clientRandom, 0, securityParameters.clientRandom.length);
        createVerifyer.update(securityParameters.serverRandom, 0, securityParameters.serverRandom.length);
        return createVerifyer;
    }

    public void processClientCredentials(TlsCredentials tlsCredentials) {
        throw new TlsFatalAlert((short) 80);
    }

    public void processClientKeyExchange(InputStream inputStream) {
        try {
            this.srpPeerCredentials = SRP6Util.validatePublicValue(this.srpGroup.getN(), TlsSRPUtils.readSRPParameter(inputStream));
            this.context.getSecurityParameters().srpIdentity = Arrays.clone(this.identity);
        } catch (Throwable e) {
            throw new TlsFatalAlert((short) 47, e);
        }
    }

    public void processServerCertificate(Certificate certificate) {
        if (this.tlsSigner == null) {
            throw new TlsFatalAlert((short) 10);
        } else if (certificate.isEmpty()) {
            throw new TlsFatalAlert((short) 42);
        } else {
            Certificate certificateAt = certificate.getCertificateAt(0);
            try {
                this.serverPublicKey = PublicKeyFactory.createKey(certificateAt.getSubjectPublicKeyInfo());
                if (this.tlsSigner.isValidPublicKey(this.serverPublicKey)) {
                    TlsUtils.validateKeyUsage(certificateAt, X509KeyUsage.digitalSignature);
                    super.processServerCertificate(certificate);
                    return;
                }
                throw new TlsFatalAlert((short) 46);
            } catch (Throwable e) {
                throw new TlsFatalAlert((short) 43, e);
            }
        }
    }

    public void processServerCredentials(TlsCredentials tlsCredentials) {
        if (this.keyExchange == 21 || !(tlsCredentials instanceof TlsSignerCredentials)) {
            throw new TlsFatalAlert((short) 80);
        }
        processServerCertificate(tlsCredentials.getCertificate());
        this.serverCredentials = (TlsSignerCredentials) tlsCredentials;
    }

    public void processServerKeyExchange(InputStream inputStream) {
        SignerInputBuffer signerInputBuffer;
        InputStream teeInputStream;
        SecurityParameters securityParameters = this.context.getSecurityParameters();
        if (this.tlsSigner != null) {
            signerInputBuffer = new SignerInputBuffer();
            teeInputStream = new TeeInputStream(inputStream, signerInputBuffer);
        } else {
            signerInputBuffer = null;
            teeInputStream = inputStream;
        }
        ServerSRPParams parse = ServerSRPParams.parse(teeInputStream);
        if (signerInputBuffer != null) {
            DigitallySigned parse2 = DigitallySigned.parse(this.context, inputStream);
            Signer initVerifyer = initVerifyer(this.tlsSigner, parse2.getAlgorithm(), securityParameters);
            signerInputBuffer.updateSigner(initVerifyer);
            if (!initVerifyer.verifySignature(parse2.getSignature())) {
                throw new TlsFatalAlert((short) 51);
            }
        }
        this.srpGroup = new SRP6GroupParameters(parse.getN(), parse.getG());
        if (this.groupVerifier.accept(this.srpGroup)) {
            this.srpSalt = parse.getS();
            try {
                this.srpPeerCredentials = SRP6Util.validatePublicValue(this.srpGroup.getN(), parse.getB());
                this.srpClient.init(this.srpGroup, TlsUtils.createHash((short) 2), this.context.getSecureRandom());
                return;
            } catch (Throwable e) {
                throw new TlsFatalAlert((short) 47, e);
            }
        }
        throw new TlsFatalAlert((short) 71);
    }

    public boolean requiresServerKeyExchange() {
        return true;
    }

    public void skipServerCredentials() {
        if (this.tlsSigner != null) {
            throw new TlsFatalAlert((short) 10);
        }
    }

    public void validateCertificateRequest(CertificateRequest certificateRequest) {
        throw new TlsFatalAlert((short) 10);
    }
}
