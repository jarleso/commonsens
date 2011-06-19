// $ANTLR 3.2 Sep 23, 2009 12:02:23 C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g 2010-06-14 13:55:46

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
public class commonSens3 extends Lexer {
    public static final int Double=17;
    public static final int LogicalOperator=11;
    public static final int Operator=14;
    public static final int QComplex=6;
    public static final int Queries=7;
    public static final int EOF=-1;
    public static final int ConcOp=12;
    public static final int Sixty=20;
    public static final int Timing=8;
    public static final int Relation=5;
    public static final int Value=15;
    public static final int Atomic=9;
    public static final int Number=21;
    public static final int QConcurrent=10;
    public static final int Chain=4;
    public static final int Timestamp=16;
    public static final int Hour=19;
    public static final int String=13;
    public static final int MaxOrMin=18;

    // delegates
    // delegators

    public commonSens3() {;} 
    public commonSens3(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public commonSens3(CharStream input, RecognizerSharedState state) {
        super(input,state);
        state.ruleMemo = new HashMap[69+1];
 
    }
    public String getGrammarFileName() { return "C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g"; }

    // $ANTLR start "QComplex"
    public final void mQComplex() throws RecognitionException {
        int QComplex_StartIndex = input.index();
        try {
            int _type = QComplex;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            if ( state.backtracking>0 && alreadyParsedRule(input, 1) ) { return ; }
            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:9:2: ( '[' Chain ']' ( Relation '[' Chain ']' )* )
            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:9:4: '[' Chain ']' ( Relation '[' Chain ']' )*
            {
            match('['); if (state.failed) return ;
            mChain(); if (state.failed) return ;
            match(']'); if (state.failed) return ;
            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:9:18: ( Relation '[' Chain ']' )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='-') ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:9:19: Relation '[' Chain ']'
            	    {
            	    mRelation(); if (state.failed) return ;
            	    match('['); if (state.failed) return ;
            	    mChain(); if (state.failed) return ;
            	    match(']'); if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 1, QComplex_StartIndex); }
        }
    }
    // $ANTLR end "QComplex"

