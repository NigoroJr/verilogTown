package com.me.myverilogTown;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

public class LocalHighScoreScreen implements Screen
{
	private static final int	level_num 					= 12;
	
	private final VerilogTown	game;
	private OrthographicCamera	camera;

	private int					levelsCars[][];
	private int					levelsTime[][];
    
	private Texture				levelsLabel[];
	private TextureButton		back;
	private TextureButton		next;
	private TextureButton		last;

	private Texture				back_normal;
	private Texture				back_hover;
	private Texture				back_pressed;
	
	private Texture				next_normal;
	private Texture				next_hover;
	private Texture				next_pressed;
	
	private Texture				last_normal;
	private Texture				last_hover;
	private Texture				last_pressed;

	private Texture				title;
	private Texture				rank_label;
	private Texture				cars_label;
	private Texture				time_label;

	private boolean				isPressed					= false;
	private boolean				wasPressed					= false;

	private double				pixOfWindowX;
	private double				pixOfWindowY;

	private double				sizeOfWindowX;
	private double				sizeOfWindowY;

	private double				mousePositionX;
	private double				mousePositionY;

	private double				centerX;
	private double				centerY;

	private double				realX;
	private double				realY;

	private int					HIGH_SCORE_SCREEN_WIDTH		= 1280;
	private int					HIGH_SCORE_SCREEN_HEIGHT	= 1380;

	private ScoreAndTimeTexture	scoreAndTimeTexture;

	private int					currentLevel;
	private int					lastLevel;

	private float				spriteX;
	private float				desireX;
	private double				actionTime;

	public LocalHighScoreScreen(VerilogTown game)
	{
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, HIGH_SCORE_SCREEN_WIDTH, HIGH_SCORE_SCREEN_HEIGHT);
		currentLevel = 1;
		lastLevel = 1;

