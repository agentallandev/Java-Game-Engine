package com.github.agentallandev.mark;

import com.github.agentallandev.mark.renderer.Shader;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class LevelEditorScene extends Scene{

    private String vertexShaderSrc = "#version 330 core\n" +
            "\n" +
            "layout (location=0) in vec3 aPos;\n" +
            "layout (location=1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main(){\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(aPos, 1.0f);\n" +
            "}\n";
    private String fragmentShaderSrc = "#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main(){\n" +
            "    color = fColor;\n" +
            "}";

    private int vertexId, fragmentID, shaderProgram;

    private float[] vertexArray = {
            //Position                  //Color
             0.5f, -0.5f, 0.0f,         1.0f, 0.0f, 0.0f, 1.0f, // Bottom  Right
            -0.5f,  0.5f, 0.0f,         0.0f, 1.0f, 0.0f, 1.0f, // Top     Left
             0.5f,  0.5f, 0.0f,         0.0f, 0.0f, 1.0f, 1.0f, // Top     Right
            -0.5f, -0.5f, 0.0f,         1.0f, 1.0f, 0.0f, 1.0f, // Bottom  Left
    };

    //Remember to use Counter-Clockwise Order
    private int[] elementArray = {
            2, 1, 0,    //Top Right
            0, 1, 3,    //Bottom Left
    };

    private int vaoID, vboID, eboID;
    private Shader defaultShader;

    public LevelEditorScene(){
    }

    @Override
    public void init(){
        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();

        // ===================================================
        //      Generate VAO, VBO, and EBO || Send to GPU
        // ===================================================
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create float buffer of verticies
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create index buffer
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        // Create EBO
        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add vertex attribute pointers
        int positionSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionSize + colorSize) * floatSizeBytes;
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * floatSizeBytes);
        glEnableVertexAttribArray(1);


    }

    @Override
    public void update(float dt) {
        defaultShader.use();
        // Bind VAO
        glBindVertexArray(vaoID);

        // Enable Vertex Atrib Pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);


        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        defaultShader.detach();

    }
}
