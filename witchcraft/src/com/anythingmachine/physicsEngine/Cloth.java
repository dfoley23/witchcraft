package com.anythingmachine.physicsEngine;

import java.util.ArrayList;
import java.util.Arrays;

import com.anythingmachine.witchcraft.Util.Util;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class Cloth implements PhysicsComponent {
	private ArrayList<Particle> links;
//	private int pid;
//	private int vboid;
//	private int indiid;
	private int offset;
	private short[] indices;
//	private FloatBuffer vbo1;
//	private ShortBuffer ibuffer;
//	private int matloc;
//	private int posLoc;
//	private int normLoc;
	private int indicount;
	private float[] verts;
	private Mesh mesh;
	private ShaderProgram shader;
	
	public Cloth(int w, int h) {
		links = new ArrayList<Particle>();
		offset = 6;
//		initShaders();
		shader = new ShaderProgram(this.vertexShader, this.fragShader);
		indices = Util.triangulateRect((short) w, (short) h, (short) offset);
		indicount = indices.length;
//		ibuffer = BufferUtils.newShortBuffer(indices.length);
//		ibuffer.put(indices);
//		ibuffer.flip();
//		indiid = GL15.glGenBuffers();
//		vboid = GL15.glGenBuffers();
//		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indiid);
//		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, ibuffer,
//				GL15.GL_STATIC_DRAW);
//		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
//		
		verts = new float[w * h * indicount];	  
		mesh = new Mesh( false, verts.length, indicount, 
			    new VertexAttribute( Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE ),
			    new VertexAttribute( Usage.Normal, 3, ShaderProgram.NORMAL_ATTRIBUTE ) );
		mesh.setIndices(indices);
	}

	public Cloth() {
		links = new ArrayList<Particle>();
	}

	public void addForce(Vector3 force) {
		for(Particle p: links) {
			p.addForce(force);
		}
	}
	public void draw(Matrix4 cam) {
//		vbo1.clear();
//		vbo1.put(verts);
//		vbo1.flip();
//		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboid);
//		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vbo1, GL15.GL_DYNAMIC_DRAW);

		mesh.setVertices(verts);
		
		updateVertsByIndex();

		shader.begin();
		shader.setUniformMatrix("u_proj", cam);
		mesh.render(shader, GL20.GL_TRIANGLES);
		shader.end();
		//GL20.glUseProgram(pid);

//		FloatBuffer modelview = BufferUtils.newFloatBuffer(16);
//		modelview.put(cam.val);
//		modelview.flip();
//		GL20.glUniformMatrix4(matloc, false, modelview);
//
//		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboid);
//
//		GL20.glEnableVertexAttribArray(posLoc);
//		GL20.glEnableVertexAttribArray(normLoc);
//		GL20.glVertexAttribPointer(posLoc, 3, GL11.GL_FLOAT, false, 24, 0);
//		GL20.glVertexAttribPointer(normLoc, 3, GL11.GL_FLOAT, false, 24, 24);
//
//		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indiid);
//		GL11.glDrawElements(GL11.GL_TRIANGLES, indicount,
//				GL11.GL_UNSIGNED_SHORT, 0);
//		
//		// Put everything back to default (deselect)
//		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
//		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
//		GL20.glDisableVertexAttribArray(posLoc);
//		GL20.glDisableVertexAttribArray(normLoc);
//		GL20.glUseProgram(0);

	}

	public void draw(ShapeRenderer batch) {
		for (Particle p : links) {
			p.draw(batch);
		}
	}

	public void addLink(Particle p) {
		links.add(p);
//		vbo1 = BufferUtils.newFloatBuffer(verts.length);
	}

	public ArrayList<Particle> getParticles() {
		return links;
	}

	public void updateVertsByIndex() {
		Arrays.fill(verts, 0);
		for (int i = 0; i < indicount; i += 3) {
			Vector3 p1 = links.get((int) (indices[i])).pos;
			Vector3 p2 = links.get((int) (indices[i + 1])).pos;
			Vector3 p3 = links.get((int) (indices[i + 2])).pos;

			verts[(indices[i] * offset)] = p1.x;
			verts[(indices[i] * offset) + 1] = p1.y;
			verts[(indices[i] * offset) + 2] = p1.z;

			verts[(indices[i + 1] * offset)] = p2.x;
			verts[(indices[i + 1] * offset) + 1] = p2.y;
			verts[(indices[i + 1] * offset) + 2] = p2.z;

			verts[(indices[i + 2] * offset)] = p3.x;
			verts[(indices[i + 2] * offset) + 1] = p3.y;
			verts[(indices[i + 2] * offset) + 2] = p3.z;

			Vector3 normal = new Vector3(p2.x - p1.x, p2.y - p1.y, p2.z
					- p1.z).nor();

			Vector3 tangent = new Vector3(p3.x - p1.x, p3.y - p1.y, p3.z
					- p1.z).nor();

			normal.crs(tangent);

			verts[(indices[i] * offset) + 3] += normal.x;
			verts[(indices[i] * offset) + 4] += normal.y;
			verts[(indices[i] * offset) + 5] += normal.z;

			verts[(indices[i + 1] * offset) + 3] += normal.x;
			verts[(indices[i + 1] * offset) + 4] += normal.y;
			verts[(indices[i + 1] * offset) + 5] += normal.z;

			verts[(indices[i + 2] * offset) + 3] += normal.x;
			verts[(indices[i + 2] * offset) + 4] += normal.y;
			verts[(indices[i + 2] * offset) + 5] += normal.z;
		}
	}

//	private void initShaders() {
//		int vs = Util.readShaderString(vertexShader, GL20.GL_VERTEX_SHADER);
//		int fs = Util.readShaderString(fragShader, GL20.GL_FRAGMENT_SHADER);
//		pid = GL20.glCreateProgram();
//		GL20.glAttachShader(pid, vs);
//		GL20.glAttachShader(pid, fs);
//		GL20.glLinkProgram(pid);
//		GL20.glValidateProgram(pid);
//		matloc = GL20.glGetUniformLocation(pid, "u_proj");
//		posLoc = GL20.glGetAttribLocation(pid, "a_position");
//		normLoc = GL20.glGetAttribLocation(pid, "a_normal");
//	}

	private String vertexShader = "attribute vec3 a_position;\n" //
			+ "attribute vec3 a_normal;\n" //
			// + "attribute vec4 a_color;\n" //
			+ "uniform mat4 u_proj;\n" //
			+ "varying vec4 v_color;\n" //
			// + "varying vec3 v_normal;\n" //
			+ "\n" //
			+ "void main()\n" //
			+ "{\n" //
			+ "   vec3 normal = normalize(a_normal);\n" //
			+ "   vec4 c = vec4(1, 1, 1, 1);\n" //
			+ "   float LdotN = dot(vec3(0, -0.5, -0.5), normal);\n" //
			+ "   if ( LdotN < 0 ) {\n" // 
			+ "      LdotN = -LdotN;\n" //
			+ "   }\n" //
			+ "   vec4 color = c;\n" //
			+ "   if ( LdotN > 0.35 ) {\n" //
			+ "      color = c * LdotN;\n" //
			+ "   } else {\n" //
			+ "      color = c * 0.35;\n" //
			+ "   }\n" //
			+ "   v_color = color;\n" //
			+ "   gl_Position =  u_proj * vec4(a_position, 1.0);\n" //
			+ "}\n";
	private String fragShader = "#ifdef GL_ES\n" //
			+ "precision mediump float;\n" //
			+ "#endif\n" //
			+ "varying vec4 v_color;\n" //
			// + "varying vec3 v_normal;\n" //
			+ "void main()\n"//
			+ "{\n" //
			+ "   gl_FragColor = v_color;\n" //
			+ "}";
}
