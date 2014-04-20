package com.me.myverilogTown;

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
import com.badlogic.gdx.math.Rectangle;

public class LevelScreen implements Screen
{
	final verilogTown game;

	private Car cars[];
	private int num_cars;

	private OrthographicCamera camera;
	private SpriteBatch thebatch;
	private SpriteBatch uibatch;
	private LevelLogic levelLogic;
	private Sprite carSprite;

	private Texture level_map;
	private Texture car_sheet_texture;
	private Texture stop;
	private Texture go;
	private Texture go_forward;
	private Texture go_right;
	private Texture go_left;

	private int LEVEL_WIDTH;
	private int LEVEL_HEIGHT;
	private int toggle;
	private Random random_number;
	private Rectangle glViewport;

	private float Time; // Game clock of passed time
	private float Frame_Time_25; // amount of time for 25FPS
	private float Next_Frame_Time; 

	private VerilogTownMap clevel;
	private boolean level_done;

	private float zoom_initial; // what the initial zoom ratio is so we can stop at it

	public LevelScreen(final verilogTown gam) 
	{
		/* set a toggle for simulation on off */
		this.toggle = 0;

		/* initialize the level */
		this.game = gam;
		this.level_done = false;
		this.random_number = new Random(3); // should this be rand seed?

		/* init current level map data structure */
		this.clevel = new VerilogTownMap(20, 21); // firts_map
		/* this might be where the XML read map goes */
		/* hard coded map */
		clevel.verilogTownMapHardCode_first_map();

		/* setup the sprites.  First is for the cars and Second is for the UI */
		thebatch = new SpriteBatch();
		uibatch = new SpriteBatch();

		/* initialize the map */
		level_map = new Texture("data/first_map.png"); 

		/* create the camera for the SpriteBatch */
		camera = new OrthographicCamera();
		/* set the level hegiht parameters */
		LEVEL_HEIGHT = 1280;
		LEVEL_WIDTH = 1280;
		/* make the viewport to the level size */
		camera.setToOrtho(false, LEVEL_WIDTH, LEVEL_HEIGHT);
		camera.position.set(LEVEL_WIDTH / 2, LEVEL_HEIGHT / 2, 0);
		glViewport = new Rectangle(0, 0, LEVEL_WIDTH, LEVEL_HEIGHT);
		/* record the starting parameters */
		zoom_initial = camera.zoom;

		/* NOTE - for graphics "paint.net" pretty good.  Need to save as 8-bit png file */

		car_sheet_texture = new Texture("data/car_sheet.png");
		car_sheet_texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

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
		num_cars = 20; // hard coded more xml loading with level
		cars = new Car[num_cars];

		/* initialize cars */
		cars[0] = new Car(clevel.grid[7][0], clevel.grid[4][22], 100, clevel, 0, 0, 64, 64, 0, 4, car_sheet_texture, random_number);
		cars[1] = new Car(clevel.grid[15][0], clevel.grid[4][22], 100, clevel,0, 0, 64, 64, 0, 4, car_sheet_texture, random_number);
		cars[2] = new Car(clevel.grid[21][9], clevel.grid[4][22], 100, clevel,0, 0, 64, 64, 0, 4, car_sheet_texture, random_number);
		cars[3] = new Car(clevel.grid[21][14], clevel.grid[14][0], 100, clevel,0, 0, 64, 64, 0, 4, car_sheet_texture, random_number);
		cars[4] = new Car(clevel.grid[3][22], clevel.grid[14][0], 100, clevel,0, 0, 64, 64, 0, 4, car_sheet_texture, random_number);
		cars[5] = new Car(clevel.grid[17][22], clevel.grid[14][0], 100, clevel,0, 0, 64, 64, 0, 4, car_sheet_texture, random_number);
		cars[6] = new Car(clevel.grid[7][0], clevel.grid[4][21], 200, clevel,0, 0, 64, 64, 0, 4, car_sheet_texture, random_number);
		cars[7] = new Car(clevel.grid[15][0], clevel.grid[4][22], 200, clevel,0, 0, 64, 64, 0, 4, car_sheet_texture, random_number);
		cars[8] = new Car(clevel.grid[21][9], clevel.grid[14][0], 200, clevel,0, 0, 64, 64, 0, 4, car_sheet_texture, random_number);
		cars[9] = new Car(clevel.grid[21][14], clevel.grid[18][22], 200, clevel,0, 0, 64, 64, 0, 4, car_sheet_texture, random_number);
		cars[10] = new Car(clevel.grid[7][0], clevel.grid[4][22], 300, clevel, 0, 0, 64, 64, 0, 4, car_sheet_texture, random_number);
		cars[11] = new Car(clevel.grid[15][0], clevel.grid[4][22], 300, clevel,0, 0, 64, 64, 0, 4, car_sheet_texture, random_number);
		cars[12] = new Car(clevel.grid[21][9], clevel.grid[4][22], 300, clevel,0, 0, 64, 64, 0, 4, car_sheet_texture, random_number);
		cars[13] = new Car(clevel.grid[21][14], clevel.grid[14][0], 300, clevel,0, 0, 64, 64, 0, 4, car_sheet_texture, random_number);
		cars[14] = new Car(clevel.grid[3][22], clevel.grid[14][0], 300, clevel,0, 0, 64, 64, 0, 4, car_sheet_texture, random_number);
		cars[15] = new Car(clevel.grid[17][22], clevel.grid[14][0], 300, clevel,0, 0, 64, 64, 0, 4, car_sheet_texture, random_number);
		cars[16] = new Car(clevel.grid[7][0], clevel.grid[14][0], 400, clevel,0, 0, 64, 64, 0, 4, car_sheet_texture, random_number);
		cars[17] = new Car(clevel.grid[15][0], clevel.grid[4][22], 400, clevel,0, 0, 64, 64, 0, 4, car_sheet_texture, random_number);
		cars[18] = new Car(clevel.grid[21][9], clevel.grid[14][0], 400, clevel,0, 0, 64, 64, 0, 4, car_sheet_texture, random_number);
		cars[19] = new Car(clevel.grid[21][14], clevel.grid[14][0], 400, clevel,0, 0, 64, 64, 0, 4, car_sheet_texture, random_number);

		/* initialize the level logic control */
		levelLogic = new LevelLogic();

		/* initialize the time */
		Time = 0f;
		Frame_Time_25 = 1/25; // 25 FPS
		Next_Frame_Time = 0f;
	}

