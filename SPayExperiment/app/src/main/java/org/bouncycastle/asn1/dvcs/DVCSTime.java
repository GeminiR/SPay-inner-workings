/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Date
 */
package org.bouncycastle.asn1.dvcs;

import java.util.Date;
import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1GeneralizedTime;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.cms.ContentInfo;

public class DVCSTime
extends ASN1Object
implements ASN1Choice {
    private ASN1GeneralizedTime genTime;
    private Date time;
    private ContentInfo timeStampToken;

    public DVCSTime(Date date) {
        this(new ASN1GeneralizedTime(date));
    }

    public DVCSTime(ASN1GeneralizedTime aSN1GeneralizedTime) {
        this.genTime = aSN1GeneralizedTime;
    }

    public DVCSTime(ContentInfo contentInfo) {
        this.timeStampToken = contentInfo;
    }

    public static DVCSTime getInstance(Object object) {
        if (object instanceof DVCSTime) {
            return (DVCSTime)object;
        }
        if (object instanceof ASN1GeneralizedTime) {
            return new DVCSTime(ASN1GeneralizedTime.getInstance(object));
        }
        if (object != null) {
            return new DVCSTime(ContentInfo.getInstance(object));
        }
        return null;
    }

    public static DVCSTime getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return DVCSTime.getInstance(aSN1TaggedObject.getObject());
    }

    public ASN1GeneralizedTime getGenTime() {
        return this.genTime;
    }

    public ContentInfo getTimeStampToken() {
        return this.timeStampToken;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        if (this.genTime != null) {
            return this.genTime;
        }
        if (this.timeStampToken != null) {
            return this.timeStampToken.toASN1Primitive();
        }
        return null;
    }

    public String toString() {
        if (this.genTime != null) {
            return this.genTime.toString();
        }
        if (this.timeStampToken != null) {
            return this.timeStampToken.toString();
        }
        return null;
    }
}

