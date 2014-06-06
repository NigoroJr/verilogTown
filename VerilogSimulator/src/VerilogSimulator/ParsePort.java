package VerilogSimulator;

public class ParsePort extends Parse
{
	private String name;
	private int busSize;
	private PortType type;
	private RegWireType r_type;
	private int []value;
	private int cycle_update_time;
	
	public ParsePort()
	{
		this.busSize = 0;
		this.name = null;
		this.type = PortType.NO_TYPE;
		this.r_type = RegWireType.COMBINATIONAL;
		/* initialize two spots for new and old values */
		this.value = new int[2];
		this.value[0] = 0;
		this.value[1] = 0;
		this.cycle_update_time = -1;
	}

	public void addPort(String name, int bus_size, boolean is_input)
	{
		if (is_input)
		{
			this.type = PortType.INPUT;
		}
		else
		{
			this.type = PortType.OUTPUT;
		}

		this.busSize = bus_size;
		this.name = name;
	}

	public void updateReg(RegWireType t)
	{
		assert (this.type == PortType.OUTPUT);
		this.r_type = t;
	}
	public String getName()
	{
		return name;
	}
	
	public int getBusSize() 
	{
		return busSize;
	}

	public int getValue(int idx)
	{
		if (this.type == PortType.INPUT)
		{
			return this.value[0];
		}
		else
		{
			return this.value[idx];
		}
	}

	public void setValue(int idx, int value, int cycle_time)
	{
		int mask = (1 << this.busSize) - 1;

		this.cycle_update_time = cycle_time;

		if (this.type == PortType.INPUT)
		{
			this.value[0] = value & mask;
			this.value[1] = value & mask;
		}
		else
		{
			this.value[idx] = value & mask;
		}
	}
	public void setBitValue(int idx, int bitIdx, int bit_value, int cycle_time)
	{
		int mask = (1 << this.busSize) - 1; // mask for the full number
		int bitMask = (bit_value << bitIdx); // 0 or 1 in the bit spot
		int bitLoc = (1 << bitIdx); // all 0s except a 1 in the bit spot
		int demask = ((1 << this.busSize) - 1) ^ bitLoc; // all 1s except bit spot

		this.cycle_update_time = cycle_time;

		if (this.type == PortType.INPUT)
		{
			this.value[0] = ((this.value[0] & demask) | bitMask) & mask;
			this.value[1] = ((this.value[0] & demask) | bitMask) & mask;
		}
		else
		{
			this.value[idx] = ((this.value[idx] & demask) | bitMask) & mask;
		}
	}

	public int getIntegerBit(int idx_bit, int idx)
	{
		int mask = (1 << idx_bit);
		int ac_value = value[idx];
		return ((ac_value & mask) > 0) ? 1 : 0;
	}

	public void seqUpdate(int cycle_time, int new_val_idx, int old_val_idx)
	{
		if (type == PortType.OUTPUT)
		{
			if (cycle_time != this.cycle_update_time) 
			{
				/* IF there has been no update for this value */
				if (r_type == RegWireType.COMBINATIONAL)
				{
					System.out.println("Error: Inferring latch");	
				}
				else if (r_type == RegWireType.SEQUENTIAL)
				{
					this.value[new_val_idx] = this.value[old_val_idx];
				}
			}
		}
	}
}
