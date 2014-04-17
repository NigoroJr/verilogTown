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
	private Sprite carSprite;
	private Texture car_texture;
	private Texture stop;
	private Texture go;
	private Texture go_forward;
	private Texture go_right;
	private Texture go_left;
	int toggle;
	Random random_number;

	float Time; // Game clock of passed time
	float Frame_Time_25; // amount of time for 25FPS
	float Next_Frame_Time; 

	private verilogTownMap clevel;
	private boolean level_done;

	public LevelScreen(final verilogTown gam) 
	{
		toggle = 0;

		this.game = gam;
		this.level_done = false;
		this.random_number = new Random(3);

		/* init current level map data structure */
		this.clevel = new verilogTownMap(20, 21); // firts_map
		/* this might be where the XML read map goes */
		/* hard coded */
		clevel.verilogTownMapHardCode_first_map();

		thebatch = new SpriteBatch();
		/* initialize the map */
		level_map = new Texture("data/first_map.png"); 

		// create the camera for the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1280, 1280);

		/* NOTE - for graphics "paint.net" pretty good.  Need to save as 8-bit png file */

		car_texture = new Texture("data/car_sheet.png");
		car_texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		/* get all the textures for the lights */
		stop = new Texture("data/stop_tran.png");
		stop.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		go = new Texture("data/go_tran.png");
		go.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		go_forward = new Texture("data/go_forward_tran.png");
		go_forward.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		go_right = new Texture("data/go_right_tran.png");
		go_right.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		go_left = new Texture("data/go_left_tran.png");
		go_left.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		/* after reading the number of cars from level */
		num_cars = 20; // hard coded
		cars = new Car[num_cars];

		/* initialize cars */
		cars[0] = new Car(clevel.grid[7][0], clevel.grid[4][22], 100, clevel, 0, 0, 64, 64, 0, 4, car_texture, random_number);
		cars[1] = new Car(clevel.grid[15][0], clevel.grid[4][22], 100, clevel,0, 0, 64, 64, 0, 4, car_texture, random_number);
		cars[2] = new Car(clevel.grid[21][9], clevel.grid[4][22], 100, clevel,0, 0, 64, 64, 0, 4, car_texture, random_number);
		cars[3] = new Car(clevel.grid[21][14], clevel.grid[14][0], 100, clevel,0, 0, 64, 64, 0, 4, car_texture, random_number);
		cars[4] = new Car(clevel.grid[3][22], clevel.grid[14][0], 100, clevel,0, 0, 64, 64, 0, 4, car_texture, random_number);
		cars[5] = new Car(clevel.grid[17][22], clevel.grid[14][0], 100, clevel,0, 0, 64, 64, 0, 4, car_texture, random_number);
		cars[6] = new Car(clevel.grid[7][0], clevel.grid[4][21], 200, clevel,0, 0, 64, 64, 0, 4, car_texture, random_number);
		cars[7] = new Car(clevel.grid[15][0], clevel.grid[4][22], 200, clevel,0, 0, 64, 64, 0, 4, car_texture, random_number);
		cars[8] = new Car(clevel.grid[21][9], clevel.grid[14][0], 200, clevel,0, 0, 64, 64, 0, 4, car_texture, random_number);
		cars[9] = new Car(clevel.grid[21][14], clevel.grid[18][22], 200, clevel,0, 0, 64, 64, 0, 4, car_texture, random_number);
		cars[10] = new Car(clevel.grid[7][0], clevel.grid[4][22], 300, clevel, 0, 0, 64, 64, 0, 4, car_texture, random_number);
		cars[11] = new Car(clevel.grid[15][0], clevel.grid[4][22], 300, clevel,0, 0, 64, 64, 0, 4, car_texture, random_number);
		cars[12] = new Car(clevel.grid[21][9], clevel.grid[4][22], 300, clevel,0, 0, 64, 64, 0, 4, car_texture, random_number);
		cars[13] = new Car(clevel.grid[21][14], clevel.grid[14][0], 300, clevel,0, 0, 64, 64, 0, 4, car_texture, random_number);
		cars[14] = new Car(clevel.grid[3][22], clevel.grid[14][0], 300, clevel,0, 0, 64, 64, 0, 4, car_texture, random_number);
		cars[15] = new Car(clevel.grid[17][22], clevel.grid[14][0], 300, clevel,0, 0, 64, 64, 0, 4, car_texture, random_number);
		cars[16] = new Car(clevel.grid[7][0], clevel.grid[14][0], 400, clevel,0, 0, 64, 64, 0, 4, car_texture, random_number);
		cars[17] = new Car(clevel.grid[15][0], clevel.grid[4][22], 400, clevel,0, 0, 64, 64, 0, 4, car_texture, random_number);
		cars[18] = new Car(clevel.grid[21][9], clevel.grid[14][0], 400, clevel,0, 0, 64, 64, 0, 4, car_texture, random_number);
		cars[19] = new Car(clevel.grid[21][14], clevel.grid[14][0], 400, clevel,0, 0, 64, 64, 0, 4, car_texture, random_number);
	

		/* initialize the level logic control */
		levelLogic = new LevelLogic();

		/* initialize the time */
		Time = 0f;
		Frame_Time_25 = 1/25;
		Next_Frame_Time = 0f;
	}

	@Override
	public void render(float delta) 
	{
		boolean fps_tick = false;

		Time += Gdx.graphics.getDeltaTime();
		Next_Frame_Time += Gdx.graphics.getDeltaTime();

		if (Next_Frame_Time >= Frame_Time_25)
		{
			Next_Frame_Time = Next_Frame_Time - Frame_Time_25;
			fps_tick = true;
		}

		// Simulation step alternate left and right 
		if((Gdx.input.isKeyPressed(Keys.DPAD_LEFT) && toggle == 1) || (Gdx.input.isKeyPressed(Keys.DPAD_RIGHT) && toggle == 0)) 
		{
			toggle = (toggle+1) % 2;
		}
		if (fps_tick = true && toggle == 1 && this.level_done == false)
		{
			/*Gdx.app.log("Time Since last simulation:", "="+ Time);*/
			this.level_done = levelLogic.update(this.cars, this.num_cars, clevel, random_number);
		}
		else if (this.level_done == true)
		{

		}

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// tell the camera to update its matrices.
		camera.update();

		if (this.level_done == true)
		{
			/* LEVEL COMPLETE */
			thebatch.begin();
			thebatch.draw(level_map, 0, 0);
			for (int i = 0; i < num_cars; i++)
			{
				if (cars[i].get_is_running() || cars[i].get_is_crashed())
				{
					cars[i].getCarSprite().setPosition(cars[i].getPosition_x(), cars[i].getPosition_y());
					cars[i].getCarSprite().setRotation(cars[i].set_and_get_rotation_based_on_direction());
					cars[i].getCarSprite().draw(thebatch);
				}
			}
	
			clevel.render_traffic_signal_lights(thebatch, stop, go, go_left, go_right, go_forward);
			this.game.font.draw(thebatch, "Level Done", 100, 150);
			this.game.font.draw(thebatch, "Score", 100, 100);

			thebatch.end();
		}
		else
		{
			/* BAsic animation */
			thebatch.begin();
			thebatch.draw(level_map, 0, 0);
			for (int i = 0; i < num_cars; i++)
			{
				if (cars[i].get_is_running() || cars[i].get_is_crashed())
				{
					if (!cars[i].get_animate_turn())
					{
						cars[i].getCarSprite().setPosition(cars[i].getPosition_x(), cars[i].getPosition_y());
						cars[i].getCarSprite().setRotation(cars[i].set_and_get_rotation_based_on_direction());
						cars[i].getCarSprite().draw(thebatch);
					}
					else
					{
						cars[i].getCarSprite().setPosition(cars[i].get_turn_x(), cars[i].get_turn_y());
						cars[i].getCarSprite().setRotation(cars[i].get_turn_rotation());
						cars[i].getCarSprite().draw(thebatch);
					}
				}
			}
	
			clevel.render_traffic_signal_lights(thebatch, stop, go, go_left, go_right, go_forward);
			thebatch.end();
		}
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


