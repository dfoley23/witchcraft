package com.anythingmachine.physicsEngine;

import java.util.ArrayList;
import java.util.Arrays;

import com.anythingmachine.witchcraft.Util.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class Cloth implements PhysicsComponent {
	private ArrayList<Particle> links;
	private int offset;
	private short[] indices;
	private int indicount;
	private float[] verts;
	private Mesh mesh;
	private ShaderProgram shader;
	
	public Cloth(int w, int h) {
		links = new ArrayList<Particle>();
		offset = 6;
		shader = new ShaderProgram(this.vertexShader, this.fragShader);
		indices = Util.triangulateRect((short) w, (short) h, (short) offset);
		indicount = indices.length;
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
			p.applyImpulse(force);
		}
	}
	public void draw(Matrix4 cam, float alpha) {
		mesh.setVertices(verts);
		
		updateVertsByIndex();
		//Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		if ( alpha < 1 ) 
			Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
		shader.begin();
		shader.setUniformMatrix("u_proj", cam);
		shader.setUniformf("alpha_val", alpha);
		mesh.render(shader, GL20.GL_TRIANGLES);
		shader.end();
		if ( alpha < 1 )
			Gdx.graphics.getGL20().glDisable(GL20.GL_BLEND);
	}

	public void addLink(Particle p) {
		links.add(p);
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

	private String vertexShader = "attribute vec3 a_position;\n" //
			+ "attribute vec3 a_normal;\n" //
			// + "attribute vec4 a_color;\n" //
			+ "uniform mat4 u_proj;\n" //
			+ "uniform float alpha_val;\n" //
			+ "varying vec4 v_color;\n" //
			// + "varying vec3 v_normal;\n" //
			+ "\n" //
			+ "void main()\n" //
			+ "{\n" //
			+ "   vec3 normal = normalize(a_normal);\n" //
			+ "   vec4 c = vec4(1, 1, 1, alpha_val);\n" //
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
