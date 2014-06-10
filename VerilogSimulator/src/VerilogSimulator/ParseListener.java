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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

public class ParseListener extends Verilog2001BaseListener
{
	private Stack<String>					var_stack;
	private Stack<Integer>					parameter_value_stack;
	private boolean							is_var_identifier;
	private boolean							is_number;
	private boolean							is_var_assign_left;
	private int								MSB_range;
	private int								LSB_range;
	Verilog2001Parser						parser;
	private ArrayList<ParsePort>			ports_list;
	private ArrayList<ParseRegWire>			vars_list;
	private Hashtable<String, ParsePort>	hash_ports;
	private Hashtable<String, ParseRegWire>	hash_vars;

	public ParseListener(
			Verilog2001Parser parser,
			ArrayList<ParsePort> ports_list,
			ArrayList<ParseRegWire> vars_list,
			Hashtable<String, ParsePort> hash_ports,
			Hashtable<String, ParseRegWire> hash_vars)
	{
		this.is_var_identifier = false;
		this.is_number = false;
		this.is_var_assign_left = false;

		this.parser = parser;
		this.ports_list = ports_list;
		this.vars_list = vars_list;
		this.hash_ports = hash_ports;
		this.hash_vars = hash_vars;

		this.var_stack = new Stack<String>();
		this.parameter_value_stack = new Stack<Integer>();
	}

	/* --------------------------------------------------------------------------
	 * -----------
	 * --------------------------------------------------------------
	 * -------------------------- These are listeners to add parameters
	 * ----------
	 * ----------------------------------------------------------------
	 * --------------
	 * ------------------------------------------------------------
	 * -------------------------- */
	@Override
	public void enterParameter_declaration_(
			Verilog2001Parser.Parameter_declaration_Context ctx)
	{
		/* intialize range so that is handled */
		MSB_range = 0;
		LSB_range = 0;
		is_var_identifier = true; /* turn on var stack */
		is_number = true;
	}

	@Override
	public void exitParameter_declaration_(
			Verilog2001Parser.Parameter_declaration_Context ctx)
	{
		ParseRegWire new_var;

		while (!var_stack.empty())
		{
			String name = var_stack.pop();
			int value = parameter_value_stack.pop();

			/* create the var */
			new_var = new ParseRegWire();
			new_var.addParameter(name, MSB_range - LSB_range + 1, value);

			/* add to the hash and list */
			hash_vars.put(name, new_var);
		}
		is_var_identifier = false; /* turn off var stack */
		is_number = false;
	}

	/* --------------------------------------------------------------------------
	 * -----------
	 * --------------------------------------------------------------
	 * -------------------------- These are listeners to add inputs (wires or
	 * reg)
	 * ----------------------------------------------------------------------
	 * ------------------
	 * --------------------------------------------------------
	 * ------------------------------ */
	@Override
	public void enterNet_declaration(
			Verilog2001Parser.Net_declarationContext ctx)
	{
		/* This is a wire declaration such as and handles all of these: wire
		 * clk; wire [1:0]a; wire [4:0]a, b; */

		/* intialize range so that is handled */
		MSB_range = 0;
		LSB_range = 0;
		is_var_identifier = true; /* turn on var stack */
	}

	@Override
	public
			void
			exitNet_declaration(Verilog2001Parser.Net_declarationContext ctx)
	{
		ParseRegWire new_var;

		while (!var_stack.empty())
		{
			String name = var_stack.pop();

			/* create the var */
			new_var = new ParseRegWire();
			new_var.addWire(name, MSB_range - LSB_range + 1);

			/* add to the hash and list */
			vars_list.add(new_var);
			hash_vars.put(name, new_var);
		}
		is_var_identifier = false; /* turn off var stack */
	}

	@Override
	public void enterReg_declaration(
			Verilog2001Parser.Reg_declarationContext ctx)
	{
		/*reg clk; reg [1:0]a; reg [4:0]a, b; */

		/* intialize range so that is handled */
		MSB_range = 0;
		LSB_range = 0;
		is_var_identifier = true; /* turn on var stack */
	}

	@Override
	public
			void
			exitReg_declaration(Verilog2001Parser.Reg_declarationContext ctx)
	{
		ParseRegWire new_var;

		while (!var_stack.empty())
		{
			String name = var_stack.pop();

			/* create the var */
			new_var = new ParseRegWire();
			new_var.addReg(name, MSB_range - LSB_range + 1);

			/* add to the hash and list */
			vars_list.add(new_var);
			hash_vars.put(name, new_var);
		}
		is_var_identifier = false; /* turn off var stack */
	}

