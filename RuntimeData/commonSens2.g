grammar commonSens2;

sjekk om det hjelper å dytte ned [ til Queries osv. hvis ikke, så drit i det.

QComplex options { k=2; backtrack = true; } 
	:	'[' Queries ']' /* (Relation '[' Chain ']')**/;

/*	
Chain
	:	Queries;*/
	
Queries options { k=20; backtrack = true; } 
	:	'(' Atomic ')' /*((Relation | LogicalOperator) Queries)**/ | 
		QConcurrent;

QConcurrent options { k=2; backtrack = true; } 
	:	'Equals' '<' QComplex '>';// ',' QComplex ')';

/*ConcOp options { k=2; backtrack = true; } 	:	'equals' /*| 'starts' | 'finishes' | 'during' | 
		'overlaps';*/

Relation options { k=2; backtrack = true; }  
	:	'->' | '=>';

/*	
LogicalOperator
	:	'&&' | '||' | '!';*/

Atomic
	:	String Operator String/* | 
		String Operator Value ',' String | 
		String Operator Value ',' String ',' Timing*/;

/*Timing
	:	Timestamp | Timestamp ',' Timestamp | 
		Timestamp ',' Timestamp ',' MaxOrMin ' ' Double;		*/

Operator 
	:	'&' /*| '!=' | '<' | '>' | '<=' | '>='*/;

/*MaxOrMin 
	:	'max' | 'min';

Timestamp
	:	Hour'.'Sixty'.'Sixty | Number'h' | Number'm' | Number's';*/

Value	: 	String/* | Number | Double*/;

String 	
	: 	('a'..'z')+;//|'A'..'Z'|'_')+;
/*
Hour	
	:	'0'..'2''3';

Sixty	
	:	'0'..'5''9';

Number	: 	'0'..'9'+;

Double	
	: 	Number'.'Number;*/

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