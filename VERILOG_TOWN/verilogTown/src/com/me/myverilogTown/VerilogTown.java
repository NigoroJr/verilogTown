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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URISyntaxException;

import javax.swing.JOptionPane;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeBitmapFontData;

public class VerilogTown extends Game
{
	/** Environment variable set when developing. Non-zero value indicates
	 * development mode. In development mode, the directory structure is
	 * different. */
	public static final String	VERILOG_TOWN_DEVELOPMENT	= "VERILOG_TOWN_DEVELOPMENT";
	public static final String	USER_ID_FILE_NAME			= "userID.txt";
	/** Whether or not to send results and usage of game to a remote server.
	 * Sends data if true, otherwise false. */
	public static boolean		SEND_TO_SERVER				= true;

	private LocalServer			localServer;

	public SpriteBatch			batch;
	public BitmapFont			font;
	private long				startTime;
	private long				totalTime;
	private long				id;

	@Override
	public void create()
	{
		batch = new SpriteBatch();

		Texture.setEnforcePotImages(false); // turns off power of 2 restriction
		// in textures...some OpenGl thing

		// Use LibGDX's default Arial font.
		font = new BitmapFont();

		startTime = System.currentTimeMillis();

		try
		{
			readID();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		if (SEND_TO_SERVER)
		{
			localServer = new LocalServer(id);
			// Start listening to LOCAL connections
			localServer.start();
		}

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
		// Send total time spent playing game to local server
		if (SEND_TO_SERVER)
		{
			totalTime = (System.currentTimeMillis() - startTime) / 1000;

			try
			{
				sendTotalTime(totalTime);
			}
			catch (IOException e)
			{
				e.printStackTrace();
				String mes = "Error communicating with local server";
				JOptionPane.showMessageDialog(null, mes, "Error", JOptionPane.ERROR_MESSAGE);
			}
		}

		// Send usage to remote server
		if (SEND_TO_SERVER)
		{
			try
			{
				localServer.sendUsage();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			localServer.stopLocalServer();
		}

		batch.dispose();
		font.dispose();
	}

	public void readID() throws IOException
	{
		File userIDFile = new File(String.format("%s/%s", VerilogTown.getRootPath(), USER_ID_FILE_NAME));

		if (!userIDFile.exists())
		{
			userIDFile.createNewFile();
			FileWriter out = new FileWriter(userIDFile);
			id = System.currentTimeMillis();
			out.write(Long.toString(id));
			out.close();
		}
		else
		{
			InputStreamReader reader = new InputStreamReader(new FileInputStream(userIDFile));
			BufferedReader br = new BufferedReader(reader);
			id = Long.parseLong(br.readLine());
			br.close();
			reader.close();
		}
	}

	public long getID()
	{
		return id;
	}

	/** Sends the total time that the game was running. This method sends to the
	 * local server, which then sends data to the remote server.
	 * 
	 * @param totalTime
	 *            The total time in seconds that the game was running. */
	public void sendTotalTime(long totalTime) throws IOException
	{
		Socket socket = new Socket(InetAddress.getByName(LocalServer.LOCAL_IP_ADDRESS), LocalServer.LOCAL_PORT);
		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

		dos.writeInt(LocalServer.TYPE_USAGE_TOTAL);
		dos.writeLong(totalTime);
		dos.flush();

		dos.close();
		socket.close();
	}

	/** Returns the root path of this program.
	 * 
	 * @return The root path of this program. */
	public static String getRootPath()
	{
		String path = null;
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
			/* getCanonicalPath() returns a path containing "\", which doesn't
			 * work (even on Windows) when passing the path as a command line
			 * argument. Thus, regular expression <code>\\\b</code> is used to
			 * substitute '\' to '/'. */
			path = new File(path).getCanonicalPath().replaceAll("\\\\\\b", "/");
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return path;
	}

	public static boolean isDevelopment()
	{
		String env = System.getenv(VERILOG_TOWN_DEVELOPMENT);
		return env != null && !env.equals("0");
	}
}
