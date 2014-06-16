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

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;

public class VerilogTown extends Game
{
	/** Environment variable set when developing. Non-zero value indicates
	 * development mode. In development mode, the directory structure is
	 * different. */
	public static final String	VERILOG_TOWN_DEVELOPMENT	= "VERILOG_TOWN_DEVELOPMENT";

	public SpriteBatch			batch;
	public BitmapFont			font;

	@Override
	public void create()
	{
		batch = new SpriteBatch();

		Texture.setEnforcePotImages(false); // turns off power of 2 restriction
		// in textures...some OpenGl thing

		// Use LibGDX's default Arial font.
		font = new BitmapFont();
		this.setScreen(new MainMenu(this));
	}

	@Override
	public void render()
	{
		super.render(); // important!
	}

	@Override
	public void dispose()
	{
		batch.dispose();
		font.dispose();
	}

	/** Returns the root path of this program.
	 * 
	 * @return The root path of this program. */
	public static String getRootPath()
	{
		String path = "";
		try
		{
			path = VerilogTown.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			// Path can be a filename when executing a jar file. (filename/../)
			// doesn't work.
			path = new File(path).getParent() + "/../";
			// Development environment has different directory structure than
			// that when releasing
			if (isDevelopment())
				path += "../";

			return new File(path).getCanonicalPath();
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public static boolean isDevelopment()
	{
		String env = System.getenv(VERILOG_TOWN_DEVELOPMENT);
		return env != null && !env.equals("0");
	}
}
