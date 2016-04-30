import java_cup.runtime.Symbol;
import java.lang.System;
import java.io.*;


class Lexer implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private boolean yy_at_bol;
	private int yy_lexical_state;

	Lexer (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	Lexer (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private Lexer () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;
	}

	private boolean yy_eof_done = false;
	private final int YYINITIAL = 0;
	private final int yy_state_dtrans[] = {
		0
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_START,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NOT_ACCEPT,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NOT_ACCEPT,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NOT_ACCEPT,
		/* 35 */ YY_NOT_ACCEPT,
		/* 36 */ YY_NOT_ACCEPT,
		/* 37 */ YY_NOT_ACCEPT,
		/* 38 */ YY_NOT_ACCEPT,
		/* 39 */ YY_NOT_ACCEPT,
		/* 40 */ YY_NOT_ACCEPT,
		/* 41 */ YY_NOT_ACCEPT,
		/* 42 */ YY_NOT_ACCEPT,
		/* 43 */ YY_NOT_ACCEPT,
		/* 44 */ YY_NOT_ACCEPT,
		/* 45 */ YY_NOT_ACCEPT,
		/* 46 */ YY_NOT_ACCEPT,
		/* 47 */ YY_NOT_ACCEPT,
		/* 48 */ YY_NOT_ACCEPT,
		/* 49 */ YY_NOT_ACCEPT,
		/* 50 */ YY_NOT_ACCEPT,
		/* 51 */ YY_NOT_ACCEPT,
		/* 52 */ YY_NOT_ACCEPT,
		/* 53 */ YY_NOT_ACCEPT,
		/* 54 */ YY_NOT_ACCEPT,
		/* 55 */ YY_NOT_ACCEPT,
		/* 56 */ YY_NOT_ACCEPT,
		/* 57 */ YY_NOT_ACCEPT,
		/* 58 */ YY_NOT_ACCEPT,
		/* 59 */ YY_NOT_ACCEPT,
		/* 60 */ YY_NOT_ACCEPT,
		/* 61 */ YY_NOT_ACCEPT,
		/* 62 */ YY_NOT_ACCEPT,
		/* 63 */ YY_NOT_ACCEPT,
		/* 64 */ YY_NOT_ACCEPT,
		/* 65 */ YY_NOT_ACCEPT,
		/* 66 */ YY_NOT_ACCEPT,
		/* 67 */ YY_NOT_ACCEPT,
		/* 68 */ YY_NOT_ACCEPT,
		/* 69 */ YY_NOT_ACCEPT,
		/* 70 */ YY_NOT_ACCEPT,
		/* 71 */ YY_NOT_ACCEPT,
		/* 72 */ YY_NOT_ACCEPT,
		/* 73 */ YY_NOT_ACCEPT,
		/* 74 */ YY_NOT_ACCEPT,
		/* 75 */ YY_NOT_ACCEPT,
		/* 76 */ YY_NOT_ACCEPT,
		/* 77 */ YY_NOT_ACCEPT,
		/* 78 */ YY_NOT_ACCEPT,
		/* 79 */ YY_NOT_ACCEPT,
		/* 80 */ YY_NOT_ACCEPT,
		/* 81 */ YY_NOT_ACCEPT,
		/* 82 */ YY_NOT_ACCEPT,
		/* 83 */ YY_NOT_ACCEPT,
		/* 84 */ YY_NOT_ACCEPT,
		/* 85 */ YY_NOT_ACCEPT,
		/* 86 */ YY_NOT_ACCEPT,
		/* 87 */ YY_NOT_ACCEPT,
		/* 88 */ YY_NOT_ACCEPT,
		/* 89 */ YY_NOT_ACCEPT,
		/* 90 */ YY_NOT_ACCEPT,
		/* 91 */ YY_NOT_ACCEPT,
		/* 92 */ YY_NOT_ACCEPT,
		/* 93 */ YY_NOT_ACCEPT,
		/* 94 */ YY_NOT_ACCEPT,
		/* 95 */ YY_NOT_ACCEPT,
		/* 96 */ YY_NOT_ACCEPT,
		/* 97 */ YY_NOT_ACCEPT,
		/* 98 */ YY_NOT_ACCEPT,
		/* 99 */ YY_NOT_ACCEPT,
		/* 100 */ YY_NOT_ACCEPT,
		/* 101 */ YY_NOT_ACCEPT,
		/* 102 */ YY_NOT_ACCEPT,
		/* 103 */ YY_NOT_ACCEPT,
		/* 104 */ YY_NOT_ACCEPT,
		/* 105 */ YY_NOT_ACCEPT,
		/* 106 */ YY_NOT_ACCEPT,
		/* 107 */ YY_NOT_ACCEPT,
		/* 108 */ YY_NOT_ACCEPT,
		/* 109 */ YY_NOT_ACCEPT,
		/* 110 */ YY_NOT_ACCEPT,
		/* 111 */ YY_NOT_ACCEPT,
		/* 112 */ YY_NOT_ACCEPT,
		/* 113 */ YY_NOT_ACCEPT,
		/* 114 */ YY_NOT_ACCEPT,
		/* 115 */ YY_NOT_ACCEPT,
		/* 116 */ YY_NOT_ACCEPT,
		/* 117 */ YY_NOT_ACCEPT,
		/* 118 */ YY_NOT_ACCEPT,
		/* 119 */ YY_NOT_ACCEPT,
		/* 120 */ YY_NOT_ACCEPT,
		/* 121 */ YY_NOT_ACCEPT,
		/* 122 */ YY_NOT_ACCEPT,
		/* 123 */ YY_NOT_ACCEPT,
		/* 124 */ YY_NOT_ACCEPT,
		/* 125 */ YY_NOT_ACCEPT,
		/* 126 */ YY_NOT_ACCEPT,
		/* 127 */ YY_NOT_ACCEPT,
		/* 128 */ YY_NOT_ACCEPT,
		/* 129 */ YY_NOT_ACCEPT,
		/* 130 */ YY_NOT_ACCEPT,
		/* 131 */ YY_NOT_ACCEPT,
		/* 132 */ YY_NOT_ACCEPT,
		/* 133 */ YY_NOT_ACCEPT,
		/* 134 */ YY_NOT_ACCEPT,
		/* 135 */ YY_NOT_ACCEPT,
		/* 136 */ YY_NOT_ACCEPT,
		/* 137 */ YY_NOT_ACCEPT,
		/* 138 */ YY_NOT_ACCEPT,
		/* 139 */ YY_NOT_ACCEPT,
		/* 140 */ YY_NOT_ACCEPT,
		/* 141 */ YY_NOT_ACCEPT,
		/* 142 */ YY_NOT_ACCEPT,
		/* 143 */ YY_NOT_ACCEPT,
		/* 144 */ YY_NOT_ACCEPT,
		/* 145 */ YY_NOT_ACCEPT,
		/* 146 */ YY_NOT_ACCEPT,
		/* 147 */ YY_NOT_ACCEPT,
		/* 148 */ YY_NOT_ACCEPT,
		/* 149 */ YY_NOT_ACCEPT,
		/* 150 */ YY_NOT_ACCEPT,
		/* 151 */ YY_NOT_ACCEPT,
		/* 152 */ YY_NOT_ACCEPT,
		/* 153 */ YY_NOT_ACCEPT,
		/* 154 */ YY_NOT_ACCEPT,
		/* 155 */ YY_NOT_ACCEPT,
		/* 156 */ YY_NOT_ACCEPT,
		/* 157 */ YY_NOT_ACCEPT,
		/* 158 */ YY_NOT_ACCEPT,
		/* 159 */ YY_NOT_ACCEPT,
		/* 160 */ YY_NOT_ACCEPT,
		/* 161 */ YY_NOT_ACCEPT,
		/* 162 */ YY_NOT_ACCEPT,
		/* 163 */ YY_NOT_ACCEPT,
		/* 164 */ YY_NOT_ACCEPT,
		/* 165 */ YY_NOT_ACCEPT,
		/* 166 */ YY_NOT_ACCEPT,
		/* 167 */ YY_NOT_ACCEPT
	};
	private int yy_cmap[] = unpackFromString(1,130,
"18:10,28,18:2,34,18:22,6,26,18:5,32,18:3,32,29:10,18:3,32,18:29,24,1,25,32," +
"31,18,8,11,12,7,5,16,20,30,3,30,22,4,21,14,13,23,33,36,9,2,10,30:2,15,30,27" +
",17,18,19,18:2,35,0")[0];

	private int yy_rmap[] = unpackFromString(1,168,
"0,1,2,3,1,4,1,5,1:2,6,7,1:5,8,1:9,9,10,11,6,12,13,1,14,15,16,17,18,19,7,20," +
"21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45," +
"46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,8,63,64,65,66,67,68,69,7" +
"0,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,9" +
"5,96,97,98,99,100,101,102,103,104,105,106,107,9,10,108,109,110,111,112,113," +
"114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132" +
",133,134,135,136,137,138,139,140,141,142,143")[0];

	private int yy_nxt[][] = unpackFromString(144,37,
"1,2,3:4,4,3:10,31,33:2,3:4,33:2,5,3,6,7,3,33,8,3,-1,30,3,-1:38,9,29,32,34,3" +
"5,-1,36,-1,37,38,39,-1:9,133,-1:17,3:4,-1,3:10,-1:3,3:4,-1:3,3,-1,3:3,-1,3," +
"-1:2,3,-1,5:27,-1,5:5,-1:2,5,-1:29,7,-1:9,10:4,-1,10:10,-1,10:8,-1,10,-1:2," +
"10:5,-1,10,-1,40:18,11,40:8,-1,40:5,-1:2,40,-1,84:18,17,84:8,-1,84:5,-1:2,8" +
"4,-1,130:18,27,130:8,-1,130:5,-1:2,130,-1,131:18,28,131:8,-1,131:5,-1:2,131" +
",-1:3,132,-1,41,-1:32,40:27,-1,40:5,-1:2,40,-1:2,134,-1:42,42,-1:42,43,-1:2" +
"1,44,-1:8,45,-1:4,46,-1:28,137,-1:4,135,-1:35,157,-1:32,47,-1:46,136,-1:32," +
"51,-1:32,52,-1:65,53,-1:2,54,-1:46,55,-1:44,58,-1:38,138,-1:18,59,-1:53,12," +
"-1:20,61,-1:48,62,-1:32,63,-1:28,13,-1:41,64,-1:28,139,-1:57,65,-1:16,66,-1" +
":38,14,-1:42,68,-1:29,69,-1:35,140,-1,70,-1,71,-1:65,72,-1:21,73,-1:23,143," +
"-1:42,141,-1:24,74,-1:50,15,-1:37,75,-1:52,76,-1:16,160,-1:35,77,-1:29,78,-" +
"1:34,83,-1:34,84:27,-1,84:5,-1:2,84,-1:10,86,-1:39,165,-1:37,145,-1:36,16,-" +
"1:26,88,-1:54,146,-1:17,161,-1,89,-1,90,-1:31,91,-1:55,92,-1:23,93,-1:38,94" +
",-1:31,18,-1:64,98,-1:16,148,-1:27,99,-1:35,100,-1:35,101,-1:55,149,-1:27,1" +
"03,-1:44,162,-1:21,104,-1:41,105,-1:31,19,-1:58,107,-1:12,108,-1:47,109,-1:" +
"26,151,-1:53,111,-1:23,112,-1:38,113,-1:31,114,-1:44,115,-1:43,20,-1:40,117" +
",-1:15,118,-1:35,119,-1:55,120,-1:34,21,-1:31,121,-1:31,153,-1:28,167:27,-1" +
",167:5,-1:2,167,-1:27,155,-1:12,123,-1:38,124,-1:50,22,-1:36,23,-1:30,156,-" +
"1:37,127,-1:39,154,-1:38,24,-1:19,129,-1:53,25,-1:36,26,-1:19,49,-1:42,48,-" +
"1:33,50,-1:42,164,-1:27,60,-1:46,56,-1:29,67,-1:34,142,-1:35,144,-1:51,82,-" +
"1:32,79,-1:35,81,-1:29,85,-1:33,95,-1:42,96,-1:31,102,-1:45,106,-1:29,150,-" +
"1:45,152,-1:30,116,-1:30,122,-1:43,125,-1:28,130:27,-1,130:5,-1:2,130,-1:5," +
"126,-1:45,128,-1:27,57,-1:34,159,-1:35,80,-1:46,87,-1:26,97,-1:39,110,-1:32" +
",131:27,-1,131:5,-1:2,131,-1:2,158,-1:55,147,-1:16,167:16,163,167:7,166,167" +
":2,-1,167:5,-1:2,167,-1,167:24,166,167:2,-1,167:5,-1:2,167");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

	return new Symbol(sym.EOF, null);
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{
  //return new Symbol(sym.ERROR, "Invalid input: " + yytext());
}
					case -3:
						break;
					case 3:
						{
  if (insideItem && mathmode % 2 == 0 && equmode % 2 == 0) {
    itemText += yytext() + " ";
  } else if (mathmode%2 == 1 || equmode % 2 == 1) {
    System.out.println("1st var");
    return (new Symbol(sym.VAR,yytext()));
  } else {
    return new Symbol(sym.BODY, yytext());
  }
}
					case -4:
						break;
					case 4:
						{ return (new Symbol(sym.DOLLAR, yytext()));}
					case -5:
						break;
					case 5:
						{}
					case -6:
						break;
					case 6:
						{ if (insideItem) {
                    Symbol s = new Symbol(sym.BODY, itemText);
                    itemText = "";
                    insideItem = false;
                     return s;
                  }
              }
					case -7:
						break;
					case 7:
						{
  if (insideItem && mathmode % 2 == 0 && equmode % 2 == 0) {
    itemText += yytext() + " ";
  } else {
    return (new Symbol(sym.NM,yytext()));
  }
}
					case -8:
						break;
					case 8:
						{return (new Symbol(sym.OPERATOR,yytext()));}
					case -9:
						break;
					case 9:
						{return new Symbol(sym.NEWLINE, yytext());}
					case -10:
						break;
					case 10:
						{
  if (mathmode % 2 == 0 && equmode % 2 == 0) {
    return new Symbol(sym.BODY,yytext());
  } else if (mathmode%2 == 1) {
  	if (equmode%2 == 1) {
  		return (new Symbol(sym.FUNC,yytext()));
  	}
  	else {
	  	System.out.println("2nd var");
	    return new Symbol(sym.VAR, yytext());
	   }
  }
}
					case -11:
						break;
					case 11:
						{ return new Symbol(sym.TEXT, yytext());}
					case -12:
						break;
					case 12:
						{
  insideItem = true;
  nItems++;
  return (new Symbol(sym.ITEM, yytext()));
}
					case -13:
						break;
					case 13:
						{ return new Symbol(sym.DATE, yytext());}
					case -14:
						break;
					case 14:
						{ return (new Symbol(sym.TITLE, yytext()));}
					case -15:
						break;
					case 15:
						{ return new Symbol(sym.BF, yytext());}
					case -16:
						break;
					case 16:
						{ return new Symbol(sym.SECTION, yytext());}
					case -17:
						break;
					case 17:
						{ return (new Symbol(sym.LABEL,yytext()));}
					case -18:
						break;
					case 18:
						{ return new Symbol(sym.SUB_TITLE, yytext());}
					case -19:
						break;
					case 19:
						{ return new Symbol(sym.MAKE, yytext());}
					case -20:
						break;
					case 20:
						{
  return new Symbol(sym.ERROR, "Line: " + yyline + " Undefined control sequence: " + yytext());
}
					case -21:
						break;
					case 21:
						{ if (nItems > 0) {
													return (new Symbol(sym.END, yytext()));
                          } else {
														return new Symbol(sym.ERROR, "Line: " + yyline + " Item list with missing \\item");
														}
												}
					case -22:
						break;
					case 22:
						{ mathmode++; equmode++; return (new Symbol(sym.END,yytext()));}
					case -23:
						break;
					case 23:
						{ return new Symbol(sym.END, yytext());}
					case -24:
						break;
					case 24:
						{
  itemText = "";
  nItems = 0;
  return (new Symbol(sym.BEGIN ,yytext()));
}
					case -25:
						break;
					case 25:
						{mathmode++; equmode++; {return (new Symbol(sym.BEGIN,yytext()));}}
					case -26:
						break;
					case 26:
						{ return new Symbol(sym.BEGIN, yytext());}
					case -27:
						break;
					case 27:
						{ return new Symbol(sym.DOC_CLASS, yytext());}
					case -28:
						break;
					case 28:
						{ return new Symbol(sym.PACKAGE, yytext());}
					case -29:
						break;
					case 30:
						
					case -30:
						break;
					case 31:
						{
  //return new Symbol(sym.ERROR, "Invalid input: " + yytext());
}
					case -31:
						break;
					case 33:
						{
  //return new Symbol(sym.ERROR, "Invalid input: " + yytext());
}
					case -32:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
