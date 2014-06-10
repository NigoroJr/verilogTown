// Generated from Verilog2001.g4 by ANTLR 4.2.2
package VerilogSimulator;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link Verilog2001Parser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface Verilog2001Visitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#LOR}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLOR(@NotNull Verilog2001Parser.LORContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#nonblocking_assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonblocking_assignment(@NotNull Verilog2001Parser.Nonblocking_assignmentContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#module_instance}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModule_instance(@NotNull Verilog2001Parser.Module_instanceContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#topmodule_identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTopmodule_identifier(@NotNull Verilog2001Parser.Topmodule_identifierContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#ADD_SUB}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitADD_SUB(@NotNull Verilog2001Parser.ADD_SUBContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#blocking_assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlocking_assignment(@NotNull Verilog2001Parser.Blocking_assignmentContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#description}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDescription(@NotNull Verilog2001Parser.DescriptionContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#conditional_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditional_statement(@NotNull Verilog2001Parser.Conditional_statementContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#hex_number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHex_number(@NotNull Verilog2001Parser.Hex_numberContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#octal_number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOctal_number(@NotNull Verilog2001Parser.Octal_numberContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#range_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRange_expression(@NotNull Verilog2001Parser.Range_expressionContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#COMBONATIONAL_ALWAYS}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCOMBONATIONAL_ALWAYS(@NotNull Verilog2001Parser.COMBONATIONAL_ALWAYSContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#BRACKETS}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBRACKETS(@NotNull Verilog2001Parser.BRACKETSContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#BIT_ACCESS}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBIT_ACCESS(@NotNull Verilog2001Parser.BIT_ACCESSContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#LNOT}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLNOT(@NotNull Verilog2001Parser.LNOTContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#case_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCase_statement(@NotNull Verilog2001Parser.Case_statementContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(@NotNull Verilog2001Parser.NumberContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#parameter_declaration_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter_declaration_(@NotNull Verilog2001Parser.Parameter_declaration_Context ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#lsb_constant_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLsb_constant_expression(@NotNull Verilog2001Parser.Lsb_constant_expressionContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(@NotNull Verilog2001Parser.StatementContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#IDENT}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIDENT(@NotNull Verilog2001Parser.IDENTContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#SEQUENTIAL_ALWAYS}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSEQUENTIAL_ALWAYS(@NotNull Verilog2001Parser.SEQUENTIAL_ALWAYSContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#module_instantiation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModule_instantiation(@NotNull Verilog2001Parser.Module_instantiationContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#inst_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInst_name(@NotNull Verilog2001Parser.Inst_nameContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#reg_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReg_declaration(@NotNull Verilog2001Parser.Reg_declarationContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#variable_lvalue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable_lvalue(@NotNull Verilog2001Parser.Variable_lvalueContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#port_reference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPort_reference(@NotNull Verilog2001Parser.Port_referenceContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#module_instance_identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModule_instance_identifier(@NotNull Verilog2001Parser.Module_instance_identifierContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#module_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModule_item(@NotNull Verilog2001Parser.Module_itemContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#FCASE_ITEM}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFCASE_ITEM(@NotNull Verilog2001Parser.FCASE_ITEMContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#OUTPUT_DECLARATION_REG}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOUTPUT_DECLARATION_REG(@NotNull Verilog2001Parser.OUTPUT_DECLARATION_REGContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#param_assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam_assignment(@NotNull Verilog2001Parser.Param_assignmentContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#list_of_ports}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList_of_ports(@NotNull Verilog2001Parser.List_of_portsContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(@NotNull Verilog2001Parser.IdentifierContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#QUES}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQUES(@NotNull Verilog2001Parser.QUESContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#module_identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModule_identifier(@NotNull Verilog2001Parser.Module_identifierContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#net_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNet_declaration(@NotNull Verilog2001Parser.Net_declarationContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#port}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPort(@NotNull Verilog2001Parser.PortContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#range}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRange(@NotNull Verilog2001Parser.RangeContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#source_text}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSource_text(@NotNull Verilog2001Parser.Source_textContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#list_of_param_assignments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList_of_param_assignments(@NotNull Verilog2001Parser.List_of_param_assignmentsContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#list_of_identifiers}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList_of_identifiers(@NotNull Verilog2001Parser.List_of_identifiersContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#constant_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstant_expression(@NotNull Verilog2001Parser.Constant_expressionContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#INUMBER}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitINUMBER(@NotNull Verilog2001Parser.INUMBERContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#decimal_number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecimal_number(@NotNull Verilog2001Parser.Decimal_numberContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#DEFAULT}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDEFAULT(@NotNull Verilog2001Parser.DEFAULTContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#parameter_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter_declaration(@NotNull Verilog2001Parser.Parameter_declarationContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#UMINUS}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUMINUS(@NotNull Verilog2001Parser.UMINUSContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#module_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModule_declaration(@NotNull Verilog2001Parser.Module_declarationContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#binary_number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinary_number(@NotNull Verilog2001Parser.Binary_numberContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#input_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInput_declaration(@NotNull Verilog2001Parser.Input_declarationContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#GENERAL}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGENERAL(@NotNull Verilog2001Parser.GENERALContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#name_of_instance}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitName_of_instance(@NotNull Verilog2001Parser.Name_of_instanceContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#UNOT}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUNOT(@NotNull Verilog2001Parser.UNOTContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#port_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPort_declaration(@NotNull Verilog2001Parser.Port_declarationContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#MULT_DIV_MOD}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMULT_DIV_MOD(@NotNull Verilog2001Parser.MULT_DIV_MODContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#msb_constant_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMsb_constant_expression(@NotNull Verilog2001Parser.Msb_constant_expressionContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#list_of_port_connections}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList_of_port_connections(@NotNull Verilog2001Parser.List_of_port_connectionsContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#COMPARES}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCOMPARES(@NotNull Verilog2001Parser.COMPARESContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#BLOGIC}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBLOGIC(@NotNull Verilog2001Parser.BLOGICContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#LAND}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLAND(@NotNull Verilog2001Parser.LANDContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#instance_identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstance_identifier(@NotNull Verilog2001Parser.Instance_identifierContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#continuous_assign}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinuous_assign(@NotNull Verilog2001Parser.Continuous_assignContext ctx);

	/**
	 * Visit a parse tree produced by {@link Verilog2001Parser#OUTPUT_DECLARATION_NO_REG}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOUTPUT_DECLARATION_NO_REG(@NotNull Verilog2001Parser.OUTPUT_DECLARATION_NO_REGContext ctx);
}