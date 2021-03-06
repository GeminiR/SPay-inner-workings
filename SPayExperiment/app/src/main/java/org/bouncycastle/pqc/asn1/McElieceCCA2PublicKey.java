/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 */
package org.bouncycastle.pqc.asn1;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.pqc.math.linearalgebra.GF2Matrix;

public class McElieceCCA2PublicKey
extends ASN1Object {
    private byte[] matrixG;
    private int n;
    private ASN1ObjectIdentifier oid;
    private int t;

    public McElieceCCA2PublicKey(ASN1ObjectIdentifier aSN1ObjectIdentifier, int n, int n2, GF2Matrix gF2Matrix) {
        this.oid = aSN1ObjectIdentifier;
        this.n = n;
        this.t = n2;
        this.matrixG = gF2Matrix.getEncoded();
    }

    private McElieceCCA2PublicKey(ASN1Sequence aSN1Sequence) {
        this.oid = (ASN1ObjectIdentifier)aSN1Sequence.getObjectAt(0);
        this.n = ((ASN1Integer)aSN1Sequence.getObjectAt(1)).getValue().intValue();
        this.t = ((ASN1Integer)aSN1Sequence.getObjectAt(2)).getValue().intValue();
        this.matrixG = ((ASN1OctetString)aSN1Sequence.getObjectAt(3)).getOctets();
    }

    public static McElieceCCA2PublicKey getInstance(Object object) {
        if (object instanceof McElieceCCA2PublicKey) {
            return (McElieceCCA2PublicKey)object;
        }
        if (object != null) {
            return new McElieceCCA2PublicKey(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public GF2Matrix getG() {
        return new GF2Matrix(this.matrixG);
    }

    public int getN() {
        return this.n;
    }

    public ASN1ObjectIdentifier getOID() {
        return this.oid;
    }

    public int getT() {
        return this.t;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.oid);
        aSN1EncodableVector.add(new ASN1Integer(this.n));
        aSN1EncodableVector.add(new ASN1Integer(this.t));
        aSN1EncodableVector.add(new DEROctetString(this.matrixG));
        return new DERSequence(aSN1EncodableVector);
    }
}

