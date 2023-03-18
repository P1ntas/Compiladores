grammar Javamm;

@header {
    package pt.up.fe.comp2023;
}

SLC : '//' ~[\n]* -> skip;
MLC : '/*' .*? '*/' -> skip;
INTEGER : [0] | [1-9][0-9]* ;
ID : [a-zA-Z$_][a-zA-Z0-9_]*;
WS : [ \n\t\r\f]+ -> skip ;


program
    : (importDeclaration)* (classDeclaration) EOF
    ;

importDeclaration: 'import' name+=ID ( '.' name+=ID )* ';';

classDeclaration: 'class' className=ID ('extends' superClassName=ID)? '{' (varDeclaration)* (methodDeclaration)* '}'? ';'? ;

varDeclaration: type varName=ID ';';

methodDeclaration
    : ('public')? 'static' 'void' 'main' '(' 'String' '[' ']' parameterName=ID ')' '{' ( varDeclaration )* ( statement )* '}' #MainDeclaration
    | ('public')? retType methodName=ID '(' ( parameter ( ',' parameter )* )? ')' '{' ( varDeclaration )* ( statement )* 'return' retExpr ';' '}'#MetDeclaration
    ;

parameter: type parameterName=ID;

retType: type;

retExpr: expression;

type
    : ty='int[]'
    | ty='int'
    | ty='boolean'
    | ty='String'
    | ty=ID
    ;

statement
    : expression ';' #ExpressionStatement
    | ID '=' expression ';' #Assignment
    | ID '[' expression ']' '=' expression ';' #BracketsAssignment
    | 'if' '(' expression ')' statement 'else' statement #IfElseStatement
    | 'while' '(' expression ')' statement #While
    | '{' ( statement )* '}' #CurlyBracesStatement
    ;

expression
    : '(' expression ')' #Parenthesis
    | expression '[' expression ']' #ArrayAccessChain
    | '!' expression #BinaryOp
    | expression op=('*' | '/') expression #BinaryOp
    | expression op=('+' | '-') expression #BinaryOp
    | expression op=('<' | '>') expression #BinaryOp
    | expression op=('&&' | '||') expression #BinaryOp
    | expression '.' 'length' #Length
    | 'new' type '[' expression ']' #NewArray
    | 'new' ID '(' (expression (',' expression)*)? ')' #NewObject
    | classDeclaration #ClassExpression
    | expression '.' ID  #MemberAccess
    | expression '.' ID '(' (expression (',' expression)*)? ')' #MethodCall
    | value=INTEGER #Integer
    | value=ID #Identifier
    ;
