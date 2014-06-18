/*
The MIT License (MIT)

Copyright (c) 2014 Peter Jamieson, Naoki Mizuno, and Boyu Zhang

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 */

package com.me.myverilogTown;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import VerilogSimulator.Parse;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.*;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class LevelScreen implements Screen
{
	public static final String	VERILOG_TOWN_DEVELOPMENT	= "VERILOG_TOWN_DEVELOPMENT";

	public final VerilogTown	game;

	private Car					cars[];
	private int					num_cars;

	private OrthographicCamera	camera;
	private OrthographicCamera	uiCamera;
	private SpriteBatch			thebatch;
	private SpriteBatch			uibatch;
	private LevelLogic			levelLogic;

	private Texture				level_map;
	private Texture				stop;
	private Texture				stop_highlighted;
	private Texture				stop_compiled_failed;
	private Texture				go;
	private Texture				go_forward;
	private Texture				go_right;
	private Texture				go_left;
	private Texture				top_score_bar;
	private Texture				check_mark;
	private Texture[]			numbers;
	private Texture				border;
	private Texture				colon1;
	private Texture				colon2;
	private Texture				crash;
	private Texture				letterT;
	private Texture				letterI;
	private Texture				letterM;
	private Texture				letterE;
	private Texture				help_menu;
	private Texture				level_finish;
	private Texture[]			numbers_chiller;
	private Texture				colon_chiller;

	private int					LEVEL_WIDTH;
	private int					LEVEL_HEIGHT;
	private int					SCORE_BAR_WIDTH;
	private int					SCORE_BAR_HEIGHT;
	private Boolean				isSimulationPaused;
	private int					success_cars;
	private int					crash_cars;
	private Random				random_number;
	private Rectangle			glViewport;

	/* Game clock of passed time */
	private float				Time;
	/* amount of time for 25F PS */
	private float				Frame_Time_25;
	private float				Next_Frame_Time;

	private VerilogTownMap		clevel;
	private boolean				level_done;
	private boolean				simulation_started;
	private boolean				reset_as_front_of_loop;

	private boolean				help_menu_pop;
	private boolean				lastHPressed;
	private boolean				currentHPressed;
	private double				helpTime;
	private int					helpYPosition;

	private double				finish_time;
	private int					finishYPosition;

	private boolean				disableZoom;

	/* what the initial zoom ratio is so we can stop at it */
	private float				zoom_initial;

	private double				lastTime;
	private double				playTime;

	private InputHandler		inputProcessor;

	private Parse[]				Compiler;
	private String				pathOfVerilogFile			= "";
	private String				pathOfEditorJar				= "";
	private String				rootPath					= "";

	private boolean				lastButtonPressed			= false;
	private boolean				currentButtonPressed		= false;
	private LevelXMLParser		parser;

	private boolean				problem_with_compile;
	private boolean[]			failed_compile_traffic;

	private int					level_number;

	public LevelScreen(final VerilogTown gam, int level_number)
	{
		this.isSimulationPaused = true;
		this.reset_as_front_of_loop = false;

		/* initialize the level */
		this.game = gam;
		this.level_done = false;
		this.simulation_started = false;
		this.random_number = new Random(3); // should this be rand seed?
		
		String xmlPath = String.format("%s/Levels/Lv%d/map/lv%02d.xml", VerilogTown.getRootPath(), level_number, level_number);
		parser = new LevelXMLParser(xmlPath);
		/* init current level map data structure */
		int visibleGridX = parser.getGridArray().length - 2;
		int visibleGridY = parser.getGridArray()[0].length - 2;

		this.clevel = new VerilogTownMap(visibleGridX, visibleGridY); // firts_map

		failed_compile_traffic = new boolean[parser.getTrafficControls().length];

		this.level_number = level_number;
		/* Read XML */
		clevel.readMap(parser);

		/* setup the sprites. First is for the cars and Second is for the UI */
		thebatch = new SpriteBatch();
		uibatch = new SpriteBatch();

		/* Set map picture */
		String pngPath = String.format("%s/Levels/Lv%d/map/lv%02d.png", VerilogTown.getRootPath(), level_number, level_number);
		level_map = new Texture(pngPath);
		/* create the camera for the SpriteBatch */
		camera = new OrthographicCamera();
		uiCamera = new OrthographicCamera();
		/* set the level hegiht parameters */
		LEVEL_HEIGHT = 64 * visibleGridY;
		LEVEL_WIDTH = 64 * visibleGridX;
		/* set the score bar parameters */
		SCORE_BAR_WIDTH = LEVEL_HEIGHT;
		SCORE_BAR_HEIGHT = 100;
		/* make the viewport to the level size */
		camera.setToOrtho(false, LEVEL_WIDTH, LEVEL_HEIGHT + SCORE_BAR_HEIGHT);
		camera.position.set((LEVEL_WIDTH) / 2, (LEVEL_HEIGHT + SCORE_BAR_HEIGHT) / 2, 0);
		glViewport = new Rectangle(0, 0, LEVEL_WIDTH, LEVEL_HEIGHT + SCORE_BAR_HEIGHT);

		uiCamera.setToOrtho(false, LEVEL_WIDTH, LEVEL_HEIGHT + SCORE_BAR_HEIGHT);
		uiCamera.position.set((LEVEL_WIDTH) / 2, (LEVEL_HEIGHT + SCORE_BAR_HEIGHT) / 2, 0);
		glViewport = new Rectangle(0, 0, LEVEL_WIDTH, LEVEL_HEIGHT + SCORE_BAR_HEIGHT);
		/* record the starting parameters */
		zoom_initial = camera.zoom;

		/* NOTE - for graphics "paint.net" pretty good. Need to save as 8-bit
		 * png file */

		/* get all the textures for the lights */
		stop = new Texture("data/stop_tran.png");
		stop.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		stop_highlighted = new Texture("data/stop_tran_highlighted.png");
		stop_highlighted.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		stop_compiled_failed = new Texture("data/stop_compiled_failed.png");
		stop_compiled_failed.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		go = new Texture("data/go_tran.png");
		go.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		go_forward = new Texture("data/go_forward_tran.png");
		go_forward.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		go_right = new Texture("data/go_right_tran.png");
		go_right.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		go_left = new Texture("data/go_left_tran.png");
		go_left.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		top_score_bar = new Texture("data/score_bar.jpg");
		top_score_bar.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		numbers = new Texture[10];
		numbers_chiller = new Texture[10];
		for (int i = 0; i < 10; i++)
		{
			String temp = "data/red_num_" + i + ".png";
			numbers[i] = new Texture(temp);
			numbers[i].setFilter(TextureFilter.Linear, TextureFilter.Linear);
			temp = "data/chiller_num_" + i + ".png";
			numbers_chiller[i] = new Texture(temp);
			numbers_chiller[i].setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		check_mark = new Texture("data/checkmark.png");
		check_mark.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		border = new Texture("data/border.png");
		border.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		colon1 = new Texture("data/dot.png");
		colon1.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		colon2 = new Texture("data/dot.png");
		colon2.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		crash = new Texture("data/skeleton.png");
		colon_chiller = new Texture("data/chiller_colon.png");
		colon_chiller.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		crash.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		letterT = new Texture("data/Letter-T.png");
		letterT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		letterI = new Texture("data/Letter-I.png");
		letterI.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		letterM = new Texture("data/Letter-M.png");
		letterM.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		letterE = new Texture("data/Letter-E.png");
		letterE.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		help_menu = new Texture("data/help_menu.png");
		help_menu.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		level_finish = new Texture("data/level_finish.png");
		level_finish.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		inputProcessor = new InputHandler(camera, LEVEL_WIDTH, LEVEL_HEIGHT, SCORE_BAR_HEIGHT, disableZoom);

		/* Get parsed cars */
		cars = parser.getCars();
		this.num_cars = cars.length;

		/* initialize the level logic control */
		levelLogic = new LevelLogic();

		/* Setup the paths of the verilog files and the jar */
		setupPaths();

		/* initialize the simulators for the Verilog */
		Compiler = new Parse[clevel.get_num_traffic_signals()];
		for (int i = 0; i < clevel.get_num_traffic_signals(); i++)
		{
			Compiler[i] = new Parse();
		}

		/* initialize the time */
		Time = 0f;
		Frame_Time_25 = 1 / 25; // 25 FPS
		Next_Frame_Time = 0f;

		helpYPosition = LEVEL_HEIGHT + SCORE_BAR_HEIGHT;
		finishYPosition = LEVEL_HEIGHT + SCORE_BAR_HEIGHT;
	}

	@Override
	public void render(float delta)
	{
		boolean fps_tick = false;

		if (reset_as_front_of_loop)
		{
			this.softReset();
		}
		GL10 gl = Gdx.graphics.getGL10();

		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// Camera --------------------- /

		/* gl.glViewport((int) glViewport.x, (int) glViewport.y, (int)
		 * glViewport.width, (int) glViewport.height); -- seems to cause
		 * clipping of full map */

		// tell the camera to update its matrices.
		camera.update();
		/* camera.apply(gl); -- With the above glViewport, seems to cutoff some
		 * of map */

		/* use the camera on this batch ... does zooms and translates */
		thebatch.setProjectionMatrix(camera.combined);
		uibatch.setProjectionMatrix(uiCamera.combined);

		Gdx.input.setInputProcessor(inputProcessor);

		Time += Gdx.graphics.getDeltaTime();
		Next_Frame_Time += Gdx.graphics.getDeltaTime();

		if (Next_Frame_Time >= Frame_Time_25)
		{
			Next_Frame_Time = Next_Frame_Time - Frame_Time_25;
			fps_tick = true;
		}

		if (!simulation_started)
		{
			thebatch.begin();
			thebatch.draw(level_map, 0, 0, LEVEL_WIDTH, LEVEL_HEIGHT);
			clevel.render_traffic_signal_lights(thebatch, stop, go, go_left, go_right, go_forward);
			thebatch.end();
		}

		/* check for button presses */
		lastHPressed = currentHPressed;
		handle_inputs();

		if (problem_with_compile)
		{
			thebatch.begin();
			for (int i = 0; i < clevel.get_num_traffic_signals(); i++)
			{
				if (failed_compile_traffic[i])
					clevel.traffic_signals[i].render_complied_failed_stop(thebatch, stop_compiled_failed);
			}

			thebatch.end();
		}

		if (fps_tick == true && !isSimulationPaused && simulation_started && this.level_done == false)
		{
			/* IF - tick happends and simulating then simulate a time frame */
			/* Gdx.app.log("Time Since last simulation:", "="+ Time); */
			this.level_done = levelLogic.update(this.cars, this.num_cars, clevel, random_number, Compiler);
			this.success_cars = levelLogic.success_cars;
			this.crash_cars = levelLogic.crash_cars;
			this.playTime += Gdx.graphics.getDeltaTime();
		}

		// TODO this if block should be removed because after the level is done
		// the last frame will stay there

		if (this.level_done == true && simulation_started)
		{
			// LEVEL COMPLETE
			thebatch.begin();
			thebatch.draw(level_map, 0, 0, LEVEL_WIDTH, LEVEL_HEIGHT);

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

			thebatch.end();
		}

		if (this.level_done == false && simulation_started)
		{
			/* Normal animation of simulation */
			thebatch.begin();
			thebatch.draw(level_map, 0, 0, LEVEL_WIDTH, LEVEL_HEIGHT);

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
		draw_score_bar();

		if (this.level_done == true)
		{
			/* ELSE - level is done. Probably cleanup here */
			thebatch.begin();
			finish_time += Gdx.graphics.getDeltaTime();
			finishYPosition -= (int) (300 * Math.pow(finish_time + 0.4, 4));
			if (finishYPosition >= LEVEL_HEIGHT + SCORE_BAR_HEIGHT)
				finishYPosition = LEVEL_HEIGHT + SCORE_BAR_HEIGHT;
			else if (finishYPosition < 0)
			{
				finishYPosition = 0;
			}
			thebatch.draw(level_finish, 0, finishYPosition, LEVEL_WIDTH, LEVEL_HEIGHT + SCORE_BAR_HEIGHT);

			thebatch.draw(numbers_chiller[level_number % 10], 578, finishYPosition + 1042, 80, 80);

			thebatch.draw(numbers_chiller[success_cars / 100], 780, finishYPosition + 815, 80, 80);
			thebatch.draw(numbers_chiller[(success_cars % 100) / 10], 880, finishYPosition + 815, 80, 80);
			thebatch.draw(numbers_chiller[success_cars % 10], 980, finishYPosition + 815, 80, 80);

			thebatch.draw(numbers_chiller[crash_cars / 100], 780, finishYPosition + 600, 80, 80);
			thebatch.draw(numbers_chiller[(crash_cars % 100) / 10], 880, finishYPosition + 600, 80, 80);
			thebatch.draw(numbers_chiller[crash_cars % 10], 980, finishYPosition + 600, 80, 80);

			thebatch.draw(numbers_chiller[((int) playTime / 60) / 10], 780, finishYPosition + 375, 80, 80);
			thebatch.draw(numbers_chiller[((int) playTime / 60) % 10], 840, finishYPosition + 375, 80, 80);
			thebatch.draw(colon_chiller, 890, finishYPosition + 375, 80, 80);
			thebatch.draw(numbers_chiller[((int) playTime % 60) / 10], 940, finishYPosition + 375, 80, 80);
			thebatch.draw(numbers_chiller[((int) playTime % 60) % 10], 1000, finishYPosition + 375, 80, 80);

			if (finishYPosition == 0 && finish_time >= 0.7)
			{
				this.dispose();
				game.setScreen(new ScoreScreen(game, level_number, success_cars, crash_cars, playTime));
			}
			thebatch.end();
		}

		// determine the type of tile that the mouse is click
		double pixOfWindowX = LEVEL_WIDTH * camera.zoom;
		double pixOfWindowY = (LEVEL_HEIGHT + SCORE_BAR_HEIGHT) * camera.zoom;

		double sizeOfWindowX = Gdx.graphics.getWidth();
		double sizeOfWindowY = Gdx.graphics.getHeight();

		double mousePositionX = Gdx.input.getX();
		double mousePositionY = Gdx.input.getY();

		double centerX = camera.position.x;
		double centerY = camera.position.y;

		double realX = (centerX - pixOfWindowX / 2) + ((mousePositionX / sizeOfWindowX) * pixOfWindowX);
		double realY = (centerY - pixOfWindowY / 2) + ((1 - mousePositionY / sizeOfWindowY) * pixOfWindowY);

		realX = Math.min(realX, LEVEL_WIDTH);
		realY = Math.min(realY, LEVEL_HEIGHT + SCORE_BAR_HEIGHT);

		int gridX = 0, gridY = 0;

		if (realY <= LEVEL_HEIGHT)
		{
			gridX = (int) (realX / 64);
			gridY = (int) (realY / 64);
		}

		int counter;
		for (counter = 0; counter < clevel.get_num_traffic_signals(); counter++)
		{
			if (clevel.grid[gridX + 1][gridY + 1].signal == clevel.traffic_signals[counter])
				break;
		}

		// traffic lights highlighting
		if (clevel.grid[gridX + 1][gridY + 1].signal != null && !simulation_started && isSimulationPaused)
		{
			thebatch.begin();
			clevel.traffic_signals[counter].render_highlighted_stop(thebatch, stop_highlighted);
			thebatch.end();
			draw_score_bar();
		}

		lastButtonPressed = currentButtonPressed;
		currentButtonPressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
		if (!currentButtonPressed && (clevel.grid[gridX + 1][gridY + 1].signal != null) && lastButtonPressed && (Time - lastTime) >= 0.6 && !simulation_started && isSimulationPaused)
		{
			/* Open the editor from an external jar */
			String jar_path;
			String verilogFileName = "Traffic_signal_set_" + counter;

			jar_path = this.pathOfEditorJar + "VerilogEditor.jar";
			
			String OSName = System.getProperty("os.name");
			List<String> list = new ArrayList<String>();
			list.add("java");
			list.add("-jar");
			list.add(jar_path);
			// list.add("D:/Program Files/eclipse-java/program/verilogTownStuff/myverilogTown/VerilogEditor.jar");
			list.add(verilogFileName);
			list.add(pathOfEditorJar);
			list.add(rootPath);
			list.add(Integer.toString(level_number));
			try
			{
				ProcessBuilder proc = new ProcessBuilder(list);
				// proc.directory(new File("C:\\"));
				Process p = proc.start();
			}
			catch (Exception e)
			{
				System.out.println("Error invoking the verilog editor");
			}

			lastTime = Time;
		}
		if (help_menu_pop)
		{
			thebatch.begin();
			helpTime += Gdx.graphics.getDeltaTime();
			helpYPosition -= (int) (300 * Math.pow(helpTime + 0.4, 4));
			if (helpYPosition <= 0)
				helpYPosition = 0;
			else if (helpYPosition > LEVEL_HEIGHT + SCORE_BAR_HEIGHT)
				helpYPosition = LEVEL_HEIGHT + SCORE_BAR_HEIGHT;
			thebatch.draw(help_menu, 0, helpYPosition, LEVEL_WIDTH, LEVEL_HEIGHT + SCORE_BAR_HEIGHT);
			thebatch.end();
		}
		else if (!help_menu_pop && helpYPosition < LEVEL_HEIGHT + SCORE_BAR_HEIGHT)
		{
			thebatch.begin();
			helpTime += Gdx.graphics.getDeltaTime();
			helpYPosition += (int) (300 * Math.pow(helpTime + 0.4, 4));
			if (helpYPosition > LEVEL_HEIGHT + SCORE_BAR_HEIGHT || helpYPosition < 0)
				helpYPosition = LEVEL_HEIGHT + SCORE_BAR_HEIGHT;
			thebatch.draw(help_menu, 0, helpYPosition, LEVEL_WIDTH, LEVEL_HEIGHT + SCORE_BAR_HEIGHT);
			thebatch.end();
		}

		if (help_menu_pop || this.level_done)
			disableZoom = true;
		else
			disableZoom = false;
		inputProcessor.set_disable_zoom(disableZoom);
	}

	public void setupPaths()
	{
		
		if(VerilogTown.isDevelopment())
			pathOfEditorJar = VerilogTown.getRootPath() + "/VERILOG_TOWN/";
		else
			pathOfEditorJar = VerilogTown.getRootPath() + "/jars/";
		
		rootPath = VerilogTown.getRootPath() + "/";
		pathOfVerilogFile = rootPath + "Levels/" + "Lv" + level_number + "/" + "VerilogFiles/";
	}

	public void draw_score_bar()
	{
		uibatch.begin();

		uibatch.draw(top_score_bar, 0, LEVEL_HEIGHT, LEVEL_WIDTH, 100);

		uibatch.draw(check_mark, 70, LEVEL_HEIGHT + 18);
		uibatch.draw(border, 140, LEVEL_HEIGHT + 18);
		int successedCars3 = success_cars / 100;
		uibatch.draw(numbers[successedCars3], 140, LEVEL_HEIGHT + 18);
		uibatch.draw(border, 210, LEVEL_HEIGHT + 18);
		int successedCars2 = (success_cars - successedCars3 * 100) / 10;
		uibatch.draw(numbers[successedCars2], 210, LEVEL_HEIGHT + 18);
		uibatch.draw(border, 280, LEVEL_HEIGHT + 18);
		int successedCars1 = success_cars % 10;
		uibatch.draw(numbers[successedCars1], 280, LEVEL_HEIGHT + 18);

		uibatch.draw(crash, 380, LEVEL_HEIGHT + 18);
		uibatch.draw(border, 450, LEVEL_HEIGHT + 18);
		int crashedCars3 = crash_cars / 100;
		uibatch.draw(numbers[crashedCars3], 450, LEVEL_HEIGHT + 18);
		uibatch.draw(border, 520, LEVEL_HEIGHT + 18);
		int crashedCars2 = (crash_cars - crashedCars3 * 100) / 10;
		uibatch.draw(numbers[crashedCars2], 520, LEVEL_HEIGHT + 18);
		uibatch.draw(border, 590, LEVEL_HEIGHT + 18);
		int crashedCars1 = crash_cars % 10;
		uibatch.draw(numbers[crashedCars1], 590, LEVEL_HEIGHT + 18);

		uibatch.draw(letterT, 700, LEVEL_HEIGHT + 18);
		uibatch.draw(letterI, 763, LEVEL_HEIGHT + 18);
		uibatch.draw(letterM, 826, LEVEL_HEIGHT + 18);
		uibatch.draw(letterE, 889, LEVEL_HEIGHT + 18);
		int playTimeSecond = (int) playTime % 60;
		int playTimeMinute = (int) playTime / 60;
		uibatch.draw(border, 959, LEVEL_HEIGHT + 18);
		uibatch.draw(numbers[playTimeMinute / 10], 959, LEVEL_HEIGHT + 18);
		uibatch.draw(border, 1029, LEVEL_HEIGHT + 18);
		uibatch.draw(numbers[playTimeMinute % 10], 1029, LEVEL_HEIGHT + 18);

		uibatch.draw(colon1, 1090, LEVEL_HEIGHT + 22);
		uibatch.draw(colon2, 1090, LEVEL_HEIGHT + 46);

		uibatch.draw(border, 1114, LEVEL_HEIGHT + 18);
		uibatch.draw(numbers[playTimeSecond / 10], 1114, LEVEL_HEIGHT + 18);
		uibatch.draw(border, 1184, LEVEL_HEIGHT + 18);
		uibatch.draw(numbers[playTimeSecond % 10], 1184, LEVEL_HEIGHT + 18);

		uibatch.end();
	}

	public void handle_inputs()
	{
		/* Polling solution at present */

		/* Sim on and off */
		if ((Gdx.input.isKeyPressed(Keys.S) && isSimulationPaused && !help_menu_pop) || (Gdx.input.isKeyPressed(Keys.X) && !isSimulationPaused))
		{
			if (!simulation_started)
			{
				problem_with_compile = false;

				/* Check if all the verilog compiles properly */
				for (int i = 0; i < clevel.get_num_traffic_signals(); i++)
				{
					try
					{
						Compiler[i].compileFileForGame(pathOfVerilogFile + "Traffic_signal_set_" + i + ".txt");
					}
					catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (!Compiler[i].is_compiled_yet())
					{
						/* NOTE - tell user there's an error in file 'i' */
						problem_with_compile = true;
						failed_compile_traffic[i] = true;
					}
					else if (Compiler[i].is_compiled_yet())
						failed_compile_traffic[i] = false;
				}

				if (!problem_with_compile)
				{
					/* Recorded that the simulation has started */
					simulation_started = true;

					/* If compiled simulate first 2 cycles to reset system */
					for (int i = 0; i < clevel.get_num_traffic_signals(); i++)
					{
						Compiler[i].sim_cycle("0", "00000000", "000000000000000000000000000000");
						Compiler[i].sim_cycle("0", "00000000", "000000000000000000000000000000");
					}
				}
			}

			if (isSimulationPaused && simulation_started)
			{
				isSimulationPaused = false;
			}
			else
			{
				isSimulationPaused = true;
			}
			// Gdx.app.log("LevelScreen",
			// "isSimulationPaused="+isSimulationPaused);
		}

		/* Help button */
		if ((currentHPressed = Gdx.input.isKeyPressed(Keys.H)) && !lastHPressed)
		{
			if (!simulation_started || isSimulationPaused)
			{
				if (help_menu_pop)
					help_menu_pop = false;
				else
					help_menu_pop = true;
				helpTime = 0;
			}
		}

		/* Reset Level */
		if (Gdx.input.isKeyPressed(Keys.R))
		{
			reset_as_front_of_loop = true;
		}

		/* Zoom out */
		if (Gdx.input.isKeyPressed(Keys.A) && !disableZoom)
		{
			if (zoom_initial != camera.zoom)
			{
				camera.zoom += 0.02;
				zoom_limit_to_border();
			}
			// System.out.println("camera zoom: "+camera.zoom);
		}

		/* Zoom in */
		if (Gdx.input.isKeyPressed(Keys.Q) && !disableZoom)
		{
			if (camera.zoom >= 0.1)
				camera.zoom -= 0.02;
			zoom_limit_to_border();
			// System.out.println("camera zoom: "+camera.zoom);
		}

		/* Back to main menu */
		if (Gdx.input.isKeyPressed(Keys.M))
		{
			// Only allow going back to menu when simulation hasn't started
			if (!simulation_started)
			{
				this.dispose();
				game.setScreen(new MainMenu(game));
			}
		}

		/* Panning functions */
		if (Gdx.input.isKeyPressed(Keys.LEFT) && !disableZoom)
		{
			camera.translate(-3, 0, 0);
			zoom_limit_to_border();
			// System.out.println("camera position x: " + camera.position.x
			// +"camera position y: " + camera.position.y);
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT) && !disableZoom)
		{
			camera.translate(3, 0, 0);
			zoom_limit_to_border();
			// System.out.println("camera position x: " + camera.position.x
			// +"camera position y: " + camera.position.y);
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN) && !disableZoom)
		{
			camera.translate(0, -3, 0);
			zoom_limit_to_border();
			// System.out.println("camera position x: " + camera.position.x
			// +"camera position y: " + camera.position.y);
		}
		if (Gdx.input.isKeyPressed(Keys.UP) && !disableZoom)
		{
			camera.translate(0, 3, 0);
			zoom_limit_to_border();
			// System.out.println("camera position x: " + camera.position.x
			// +"camera position y: " + camera.position.y);
			// System.out.println("mouse position x: " + Gdx.input.getX() +
			// "mouse position y: " + Gdx.input.getY());
		}
	}

	public void softReset()
	{
		System.out.println("Reset Button");
		this.lastButtonPressed = false;
		this.currentButtonPressed = false;
		this.isSimulationPaused = true;
		this.level_done = false;
		this.simulation_started = false;

		/* Get parsed cars */
		for (int i = 0; i < this.num_cars; i++)
		{
			cars[i].softReset();
		}

		/* reset the grid */
		clevel.softReset();

		/* initialize the level logic control */
		levelLogic = new LevelLogic();

		/* initialize the time */
		Time = 0f;
		Frame_Time_25 = 1 / 25; // 25 FPS
		Next_Frame_Time = 0f;

		playTime = 0;

		reset_as_front_of_loop = false;
	}

	private void zoom_limit_to_border()
	{
		/* keeps any move on a zoomed region to the limit of the map */
		float camX;
		float camY;

		camX = camera.position.x;
		camY = camera.position.y;

		Vector2 camMin = new Vector2(camera.viewportWidth, camera.viewportHeight);
		camMin.scl(camera.zoom / 2); // bring to center and scale by the zoom
										// level
		Vector2 camMax = new Vector2(LEVEL_WIDTH, LEVEL_HEIGHT + SCORE_BAR_HEIGHT);
		camMax.sub(camMin); // bring to center

		/* keep camera within borders */
		camX = Math.min(camMax.x, Math.max(camX, camMin.x));
		camY = Math.min(camMax.y - (1 - camera.zoom) * SCORE_BAR_HEIGHT, Math.max(camY, camMin.y));

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

class InputHandler extends InputAdapter
{
	private OrthographicCamera	camera;
	private int					LEVEL_WIDTH;
	private int					LEVEL_HEIGHT;
	private int					SCORE_BAR_HEIGHT;
	private boolean				DISABLEZOOM;

	float						pixOfWindowX;
	float						pixOfWindowY;
	float						sizeOfWindowX;
	float						sizeOfWindowY;
	float						lastMousePositionX;
	float						lastMousePositionY;
	float						currentMousePositionX;
	float						currentMousePositionY;
	float						centerX;
	float						centerY;
	float						currentRealX;
	float						currentRealY;
	float						lastRealX;
	float						lastRealY;
	int							gridX;
	int							gridY;

	public InputHandler(
			OrthographicCamera camera,
			int LEVEL_WIDTH,
			int LEVEL_HEIGHT,
			int SCORE_BAR_HEIGHT,
			boolean DISABLEZOOM)
	{
		this.camera = camera;
		this.LEVEL_WIDTH = LEVEL_WIDTH;
		this.LEVEL_HEIGHT = LEVEL_HEIGHT;
		this.SCORE_BAR_HEIGHT = SCORE_BAR_HEIGHT;
		this.DISABLEZOOM = DISABLEZOOM;
	}

	public void set_disable_zoom(boolean disableZoom)
	{
		this.DISABLEZOOM = disableZoom;
	}

	@Override
	public boolean scrolled(int amount)
	{
		if (amount == -1 && !DISABLEZOOM)
		{
			System.out.println("amount == " + amount);
			System.out.println("zoom == " + camera.zoom);
			if ((camera.zoom - 0.04) >= 0.2)
				camera.zoom -= 0.04;
			else
				camera.zoom = 0.2f;
			zoom_limit_to_border();
		}
		else if (amount == 1 && !DISABLEZOOM)
		{
			System.out.println("amount == " + amount);
			System.out.println("zoom == " + camera.zoom);
			if ((camera.zoom + 0.04) > 1)
				camera.zoom = 1;
			else
				camera.zoom += 0.04;
			zoom_limit_to_border();
		}
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		lastMousePositionX = currentMousePositionX;
		lastMousePositionY = currentMousePositionY;

		currentMousePositionX = screenX;
		currentMousePositionY = screenY;

		get_real_xy();

		if (lastRealX != 0 && lastRealY != 0 && !DISABLEZOOM)
			camera.translate((lastRealX - currentRealX), (lastRealY - currentRealY), 0);
		zoom_limit_to_border();

		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		lastMousePositionX = currentMousePositionX;
		lastMousePositionY = currentMousePositionY;
		currentMousePositionX = screenX;
		currentMousePositionY = screenY;

		return true;
	}

	private void get_real_xy()
	{
		pixOfWindowX = LEVEL_WIDTH * camera.zoom;
		pixOfWindowY = (LEVEL_HEIGHT + SCORE_BAR_HEIGHT) * camera.zoom;
		sizeOfWindowX = Gdx.graphics.getWidth();
		sizeOfWindowY = Gdx.graphics.getHeight();
		centerX = camera.position.x;
		centerY = camera.position.y;
		currentRealX = (centerX - pixOfWindowX / 2) + ((currentMousePositionX / sizeOfWindowX) * pixOfWindowX);
		currentRealY = (centerY - pixOfWindowY / 2) + ((1 - currentMousePositionY / sizeOfWindowY) * pixOfWindowY);
		currentRealX = Math.min(currentRealX, LEVEL_WIDTH);
		currentRealY = Math.min(currentRealY, LEVEL_HEIGHT + SCORE_BAR_HEIGHT);

		lastRealX = (centerX - pixOfWindowX / 2) + ((lastMousePositionX / sizeOfWindowX) * pixOfWindowX);
		lastRealY = (centerY - pixOfWindowY / 2) + ((1 - lastMousePositionY / sizeOfWindowY) * pixOfWindowY);
		lastRealX = Math.min(lastRealX, LEVEL_WIDTH);
		lastRealY = Math.min(lastRealY, LEVEL_HEIGHT + SCORE_BAR_HEIGHT);
	}

	private void zoom_limit_to_border()
	{
		/* keeps any move on a zoomed region to the limit of the map */
		float camX;
		float camY;

		camX = camera.position.x;
		camY = camera.position.y;

		Vector2 camMin = new Vector2(camera.viewportWidth, camera.viewportHeight);
		camMin.scl(camera.zoom / 2); // bring to center and scale by the zoom
										// level
		Vector2 camMax = new Vector2(LEVEL_WIDTH, LEVEL_HEIGHT + SCORE_BAR_HEIGHT);
		camMax.sub(camMin); // bring to center

		/* keep camera within borders */
		camX = Math.min(camMax.x, Math.max(camX, camMin.x));
		camY = Math.min(camMax.y - (1 - camera.zoom) * 100, Math.max(camY, camMin.y));

		/* reset position */
		camera.position.set(camX, camY, camera.position.z);
	}
}
