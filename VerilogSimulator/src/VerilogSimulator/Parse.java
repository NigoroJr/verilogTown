package VerilogSimulator;

import java.io.IOException;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Hashtable;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.*;

public class Parse
{
	private ParseTree root_tree;
	private SimVisitor visitor;

	private ArrayList<ParsePort> ports_list;
	private ArrayList<ParseRegWire> vars_list;
	private Hashtable<String, ParsePort> hash_ports;
	private Hashtable<String, ParseRegWire> hash_vars;

	private ArrayList<Integer> output_vector_list;

	int depth = 0;
	boolean on = false;

	public Parse() 
	{
		hash_ports = new Hashtable<String, ParsePort>();
		hash_vars = new Hashtable<String, ParseRegWire>();
		ports_list = new ArrayList<ParsePort>();
		vars_list = new ArrayList<ParseRegWire>();
	}

	public void compileFile(String fileName) throws IOException
	{
		ANTLRInputStream input = new ANTLRInputStream(new FileInputStream(fileName));
		Verilog2001Lexer lexer = new Verilog2001Lexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		Verilog2001Parser parser = new Verilog2001Parser(tokens);
		ParseTreeWalker walker = new ParseTreeWalker();
		ParseListener listener = new ParseListener(parser, ports_list, vars_list, hash_ports, hash_vars); 

		this.visitor = new SimVisitor(ports_list, vars_list, hash_ports, hash_vars);

		root_tree = parser.module_declaration();
		/* first pass to make all the symbol tables */
		walker.walk(listener, root_tree);

		System.out.println(root_tree.toStringTree(parser));

		/* visitor pass for simulation */
	/*
		System.out.println("1 Cycle");
		sim_cycle("0", "00000000", "000000000000000000000000000000");
		for (int i=0; i < 4; i++)
		{
			System.out.println("Out:"+i+" Val:"+output_vector_list.get(i));
		}
		System.out.println("Combo 1 Cycle");
		sim_cycle("1", "10001000", "000000000000000000000000000000");
		for (int i=0; i < 4; i++)
		{
			System.out.println("Out:"+i+" Val:"+output_vector_list.get(i));
		}
		System.out.println("2 Cycle");
		sim_cycle("1", "10000000", "000000000000000000000000000000");
		for (int i=0; i < 4; i++)
		{
			System.out.println("Out:"+i+" Val:"+output_vector_list.get(i));
		}
	*/
		System.out.println("Reset Cycle");
		sim_cycle("0", "00000000", "000000000000000000000000000000");
		for (int i=0; i < 4; i++)
		{
			System.out.println("Out:"+i+" Val:"+output_vector_list.get(i));
		}
		for (int i = 0; i <600; i++)
		{
			System.out.println(i+" Cycle");
			sim_cycle("1", "00000000", "000000000000000000000000000000");
			for (int j=0; j < 4; j++)
			{
				System.out.println("Out:"+j+" Val:"+output_vector_list.get(j));
			}
		}

	}

	public void sim_cycle(String rst, String light_sensors, String general_sensors)
	{
		visitor.next_sim_cycle();
		visitor.update_vector_inputs(rst, light_sensors, general_sensors);
		visitor.visit(root_tree);
		visitor.clean_sim_cycle();
		output_vector_list = visitor.update_vector_ouputs();
	}
}