    // $ANTLR start "Chain"
    public final void mChain() throws RecognitionException {
        int Chain_StartIndex = input.index();
        try {
            int _type = Chain;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            if ( state.backtracking>0 && alreadyParsedRule(input, 2) ) { return ; }
            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:12:2: ( Queries ( ',' Timing )? )
            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:12:4: Queries ( ',' Timing )?
            {
            mQueries(); if (state.failed) return ;
            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:12:12: ( ',' Timing )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==',') ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:12:13: ',' Timing
                    {
                    match(','); if (state.failed) return ;
                    mTiming(); if (state.failed) return ;

                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 2, Chain_StartIndex); }
        }
    }
    // $ANTLR end "Chain"

    // $ANTLR start "Queries"
    public final void mQueries() throws RecognitionException {
        int Queries_StartIndex = input.index();
        try {
            int _type = Queries;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            if ( state.backtracking>0 && alreadyParsedRule(input, 3) ) { return ; }
            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:15:2: ( ( Atomic | QConcurrent ) ( ( Relation | ( LogicalOperator )+ ) ( Atomic | QConcurrent ) )* )
            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:15:4: ( Atomic | QConcurrent ) ( ( Relation | ( LogicalOperator )+ ) ( Atomic | QConcurrent ) )*
            {
            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:15:4: ( Atomic | QConcurrent )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='(') ) {
                alt3=1;
            }
            else if ( ((LA3_0>='d' && LA3_0<='f')||LA3_0=='o'||LA3_0=='s') ) {
                alt3=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:15:5: Atomic
                    {
                    mAtomic(); if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:15:14: QConcurrent
                    {
                    mQConcurrent(); if (state.failed) return ;

                    }
                    break;

            }

            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:15:27: ( ( Relation | ( LogicalOperator )+ ) ( Atomic | QConcurrent ) )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0=='!'||LA7_0=='&'||LA7_0=='-'||LA7_0=='|') ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:15:28: ( Relation | ( LogicalOperator )+ ) ( Atomic | QConcurrent )
            	    {
            	    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:15:28: ( Relation | ( LogicalOperator )+ )
            	    int alt5=2;
            	    int LA5_0 = input.LA(1);

            	    if ( (LA5_0=='-') ) {
            	        alt5=1;
            	    }
            	    else if ( (LA5_0=='!'||LA5_0=='&'||LA5_0=='|') ) {
            	        alt5=2;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return ;}
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 5, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt5) {
            	        case 1 :
            	            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:15:29: Relation
            	            {
            	            mRelation(); if (state.failed) return ;

            	            }
            	            break;
            	        case 2 :
            	            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:15:40: ( LogicalOperator )+
            	            {
            	            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:15:40: ( LogicalOperator )+
            	            int cnt4=0;
            	            loop4:
            	            do {
            	                int alt4=2;
            	                int LA4_0 = input.LA(1);

            	                if ( (LA4_0=='!'||LA4_0=='&'||LA4_0=='|') ) {
            	                    alt4=1;
            	                }


            	                switch (alt4) {
            	            	case 1 :
            	            	    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:15:41: LogicalOperator
            	            	    {
            	            	    mLogicalOperator(); if (state.failed) return ;

            	            	    }
            	            	    break;

            	            	default :
            	            	    if ( cnt4 >= 1 ) break loop4;
            	            	    if (state.backtracking>0) {state.failed=true; return ;}
            	                        EarlyExitException eee =
            	                            new EarlyExitException(4, input);
            	                        throw eee;
            	                }
            	                cnt4++;
            	            } while (true);


            	            }
            	            break;

            	    }

            	    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:16:3: ( Atomic | QConcurrent )
            	    int alt6=2;
            	    int LA6_0 = input.LA(1);

            	    if ( (LA6_0=='(') ) {
            	        alt6=1;
            	    }
            	    else if ( ((LA6_0>='d' && LA6_0<='f')||LA6_0=='o'||LA6_0=='s') ) {
            	        alt6=2;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return ;}
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 6, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt6) {
            	        case 1 :
            	            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:16:4: Atomic
            	            {
            	            mAtomic(); if (state.failed) return ;

            	            }
            	            break;
            	        case 2 :
            	            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:16:13: QConcurrent
            	            {
            	            mQConcurrent(); if (state.failed) return ;

            	            }
            	            break;

            	    }


            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 3, Queries_StartIndex); }
        }
    }
    // $ANTLR end "Queries"

    // $ANTLR start "QConcurrent"
    public final void mQConcurrent() throws RecognitionException {
        int QConcurrent_StartIndex = input.index();
        try {
            int _type = QConcurrent;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            if ( state.backtracking>0 && alreadyParsedRule(input, 4) ) { return ; }
            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:19:2: ( ConcOp '(' '[' Chain ']' ',' '[' Chain ']' ')' )
            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:19:4: ConcOp '(' '[' Chain ']' ',' '[' Chain ']' ')'
            {
            mConcOp(); if (state.failed) return ;
            match('('); if (state.failed) return ;
            match('['); if (state.failed) return ;
            mChain(); if (state.failed) return ;
            match(']'); if (state.failed) return ;
            match(','); if (state.failed) return ;
            match('['); if (state.failed) return ;
            mChain(); if (state.failed) return ;
            match(']'); if (state.failed) return ;
            match(')'); if (state.failed) return ;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 4, QConcurrent_StartIndex); }
        }
    }
    // $ANTLR end "QConcurrent"

    // $ANTLR start "ConcOp"
    public final void mConcOp() throws RecognitionException {
        int ConcOp_StartIndex = input.index();
        try {
            int _type = ConcOp;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            if ( state.backtracking>0 && alreadyParsedRule(input, 5) ) { return ; }
            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:22:2: ( 'equals' | 'starts' | 'finishes' | 'during' | 'overlaps' )
            int alt8=5;
            switch ( input.LA(1) ) {
            case 'e':
                {
                alt8=1;
                }
                break;
            case 's':
                {
                alt8=2;
                }
                break;
            case 'f':
                {
                alt8=3;
                }
                break;
            case 'd':
                {
                alt8=4;
                }
                break;
            case 'o':
                {
                alt8=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;
            }

            switch (alt8) {
                case 1 :
                    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:22:4: 'equals'
                    {
                    match("equals"); if (state.failed) return ;


                    }
                    break;
                case 2 :
                    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:22:15: 'starts'
                    {
                    match("starts"); if (state.failed) return ;


                    }
                    break;
                case 3 :
                    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:22:26: 'finishes'
                    {
                    match("finishes"); if (state.failed) return ;


                    }
                    break;
                case 4 :
                    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:22:39: 'during'
                    {
                    match("during"); if (state.failed) return ;


                    }
                    break;
                case 5 :
                    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:23:3: 'overlaps'
                    {
                    match("overlaps"); if (state.failed) return ;


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 5, ConcOp_StartIndex); }
        }
    }
    // $ANTLR end "ConcOp"

    // $ANTLR start "Relation"
    public final void mRelation() throws RecognitionException {
        int Relation_StartIndex = input.index();
        try {
            int _type = Relation;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            if ( state.backtracking>0 && alreadyParsedRule(input, 6) ) { return ; }
            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:26:2: ( '->' )
            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:26:4: '->'
            {
            match("->"); if (state.failed) return ;


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 6, Relation_StartIndex); }
        }
    }
    // $ANTLR end "Relation"

    // $ANTLR start "LogicalOperator"
    public final void mLogicalOperator() throws RecognitionException {
        int LogicalOperator_StartIndex = input.index();
        try {
            int _type = LogicalOperator;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            if ( state.backtracking>0 && alreadyParsedRule(input, 7) ) { return ; }
            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:29:2: ( '&&' | '||' | '!' )
            int alt9=3;
            switch ( input.LA(1) ) {
            case '&':
                {
                alt9=1;
                }
                break;
            case '|':
                {
                alt9=2;
                }
                break;
            case '!':
                {
                alt9=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;
            }

            switch (alt9) {
                case 1 :
                    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:29:4: '&&'
                    {
                    match("&&"); if (state.failed) return ;


                    }
                    break;
                case 2 :
                    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:29:11: '||'
                    {
                    match("||"); if (state.failed) return ;


                    }
                    break;
                case 3 :
                    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:29:18: '!'
                    {
                    match('!'); if (state.failed) return ;

                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 7, LogicalOperator_StartIndex); }
        }
    }
    // $ANTLR end "LogicalOperator"

    // $ANTLR start "Atomic"
    public final void mAtomic() throws RecognitionException {
        int Atomic_StartIndex = input.index();
        try {
            int _type = Atomic;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            if ( state.backtracking>0 && alreadyParsedRule(input, 8) ) { return ; }
            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:32:2: ( '(' String Operator String ')' | '(' String Operator Value ',' String ')' | '(' String Operator Value ',' String ',' Timing ')' )
            int alt10=3;
            alt10 = dfa10.predict(input);
            switch (alt10) {
                case 1 :
                    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:32:4: '(' String Operator String ')'
                    {
                    match('('); if (state.failed) return ;
                    mString(); if (state.failed) return ;
                    mOperator(); if (state.failed) return ;
                    mString(); if (state.failed) return ;
                    match(')'); if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:33:3: '(' String Operator Value ',' String ')'
                    {
                    match('('); if (state.failed) return ;
                    mString(); if (state.failed) return ;
                    mOperator(); if (state.failed) return ;
                    mValue(); if (state.failed) return ;
                    match(','); if (state.failed) return ;
                    mString(); if (state.failed) return ;
                    match(')'); if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:34:3: '(' String Operator Value ',' String ',' Timing ')'
                    {
                    match('('); if (state.failed) return ;
                    mString(); if (state.failed) return ;
                    mOperator(); if (state.failed) return ;
                    mValue(); if (state.failed) return ;
                    match(','); if (state.failed) return ;
                    mString(); if (state.failed) return ;
                    match(','); if (state.failed) return ;
                    mTiming(); if (state.failed) return ;
                    match(')'); if (state.failed) return ;

                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 8, Atomic_StartIndex); }
        }
    }
    // $ANTLR end "Atomic"

    // $ANTLR start "Timing"
    public final void mTiming() throws RecognitionException {
        int Timing_StartIndex = input.index();
        try {
            int _type = Timing;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            if ( state.backtracking>0 && alreadyParsedRule(input, 9) ) { return ; }
            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:37:2: ( Timestamp | Timestamp ',' Timestamp | Timestamp ',' ( 'max' | 'min' ) ' ' Double | Timestamp ',' Timestamp ',' ( 'max' | 'min' ) ' ' Double )
            int alt13=4;
            alt13 = dfa13.predict(input);
            switch (alt13) {
                case 1 :
                    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:37:4: Timestamp
                    {
                    mTimestamp(); if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:37:16: Timestamp ',' Timestamp
                    {
                    mTimestamp(); if (state.failed) return ;
                    match(','); if (state.failed) return ;
                    mTimestamp(); if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:38:3: Timestamp ',' ( 'max' | 'min' ) ' ' Double
                    {
                    mTimestamp(); if (state.failed) return ;
                    match(','); if (state.failed) return ;
                    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:38:17: ( 'max' | 'min' )
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( (LA11_0=='m') ) {
                        int LA11_1 = input.LA(2);

                        if ( (LA11_1=='a') ) {
                            alt11=1;
                        }
                        else if ( (LA11_1=='i') ) {
                            alt11=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return ;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 11, 1, input);

                            throw nvae;
                        }
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 11, 0, input);

                        throw nvae;
                    }
                    switch (alt11) {
                        case 1 :
                            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:38:18: 'max'
                            {
                            match("max"); if (state.failed) return ;


                            }
                            break;
                        case 2 :
                            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:38:26: 'min'
                            {
                            match("min"); if (state.failed) return ;


                            }
                            break;

                    }

                    match(' '); if (state.failed) return ;
                    mDouble(); if (state.failed) return ;

                    }
                    break;
                case 4 :
                    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:39:3: Timestamp ',' Timestamp ',' ( 'max' | 'min' ) ' ' Double
                    {
                    mTimestamp(); if (state.failed) return ;
                    match(','); if (state.failed) return ;
                    mTimestamp(); if (state.failed) return ;
                    match(','); if (state.failed) return ;
                    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:39:31: ( 'max' | 'min' )
                    int alt12=2;
                    int LA12_0 = input.LA(1);

                    if ( (LA12_0=='m') ) {
                        int LA12_1 = input.LA(2);

                        if ( (LA12_1=='a') ) {
                            alt12=1;
                        }
                        else if ( (LA12_1=='i') ) {
                            alt12=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return ;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 12, 1, input);

                            throw nvae;
                        }
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 12, 0, input);

                        throw nvae;
                    }
                    switch (alt12) {
                        case 1 :
                            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:39:32: 'max'
                            {
                            match("max"); if (state.failed) return ;


                            }
                            break;
                        case 2 :
                            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:39:40: 'min'
                            {
                            match("min"); if (state.failed) return ;


                            }
                            break;

                    }

                    match(' '); if (state.failed) return ;
                    mDouble(); if (state.failed) return ;

                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 9, Timing_StartIndex); }
        }
    }
    // $ANTLR end "Timing"

    // $ANTLR start "Operator"
    public final void mOperator() throws RecognitionException {
        int Operator_StartIndex = input.index();
        try {
            int _type = Operator;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            if ( state.backtracking>0 && alreadyParsedRule(input, 10) ) { return ; }
            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:42:2: ( '=' | '!=' | '<' | '>' | '<=' | '>=' )
            int alt14=6;
            switch ( input.LA(1) ) {
            case '=':
                {
                alt14=1;
                }
                break;
            case '!':
                {
                alt14=2;
                }
                break;
            case '<':
                {
                int LA14_3 = input.LA(2);

                if ( (LA14_3=='=') ) {
                    alt14=5;
                }
                else {
                    alt14=3;}
                }
                break;
            case '>':
                {
                int LA14_4 = input.LA(2);

                if ( (LA14_4=='=') ) {
                    alt14=6;
                }
                else {
                    alt14=4;}
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 14, 0, input);

                throw nvae;
            }

            switch (alt14) {
                case 1 :
                    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:42:4: '='
                    {
                    match('='); if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:42:10: '!='
                    {
                    match("!="); if (state.failed) return ;


                    }
                    break;
                case 3 :
                    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:42:17: '<'
                    {
                    match('<'); if (state.failed) return ;

                    }
                    break;
                case 4 :
                    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:42:23: '>'
                    {
                    match('>'); if (state.failed) return ;

                    }
                    break;
                case 5 :
                    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:42:29: '<='
                    {
                    match("<="); if (state.failed) return ;


                    }
                    break;
                case 6 :
                    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:42:36: '>='
                    {
                    match(">="); if (state.failed) return ;


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 10, Operator_StartIndex); }
        }
    }
    // $ANTLR end "Operator"

    // $ANTLR start "MaxOrMin"
    public final void mMaxOrMin() throws RecognitionException {
        int MaxOrMin_StartIndex = input.index();
        try {
            int _type = MaxOrMin;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            if ( state.backtracking>0 && alreadyParsedRule(input, 11) ) { return ; }
            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:45:2: ( 'max' | 'min' )
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0=='m') ) {
                int LA15_1 = input.LA(2);

                if ( (LA15_1=='a') ) {
                    alt15=1;
                }
                else if ( (LA15_1=='i') ) {
                    alt15=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 15, 0, input);

                throw nvae;
            }
            switch (alt15) {
                case 1 :
                    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:45:4: 'max'
                    {
                    match("max"); if (state.failed) return ;


                    }
                    break;
                case 2 :
                    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:45:12: 'min'
                    {
                    match("min"); if (state.failed) return ;


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 11, MaxOrMin_StartIndex); }
        }
    }
    // $ANTLR end "MaxOrMin"

    // $ANTLR start "Timestamp"
    public final void mTimestamp() throws RecognitionException {
        int Timestamp_StartIndex = input.index();
        try {
            int _type = Timestamp;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            if ( state.backtracking>0 && alreadyParsedRule(input, 12) ) { return ; }
            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:48:2: ( Hour '.' Sixty '.' Sixty | Number 'h' | Number 'm' | Number 's' )
            int alt16=4;
            alt16 = dfa16.predict(input);
            switch (alt16) {
                case 1 :
                    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:48:4: Hour '.' Sixty '.' Sixty
                    {
                    mHour(); if (state.failed) return ;
                    match('.'); if (state.failed) return ;
                    mSixty(); if (state.failed) return ;
                    match('.'); if (state.failed) return ;
                    mSixty(); if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:48:27: Number 'h'
                    {
                    mNumber(); if (state.failed) return ;
                    match('h'); if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:48:39: Number 'm'
                    {
                    mNumber(); if (state.failed) return ;
                    match('m'); if (state.failed) return ;

                    }
                    break;
                case 4 :
                    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:48:51: Number 's'
                    {
                    mNumber(); if (state.failed) return ;
                    match('s'); if (state.failed) return ;

                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 12, Timestamp_StartIndex); }
        }
    }
    // $ANTLR end "Timestamp"

    // $ANTLR start "Value"
    public final void mValue() throws RecognitionException {
        int Value_StartIndex = input.index();
        try {
            int _type = Value;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            if ( state.backtracking>0 && alreadyParsedRule(input, 13) ) { return ; }
            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:51:2: ( String | Number | Double )
            int alt17=3;
            alt17 = dfa17.predict(input);
            switch (alt17) {
                case 1 :
                    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:51:5: String
                    {
                    mString(); if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:51:14: Number
                    {
                    mNumber(); if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:51:23: Double
                    {
                    mDouble(); if (state.failed) return ;

                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 13, Value_StartIndex); }
        }
    }
    // $ANTLR end "Value"

    // $ANTLR start "String"
    public final void mString() throws RecognitionException {
        int String_StartIndex = input.index();
        try {
            int _type = String;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            if ( state.backtracking>0 && alreadyParsedRule(input, 14) ) { return ; }
            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:54:2: ( ( 'a' .. 'z' )+ )
            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:54:5: ( 'a' .. 'z' )+
            {
            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:54:5: ( 'a' .. 'z' )+
            int cnt18=0;
            loop18:
            do {
                int alt18=2;
                int LA18_0 = input.LA(1);

                if ( ((LA18_0>='a' && LA18_0<='z')) ) {
                    alt18=1;
                }


                switch (alt18) {
            	case 1 :
            	    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:54:6: 'a' .. 'z'
            	    {
            	    matchRange('a','z'); if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    if ( cnt18 >= 1 ) break loop18;
            	    if (state.backtracking>0) {state.failed=true; return ;}
                        EarlyExitException eee =
                            new EarlyExitException(18, input);
                        throw eee;
                }
                cnt18++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 14, String_StartIndex); }
        }
    }
    // $ANTLR end "String"

    // $ANTLR start "Hour"
    public final void mHour() throws RecognitionException {
        int Hour_StartIndex = input.index();
        try {
            int _type = Hour;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            if ( state.backtracking>0 && alreadyParsedRule(input, 15) ) { return ; }
            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:57:2: ( '0' .. '2' '3' )
            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:57:4: '0' .. '2' '3'
            {
            matchRange('0','2'); if (state.failed) return ;
            match('3'); if (state.failed) return ;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 15, Hour_StartIndex); }
        }
    }
    // $ANTLR end "Hour"

    // $ANTLR start "Sixty"
    public final void mSixty() throws RecognitionException {
        int Sixty_StartIndex = input.index();
        try {
            int _type = Sixty;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            if ( state.backtracking>0 && alreadyParsedRule(input, 16) ) { return ; }
            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:60:2: ( '0' .. '5' '9' )
            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:60:4: '0' .. '5' '9'
            {
            matchRange('0','5'); if (state.failed) return ;
            match('9'); if (state.failed) return ;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 16, Sixty_StartIndex); }
        }
    }
    // $ANTLR end "Sixty"

    // $ANTLR start "Number"
    public final void mNumber() throws RecognitionException {
        int Number_StartIndex = input.index();
        try {
            int _type = Number;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            if ( state.backtracking>0 && alreadyParsedRule(input, 17) ) { return ; }
            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:63:2: ( ( '0' .. '9' )+ )
            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:63:5: ( '0' .. '9' )+
            {
            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:63:5: ( '0' .. '9' )+
            int cnt19=0;
            loop19:
            do {
                int alt19=2;
                int LA19_0 = input.LA(1);

                if ( ((LA19_0>='0' && LA19_0<='9')) ) {
                    alt19=1;
                }


                switch (alt19) {
            	case 1 :
            	    // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:0:0: '0' .. '9'
            	    {
            	    matchRange('0','9'); if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    if ( cnt19 >= 1 ) break loop19;
            	    if (state.backtracking>0) {state.failed=true; return ;}
                        EarlyExitException eee =
                            new EarlyExitException(19, input);
                        throw eee;
                }
                cnt19++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 17, Number_StartIndex); }
        }
    }
    // $ANTLR end "Number"

    // $ANTLR start "Double"
    public final void mDouble() throws RecognitionException {
        int Double_StartIndex = input.index();
        try {
            int _type = Double;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            if ( state.backtracking>0 && alreadyParsedRule(input, 18) ) { return ; }
            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:66:2: ( Number '.' Number )
            // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:66:5: Number '.' Number
            {
            mNumber(); if (state.failed) return ;
            match('.'); if (state.failed) return ;
            mNumber(); if (state.failed) return ;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 18, Double_StartIndex); }
        }
    }
    // $ANTLR end "Double"

    public void mTokens() throws RecognitionException {
        // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:8: ( QComplex | Chain | Queries | QConcurrent | ConcOp | Relation | LogicalOperator | Atomic | Timing | Operator | MaxOrMin | Timestamp | Value | String | Hour | Sixty | Number | Double )
        int alt20=18;
        alt20 = dfa20.predict(input);
        switch (alt20) {
            case 1 :
                // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:10: QComplex
                {
                mQComplex(); if (state.failed) return ;

                }
                break;
            case 2 :
                // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:19: Chain
                {
                mChain(); if (state.failed) return ;

                }
                break;
            case 3 :
                // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:25: Queries
                {
                mQueries(); if (state.failed) return ;

                }
                break;
            case 4 :
                // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:33: QConcurrent
                {
                mQConcurrent(); if (state.failed) return ;

                }
                break;
            case 5 :
                // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:45: ConcOp
                {
                mConcOp(); if (state.failed) return ;

                }
                break;
            case 6 :
                // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:52: Relation
                {
                mRelation(); if (state.failed) return ;

                }
                break;
            case 7 :
                // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:61: LogicalOperator
                {
                mLogicalOperator(); if (state.failed) return ;

                }
                break;
            case 8 :
                // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:77: Atomic
                {
                mAtomic(); if (state.failed) return ;

                }
                break;
            case 9 :
                // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:84: Timing
                {
                mTiming(); if (state.failed) return ;

                }
                break;
            case 10 :
                // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:91: Operator
                {
                mOperator(); if (state.failed) return ;

                }
                break;
            case 11 :
                // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:100: MaxOrMin
                {
                mMaxOrMin(); if (state.failed) return ;

                }
                break;
            case 12 :
                // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:109: Timestamp
                {
                mTimestamp(); if (state.failed) return ;

                }
                break;
            case 13 :
                // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:119: Value
                {
                mValue(); if (state.failed) return ;

                }
                break;
            case 14 :
                // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:125: String
                {
                mString(); if (state.failed) return ;

                }
                break;
            case 15 :
                // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:132: Hour
                {
                mHour(); if (state.failed) return ;

                }
                break;
            case 16 :
                // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:137: Sixty
                {
                mSixty(); if (state.failed) return ;

                }
                break;
            case 17 :
                // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:143: Number
                {
                mNumber(); if (state.failed) return ;

                }
                break;
            case 18 :
                // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:150: Double
                {
                mDouble(); if (state.failed) return ;

                }
                break;

        }

    }

    // $ANTLR start synpred35_commonSens3
    public final void synpred35_commonSens3_fragment() throws RecognitionException {   
        // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:19: ( Chain )
        // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:19: Chain
        {
        mChain(); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred35_commonSens3

    // $ANTLR start synpred36_commonSens3
    public final void synpred36_commonSens3_fragment() throws RecognitionException {   
        // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:25: ( Queries )
        // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:25: Queries
        {
        mQueries(); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred36_commonSens3

    // $ANTLR start synpred37_commonSens3
    public final void synpred37_commonSens3_fragment() throws RecognitionException {   
        // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:33: ( QConcurrent )
        // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:33: QConcurrent
        {
        mQConcurrent(); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred37_commonSens3

    // $ANTLR start synpred38_commonSens3
    public final void synpred38_commonSens3_fragment() throws RecognitionException {   
        // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:45: ( ConcOp )
        // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:45: ConcOp
        {
        mConcOp(); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred38_commonSens3

    // $ANTLR start synpred40_commonSens3
    public final void synpred40_commonSens3_fragment() throws RecognitionException {   
        // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:61: ( LogicalOperator )
        // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:61: LogicalOperator
        {
        mLogicalOperator(); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred40_commonSens3

    // $ANTLR start synpred41_commonSens3
    public final void synpred41_commonSens3_fragment() throws RecognitionException {   
        // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:77: ( Atomic )
        // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:77: Atomic
        {
        mAtomic(); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred41_commonSens3

    // $ANTLR start synpred42_commonSens3
    public final void synpred42_commonSens3_fragment() throws RecognitionException {   
        // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:84: ( Timing )
        // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:84: Timing
        {
        mTiming(); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred42_commonSens3

    // $ANTLR start synpred43_commonSens3
    public final void synpred43_commonSens3_fragment() throws RecognitionException {   
        // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:91: ( Operator )
        // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:91: Operator
        {
        mOperator(); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred43_commonSens3

    // $ANTLR start synpred44_commonSens3
    public final void synpred44_commonSens3_fragment() throws RecognitionException {   
        // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:100: ( MaxOrMin )
        // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:100: MaxOrMin
        {
        mMaxOrMin(); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred44_commonSens3

    // $ANTLR start synpred45_commonSens3
    public final void synpred45_commonSens3_fragment() throws RecognitionException {   
        // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:109: ( Timestamp )
        // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:109: Timestamp
        {
        mTimestamp(); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred45_commonSens3

    // $ANTLR start synpred46_commonSens3
    public final void synpred46_commonSens3_fragment() throws RecognitionException {   
        // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:119: ( Value )
        // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:119: Value
        {
        mValue(); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred46_commonSens3

    // $ANTLR start synpred47_commonSens3
    public final void synpred47_commonSens3_fragment() throws RecognitionException {   
        // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:125: ( String )
        // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:125: String
        {
        mString(); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred47_commonSens3

    // $ANTLR start synpred48_commonSens3
    public final void synpred48_commonSens3_fragment() throws RecognitionException {   
        // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:132: ( Hour )
        // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:132: Hour
        {
        mHour(); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred48_commonSens3

    // $ANTLR start synpred49_commonSens3
    public final void synpred49_commonSens3_fragment() throws RecognitionException {   
        // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:137: ( Sixty )
        // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:137: Sixty
        {
        mSixty(); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred49_commonSens3

    // $ANTLR start synpred50_commonSens3
    public final void synpred50_commonSens3_fragment() throws RecognitionException {   
        // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:143: ( Number )
        // C:\\Users\\jarleso\\workspace\\CommonSens\\RuntimeData\\commonSens3.g:1:143: Number
        {
        mNumber(); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred50_commonSens3

    public final boolean synpred44_commonSens3() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred44_commonSens3_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred47_commonSens3() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred47_commonSens3_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred45_commonSens3() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred45_commonSens3_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred37_commonSens3() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred37_commonSens3_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred46_commonSens3() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred46_commonSens3_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred50_commonSens3() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred50_commonSens3_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred35_commonSens3() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred35_commonSens3_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred38_commonSens3() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred38_commonSens3_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred41_commonSens3() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred41_commonSens3_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred49_commonSens3() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred49_commonSens3_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred42_commonSens3() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred42_commonSens3_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred36_commonSens3() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred36_commonSens3_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred40_commonSens3() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred40_commonSens3_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred43_commonSens3() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred43_commonSens3_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred48_commonSens3() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred48_commonSens3_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


    protected DFA10 dfa10 = new DFA10(this);
    protected DFA13 dfa13 = new DFA13(this);
    protected DFA16 dfa16 = new DFA16(this);
    protected DFA17 dfa17 = new DFA17(this);
    protected DFA20 dfa20 = new DFA20(this);
    static final String DFA10_eotS =
        "\23\uffff";
    static final String DFA10_eofS =
        "\23\uffff";
    static final String DFA10_minS =
        "\1\50\1\141\1\41\1\60\1\75\2\60\1\51\1\54\3\60\1\141\1\uffff\1"+
        "\60\1\51\1\54\2\uffff";
    static final String DFA10_maxS =
        "\1\50\3\172\1\75\3\172\1\71\4\172\1\uffff\1\71\1\172\1\71\2\uffff";
    static final String DFA10_acceptS =
        "\15\uffff\1\1\3\uffff\1\2\1\3";
    static final String DFA10_specialS =
        "\23\uffff}>";
    static final String[] DFA10_transitionS = {
            "\1\1",
            "\32\2",
            "\1\4\32\uffff\1\5\1\3\1\6\42\uffff\32\2",
            "\12\10\47\uffff\32\7",
            "\1\11",
            "\12\10\3\uffff\1\12\43\uffff\32\7",
            "\12\10\3\uffff\1\13\43\uffff\32\7",
            "\1\15\2\uffff\1\14\64\uffff\32\7",
            "\1\14\1\uffff\1\16\1\uffff\12\10",
            "\12\10\47\uffff\32\7",
            "\12\10\47\uffff\32\7",
            "\12\10\47\uffff\32\7",
            "\32\17",
            "",
            "\12\20",
            "\1\21\2\uffff\1\22\64\uffff\32\17",
            "\1\14\3\uffff\12\20",
            "",
            ""
    };

    static final short[] DFA10_eot = DFA.unpackEncodedString(DFA10_eotS);
    static final short[] DFA10_eof = DFA.unpackEncodedString(DFA10_eofS);
    static final char[] DFA10_min = DFA.unpackEncodedStringToUnsignedChars(DFA10_minS);
    static final char[] DFA10_max = DFA.unpackEncodedStringToUnsignedChars(DFA10_maxS);
    static final short[] DFA10_accept = DFA.unpackEncodedString(DFA10_acceptS);
    static final short[] DFA10_special = DFA.unpackEncodedString(DFA10_specialS);
    static final short[][] DFA10_transition;

    static {
        int numStates = DFA10_transitionS.length;
        DFA10_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA10_transition[i] = DFA.unpackEncodedString(DFA10_transitionS[i]);
        }
    }

    class DFA10 extends DFA {

        public DFA10(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 10;
            this.eot = DFA10_eot;
            this.eof = DFA10_eof;
            this.min = DFA10_min;
            this.max = DFA10_max;
            this.accept = DFA10_accept;
            this.special = DFA10_special;
            this.transition = DFA10_transition;
        }
        public String getDescription() {
            return "31:1: Atomic : ( '(' String Operator String ')' | '(' String Operator Value ',' String ')' | '(' String Operator Value ',' String ',' Timing ')' );";
        }
    }
    static final String DFA13_eotS =
        "\4\uffff\3\10\11\uffff\3\25\6\uffff\1\10\3\uffff\1\25";
    static final String DFA13_eofS =
        "\36\uffff";
    static final String DFA13_minS =
        "\3\60\1\56\3\54\1\60\1\uffff\1\60\1\71\1\uffff\2\60\2\56\3\54\2"+
        "\60\2\uffff\2\71\1\54\1\56\1\60\1\71\1\54";
    static final String DFA13_maxS =
        "\1\71\3\163\3\54\1\65\1\uffff\1\155\1\71\1\uffff\2\163\1\56\1\163"+
        "\3\54\2\65\2\uffff\2\71\1\54\1\56\1\65\1\71\1\54";
    static final String DFA13_acceptS =
        "\10\uffff\1\1\2\uffff\1\3\11\uffff\1\2\1\4\7\uffff";
    static final String DFA13_specialS =
        "\36\uffff}>";
    static final String[] DFA13_transitionS = {
            "\3\1\7\2",
            "\3\2\1\3\6\2\56\uffff\1\5\4\uffff\1\4\5\uffff\1\6",
            "\12\2\56\uffff\1\5\4\uffff\1\4\5\uffff\1\6",
            "\1\7\1\uffff\12\2\56\uffff\1\5\4\uffff\1\4\5\uffff\1\6",
            "\1\11",
            "\1\11",
            "\1\11",
            "\6\12",
            "",
            "\3\14\7\15\63\uffff\1\13",
            "\1\16",
            "",
            "\3\15\1\17\6\15\56\uffff\1\21\4\uffff\1\20\5\uffff\1\22",
            "\12\15\56\uffff\1\21\4\uffff\1\20\5\uffff\1\22",
            "\1\23",
            "\1\24\1\uffff\12\15\56\uffff\1\21\4\uffff\1\20\5\uffff\1\22",
            "\1\26",
            "\1\26",
            "\1\26",
            "\6\27",
            "\6\30",
            "",
            "",
            "\1\31",
            "\1\32",
            "\1\11",
            "\1\33",
            "\6\34",
            "\1\35",
            "\1\26"
    };

    static final short[] DFA13_eot = DFA.unpackEncodedString(DFA13_eotS);
    static final short[] DFA13_eof = DFA.unpackEncodedString(DFA13_eofS);
    static final char[] DFA13_min = DFA.unpackEncodedStringToUnsignedChars(DFA13_minS);
    static final char[] DFA13_max = DFA.unpackEncodedStringToUnsignedChars(DFA13_maxS);
    static final short[] DFA13_accept = DFA.unpackEncodedString(DFA13_acceptS);
    static final short[] DFA13_special = DFA.unpackEncodedString(DFA13_specialS);
    static final short[][] DFA13_transition;

    static {
        int numStates = DFA13_transitionS.length;
        DFA13_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA13_transition[i] = DFA.unpackEncodedString(DFA13_transitionS[i]);
        }
    }

    class DFA13 extends DFA {

        public DFA13(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 13;
            this.eot = DFA13_eot;
            this.eof = DFA13_eof;
            this.min = DFA13_min;
            this.max = DFA13_max;
            this.accept = DFA13_accept;
            this.special = DFA13_special;
            this.transition = DFA13_transition;
        }
        public String getDescription() {
            return "36:1: Timing : ( Timestamp | Timestamp ',' Timestamp | Timestamp ',' ( 'max' | 'min' ) ' ' Double | Timestamp ',' Timestamp ',' ( 'max' | 'min' ) ' ' Double );";
        }
    }
    static final String DFA16_eotS =
        "\10\uffff";
    static final String DFA16_eofS =
        "\10\uffff";
    static final String DFA16_minS =
        "\3\60\1\56\4\uffff";
    static final String DFA16_maxS =
        "\1\71\3\163\4\uffff";
    static final String DFA16_acceptS =
        "\4\uffff\1\3\1\4\1\2\1\1";
    static final String DFA16_specialS =
        "\10\uffff}>";
    static final String[] DFA16_transitionS = {
            "\3\1\7\2",
            "\3\2\1\3\6\2\56\uffff\1\6\4\uffff\1\4\5\uffff\1\5",
            "\12\2\56\uffff\1\6\4\uffff\1\4\5\uffff\1\5",
            "\1\7\1\uffff\12\2\56\uffff\1\6\4\uffff\1\4\5\uffff\1\5",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA16_eot = DFA.unpackEncodedString(DFA16_eotS);
    static final short[] DFA16_eof = DFA.unpackEncodedString(DFA16_eofS);
    static final char[] DFA16_min = DFA.unpackEncodedStringToUnsignedChars(DFA16_minS);
    static final char[] DFA16_max = DFA.unpackEncodedStringToUnsignedChars(DFA16_maxS);
    static final short[] DFA16_accept = DFA.unpackEncodedString(DFA16_acceptS);
    static final short[] DFA16_special = DFA.unpackEncodedString(DFA16_specialS);
    static final short[][] DFA16_transition;

    static {
        int numStates = DFA16_transitionS.length;
        DFA16_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA16_transition[i] = DFA.unpackEncodedString(DFA16_transitionS[i]);
        }
    }

    class DFA16 extends DFA {

        public DFA16(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 16;
            this.eot = DFA16_eot;
            this.eof = DFA16_eof;
            this.min = DFA16_min;
            this.max = DFA16_max;
            this.accept = DFA16_accept;
            this.special = DFA16_special;
            this.transition = DFA16_transition;
        }
        public String getDescription() {
            return "47:1: Timestamp : ( Hour '.' Sixty '.' Sixty | Number 'h' | Number 'm' | Number 's' );";
        }
    }
    static final String DFA17_eotS =
        "\2\uffff\1\3\2\uffff";
    static final String DFA17_eofS =
        "\5\uffff";
    static final String DFA17_minS =
        "\1\60\1\uffff\1\56\2\uffff";
    static final String DFA17_maxS =
        "\1\172\1\uffff\1\71\2\uffff";
    static final String DFA17_acceptS =
        "\1\uffff\1\1\1\uffff\1\2\1\3";
    static final String DFA17_specialS =
        "\5\uffff}>";
    static final String[] DFA17_transitionS = {
            "\12\2\47\uffff\32\1",
            "",
            "\1\4\1\uffff\12\2",
            "",
            ""
    };

    static final short[] DFA17_eot = DFA.unpackEncodedString(DFA17_eotS);
    static final short[] DFA17_eof = DFA.unpackEncodedString(DFA17_eofS);
    static final char[] DFA17_min = DFA.unpackEncodedStringToUnsignedChars(DFA17_minS);
    static final char[] DFA17_max = DFA.unpackEncodedStringToUnsignedChars(DFA17_maxS);
    static final short[] DFA17_accept = DFA.unpackEncodedString(DFA17_acceptS);
    static final short[] DFA17_special = DFA.unpackEncodedString(DFA17_specialS);
    static final short[][] DFA17_transition;

    static {
        int numStates = DFA17_transitionS.length;
        DFA17_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA17_transition[i] = DFA.unpackEncodedString(DFA17_transitionS[i]);
        }
    }

    class DFA17 extends DFA {

        public DFA17(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 17;
            this.eot = DFA17_eot;
            this.eof = DFA17_eof;
            this.min = DFA17_min;
            this.max = DFA17_max;
            this.accept = DFA17_accept;
            this.special = DFA17_special;
            this.transition = DFA17_transition;
        }
        public String getDescription() {
            return "50:1: Value : ( String | Number | Double );";
        }
    }
    static final String DFA20_eotS =
        "\42\uffff";
    static final String DFA20_eofS =
        "\42\uffff";
    static final String DFA20_minS =
        "\1\41\1\uffff\6\0\3\uffff\3\0\3\uffff\3\0\16\uffff";
    static final String DFA20_maxS =
        "\1\174\1\uffff\6\0\3\uffff\3\0\3\uffff\3\0\16\uffff";
    static final String DFA20_acceptS =
        "\1\uffff\1\1\6\uffff\1\6\1\7\4\uffff\1\12\5\uffff\1\2\1\3\1\10"+
        "\1\4\1\5\1\15\1\16\1\11\1\14\1\17\1\20\1\21\1\22\1\13";
    static final String DFA20_specialS =
        "\2\uffff\1\0\1\1\1\2\1\3\1\4\1\5\3\uffff\1\6\1\7\1\10\3\uffff\1"+
        "\11\1\12\1\13\16\uffff}>";
    static final String[] DFA20_transitionS = {
            "\1\13\4\uffff\1\11\1\uffff\1\2\4\uffff\1\10\2\uffff\3\14\3"+
            "\15\4\23\2\uffff\3\16\34\uffff\1\1\5\uffff\3\22\1\6\1\3\1\5"+
            "\6\22\1\21\1\22\1\7\3\22\1\4\7\22\1\uffff\1\11",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA20_eot = DFA.unpackEncodedString(DFA20_eotS);
    static final short[] DFA20_eof = DFA.unpackEncodedString(DFA20_eofS);
    static final char[] DFA20_min = DFA.unpackEncodedStringToUnsignedChars(DFA20_minS);
    static final char[] DFA20_max = DFA.unpackEncodedStringToUnsignedChars(DFA20_maxS);
    static final short[] DFA20_accept = DFA.unpackEncodedString(DFA20_acceptS);
    static final short[] DFA20_special = DFA.unpackEncodedString(DFA20_specialS);
    static final short[][] DFA20_transition;

    static {
        int numStates = DFA20_transitionS.length;
        DFA20_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA20_transition[i] = DFA.unpackEncodedString(DFA20_transitionS[i]);
        }
    }

    class DFA20 extends DFA {

        public DFA20(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 20;
            this.eot = DFA20_eot;
            this.eof = DFA20_eof;
            this.min = DFA20_min;
            this.max = DFA20_max;
            this.accept = DFA20_accept;
            this.special = DFA20_special;
            this.transition = DFA20_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( QComplex | Chain | Queries | QConcurrent | ConcOp | Relation | LogicalOperator | Atomic | Timing | Operator | MaxOrMin | Timestamp | Value | String | Hour | Sixty | Number | Double );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA20_2 = input.LA(1);

                         
                        int index20_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred35_commonSens3()) ) {s = 20;}

                        else if ( (synpred36_commonSens3()) ) {s = 21;}

                        else if ( (synpred41_commonSens3()) ) {s = 22;}

                         
                        input.seek(index20_2);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA20_3 = input.LA(1);

                         
                        int index20_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred35_commonSens3()) ) {s = 20;}

                        else if ( (synpred36_commonSens3()) ) {s = 21;}

                        else if ( (synpred37_commonSens3()) ) {s = 23;}

                        else if ( (synpred38_commonSens3()) ) {s = 24;}

                        else if ( (synpred46_commonSens3()) ) {s = 25;}

                        else if ( (synpred47_commonSens3()) ) {s = 26;}

                         
                        input.seek(index20_3);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA20_4 = input.LA(1);

                         
                        int index20_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred35_commonSens3()) ) {s = 20;}

                        else if ( (synpred36_commonSens3()) ) {s = 21;}

                        else if ( (synpred37_commonSens3()) ) {s = 23;}

                        else if ( (synpred38_commonSens3()) ) {s = 24;}

                        else if ( (synpred46_commonSens3()) ) {s = 25;}

                        else if ( (synpred47_commonSens3()) ) {s = 26;}

                         
                        input.seek(index20_4);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA20_5 = input.LA(1);

                         
                        int index20_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred35_commonSens3()) ) {s = 20;}

                        else if ( (synpred36_commonSens3()) ) {s = 21;}

                        else if ( (synpred37_commonSens3()) ) {s = 23;}

                        else if ( (synpred38_commonSens3()) ) {s = 24;}

                        else if ( (synpred46_commonSens3()) ) {s = 25;}

                        else if ( (synpred47_commonSens3()) ) {s = 26;}

                         
                        input.seek(index20_5);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA20_6 = input.LA(1);

                         
                        int index20_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred35_commonSens3()) ) {s = 20;}

                        else if ( (synpred36_commonSens3()) ) {s = 21;}

                        else if ( (synpred37_commonSens3()) ) {s = 23;}

                        else if ( (synpred38_commonSens3()) ) {s = 24;}

                        else if ( (synpred46_commonSens3()) ) {s = 25;}

                        else if ( (synpred47_commonSens3()) ) {s = 26;}

                         
                        input.seek(index20_6);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA20_7 = input.LA(1);

                         
                        int index20_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred35_commonSens3()) ) {s = 20;}

                        else if ( (synpred36_commonSens3()) ) {s = 21;}

                        else if ( (synpred37_commonSens3()) ) {s = 23;}

                        else if ( (synpred38_commonSens3()) ) {s = 24;}

                        else if ( (synpred46_commonSens3()) ) {s = 25;}

                        else if ( (synpred47_commonSens3()) ) {s = 26;}

                         
                        input.seek(index20_7);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA20_11 = input.LA(1);

                         
                        int index20_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred40_commonSens3()) ) {s = 9;}

                        else if ( (synpred43_commonSens3()) ) {s = 14;}

                         
                        input.seek(index20_11);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA20_12 = input.LA(1);

                         
                        int index20_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred42_commonSens3()) ) {s = 27;}

                        else if ( (synpred45_commonSens3()) ) {s = 28;}

                        else if ( (synpred46_commonSens3()) ) {s = 25;}

                        else if ( (synpred48_commonSens3()) ) {s = 29;}

                        else if ( (synpred49_commonSens3()) ) {s = 30;}

                        else if ( (synpred50_commonSens3()) ) {s = 31;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index20_12);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA20_13 = input.LA(1);

                         
                        int index20_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred42_commonSens3()) ) {s = 27;}

                        else if ( (synpred45_commonSens3()) ) {s = 28;}

                        else if ( (synpred46_commonSens3()) ) {s = 25;}

                        else if ( (synpred49_commonSens3()) ) {s = 30;}

                        else if ( (synpred50_commonSens3()) ) {s = 31;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index20_13);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA20_17 = input.LA(1);

                         
                        int index20_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred44_commonSens3()) ) {s = 33;}

                        else if ( (synpred46_commonSens3()) ) {s = 25;}

                        else if ( (synpred47_commonSens3()) ) {s = 26;}

                         
                        input.seek(index20_17);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA20_18 = input.LA(1);

                         
                        int index20_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred46_commonSens3()) ) {s = 25;}

                        else if ( (synpred47_commonSens3()) ) {s = 26;}

                         
                        input.seek(index20_18);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA20_19 = input.LA(1);

                         
                        int index20_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred42_commonSens3()) ) {s = 27;}

                        else if ( (synpred45_commonSens3()) ) {s = 28;}

                        else if ( (synpred46_commonSens3()) ) {s = 25;}

                        else if ( (synpred50_commonSens3()) ) {s = 31;}

                        else if ( (true) ) {s = 32;}

                         
                        input.seek(index20_19);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 20, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

}