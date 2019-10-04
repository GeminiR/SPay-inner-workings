/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.security.cert.CertPathParameters
 *  java.security.cert.CertSelector
 *  java.security.cert.CertStore
 *  java.security.cert.Certificate
 *  java.security.cert.PKIXParameters
 *  java.security.cert.TrustAnchor
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Collections
 *  java.util.Date
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 *  java.util.Set
 */
package org.bouncycastle.jcajce;

import java.security.cert.CertPathParameters;
import java.security.cert.CertSelector;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.jcajce.PKIXCRLStore;
import org.bouncycastle.jcajce.PKIXCertStore;
import org.bouncycastle.jcajce.PKIXCertStoreSelector;

public class PKIXExtendedParameters
implements CertPathParameters {
    public static final int CHAIN_VALIDITY_MODEL = 1;
    public static final int PKIX_VALIDITY_MODEL;
    private final PKIXParameters baseParameters;
    private final Date date;
    private final List<PKIXCRLStore> extraCRLStores;
    private final List<PKIXCertStore> extraCertStores;
    private final Map<GeneralName, PKIXCRLStore> namedCRLStoreMap;
    private final Map<GeneralName, PKIXCertStore> namedCertificateStoreMap;
    private final boolean revocationEnabled;
    private final PKIXCertStoreSelector targetConstraints;
    private final Set<TrustAnchor> trustAnchors;
    private final boolean useDeltas;
    private final int validityModel;

    private PKIXExtendedParameters(Builder builder) {
        this.baseParameters = builder.baseParameters;
        this.date = builder.date;
        this.extraCertStores = Collections.unmodifiableList((List)builder.extraCertStores);
        this.namedCertificateStoreMap = Collections.unmodifiableMap((Map)new HashMap(builder.namedCertificateStoreMap));
        this.extraCRLStores = Collections.unmodifiableList((List)builder.extraCRLStores);
        this.namedCRLStoreMap = Collections.unmodifiableMap((Map)new HashMap(builder.namedCRLStoreMap));
        this.targetConstraints = builder.targetConstraints;
        this.revocationEnabled = builder.revocationEnabled;
        this.useDeltas = builder.useDeltas;
        this.validityModel = builder.validityModel;
        this.trustAnchors = Collections.unmodifiableSet((Set)builder.trustAnchors);
    }

    public Object clone() {
        return this;
    }

    public List<PKIXCRLStore> getCRLStores() {
        return this.extraCRLStores;
    }

    public List getCertPathCheckers() {
        return this.baseParameters.getCertPathCheckers();
    }

    public List<CertStore> getCertStores() {
        return this.baseParameters.getCertStores();
    }

    public List<PKIXCertStore> getCertificateStores() {
        return this.extraCertStores;
    }

    public Date getDate() {
        return new Date(this.date.getTime());
    }

    public Set getInitialPolicies() {
        return this.baseParameters.getInitialPolicies();
    }

    public Map<GeneralName, PKIXCRLStore> getNamedCRLStoreMap() {
        return this.namedCRLStoreMap;
    }

    public Map<GeneralName, PKIXCertStore> getNamedCertificateStoreMap() {
        return this.namedCertificateStoreMap;
    }

    public String getSigProvider() {
        return this.baseParameters.getSigProvider();
    }

    public PKIXCertStoreSelector getTargetConstraints() {
        return this.targetConstraints;
    }

    public Set getTrustAnchors() {
        return this.trustAnchors;
    }

    public int getValidityModel() {
        return this.validityModel;
    }

    public boolean isAnyPolicyInhibited() {
        return this.baseParameters.isAnyPolicyInhibited();
    }

    public boolean isExplicitPolicyRequired() {
        return this.baseParameters.isExplicitPolicyRequired();
    }

    public boolean isPolicyMappingInhibited() {
        return this.baseParameters.isPolicyMappingInhibited();
    }

    public boolean isRevocationEnabled() {
        return this.revocationEnabled;
    }

    public boolean isUseDeltasEnabled() {
        return this.useDeltas;
    }

    public static class Builder {
        private final PKIXParameters baseParameters;
        private final Date date;
        private List<PKIXCRLStore> extraCRLStores = new ArrayList();
        private List<PKIXCertStore> extraCertStores = new ArrayList();
        private Map<GeneralName, PKIXCRLStore> namedCRLStoreMap = new HashMap();
        private Map<GeneralName, PKIXCertStore> namedCertificateStoreMap = new HashMap();
        private boolean revocationEnabled;
        private PKIXCertStoreSelector targetConstraints;
        private Set<TrustAnchor> trustAnchors;
        private boolean useDeltas = false;
        private int validityModel = 0;

        public Builder(PKIXParameters pKIXParameters) {
            Date date;
            this.baseParameters = (PKIXParameters)pKIXParameters.clone();
            CertSelector certSelector = pKIXParameters.getTargetCertConstraints();
            if (certSelector != null) {
                this.targetConstraints = new PKIXCertStoreSelector.Builder(certSelector).build();
            }
            if ((date = pKIXParameters.getDate()) == null) {
                date = new Date();
            }
            this.date = date;
            this.revocationEnabled = pKIXParameters.isRevocationEnabled();
            this.trustAnchors = pKIXParameters.getTrustAnchors();
        }

        public Builder(PKIXExtendedParameters pKIXExtendedParameters) {
            this.baseParameters = pKIXExtendedParameters.baseParameters;
            this.date = pKIXExtendedParameters.date;
            this.targetConstraints = pKIXExtendedParameters.targetConstraints;
            this.extraCertStores = new ArrayList((Collection)pKIXExtendedParameters.extraCertStores);
            this.namedCertificateStoreMap = new HashMap(pKIXExtendedParameters.namedCertificateStoreMap);
            this.extraCRLStores = new ArrayList((Collection)pKIXExtendedParameters.extraCRLStores);
            this.namedCRLStoreMap = new HashMap(pKIXExtendedParameters.namedCRLStoreMap);
            this.useDeltas = pKIXExtendedParameters.useDeltas;
            this.validityModel = pKIXExtendedParameters.validityModel;
            this.revocationEnabled = pKIXExtendedParameters.isRevocationEnabled();
            this.trustAnchors = pKIXExtendedParameters.getTrustAnchors();
        }

        public Builder addCRLStore(PKIXCRLStore pKIXCRLStore) {
            this.extraCRLStores.add((Object)pKIXCRLStore);
            return this;
        }

        public Builder addCertificateStore(PKIXCertStore pKIXCertStore) {
            this.extraCertStores.add((Object)pKIXCertStore);
            return this;
        }

        public Builder addNamedCRLStore(GeneralName generalName, PKIXCRLStore pKIXCRLStore) {
            this.namedCRLStoreMap.put((Object)generalName, (Object)pKIXCRLStore);
            return this;
        }

        public Builder addNamedCertificateStore(GeneralName generalName, PKIXCertStore pKIXCertStore) {
            this.namedCertificateStoreMap.put((Object)generalName, (Object)pKIXCertStore);
            return this;
        }

        public PKIXExtendedParameters build() {
            return new PKIXExtendedParameters(this);
        }

        public void setRevocationEnabled(boolean bl) {
            this.revocationEnabled = bl;
        }

        public Builder setTargetConstraints(PKIXCertStoreSelector pKIXCertStoreSelector) {
            this.targetConstraints = pKIXCertStoreSelector;
            return this;
        }

        public Builder setTrustAnchor(TrustAnchor trustAnchor) {
            this.trustAnchors = Collections.singleton((Object)trustAnchor);
            return this;
        }

        public Builder setTrustAnchors(Set<TrustAnchor> set) {
            this.trustAnchors = set;
            return this;
        }

        public Builder setUseDeltasEnabled(boolean bl) {
            this.useDeltas = bl;
            return this;
        }

        public Builder setValidityModel(int n2) {
            this.validityModel = n2;
            return this;
        }
    }

}

