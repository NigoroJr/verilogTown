package com.me.myverilogTown;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenu implements Screen
{
    final verilogTown game;
    OrthographicCamera camera;
    Texture title, title2, level1;
    TextButtonStyle buttonStyle;
    BitmapFont font;
    Skin skin;
    Stage stage;
    TextureAtlas buttonAtlas;
    SpriteBatch batch;

    public MainMenu(final verilogTown gam)
    {
        game = gam;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 1280);
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        title2 = new Texture(Gdx.files.internal("ASSET_RESOURCES/title2.png"));
        
        // Generate the buttons using the corresponding methods..
        // generateTextButton creates a text button with the target text as the text displayed
        // generateImageButton creates an image button with the image at the target image's path as the displayed image
        // For some reason, I ran into rendering errors when these methods were in their own class.
        
        final TextButton tutorialButton = generateTextButton("Tutorial");
        // These are just two images I made in MSPaint quickly...so they need replaced.
        final Image levelOneButton = generateImageButton("ASSET_RESOURCES/level1.png");
        final Image levelTwoButton = generateImageButton("ASSET_RESOURCES/level2.png");
        
        // Position and size must be set manually for text buttons.
        // for image buttons, just the position - the generate method will automatically set the size as the input image's size.
        
        tutorialButton.setPosition(400, 200);
        tutorialButton.setSize(150, 75);
        //For some reason, when the levelOneButton is activated and the new game screen loads, the window changes size. 
        levelOneButton.setPosition(50, 400);
        //When the levelTwoButton is activated, however, the windows does not. 
        //Their code should be exactly the same, so I have no idea what is happening. 
        levelTwoButton.setPosition(975, 400);
        
        // As well as adding them to the stage as an actor.       
        stage.addActor(tutorialButton);
        stage.addActor(levelOneButton);
        stage.addActor(levelTwoButton);
        
        // Finally, the input processor needs set to the 'stage' of the buttons        
        Gdx.input.setInputProcessor(stage);
        // Listeners need manually coded for each button. I couldn't find an easy way around this. 
        // Additionally, ClickListener only seems to work for Images. For text, ChangeListener is necessary.
		levelOneButton.addListener(new ClickListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
				game.setScreen(new LevelScreen((game)));
				dispose();
				return true;
			}
		});
		
		levelTwoButton.addListener(new ClickListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
				game.setScreen(new LevelScreen((game)));
				dispose();
				return true;
			}
		});
		
		// Change listener for text buttons...	
		tutorialButton.addListener(new ChangeListener(){	
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Stuff here
				
			}
		});
		
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        // the stage MUST BE DRAWN outside of the screen's batch, otherwise rendering errors occur with textures.
        stage.draw();
        game.batch.begin();
		game.batch.draw(title2, 325, 600);
        //game.font.draw(game.batch, "Welcome to Verilog Town!!! ", 100, 150);
        //game.font.draw(game.batch, "Click anywhere to begin!", 100, 100);
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
    
    /*Generates a button with the image located at imagePath.
     * Does not create an event listener or set location, this must be done manually.
     * Automatically sets the button's size as the size of the input image.
     * ClickListeners seem to work better than ChangeListeners in regards to these
     * image buttons. 
     */
    
    public Image generateImageButton(String imagePath){
    	Texture temp = new Texture(imagePath);
    	Image output = new Image(temp);
    	output.setWidth(temp.getWidth());
    	output.setHeight(temp.getHeight());
    	return output;	
    }
    
    /*Generates a text button with the desired text on it.
     *  Does not create an event listener - this must be done manually.
     *  Does not set size - depending on the amount of text on the button, size 
     *  can vary wildly. After generation, call the buttonName.setSize(x dim, y dim) function.
     *  Click listeners don't seem to work with text buttons; instead ChangeListeners are necessary. 
     */
    
    public TextButton generateTextButton(String text){
		font = new BitmapFont();
		skin = new Skin();
		stage = new Stage();
		batch = new SpriteBatch();
		TextButton output;
		Pixmap pixmap = new Pixmap(100,100, Format.RGBA8888);
		pixmap.setColor(Color.GREEN);
		pixmap.fill();
		skin.add("white", new Texture(pixmap));
		BitmapFont font = new BitmapFont();
		font.scale(1);
		skin.add("default",font);
		
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
		textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
		textButtonStyle.font = skin.getFont("default");
		skin.add("default", textButtonStyle);
		output = new TextButton(text, textButtonStyle);
		return output;
    }
}
