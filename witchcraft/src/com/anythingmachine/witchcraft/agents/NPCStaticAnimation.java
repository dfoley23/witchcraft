package com.anythingmachine.witchcraft.agents;

import com.anythingmachine.animations.AnimationManager;
import com.anythingmachine.witchcraft.WitchCraft;
import com.anythingmachine.witchcraft.GameStates.Containers.GamePlayManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.spine.SkeletonBinary;
import com.esotericsoftware.spine.SkeletonData;

public class NPCStaticAnimation {
	private AnimationManager animate;
	private String datafile;
	public int level;
	private Vector3 pos;
	private boolean onscreen = true;

	public NPCStaticAnimation(String skinname, Vector3 pos, Vector2 bodyScale,
			String datafile) {
		this.datafile = datafile;
		this.pos = pos;
		FileHandle handle = Gdx.files.internal(datafile);
		String[] fileContent = handle.readString().split("\n");

		level = Integer.parseInt(fileContent[0].split(",")[1]) - 1;

		setupAnimations(skinname, pos, bodyScale, fileContent);

	}

	public void update(float dT) {
		onscreen = checkInBounds();
		if ( onscreen) {
		float delta = Gdx.graphics.getDeltaTime();

		animate.applyTotalTime(true, delta);

		animate.updateSkel(dT);
		}
	}

	public void draw(Batch batch) {
		if ( onscreen )
			animate.draw(batch);
	}

	public boolean checkInBounds() {
		return level == GamePlayManager.currentlevel && WitchCraft.cam.inscaledBounds(pos);
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

		animate.addAnimation(animations[1], sd.findAnimation(animations[1]));

		animate.setCurrent(animations[1], true);

		animate.setPos(pos, 0, 0);
	}
}
