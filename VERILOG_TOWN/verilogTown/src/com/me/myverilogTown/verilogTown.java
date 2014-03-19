package com.me.myverilogTown;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;

public class verilogTown extends Game
{
    public SpriteBatch batch;
    public BitmapFont font;

    public void create()
    {
        batch = new SpriteBatch();

        Texture.setEnforcePotImages(false); // turns off power of 2 restriction
                                            // in textures...some OpenGl thing

        // Use LibGDX's default Arial font.
        font = new BitmapFont();
        this.setScreen(new MainMenu(this));

    }

    public void render()
    {
        super.render(); // important!
    }

    public void dispose()
    {
        batch.dispose();
        font.dispose();
    }
}
