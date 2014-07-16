package verilogTownServer;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ConnectionHandler
{
	private Socket			socket;
	private DataInputStream	dis;

	private long			id;

	public ConnectionHandler(Socket socket)
	{
		System.out.println("Handle connection with: " + socket.toString());
		this.socket = socket;
		try
		{
			dis = new DataInputStream(socket.getInputStream());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/** Handles the connection by receiving data according to the TYPE. After
	 * receiving the data, it closes the connection to the client.
	 * 
	 * @throws IOException */
	public void handleConnection() throws IOException
	{
		id = dis.readLong();
		int type = dis.readInt();

		switch (type)
		{
			case VerilogTownServer.TYPE_RESULT:
				Level level = new Level();
				receiveResult(level);
			break;
			case VerilogTownServer.TYPE_USAGE:
				receiveUsage();
			break;
		}

		closeConnection();
	}

	/** Waits for a result from the client. The result consists of:
	 * 
	 * <pre>
	 * [       ID       |  TYPE  |  LV #  |  TIME  |  TOTAL |  PASS  ]
	 * [      long      |  int   |  int   |  int   |  int   |  int   ]
	 * </pre>
	 * 
	 * "Time" is in seconds, "total" and "pass" are the number of cars that
	 * appeared and successfully reached its destination, respectively.
	 * 
	 * @param level
	 *            The Level object to put the received information into.
	 * @throws IOException */
	private void receiveResult(Level level) throws IOException
	{
		level.id = id;
		// Note: ID and TYPE are read prior to this
		level.levelNumber = dis.readInt();
		level.time = dis.readInt();
		level.carsTotal = dis.readInt();
		level.carsPassed = dis.readInt();

		// DEBUG
		System.out.println(level);
	}

	/** Waits for a usage from the client.
	 * 
	 * <pre>
	 * [       ID       |  TYPE  |      TOTAL     |     EDITOR     ]
	 * [      long      |  int   |      long      |      long      ]
	 * </pre>
	 * 
	 * "Total" and "editor" are the time spent in seconds using the game and the
	 * Verilog editor, respectively. */
	private void receiveUsage() throws IOException
	{
		// Note: ID and TYPE are read prior to this
		long total = dis.readLong();
		long editor = dis.readLong();

		// DEBUG
		System.out.println("TOTAL: " + total);
		System.out.println("EDITOR: " + editor);
	}

	/** Closes the DataInputStream and the Socket. */
	public void closeConnection()
	{
		System.out.println("Close connection");
		try
		{
			if (dis != null)
				dis.close();
			if (socket != null && !socket.isClosed())
				socket.close();
		}
		catch (IOException e)
		{
			System.err.println("Error when closing connection:");
			e.printStackTrace();
		}
	}
}
