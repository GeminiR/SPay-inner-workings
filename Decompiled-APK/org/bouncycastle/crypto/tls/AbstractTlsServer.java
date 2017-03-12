package org.bouncycastle.crypto.tls;

import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.util.Arrays;

public abstract class AbstractTlsServer extends AbstractTlsPeer implements TlsServer {
    protected TlsCipherFactory cipherFactory;
    protected short[] clientECPointFormats;
    protected Hashtable clientExtensions;
    protected ProtocolVersion clientVersion;
    protected TlsServerContext context;
    protected boolean eccCipherSuitesOffered;
    protected boolean encryptThenMACOffered;
    protected short maxFragmentLengthOffered;
    protected int[] namedCurves;
    protected int[] offeredCipherSuites;
    protected short[] offeredCompressionMethods;
    protected int selectedCipherSuite;
    protected short selectedCompressionMethod;
    protected short[] serverECPointFormats;
    protected Hashtable serverExtensions;
    protected ProtocolVersion serverVersion;
    protected Vector supportedSignatureAlgorithms;
    protected boolean truncatedHMacOffered;

    public AbstractTlsServer() {
        this(new DefaultTlsCipherFactory());
    }

    public AbstractTlsServer(TlsCipherFactory tlsCipherFactory) {
        this.cipherFactory = tlsCipherFactory;
    }

    protected boolean allowEncryptThenMAC() {
        return true;
    }

    protected boolean allowTruncatedHMac() {
        return false;
    }

    protected Hashtable checkServerExtensions() {
        Hashtable ensureExtensionsInitialised = TlsExtensionsUtils.ensureExtensionsInitialised(this.serverExtensions);
        this.serverExtensions = ensureExtensionsInitialised;
        return ensureExtensionsInitialised;
    }

    public CertificateRequest getCertificateRequest() {
        return null;
    }

    public CertificateStatus getCertificateStatus() {
        return null;
    }

    protected abstract int[] getCipherSuites();

    public TlsCompression getCompression() {
        switch (this.selectedCompressionMethod) {
            case ECCurve.COORD_AFFINE /*0*/:
                return new TlsNullCompression();
            default:
                throw new TlsFatalAlert((short) 80);
        }
    }

    protected short[] getCompressionMethods() {
        return new short[]{(short) 0};
    }

    protected ProtocolVersion getMaximumVersion() {
        return ProtocolVersion.TLSv11;
    }

    protected ProtocolVersion getMinimumVersion() {
        return ProtocolVersion.TLSv10;
    }

    public NewSessionTicket getNewSessionTicket() {
        return new NewSessionTicket(0, TlsUtils.EMPTY_BYTES);
    }

    public int getSelectedCipherSuite() {
        boolean supportsClientECCCapabilities = supportsClientECCCapabilities(this.namedCurves, this.clientECPointFormats);
        int[] cipherSuites = getCipherSuites();
        int i = 0;
        while (i < cipherSuites.length) {
            int i2 = cipherSuites[i];
            if (!Arrays.contains(this.offeredCipherSuites, i2) || ((!supportsClientECCCapabilities && TlsECCUtils.isECCCipherSuite(i2)) || !TlsUtils.isValidCipherSuiteForVersion(i2, this.serverVersion))) {
                i++;
            } else {
                this.selectedCipherSuite = i2;
                return i2;
            }
        }
        throw new TlsFatalAlert((short) 40);
    }

    public short getSelectedCompressionMethod() {
        short[] compressionMethods = getCompressionMethods();
        for (int i = 0; i < compressionMethods.length; i++) {
            if (Arrays.contains(this.offeredCompressionMethods, compressionMethods[i])) {
                short s = compressionMethods[i];
                this.selectedCompressionMethod = s;
                return s;
            }
        }
        throw new TlsFatalAlert((short) 40);
    }

