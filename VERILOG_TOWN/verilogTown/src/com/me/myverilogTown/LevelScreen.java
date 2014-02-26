package com.me.myverilogtown;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me.myverilogTown.Car;


public class LevelScreen implements Screen 
{
	final verilogTown game;

	private OrthographicCamera camera;
	private Texture level_map;
	private SpriteBatch thebatch;
	private LevelLogic levelLogic;
	private Car testCar;
	private Sprite carSprite;
	private Texture tmp;

	public LevelScreen(final verilogTown gam) 
	{
		/* initialize the level logic control */
		levelLogic = new LevelLogic();

		this.game = gam;

		thebatch = new SpriteBatch();
		/* initialize the map */
		// Josh's level_map = new Texture("asset_resources/tiled_maps/first_map.png");
		level_map = new Texture("data/first_map.png"); // mine

		// create the camera for the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1280, 1280);

		tmp = new Texture("data/CAR_BLUE_WHITE_STRIPE_SINGLE.png");

		tmp.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		testCar = new Car(new Vector2(640,640), 64, 64, 0, 100f, tmp);
		// paj commented: Gdx.input.setInputProcessor(new InputHandler(this));
	}

	@Override
	public void render(float delta) 
	{
		levelLogic.update();

		/*
		 * Below is the test code for redrawing, rotating, and moving the sprite about the map
		 * 
		if(Gdx.input.isKeyPressed(Keys.DPAD_LEFT)) {
			testCar.getCarSprite().setRotation(-180);
			testCar.getCarSprite().setX(testCar.getCarSprite().getX()-(Gdx.graphics.getDeltaTime() * testCar.speed));
		}
		if(Gdx.input.isKeyPressed(Keys.DPAD_RIGHT)) {
			testCar.getCarSprite().setRotation(0);
			testCar.getCarSprite().setX(testCar.getCarSprite().getX() + (Gdx.graphics.getDeltaTime() * testCar.speed));
		}
		
		//For some reason, north/south movement is slower than east/west. Probably due to widescreen screen resolutions. Multiplying
		//Velocity by 1.5 seems to make it look at least similar, speed wise - that's why it's cast as a float. 

		if(Gdx.input.isKeyPressed(Keys.DPAD_UP)) {
			testCar.getCarSprite().setRotation(90);
			testCar.getCarSprite().setY((float) (testCar.getCarSprite().getY() + (Gdx.graphics.getDeltaTime() * testCar.speed * 1.5)));
		}
		if(Gdx.input.isKeyPressed(Keys.DPAD_DOWN)) {
			testCar.getCarSprite().setRotation(-90);
			testCar.getCarSprite().setY((float) (testCar.getCarSprite().getY() - (Gdx.graphics.getDeltaTime() * testCar.speed * 1.5)));
		}
	
		*/

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// tell the camera to update its matrices.
		camera.update();

		thebatch.begin();
		thebatch.draw(level_map, 0, 0);
		testCar.getCarSprite().draw(thebatch);
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

	public Car getCar() {
		return testCar;
		// TODO Auto-generated method stub
	}
}
