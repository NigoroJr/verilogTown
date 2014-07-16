package com.me.myverilogTown;

import java.awt.Color;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeBitmapFontData;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class CreditsScreen implements Screen
{
	private final VerilogTown		game;
	private OrthographicCamera		camera;
	private boolean					isPressed				= false;
	private boolean					wasPressed				= false;

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

	private int						CREDITS_SCREEN_WIDTH	= 1280;
	private int						CREDITS_SCREEN_HEIGHT	= 1380;

	private Texture					back_normal;
	private Texture					back_hover;
	private Texture					back_pressed;
	private TextureButton			back;

	private BitmapFont				mainFont;
	private BitmapFont				nameFont;
	private FreeTypeFontGenerator	fontGenerator;
	private FreeTypeBitmapFontData	mainFontData;
	private FreeTypeBitmapFontData	nameFontData;
	
	private Texture					credits;

	public CreditsScreen(VerilogTown game)
	{
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, CREDITS_SCREEN_WIDTH, CREDITS_SCREEN_HEIGHT);
		
		credits = new Texture("ASSET_RESOURCES/credits.png");
		back_normal = new Texture("ASSET_RESOURCES/back_normal.png");
		back_hover = new Texture("ASSET_RESOURCES/back_mouse_on.png");
		back_pressed = new Texture("ASSET_RESOURCES/back_pressed.png");
		back = new TextureButton(game.batch, 1080, 40, 160, 59, back_normal, back_hover, back_pressed);
		
		fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font/CHILLER.TTF"));
		mainFontData = fontGenerator.generateData(50);
		nameFontData = fontGenerator.generateData(58);
		mainFont = new BitmapFont(mainFontData, mainFontData.getTextureRegions(), false);
		mainFont.setColor((float)0, (float)0, (float)0, (float)1);
		nameFont = new BitmapFont(nameFontData, nameFontData.getTextureRegions(), false);
		nameFont.setColor((float)0, (float)0, (float)0, (float)1);
	}

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0.78f, 0.9f, 0.78f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		pixOfWindowX = CREDITS_SCREEN_WIDTH * camera.zoom;
		pixOfWindowY = CREDITS_SCREEN_HEIGHT * camera.zoom;

		sizeOfWindowX = Gdx.graphics.getWidth();
		sizeOfWindowY = Gdx.graphics.getHeight();

		mousePositionX = Gdx.input.getX();
		mousePositionY = Gdx.input.getY();

		centerX = camera.position.x;
		centerY = camera.position.y;

		realX = (centerX - pixOfWindowX / 2) + ((mousePositionX / sizeOfWindowX) * pixOfWindowX);
		realY = (centerY - pixOfWindowY / 2) + ((1 - mousePositionY / sizeOfWindowY) * pixOfWindowY);

		realX = Math.min(realX, CREDITS_SCREEN_WIDTH);
		realY = Math.min(realY, CREDITS_SCREEN_HEIGHT);

		wasPressed = isPressed;
		isPressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
		
		game.batch.begin();
		game.batch.draw(credits, 450, 1160, 360, 110);
		
		nameFont.drawMultiLine(game.batch, "Dr. Peter Jamieson", 30, 1150);
		mainFont.drawMultiLine(game.batch, "Created the game design, and programmed many parts of the game\n" +
											"including the Verilog Simulation and car movement and path planning.", 120, 1080);
		
		nameFont.draw(game.batch, "Naoki Mizuno", 30, 950);
		mainFont.drawMultiLine(game.batch, "Built the XML level descriptions and a separate tool for level creation.\n" +
											"Naoki is one of the main developers of the Beta release of verilogTown.", 120, 880);

		nameFont.draw(game.batch, "Boyu Zhang", 30, 750);
		mainFont.drawMultiLine(game.batch, "Built the standalone Verilog Editor and structured much of the game control.\n" +
											"Boyu is one of the main developers of the Beta release of verilogTown.", 120, 680);
		
		nameFont.draw(game.batch, "Alex Williams", 30, 550);
		mainFont.drawMultiLine(game.batch, "Built the early version of the Verilog parser using ANTLR 4.0.", 120, 480);
		
		nameFont.draw(game.batch, "Josh Collins", 30, 420);
		mainFont.drawMultiLine(game.batch, "Built the early parts of the game including animation and level.", 120, 350);

		nameFont.draw(game.batch, "Lindsay Grace", 30, 290);
		mainFont.drawMultiLine(game.batch, "Collaborator on game design.", 120, 220);

		nameFont.draw(game.batch, "John-Rhys Garcia", 30, 160);
		mainFont.drawMultiLine(game.batch, "Main artist for tileset and vehicles.", 120, 90);
		
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
		credits.dispose();
		back_normal.dispose();
		back_hover.dispose();
		back_pressed.dispose();
		fontGenerator.dispose();
		mainFont.dispose();
		nameFont.dispose();
	}

}
