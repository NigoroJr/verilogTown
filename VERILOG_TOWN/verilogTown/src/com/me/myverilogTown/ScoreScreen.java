package com.me.myverilogTown;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

import java.io.*;

public class ScoreScreen implements Screen
{
	private final VerilogTown		game;
	private OrthographicCamera		camera;
	private Texture[] 				numbers_chiller;
	private TextureButton			main_menu;
	private TextureButton			next_level;
	private TextureButton			try_again;
	private Texture					background;
	private Texture					colon_chiller;
	private int						level_number;
	private int						success_cars;
	private int						failed_cars;
	private int 					deviated_cars;
	private int						three_way_int;
	private int						four_way_int;
	private double					playTime;
	
	private boolean					isPressed		= false;
	private boolean					wasPressed		= false;

	private double					pixOfWindowX;
	private double					pixOfWindowY;

	private double					sizeOfWindowX;
	private double					sizeOfWindowY;
	
	private double					mousePositionX;
	private double					mousePositionY;
	
	private double					centerX;
	private double					centerY;

	private double					realX;
	private double					realY;
	
	private int 					SCORE_SCREEN_WIDTH = 1280;
	private int						SCORE_SCREEN_HEIGHT = 1380;
	
	private Texture					main_menu_normal;
	private Texture					main_menu_hover;
	private Texture					main_menu_pressed;
	
	private Texture 				next_level_normal;
	private Texture					next_level_hover;
	private Texture					next_level_pressed;
	
	private Texture					try_again_normal;
	private Texture					try_again_hover;
	private Texture					try_again_pressed;
	
	private File					high_score_file;
	private int						high_score_car_passed[];
	private int 					high_score_time[];
	
	public ScoreScreen (VerilogTown game, int level_number, int car_success, int car_failed,
						double time, int three_way_int, int four_way_int){
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCORE_SCREEN_WIDTH, SCORE_SCREEN_HEIGHT);
		
		this.level_number = level_number;
		this.success_cars = car_success;
		this.failed_cars = car_failed;
		this.three_way_int = three_way_int;
		this.four_way_int = four_way_int;
		this.playTime = time;
		numbers_chiller = new Texture[10];
		background = new Texture("data/level_finish.png");
		
		main_menu_normal = new Texture("ASSET_RESOURCES/main_menu_normal.png");
		main_menu_hover = new Texture("ASSET_RESOURCES/main_menu_mouse_on.png");
		main_menu_pressed = new Texture("ASSET_RESOURCES/main_menu_pressed.png");
		
		next_level_normal = new Texture("ASSET_RESOURCES/next_level_normal.png");
		next_level_hover = new Texture("ASSET_RESOURCES/next_level_mouse_on.png");
		next_level_pressed = new Texture("ASSET_RESOURCES/next_level_pressed.png");
		
		try_again_normal = new Texture("ASSET_RESOURCES/try_again_normal.png");
		try_again_hover = new Texture("ASSET_RESOURCES/try_again_mouse_on.png");
		try_again_pressed = new Texture("ASSET_RESOURCES/try_again_pressed.png");
		
		main_menu = new TextureButton(game.batch, 150, 150, 300, 98, main_menu_normal, main_menu_hover, main_menu_pressed);
		try_again = new TextureButton(game.batch, 500, 150, 300, 98, try_again_normal, try_again_hover, try_again_pressed);
		next_level = new TextureButton(game.batch, 850, 151, 300, 98, next_level_normal, next_level_hover, next_level_pressed);
		