    public Hashtable getServerExtensions() {
        if (this.encryptThenMACOffered && allowEncryptThenMAC() && TlsUtils.isBlockCipherSuite(this.selectedCipherSuite)) {
            TlsExtensionsUtils.addEncryptThenMACExtension(checkServerExtensions());
        }
        if (this.maxFragmentLengthOffered >= (short) 0 && MaxFragmentLength.isValid(this.maxFragmentLengthOffered)) {
            TlsExtensionsUtils.addMaxFragmentLengthExtension(checkServerExtensions(), this.maxFragmentLengthOffered);
        }
        if (this.truncatedHMacOffered && allowTruncatedHMac()) {
            TlsExtensionsUtils.addTruncatedHMacExtension(checkServerExtensions());
        }
        if (this.clientECPointFormats != null && TlsECCUtils.isECCCipherSuite(this.selectedCipherSuite)) {
            this.serverECPointFormats = new short[]{(short) 0, (short) 1, (short) 2};
            TlsECCUtils.addSupportedPointFormatsExtension(checkServerExtensions(), this.serverECPointFormats);
        }
        return this.serverExtensions;
    }

    public Vector getServerSupplementalData() {
        return null;
    }

    public ProtocolVersion getServerVersion() {
        if (getMinimumVersion().isEqualOrEarlierVersionOf(this.clientVersion)) {
            ProtocolVersion maximumVersion = getMaximumVersion();
            if (this.clientVersion.isEqualOrEarlierVersionOf(maximumVersion)) {
                maximumVersion = this.clientVersion;
                this.serverVersion = maximumVersion;
                return maximumVersion;
            } else if (this.clientVersion.isLaterVersionOf(maximumVersion)) {
                this.serverVersion = maximumVersion;
                return maximumVersion;
            }
        }
        throw new TlsFatalAlert((short) 70);
    }

    public void init(TlsServerContext tlsServerContext) {
        this.context = tlsServerContext;
    }

    public void notifyClientCertificate(Certificate certificate) {
        throw new TlsFatalAlert((short) 80);
    }

    public void notifyClientVersion(ProtocolVersion protocolVersion) {
        this.clientVersion = protocolVersion;
    }

    public void notifyFallback(boolean z) {
        if (z && getMaximumVersion().isLaterVersionOf(this.clientVersion)) {
            throw new TlsFatalAlert((short) 86);
        }
    }

    public void notifyOfferedCipherSuites(int[] iArr) {
        this.offeredCipherSuites = iArr;
        this.eccCipherSuitesOffered = TlsECCUtils.containsECCCipherSuites(this.offeredCipherSuites);
    }

    public void notifyOfferedCompressionMethods(short[] sArr) {
        this.offeredCompressionMethods = sArr;
    }

    public void processClientExtensions(Hashtable hashtable) {
        this.clientExtensions = hashtable;
        if (hashtable != null) {
            this.encryptThenMACOffered = TlsExtensionsUtils.hasEncryptThenMACExtension(hashtable);
            this.maxFragmentLengthOffered = TlsExtensionsUtils.getMaxFragmentLengthExtension(hashtable);
            this.truncatedHMacOffered = TlsExtensionsUtils.hasTruncatedHMacExtension(hashtable);
            this.supportedSignatureAlgorithms = TlsUtils.getSignatureAlgorithmsExtension(hashtable);
            if (this.supportedSignatureAlgorithms == null || TlsUtils.isSignatureAlgorithmsExtensionAllowed(this.clientVersion)) {
                this.namedCurves = TlsECCUtils.getSupportedEllipticCurvesExtension(hashtable);
                this.clientECPointFormats = TlsECCUtils.getSupportedPointFormatsExtension(hashtable);
            } else {
                throw new TlsFatalAlert((short) 47);
            }
        }
        if (!this.eccCipherSuitesOffered) {
            if (this.namedCurves != null || this.clientECPointFormats != null) {
                throw new TlsFatalAlert((short) 47);
            }
        }
    }

    public void processClientSupplementalData(Vector vector) {
        if (vector != null) {
            throw new TlsFatalAlert((short) 10);
        }
    }

    protected boolean supportsClientECCCapabilities(int[] iArr, short[] sArr) {
        if (iArr == null) {
            return TlsECCUtils.hasAnySupportedNamedCurves();
        }
        for (int i : iArr) {
            if (NamedCurve.isValid(i) && (!NamedCurve.refersToASpecificNamedCurve(i) || TlsECCUtils.isSupportedNamedCurve(i))) {
                return true;
            }
        }
        return false;
    }
}
