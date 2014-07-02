package com.me.myverilogTown;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

public class LocalHighScoreScreen implements Screen
{
	private final VerilogTown	game;
	private OrthographicCamera	camera;

	private int					level1Cars[];
	private int					level2Cars[];
	private int					level3Cars[];
	private int					level1Time[];
	private int					level2Time[];
	private int					level3Time[];

	private TextureButton		level1Button;
	private TextureButton		level2Button;
	private TextureButton		level3Button;
	private TextureButton		back;

	private Texture				level1normal;
	private Texture				level1hover;
	private Texture				level1pressed;

	private Texture				level2normal;
	private Texture				level2hover;
	private Texture				level2pressed;

	private Texture				level3normal;
	private Texture				level3hover;
	private Texture				level3pressed;

	private Texture				back_normal;
	private Texture				back_hover;
	private Texture				back_pressed;

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

	private final int			STATE1						= 1;
	private final int			STATE2						= 2;
	private final int			STATE3						= 3;

	private int					currentState;
	private int					lastState;

	private float				spriteX;
	private float				desireX;
	private double				actionTime;

	public LocalHighScoreScreen(VerilogTown game)
	{
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, HIGH_SCORE_SCREEN_WIDTH, HIGH_SCORE_SCREEN_HEIGHT);
		currentState = STATE1;
		lastState = STATE1;