		for(int i = 0; i < 10; i++){
			numbers_chiller[i] = new Texture("data/chiller_num_" + i + ".png");
			numbers_chiller[i].setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		colon_chiller = new Texture("data/chiller_colon.png");
		colon_chiller.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		high_score_car_passed = new int[10];
		high_score_time = new int[10];
		high_score_file = new File(VerilogTown.getRootPath() + "/Levels/" + "Lv" + level_number + "/high_score.txt");
		if(!high_score_file.exists()){
			try
			{
				high_score_file.createNewFile();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			try
			{
				InputStreamReader reader = new InputStreamReader(new FileInputStream(high_score_file));
				BufferedReader br = new BufferedReader(reader);
				String temp = null;
				for(int i = 0; i < 10; i++){
					if((temp = br.readLine()) != null){
						high_score_car_passed[i] = Integer.parseInt(temp.substring(0, temp.indexOf(" ")));
						high_score_time[i] = Integer.parseInt(temp.substring(temp.indexOf(" ") + 1));
					}
					else{
						high_score_car_passed[i] = 0;
						high_score_time[i] = 0;
					}
				}
				br.close();
				reader.close();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		for(int i = 0; i < 10; i++){
			if(this.success_cars == high_score_car_passed[i]){
				if((int)this.playTime == high_score_time[i])
					break;
				else if((int)this.playTime > high_score_time[i])
					continue;
				else {
					for(int j = 9; j > i; j--){
						high_score_car_passed[j] = high_score_car_passed[j - 1];
						high_score_time[j] = high_score_time[j - 1];
					}
					high_score_car_passed[i] = this.success_cars;
					high_score_time[i] = (int)this.playTime;
				}
			}
			else if(this.success_cars > high_score_car_passed[i]){
				for(int j = 9; j > i; j--){
					high_score_car_passed[j] = high_score_car_passed[j - 1];
					high_score_time[j] = high_score_time[j - 1];
				}
				high_score_car_passed[i] = this.success_cars;
				high_score_time[i] = (int)this.playTime;
				break;
			}
			else if(this.success_cars < high_score_car_passed[i])
				continue;
		}
		
		try
		{
			FileWriter out = new FileWriter(high_score_file);
			for(int i = 0; i < 10; i++){
				out.write(high_score_car_passed[i] + " " + high_score_time[i] + "\n");
			}
			out.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		pixOfWindowX = SCORE_SCREEN_WIDTH * camera.zoom;
		pixOfWindowY = SCORE_SCREEN_HEIGHT * camera.zoom;

		sizeOfWindowX = Gdx.graphics.getWidth();
		sizeOfWindowY = Gdx.graphics.getHeight();

		mousePositionX = Gdx.input.getX();
		mousePositionY = Gdx.input.getY();

		centerX = camera.position.x;
		centerY = camera.position.y;

		realX = (centerX - pixOfWindowX / 2) + ((mousePositionX / sizeOfWindowX) * pixOfWindowX);
		realY = (centerY - pixOfWindowY / 2) + ((1 - mousePositionY / sizeOfWindowY) * pixOfWindowY);

		realX = Math.min(realX, SCORE_SCREEN_WIDTH);
		realY = Math.min(realY, SCORE_SCREEN_HEIGHT);
		
		wasPressed = isPressed;
		isPressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
		
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		
		game.batch.begin();
		
		game.batch.draw(background, 0, 0, SCORE_SCREEN_WIDTH, SCORE_SCREEN_HEIGHT);
		
		game.batch.draw(numbers_chiller[level_number % 10], 578, 1088, 80, 80);
		
		game.batch.draw(numbers_chiller[success_cars / 100], 725, 660, 75, 75);
		game.batch.draw(numbers_chiller[(success_cars % 100) / 10], 800, 660, 75, 75);
		game.batch.draw(numbers_chiller[success_cars % 10], 875, 660, 75, 75);
		
		game.batch.draw(numbers_chiller[failed_cars / 100], 725, 420, 75, 75);
		game.batch.draw(numbers_chiller[(failed_cars % 100) / 10], 800, 420, 75, 75);
		game.batch.draw(numbers_chiller[failed_cars % 10], 875, 420, 75, 75);
		
		game.batch.draw(numbers_chiller[((int) playTime / 60) / 10], 725, 895, 75, 75);
		game.batch.draw(numbers_chiller[((int) playTime / 60) % 10], 790, 895, 75, 75);
		game.batch.draw(colon_chiller, 840, 895, 75, 75);
		game.batch.draw(numbers_chiller[((int) playTime % 60) / 10], 895, 895, 75, 75);
		game.batch.draw(numbers_chiller[((int) playTime % 60) % 10], 960, 895, 75, 75);
		
		if(main_menu.isOnButton(realX, realY)){
			if (isPressed)
				main_menu.drawTexture(TextureButton.PRESSED);
			else if (wasPressed){
				this.dispose();
				game.setScreen(new MainMenu(game));
			}
			else 
				main_menu.drawTexture(TextureButton.HOVER);
		}
		else
			main_menu.drawTexture(TextureButton.NORMAL);
		
		if(try_again.isOnButton(realX, realY)){
			if (isPressed)
				try_again.drawTexture(TextureButton.PRESSED);
			else if (wasPressed){
				this.dispose();
				game.setScreen(new LevelScreen(game, level_number));
			}
			else 
				try_again.drawTexture(TextureButton.HOVER);
		}
		else
			try_again.drawTexture(TextureButton.NORMAL);
		
		if(next_level.isOnButton(realX, realY)){
			if (isPressed)
				next_level.drawTexture(TextureButton.PRESSED);
			else if (wasPressed){
				this.dispose();
				if(level_number + 1 < 4)
					game.setScreen(new LevelScreen(game, level_number + 1));
				else 
					game.setScreen(new MainMenu(game));
				//go to the next level
			}
			else 
				next_level.drawTexture(TextureButton.HOVER);
		}
		else
			next_level.drawTexture(TextureButton.NORMAL);
		
		game.batch.end();
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
