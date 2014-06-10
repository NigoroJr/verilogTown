package VerilogSimulator;

import java.io.IOException;

public class VerilogMain
{
	public static void main(String[] args) throws IOException
	{
		Parse compiler = new Parse();

		/* Read the directory we are pointing to */
		System.out.println(System.getProperty("user.dir"));
		/* Parse the file into a ATS */
		compiler.compileFile("stop_light_seq.v");
//		compiler.compileFile("stop_light_combo.v");
		/* now call the simulator */
//		ParseToSimTable simulator = new ParseToSimTable(module);
		System.out.println("Finished");
	}
}
