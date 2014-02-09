package com.me.myverilogTown;

import java.util.Iterator;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
	 
public class LevelScreen implements Screen 
{
	final verilogTown game;
	 
	private OrthographicCamera camera;
	private Texture level_map;
	private SpriteBatch thebatch;
	 
	public LevelScreen(final verilogTown gam) 
	{
		this.game = gam;

		thebatch = new SpriteBatch();
		/* initialize the map */
		level_map = new Texture(Gdx.files.internal("data/first_map.png"));
	 
		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1280, 1280);
	}
	 
	@Override
	public void render(float delta) 
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	 
		// tell the camera to update its matrices.
		camera.update();

		thebatch.begin();
		thebatch.draw(level_map, 0, 0);
		thebatch.end();
	}
	 
	@Override
	public void resize(int width, int height) 
	{
	}
	 
	@Override
	public void show() 
	{
	}
	 
	@Override
	public void hide() 
	{
	}
	 
	@Override
	public void pause() 
	{
	}
	 
	@Override
	public void resume() 
	{
	}
	 
	@Override
	public void dispose() 
	{
	
	}
	 
	
}
