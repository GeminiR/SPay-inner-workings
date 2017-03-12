package org.bouncycastle.math.ec.custom.sec;

import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.ECPoint.AbstractFp;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.math.raw.Nat224;

public class SecP224R1Point extends AbstractFp {
    public SecP224R1Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
        this(eCCurve, eCFieldElement, eCFieldElement2, false);
    }

    public SecP224R1Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, boolean z) {
        Object obj = 1;
        super(eCCurve, eCFieldElement, eCFieldElement2);
        Object obj2 = eCFieldElement == null ? 1 : null;
        if (eCFieldElement2 != null) {
            obj = null;
        }
        if (obj2 != obj) {
            throw new IllegalArgumentException("Exactly one of the field elements is null");
        }
        this.withCompression = z;
    }

    SecP224R1Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] eCFieldElementArr, boolean z) {
        super(eCCurve, eCFieldElement, eCFieldElement2, eCFieldElementArr);
        this.withCompression = z;
    }

    public ECPoint add(ECPoint eCPoint) {
        if (isInfinity()) {
            return eCPoint;
        }
        if (eCPoint.isInfinity()) {
            return this;
        }
        if (this == eCPoint) {
            return twice();
        }
        int[] iArr;
        int[] iArr2;
        int[] iArr3;
        int[] iArr4;
        int[] iArr5;
        ECCurve curve = getCurve();
        SecP224R1FieldElement secP224R1FieldElement = (SecP224R1FieldElement) this.x;
        SecP224R1FieldElement secP224R1FieldElement2 = (SecP224R1FieldElement) this.y;
        SecP224R1FieldElement secP224R1FieldElement3 = (SecP224R1FieldElement) eCPoint.getXCoord();
        SecP224R1FieldElement secP224R1FieldElement4 = (SecP224R1FieldElement) eCPoint.getYCoord();
        SecP224R1FieldElement secP224R1FieldElement5 = (SecP224R1FieldElement) this.zs[0];
        SecP224R1FieldElement secP224R1FieldElement6 = (SecP224R1FieldElement) eCPoint.getZCoord(0);
        int[] createExt = Nat224.createExt();
        int[] create = Nat224.create();
        int[] create2 = Nat224.create();
        int[] create3 = Nat224.create();
        boolean isOne = secP224R1FieldElement5.isOne();
        if (isOne) {
            iArr = secP224R1FieldElement3.f359x;
            iArr2 = secP224R1FieldElement4.f359x;
            iArr3 = iArr;
        } else {
            SecP224R1Field.square(secP224R1FieldElement5.f359x, create2);
            SecP224R1Field.multiply(create2, secP224R1FieldElement3.f359x, create);
            SecP224R1Field.multiply(create2, secP224R1FieldElement5.f359x, create2);
            SecP224R1Field.multiply(create2, secP224R1FieldElement4.f359x, create2);
            iArr2 = create2;
            iArr3 = create;
        }
        boolean isOne2 = secP224R1FieldElement6.isOne();
        if (isOne2) {
            iArr = secP224R1FieldElement.f359x;
            iArr4 = secP224R1FieldElement2.f359x;
            iArr5 = iArr;
        } else {
            SecP224R1Field.square(secP224R1FieldElement6.f359x, create3);
            SecP224R1Field.multiply(create3, secP224R1FieldElement.f359x, createExt);
            SecP224R1Field.multiply(create3, secP224R1FieldElement6.f359x, create3);
            SecP224R1Field.multiply(create3, secP224R1FieldElement2.f359x, create3);
            iArr4 = create3;
            iArr5 = createExt;
        }
        iArr = Nat224.create();
        SecP224R1Field.subtract(iArr5, iArr3, iArr);
        SecP224R1Field.subtract(iArr4, iArr2, create);
        if (Nat224.isZero(iArr)) {
            return Nat224.isZero(create) ? twice() : curve.getInfinity();
        } else {
            SecP224R1Field.square(iArr, create2);
            iArr3 = Nat224.create();
            SecP224R1Field.multiply(create2, iArr, iArr3);
            SecP224R1Field.multiply(create2, iArr5, create2);
            SecP224R1Field.negate(iArr3, iArr3);
            Nat224.mul(iArr4, iArr3, createExt);
            SecP224R1Field.reduce32(Nat224.addBothTo(create2, create2, iArr3), iArr3);
            ECFieldElement secP224R1FieldElement7 = new SecP224R1FieldElement(create3);
            SecP224R1Field.square(create, secP224R1FieldElement7.f359x);
            SecP224R1Field.subtract(secP224R1FieldElement7.f359x, iArr3, secP224R1FieldElement7.f359x);
            ECFieldElement secP224R1FieldElement8 = new SecP224R1FieldElement(iArr3);
            SecP224R1Field.subtract(create2, secP224R1FieldElement7.f359x, secP224R1FieldElement8.f359x);
            SecP224R1Field.multiplyAddToExt(secP224R1FieldElement8.f359x, create, createExt);
            SecP224R1Field.reduce(createExt, secP224R1FieldElement8.f359x);
            secP224R1FieldElement = new SecP224R1FieldElement(iArr);
            if (!isOne) {
                SecP224R1Field.multiply(secP224R1FieldElement.f359x, secP224R1FieldElement5.f359x, secP224R1FieldElement.f359x);
            }
            if (!isOne2) {
                SecP224R1Field.multiply(secP224R1FieldElement.f359x, secP224R1FieldElement6.f359x, secP224R1FieldElement.f359x);
            }
            return new SecP224R1Point(curve, secP224R1FieldElement7, secP224R1FieldElement8, new ECFieldElement[]{secP224R1FieldElement}, this.withCompression);
        }
    }

    protected ECPoint detach() {
        return new SecP224R1Point(null, getAffineXCoord(), getAffineYCoord());
    }

    public ECPoint negate() {
        return isInfinity() ? this : new SecP224R1Point(this.curve, this.x, this.y.negate(), this.zs, this.withCompression);
    }

    public ECPoint threeTimes() {
        return (isInfinity() || this.y.isZero()) ? this : twice().add(this);
    }

    public ECPoint twice() {
        if (isInfinity()) {
            return this;
        }
        ECCurve curve = getCurve();
        SecP224R1FieldElement secP224R1FieldElement = (SecP224R1FieldElement) this.y;
        if (secP224R1FieldElement.isZero()) {
            return curve.getInfinity();
        }
        SecP224R1FieldElement secP224R1FieldElement2 = (SecP224R1FieldElement) this.x;
        SecP224R1FieldElement secP224R1FieldElement3 = (SecP224R1FieldElement) this.zs[0];
        int[] create = Nat224.create();
        int[] create2 = Nat224.create();
        int[] create3 = Nat224.create();
        SecP224R1Field.square(secP224R1FieldElement.f359x, create3);
        int[] create4 = Nat224.create();
        SecP224R1Field.square(create3, create4);
        boolean isOne = secP224R1FieldElement3.isOne();
        int[] iArr = secP224R1FieldElement3.f359x;
        if (!isOne) {
            SecP224R1Field.square(secP224R1FieldElement3.f359x, create2);
            iArr = create2;
        }
        SecP224R1Field.subtract(secP224R1FieldElement2.f359x, iArr, create);
        SecP224R1Field.add(secP224R1FieldElement2.f359x, iArr, create2);
        SecP224R1Field.multiply(create2, create, create2);
        SecP224R1Field.reduce32(Nat224.addBothTo(create2, create2, create2), create2);
        SecP224R1Field.multiply(create3, secP224R1FieldElement2.f359x, create3);
        SecP224R1Field.reduce32(Nat.shiftUpBits(7, create3, 2, 0), create3);
        SecP224R1Field.reduce32(Nat.shiftUpBits(7, create4, 3, 0, create), create);
        ECFieldElement secP224R1FieldElement4 = new SecP224R1FieldElement(create4);
        SecP224R1Field.square(create2, secP224R1FieldElement4.f359x);
        SecP224R1Field.subtract(secP224R1FieldElement4.f359x, create3, secP224R1FieldElement4.f359x);
        SecP224R1Field.subtract(secP224R1FieldElement4.f359x, create3, secP224R1FieldElement4.f359x);
        ECFieldElement secP224R1FieldElement5 = new SecP224R1FieldElement(create3);
        SecP224R1Field.subtract(create3, secP224R1FieldElement4.f359x, secP224R1FieldElement5.f359x);
        SecP224R1Field.multiply(secP224R1FieldElement5.f359x, create2, secP224R1FieldElement5.f359x);
        SecP224R1Field.subtract(secP224R1FieldElement5.f359x, create, secP224R1FieldElement5.f359x);
        SecP224R1FieldElement secP224R1FieldElement6 = new SecP224R1FieldElement(create2);
        SecP224R1Field.twice(secP224R1FieldElement.f359x, secP224R1FieldElement6.f359x);
        if (!isOne) {
            SecP224R1Field.multiply(secP224R1FieldElement6.f359x, secP224R1FieldElement3.f359x, secP224R1FieldElement6.f359x);
        }
        return new SecP224R1Point(curve, secP224R1FieldElement4, secP224R1FieldElement5, new ECFieldElement[]{secP224R1FieldElement6}, this.withCompression);
    }

    public ECPoint twicePlus(ECPoint eCPoint) {
        return this == eCPoint ? threeTimes() : !isInfinity() ? eCPoint.isInfinity() ? twice() : !this.y.isZero() ? twice().add(eCPoint) : eCPoint : eCPoint;
    }
}
