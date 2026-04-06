package net.minecraft.util;

public class Matrix4f
{
    public float m00, m01, m02, m03;
    public float m10, m11, m12, m13;
    public float m20, m21, m22, m23;
    public float m30, m31, m32, m33;

    public Matrix4f(float[] p_i46413_1_)
    {
        this.m00 = p_i46413_1_[0];
        this.m01 = p_i46413_1_[1];
        this.m02 = p_i46413_1_[2];
        this.m03 = p_i46413_1_[3];
        this.m10 = p_i46413_1_[4];
        this.m11 = p_i46413_1_[5];
        this.m12 = p_i46413_1_[6];
        this.m13 = p_i46413_1_[7];
        this.m20 = p_i46413_1_[8];
        this.m21 = p_i46413_1_[9];
        this.m22 = p_i46413_1_[10];
        this.m23 = p_i46413_1_[11];
        this.m30 = p_i46413_1_[12];
        this.m31 = p_i46413_1_[13];
        this.m32 = p_i46413_1_[14];
        this.m33 = p_i46413_1_[15];
    }

    public Matrix4f()
    {
        this.m00 = this.m01 = this.m02 = this.m03 = this.m10 = this.m11 = this.m12 = this.m13 = this.m20 = this.m21 = this.m22 = this.m23 = this.m30 = this.m31 = this.m32 = this.m33 = 0.0F;
    }

    public Matrix4f setIdentity()
    {
        this.m00 = 1.0F; this.m01 = 0.0F; this.m02 = 0.0F; this.m03 = 0.0F;
        this.m10 = 0.0F; this.m11 = 1.0F; this.m12 = 0.0F; this.m13 = 0.0F;
        this.m20 = 0.0F; this.m21 = 0.0F; this.m22 = 1.0F; this.m23 = 0.0F;
        this.m30 = 0.0F; this.m31 = 0.0F; this.m32 = 0.0F; this.m33 = 1.0F;
        return this;
    }

    public static Matrix4f rotate(float angle, org.joml.Vector3f axis, Matrix4f src, Matrix4f dest)
    {
        if (dest == null) dest = new Matrix4f();
        float c = (float) Math.cos(angle);
        float s = (float) Math.sin(angle);
        float oneminusc = 1.0F - c;
        float xy = axis.x * axis.y;
        float yz = axis.y * axis.z;
        float xz = axis.x * axis.z;
        float xs = axis.x * s;
        float ys = axis.y * s;
        float zs = axis.z * s;

        float f00 = axis.x * axis.x * oneminusc + c;
        float f01 = xy * oneminusc + zs;
        float f02 = xz * oneminusc - ys;
        float f10 = xy * oneminusc - zs;
        float f11 = axis.y * axis.y * oneminusc + c;
        float f12 = yz * oneminusc + xs;
        float f20 = xz * oneminusc + ys;
        float f21 = yz * oneminusc - xs;
        float f22 = axis.z * axis.z * oneminusc + c;

        float t00 = src.m00 * f00 + src.m10 * f01 + src.m20 * f02;
        float t01 = src.m01 * f00 + src.m11 * f01 + src.m21 * f02;
        float t02 = src.m02 * f00 + src.m12 * f01 + src.m22 * f02;
        float t03 = src.m03 * f00 + src.m13 * f01 + src.m23 * f02;
        float t10 = src.m00 * f10 + src.m10 * f11 + src.m20 * f12;
        float t11 = src.m01 * f10 + src.m11 * f11 + src.m21 * f12;
        float t12 = src.m02 * f10 + src.m12 * f11 + src.m22 * f12;
        float t13 = src.m03 * f10 + src.m13 * f11 + src.m23 * f12;

        dest.m20 = src.m00 * f20 + src.m10 * f21 + src.m20 * f22;
        dest.m21 = src.m01 * f20 + src.m11 * f21 + src.m21 * f22;
        dest.m22 = src.m02 * f20 + src.m12 * f21 + src.m22 * f22;
        dest.m23 = src.m03 * f20 + src.m13 * f21 + src.m23 * f22;
        dest.m00 = t00; dest.m01 = t01; dest.m02 = t02; dest.m03 = t03;
        dest.m10 = t10; dest.m11 = t11; dest.m12 = t12; dest.m13 = t13;
        dest.m30 = src.m30; dest.m31 = src.m31; dest.m32 = src.m32; dest.m33 = src.m33;
        return dest;
    }