		level1Cars = new int[10];
		level2Cars = new int[10];
		level3Cars = new int[10];
		level1Time = new int[10];
		level2Time = new int[10];
		level3Time = new int[10];
		File highScoreLevel1 = new File(VerilogTown.getRootPath() + "/Levels/Lv1/high_score.txt");
		File highScoreLevel2 = new File(VerilogTown.getRootPath() + "/Levels/Lv2/high_score.txt");
		File highScoreLevel3 = new File(VerilogTown.getRootPath() + "/Levels/Lv3/high_score.txt");
		try
		{
			InputStreamReader reader1 = new InputStreamReader(new FileInputStream(highScoreLevel1));
			InputStreamReader reader2 = new InputStreamReader(new FileInputStream(highScoreLevel2));
			InputStreamReader reader3 = new InputStreamReader(new FileInputStream(highScoreLevel3));
			BufferedReader br1 = new BufferedReader(reader1);
			BufferedReader br2 = new BufferedReader(reader2);
			BufferedReader br3 = new BufferedReader(reader3);
			String temp = null;
			for (int i = 0; i < 10; i++)
			{
				if ((temp = br1.readLine()) != null)
				{
					level1Cars[i] = Integer.parseInt(temp.substring(0, temp.indexOf(" ")));
					level1Time[i] = Integer.parseInt(temp.substring(temp.indexOf(" ") + 1));
				}
				else
				{
					level1Cars[i] = 0;
					level1Time[i] = 0;
				}

				if ((temp = br2.readLine()) != null)
				{
					level2Cars[i] = Integer.parseInt(temp.substring(0, temp.indexOf(" ")));
					level2Time[i] = Integer.parseInt(temp.substring(temp.indexOf(" ") + 1));
				}
				else
				{
					level2Cars[i] = 0;
					level2Time[i] = 0;
				}

				if ((temp = br3.readLine()) != null)
				{
					level3Cars[i] = Integer.parseInt(temp.substring(0, temp.indexOf(" ")));
					level3Time[i] = Integer.parseInt(temp.substring(temp.indexOf(" ") + 1));
				}
				else
				{
					level3Cars[i] = 0;
					level3Time[i] = 0;
				}
			}
			reader1.close();
			reader2.close();
			reader3.close();
			br1.close();
			br2.close();
			br3.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		level1normal = new Texture("ASSET_RESOURCES/score_level1_normal.png");
		level1hover = new Texture("ASSET_RESOURCES/score_level1_mouse_on.png");
		level1pressed = new Texture("ASSET_RESOURCES/score_level1_pressed.png");

		level2normal = new Texture("ASSET_RESOURCES/score_level2_normal.png");
		level2hover = new Texture("ASSET_RESOURCES/score_level2_mouse_on.png");
		level2pressed = new Texture("ASSET_RESOURCES/score_level2_pressed.png");

		level3normal = new Texture("ASSET_RESOURCES/score_level3_normal.png");
		level3hover = new Texture("ASSET_RESOURCES/score_level3_mouse_on.png");
		level3pressed = new Texture("ASSET_RESOURCES/score_level3_pressed.png");

		back_normal = new Texture("ASSET_RESOURCES/back_normal.png");
		back_hover = new Texture("ASSET_RESOURCES/back_mouse_on.png");
		back_pressed = new Texture("ASSET_RESOURCES/back_pressed.png");

		title = new Texture("ASSET_RESOURCES/local_high_score.png");
		rank_label = new Texture("ASSET_RESOURCES/rank.png");
		cars_label = new Texture("ASSET_RESOURCES/cars.png");
		time_label = new Texture("ASSET_RESOURCES/time.png");

		level1Button = new TextureButton(game.batch, 135, 1110, 240, 120, level1normal, level1hover, level1pressed);
		level2Button = new TextureButton(game.batch, 535, 1110, 240, 120, level2normal, level2hover, level2pressed);
		level3Button = new TextureButton(game.batch, 935, 1110, 240, 120, level3normal, level3hover, level3pressed);
		back = new TextureButton(game.batch, 30, 45, 160, 59, back_normal, back_hover, back_pressed);

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

		game.batch.draw(title, 280, 1130, 750, 280);
		game.batch.draw(rank_label, 180, 1037, 150, 84);
		game.batch.draw(cars_label, 578, 1037, 150, 84);
		game.batch.draw(time_label, 975, 1037, 150, 84);

		if (level1Button.isOnButton(realX, realY))
		{
			if (isPressed)
				level1Button.drawTexture(TextureButton.PRESSED);
			else if (wasPressed)
			{
				lastState = currentState;
				currentState = STATE1;
				desireX = (currentState - 1) * HIGH_SCORE_SCREEN_WIDTH;
				actionTime = 0;
			}
			else
				level1Button.drawTexture(TextureButton.HOVER);
		}
		else
			level1Button.drawTexture(TextureButton.NORMAL);

		if (level2Button.isOnButton(realX, realY))
		{
			if (isPressed)
				level2Button.drawTexture(TextureButton.PRESSED);
			else if (wasPressed)
			{
				lastState = currentState;
				currentState = STATE2;
				desireX = -(currentState - 1) * HIGH_SCORE_SCREEN_WIDTH;
				actionTime = 0;
			}
			else
				level2Button.drawTexture(TextureButton.HOVER);
		}
		else
			level2Button.drawTexture(TextureButton.NORMAL);

		if (level3Button.isOnButton(realX, realY))
		{
			if (isPressed)
				level3Button.drawTexture(TextureButton.PRESSED);
			else if (wasPressed)
			{
				lastState = currentState;
				currentState = STATE3;
				desireX = -(currentState - 1) * HIGH_SCORE_SCREEN_WIDTH;
				actionTime = 0;
			}
			else
				level3Button.drawTexture(TextureButton.HOVER);
		}
		else
			level3Button.drawTexture(TextureButton.NORMAL);

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

		if (currentState == STATE1)
			level1Button.drawTexture(TextureButton.PRESSED);
		else if (currentState == STATE2)
			level2Button.drawTexture(TextureButton.PRESSED);
		else if (currentState == STATE3)
			level3Button.drawTexture(TextureButton.PRESSED);

		if (currentState - lastState > 0)
		{
			if (spriteX <= desireX)
			{
				spriteX = desireX;
			}
			else
			{
				actionTime += Gdx.graphics.getDeltaTime();
				spriteX += (desireX - spriteX) / Math.abs(spriteX - desireX) * (300 * Math.pow(actionTime + 0.4, 4));
			}
		}
		else if (currentState - lastState < 0)
		{
			if (spriteX >= desireX)
				spriteX = desireX;
			else
			{
				actionTime += Gdx.graphics.getDeltaTime();
				spriteX += (desireX - spriteX) / Math.abs(spriteX - desireX) * (300 * Math.pow(actionTime + 0.4, 4));
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
	}

	public void showScoresAndTimes(float x, float y)
	{
		for (int i = 0; i < 10; i++)
		{
			scoreAndTimeTexture.drawScore(x + 200, y + 950 - 100 * i, 1 + i, 2);
			scoreAndTimeTexture.drawScore(x + 200 + HIGH_SCORE_SCREEN_WIDTH, y + 950 - 100 * i, i + 1, 2);
			scoreAndTimeTexture.drawScore(x + 200 + 2 * HIGH_SCORE_SCREEN_WIDTH, y + 950 - 100 * i, i + 1, 2);

			scoreAndTimeTexture.drawScore(x + 575, y + 950 - 100 * i, level1Cars[i], 3);
			scoreAndTimeTexture.drawScore(x + 575 + HIGH_SCORE_SCREEN_WIDTH, y + 950 - 100 * i, level2Cars[i], 3);
			scoreAndTimeTexture.drawScore(x + 575 + 2 * HIGH_SCORE_SCREEN_WIDTH, y + 950 - 100 * i, level3Cars[i], 3);

			scoreAndTimeTexture.drawTime(x + 945, y + 950 - 100 * i, level1Time[i]);
			scoreAndTimeTexture.drawTime(x + 945 + HIGH_SCORE_SCREEN_WIDTH, y + 950 - 100 * i, level2Time[i]);
			scoreAndTimeTexture.drawTime(x + 945 + 2 * HIGH_SCORE_SCREEN_WIDTH, y + 950 - 100 * i, level3Time[i]);
		}
	}
}
