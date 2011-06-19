grammar CommonSens;

QConcurrent
	:	ConcOp '(' QComplex ',' QComplex ')'
	;

ConcOp	:	'Equals' | 'Starts' | 'Finishes' | 'During' | 'Overlaps'
	;

QComplex
	:	
	;

QConsecutive
	:	Q_atomic ARROW Q_atomic | Q_atomic ARROW Q_consecutive
	;

ARROW	:	'->' | '=>'
	;

QAtomic
	:	'(' CAPABILITY OP VALUE ')' | '(' CAPABILITY OP VALUE ',' LOI ')' | 
		'(' CAPABILITY OP VALUE ',' LOI ',' TIMESTAMP ')' |
		'(' CAPABILITY OP VALUE ',' LOI ',' TIMESTAMP ',' TIMESTAMP ')' | 
		'(' CAPABILITY OP VALUE ',' LOI ',' TIMESTAMP ',' TIMESTAMP ',' MAXORMIN DOUBLE ')'
	;

OP 
	:	'=' | '!=' | '<' | '>' | '<=' | '>=' 
	;

MAXORMIN 
	:	'max' | 'min'
	;

VALUE	:	STRING | NUMBER
	;

TIMESTAMP
	:	
	;

LOI	:	STRING
	;

CAPABILITY
	:	STRING	
	;

STRING	:	('a'..'z'|'A'..'Z')+
	;
	
NUMBER	:	DOUBLE | INT
	;	
	
DOUBLE	:	('0'..'9')+ '.' ('0'..'9')+
	;
INT	:	('0'..'9')+ 
	;