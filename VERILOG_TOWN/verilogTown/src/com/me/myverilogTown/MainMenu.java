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
	private TextureButton		level1;
	private TextureButton		level2;
	private TextureButton		tutorial;
	private TextureButton		credits;
	
	private Texture				level1_normal;
	private Texture				level1_hover;
	private Texture				level1_pressed;
	
	private Texture				level2_normal;				
	private Texture				level2_hover;
	private Texture				level2_pressed;
	
	private Texture				tutorial_normal;				
	private Texture				tutorial_hover;
	private Texture				tutorial_pressed;
	
	private Texture				credits_normal;
	private Texture				credits_hover;
	private Texture				credits_pressed;
	

	public MainMenu(final VerilogTown gam)
	{
		game = gam;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, MAINMENU_WIDTH, MAINMENU_HEIGHT);

		title = new Texture(Gdx.files.internal("ASSET_RESOURCES/welcom_to_verilogtown.png"));

		select_level = new Texture("ASSET_RESOURCES/select_a_level.png");
		
		level1_normal = new Texture("ASSET_RESOURCES/level1_normal.png");
		level1_hover = new Texture("ASSET_RESOURCES/level1_mouse_on.png");
		level1_pressed = new Texture("ASSET_RESOURCES/level1_pressed.png");

		level2_normal = new Texture("ASSET_RESOURCES/level2_normal.png");
		level2_hover = new Texture("ASSET_RESOURCES/level2_mouse_on.png");
		level2_pressed = new Texture("ASSET_RESOURCES/level2_pressed.png");
		
		tutorial_normal = new Texture("ASSET_RESOURCES/tutorial_normal.png");
		tutorial_hover = new Texture("ASSET_RESOURCES/tutorial_mouse_on.png");
		tutorial_pressed = new Texture("ASSET_RESOURCES/tutorial_pressed.png");
		
		credits_normal = new Texture("ASSET_RESOURCES/credits_normal.png");
		credits_hover = new Texture("ASSET_RESOURCES/credits_mouse_on.png");
		credits_pressed = new Texture("ASSET_RESOURCES/credits_pressed.png");
		
		level1 = new TextureButton(game.batch, 512, 805, 250, 60, level1_normal, level1_hover, level1_pressed);
		level2 = new TextureButton(game.batch, 512, 705, 250, 60, level2_normal, level2_hover, level2_pressed);
		tutorial = new TextureButton(game.batch, 512, 605, 250, 60, tutorial_normal, tutorial_hover, tutorial_pressed);
		credits = new TextureButton(game.batch, 512, 505, 250, 60, credits_normal, credits_hover, credits_pressed);

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

		// Level 1 button
		if (level1.isOnButton(realX, realY))
		{
			if (isPressed)
				level1.drawTexture(TextureButton.PRESSED);
			else if (wasPressed){
				this.dispose();
				game.setScreen(new LevelScreen(game, 1));
			}
			else
				level1.drawTexture(TextureButton.HOVER);
		}
		else
			level1.drawTexture(TextureButton.NORMAL);

		// Level 2 button
		if (level2.isOnButton(realX, realY))
		{
			if (isPressed)
				level2.drawTexture(TextureButton.PRESSED);
			else if (wasPressed){
				this.dispose();
				game.setScreen(new LevelScreen(game, 2));
			}
			else
				level2.drawTexture(TextureButton.HOVER);
		}
		else
			level2.drawTexture(TextureButton.NORMAL);

		// Tutorial button
		if (tutorial.isOnButton(realX, realY))
		{
			if (isPressed)
				tutorial.drawTexture(TextureButton.PRESSED);
			else if (wasPressed){
				this.dispose();
				game.setScreen(new LevelScreen(game, 0));
			}
			else
				tutorial.drawTexture(TextureButton.HOVER);
		}
		else
			tutorial.drawTexture(TextureButton.NORMAL);

		// Credits button
		if (credits.isOnButton(realX, realY))
		{
			if (isPressed)
				credits.drawTexture(TextureButton.PRESSED);
			else if (wasPressed){
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
	}
}
