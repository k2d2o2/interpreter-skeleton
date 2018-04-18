/* intellij setting: File - Project Structure - Modules - mark target/scala-2.12/src_managed/main/antlr4 as source */
/* You should sbt compile to generate antlr4*/
/* if you want to generate antlr4 file manually in intellij, Tools - Configure ANTLR and then Generate ANTLR Recognizer */

grammar MyGrammar;

/* Lexical rules */

DEF: 'def';
ASSIGN: ':=';


IF   : 'if' ;
ELSE : 'else' ;

WHILE: 'while';
RETURN: 'return';
PRINT: 'print';

AND : '&&' ;
OR  : '||' ;

TRUE  : 'true' ;
FALSE : 'false' ;

MULT  : '*' ;
DIV   : '/' ;
PLUS  : '+' ;
MINUS : '-' ;

GT : '>' ;
GE : '>=' ;
LT : '<' ;
LE : '<=' ;
EQ : '==' ;
NE : '!=' ;

NOT : '!';

LPAREN : '(' ;
RPAREN : ')' ;

LBRACK: '{';
RBRACK: '}';

// DECIMAL, IDENTIFIER, COMMENTS, WS are set using regular expressions

DECIMAL : '-'?[0-9]+('.'[0-9]+)? ;

IDENTIFIER : [a-zA-Z_][a-zA-Z_0-9]* ;

SEMI : ';' ;

STRING : '"' .*? '"';

// COMMENT and WS are stripped from the output token stream by sending
// to a different channel 'skip'

COMMENT : '//' .+? ('\n'|EOF) -> skip ;

WS : [ \r\t\u000C\n]+ -> skip ;

/* Grammar rules */

program: function* function EOF ;

function: DEF IDENTIFIER LPAREN params RPAREN ASSIGN block;

params: param*;

param: IDENTIFIER;

block
  : LBRACK stmt* RBRACK
  | stmt
  ;

stmt
  : lv ASSIGN e SEMI #AssignStmt
  | IF LPAREN cond RPAREN block ELSE block #IfElseStmt
  | IF LPAREN cond RPAREN block #IfStmt
  | WHILE LPAREN cond RPAREN block #WhileStmt
  | RETURN e SEMI #ReturnStmt
  | PRINT e SEMI #PrintStmt
  ;

lv : IDENTIFIER;

e
  : cond #CondE
  | LPAREN e RPAREN #EInParen
  ;

args: e*;

arith
  : MINUS arith #MinusArith
  | arith PLUS arith #ArithBinPlus
  | arith MINUS arith #ArithBinMinus
  | arith MULT arith #ArithBinMult
  | arith DIV arith #ArithBinDiv
  | DECIMAL #Dec
  | IDENTIFIER LPAREN args RPAREN #ECall
  | lv #ELv
  ;

cond
  : NOT cond #NotCond
  | arith EQ arith #ArithBinEq
  | arith NE arith #ArithBinNe
  | cond EQ cond #CondBinEq
  | cond NE cond #CondBinNe
  | arith LT arith #ArithBinLt
  | arith GT arith #ArithBinGt
  | arith LE arith #ArithBinLe
  | arith GE arith #ArithBinGe
  | cond AND cond #CondBinAnd
  | cond OR cond #CondBinOr
  | TRUE #CondTrue
  | FALSE #CondFalse
  | arith #ArithE
  ;

