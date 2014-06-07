package VerilogSimulator;

import java.io.IOException;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.*;

import javax.swing.*;

public class Parse
{
	private ParseTree root_tree;
	private SimVisitor visitor;

	private ArrayList<ParsePort> ports_list;
	private ArrayList<ParseRegWire> vars_list;
	private Hashtable<String, ParsePort> hash_ports;
	private Hashtable<String, ParseRegWire> hash_vars;

	private ArrayList<Integer> output_vector_list;

	private Boolean is_compiled = false;
	private Boolean is_no_parse_errors = true;

	private JTextPane errorText;

	public Parse() 
	{
		hash_ports = new Hashtable<String, ParsePort>();
		hash_vars = new Hashtable<String, ParseRegWire>();
		ports_list = new ArrayList<ParsePort>();
		vars_list = new ArrayList<ParseRegWire>();
	}
	
	/* Constructor for Editor ... needs error pane to display syntax errors */
	public Parse(JTextPane errorText) 
	{
		hash_ports = new Hashtable<String, ParsePort>();
		hash_vars = new Hashtable<String, ParseRegWire>();
		ports_list = new ArrayList<ParsePort>();
		vars_list = new ArrayList<ParseRegWire>();

		this.errorText = errorText;
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
		is_compiled = true;
	}

	public void compileFileForEditor(String fileName) throws IOException
	{
		ANTLRInputStream input = new ANTLRInputStream(new FileInputStream(fileName));
		Verilog2001Lexer lexer = new Verilog2001Lexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		Verilog2001Parser parser = new Verilog2001Parser(tokens);
		ParseTreeWalker walker = new ParseTreeWalker();
		ParseListener listener = new ParseListener(parser, ports_list, vars_list, hash_ports, hash_vars); 

		this.visitor = new SimVisitor(ports_list, vars_list, hash_ports, hash_vars);

		is_no_parse_errors = true;

		parser.removeErrorListeners();
		parser.addErrorListener(new VerboseListenerE());
		root_tree = parser.module_declaration();
		/* first pass to make all the symbol tables */
		walker.walk(listener, root_tree);

		if (is_no_parse_errors)
		{
			is_compiled = true;
		}
		else
		{
			is_compiled = false;
		}
	}

	public ArrayList<Integer> sim_cycle(String rst, String light_sensors, String general_sensors)
	{
		if (is_compiled)
		{
			visitor.next_sim_cycle();
			visitor.update_vector_inputs(rst, light_sensors, general_sensors);
			visitor.visit(root_tree);
			visitor.clean_sim_cycle();
			output_vector_list = visitor.update_vector_ouputs();
	
			return output_vector_list;
		}

		return null;
	}

	public Boolean is_compiled_yet()
	{
		return is_compiled;
	}
	
	public class VerboseListenerE extends BaseErrorListener 
	{
		@Override
		public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e)
		{
			String old_text;
			List<String> stack = ((Parser)recognizer).getRuleInvocationStack();
			Collections.reverse(stack);
			old_text = errorText.getText();
			errorText.setText(old_text+"\nError at line "+line+":"+charPositionInLine+" at "+ offendingSymbol+": "+msg);
			/* flag found parse error */
			is_no_parse_errors = false;
		}
	}
}

