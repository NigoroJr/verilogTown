package com.me.myverilogTown;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/** This class is a server that runs locally and sits between the game and the
 * remote server. This server/client is used to send level results and the game
 * usage information to the remote server. The game usage is the time in total
 * that the user spent playing the game and the time spent in the editor. The
 * usage is used for academic purpose.
 * 
 * @author Naoki Mizuno */

public class LocalServer extends Thread
{
	public static final String	REMOTE_IP_ADDRESS	= "127.0.0.1";
	public static final int		REMOTE_PORT			= 32150;
	public static final String	LOCAL_IP_ADDRESS	= "127.0.0.1";
	public static final int		LOCAL_PORT			= 32151;
	private String				remoteIP;
	private int					remotePort;

	public static final int		TYPE_RESULT			= 0;
	public static final int		TYPE_USAGE			= 1;
	public static final int		TYPE_USAGE_TOTAL	= 2;
	public static final int		TYPE_USAGE_EDITOR	= 3;

	public static final String	CONFIG_FILE_NAME	= "server.conf";

	private long				id;
	private boolean				localServerRunning;
	private long				totalTime;
	private long				editorTime;

	private ServerSocket		serverSocket;

	public LocalServer(long id)
	{
		this.id = id;
		this.localServerRunning = true;
		this.totalTime = 0;
		this.editorTime = 0;
		this.remoteIP = "";
		this.remotePort = -1;

		readConfig();

		try
		{
			serverSocket = new ServerSocket(LOCAL_PORT);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void run()
	{
		try
		{
			waitForData();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/** Waits for data (either the total time from the game or the editor time
	 * from the editor).
	 * 
	 * @throws IOException
	 *             When there is an error when closing the ServerSocket. */
	private void waitForData() throws IOException
	{
		while (localServerRunning)
		{
			try
			{
				Socket connection = serverSocket.accept();
				handleConnection(connection);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		serverSocket.close();
	}

	private void handleConnection(Socket socket) throws IOException
	{
		DataInputStream dis = new DataInputStream(socket.getInputStream());

		int type = dis.readInt();
		switch (type)
		{
			case TYPE_RESULT:
				int levelNumber = dis.readInt();
				int time = dis.readInt();
				int carsTotal = dis.readInt();
				int carsPassed = dis.readInt();
				// Send level result to remote server
				sendResult(levelNumber, time, carsTotal, carsPassed);
			break;
			case TYPE_USAGE_TOTAL:
				totalTime += dis.readLong();
			break;
			case TYPE_USAGE_EDITOR:
				editorTime += dis.readLong();
			break;
		}
		dis.close();
	}

	/** Stops the local server. */
	public void stopLocalServer()
	{
		localServerRunning = false;
	}

	/** Sends the given result to the remote server.
	 * 
	 * @param levelNumber
	 * @param time
	 *            Time it took to complete this level.
	 * @param carsTotal
	 *            Total cars that appeared in this level.
	 * @param carsPassed
	 *            Total number of cars that passed.
	 * @throws IOException */
	public void sendResult(
			int levelNumber,
			int time,
			int carsTotal,
			int carsPassed) throws IOException
	{
		if (remoteIP.equals(""))
			remoteIP = REMOTE_IP_ADDRESS;
		if (remotePort == -1)
			remotePort = REMOTE_PORT;
		Socket s = new Socket(InetAddress.getByName(remoteIP), remotePort);
		DataOutputStream dos = new DataOutputStream(s.getOutputStream());
		dos.writeLong(id);
		dos.writeInt(TYPE_RESULT);
		dos.writeInt(levelNumber);
		dos.writeInt(time);
		dos.writeInt(carsTotal);
		dos.writeInt(carsPassed);
		dos.flush();
		s.close();

		System.out.println("Send following level result to remote server:");
		System.out.println("Level Number: " + levelNumber);
		System.out.println("Time: " + time);
		System.out.println("Cars total: " + carsTotal);
		System.out.println("Cars passed: " + carsPassed);
	}

	public void sendUsage() throws IOException
	{
		sendUsage(totalTime, editorTime);
	}

	/** Sends the usage of the game and editor to remote server.
	 * 
	 * @param totalTime
	 *            The total time spent in the game, including the time spent in
	 *            editor. Unit is seconds.
	 * @param editorTime
	 *            The total time spent in the editor. The time when the editor
	 *            loses focus is not included in the count. Unit is seconds.
	 * @throws IOException */
	public void sendUsage(long totalTime, long editorTime) throws IOException
	{
		if (remoteIP.equals(""))
			remoteIP = REMOTE_IP_ADDRESS;
		if (remotePort == -1)
			remotePort = REMOTE_PORT;
		Socket s = new Socket(InetAddress.getByName(remoteIP), remotePort);
		DataOutputStream dos = new DataOutputStream(s.getOutputStream());
		dos.writeLong(id);
		dos.writeInt(TYPE_USAGE);
		dos.writeLong(totalTime);
		dos.writeLong(editorTime);
		dos.flush();
		s.close();

		System.out.println("Send following information to remote server:");
		System.out.println("Total: " + totalTime);
		System.out.println("Editor: " + editorTime);
	}

	/** Reads the configuration file in the root directory of the project. */
	public void readConfig()
	{
		File configFile = new File(String.format("%s/%s", VerilogTown.getRootPath(), CONFIG_FILE_NAME));
		try
		{
			Scanner scanner = new Scanner(configFile);

			while (scanner.hasNext())
			{
				String line = scanner.nextLine();
				try
				{
					processLine(line);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
					// Ignore
				}
			}

			scanner.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	/** Reads in a line and updates the variables according to the configuration.
	 * Lines that start with '#' are skipped.
	 * 
	 * @param line
	 *            Line from the configuration file.
	 * @throws ArrayIndexOutOfBoundsException
	 *             When the entry has a key but doesn't have a value. */
	private void processLine(String line) throws ArrayIndexOutOfBoundsException
	{
		if (line.trim().isEmpty() || line.trim().startsWith("#"))
			return;

		String[] entry = line.split(":");
		switch (entry[0])
		{
			case "IP":
				remoteIP = entry[1];
			break;
			case "Port":
				remotePort = Integer.parseInt(entry[1]);
			break;
			case "SendUsage":
				if (entry[1].equals("false"))
					VerilogTown.SEND_TO_SERVER = false;
			break;
		}
	}
}
