package VerilogSimulator;

import java.util.Hashtable;
import java.util.ArrayList;

public class SimVisitor extends Verilog2001BaseVisitor<Value> 
{
	private boolean is_sequential_sim_cycle;
	private int cycle_time;

	private boolean is_combinational;
	private boolean is_sequential;

	private Hashtable<String, ParsePort> hash_ports;
	private Hashtable<String, ParseRegWire> hash_vars;
	private ArrayList<ParsePort> ports_list;
	private ArrayList<ParseRegWire> vars_list;

	private ArrayList<Integer> output_vector_list;

	private int new_val_idx;
	private int old_val_idx;

	private Integer outN;
	private Integer outS;
	private Integer outE;
	private Integer outW;

	public SimVisitor (ArrayList<ParsePort> ports_list, ArrayList<ParseRegWire> vars_list, Hashtable<String, ParsePort> hash_ports, Hashtable<String, ParseRegWire> hash_vars)
	{
		this.is_combinational = false;
		this.is_sequential = false;

		this.is_sequential_sim_cycle = true;
		this.cycle_time = 0;

		this.new_val_idx = 0;
		this.old_val_idx = 1;

		this.ports_list = ports_list;
		this.vars_list = vars_list;
		this.hash_ports = hash_ports;
		this.hash_vars = hash_vars;

		this.output_vector_list = new ArrayList<Integer>();
		this.output_vector_list.add(outN);
		this.output_vector_list.add(outS);
		this.output_vector_list.add(outE);
		this.output_vector_list.add(outW);
	}

/* -------------------------------------------------------------------------------------	
----------------------------------------------------------------------------------------	
Simulate
----------------------------------------------------------------------------------------	
--------------------------------------------------------------------------------------*/	
	/* toggles the access index into the values */
	public void next_sim_cycle()
	{
		/* increase the cycle stamp */
		cycle_time ++;
		/* toggle the idx for old and new */
		new_val_idx = old_val_idx;
		old_val_idx = (new_val_idx == 1) ? 0 : 1;
		/* toggle between sequential sims and combinational sims */
		is_sequential_sim_cycle = (is_sequential_sim_cycle) ? false : true;
	}

	public void clean_sim_cycle()
	{
		if (!is_sequential_sim_cycle)
		{
			/* Makes sure the sequential registers keep value and catches inferred latches */
			for (int i = 0; i < ports_list.size(); i++)
			{
				ports_list.get(i).seqUpdate(cycle_time, new_val_idx, old_val_idx);
			}
			for (int i = 0; i < vars_list.size(); i++)
			{
				vars_list.get(i).seqUpdate(cycle_time, new_val_idx, old_val_idx);
			}
		}
	}

	/* Greatest to least as in lightsensors[7] = char 0 ... */
	public void update_vector_inputs(String rst, String light_sensors, String level_sensors)
	{
		ParsePort update_port;

		/* rst */
		hash_ports.get("rst").setValue(old_val_idx, getBitValFromString(rst, 0), cycle_time);

		update_port = hash_ports.get("sensor_light");
		for (int i = 7; i >= 0; i--)
		{
			update_port.setBitValue(old_val_idx, i, getBitValFromString(light_sensors, 7-i), cycle_time);
		}

		update_port = hash_ports.get("general_sensors");
		for (int i = 29; i >= 0; i--)
		{
			update_port.setBitValue(old_val_idx, i, getBitValFromString(level_sensors, 29-i), cycle_time);
		}
	}

	public int getBitValFromString(String str, int idx)
	{
		return (str.charAt(idx) == '0' ? 0 : 1);
	}

