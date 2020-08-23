package com.sasaj.logoopengles;

import android.opengl.GLES32;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import com.sasaj.logoopengles.objects.CharacterK;
import com.sasaj.logoopengles.objects.CharacterO;
import com.sasaj.logoopengles.objects.Ring;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRenderer implements GLSurfaceView.Renderer {

    private final float[] mMVPMatrix = new float[16];//model view projection matrix
    private final float[] mProjectionMatrix = new float[16];//projection mastrix
    private final float[] mViewMatrix = new float[16];//view matrix
    private final float[] mMVMatrix = new float[16];//model view matrix
    private final float[] mModelMatrix = new float[16];//model  matrix

    private final float zDim = 0.0f;
    private final float near = 1.0f;
    private final float far = 500.0f;
    private final float x = 1.0f;
    private final float y = 1.0f;
    private final float z = 0.0f;

    private Ring ring;
    private CharacterO charO;
    private CharacterK charK;

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        // Set the background frame color to black
        GLES32.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        ring = new Ring();
        charO = new CharacterO();
        charK = new CharacterK();
        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0,
                0.0f, 0f, 50.0f,//camera is at (0,0,1)
                0f, 0f, 0f,//looks at the origin
                0f, 1f, 0.0f);//head is down (set to (0,1,0) to look from the top)
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        // Adjust the view based on view window changes, such as screen rotation
        GLES32.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        float left = -ratio, right = ratio;
        Matrix.frustumM(mProjectionMatrix, 0, left, right, -1.0f, 1.0f, near, far);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {

        long time = SystemClock.uptimeMillis() % 10000L;
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);

        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT | GLES32.GL_DEPTH_BUFFER_BIT);// Draw background color
        GLES32.glClearDepthf(1.0f);//set up the depth buffer
        GLES32.glEnable(GLES32.GL_DEPTH_TEST);//enable depth test (so, it will not look through the surfaces)
        GLES32.glDepthFunc(GLES32.GL_LEQUAL);//indicate what type of depth test

        setIdentitiyMatrices();
        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, x, y, z);
        Matrix.multiplyMM(mMVMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVMatrix, 0);
        ring.draw(mMVPMatrix);

        setIdentitiyMatrices();
        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, x, y, z);
        Matrix.translateM(mModelMatrix, 0, -5.0f, 0.0f, zDim);
        Matrix.multiplyMM(mMVMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVMatrix, 0);
        charO.draw(mMVPMatrix);

        setIdentitiyMatrices();
        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, x, y, z);
        Matrix.translateM(mModelMatrix, 0, 5.0f, 0.0f, zDim);
        Matrix.multiplyMM(mMVMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVMatrix, 0);
        charK.draw(mMVPMatrix);
    }

    private void setIdentitiyMatrices() {
        Matrix.setIdentityM(mMVPMatrix, 0);//set the model view projection matrix to an identity matrix
        Matrix.setIdentityM(mMVMatrix, 0);//set the model view  matrix to an identity matrix
        Matrix.setIdentityM(mModelMatrix, 0);//set the model matrix to an identity matrix
    }

    public static void checkGlError(String glOperation) {
        int error;
        if ((error = GLES32.glGetError()) != GLES32.GL_NO_ERROR) {
            Log.e("MyRenderer", glOperation + ": glError " + error);
        }
    }

    public static int loadShader(int type, String shaderCode) {
        // create a vertex shader  (GLES32.GL_VERTEX_SHADER) or a fragment shader (GLES32.GL_FRAGMENT_SHADER)
        int shader = GLES32.glCreateShader(type);
        GLES32.glShaderSource(shader, shaderCode);// add the source code to the shader and compile it
        GLES32.glCompileShader(shader);
        return shader;
    }
}
