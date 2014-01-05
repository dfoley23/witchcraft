package com.anythingmachine.physicsEngine.particleEngine;

import java.util.Random;

import com.anythingmachine.witchcraft.Util.Util;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class CloudParticle implements Emitter {
	public Vector2 position;
	private Vector2 initpos;
	private Vector2 origin = new Vector2(256, 256);
	private Sprite sprite;
	private float speedx;
	private float speedy;
	private float rotation;
	private float rotationSpeed;
	private float time;
	private float scale;
	private int reset = 0;

    public CloudParticle(Texture tex, int direction, Vector2 position, float speedx,
        float rotationSpeed, float scale)
    {
        this.sprite = new Sprite(tex);
        this.position = position;
        this.initpos = position;
        this.speedx = speedx;
        this.rotationSpeed = rotationSpeed;
        this.scale = scale;
        this.time = 0.0f;
        setDirection(scale, direction);
        this.scale = scale;
        Random Random = new Random();
        this.rotation = 0 + (float)Random.nextDouble() * (Util.TWO_PI - 0);
    }

    public void Update()
    {
        position.x += speedx;
        //position.Y = initpos.Y + speedy * time + (0.5f * force * time);

        //rotation += rotationSpeed;

        time += 2;
        //lifeSpan--;
    }

    public boolean Lessthanzero(int winWidth, Vector2 camera)
    {
        if (position.x < 0)
        { //&& position.Y > camera.Y && position.Y < camera.Y + 1000
            return true;
        }
        return false;
    }

    public boolean MoreX(int winWidth, Vector2 camera)
    {
        if (position.x > winWidth)
        { //&& position.Y > camera.Y && position.Y < camera.Y + 1000
            return true;
        }
        return false;
    }

    public boolean Activex(int winWidth, Vector2 camera)
    {
        if (position.x < camera.x - 100 && position.x > camera.x + winWidth + 100)
        { //&& position.Y > camera.Y && position.Y < camera.Y + 1000
            return false;
        }
        return true;
    }

    public boolean Activey(int winHeight, Vector2 camera)
    {
        if (position.y > camera.y - 500)
            return true;
        return false;
    }

    public void Draw(SpriteBatch batch)
    {
        //float normalizedLifetime;
        //normalizedLifetime = time / lifeSpan;
        //float alpha = 4 * normalizedLifetime * (1 - normalizedLifetime);
    	sprite.draw(batch);
    }

    public void setDirection(float scale, int direction)
    {
        if (scale > 2)
        {
            switch (direction)
            {
                case 0:
                    this.speedx *= -1;
                    break;
                case 1:
                    this.speedx *= -1;
                    break;
                case 2:
                    this.speedx *= -1;
                    break;
                case 3:
                    this.rotationSpeed *= -1;
                    this.speedx *= -1;
                    break;
                case 4:
                    this.speedx *= 1;
                    this.rotationSpeed *= -1;
                    break;
                case 5:
                    this.rotationSpeed *= -1;
                    break;
                case 6:
                    this.speedx *= -1;
                    break;
                default:
                    this.speedx *= 1;
                    break;
            }
        }
        else
        {
            switch (direction)
            {
                case 0:
                    this.speedx *= -1;
                    this.speedy *= -1;
                    break;
                case 1:
                    this.speedy *= -1;
                    break;
                case 2:
                    this.speedx *= -1;
                    this.speedy *= -1;
                    break;
                case 3:
                    this.rotationSpeed *= -1;
                    this.speedx *= -1;
                    this.speedy *= -1;
                    break;
                case 4:
                    this.speedy *= 1;
                    this.rotationSpeed *= -1;
                    break;
                case 5:
                    this.rotationSpeed *= -1;
                    this.speedy *= -1;
                    break;
                case 6:
                    this.speedy *= -1;
                    break;
                case 7:
                    this.speedx *= -1;
                    this.speedy *= -1;
                    break;
                case 8:
                    this.speedx *= -1;
                    break;
                default:
                    this.speedy *= -1;
                    break;
            }
        }
    }
}
