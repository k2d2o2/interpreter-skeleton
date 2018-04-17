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
  : lv ASSIGN e SEMI
  | IF LPAREN cond RPAREN block ELSE block
  | IF LPAREN cond RPAREN block
  | WHILE LPAREN cond RPAREN block
  | RETURN e
  ;

lv : IDENTIFIER;

e
  : arith
  | cond
  | LPAREN e RPAREN
  | lv
  ;

arith
  : MINUS arith
  | arith PLUS arith
  | arith MINUS arith
  | arith MULT arith
  | arith DIV arith
  | DECIMAL
  ;

cond
  : NOT cond
  | arith EQ arith
  | arith NE arith
  | cond EQ cond
  | cond NE cond
  | arith LT arith
  | arith GT arith
  | arith LE arith
  | arith GE arith
  | cond AND cond
  | cond OR cond
  | TRUE
  | FALSE
  ;

