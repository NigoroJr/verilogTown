// Generated from Verilog2001.g4 by ANTLR 4.2.2
package VerilogSimulator;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link Verilog2001Parser}.
 */
public interface Verilog2001Listener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#LOR}.
	 * @param ctx the parse tree
	 */
	void enterLOR(@NotNull Verilog2001Parser.LORContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#LOR}.
	 * @param ctx the parse tree
	 */
	void exitLOR(@NotNull Verilog2001Parser.LORContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#nonblocking_assignment}.
	 * @param ctx the parse tree
	 */
	void enterNonblocking_assignment(@NotNull Verilog2001Parser.Nonblocking_assignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#nonblocking_assignment}.
	 * @param ctx the parse tree
	 */
	void exitNonblocking_assignment(@NotNull Verilog2001Parser.Nonblocking_assignmentContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#module_instance}.
	 * @param ctx the parse tree
	 */
	void enterModule_instance(@NotNull Verilog2001Parser.Module_instanceContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#module_instance}.
	 * @param ctx the parse tree
	 */
	void exitModule_instance(@NotNull Verilog2001Parser.Module_instanceContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#topmodule_identifier}.
	 * @param ctx the parse tree
	 */
	void enterTopmodule_identifier(@NotNull Verilog2001Parser.Topmodule_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#topmodule_identifier}.
	 * @param ctx the parse tree
	 */
	void exitTopmodule_identifier(@NotNull Verilog2001Parser.Topmodule_identifierContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#ADD_SUB}.
	 * @param ctx the parse tree
	 */
	void enterADD_SUB(@NotNull Verilog2001Parser.ADD_SUBContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#ADD_SUB}.
	 * @param ctx the parse tree
	 */
	void exitADD_SUB(@NotNull Verilog2001Parser.ADD_SUBContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#blocking_assignment}.
	 * @param ctx the parse tree
	 */
	void enterBlocking_assignment(@NotNull Verilog2001Parser.Blocking_assignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#blocking_assignment}.
	 * @param ctx the parse tree
	 */
	void exitBlocking_assignment(@NotNull Verilog2001Parser.Blocking_assignmentContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#description}.
	 * @param ctx the parse tree
	 */
	void enterDescription(@NotNull Verilog2001Parser.DescriptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#description}.
	 * @param ctx the parse tree
	 */
	void exitDescription(@NotNull Verilog2001Parser.DescriptionContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#conditional_statement}.
	 * @param ctx the parse tree
	 */
	void enterConditional_statement(@NotNull Verilog2001Parser.Conditional_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#conditional_statement}.
	 * @param ctx the parse tree
	 */
	void exitConditional_statement(@NotNull Verilog2001Parser.Conditional_statementContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#hex_number}.
	 * @param ctx the parse tree
	 */
	void enterHex_number(@NotNull Verilog2001Parser.Hex_numberContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#hex_number}.
	 * @param ctx the parse tree
	 */
	void exitHex_number(@NotNull Verilog2001Parser.Hex_numberContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#octal_number}.
	 * @param ctx the parse tree
	 */
	void enterOctal_number(@NotNull Verilog2001Parser.Octal_numberContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#octal_number}.
	 * @param ctx the parse tree
	 */
	void exitOctal_number(@NotNull Verilog2001Parser.Octal_numberContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#range_expression}.
	 * @param ctx the parse tree
	 */
	void enterRange_expression(@NotNull Verilog2001Parser.Range_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#range_expression}.
	 * @param ctx the parse tree
	 */
	void exitRange_expression(@NotNull Verilog2001Parser.Range_expressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#COMBONATIONAL_ALWAYS}.
	 * @param ctx the parse tree
	 */
	void enterCOMBONATIONAL_ALWAYS(@NotNull Verilog2001Parser.COMBONATIONAL_ALWAYSContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#COMBONATIONAL_ALWAYS}.
	 * @param ctx the parse tree
	 */
	void exitCOMBONATIONAL_ALWAYS(@NotNull Verilog2001Parser.COMBONATIONAL_ALWAYSContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#BRACKETS}.
	 * @param ctx the parse tree
	 */
	void enterBRACKETS(@NotNull Verilog2001Parser.BRACKETSContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#BRACKETS}.
	 * @param ctx the parse tree
	 */
	void exitBRACKETS(@NotNull Verilog2001Parser.BRACKETSContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#BIT_ACCESS}.
	 * @param ctx the parse tree
	 */
	void enterBIT_ACCESS(@NotNull Verilog2001Parser.BIT_ACCESSContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#BIT_ACCESS}.
	 * @param ctx the parse tree
	 */
	void exitBIT_ACCESS(@NotNull Verilog2001Parser.BIT_ACCESSContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#LNOT}.
	 * @param ctx the parse tree
	 */
	void enterLNOT(@NotNull Verilog2001Parser.LNOTContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#LNOT}.
	 * @param ctx the parse tree
	 */
	void exitLNOT(@NotNull Verilog2001Parser.LNOTContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#case_statement}.
	 * @param ctx the parse tree
	 */
	void enterCase_statement(@NotNull Verilog2001Parser.Case_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#case_statement}.
	 * @param ctx the parse tree
	 */
	void exitCase_statement(@NotNull Verilog2001Parser.Case_statementContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#number}.
	 * @param ctx the parse tree
	 */
	void enterNumber(@NotNull Verilog2001Parser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#number}.
	 * @param ctx the parse tree
	 */
	void exitNumber(@NotNull Verilog2001Parser.NumberContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#parameter_declaration_}.
	 * @param ctx the parse tree
	 */
	void enterParameter_declaration_(@NotNull Verilog2001Parser.Parameter_declaration_Context ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#parameter_declaration_}.
	 * @param ctx the parse tree
	 */
	void exitParameter_declaration_(@NotNull Verilog2001Parser.Parameter_declaration_Context ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#lsb_constant_expression}.
	 * @param ctx the parse tree
	 */
	void enterLsb_constant_expression(@NotNull Verilog2001Parser.Lsb_constant_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#lsb_constant_expression}.
	 * @param ctx the parse tree
	 */
	void exitLsb_constant_expression(@NotNull Verilog2001Parser.Lsb_constant_expressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(@NotNull Verilog2001Parser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(@NotNull Verilog2001Parser.StatementContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#IDENT}.
	 * @param ctx the parse tree
	 */
	void enterIDENT(@NotNull Verilog2001Parser.IDENTContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#IDENT}.
	 * @param ctx the parse tree
	 */
	void exitIDENT(@NotNull Verilog2001Parser.IDENTContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#SEQUENTIAL_ALWAYS}.
	 * @param ctx the parse tree
	 */
	void enterSEQUENTIAL_ALWAYS(@NotNull Verilog2001Parser.SEQUENTIAL_ALWAYSContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#SEQUENTIAL_ALWAYS}.
	 * @param ctx the parse tree
	 */
	void exitSEQUENTIAL_ALWAYS(@NotNull Verilog2001Parser.SEQUENTIAL_ALWAYSContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#module_instantiation}.
	 * @param ctx the parse tree
	 */
	void enterModule_instantiation(@NotNull Verilog2001Parser.Module_instantiationContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#module_instantiation}.
	 * @param ctx the parse tree
	 */
	void exitModule_instantiation(@NotNull Verilog2001Parser.Module_instantiationContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#inst_name}.
	 * @param ctx the parse tree
	 */
	void enterInst_name(@NotNull Verilog2001Parser.Inst_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#inst_name}.
	 * @param ctx the parse tree
	 */
	void exitInst_name(@NotNull Verilog2001Parser.Inst_nameContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#reg_declaration}.
	 * @param ctx the parse tree
	 */
	void enterReg_declaration(@NotNull Verilog2001Parser.Reg_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#reg_declaration}.
	 * @param ctx the parse tree
	 */
	void exitReg_declaration(@NotNull Verilog2001Parser.Reg_declarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#variable_lvalue}.
	 * @param ctx the parse tree
	 */
	void enterVariable_lvalue(@NotNull Verilog2001Parser.Variable_lvalueContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#variable_lvalue}.
	 * @param ctx the parse tree
	 */
	void exitVariable_lvalue(@NotNull Verilog2001Parser.Variable_lvalueContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#port_reference}.
	 * @param ctx the parse tree
	 */
	void enterPort_reference(@NotNull Verilog2001Parser.Port_referenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#port_reference}.
	 * @param ctx the parse tree
	 */
	void exitPort_reference(@NotNull Verilog2001Parser.Port_referenceContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#module_instance_identifier}.
	 * @param ctx the parse tree
	 */
	void enterModule_instance_identifier(@NotNull Verilog2001Parser.Module_instance_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#module_instance_identifier}.
	 * @param ctx the parse tree
	 */
	void exitModule_instance_identifier(@NotNull Verilog2001Parser.Module_instance_identifierContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#module_item}.
	 * @param ctx the parse tree
	 */
	void enterModule_item(@NotNull Verilog2001Parser.Module_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#module_item}.
	 * @param ctx the parse tree
	 */
	void exitModule_item(@NotNull Verilog2001Parser.Module_itemContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#FCASE_ITEM}.
	 * @param ctx the parse tree
	 */
	void enterFCASE_ITEM(@NotNull Verilog2001Parser.FCASE_ITEMContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#FCASE_ITEM}.
	 * @param ctx the parse tree
	 */
	void exitFCASE_ITEM(@NotNull Verilog2001Parser.FCASE_ITEMContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#OUTPUT_DECLARATION_REG}.
	 * @param ctx the parse tree
	 */
	void enterOUTPUT_DECLARATION_REG(@NotNull Verilog2001Parser.OUTPUT_DECLARATION_REGContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#OUTPUT_DECLARATION_REG}.
	 * @param ctx the parse tree
	 */
	void exitOUTPUT_DECLARATION_REG(@NotNull Verilog2001Parser.OUTPUT_DECLARATION_REGContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#param_assignment}.
	 * @param ctx the parse tree
	 */
	void enterParam_assignment(@NotNull Verilog2001Parser.Param_assignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#param_assignment}.
	 * @param ctx the parse tree
	 */
	void exitParam_assignment(@NotNull Verilog2001Parser.Param_assignmentContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#list_of_ports}.
	 * @param ctx the parse tree
	 */
	void enterList_of_ports(@NotNull Verilog2001Parser.List_of_portsContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#list_of_ports}.
	 * @param ctx the parse tree
	 */
	void exitList_of_ports(@NotNull Verilog2001Parser.List_of_portsContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(@NotNull Verilog2001Parser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(@NotNull Verilog2001Parser.IdentifierContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#QUES}.
	 * @param ctx the parse tree
	 */
	void enterQUES(@NotNull Verilog2001Parser.QUESContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#QUES}.
	 * @param ctx the parse tree
	 */
	void exitQUES(@NotNull Verilog2001Parser.QUESContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#module_identifier}.
	 * @param ctx the parse tree
	 */
	void enterModule_identifier(@NotNull Verilog2001Parser.Module_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#module_identifier}.
	 * @param ctx the parse tree
	 */
	void exitModule_identifier(@NotNull Verilog2001Parser.Module_identifierContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#net_declaration}.
	 * @param ctx the parse tree
	 */
	void enterNet_declaration(@NotNull Verilog2001Parser.Net_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#net_declaration}.
	 * @param ctx the parse tree
	 */
	void exitNet_declaration(@NotNull Verilog2001Parser.Net_declarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#port}.
	 * @param ctx the parse tree
	 */
	void enterPort(@NotNull Verilog2001Parser.PortContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#port}.
	 * @param ctx the parse tree
	 */
	void exitPort(@NotNull Verilog2001Parser.PortContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#range}.
	 * @param ctx the parse tree
	 */
	void enterRange(@NotNull Verilog2001Parser.RangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#range}.
	 * @param ctx the parse tree
	 */
	void exitRange(@NotNull Verilog2001Parser.RangeContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#source_text}.
	 * @param ctx the parse tree
	 */
	void enterSource_text(@NotNull Verilog2001Parser.Source_textContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#source_text}.
	 * @param ctx the parse tree
	 */
	void exitSource_text(@NotNull Verilog2001Parser.Source_textContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#list_of_param_assignments}.
	 * @param ctx the parse tree
	 */
	void enterList_of_param_assignments(@NotNull Verilog2001Parser.List_of_param_assignmentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#list_of_param_assignments}.
	 * @param ctx the parse tree
	 */
	void exitList_of_param_assignments(@NotNull Verilog2001Parser.List_of_param_assignmentsContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#list_of_identifiers}.
	 * @param ctx the parse tree
	 */
	void enterList_of_identifiers(@NotNull Verilog2001Parser.List_of_identifiersContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#list_of_identifiers}.
	 * @param ctx the parse tree
	 */
	void exitList_of_identifiers(@NotNull Verilog2001Parser.List_of_identifiersContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#constant_expression}.
	 * @param ctx the parse tree
	 */
	void enterConstant_expression(@NotNull Verilog2001Parser.Constant_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#constant_expression}.
	 * @param ctx the parse tree
	 */
	void exitConstant_expression(@NotNull Verilog2001Parser.Constant_expressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#INUMBER}.
	 * @param ctx the parse tree
	 */
	void enterINUMBER(@NotNull Verilog2001Parser.INUMBERContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#INUMBER}.
	 * @param ctx the parse tree
	 */
	void exitINUMBER(@NotNull Verilog2001Parser.INUMBERContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#decimal_number}.
	 * @param ctx the parse tree
	 */
	void enterDecimal_number(@NotNull Verilog2001Parser.Decimal_numberContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#decimal_number}.
	 * @param ctx the parse tree
	 */
	void exitDecimal_number(@NotNull Verilog2001Parser.Decimal_numberContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#DEFAULT}.
	 * @param ctx the parse tree
	 */
	void enterDEFAULT(@NotNull Verilog2001Parser.DEFAULTContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#DEFAULT}.
	 * @param ctx the parse tree
	 */
	void exitDEFAULT(@NotNull Verilog2001Parser.DEFAULTContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#parameter_declaration}.
	 * @param ctx the parse tree
	 */
	void enterParameter_declaration(@NotNull Verilog2001Parser.Parameter_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#parameter_declaration}.
	 * @param ctx the parse tree
	 */
	void exitParameter_declaration(@NotNull Verilog2001Parser.Parameter_declarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#UMINUS}.
	 * @param ctx the parse tree
	 */
	void enterUMINUS(@NotNull Verilog2001Parser.UMINUSContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#UMINUS}.
	 * @param ctx the parse tree
	 */
	void exitUMINUS(@NotNull Verilog2001Parser.UMINUSContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#module_declaration}.
	 * @param ctx the parse tree
	 */
	void enterModule_declaration(@NotNull Verilog2001Parser.Module_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#module_declaration}.
	 * @param ctx the parse tree
	 */
	void exitModule_declaration(@NotNull Verilog2001Parser.Module_declarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#binary_number}.
	 * @param ctx the parse tree
	 */
	void enterBinary_number(@NotNull Verilog2001Parser.Binary_numberContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#binary_number}.
	 * @param ctx the parse tree
	 */
	void exitBinary_number(@NotNull Verilog2001Parser.Binary_numberContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#input_declaration}.
	 * @param ctx the parse tree
	 */
	void enterInput_declaration(@NotNull Verilog2001Parser.Input_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#input_declaration}.
	 * @param ctx the parse tree
	 */
	void exitInput_declaration(@NotNull Verilog2001Parser.Input_declarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#GENERAL}.
	 * @param ctx the parse tree
	 */
	void enterGENERAL(@NotNull Verilog2001Parser.GENERALContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#GENERAL}.
	 * @param ctx the parse tree
	 */
	void exitGENERAL(@NotNull Verilog2001Parser.GENERALContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#name_of_instance}.
	 * @param ctx the parse tree
	 */
	void enterName_of_instance(@NotNull Verilog2001Parser.Name_of_instanceContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#name_of_instance}.
	 * @param ctx the parse tree
	 */
	void exitName_of_instance(@NotNull Verilog2001Parser.Name_of_instanceContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#UNOT}.
	 * @param ctx the parse tree
	 */
	void enterUNOT(@NotNull Verilog2001Parser.UNOTContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#UNOT}.
	 * @param ctx the parse tree
	 */
	void exitUNOT(@NotNull Verilog2001Parser.UNOTContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#port_declaration}.
	 * @param ctx the parse tree
	 */
	void enterPort_declaration(@NotNull Verilog2001Parser.Port_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#port_declaration}.
	 * @param ctx the parse tree
	 */
	void exitPort_declaration(@NotNull Verilog2001Parser.Port_declarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#MULT_DIV_MOD}.
	 * @param ctx the parse tree
	 */
	void enterMULT_DIV_MOD(@NotNull Verilog2001Parser.MULT_DIV_MODContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#MULT_DIV_MOD}.
	 * @param ctx the parse tree
	 */
	void exitMULT_DIV_MOD(@NotNull Verilog2001Parser.MULT_DIV_MODContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#msb_constant_expression}.
	 * @param ctx the parse tree
	 */
	void enterMsb_constant_expression(@NotNull Verilog2001Parser.Msb_constant_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#msb_constant_expression}.
	 * @param ctx the parse tree
	 */
	void exitMsb_constant_expression(@NotNull Verilog2001Parser.Msb_constant_expressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#list_of_port_connections}.
	 * @param ctx the parse tree
	 */
	void enterList_of_port_connections(@NotNull Verilog2001Parser.List_of_port_connectionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#list_of_port_connections}.
	 * @param ctx the parse tree
	 */
	void exitList_of_port_connections(@NotNull Verilog2001Parser.List_of_port_connectionsContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#COMPARES}.
	 * @param ctx the parse tree
	 */
	void enterCOMPARES(@NotNull Verilog2001Parser.COMPARESContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#COMPARES}.
	 * @param ctx the parse tree
	 */
	void exitCOMPARES(@NotNull Verilog2001Parser.COMPARESContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#BLOGIC}.
	 * @param ctx the parse tree
	 */
	void enterBLOGIC(@NotNull Verilog2001Parser.BLOGICContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#BLOGIC}.
	 * @param ctx the parse tree
	 */
	void exitBLOGIC(@NotNull Verilog2001Parser.BLOGICContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#LAND}.
	 * @param ctx the parse tree
	 */
	void enterLAND(@NotNull Verilog2001Parser.LANDContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#LAND}.
	 * @param ctx the parse tree
	 */
	void exitLAND(@NotNull Verilog2001Parser.LANDContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#instance_identifier}.
	 * @param ctx the parse tree
	 */
	void enterInstance_identifier(@NotNull Verilog2001Parser.Instance_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#instance_identifier}.
	 * @param ctx the parse tree
	 */
	void exitInstance_identifier(@NotNull Verilog2001Parser.Instance_identifierContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#continuous_assign}.
	 * @param ctx the parse tree
	 */
	void enterContinuous_assign(@NotNull Verilog2001Parser.Continuous_assignContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#continuous_assign}.
	 * @param ctx the parse tree
	 */
	void exitContinuous_assign(@NotNull Verilog2001Parser.Continuous_assignContext ctx);

	/**
	 * Enter a parse tree produced by {@link Verilog2001Parser#OUTPUT_DECLARATION_NO_REG}.
	 * @param ctx the parse tree
	 */
	void enterOUTPUT_DECLARATION_NO_REG(@NotNull Verilog2001Parser.OUTPUT_DECLARATION_NO_REGContext ctx);
	/**
	 * Exit a parse tree produced by {@link Verilog2001Parser#OUTPUT_DECLARATION_NO_REG}.
	 * @param ctx the parse tree
	 */
	void exitOUTPUT_DECLARATION_NO_REG(@NotNull Verilog2001Parser.OUTPUT_DECLARATION_NO_REGContext ctx);
}