	@Override
	public void render(float delta) 
	{
		boolean fps_tick = false;
		GL10 gl = Gdx.graphics.getGL10();

        	
		Time += Gdx.graphics.getDeltaTime();
		Next_Frame_Time += Gdx.graphics.getDeltaTime();

		if (Next_Frame_Time >= Frame_Time_25)
		{
			Next_Frame_Time = Next_Frame_Time - Frame_Time_25;
			fps_tick = true;
		}

		/* check for button presses */
		handle_inputs();

		if (fps_tick = true && toggle == 1 && this.level_done == false)
		{
			/* IF - tick happends and simulating then simulate a time frame */
			/*Gdx.app.log("Time Since last simulation:", "="+ Time);*/
			this.level_done = levelLogic.update(this.cars, this.num_cars, clevel, random_number);
		}
		else if (this.level_done == true)
		{
			/* ELSE - level is done.  Probably cleanup here */
		}

		// Camera --------------------- /
		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	     	/*   gl.glViewport((int) glViewport.x, (int) glViewport.y, (int) glViewport.width, (int) glViewport.height); -- seems to cause clipping of full map */

		// tell the camera to update its matrices.
		camera.update();
		/*	camera.apply(gl); -- With the above glViewport, seems to cutoff some of map */

		/* use the camera on this batch ... does zooms and translates */
		thebatch.setProjectionMatrix(camera.combined);

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
			/* Normal animation of simulation */
			thebatch.begin();
			thebatch.draw(level_map, 0, 0);
			for (int i = 0; i < num_cars; i++)
			{
				if (cars[i].get_is_running() || cars[i].get_is_crashed())
				{
					if (!cars[i].get_animate_turn())
					{
						/* normally moving cars */
						cars[i].getCarSprite().setPosition(cars[i].getPosition_x(), cars[i].getPosition_y());
						cars[i].getCarSprite().setRotation(cars[i].set_and_get_rotation_based_on_direction());
						cars[i].getCarSprite().draw(thebatch);
					}
					else
					{
						/* turning or uturning cars */
						cars[i].getCarSprite().setPosition(cars[i].get_turn_x(), cars[i].get_turn_y());
						cars[i].getCarSprite().setRotation(cars[i].get_turn_rotation());
						cars[i].getCarSprite().draw(thebatch);
					}
				}
			}
	
			/* update the traffic light images */
			clevel.render_traffic_signal_lights(thebatch, stop, go, go_left, go_right, go_forward);
			thebatch.end();
		}

		/* Draw the UI ontop of the level map */
		uibatch.begin();
		uibatch.draw(stop, 0, 0);
		uibatch.end();
	}

	public void handle_inputs()
	{
		/* Polling solution at present */

		/* Sim on and off */
		if((Gdx.input.isKeyPressed(Keys.S) && toggle == 0) || (Gdx.input.isKeyPressed(Keys.X) && toggle == 1)) 
		{
			toggle = (toggle+1) % 2;
			Gdx.app.log("LevelScreen", "Toggle="+toggle);
		}
		/* Zoom out */
		if(Gdx.input.isKeyPressed(Keys.A)) 
		{
			if (zoom_initial != camera.zoom)
			{
				camera.zoom += 0.02;
				zoom_limit_to_border();
			}
		}
		/* Zoom in */
		if(Gdx.input.isKeyPressed(Keys.Q)) 
		{
			camera.zoom -= 0.02;
			zoom_limit_to_border();
		}

		/* Panning functions */
		if(Gdx.input.isKeyPressed(Keys.LEFT)) 
		{
			camera.translate(-3, 0, 0);
			zoom_limit_to_border();
		}
		if(Gdx.input.isKeyPressed(Keys.RIGHT)) 
		{
			camera.translate(3, 0, 0);
			zoom_limit_to_border();
		}
		if(Gdx.input.isKeyPressed(Keys.DOWN)) 
		{
			camera.translate(0, -3, 0);
			zoom_limit_to_border();
		}
		if(Gdx.input.isKeyPressed(Keys.UP)) 
		{
			camera.translate(0, 3, 0);
			zoom_limit_to_border();
		}
	}

	private void zoom_limit_to_border()
	{
		/* keeps any move on a zoomed region to the limit of the map */
		float camX;
		float camY;

		camX = camera.position.x;
		camY = camera.position.y;
	
		Vector2 camMin = new Vector2(camera.viewportWidth, camera.viewportHeight);
		camMin.scl(camera.zoom/2); //bring to center and scale by the zoom level
		Vector2 camMax = new Vector2(LEVEL_WIDTH, LEVEL_HEIGHT);
		camMax.sub(camMin); //bring to center

		/* keep camera within borders */
		camX = Math.min(camMax.x, Math.max(camX, camMin.x));
		camY = Math.min(camMax.y, Math.max(camY, camMin.y));

		/* reset position */
		camera.position.set(camX, camY, camera.position.z);
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