	/* --------------------------------------------------------------------------
	 * -----------
	 * --------------------------------------------------------------
	 * -------------------------- These are listeners to add input ports
	 * --------
	 * ------------------------------------------------------------------
	 * --------------
	 * ------------------------------------------------------------
	 * -------------------------- */
	@Override
	public void enterInput_declaration(
			Verilog2001Parser.Input_declarationContext ctx)
	{
		/*input clk; input [1:0]a; input [4:0]a, b; */

		/* intialize range so that is handled */
		MSB_range = 0;
		LSB_range = 0;
		is_var_identifier = true; /* turn on var stack */
	}

	@Override
	public void exitInput_declaration(
			Verilog2001Parser.Input_declarationContext ctx)
	{
		ParsePort new_var;

		while (!var_stack.empty())
		{
			String name = var_stack.pop();

			/* create the var */
			new_var = new ParsePort();
			new_var.addPort(name, MSB_range - LSB_range + 1, true);

			/* add to the hash and list */
			ports_list.add(new_var);
			hash_ports.put(name, new_var);
		}
		is_var_identifier = false; /* turn off var stack */
	}

	/* --------------------------------------------------------------------------
	 * -----------
	 * --------------------------------------------------------------
	 * -------------------------- Number conversions into the stack
	 * --------------
	 * ------------------------------------------------------------
	 * --------------
	 * ------------------------------------------------------------
	 * -------------------------- */
	@Override
	public
			void
			enterDecimal_number(Verilog2001Parser.Decimal_numberContext ctx)
	{
		if (is_number)
		{
			if (ctx.Unsigned_number().size() == 1)
			{
				parameter_value_stack.push(Integer.parseInt(ctx.Unsigned_number().get(0).getText()));
			}
			else
			{
				parameter_value_stack.push(Integer.parseInt(ctx.Unsigned_number().get(1).getText()));
			}
		}
	}

	@Override
	public void enterHex_number(Verilog2001Parser.Hex_numberContext ctx)
	{
		if (is_number)
		{
			parameter_value_stack.push(Integer.parseInt(ctx.Unsigned_number().get(1).getText(), 16));
		}
	}

	@Override
	public void enterOctal_number(Verilog2001Parser.Octal_numberContext ctx)
	{
		if (is_number)
		{
			parameter_value_stack.push(Integer.parseInt(ctx.Unsigned_number().get(1).getText(), 8));
		}
	}

	@Override
	public void enterBinary_number(Verilog2001Parser.Binary_numberContext ctx)
	{
		if (is_number)
		{
			parameter_value_stack.push(Integer.parseInt(ctx.Unsigned_number().get(1).getText(), 2));
		}
	}

	/* --------------------------------------------------------------------------
	 * -----------
	 * --------------------------------------------------------------
	 * -------------------------- These are listeners to add output ports
	 * --------
	 * ------------------------------------------------------------------
	 * --------------
	 * ------------------------------------------------------------
	 * -------------------------- */
	@Override
	public void enterOUTPUT_DECLARATION_NO_REG(
			Verilog2001Parser.OUTPUT_DECLARATION_NO_REGContext ctx)
	{
		/*output clk; output [1:0]a; output [4:0]a, b; */

		/* intialize range so that is handled */
		MSB_range = 0;
		LSB_range = 0;
		is_var_identifier = true; /* turn on var stack */
	}

	@Override
	public void exitOUTPUT_DECLARATION_NO_REG(
			Verilog2001Parser.OUTPUT_DECLARATION_NO_REGContext ctx)
	{
		ParsePort new_var;

		while (!var_stack.empty())
		{
			String name = var_stack.pop();

			/* create the var */
			new_var = new ParsePort();
			new_var.addPort(name, MSB_range - LSB_range + 1, false);

			/* add to the hash and list */
			ports_list.add(new_var);
			hash_ports.put(name, new_var);
		}
		is_var_identifier = false; /* turn off var stack */
	}

	@Override
	public void enterOUTPUT_DECLARATION_REG(
			Verilog2001Parser.OUTPUT_DECLARATION_REGContext ctx)
	{
		/*output reg clk; output reg [1:0]a; output reg [4:0]a, b; */

		/* intialize range so that is handled */
		MSB_range = 0;
		LSB_range = 0;
		is_var_identifier = true; /* turn on var stack */
	}

