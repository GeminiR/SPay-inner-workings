/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Enumeration
 */
package org.bouncycastle.asn1.crmf;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x509.Time;

public class OptionalValidity
extends ASN1Object {
    private Time notAfter;
    private Time notBefore;

    private OptionalValidity(ASN1Sequence aSN1Sequence) {
        Enumeration enumeration = aSN1Sequence.getObjects();
        while (enumeration.hasMoreElements()) {
            ASN1TaggedObject aSN1TaggedObject = (ASN1TaggedObject)enumeration.nextElement();
            if (aSN1TaggedObject.getTagNo() == 0) {
                this.notBefore = Time.getInstance(aSN1TaggedObject, true);
                continue;
            }
            this.notAfter = Time.getInstance(aSN1TaggedObject, true);
        }
    }

    public OptionalValidity(Time time, Time time2) {
        if (time == null && time2 == null) {
            throw new IllegalArgumentException("at least one of notBefore/notAfter must not be null.");
        }
        this.notBefore = time;
        this.notAfter = time2;
    }

    public static OptionalValidity getInstance(Object object) {
        if (object instanceof OptionalValidity) {
            return (OptionalValidity)object;
        }
        if (object != null) {
            return new OptionalValidity(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public Time getNotAfter() {
        return this.notAfter;
    }

    public Time getNotBefore() {
        return this.notBefore;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.notBefore != null) {
            aSN1EncodableVector.add(new DERTaggedObject(true, 0, this.notBefore));
        }
        if (this.notAfter != null) {
            aSN1EncodableVector.add(new DERTaggedObject(true, 1, this.notAfter));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}
