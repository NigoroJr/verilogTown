package com.me.myverilogTown;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/*
 * Class used to simplify loading audio files for VerilogTown
 */

public class AudioLoader {
	
	private Sound collision;

/*
 * This constructor used to specify a new collision file
 */
	public AudioLoader(Sound collision){
		this.collision = collision;
	}
	
/*
 * This constructor used to use the file I'm currently using
 */
	public AudioLoader(){
		this.collision = Gdx.audio.newSound(Gdx.files.internal("myleg.mp3"));
	}

	public Sound getCollisionSound() {
		return collision;
	}

	public void setCollisionSound(Sound collision) {
		this.collision = collision;
	}
	

	

}