	@Override
	public void exitOUTPUT_DECLARATION_REG(
			Verilog2001Parser.OUTPUT_DECLARATION_REGContext ctx)
	{
		ParsePort new_port;
		ParseRegWire new_reg;

		while (!var_stack.empty())
		{
			String name = var_stack.pop();

			/* create the var */
			new_reg = new ParseRegWire();
			new_reg.addReg(name, MSB_range - LSB_range + 1);

			/* add to the hash and list */
			vars_list.add(new_reg);
			hash_vars.put(name, new_reg);

			/* create the port */
			new_port = new ParsePort();
			new_port.addPort(name, MSB_range - LSB_range + 1, false);

			/* add to the hash and list */
			ports_list.add(new_port);
			hash_ports.put(name, new_port);
		}
	}

	/* --------------------------------------------------------------------------
	 * -----------
	 * --------------------------------------------------------------
	 * -------------------------- This listener is for identifiers...very common
	 * so turned on and off by parse points
	 * --------------------------------------
	 * --------------------------------------------------
	 * ------------------------
	 * -------------------------------------------------------------- */
	@Override
	public void exitIdentifier(Verilog2001Parser.IdentifierContext ctx)
	{
		if (is_var_identifier || is_var_assign_left)
		{
			var_stack.push(ctx.getText());
		}
	}

	/* --------------------------------------------------------------------------
	 * -----------
	 * --------------------------------------------------------------
	 * -------------------------- These handle ranges in [ : ] returning only
	 * integers
	 * ------------------------------------------------------------------
	 * ----------------------
	 * ----------------------------------------------------
	 * ---------------------------------- */
	@Override
	public void enterMsb_constant_expression(
			Verilog2001Parser.Msb_constant_expressionContext ctx)
	{
		MSB_range = Integer.parseInt(ctx.getText());
	}

	@Override
	public void enterLsb_constant_expression(
			Verilog2001Parser.Lsb_constant_expressionContext ctx)
	{
		LSB_range = Integer.parseInt(ctx.getText());
	}

	/* --------------------------------------------------------------------------
	 * -----------
	 * --------------------------------------------------------------
	 * -------------------------- Traverses all variables on left side and
	 * updates there status...removes outputs from var list and hash
	 * ------------
	 * --------------------------------------------------------------
	 * --------------
	 * ------------------------------------------------------------
	 * -------------------------- */
	@Override
	public void enterVariable_lvalue(
			Verilog2001Parser.Variable_lvalueContext ctx)
	{
		is_var_assign_left = true;
	}

	@Override
	public
			void
			exitVariable_lvalue(Verilog2001Parser.Variable_lvalueContext ctx)
	{
		is_var_assign_left = false;
	}

	@Override
	public void exitBlocking_assignment(
			Verilog2001Parser.Blocking_assignmentContext ctx)
	{
		String name = var_stack.pop();
		ParsePort port = hash_ports.get(name);
		ParseRegWire var = hash_vars.get(name);

		if (port == null)
		{
			var.setCombinational();
		}
		else
		{
			port.updateReg(RegWireType.COMBINATIONAL);

			if (var != null)
			{
				/* remove the output register from the var list */
				hash_vars.remove(name);
				vars_list.remove(var);
			}
		}
	}

	@Override
	public void exitNonblocking_assignment(
			Verilog2001Parser.Nonblocking_assignmentContext ctx)
	{
		String name = var_stack.pop();
		ParsePort port = hash_ports.get(name);
		ParseRegWire var = hash_vars.get(name);

		if (port == null)
		{
			var.setSequential();
		}
		else
		{
			port.updateReg(RegWireType.SEQUENTIAL);

			if (var != null)
			{
				/* remove the output register from the var list */
				hash_vars.remove(name);
				vars_list.remove(var);
			}
		}
	}

	@Override
	public void exitContinuous_assign(
			Verilog2001Parser.Continuous_assignContext ctx)
	{
		String name = var_stack.pop();
		ParsePort port = hash_ports.get(name);
		ParseRegWire var = hash_vars.get(name);

		if (port == null)
		{
			var.setCombinational();
		}
		else
		{
			port.updateReg(RegWireType.COMBINATIONAL);

			if (var != null)
			{
				/* remove the output register from the var list */
				hash_vars.remove(name);
				vars_list.remove(var);
			}
		}
	}
}