	public ArrayList<Integer> update_vector_ouputs()
	{
		ParsePort out_port;

		out_port = hash_ports.get("outN");
		output_vector_list.set(0, out_port.getValue(new_val_idx));
		out_port = hash_ports.get("outS");
		output_vector_list.set(1, out_port.getValue(new_val_idx));
		out_port = hash_ports.get("outE");
		output_vector_list.set(2, out_port.getValue(new_val_idx));
		out_port = hash_ports.get("outW");
		output_vector_list.set(3, out_port.getValue(new_val_idx));
		
		return output_vector_list;
	}

/* -------------------------------------------------------------------------------------	
----------------------------------------------------------------------------------------	
Handle the conditional IFs 
----------------------------------------------------------------------------------------	
--------------------------------------------------------------------------------------*/	
	@Override 
	public Value visitConditional_statement(Verilog2001Parser.Conditional_statementContext ctx) 
	{ 
		for (int i = 0; i < ctx.statement().size(); i++)
		{
			if (ctx.expression(i) == null && i == ctx.statement().size()-1)
			{
				/* ELSE statment */
				visit(ctx.statement(i));
				break; 
			}
			else if (visit(ctx.expression(i)).asBoolean())
			{
				/* IF - this if condition is true, then evaluate statement */	
				visit(ctx.statement(i));
				break;
			}
		}

		return null;
	}

	Value case_expression;
	@Override 
	public Value visitCase_statement(Verilog2001Parser.Case_statementContext ctx) 
	{
		case_expression = visit(ctx.expression());

		for (int i = 0; i < ctx.case_item().size(); i++)
		{
			if (visit(ctx.case_item(i)).asBoolean())
			{
				/* If one evaluates true then exit */
				break;
			}
		}

		return null;

	}
	@Override 
	public Value visitFCASE_ITEM(Verilog2001Parser.FCASE_ITEMContext ctx)
	{
		Value constant_expression = visit(ctx.expression());

		if (constant_expression.asInt() == case_expression.asInt())
		{
			visit(ctx.statement());
			return new Value(true);
		}
		return new Value(false);
	}
	@Override 
	public Value visitDEFAULT(Verilog2001Parser.DEFAULTContext ctx) 
	{ 
		/* If we get to the default then it's true */
		visit(ctx.statement());
		return new Value(true);
	}

/* -------------------------------------------------------------------------------------	
----------------------------------------------------------------------------------------	
Handle the always blocks
----------------------------------------------------------------------------------------	
--------------------------------------------------------------------------------------*/	
	@Override 
	public Value visitCOMBONATIONAL_ALWAYS(Verilog2001Parser.COMBONATIONAL_ALWAYSContext ctx) 
	{ 
		is_combinational = true;

		/* visit the children of the always block */
		visitChildren(ctx); 

		is_combinational = false;

		return null;
	}

	@Override 
	public Value visitSEQUENTIAL_ALWAYS(Verilog2001Parser.SEQUENTIAL_ALWAYSContext ctx) 
	{ 
		is_sequential = true;

		/* visit the children of the always block */
		visitChildren(ctx); 

		is_sequential = false;

		return null;
	}

/* -------------------------------------------------------------------------------------	
----------------------------------------------------------------------------------------	
Handle statements
----------------------------------------------------------------------------------------	
--------------------------------------------------------------------------------------*/	
	@Override 
	public Value visitBlocking_assignment(Verilog2001Parser.Blocking_assignmentContext ctx) 
	{ 
		if (!is_combinational)
		{
			System.out.println("ERROR: Blocking Statement in a sequential block");
			return new Value(false);
		}

		//System.out.println("Visit:"+ctx.getText()+" Line:"+ctx.start.getLine());

		Value left = visit(ctx.variable_lvalue());
		Value right = visit(ctx.expression());

		/* Update the data structure with the right value */
		left.setVar(new_val_idx, right.asInt(), cycle_time);

		//System.out.println("AssignBlocking:"+left.getVarName()+" Value = "+right.asInt());

		return null;
	}

	@Override 
	public Value visitNonblocking_assignment(Verilog2001Parser.Nonblocking_assignmentContext ctx) 
	{ 
		if (is_sequential_sim_cycle)
		{
			/* Only store on simulate cycles */
			if (!is_sequential)
			{
				System.out.println("ERROR: Non Blocking Statement in a combinational block");
				return new Value(false);
			}
	
			//System.out.println("Visit:"+ctx.getText());
	
			Value left = visit(ctx.variable_lvalue());
			Value right = visit(ctx.expression());
	
			/* Update the data structure with the right value */
			left.setVar(new_val_idx, right.asInt(), cycle_time);
	
			//System.out.println("AssignNonBlocking:"+left.getVarName()+" Value = "+right);
		}

		return null;
	}


