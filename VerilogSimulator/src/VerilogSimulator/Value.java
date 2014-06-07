package VerilogSimulator;

public class Value 
{
	public static Value VOID = new Value(new Object());
	private Object value;
	private int size;

	private ParseRegWire regWire;
	private ParsePort port;
	private int idx;
	private ValueType v_type;
	
	public Value(Object value) 
	{
		this.value = value;
		this.size = -1;
		this.v_type = ValueType.NULL_T;
	}

	public Value(Object value, int size) 
	{
		this.value = value;
		this.size = size;
		this.v_type = ValueType.INTEGER_T;
		int mask;

		if (size != -1)
		{
			if (size > 30)
				this.size = 30;
			
			/* Mask based on size */
			mask = (1 << (size)) - 1;
			this.value = asInt() & mask;
		}
	}
	public Value(ValueType v, ParsePort port, ParseRegWire regWire, int idx)
	{
		this.value = null;
		this.size = -1;
		this.v_type = v;
		this.idx = idx;
		this.regWire = regWire;
		this.port = port;
	}

	public String getVarName()
	{
		if (this.v_type == ValueType.PORT_T)
		{
			return port.getName();
		}
		else if (this.v_type == ValueType.REG_WIRE_T)
		{
			return regWire.getName();
		}
		return null;
	}

	public void setVar(int idx, int value, int cycle_time)
	{
		if (this.v_type == ValueType.PORT_T)
		{
			port.setValue(idx, value, cycle_time);
		}
		else if (this.v_type == ValueType.REG_WIRE_T)
		{
			regWire.setValue(idx, value, cycle_time);
		}
	}

	public void setInteger(int value, int size)
	{
		int mask;
		this.size = size;
		this.v_type = ValueType.INTEGER_T;

		if (size != -1)
		{
			/* Mask based on size */
			mask = (1 << (size)) - 1;
			this.value = value & mask;
		}
	}

	public int getIntegerBit(int idx)
	{
		int mask = (1 << (size-1));
		int ac_value = asInt();
		return ((ac_value & mask) > 0) ? 1 : 0;
	}
	
	public Boolean asBoolean() 
	{
		if (value instanceof Integer)
		{
			if ((int)value == 0)
				return false;
			else
				return true;
		}

		return (Boolean)value;
	}
	
	public int asInt() 
	{
		if (value instanceof Boolean)
		{
			if ((Boolean)value == true)
				return 1;
			else
				return 0;
		}

		return (int)value;
	}
	public String asString() 
	{
		return String.valueOf(value);
	}

	public int getSize()
	{
		return this.size;
	}
	
	@Override
	public boolean equals(Object o) 
	{
		if(value == o) 
		{
			return true;
		}
		
		if(value == null || o == null || o.getClass() != value.getClass()) 
		{
			return false;
		}
		
		Value that = (Value)o;
	
		return this.value.equals(that.value);
	}
	
	@Override
	public String toString() 
	{
		return String.valueOf(value);
	}
}
