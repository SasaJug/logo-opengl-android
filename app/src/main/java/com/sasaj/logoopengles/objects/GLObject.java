package com.sasaj.logoopengles.objects;

import android.opengl.GLES32;

import com.sasaj.logoopengles.GLRenderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public abstract class GLObject {

    float frontR = 1.0f;
    float frontG = 1.0f;
    float frontB = 1.0f;
    float frontA = 1.0f;

    float backR = 0.502f;
    float backG = 0.502f;
    float backB = 0.502f;
    float backA = 1.0f;


    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static final int COLOR_PER_VERTEX = 4;
    final String vertexShaderCode =
            "attribute vec3 aVertexPosition;" +//vertex of an object
                    " attribute vec4 aVertexColor;" +//the colour  of the object
                    "     uniform mat4 uMVPMatrix;" +//model view  projection matrix
                    "    varying vec4 vColor;" +//variable to be accessed by the fragment shader
                    "    void main() {" +
                    "        gl_Position = uMVPMatrix* vec4(aVertexPosition, 1.0);" +//calculate the position of the vertex
                    "        vColor=aVertexColor;}";//get the colour from the application program
    final String fragmentShaderCode =
            "precision mediump float;" + //define the precision of float
                    "varying vec4 vColor;" + //variable from the vertex shader
                    "void main() {" +
                    "   gl_FragColor = vColor; }";//change the colour based on the variable from the vertex shader
    final FloatBuffer vertexBuffer, colorBuffer;
    final IntBuffer indexBuffer;
    final int mProgram;
    final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    final int colorStride = COLOR_PER_VERTEX * 4;//4 bytes per vertex
    int mPositionHandle, mColorHandle;
    int mMVPMatrixHandle;
    int vertexCount;// number of vertices
    float CharVertex[];
    int CharIndex[];
    float CharColor[];

    {
        initializeArrays();
    }

    GLObject() {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(CharVertex.length * 4);// (# of coordinate values * 4 bytes per float)
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(CharVertex);
        vertexBuffer.position(0);
        vertexCount = CharVertex.length / COORDS_PER_VERTEX;
        ByteBuffer cb = ByteBuffer.allocateDirect(CharColor.length * 4);// (# of coordinate values * 4 bytes per float)
        cb.order(ByteOrder.nativeOrder());
        colorBuffer = cb.asFloatBuffer();
        colorBuffer.put(CharColor);
        colorBuffer.position(0);
        IntBuffer ib = IntBuffer.allocate(CharIndex.length);
        indexBuffer = ib;
        indexBuffer.put(CharIndex);
        indexBuffer.position(0);
        // prepare shaders and OpenGL program
        int vertexShader = GLRenderer.loadShader(GLES32.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = GLRenderer.loadShader(GLES32.GL_FRAGMENT_SHADER, fragmentShaderCode);
        mProgram = GLES32.glCreateProgram();             // create empty OpenGL Program
        GLES32.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES32.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES32.glLinkProgram(mProgram);                  // link the  OpenGL program to create an executable
        GLES32.glUseProgram(mProgram);// Add program to OpenGL environment
        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES32.glGetAttribLocation(mProgram, "aVertexPosition");
        // Enable a handle to the triangle vertices
        GLES32.glEnableVertexAttribArray(mPositionHandle);
        mColorHandle = GLES32.glGetAttribLocation(mProgram, "aVertexColor");
        // Enable a handle to the  colour
        GLES32.glEnableVertexAttribArray(mColorHandle);
        // Prepare the colour coordinate data
        GLES32.glVertexAttribPointer(mColorHandle, COLOR_PER_VERTEX, GLES32.GL_FLOAT, false, colorStride, colorBuffer);
        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES32.glGetUniformLocation(mProgram, "uMVPMatrix");
        GLRenderer.checkGlError("glGetUniformLocation");
    }

    abstract void initializeArrays();

    public void draw(float[] mvpMatrix) {
        GLES32.glUseProgram(mProgram);//use the object's shading programs
        // Apply the projection and view transformation
        GLES32.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        GLRenderer.checkGlError("glUniformMatrix4fv");
        //set the attribute of the vertex to point to the vertex buffer
        GLES32.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES32.GL_FLOAT, false, vertexStride, vertexBuffer);
        GLES32.glVertexAttribPointer(mColorHandle, COORDS_PER_VERTEX,
                GLES32.GL_FLOAT, false, colorStride, colorBuffer);
        GLES32.glDrawElements(GLES32.GL_TRIANGLES, CharIndex.length, GLES32.GL_UNSIGNED_INT, indexBuffer);
    }
}