	@Override 
	public Value visitContinuous_assign(Verilog2001Parser.Continuous_assignContext ctx) 
	{ 
		//System.out.println("Continuous Visit:"+ctx.getText()+" Line:"+ctx.start.getLine());

		Value left = visit(ctx.variable_lvalue());
		Value right = visit(ctx.expression());

		/* Update the data structure with the right value */
		left.setVar(new_val_idx, right.asInt(), cycle_time);

		//System.out.println("Continuous assign:"+left.getVarName()+" Value = "+right.asInt());

		return null;
	}

/* -------------------------------------------------------------------------------------	
----------------------------------------------------------------------------------------	
Handle assignment statements
----------------------------------------------------------------------------------------	
--------------------------------------------------------------------------------------*/	
	@Override 
	public Value visitVariable_lvalue(Verilog2001Parser.Variable_lvalueContext ctx) 
	{ 
		String ident = ctx.identifier().getText();
		ParsePort port = hash_ports.get(ident); 
		ParseRegWire regWire = hash_vars.get(ident); 
		Value Size = null;

		if (ctx.range_expression() != null)
		{
			/* Visit the decimal number ... NOTE range is not done here! */
			Size = visit(ctx.range_expression().decimal_number());	
		}

		if (regWire != null)
		{
			return new Value(ValueType.REG_WIRE_T, null, regWire, (Size != null ? Size.asInt() : -1));
		}
		else if (port != null)
		{
			return new Value(ValueType.PORT_T, port, null, (Size != null ? Size.asInt() : -1));
		}

		System.out.println("Error: No left hand side called: "+ident);

		return null;
	}

/* -------------------------------------------------------------------------------------	
----------------------------------------------------------------------------------------	
Handle expressions
----------------------------------------------------------------------------------------	
--------------------------------------------------------------------------------------*/	
	@Override 
	public Value visitUMINUS(Verilog2001Parser.UMINUSContext ctx) 
	{ 
		//System.out.println("UMINUS");
		Value unary = visit(ctx.expression());
		return new Value(Integer.valueOf(-unary.asInt()));

	}
	@Override 
	public Value visitUNOT(Verilog2001Parser.UNOTContext ctx) 
	{ 
		//System.out.println("UNOT");
		Value unary = visit(ctx.expression());
		return new Value(Integer.valueOf(~unary.asInt()));
	}
	@Override 
	public Value visitMULT_DIV_MOD(Verilog2001Parser.MULT_DIV_MODContext ctx) 
	{ 
		//System.out.println("*/%");
		Value left = visit(ctx.expression(0));
		Value right = visit(ctx.expression(1));
		int mask1 = (1 << left.getSize()) - 1;
		int mask2 = (1 << right.getSize()) - 1;

		switch(ctx.op.getType())
		{
			case (Verilog2001Parser.MULT) :
			{
				return new Value(Integer.valueOf(left.asInt() * right.asInt()), (mask1 >= mask2) ? 2*mask1 : 2*mask2); 
			}
			case (Verilog2001Parser.DIV) :
			{
				return new Value(Integer.valueOf(left.asInt() / right.asInt()), (mask1 >= mask2) ? mask1 : mask2); 
			}
			case (Verilog2001Parser.MOD) :
			{
				return new Value(Integer.valueOf(left.asInt() % right.asInt()), (mask1 >= mask2) ? mask1 : mask2); 
			}
			default:
			{
				System.out.println("Error: Not ADD SUB called: "+ctx.getText());
			}
		}		

		return null;

	} 
	@Override 
	public Value visitADD_SUB(Verilog2001Parser.ADD_SUBContext ctx) 
	{ 
		//System.out.println("+-");
		Value left = visit(ctx.expression(0));
		Value right = visit(ctx.expression(1));
		int mask1 = (1 << left.getSize()) - 1;
		int mask2 = (1 << right.getSize()) - 1;

		switch(ctx.op.getType())
		{
			case (Verilog2001Parser.SUB) :
			{
				return new Value(Integer.valueOf(left.asInt() - right.asInt()), (mask1 >= mask2) ? mask1 : mask2); 
			}
			case (Verilog2001Parser.ADD) :
			{
				return new Value(Integer.valueOf(left.asInt() + right.asInt()), (mask1 >= mask2) ? mask1 : mask2); 
			}
			default:
			{
				System.out.println("Error: Not ADD SUB called: "+ctx.getText());
			}
		}		

		return null;

	}
	@Override 
	public Value visitBLOGIC(Verilog2001Parser.BLOGICContext ctx) 
	{ 
		//System.out.println("Binary Logic");
		Value left = visit(ctx.expression(0));
		Value right = visit(ctx.expression(1));
		int mask1 = (1 << left.getSize()) - 1;
		int mask2 = (1 << right.getSize()) - 1;

		switch(ctx.op.getType())
		{
			case (Verilog2001Parser.BITWISE_AND) :
			{
				return new Value(Integer.valueOf(left.asInt() & right.asInt()), (mask1 >= mask2) ? mask1 : mask2); 
			}
			case (Verilog2001Parser.BITWISE_NAND) :
			{
				return new Value(Integer.valueOf(~(left.asInt() & right.asInt())), (mask1 >= mask2) ? mask1 : mask2); 
			}
			case (Verilog2001Parser.BITWISE_OR) :
			{
				return new Value(Integer.valueOf(left.asInt() | right.asInt()), (mask1 >= mask2) ? mask1 : mask2); 
			}
			case (Verilog2001Parser.BITWISE_NOR) :
			{
				return new Value(Integer.valueOf(~(left.asInt() | right.asInt())), (mask1 >= mask2) ? mask1 : mask2); 
			}
			case (Verilog2001Parser.BITWISE_XOR) :
			{
				return new Value(Integer.valueOf(left.asInt() ^ right.asInt()), (mask1 >= mask2) ? mask1 : mask2); 
			}
			case (Verilog2001Parser.BITWISE_XNOR) :
			{
				return new Value(Integer.valueOf(~(left.asInt() ^ right.asInt())), (mask1 >= mask2) ? mask1 : mask2); 
			}
			case (Verilog2001Parser.SHIFT_LEFT) :
			{
				return new Value(Integer.valueOf(left.asInt() << right.asInt()), (mask1 >= mask2) ? mask1 : mask2); 
			}
			case (Verilog2001Parser.SHIFT_RIGHT) :
			{
				return new Value(Integer.valueOf(left.asInt() >> right.asInt()), (mask1 >= mask2) ? mask1 : mask2); 
			}

			default:
			{
				System.out.println("Error: No Binary operator called: "+ctx.getText());
			}
		}		

		return null;
	}
	@Override 
	public Value visitCOMPARES(Verilog2001Parser.COMPARESContext ctx) 
	{ 
		Value left = visit(ctx.expression(0));
		Value right = visit(ctx.expression(1));

		switch(ctx.op.getType())
		{
			case (Verilog2001Parser.NOTEQUAL) :
			{
				return new Value(Boolean.valueOf(left.asInt() != right.asInt())); 
			}
			case (Verilog2001Parser.EQUAL) :
			{
				return new Value(Boolean.valueOf(left.asInt() == right.asInt())); 
			}
			case (Verilog2001Parser.LT) :
			{
				return new Value(Boolean.valueOf(left.asInt() < right.asInt())); 
			}
			case (Verilog2001Parser.LTE) :
			{
				return new Value(Boolean.valueOf(left.asInt() <= right.asInt())); 
			}
			case (Verilog2001Parser.GT) :
			{
				return new Value(Boolean.valueOf(left.asInt() > right.asInt())); 
			}
			case (Verilog2001Parser.GTE) :
			{
				return new Value(Boolean.valueOf(left.asInt() >= right.asInt())); 
			}
			default:
			{
				System.out.println("Error: No lofical operator called: "+ctx.getText());
			}
		}		

		return null;
	}
	@Override 
	public Value visitLNOT(Verilog2001Parser.LNOTContext ctx) 
	{ 
		Value unary = visit(ctx.expression(0));
		return new Value(Boolean.valueOf(!unary.asBoolean()));
	}
	@Override 
	public Value visitLAND(Verilog2001Parser.LANDContext ctx) 
	{ 
		Value left = visit(ctx.expression(0));
		Value right = visit(ctx.expression(1));
		return new Value(Boolean.valueOf(left.asBoolean() && right.asBoolean())); 
	}
	@Override 
	public Value visitLOR(Verilog2001Parser.LORContext ctx) 
	{ 
		Value left = visit(ctx.expression(0));
		Value right = visit(ctx.expression(1));
		return new Value(Boolean.valueOf(left.asBoolean() || right.asBoolean())); 
	}
	@Override 
	public Value visitQUES(Verilog2001Parser.QUESContext ctx) 
	{ 
		System.out.println("?");
		System.out.println("Error: Not implemented yet");
		return null;
	}
	@Override 
	public Value visitBRACKETS(Verilog2001Parser.BRACKETSContext ctx) 
	{ 
		/* just pass results up */
		return visit(ctx.expression()); 
	}

