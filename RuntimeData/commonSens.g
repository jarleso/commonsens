grammar commonSens;

QComplex
	:	QAtomic (OP QAtomic)* | QConsecutive;

QConcurrent
	:	ConcOp '(' QComplex ',' QComplex ')';

ConcOp	:	'equals' | 'starts' | 'finishes' | 'during' | 'overlaps';

QConsecutive
	:	QComplex ARROW QComplex (ARROW QComplex)*;

ARROW 
	:	'->' | '=>';

QAtomic
	:	'(' STRING OP VALUE ')' | '(' STRING OP VALUE ',' STRING ')' | 
		'(' STRING OP VALUE ',' STRING ',' TIMESTAMP ')' |
		'(' STRING OP VALUE ',' STRING ',' TIMESTAMP ',' TIMESTAMP ')' | 
		'(' STRING OP VALUE ',' STRING ',' TIMESTAMP ',' TIMESTAMP ',' MAXORMIN DOUBLE ')';

OP 
	:	'=' | '!=' | '<' | '>' | '<=' | '>=';

MAXORMIN 
	:	'max' | 'min';

TIMESTAMP
	:	HOUR'.'SIXTY'.'SIXTY | INT'h' | INT'm' | INT's';

VALUE	: 	STRING | INT | DOUBLE;

STRING 	
	: 	('a'..'z'|'A'..'Z'|'_')+;

fragment HOUR	
	:	'0'..'2''3';

fragment SIXTY	
	:	'0'..'5''9';

INT	: 	'0'..'9'+;

fragment DOUBLE	
	: 	INT'.'INT;

/*	
WS      :
		(' '
	        | '\\t'
                | '\\r' '\\n' { newline(); }
                | '\\n'      { newline(); }
                )
	        { $setType(Token.SKIP); }
        ;
        */