		levelsCars = new int[level_num][10];
		levelsTime = new int[level_num][10];
		for(int i = 0; i < level_num; i++){
			File highScoreFile = new File(VerilogTown.getRootPath() + "/Levels/Lv" + (i + 1) + "/high_score.txt");
			try{
				InputStreamReader reader = new InputStreamReader(new FileInputStream(highScoreFile));
				BufferedReader br = new BufferedReader(reader);
				String temp = null;
				for(int j = 0; j < 10; j++){
					if((temp = br.readLine()) != null){
						levelsCars[i][j] = Integer.parseInt(temp.substring(0, temp.indexOf(" ")));
						levelsTime[i][j] = Integer.parseInt(temp.substring(temp.indexOf(" ") + 1));
					}
					else{
						levelsCars[i][j] = 0;
						levelsTime[i][j] = 0;
					}
				}
				br.close();
				reader.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		levelsLabel = new Texture[level_num];
		for(int i = 0; i < level_num; i++){
			levelsLabel[i] = new Texture("ASSET_RESOURCES/score_level" + (i + 1) + "_pressed.png");
		}

		next_normal = new Texture("ASSET_RESOURCES/next_normal.png");
		next_hover = new Texture("ASSET_RESOURCES/next_hover.png");
		next_pressed = new Texture("ASSET_RESOURCES/next_pressed.png");
		
		back_normal = new Texture("ASSET_RESOURCES/back_normal.png");
		back_hover = new Texture("ASSET_RESOURCES/back_mouse_on.png");
		back_pressed = new Texture("ASSET_RESOURCES/back_pressed.png");
		
		last_normal = new Texture("ASSET_RESOURCES/last_normal.png");
		last_hover = new Texture("ASSET_RESOURCES/last_hover.png");
		last_pressed = new Texture("ASSET_RESOURCES/last_pressed.png");

		title = new Texture("ASSET_RESOURCES/local_high_score.png");
		rank_label = new Texture("ASSET_RESOURCES/rank.png");
		cars_label = new Texture("ASSET_RESOURCES/cars.png");
		time_label = new Texture("ASSET_RESOURCES/time.png");

		back = new TextureButton(game.batch, 30, 45, 160, 59, back_normal, back_hover, back_pressed);
		next = new TextureButton(game.batch, 1180, 500, 90, 300, next_normal, next_hover, next_pressed);
		last = new TextureButton(game.batch, 10, 500, 90, 300, last_normal, last_hover, last_pressed);

		scoreAndTimeTexture = new ScoreAndTimeTexture(game.batch, 48, 53, 55);

		spriteX = 0;
	}

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0.78f, 0.9f, 0.78f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		pixOfWindowX = HIGH_SCORE_SCREEN_WIDTH * camera.zoom;
		pixOfWindowY = HIGH_SCORE_SCREEN_HEIGHT * camera.zoom;

		sizeOfWindowX = Gdx.graphics.getWidth();
		sizeOfWindowY = Gdx.graphics.getHeight();

		mousePositionX = Gdx.input.getX();
		mousePositionY = Gdx.input.getY();

		centerX = camera.position.x;
		centerY = camera.position.y;

		realX = (centerX - pixOfWindowX / 2) + ((mousePositionX / sizeOfWindowX) * pixOfWindowX);
		realY = (centerY - pixOfWindowY / 2) + ((1 - mousePositionY / sizeOfWindowY) * pixOfWindowY);

		realX = Math.min(realX, HIGH_SCORE_SCREEN_WIDTH);
		realY = Math.min(realY, HIGH_SCORE_SCREEN_HEIGHT);

		wasPressed = isPressed;
		isPressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();

		game.batch.draw(title, 260, 1130, 750, 280);
		game.batch.draw(rank_label, 180, 1037, 150, 84);
		game.batch.draw(cars_label, 560, 1037, 150, 84);
		game.batch.draw(time_label, 940, 1037, 150, 84);

		if (next.isOnButton(realX, realY))
		{
			if (isPressed)
				next.drawTexture(TextureButton.PRESSED);
			else if (wasPressed)
			{
				if(currentLevel < level_num)
					currentLevel++;
				desireX = -(currentLevel - 1) * HIGH_SCORE_SCREEN_WIDTH;
			}
			else
				next.drawTexture(TextureButton.HOVER);
		}
		else
			next.drawTexture(TextureButton.NORMAL);
		
		
		if (last.isOnButton(realX, realY))
		{
			if (isPressed)
				last.drawTexture(TextureButton.PRESSED);
			else if (wasPressed)
			{
				if(currentLevel > 1)
					currentLevel--;
				desireX = -(currentLevel - 1) * HIGH_SCORE_SCREEN_WIDTH;
			}
			else
				last.drawTexture(TextureButton.HOVER);
		}
		else
			last.drawTexture(TextureButton.NORMAL);

		
		if (back.isOnButton(realX, realY))
		{
			if (isPressed)
				back.drawTexture(TextureButton.PRESSED);
			else if (wasPressed)
			{
				this.dispose();
				game.setScreen(new MainMenu(game));
			}
			else
				back.drawTexture(TextureButton.HOVER);
		}
		else
			back.drawTexture(TextureButton.NORMAL);
		
		if (currentLevel - lastLevel > 0)
		{
			if (spriteX <= desireX)
			{
				spriteX = desireX;
				actionTime = 0;
				lastLevel = currentLevel;
			}
			else
			{
				actionTime += Gdx.graphics.getDeltaTime();
				spriteX += (desireX - spriteX) / Math.abs(spriteX - desireX) * Math.min((300 * Math.pow(actionTime + 0.4, 4)), 100);
			}
		}
		else if (currentLevel - lastLevel < 0)
		{
			if (spriteX >= desireX){
				spriteX = desireX;
				actionTime = 0;
				lastLevel = currentLevel;
			}
			else
			{
				actionTime += Gdx.graphics.getDeltaTime();
				spriteX += (desireX - spriteX) / Math.abs(spriteX - desireX) * Math.min((300 * Math.pow(actionTime + 0.4, 4)), 100);
			}
		}

		showScoresAndTimes(spriteX, 0);
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
		for(int i = 0; i < level_num; i++){
			levelsLabel[i].dispose();
		}
		back_normal.dispose();
		back_hover.dispose();
		back_pressed.dispose();
		next_normal.dispose();
		next_hover.dispose();
		next_pressed.dispose();
		last_normal.dispose();
		last_hover.dispose();
		last_pressed.dispose();
		title.dispose();
		rank_label.dispose();
		cars_label.dispose();
		time_label.dispose();
	}
	
	
	public void showScoresAndTimes(float x, float y)
	{
		for(int i = 0; i < level_num; i++){
			game.batch.draw(levelsLabel[i], x + 500 + i * HIGH_SCORE_SCREEN_WIDTH, y + 1090, 300, 140);
			for (int j = 0; j < 10; j++)
			{
				scoreAndTimeTexture.drawScore(x + 200 + i * HIGH_SCORE_SCREEN_WIDTH, y + 950 - 100 * j, 1 + j, 2);
	
				scoreAndTimeTexture.drawScore(x + 555 + i * HIGH_SCORE_SCREEN_WIDTH, y + 950 - 100 * j, levelsCars[i][j], 3);
	
				scoreAndTimeTexture.drawTime(x + 905 + i * HIGH_SCORE_SCREEN_WIDTH, y + 950 - 100 * j, levelsTime[i][j]);
			}
		}
	}
}
