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

package VerilogSimulator;

public class ParseRegWire extends Parse
{
	private String		name;
	private int			busSize;
	private RegWireType	type;
	private int[]		value;
	private int			cycle_update_time;

	public ParseRegWire()
	{
		this.busSize = 0;
		this.name = null;
		this.type = RegWireType.NO_TYPE;
		/* initialize two spots for new and old values */
		this.value = new int[2];
		this.value[0] = 0;
		this.value[1] = 0;
		this.cycle_update_time = -1;
	}

	public void addReg(String name, int bus_size)
	{
		this.type = RegWireType.REG; // will be changed later to COMB or SEQ
		this.busSize = bus_size;
		this.name = name;
	}

	public void addWire(String name, int bus_size)
	{
		this.type = RegWireType.COMBINATIONAL;
		this.busSize = bus_size;
		this.name = name;
	}

	public void addParameter(String name, int bus_size, int constant_value)
	{
		this.type = RegWireType.PARAMETER;
		this.busSize = bus_size;
		this.name = name;
		this.value[0] = constant_value;
		this.value[1] = constant_value;
	}

	public void setCombinational()
	{
		this.type = RegWireType.COMBINATIONAL;
	}

	public void setSequential()
	{
		this.type = RegWireType.SEQUENTIAL;
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
		return this.value[idx];
	}

	public void setValue(int idx, int value, int cycle_time)
	{
		int mask = (1 << this.busSize) - 1;

		this.value[idx] = value & mask;

		this.cycle_update_time = cycle_time;
	}

	public void setBitValue(int idx, int bitIdx, int bit_value, int cycle_time)
	{
		int mask = (1 << this.busSize) - 1; // mask for the full number
		int bitMask = (bit_value << bitIdx); // 0 or 1 in the bit spot
		int bitLoc = (1 << bitIdx); // all 0s except a 1 in the bit spot
		int demask = ((1 << this.busSize) - 1) ^ bitLoc; // all 1s except bit
															// spot

		this.cycle_update_time = cycle_time;

		this.value[idx] = ((this.value[idx] & demask) | bitMask) & mask;
	}

	public int getIntegerBit(int idx_bit, int idx)
	{
		int mask = (1 << idx_bit);
		int ac_value = value[idx];
		return ((ac_value & mask) > 0) ? 1 : 0;
	}

	public void seqUpdate(int cycle_time, int new_val_idx, int old_val_idx)
	{
		if (cycle_time != this.cycle_update_time)
		{
			/* IF there has been no update for this value */
			if (type == RegWireType.COMBINATIONAL)
			{
				System.out.println("Error: Inferring latch");
			}
			else if (type == RegWireType.SEQUENTIAL)
			{
				this.value[new_val_idx] = this.value[old_val_idx];
			}
		}
	}
}
