/* Dervied from Verilog 2001 but simplified by Peter Jamieson 2014 */
grammar Verilog2001;

inst_name : topmodule_identifier ( '.' instance_identifier )* ;
source_text : description* EOF ;

description : module_declaration ;
module_declaration : 'module' module_identifier ( list_of_ports )? ';' module_item* 'endmodule' ;

list_of_ports : '(' port ( ',' port )* ')' ;
port: port_reference? ;
port_reference :
identifier
| identifier '[' constant_expression ']'
| identifier '[' range_expression ']'
;

module_item :
port_declaration ';'
| net_declaration
| reg_declaration
| parameter_declaration
| continuous_assign
| module_instantiation
| always_construct
;

/* ---- DECLARATIONS ---- */
/* input, output, parameter, wire, reg */

port_declaration :
input_declaration
| output_declaration
;

parameter_declaration : parameter_declaration_ ';' ;
parameter_declaration_ :
'parameter' ( range )? list_of_param_assignments
;
list_of_param_assignments : param_assignment ( ',' param_assignment )* ;
param_assignment : identifier '=' constant_expression ;

input_declaration : 'input' ( range )? list_of_identifiers ;

output_declaration :
'output' ( range )? list_of_identifiers	# OUTPUT_DECLARATION_NO_REG
| 'output' 'reg' ( range )? list_of_identifiers # OUTPUT_DECLARATION_REG
;

reg_declaration : 'reg' ( range )? list_of_identifiers ';' ;

net_declaration : 'wire' ( range )? list_of_identifiers ';' ;

/* ---- MODULE_INSTANTIATION ---- */

module_instantiation :   module_identifier module_instance ( ',' module_instance )* ';' ;
module_instance : name_of_instance '(' list_of_port_connections ')' ;
name_of_instance : module_instance_identifier ( range )? ;
list_of_port_connections : identifier ( ',' identifier )* ;

/* ---- ASSIGN ---- */

continuous_assign : 'assign' variable_lvalue '=' expression ';' ;

/* ---- ALWAYS BLOCKS ---- */

always_construct : 
'always' '@(*)' statement								# COMBONATIONAL_ALWAYS 
| 'always' '@' '(' 'posedge' identifier 'or' 'negedge' identifier ')' statement 	# SEQUENTIAL_ALWAYS 
;

/* ---- STATEMENTS ---- */

statement :
'begin' ( statement )* 'end'
| blocking_assignment ';'
| case_statement
| conditional_statement
| nonblocking_assignment ';'
| ';'
;

blocking_assignment : variable_lvalue '=' expression ;
nonblocking_assignment : variable_lvalue '<=' expression ;

variable_lvalue :
identifier
| identifier '[' range_expression ']'
;

conditional_statement : 'if' '(' expression ')' statement ( 'else' 'if' '(' expression ')' statement )* ( 'else' statement )?  ;

case_statement : 'case' '(' expression ')' case_item ( case_item )* 'endcase' ;

case_item : 
expression ':' statement 					# FCASE_ITEM
| 'default' ':' statement 					# DEFAULT
;

/* ---- EXPRESSIONS ---- */

constant_expression : number ;

range_expression :
decimal_number
| msb_constant_expression ':' lsb_constant_expression
;

expression
: '-' expression 								# UMINUS
| '~' expression								# UNOT
| expression op=('*' | '/' | '%') expression					# MULT_DIV_MOD
| expression op=('+' | '-') expression						# ADD_SUB
| expression op=('&' | '|' | '^' | '~^'| '~&' | '~|' | '<<' | '>>') expression	# BLOGIC
| expression op=('<' | '>' | '<=' | '>=' | '!=' | '==') expression		# COMPARES
| expression '!' expression							# LNOT
| expression '&&' expression							# LAND
| expression '||' expression							# LOR
| expression '?' expression ':' expression					# QUES
| '(' expression ')' 								# BRACKETS
| identifier_types								# IDENT
| number									# INUMBER
;

range : '[' msb_constant_expression ':' lsb_constant_expression ']' ;

msb_constant_expression : decimal_number ;
lsb_constant_expression : decimal_number ;

/* ---- IDENTIFIERS ---- */

instance_identifier : identifier ;
module_identifier : identifier ;
module_instance_identifier : identifier ;
topmodule_identifier : identifier ;

identifier_types : 
identifier '[' constant_expression ']'			# BIT_ACCESS
| identifier									# GENERAL
//'{' expression ( ',' expression )* '}'		# CONCAT
;

list_of_identifiers : identifier ( ',' identifier )* ;

identifier :
Simple_identifier
| Escaped_identifier
;

/* ---- NUMBERS ---- */

number
    :   decimal_number
    |   octal_number 
    |   binary_number
    |   hex_number
    ;

decimal_number
    : Unsigned_number
    | ( Unsigned_number )? Decimal_base Unsigned_number
    ;
binary_number : ( Unsigned_number )? Binary_base Unsigned_number ;
octal_number : ( Unsigned_number )? Octal_base Unsigned_number ;
hex_number : (Unsigned_number )? Hex_base Unsigned_number;

// LEXER RULES
White_space : [ \t\n\r]+ -> channel(HIDDEN) ;

Unsigned_number : Digit ( Digit )* ;

Decimal_base : '\'' [dD] ;
Binary_base : '\'' [bB] ;
Octal_base : '\''  [oO] ;
Hex_base : '\'' [hH] ;

fragment
Digit : [0-9a-fA-F] ;

One_line_comment : '//' .*? '\r'? '\n' -> channel(HIDDEN);
Block_comment : '/*' .*? '*/' -> channel(HIDDEN);

Simple_identifier : [a-zA-Z_] [a-zA-Z0-9_$]* ;
Escaped_identifier
	:	'\\' ~[ \r\t\n]*
        {_input.LA(1)!=' '&&_input.LA(1)!='\t'&&_input.LA(1)!='\t'&&_input.LA(1)!='\n'}?
    ;

SUB : '-' ;
TILDE : '~' ;
MULT : '*' ;
DIV : '/' ;
MOD : '%' ;
ADD : '+' ;
BITWISE_AND : '&' ;
BITWISE_NAND : '~&' ;
BITWISE_OR : '|' ;
BITWISE_NOR : '~|' ;
BITWISE_XOR : '^' ;
BITWISE_XNOR : '~^' ;
SHIFT_LEFT : '<<' ;
SHIFT_RIGHT : '>>' ;
GT : '>' ;
GTE : '>=' ;
LT : '<' ;
LTE : '<=' ;
NOTEQUAL : '!=' ;
EQUAL : '==' ;
LOGICAL_AND : '&&' ;
LOGICAL_OR : '||' ;
LOGICAL_NOT : '!' ;

