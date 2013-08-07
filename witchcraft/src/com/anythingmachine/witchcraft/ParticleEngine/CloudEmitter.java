package com.anythingmachine.witchcraft.ParticleEngine;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class CloudEmitter {
	 private ArrayList<CloudParticle> particleList = new ArrayList<CloudParticle>();
	 private int init = 0;
	 private float yPosition;
	 private float xStart, xStop;
	 private int winWidth;
	 private int winHeight;
	 private Texture tex;
	 private Random rnd;

     public CloudEmitter(float yPosition, float xStart, float xStop, int winWidth, int winHeight)
     {
         this.tex = tex;
         rnd = new Random();
         this.yPosition = yPosition;
         this.xStart = xStart;
         this.xStop = xStop;
         this.winHeight = winHeight;
         this.winWidth = winWidth;
     }

     public void Update(Vector2 camera)
     {
         if (init == 0)
         {
             for (int i = 0; i < 13; i++)
             {
                 particleList.add((new CloudParticle(tex, rnd.nextInt(4), new Vector2((float)rnd.nextInt((int)xStop),
                     Math.min(winHeight - 256, (float)rnd.nextInt((int)yPosition) - rnd.nextInt(340))), //texture, direction, position
                     ((float)(rnd.nextInt(5)) / 10) + ((float)(rnd.nextInt(5)) / 10) + .1f, //speedx
                     ((float)(rnd.nextInt(10)) / 40), //rotationSpeed
                     (float)rnd.nextInt(7) / 10 + 5f))); //scale
             }
             init = 1;
         }
         for (int i = 0; i < particleList.size(); i++)
         {
             if (particleList.get(i).position.x < -170)
             {
                 particleList.get(i).position.x = winWidth + 170;
             }
         }
         for(CloudParticle n : particleList)
         {
             n.Update();
         }
     }

     public void Draw(SpriteBatch batch, Vector2 camera)
     {
         for (CloudParticle d : particleList)
         {
             d.Draw(batch);
         }
     }
}