    public static Matrix4f mul(Matrix4f left, Matrix4f right, Matrix4f dest)
    {
        if (dest == null) dest = new Matrix4f();

        float nm00 = left.m00 * right.m00 + left.m10 * right.m01 + left.m20 * right.m02 + left.m30 * right.m03;
        float nm01 = left.m01 * right.m00 + left.m11 * right.m01 + left.m21 * right.m02 + left.m31 * right.m03;
        float nm02 = left.m02 * right.m00 + left.m12 * right.m01 + left.m22 * right.m02 + left.m32 * right.m03;
        float nm03 = left.m03 * right.m00 + left.m13 * right.m01 + left.m23 * right.m02 + left.m33 * right.m03;
        float nm10 = left.m00 * right.m10 + left.m10 * right.m11 + left.m20 * right.m12 + left.m30 * right.m13;
        float nm11 = left.m01 * right.m10 + left.m11 * right.m11 + left.m21 * right.m12 + left.m31 * right.m13;
        float nm12 = left.m02 * right.m10 + left.m12 * right.m11 + left.m22 * right.m12 + left.m32 * right.m13;
        float nm13 = left.m03 * right.m10 + left.m13 * right.m11 + left.m23 * right.m12 + left.m33 * right.m13;
        float nm20 = left.m00 * right.m20 + left.m10 * right.m21 + left.m20 * right.m22 + left.m30 * right.m23;
        float nm21 = left.m01 * right.m20 + left.m11 * right.m21 + left.m21 * right.m22 + left.m31 * right.m23;
        float nm22 = left.m02 * right.m20 + left.m12 * right.m21 + left.m22 * right.m22 + left.m32 * right.m23;
        float nm23 = left.m03 * right.m20 + left.m13 * right.m21 + left.m23 * right.m22 + left.m33 * right.m23;
        float nm30 = left.m00 * right.m30 + left.m10 * right.m31 + left.m20 * right.m32 + left.m30 * right.m33;
        float nm31 = left.m01 * right.m30 + left.m11 * right.m31 + left.m21 * right.m32 + left.m31 * right.m33;
        float nm32 = left.m02 * right.m30 + left.m12 * right.m31 + left.m22 * right.m32 + left.m32 * right.m33;
        float nm33 = left.m03 * right.m30 + left.m13 * right.m31 + left.m23 * right.m32 + left.m33 * right.m33;

        dest.m00 = nm00; dest.m01 = nm01; dest.m02 = nm02; dest.m03 = nm03;
        dest.m10 = nm10; dest.m11 = nm11; dest.m12 = nm12; dest.m13 = nm13;
        dest.m20 = nm20; dest.m21 = nm21; dest.m22 = nm22; dest.m23 = nm23;
        dest.m30 = nm30; dest.m31 = nm31; dest.m32 = nm32; dest.m33 = nm33;
        return dest;
    }

    public static org.joml.Vector4f transform(Matrix4f left, org.joml.Vector4f right, org.joml.Vector4f dest)
    {
        if (dest == null) dest = new org.joml.Vector4f();
        float x = left.m00 * right.x + left.m10 * right.y + left.m20 * right.z + left.m30 * right.w;
        float y = left.m01 * right.x + left.m11 * right.y + left.m21 * right.z + left.m31 * right.w;
        float z = left.m02 * right.x + left.m12 * right.y + left.m22 * right.z + left.m32 * right.w;
        float w = left.m03 * right.x + left.m13 * right.y + left.m23 * right.z + left.m33 * right.w;
        dest.x = x; dest.y = y; dest.z = z; dest.w = w;
        return dest;
    }

    public Matrix4f transpose()
    {
        float t01 = m10; float t02 = m20; float t03 = m30;
        float t12 = m21; float t13 = m31;
        float t23 = m32;
        m10 = m01; m20 = m02; m30 = m03;
        m01 = t01; m21 = m12; m31 = m13;
        m02 = t02; m12 = t12; m32 = m23;
        m03 = t03; m13 = t13; m23 = t23;
        return this;
    }

    public Matrix4f invert()
    {
        float a = m00 * m11 - m01 * m10;
        float b = m00 * m12 - m02 * m10;
        float c = m00 * m13 - m03 * m10;
        float d = m01 * m12 - m02 * m11;
        float e = m01 * m13 - m03 * m11;
        float f = m02 * m13 - m03 * m12;
        float g = m20 * m31 - m21 * m30;
        float h = m20 * m32 - m22 * m30;
        float i = m20 * m33 - m23 * m30;
        float j = m21 * m32 - m22 * m31;
        float k = m21 * m33 - m23 * m31;
        float l = m22 * m33 - m23 * m32;
        float det = a * l - b * k + c * j + d * i - e * h + f * g;
        if (det == 0.0F) return this;
        float invDet = 1.0F / det;
        float nm00 = ( m11 * l - m12 * k + m13 * j) * invDet;
        float nm01 = (-m01 * l + m02 * k - m03 * j) * invDet;
        float nm02 = ( m31 * f - m32 * e + m33 * d) * invDet;
        float nm03 = (-m21 * f + m22 * e - m23 * d) * invDet;
        float nm10 = (-m10 * l + m12 * i - m13 * h) * invDet;
        float nm11 = ( m00 * l - m02 * i + m03 * h) * invDet;
        float nm12 = (-m30 * f + m32 * c - m33 * b) * invDet;
        float nm13 = ( m20 * f - m22 * c + m23 * b) * invDet;
        float nm20 = ( m10 * k - m11 * i + m13 * g) * invDet;
        float nm21 = (-m00 * k + m01 * i - m03 * g) * invDet;
        float nm22 = ( m30 * e - m31 * c + m33 * a) * invDet;
        float nm23 = (-m20 * e + m21 * c - m23 * a) * invDet;
        float nm30 = (-m10 * j + m11 * h - m12 * g) * invDet;
        float nm31 = ( m00 * j - m01 * h + m02 * g) * invDet;
        float nm32 = (-m30 * d + m31 * b - m32 * a) * invDet;
        float nm33 = ( m20 * d - m21 * b + m22 * a) * invDet;
        m00 = nm00; m01 = nm01; m02 = nm02; m03 = nm03;
        m10 = nm10; m11 = nm11; m12 = nm12; m13 = nm13;
        m20 = nm20; m21 = nm21; m22 = nm22; m23 = nm23;
        m30 = nm30; m31 = nm31; m32 = nm32; m33 = nm33;
        return this;
    }
}
