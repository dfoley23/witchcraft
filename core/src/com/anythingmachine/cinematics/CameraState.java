package com.anythingmachine.cinematics;

import com.anythingmachine.witchcraft.WitchCraft;
import com.badlogic.gdx.math.Vector3;

public enum CameraState {
	FOLLOWPLAYER {
		@Override
		public void update(Vector3 pos, float maxX) {
			Camera.camera.position.x = Math.min(pos.x, maxX
					- (WitchCraft.screenWidth * WitchCraft.scale));
			Camera.camera.position.y = pos.y;
		}
	},
	CINEMATIC {
		@Override
		public void update(Vector3 pos, float maxX) {

		}
	},
	LOCKED {
		@Override
		public void update(Vector3 pos, float maxX) {

		}
	},
	LEFTALIGNPLAYER {
		@Override
		public void update(Vector3 pos, float maxX) {
			Camera.camera.position.x = Math.min(pos.x+(WitchCraft.screenWidth*0.5f), maxX
					- (WitchCraft.screenWidth * WitchCraft.scale));
			Camera.camera.position.y = pos.y;
		}		
	};

	public void update(Vector3 pos, float maxX) {

	}

}
