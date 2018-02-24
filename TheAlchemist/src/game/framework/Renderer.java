package game.framework;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import game.Camera;

public class Renderer {
	
	private ShaderProgram shader;
	private Camera camera;
	
	public Renderer(Camera camera) {
		try {
			shader = new ShaderProgram();
			shader.createVertexShader(
				"#version 330 core\n" +
				"uniform mat4 camera; " +
				"layout (location = 0) in vec3 aPos; " +
				"layout (location = 1) in vec3 aColor; " +
				"layout (location = 2) in vec2 aTexCoord; " +
				"out vec3 ourColor; " +
				"out vec2 TexCoord; " +
				"void main() { " +
				"gl_Position = camera * vec4(aPos, 1.0); " +
				"ourColor = aColor; " +
				"TexCoord = aTexCoord; }");
			shader.createFragmentShader(
				"#version 330 core\n" +
				"out vec4 FragColor; " +
				"in vec3 ourColor; " +
				"in vec2 TexCoord; " +
				"// texture sampler\n" +
				"uniform sampler2D texture1; " +
				"void main() { " +
				"FragColor = texture(texture1, TexCoord); }");
			shader.link();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.camera = camera;
	}
	
	public void init(IRenderable obj) {
		if (obj == null) return;

		float[] vertices = obj.init(glGenVertexArrays());
		FloatBuffer vBuffer = MemoryUtil.memAllocFloat(vertices.length);
		vBuffer.put(vertices).flip();
		
		int[] indices = new int[] {
				0, 1, 2,
				1, 2, 3
		};		
		IntBuffer iBuffer = MemoryUtil.memAllocInt(indices.length);
		iBuffer.put(indices).flip();
		
		glBindVertexArray(obj.getVao());
		
		glBindBuffer(GL_ARRAY_BUFFER, obj.getVbo());
	    glBufferData(GL_ARRAY_BUFFER, vBuffer, GL_STATIC_DRAW);
	    MemoryUtil.memFree(vBuffer);
	
	    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, obj.getEbo());
	    glBufferData(GL_ELEMENT_ARRAY_BUFFER, iBuffer, GL_STATIC_DRAW);
	    MemoryUtil.memFree(iBuffer);
	
		// vertex attribute
	    glVertexAttribPointer(0, 3, GL_FLOAT, false, 32, 0);
	    // color attribute
	    glVertexAttribPointer(1, 3, GL_FLOAT, false, 32, 12);
	    // texture coord attribute
	    glVertexAttribPointer(2, 2, GL_FLOAT, false, 32, 24);
	
	    glBindBuffer(GL_ARRAY_BUFFER, 0);
	    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	    glBindVertexArray(0);
	}
	
	public void render(IRenderable obj) {
		if (obj == null) return;
		
		shader.bind();
		
		shader.setUniformMatrix4("camera", false, camera.getCamera());
		glBindVertexArray(obj.getVao());
		glBindTexture(GL11.GL_TEXTURE_2D, obj.getTexture());
		glBindBuffer(GL_ARRAY_BUFFER, obj.getVbo());
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, obj.getEbo());
	    glEnableVertexAttribArray(0);
	  	glEnableVertexAttribArray(1);
	  	glEnableVertexAttribArray(2);
	  	glEnable(GL_BLEND);
	    glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
		glDisable(GL_BLEND);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindTexture(GL11.GL_TEXTURE_2D, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
		
		shader.unbind();
	}
	
	public void cleanup(IRenderable obj) {
		if (obj == null) return;
		if (shader != null) shader.cleanup();
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
		glDeleteVertexArrays(obj.getVao());
		glDeleteBuffers(obj.getVbo());
		glDeleteBuffers(obj.getEbo());
	}
	

}
