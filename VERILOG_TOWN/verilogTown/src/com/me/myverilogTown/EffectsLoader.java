package com.me.myverilogtown;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

/*
 * Class used to simplify loading of particle effects for collisions
 * 
 */
public class EffectsLoader {
	
	private ParticleEffect fx;
	
	public EffectsLoader(){
		fx = new ParticleEffect();
		fx.load(Gdx.files.internal("ASSET_RESOURCES/PARTICLES/FLARE.p"), Gdx.files.internal("ASSET_RESOURCES"));
	}

	public ParticleEffect getFx() {
		return fx;
	}

	public void setFx(ParticleEffect fx) {
		this.fx = fx;
	}	

}
