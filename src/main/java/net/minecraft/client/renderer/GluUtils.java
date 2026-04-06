package net.minecraft.client.renderer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL11;

public class GluUtils
{
    public static void gluPerspective(float fovy, float aspect, float zNear, float zFar)
    {
        float radians = fovy / 2.0F * (float) Math.PI / 180.0F;
        float deltaZ = zFar - zNear;
        float sine = (float) Math.sin(radians);

        if (deltaZ == 0.0F || sine == 0.0F || aspect == 0.0F)
        {
            return;
        }

        float cotangent = (float) Math.cos(radians) / sine;
        FloatBuffer matrix = GLAllocation.createDirectFloatBuffer(16);
        matrix.put(0, cotangent / aspect);
        matrix.put(1, 0.0F);
        matrix.put(2, 0.0F);
        matrix.put(3, 0.0F);
        matrix.put(4, 0.0F);
        matrix.put(5, cotangent);
        matrix.put(6, 0.0F);
        matrix.put(7, 0.0F);
        matrix.put(8, 0.0F);
        matrix.put(9, 0.0F);
        matrix.put(10, -(zFar + zNear) / deltaZ);
        matrix.put(11, -1.0F);
        matrix.put(12, 0.0F);
        matrix.put(13, 0.0F);
        matrix.put(14, -2.0F * zNear * zFar / deltaZ);
        matrix.put(15, 0.0F);
        GL11.glMultMatrixf(matrix);
    }

    public static String gluErrorString(int errorCode)
    {
        switch (errorCode)
        {
            case GL11.GL_NO_ERROR:
                return "No error";
            case GL11.GL_INVALID_ENUM:
                return "Invalid enum";
            case GL11.GL_INVALID_VALUE:
                return "Invalid value";
            case GL11.GL_INVALID_OPERATION:
                return "Invalid operation";
            case GL11.GL_STACK_OVERFLOW:
                return "Stack overflow";
            case GL11.GL_STACK_UNDERFLOW:
                return "Stack underflow";
            case GL11.GL_OUT_OF_MEMORY:
                return "Out of memory";
            default:
                return "Unknown error (" + errorCode + ")";
        }
    }

