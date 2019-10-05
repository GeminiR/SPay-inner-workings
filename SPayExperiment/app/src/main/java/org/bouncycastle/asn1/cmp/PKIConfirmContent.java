/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.ASN1Null;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERNull;

public class PKIConfirmContent
extends ASN1Object {
    private ASN1Null val;

    public PKIConfirmContent() {
        this.val = DERNull.INSTANCE;
    }

    private PKIConfirmContent(ASN1Null aSN1Null) {
        this.val = aSN1Null;
    }

    public static PKIConfirmContent getInstance(Object object) {
        if (object == null || object instanceof PKIConfirmContent) {
            return (PKIConfirmContent)object;
        }
        if (object instanceof ASN1Null) {
            return new PKIConfirmContent((ASN1Null)object);
        }
        throw new IllegalArgumentException("Invalid object: " + object.getClass().getName());
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.val;
    }
}
