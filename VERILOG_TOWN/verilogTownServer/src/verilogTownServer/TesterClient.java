package verilogTownServer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class TesterClient
{
	public static void main(String[] args)
	{
		try
		{
			testResult();

			testUsage();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static void testResult() throws IOException
	{
		Socket s = new Socket(InetAddress.getByName("127.0.0.1"), VerilogTownServer.SERVER_PORT);

		long id = 1234567890;
		int levelNumber = 9;
		int time = 18;
		int carsTotal = 24;
		int carsPassed = 21;

		DataOutputStream dos = new DataOutputStream(s.getOutputStream());
		dos.writeLong(id);
		dos.writeInt(VerilogTownServer.TYPE_RESULT);
		dos.writeInt(levelNumber);
		dos.writeInt(time);
		dos.writeInt(carsTotal);
		dos.writeInt(carsPassed);
		dos.flush();

		s.close();
	}

	private static void testUsage() throws IOException
	{
		Socket s = new Socket(InetAddress.getByName("127.0.0.1"), VerilogTownServer.SERVER_PORT);

		long id = 1234567890;
		long total = 2580;
		long editor = 1789;

		DataOutputStream dos = new DataOutputStream(s.getOutputStream());
		dos.writeLong(id);
		dos.writeInt(VerilogTownServer.TYPE_USAGE);
		dos.writeLong(total);
		dos.writeLong(editor);
		dos.flush();

		s.close();
	}
}
