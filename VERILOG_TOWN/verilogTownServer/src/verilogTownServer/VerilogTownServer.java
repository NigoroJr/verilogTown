package verilogTownServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class VerilogTownServer
{
	public static final int	SERVER_PORT	= 32150;

	/* Types of connections */
	/** Receive data about level result */
	public static final int	TYPE_RESULT	= 0;
	/** Receive data about game and editor usage */
	public static final int	TYPE_USAGE	= 1;

	private ServerSocket	serverSocket;

	public VerilogTownServer()
	{
		try
		{
			serverSocket = new ServerSocket(SERVER_PORT);

			waitForConnection();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/** Goes into an infinite loop that waits for connection and handles the
	 * requests from the client once a connection is established. */
	private void waitForConnection()
	{
		while (true)
		{
			try
			{
				Socket s = serverSocket.accept();
				new ConnectionHandler(s).handleConnection();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args)
	{
		new VerilogTownServer();
	}
}
