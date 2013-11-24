package com.anythingmachine.spinewrapper;

import com.badlogic.gdx.physics.box2d.Body;
import com.esotericsoftware.spine.attachments.RegionAttachment;

public class 	Box2dAttachment extends RegionAttachment {
	public Body body;

	public Box2dAttachment (String name) {
		super(name);
	}
}