	@Override 
	public Value visitIDENT(Verilog2001Parser.IDENTContext ctx) 
	{ 
		/* Gets numbers */
		Value next = visit(ctx.identifier_types());
		//System.out.println("Visit Expression:"+next.asInt());
		return next; 
	}
	@Override 
	public Value visitBIT_ACCESS(Verilog2001Parser.BIT_ACCESSContext ctx) 
	{ 
		String ident = ctx.identifier().getText();
		ParsePort port = hash_ports.get(ident); 
		ParseRegWire regWire = hash_vars.get(ident); 
		Value number = visit(ctx.constant_expression());

		if (regWire != null)
		{
			int value_of_bit = regWire.getIntegerBit(number.asInt(), old_val_idx); 
			//System.out.println("RegWire bit:"+ident+" loc: "+number.asInt()+" val:"+value_of_bit);
			return new Value (value_of_bit, 1);
		}
		else if (port != null)
		{
			int value_of_bit = port.getIntegerBit(number.asInt(), old_val_idx); 
			//System.out.println("Port bit:"+ident+" loc: "+number.asInt()+" val:"+value_of_bit);
			return new Value (value_of_bit, 1);
		}

		System.out.println("Error: No item called: "+ident);

		return visitChildren(ctx); 
	}
	@Override 
	public Value visitGENERAL(Verilog2001Parser.GENERALContext ctx) 
	{
		String ident = ctx.getText();
		ParsePort port = hash_ports.get(ident); 
		ParseRegWire regWire = hash_vars.get(ident); 

		if (regWire != null)
		{
			//System.out.println("General Ident:"+ident+" val:"+regWire.getValue(old_val_idx));
			return new Value(regWire.getValue(old_val_idx), 30);
		}
		else if (port != null)
		{
			return new Value(port.getValue(old_val_idx), 30);
		}

		System.out.println("Error: No item called: "+ident);

		return null;
	}

