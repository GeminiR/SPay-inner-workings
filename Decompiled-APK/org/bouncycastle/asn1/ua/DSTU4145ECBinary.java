package org.bouncycastle.asn1.ua;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.field.PolynomialExtensionField;
import org.bouncycastle.util.Arrays;

public class DSTU4145ECBinary extends ASN1Object {
    ASN1Integer f48a;
    ASN1OctetString f49b;
    ASN1OctetString bp;
    DSTU4145BinaryField f50f;
    ASN1Integer f51n;
    BigInteger version;

    private DSTU4145ECBinary(ASN1Sequence aSN1Sequence) {
        this.version = BigInteger.valueOf(0);
        int i = 0;
        if (aSN1Sequence.getObjectAt(0) instanceof ASN1TaggedObject) {
            ASN1TaggedObject aSN1TaggedObject = (ASN1TaggedObject) aSN1Sequence.getObjectAt(0);
            if (aSN1TaggedObject.isExplicit() && aSN1TaggedObject.getTagNo() == 0) {
                this.version = ASN1Integer.getInstance(aSN1TaggedObject.getLoadedObject()).getValue();
                i = 1;
            } else {
                throw new IllegalArgumentException("object parse error");
            }
        }
        this.f50f = DSTU4145BinaryField.getInstance(aSN1Sequence.getObjectAt(i));
        i++;
        this.f48a = ASN1Integer.getInstance(aSN1Sequence.getObjectAt(i));
        i++;
        this.f49b = ASN1OctetString.getInstance(aSN1Sequence.getObjectAt(i));
        i++;
        this.f51n = ASN1Integer.getInstance(aSN1Sequence.getObjectAt(i));
        this.bp = ASN1OctetString.getInstance(aSN1Sequence.getObjectAt(i + 1));
    }

    public DSTU4145ECBinary(ECDomainParameters eCDomainParameters) {
        this.version = BigInteger.valueOf(0);
        ECCurve curve = eCDomainParameters.getCurve();
        if (ECAlgorithms.isF2mCurve(curve)) {
            int[] exponentsPresent = ((PolynomialExtensionField) curve.getField()).getMinimalPolynomial().getExponentsPresent();
            if (exponentsPresent.length == 3) {
                this.f50f = new DSTU4145BinaryField(exponentsPresent[2], exponentsPresent[1]);
            } else if (exponentsPresent.length == 5) {
                this.f50f = new DSTU4145BinaryField(exponentsPresent[4], exponentsPresent[1], exponentsPresent[2], exponentsPresent[3]);
            }
            this.f48a = new ASN1Integer(curve.getA().toBigInteger());
            this.f49b = new DEROctetString(curve.getB().getEncoded());
            this.f51n = new ASN1Integer(eCDomainParameters.getN());
            this.bp = new DEROctetString(DSTU4145PointEncoder.encodePoint(eCDomainParameters.getG()));
            return;
        }
        throw new IllegalArgumentException("only binary domain is possible");
    }

    public static DSTU4145ECBinary getInstance(Object obj) {
        return obj instanceof DSTU4145ECBinary ? (DSTU4145ECBinary) obj : obj != null ? new DSTU4145ECBinary(ASN1Sequence.getInstance(obj)) : null;
    }

    public BigInteger getA() {
        return this.f48a.getValue();
    }

    public byte[] getB() {
        return Arrays.clone(this.f49b.getOctets());
    }

    public DSTU4145BinaryField getField() {
        return this.f50f;
    }

    public byte[] getG() {
        return Arrays.clone(this.bp.getOctets());
    }

    public BigInteger getN() {
        return this.f51n.getValue();
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.version.compareTo(BigInteger.valueOf(0)) != 0) {
            aSN1EncodableVector.add(new DERTaggedObject(true, 0, new ASN1Integer(this.version)));
        }
        aSN1EncodableVector.add(this.f50f);
        aSN1EncodableVector.add(this.f48a);
        aSN1EncodableVector.add(this.f49b);
        aSN1EncodableVector.add(this.f51n);
        aSN1EncodableVector.add(this.bp);
        return new DERSequence(aSN1EncodableVector);
    }
}
