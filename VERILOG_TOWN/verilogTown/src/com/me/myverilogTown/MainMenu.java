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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Input;

public class MainMenu implements Screen

{
	public final VerilogTown	game;
	private OrthographicCamera	camera;
	private Texture				title;
	private Texture				level1_normal;
	private Texture				level1_mouse_on;
	private Texture				level1_pressed;
	private Texture				level2_normal;
	private Texture				level2_mouse_on;
	private Texture				level2_pressed;
	private Texture				tutorial_normal;
	private Texture				tutorial_mouse_on;
	private Texture				tutorial_pressed;
	private Texture				credits_normal;
	private Texture				credits_mouse_on;
	private Texture				credits_pressed;

	private static final int	MAINMENU_WIDTH	= 1280;
	private static final int	MAINMENU_HEIGHT	= 1280;

	private boolean				isPressed		= false;
	private boolean				wasPressed		= false;

	public double				pixOfWindowX;
	public double				pixOfWindowY;

	public double				sizeOfWindowX;
	public double				sizeOfWindowY;

	public double				mousePositionX;
	public double				mousePositionY;

	public double				centerX;
	public double				centerY;

	public double				realX;
	public double				realY;

	public MainMenu(final VerilogTown gam)
	{
		game = gam;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, MAINMENU_WIDTH, MAINMENU_HEIGHT);

		title = new Texture(Gdx.files.internal("ASSET_RESOURCES/title2.png"));

		level1_normal = new Texture("ASSET_RESOURCES/level1_normal.png");
		level1_mouse_on = new Texture("ASSET_RESOURCES/level1_mouse_on.png");
		level1_pressed = new Texture("ASSET_RESOURCES/level1_pressed.png");

		level2_normal = new Texture("ASSET_RESOURCES/level2_normal.png");
		level2_mouse_on = new Texture("ASSET_RESOURCES/level2_mouse_on.png");
		level2_pressed = new Texture("ASSET_RESOURCES/level2_pressed.png");

		tutorial_normal = new Texture("ASSET_RESOURCES/tutorial_normal.png");
		tutorial_mouse_on = new Texture("ASSET_RESOURCES/tutorial_mouse_on.png");
		tutorial_pressed = new Texture("ASSET_RESOURCES/tutorial_pressed.png");

		credits_normal = new Texture("ASSET_RESOURCES/credits_normal.png");
		credits_mouse_on = new Texture("ASSET_RESOURCES/credits_mouse_on.png");
		credits_pressed = new Texture("ASSET_RESOURCES/credits_pressed.png");
	}

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0f, 0f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		pixOfWindowX = MAINMENU_WIDTH * camera.zoom;
		pixOfWindowY = MAINMENU_HEIGHT * camera.zoom;

		sizeOfWindowX = Gdx.graphics.getWidth();
		sizeOfWindowY = Gdx.graphics.getHeight();

		mousePositionX = Gdx.input.getX();
		mousePositionY = Gdx.input.getY();

		centerX = camera.position.x;
		centerY = camera.position.y;

		realX = (centerX - pixOfWindowX / 2) + ((mousePositionX / sizeOfWindowX) * pixOfWindowX);
		realY = (centerY - pixOfWindowY / 2) + ((1 - mousePositionY / sizeOfWindowY) * pixOfWindowY);

		realX = Math.min(realX, MAINMENU_WIDTH);
		realY = Math.min(realY, MAINMENU_HEIGHT);

		wasPressed = isPressed;
		isPressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		// the stage MUST BE DRAWN outside of the screen's batch, otherwise
		// rendering errors occur with textures.

		game.batch.begin();

		game.batch.draw(title, 325, 900);
		if (realX >= 514 && realX <= 763 && realY >= 805 && realY <= 863)
		{
			if (isPressed)
			{
				game.batch.draw(level1_pressed, 512, 700);
			}
			// If clicked
			else if (!isPressed && wasPressed)
			{
				game.setScreen(new LevelScreen((game)));
			}
			else
			{
				game.batch.draw(level1_mouse_on, 512, 700);
			}
		}
		else
			game.batch.draw(level1_normal, 512, 700);

		if (realX >= 514 && realX <= 763 && realY >= 705 && realY <= 763)
		{
			if (isPressed)
			{
				game.batch.draw(level2_pressed, 512, 600);
			}
			// If clicked
			else if (!isPressed && wasPressed)
			{
				game.setScreen(new LevelScreen((game)));
			}
			else
			{
				game.batch.draw(level2_mouse_on, 512, 600);
			}
		}
		else
		{
			game.batch.draw(level2_normal, 512, 600);
		}

		if (realX >= 514 && realX <= 763 && realY >= 605 && realY <= 663)
		{
			if (isPressed)
			{
				game.batch.draw(tutorial_pressed, 512, 500);
			}
			// If clicked
			else if (!isPressed && wasPressed)
			{
				// show the tutorial screen here
			}
			else
			{
				game.batch.draw(tutorial_mouse_on, 512, 500);
			}
		}
		else
		{
			game.batch.draw(tutorial_normal, 512, 500);
		}

		if (realX >= 514 && realX <= 763 && realY >= 505 && realY <= 563)
		{
			if (isPressed)
			{
				game.batch.draw(credits_pressed, 512, 400);
			}
			else if (!isPressed && wasPressed)
			{
				// show the credits screen here
			}
			else
			{
				game.batch.draw(credits_mouse_on, 512, 400);
			}
		}
		else
		{
			game.batch.draw(credits_normal, 512, 400);
		}

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
