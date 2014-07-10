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
	private static final int	level_num 		= 12;
	
	public final VerilogTown	game;
	private OrthographicCamera	camera;
	private Texture				title;
	private Texture				select_level;
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

	private double				time;
	private TextureButton		levels[];
	private TextureButton		tutorial;
	private TextureButton		credits;
	private TextureButton		high_score;

	private Texture				levels_normal[];
	private Texture				levels_hover[];
	private Texture				levels_pressed[];

	private Texture				tutorial_normal;
	private Texture				tutorial_hover;
	private Texture				tutorial_pressed;

	private Texture				credits_normal;
	private Texture				credits_hover;
	private Texture				credits_pressed;

	private Texture				high_score_normal;
	private Texture				high_score_hover;
	private Texture				high_score_pressed;

	public MainMenu(final VerilogTown gam)
	{
		game = gam;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, MAINMENU_WIDTH, MAINMENU_HEIGHT);

		title = new Texture(Gdx.files.internal("ASSET_RESOURCES/welcom_to_verilogtown.png"));

		select_level = new Texture("ASSET_RESOURCES/select_a_level.png");
		
		levels_normal = new Texture[level_num];
		levels_hover = new Texture[level_num];
		levels_pressed = new Texture[level_num];
		for(int i = 0; i < level_num; i++){
			levels_normal[i] = new Texture("ASSET_RESOURCES/level" + (i + 1) + "_normal.png");
			levels_hover[i] = new Texture("ASSET_RESOURCES/level" + (i + 1) + "_mouse_on.png");
			levels_pressed[i] = new Texture("ASSET_RESOURCES/level" + (i + 1) + "_pressed.png");	
		}

		tutorial_normal = new Texture("ASSET_RESOURCES/tutorial_normal.png");
		tutorial_hover = new Texture("ASSET_RESOURCES/tutorial_mouse_on.png");
		tutorial_pressed = new Texture("ASSET_RESOURCES/tutorial_pressed.png");
		
		credits_normal = new Texture("ASSET_RESOURCES/credits_normal.png");
		credits_hover = new Texture("ASSET_RESOURCES/credits_mouse_on.png");
		credits_pressed = new Texture("ASSET_RESOURCES/credits_pressed.png");

		high_score_normal = new Texture("ASSET_RESOURCES/high_score_normal.png");
		high_score_hover = new Texture("ASSET_RESOURCES/high_score_mouse_on.png");
		high_score_pressed = new Texture("ASSET_RESOURCES/high_score_pressed.png");

		levels = new TextureButton[level_num];
		for(int i = 0; i < level_num; i++){
			levels[i] = new TextureButton(game.batch, 162 + (i % 3) * 350, 705 - 100 * (i / 3), 250, 60, levels_normal[i], levels_hover[i], levels_pressed[i]);
		}
		
		tutorial = new TextureButton(game.batch, 512, 805, 250, 60, tutorial_normal, tutorial_hover, tutorial_pressed);
		high_score = new TextureButton(game.batch, 512, 305, 250, 60, high_score_normal, high_score_hover, high_score_pressed);
		credits = new TextureButton(game.batch, 512, 205, 250, 60, credits_normal, credits_hover, credits_pressed);

		time = 0;
	}

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0.78f, 0.9f, 0.78f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		time += Gdx.graphics.getDeltaTime();

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

		game.batch.begin();

		game.batch.draw(title, 200, 950, 800, 200);

		if ((int) (time * 1.3) % 2 == 0)
			game.batch.draw(select_level, 370, 850, 540, 120);

		for(int i = 0; i < level_num; i++){
			if (levels[i].isOnButton(realX, realY))
			{
				if (isPressed)
					levels[i].drawTexture(TextureButton.PRESSED);
				else if (wasPressed)
				{
					this.dispose();
					game.setScreen(new LevelScreen(game, i + 1));
				}
				else
					levels[i].drawTexture(TextureButton.HOVER);
			}
			else
				levels[i].drawTexture(TextureButton.NORMAL);
		}
		
		// Tutorial button
		if (tutorial.isOnButton(realX, realY))
		{
			if (isPressed)
				tutorial.drawTexture(TextureButton.PRESSED);
			else if (wasPressed)
			{
				this.dispose();
				game.setScreen(new LevelScreen(game, 0));
			}
			else
				tutorial.drawTexture(TextureButton.HOVER);
		}
		else
			tutorial.drawTexture(TextureButton.NORMAL);

		if (high_score.isOnButton(realX, realY))
		{
			if (isPressed)
				high_score.drawTexture(TextureButton.PRESSED);
			else if (wasPressed)
			{
				this.dispose();
				// show the high score screen here
				game.setScreen(new LocalHighScoreScreen(game));
			}
			else
				high_score.drawTexture(TextureButton.HOVER);
		}
		else
			high_score.drawTexture(TextureButton.NORMAL);

		// Credits button
		if (credits.isOnButton(realX, realY))
		{
			if (isPressed)
				credits.drawTexture(TextureButton.PRESSED);
			else if (wasPressed)
			{
				this.dispose();
				// TODO: Show credits
			}
			else
				credits.drawTexture(TextureButton.HOVER);
		}
		else
			credits.drawTexture(TextureButton.NORMAL);

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
			levels_normal[i].dispose();
			levels_hover[i].dispose();
			levels_pressed[i].dispose();
		}
		tutorial_normal.dispose();
		tutorial_hover.dispose();
		tutorial_pressed.dispose();
		credits_normal.dispose();
		credits_hover.dispose();
		credits_pressed.dispose();
		high_score_normal.dispose();
		high_score_hover.dispose();
		high_score_pressed.dispose();
		title.dispose();
		select_level.dispose();
	}
}
