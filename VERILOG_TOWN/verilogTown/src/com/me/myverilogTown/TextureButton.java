package com.me.myverilogTown;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/** This class is a button rendered as a Texture. This class works by using
 * filenames consisting of different suffixes preceded by the same "text" name.
 * 
 * @author Naoki Mizuno */

public class TextureButton
{
	public static final int	NORMAL	= 0;
	public static final int	HOVER	= 1;
	public static final int	PRESSED	= 2;

	private SpriteBatch		batch;
	/* Bottom-left corner of this button is (x, y) */
	private int				x, y, width, height;
	private Texture			normal;
	private Texture			hover;
	private Texture			pressed;

	/** Creates a new button as a texture. If the path to the file is:
	 * <code>data/level1_normal.png</code>, <code>location</code> is set to
	 * <code>data</code>, <code>text</code> is set to <code>level1</code>, and
	 * <code>suffixNormal</code> is set to <code>_normal</code>.
	 * 
	 * @param batch
	 *            The SpriteBatch to render to.
	 * @param x
	 *            The X coordinate of the bottom-left corner of this button.
	 * @param y
	 *            The Y coordinate of the bottom-left corner of this button.
	 * @param width
	 *            The width of this button in pixels.
	 * @param height
	 *            The height of this button in pixels.
	 * @param location
	 *            The path to the parent directory of the file. The current
	 *            directory when looking for resources is set to
	 *            <code>verilogTown-android/assets/</code>
	 * @param text
	 *            The text part of the filename.
	 * @param suffixNormal
	 *            The suffix of the filename to render as a normal texture.
	 * @param suffixHover
	 *            The suffix of the filename to render when the mouse is
	 *            hovering on the button.
	 * @param suffixClick
	 *            The suffix of the filename to render when the button is
	 *            clicked. */
	public TextureButton(
			SpriteBatch batch,
			int x,
			int y,
			int width,
			int height,
			Texture normal,
			Texture hover,
			Texture pressed)
	{
		this.batch = batch;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.normal = normal;
		this.hover = hover;
		this.pressed = pressed;
	}



	/** Sets the texture to the appropriate texture according to the state of the
	 * mouse. The state must be either NORMAL, HOVER, or CLICK.
	 * 
	 * @param type
	 *            State of the mouse. Select from NORMAL, HOVER, or CLICK. */
	public void drawTexture(int type)
	{
		switch (type){
		case NORMAL:{
			batch.draw(normal, x, y, width, height);
			break;
		}
		case HOVER :{
			batch.draw(hover, x, y, width, height);
			break;
		}
		case PRESSED:{
			batch.draw(pressed, x, y, width, height);
			break;
		}
		}
		
	}

	/** Returns whether the given x and y coordinates of the mouse is above this
	 * button.
	 * 
	 * @param mouseX
	 *            The X coordinate of the mouse.
	 * @param mouseY
	 *            The Y coordinate of the mouse.
	 * @return True if mouse is on button, false if not. */
	public boolean isOnButton(double mouseX, double mouseY)
	{
		return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}
}
