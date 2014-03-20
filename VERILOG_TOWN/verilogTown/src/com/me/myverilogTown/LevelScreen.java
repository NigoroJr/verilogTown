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

public class LevelScreen implements Screen 
{
	final verilogTown game;

	private Car cars[];
	private int num_cars;

	private OrthographicCamera camera;
	private Texture level_map;
	private SpriteBatch thebatch;
	private LevelLogic levelLogic;
	private Car testCar;
	private Sprite carSprite;
	private Texture car_texture;
	int toggle;

	float Time; // Game clock of passed time

	private verilogTownMap clevel;

	public LevelScreen(final verilogTown gam) 
	{
		toggle = 0;

		this.game = gam;

		/* init current level map data structure */
		this.clevel = new verilogTownMap(20, 21); // firts_map
		/* this might be where the XML read map goes */
		/* hard coded */
		clevel.verilogTownMapHardCode_first_map();

		thebatch = new SpriteBatch();
		/* initialize the map */
		// Josh's level_map = new Texture("asset_resources/tiled_maps/first_map.png");
		level_map = new Texture("data/first_map.png"); // mine

		// create the camera for the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1280, 1280);

		car_texture = new Texture("data/CAR_BLUE_WHITE_STRIPE_SINGLE.png");
		car_texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	//	testCar = new Car(new Vector2(640,640), 64, 64, 0, 100f, tmp);

		/* after reading the number of cars from level */
		num_cars = 10; // hard coded
		cars = new Car[num_cars];

		/* initialize cars */
		cars[0] = new Car(clevel.grid[7][0], clevel.grid[4][22], 1, clevel,new Vector2(640,640), 64, 64, 0, 100f, car_texture);
		cars[1] = new Car(clevel.grid[15][0], clevel.grid[4][22], 1, clevel,new Vector2(640,640), 64, 64, 0, 100f, car_texture);
		cars[2] = new Car(clevel.grid[21][9], clevel.grid[4][22], 1, clevel,new Vector2(640,640), 64, 64, 0, 100f, car_texture);
		cars[3] = new Car(clevel.grid[21][14], clevel.grid[14][0], 2, clevel,new Vector2(640,640), 64, 64, 0, 100f, car_texture);
		cars[4] = new Car(clevel.grid[3][22], clevel.grid[14][0], 2, clevel,new Vector2(640,640), 64, 64, 0, 100f, car_texture);
		cars[5] = new Car(clevel.grid[17][22], clevel.grid[14][0], 2, clevel,new Vector2(640,640), 64, 64, 0, 100f, car_texture);
		cars[6] = new Car(clevel.grid[7][0], clevel.grid[4][21], 3, clevel,new Vector2(640,640), 64, 64, 0, 100f, car_texture);
		cars[7] = new Car(clevel.grid[15][0], clevel.grid[14][0], 3, clevel,new Vector2(640,640), 64, 64, 0, 100f, car_texture);
		cars[8] = new Car(clevel.grid[21][9], clevel.grid[14][0], 4, clevel,new Vector2(640,640), 64, 64, 0, 100f, car_texture);
		cars[9] = new Car(clevel.grid[21][14], clevel.grid[18][22], 4, clevel,new Vector2(640,640), 64, 64, 0, 100f, car_texture);

		/* initialize the level logic control */
		levelLogic = new LevelLogic();

		/* initialize the time */
		Time = 0f;
	}

	@Override
	public void render(float delta) 
	{
		// prints out delta time	Gdx.app.log("Time:", "="+ Time);
		Time += Gdx.graphics.getDeltaTime();

		// Simulation step alternate left and right 
		if((Gdx.input.isKeyPressed(Keys.DPAD_LEFT) && toggle == 1) || Gdx.input.isKeyPressed(Keys.DPAD_RIGHT) && toggle == 0) 
		{
			toggle = (toggle+1) % 2;
			Gdx.app.log("Time Since last simulation:", "="+ Time);
			levelLogic.update(this.cars, this.num_cars, clevel);
		}

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// tell the camera to update its matrices.
		camera.update();

		thebatch.begin();
		thebatch.draw(level_map, 0, 0);
		for (int i = 0; i < num_cars; i++)
		{
			if (cars[i].get_is_start_path() && !cars[i].get_is_done_path())
			{
				cars[i].getCarSprite().setPosition((cars[i].get_current_point().get_x()-1)*64, (cars[i].get_current_point().get_y()-1)*64);
				cars[i].getCarSprite().setRotation(cars[i].set_and_get_rotation_based_on_direction());
				cars[i].getCarSprite().draw(thebatch);
			}
		}
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