    public static boolean gluUnProject(float winX, float winY, float winZ,
                                        FloatBuffer modelview, FloatBuffer projection,
                                        IntBuffer viewport, FloatBuffer objPos)
    {
        float[] mv = new float[16];
        float[] pj = new float[16];
        modelview.position(0);
        modelview.get(mv);
        modelview.position(0);
        projection.position(0);
        projection.get(pj);
        projection.position(0);

        float[] combined = new float[16];
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                combined[i * 4 + j] = 0;
                for (int k = 0; k < 4; k++)
                {
                    combined[i * 4 + j] += pj[k * 4 + j] * mv[i * 4 + k];
                }
            }
        }

        float[] inv = new float[16];
        if (!invertMatrix(combined, inv))
        {
            return false;
        }

        float[] in = new float[4];
        in[0] = (winX - viewport.get(0)) / viewport.get(2) * 2.0F - 1.0F;
        in[1] = (winY - viewport.get(1)) / viewport.get(3) * 2.0F - 1.0F;
        in[2] = winZ * 2.0F - 1.0F;
        in[3] = 1.0F;

        float[] out = new float[4];
        for (int i = 0; i < 4; i++)
        {
            out[i] = inv[0 * 4 + i] * in[0] + inv[1 * 4 + i] * in[1] + inv[2 * 4 + i] * in[2] + inv[3 * 4 + i] * in[3];
        }

        if (out[3] == 0.0F)
        {
            return false;
        }

        out[3] = 1.0F / out[3];
        objPos.position(0);
        objPos.put(0, out[0] * out[3]);
        objPos.put(1, out[1] * out[3]);
        objPos.put(2, out[2] * out[3]);
        return true;
    }

    public static boolean gluProject(float objX, float objY, float objZ,
                                      FloatBuffer modelview, FloatBuffer projection,
                                      IntBuffer viewport, FloatBuffer winPos)
    {
        float[] mv = new float[16];
        float[] pj = new float[16];
        modelview.position(0);
        modelview.get(mv);
        modelview.position(0);
        projection.position(0);
        projection.get(pj);
        projection.position(0);

        float[] in = new float[4];
        in[0] = mv[0] * objX + mv[4] * objY + mv[8] * objZ + mv[12];
        in[1] = mv[1] * objX + mv[5] * objY + mv[9] * objZ + mv[13];
        in[2] = mv[2] * objX + mv[6] * objY + mv[10] * objZ + mv[14];
        in[3] = mv[3] * objX + mv[7] * objY + mv[11] * objZ + mv[15];

        float[] out = new float[4];
        out[0] = pj[0] * in[0] + pj[4] * in[1] + pj[8] * in[2] + pj[12] * in[3];
        out[1] = pj[1] * in[0] + pj[5] * in[1] + pj[9] * in[2] + pj[13] * in[3];
        out[2] = pj[2] * in[0] + pj[6] * in[1] + pj[10] * in[2] + pj[14] * in[3];
        out[3] = pj[3] * in[0] + pj[7] * in[1] + pj[11] * in[2] + pj[15] * in[3];

        if (out[3] == 0.0F)
        {
            return false;
        }

        out[3] = 1.0F / out[3];
        winPos.position(0);
        winPos.put(0, viewport.get(0) + viewport.get(2) * (out[0] * out[3] + 1.0F) / 2.0F);
        winPos.put(1, viewport.get(1) + viewport.get(3) * (out[1] * out[3] + 1.0F) / 2.0F);
        winPos.put(2, (out[2] * out[3] + 1.0F) / 2.0F);
        return true;
    }

    private static boolean invertMatrix(float[] m, float[] inv)
    {
        float[] tmp = new float[16];
        tmp[0] = m[5]*m[10]*m[15] - m[5]*m[11]*m[14] - m[9]*m[6]*m[15] + m[9]*m[7]*m[14] + m[13]*m[6]*m[11] - m[13]*m[7]*m[10];
        tmp[4] = -m[4]*m[10]*m[15] + m[4]*m[11]*m[14] + m[8]*m[6]*m[15] - m[8]*m[7]*m[14] - m[12]*m[6]*m[11] + m[12]*m[7]*m[10];
        tmp[8] = m[4]*m[9]*m[15] - m[4]*m[11]*m[13] - m[8]*m[5]*m[15] + m[8]*m[7]*m[13] + m[12]*m[5]*m[11] - m[12]*m[7]*m[9];
        tmp[12] = -m[4]*m[9]*m[14] + m[4]*m[10]*m[13] + m[8]*m[5]*m[14] - m[8]*m[6]*m[13] - m[12]*m[5]*m[10] + m[12]*m[6]*m[9];
        tmp[1] = -m[1]*m[10]*m[15] + m[1]*m[11]*m[14] + m[9]*m[2]*m[15] - m[9]*m[3]*m[14] - m[13]*m[2]*m[11] + m[13]*m[3]*m[10];
        tmp[5] = m[0]*m[10]*m[15] - m[0]*m[11]*m[14] - m[8]*m[2]*m[15] + m[8]*m[3]*m[14] + m[12]*m[2]*m[11] - m[12]*m[3]*m[10];
        tmp[9] = -m[0]*m[9]*m[15] + m[0]*m[11]*m[13] + m[8]*m[1]*m[15] - m[8]*m[3]*m[13] - m[12]*m[1]*m[11] + m[12]*m[3]*m[9];
        tmp[13] = m[0]*m[9]*m[14] - m[0]*m[10]*m[13] - m[8]*m[1]*m[14] + m[8]*m[2]*m[13] + m[12]*m[1]*m[10] - m[12]*m[2]*m[9];
        tmp[2] = m[1]*m[6]*m[15] - m[1]*m[7]*m[14] - m[5]*m[2]*m[15] + m[5]*m[3]*m[14] + m[13]*m[2]*m[7] - m[13]*m[3]*m[6];
        tmp[6] = -m[0]*m[6]*m[15] + m[0]*m[7]*m[14] + m[4]*m[2]*m[15] - m[4]*m[3]*m[14] - m[12]*m[2]*m[7] + m[12]*m[3]*m[6];
        tmp[10] = m[0]*m[5]*m[15] - m[0]*m[7]*m[13] - m[4]*m[1]*m[15] + m[4]*m[3]*m[13] + m[12]*m[1]*m[7] - m[12]*m[3]*m[5];
        tmp[14] = -m[0]*m[5]*m[14] + m[0]*m[6]*m[13] + m[4]*m[1]*m[14] - m[4]*m[2]*m[13] - m[12]*m[1]*m[6] + m[12]*m[2]*m[5];
        tmp[3] = -m[1]*m[6]*m[11] + m[1]*m[7]*m[10] + m[5]*m[2]*m[11] - m[5]*m[3]*m[10] - m[9]*m[2]*m[7] + m[9]*m[3]*m[6];
        tmp[7] = m[0]*m[6]*m[11] - m[0]*m[7]*m[10] - m[4]*m[2]*m[11] + m[4]*m[3]*m[10] + m[8]*m[2]*m[7] - m[8]*m[3]*m[6];
        tmp[11] = -m[0]*m[5]*m[11] + m[0]*m[7]*m[9] + m[4]*m[1]*m[11] - m[4]*m[3]*m[9] - m[8]*m[1]*m[7] + m[8]*m[3]*m[5];
        tmp[15] = m[0]*m[5]*m[10] - m[0]*m[6]*m[9] - m[4]*m[1]*m[10] + m[4]*m[2]*m[9] + m[8]*m[1]*m[6] - m[8]*m[2]*m[5];

        float det = m[0]*tmp[0] + m[1]*tmp[4] + m[2]*tmp[8] + m[3]*tmp[12];
        if (det == 0.0F) return false;
        det = 1.0F / det;
        for (int i = 0; i < 16; i++) inv[i] = tmp[i] * det;
        return true;
    }
}
