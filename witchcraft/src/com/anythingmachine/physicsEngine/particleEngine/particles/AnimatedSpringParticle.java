package com.anythingmachine.physicsEngine.particleEngine.particles;

import com.anythingmachine.animations.AnimationManager;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.Util.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.spine.SkeletonBinary;
import com.esotericsoftware.spine.SkeletonData;

public class AnimatedSpringParticle extends SpringParticle {
	private AnimationManager animate;
	public int level;
	private Vector3 pos;
	private boolean onscreen = true;

	public AnimatedSpringParticle(String skinname, Vector3 pos,
			Vector2 bodyScale, String datafile) {
		super(pos);
		this.pos = pos;
		FileHandle handle = Gdx.files.internal(datafile);
		String[] fileContent = handle.readString().split("\n");

		level = Integer.parseInt(fileContent[0].split(",")[1]) - 1;

		setupAnimations(skinname, pos, bodyScale, fileContent);
		this.externalForce.y = Util.GRAVITY;
	}

	@Override
	public void update(float dT) {
		// externalForce.x = GamePlayManager.windx/mass;
		onscreen = checkInBounds();
		if (onscreen) {
			if (!stable) {
				pos.x += vel.x * dT;
				pos.y += vel.y * dT;
				Vector3 tempvel = accel(this.pos, this.vel, dT);
				vel.x += tempvel.x * dT;
				vel.y += tempvel.y * dT;
			}
			animate.setPos(pos, 0, 0);
			// float delta = Gdx.graphics.getDeltaTime();

			animate.applyTotalTime(true, dT);

			animate.updateSkel(dT);
		}
	}

	@Override
	public void setAnimation(String anim, boolean val) {
		animate.setCurrent(anim, val);
	}
	
	@Override
	public boolean isAnimationEnded(float delta) {
		return animate.isAnimationEnded(delta);
	}

	@Override
	public void draw(Batch batch) {
		if (onscreen)
			animate.draw(batch);

	}

	@Override
	public boolean checkInBounds() {
		return WitchCraft.cam.inscaledBounds(pos);
	}

	protected void setupAnimations(String skinname, Vector3 pos, Vector2 scale,
			String[] fileContent) {
		SkeletonBinary sb = new SkeletonBinary(
				(TextureAtlas) WitchCraft.assetManager
						.get("data/spine/characters.atlas"));
		SkeletonData sd = sb.readSkeletonData(Gdx.files
				.internal("data/spine/characters.skel"));

		String[] animations = fileContent[1].split(",");

		animate = new AnimationManager(skinname, pos, scale, false, sd);

		for( String a: animations) {
			if ( !a.contains("ANIM"))
				animate.addAnimation(a, sd.findAnimation(a));
		}
		animate.setCurrent(animations[1], true);

		animate.setPos(pos, 0, 0);
	}

}
