package com.me.myverilogTown;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

public class ScoreScreen implements Screen
{
	private final VerilogTown		game;
	private OrthographicCamera		camera;
	private Texture[] 				numbers_chiller;
	private TextureButton			main_menu;
	private TextureButton			next_level;
	private Texture					background;
	private Texture					colon_chiller;
	private int						level_number;
	private int						success_cars;
	private int						crash_cars;
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
	
	public ScoreScreen (VerilogTown game, int level_number, int car_success, int car_crash, double time){
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCORE_SCREEN_WIDTH, SCORE_SCREEN_HEIGHT);
		
		this.level_number = level_number;
		this.success_cars = car_success;
		this.crash_cars = car_crash;
		this.playTime = time;
		numbers_chiller = new Texture[10];
		background = new Texture("data/level_finish.png");
		
		main_menu_normal = new Texture("ASSET_RESOURCES/main_menu_normal.png");
		main_menu_hover = new Texture("ASSET_RESOURCES/main_menu_mouse_on.png");
		main_menu_pressed = new Texture("ASSET_RESOURCES/main_menu_pressed.png");
		
		next_level_normal = new Texture("ASSET_RESOURCES/next_level_normal.png");
		next_level_hover = new Texture("ASSET_RESOURCES/next_level_mouse_on.png");
		next_level_pressed = new Texture("ASSET_RESOURCES/next_level_pressed.png");
		
		next_level = new TextureButton(game.batch, 722, 152, 350, 106, next_level_normal, next_level_hover, next_level_pressed);
		main_menu = new TextureButton(game.batch, 190, 150, 350, 112, main_menu_normal, main_menu_hover, main_menu_pressed);
		for(int i = 0; i < 10; i++){
			numbers_chiller[i] = new Texture("data/chiller_num_" + i + ".png");
			numbers_chiller[i].setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		colon_chiller = new Texture("data/chiller_colon.png");
		colon_chiller.setFilter(TextureFilter.Linear, TextureFilter.Linear);
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
		
		game.batch.draw(numbers_chiller[level_number % 10], 578, 1042, 80, 80);

		game.batch.draw(numbers_chiller[success_cars / 100], 780, 815, 80, 80);
		game.batch.draw(numbers_chiller[(success_cars % 100) / 10], 880, 815, 80, 80);
		game.batch.draw(numbers_chiller[success_cars % 10], 980, 815, 80, 80);

		game.batch.draw(numbers_chiller[crash_cars / 100], 780, 600, 80, 80);
		game.batch.draw(numbers_chiller[(crash_cars % 100) / 10], 880, 600, 80, 80);
		game.batch.draw(numbers_chiller[crash_cars % 10], 980, 600, 80, 80);

		game.batch.draw(numbers_chiller[((int) playTime / 60) / 10], 780, 375, 80, 80);
		game.batch.draw(numbers_chiller[((int) playTime / 60) % 10], 840, 375, 80, 80);
		game.batch.draw(colon_chiller, 890, 375, 80, 80);
		game.batch.draw(numbers_chiller[((int) playTime % 60) / 10], 940, 375, 80, 80);
		game.batch.draw(numbers_chiller[((int) playTime % 60) % 10], 1000, 375, 80, 80);
		
		if(main_menu.isOnButton(realX, realY)){
			if (isPressed)
				main_menu.drawTexture(TextureButton.PRESSED);
			else if (wasPressed)
				game.setScreen(new MainMenu(game));
			else 
				main_menu.drawTexture(TextureButton.HOVER);
		}
		else
			main_menu.drawTexture(TextureButton.NORMAL);
		
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
