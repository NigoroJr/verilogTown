package verilogTownServer;

/** This is class contains a result of one level in the game verilogTown.
 * 
 * @author Naoki Mizuno */

public class Level
{
	public long	id;
	public int	levelNumber;
	public int	time;
	public int	carsTotal;
	public int	carsPassed;

	// Default constructor
	public Level()
	{
	}

	public Level(
			long id,
			int levelNumber,
			int time,
			int carsTotal,
			int carsPassed)
	{
		this.id = id;
		this.levelNumber = levelNumber;
		this.time = time;
		this.carsTotal = carsTotal;
		this.carsPassed = carsPassed;
	}

	@Override
	public String toString()
	{
		String mes = "";
		mes += "ID: " + id + "\n";
		mes += "Level #: " + levelNumber + "\n";
		mes += "Time :" + time + "\n";
		mes += "Total cars: " + carsTotal + "\n";
		mes += "Passed cars: " + carsPassed + "\n";

		return mes;
	}
}