	@Override 
	public Value visitINUMBER(Verilog2001Parser.INUMBERContext ctx) 
	{ 
		/* Visit your children and return the appropriate Value - holds size if need be */
		return visitChildren(ctx); 
	}
	@Override 
	public Value visitDecimal_number(Verilog2001Parser.Decimal_numberContext ctx) 
	{ 
		/* get the 2nd list array item and convert to int */
		if (ctx.Unsigned_number().size() == 1)
		{
			return new Value(Integer.parseInt(ctx.Unsigned_number().get(0).getText()), 30);
		}
		else
		{
			return new Value(Integer.parseInt(ctx.Unsigned_number().get(1).getText()), Integer.parseInt(ctx.Unsigned_number().get(0).getText()));
		}
	}
	@Override 
	public Value visitBinary_number(Verilog2001Parser.Binary_numberContext ctx) 
	{ 
		/* get the 2nd list array item and convert to int */
		return new Value(Integer.parseInt(ctx.Unsigned_number().get(1).getText() ,2), Integer.parseInt(ctx.Unsigned_number().get(0).getText())); 
	}
	@Override 
	public Value visitOctal_number(Verilog2001Parser.Octal_numberContext ctx) 
	{ 
		return new Value(Integer.parseInt(ctx.Unsigned_number().get(1).getText() ,8), Integer.parseInt(ctx.Unsigned_number().get(0).getText())); 
	}
	@Override 
	public Value visitHex_number(Verilog2001Parser.Hex_numberContext ctx) 
	{ 
		return new Value(Integer.parseInt(ctx.Unsigned_number().get(1).getText() ,16), Integer.parseInt(ctx.Unsigned_number().get(0).getText())); 
	}
}
