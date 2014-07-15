package com.me.myverilogTown;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

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

	public static final int		TYPE_RESULT			= 0;
	public static final int		TYPE_USAGE			= 1;
	public static final int		TYPE_USAGE_TOTAL	= 2;
	public static final int		TYPE_USAGE_EDITOR	= 3;

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
		Socket s = new Socket(InetAddress.getByName(REMOTE_IP_ADDRESS), REMOTE_PORT);
		DataOutputStream dos = new DataOutputStream(s.getOutputStream());
		dos.writeLong(id);
		dos.writeInt(TYPE_RESULT);
		dos.writeInt(levelNumber);
		dos.writeInt(time);
		dos.writeInt(carsTotal);
		dos.writeInt(carsPassed);
		dos.flush();
		s.close();
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
		Socket s = new Socket(InetAddress.getByName(REMOTE_IP_ADDRESS), REMOTE_PORT);
		DataOutputStream dos = new DataOutputStream(s.getOutputStream());
		dos.writeLong(id);
		dos.writeInt(TYPE_USAGE);
		dos.writeLong(totalTime);
		dos.writeLong(editorTime);
		dos.flush();
		s.close();
	}
}
