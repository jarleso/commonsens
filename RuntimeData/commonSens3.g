grammar commonSens3;

options {
  backtrack = true;
  memoize = true;
}

QComplex
	:	'[' Chain ']' (Relation '[' Chain ']')*;
	
Chain
	:	Queries (',' Timing)? | 'dev(' Queries (',' Timing)? ')';	
	
Queries
	:	(Atomic | QConcurrent) ((Relation | (LogicalOperator)+) 
		(Atomic | QConcurrent))*;

QConcurrent
	:	ConcOp '(' '[' Chain ']' ',' '[' Chain ']' ')';

ConcOp
	:	'equals' | 'starts' | 'finishes' | 'during' | 
		'overlaps';

Relation
	:	'->';
	
LogicalOperator
	:	'&&' | '||' | '!';

Atomic
	:	'(' String Operator Value ')' | 
		'(' String Operator Value ',' String ')' | 
		'(' String Operator Value ',' String ',' Timing ')';

Timing
	:	Timestamp | Timestamp ',' Timestamp | 
		Timestamp ',' ('max' | 'min') ' ' Double '%' |
		Timestamp ',' Timestamp ',' ('max' | 'min') ' ' Double '%';		

Operator
	:	'==' | '!=' | '<' | '>' | '<=' | '>=';

MaxOrMin
	:	'max' | 'min';

Timestamp
	:	Hour'.'Sixty'.'Sixty | Number'h' | Number'm' | Number's';

Value
	: 	String | Number | Double;

String
	: 	('a'..'z')+;//|'A'..'Z'|'_')+;

Hour
	:	'0'..'2''3';

Sixty
	:	'0'..'5''9';

Number
	: 	'0'..'9'+;

Double
	: 	Number'.'Number;

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