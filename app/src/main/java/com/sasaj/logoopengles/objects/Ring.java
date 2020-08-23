package com.sasaj.logoopengles.objects;

import java.util.Arrays;

public class Ring extends GLObject {
    private float outerRadius;
    private float innerRadius;
    private float width;
    private int resolution;

    @Override
    void initializeArrays() {
        outerRadius = 15.0f;
        innerRadius = 12.0f;
        width = 2.0f;
        resolution = 1;
        calculateVertices();
        calculateIndices();
        calculateColors();
    }

    private void calculateVertices() {
        float vertices[] = new float[65535];
        int vindex = 0;

        float zFront = width / 2;
        float zBack = -width / 2;

        //First quadrant
        for (float i = 0.0f; i < 90; i++) {
            float angle = i / 180.f * (float) Math.PI;

            float xOuter = outerRadius * (float) Math.sin(angle);
            float yOuter = outerRadius * (float) Math.cos(angle);

            float xInner = innerRadius * (float) Math.sin(angle);
            float yInner = innerRadius * (float) Math.cos(angle);

            vertices[vindex++] = xOuter;
            vertices[vindex++] = yOuter;
            vertices[vindex++] = zFront;

            vertices[vindex++] = xInner;
            vertices[vindex++] = yInner;
            vertices[vindex++] = zFront;

            vertices[vindex++] = xOuter;
            vertices[vindex++] = yOuter;
            vertices[vindex++] = zBack;

            vertices[vindex++] = xInner;
            vertices[vindex++] = yInner;
            vertices[vindex++] = zBack;
        }

        //Second quadrant
        for (float i = 0.0f; i < 90; i++) {
            float angle = i / 180.f * (float) Math.PI;

            float xOuter = outerRadius * (float) Math.cos(angle);
            float yOuter = outerRadius * (float) Math.sin(angle);

            float xInner = innerRadius * (float) Math.cos(angle);
            float yInner = innerRadius * (float) Math.sin(angle);

            vertices[vindex++] = xOuter;
            vertices[vindex++] = -yOuter;
            vertices[vindex++] = zFront;

            vertices[vindex++] = xInner;
            vertices[vindex++] = -yInner;
            vertices[vindex++] = zFront;

            vertices[vindex++] = xOuter;
            vertices[vindex++] = -yOuter;
            vertices[vindex++] = zBack;

            vertices[vindex++] = xInner;
            vertices[vindex++] = -yInner;
            vertices[vindex++] = zBack;
        }

        //Third quadrant
        for (float i = 0.0f; i < 90; i++) {
            float angle = i / 180.f * (float) Math.PI;

            float xOuter = outerRadius * (float) Math.sin(angle);
            float yOuter = outerRadius * (float) Math.cos(angle);

            float xInner = innerRadius * (float) Math.sin(angle);
            float yInner = innerRadius * (float) Math.cos(angle);

            vertices[vindex++] = -xOuter;
            vertices[vindex++] = -yOuter;
            vertices[vindex++] = zFront;

            vertices[vindex++] = -xInner;
            vertices[vindex++] = -yInner;
            vertices[vindex++] = zFront;

            vertices[vindex++] = -xOuter;
            vertices[vindex++] = -yOuter;
            vertices[vindex++] = zBack;

            vertices[vindex++] = -xInner;
            vertices[vindex++] = -yInner;
            vertices[vindex++] = zBack;
        }

        //Fourth quadrant
        for (float i = 0.0f; i < 90; i++) {
            float angle = i / 180.f * (float) Math.PI;

            float xOuter = outerRadius * (float) Math.cos(angle);
            float yOuter = outerRadius * (float) Math.sin(angle);

            float xInner = innerRadius * (float) Math.cos(angle);
            float yInner = innerRadius * (float) Math.sin(angle);

            vertices[vindex++] = -xOuter;
            vertices[vindex++] = yOuter;
            vertices[vindex++] = zFront;

            vertices[vindex++] = -xInner;
            vertices[vindex++] = yInner;
            vertices[vindex++] = zFront;

            vertices[vindex++] = -xOuter;
            vertices[vindex++] = yOuter;
            vertices[vindex++] = zBack;

            vertices[vindex++] = -xInner;
            vertices[vindex++] = yInner;
            vertices[vindex++] = zBack;
        }
        CharVertex = Arrays.copyOf(vertices, vindex);
    }

    private void calculateIndices() {
        int indices[] = new int[65535];
        int iindex = 0;
        int v0, v1, v2, v3, v4, v5, v6, v7;

        for (v0 = 0, v1 = 1, v2 = 4, v3 = 5, v4 = 2, v5 = 3, v6 = 6, v7 = 7; v7 < (CharVertex.length - 1) / 3; v0 += 4, v1 += 4, v2 += 4, v3 += 4, v4 += 4, v5 += 4, v6 += 4, v7 += 4) {
            //the front
            indices[iindex++] = v0;
            indices[iindex++] = v1;
            indices[iindex++] = v2;
            indices[iindex++] = v2;
            indices[iindex++] = v3;
            indices[iindex++] = v1;

            //back
            indices[iindex++] = v4;
            indices[iindex++] = v5;
            indices[iindex++] = v6;
            indices[iindex++] = v6;
            indices[iindex++] = v7;
            indices[iindex++] = v5;

            //bottom
            indices[iindex++] = v1;
            indices[iindex++] = v5;
            indices[iindex++] = v3;
            indices[iindex++] = v3;
            indices[iindex++] = v7;
            indices[iindex++] = v5;

            //top
            indices[iindex++] = v0;
            indices[iindex++] = v4;
            indices[iindex++] = v2;
            indices[iindex++] = v2;
            indices[iindex++] = v6;
            indices[iindex++] = v4;
        }

        //the front
        indices[iindex++] = v2 - 4;
        indices[iindex++] = v3 - 4;
        indices[iindex++] = 0;
        indices[iindex++] = 0;
        indices[iindex++] = 1;
        indices[iindex++] = v3 - 4;

        //back
        indices[iindex++] = v6 - 4;
        indices[iindex++] = v7 - 4;
        indices[iindex++] = 2;
        indices[iindex++] = 2;
        indices[iindex++] = 3;
        indices[iindex++] = v7 - 4;

        //bottom
        indices[iindex++] = v3 - 4;
        indices[iindex++] = v7 - 4;
        indices[iindex++] = 1;
        indices[iindex++] = 1;
        indices[iindex++] = 3;
        indices[iindex++] = v7 - 4;

        //top
        indices[iindex++] = v2 - 4;
        indices[iindex++] = v6 - 4;
        indices[iindex++] = 0;
        indices[iindex++] = 0;
        indices[iindex++] = 2;
        indices[iindex++] = v6 - 4;

        CharIndex = Arrays.copyOf(indices, iindex);
    }

    private void calculateColors() {
        float colors[] = new float[65535];
        int colorIndex = 0;

        for (int i = 0; i < CharVertex.length; i = i + 3) {
            colors[colorIndex++] = frontR;
            colors[colorIndex++] = frontG;
            colors[colorIndex++] = frontB;
            colors[colorIndex++] = frontA;
        }

        CharColor = Arrays.copyOf(colors, colorIndex);
    }
}
