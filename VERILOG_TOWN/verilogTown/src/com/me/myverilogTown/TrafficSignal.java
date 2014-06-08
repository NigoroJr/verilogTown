package com.me.myverilogTown;

public enum TrafficSignal
{
	STOP,
	GO_FORWARD,
	GO_LEFT,
	GO_RIGHT,
	GO,
	NO_SIGNAL;

	public static TrafficSignal fromInt(int x) 
	{
        	switch(x) 
		{
		        case 0:
				return STOP;
			case 1:
				return GO_FORWARD;
			case 2:
				return GO_LEFT;
			case 3:
				return GO_RIGHT;
			case 4:
				return GO;
	        }
	        return null;
	}
}
