package Lex;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.File;

/*
   Title:   Lexical Analyzer
   Author:  Timothy Manno
   Date:    2/13/2020
   Version: 1.0
   Purpose: To read in source code and seperate it into tokens and symbol table
*/

public class Lexi
{   
   private Scanner file;
   private String source;
   private int codeAddr;
   private int dataAddr;
   private final String [] SEGS = {"DS","CS"};
   private Token [] token;
   private ArrayList<Token> modToks;
   private ArrayList<Token> als;
   private ArrayList<Symbol> sym;
   private final char [] chars = {'L','D','=',',',';','+','–','*','/','(',')','<','>','{','}','!','\t','\n',' '};
   private final int [][] stateTab = {{1,2,3,23,24,21,22,10,4,25,26,8,9,27,28,7,0,0,0},
                                      {1,1,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12},
                                      {1,2,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11},
                                      {14,14,15,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14},
                                      {13,13,13,13,13,13,13,5,13,13,13,13,13,13,13,13,13,13,13},
                                      {5,5,5,5,5,5,5,6,5,5,5,5,5,5,5,5,5,5,5},
                                      {5,5,5,5,5,5,5,5,0,5,5,5,5,5,5,5,5,5,5},
                                      {29,29,20,29,29,29,29,29,29,29,29,29,29,29,29,29,29,29,29},
                                      {16,16,17,29,29,29,29,29,29,29,29,29,29,29,29,29,16,16,16},
                                      {18,18,19,29,29,29,29,29,29,29,29,29,29,29,29,29,18,18,18}};
   private final String [][] resw = {{"CONST", "$Const"}, {"IF", "$If"}, {"DO", "$Do"}, {"CLASS", "$Class"}, {"PROC", "$Proc"},
                                     {"VAR", "$Var"}, {"WHILE", "$While"}, {"THEN", "$Then"}, {"CALL", "$Call"}, {"ODD", "$Odd"}};
   private final String [] clsTypes = {"$Class","<var>","$Const","<assign>","<int>","<comma>","<semi>","$Var","$LB","$RB"};
   private final int [][] symState = {{1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
                                      {-1,2,-1,-1,-1,-1,-1,-1,-1,-1},
                                      {-1,-1,-1,-1,-1,-1,-1,-1,3,-1},
                                      {-1,10,4,-1,-1,-1,-1,8,-1,-1},
                                      {-1,5,-1,-1,-1,-1,-1,-1,-1,-1},
                                      {-1,-1,-1,6,-1,-1,-1,-1,-1,-1},
                                      {-1,-1,-1,-1,7,-1,-1,-1,-1,-1},
                                      {-1,-1,-1,-1,-1,4,3,-1,-1,-1},
                                      {-1,9,-1,-1,-1,-1,-1,-1,-1,-1},
                                      {-1,-1,-1,-1,-1,8,3,-1,-1,-1},
                                      {10,10,10,10,11,10,10,10,10,10},
                                      {-1,10,10,10,-1,-1,10,-1,10,12}};
   
   public Lexi(String fname)
   {
     /*try
      {
         //file = new Scanner(new File(fname));
      }
      catch (FileNotFoundException fnfe)
      {
         System.err.println("File is missing.. " + fnfe);
      }*/
      source = fname;
      als = new ArrayList<>();
      sym = new ArrayList<>();
      modToks = new ArrayList<>();
      codeAddr = 0;
      dataAddr = 0;
      processString(source);
      processSymbolTable();
      //readData();
      setUpMod();
      //closeFile();
   }
   
   /*
      Purpose: Loops through entire file line by line and sends data
               to the processString() method for further analysis.
      Inputs:  No Inputs
      Outputs: No Outputs
      Date:    2/13/2020
      Version: 1.0
      Author:  Timothy Manno
   */
   private void readData()
   {
      String data = "";
      // read data from file to array list      
      while (file.hasNext())
      {
         // pass string to be processed
         data += file.next() + " ";
         //processString(file.nextLine());
         //processString(file.next());
      }
      processString(data);
      // after source code is processed then create symbol table
      processSymbolTable();
   }
   
   /*
      Purpose: Process string into char array and feed it to Finite State Machine
               a character at a time so that tokens are seperated correctly.
      Inputs:  String s
      Outputs: No Outputs
      Date:    2/13/2020
      Version: 1.0
      Author:  Timothy Manno
   */
   private void processString(String s)
   {
      // declare/initialize char array based on string received
      char [] c = s.toCharArray();
      int state = 0;
      int locate = 0;
      String item = "";
      boolean tab = false;
      
      // loop through char array and check each char/state using fsa
      //for (int i = 0; i < c.length; i++)
      for (char atom: c)
      {
         locate = getLoc(atom);
         if (locate < 0)
         {
            System.err.println("Error occurred");
            return;
         }
         do
         {
            tab = false;
            switch (state)
            {
               case 0:
                  state = stateTab[state][locate];
                  break;
               case 1:
                  state = stateTab[state][locate];
                  break;
               case 2:
                  state = stateTab[state][locate];
                  break;
               case 3:
                  state = stateTab[state][locate];
                  break;
               case 4:
                  state = stateTab[state][locate];
                  break;
               case 5:
                  state = stateTab[state][locate];
                  break;
               case 6:
                  state = stateTab[state][locate];
                  break;
               case 7:
                  state = stateTab[state][locate];
                  break;
               case 8:
                  state = stateTab[state][locate];
                  break;
               case 9:
                  state = stateTab[state][locate];
                  break;
               case 10:
                  als.add(new Token(item,"<mop>"));
                  item = "";
                  state = 0;
                  tab = false;
                  break;
               case 11:
                  als.add(new Token(item,"<int>"));
                  item = "";
                  state = 0;
                  tab = false;
                  break;
               case 12:
                  addReswords(item);
                  item = "";
                  state = 0;
                  tab = false;
                  break;
               case 13:
                  als.add(new Token(item,"<mop>"));
                  item = "";
                  state = 0;
                  tab = false;
                  break;
               case 14:
                  als.add(new Token(item,"<assign>"));
                  item = "";
                  state = 0;
                  tab = false;
                  break;
               case 15:
                  als.add(new Token(item,"<relop>"));
                  item = "";
                  state = 0;
                  tab = false;
                  break;
               case 16:
                  als.add(new Token(item,"<relop>"));
                  item = "";
                  state = 0;
                  tab = false;
                  break;
               case 17:
                  als.add(new Token(item,"<relop>"));
                  item = "";
                  state = 0;
                  tab = false;
                  break;
               case 18:
                  als.add(new Token(item,"<relop>"));
                  item = "";
                  state = 0;
                  tab = false;
                  break;
               case 19:
                  als.add(new Token(item,"<relop>"));
                  item = "";
                  state = 0;
                  tab = false;
                  break;
               case 20:
                  als.add(new Token(item,"<relop>"));
                  item = "";
                  state = 0;
                  tab = false;
                  break;
               case 21:
                  als.add(new Token(item,"<addop>"));
                  item = "";
                  state = 0;
                  tab = false;
                  break;
               case 22:
                  als.add(new Token(item,"<addop>"));
                  item = "";
                  state = 0;
                  tab = false;
                  break;
               case 23:
                  als.add(new Token(item,"<comma>"));
                  item = "";
                  state = 0;
                  tab = false;
                  break;
               case 24:
                  als.add(new Token(item,"<semi>"));
                  item = "";
                  state = 0;
                  tab = false;
                  break;
               case 25:
                  als.add(new Token(item,"$LP"));
                  item = "";
                  state = 0;
                  tab = false;
                  break;
               case 26:
                  als.add(new Token(item,"$RP"));
                  item = "";
                  state = 0;
                  tab = false;
                  break;
               case 27:
                  als.add(new Token(item,"$LB"));
                  item = "";
                  state = 0;
                  tab = false;
                  break;
               case 28:
                  als.add(new Token(item,"$RB"));
                  item = "";
                  state = 0;
                  tab = false;
                  break;
               default:
                  System.err.println("Error Occurred");
            }
            if (locate < 16 && !tab)
            {
               item += atom;
            }
            else if (state > 9)
            {
               tab = true;
            }
         }
         while (tab);
      }
   }
   
   /*
      Purpose: To determine whether given string is a variable or
               if the string belongs to the keywords given in array resw.
      Inputs:  String s
      Outputs: No Outputs
      Date:    2/13/2020
      Version: 1.0
      Author:  Timothy Manno
   */
   private void addReswords(String s)
   {
      String keyword;
      String classWord;
      // take string s and compare it to resw array
      for (int i = 0; i < resw.length; i++)
      {
         keyword = resw[i][0];
         // compare s to each position in array
         if (s.equals(keyword))
         {
            classWord = resw[i][1];
            // add to token array list
            als.add(new Token(keyword, classWord));
            return;
         }
      }
      // keyword not found then token is a variable
      als.add(new Token(s,"<var>"));
   }
   
   /*
      Purpose: takes a char value and searches an array called chars to find
               the location for my state table.
      Author:  Timothy Manno
      Date:    2/13/2020
      Version: 1.0
      Inputs:  Character c
      Outputs: Integer used for index of array
   */
   private int getLoc(char c)
   {
      if (Character.isLetter(c))
      {
         // process through fsa array looking from perspective of L
         return 0;
      }
      else if (Character.isDigit(c))
      {
         // process through fsa array looking from perspective of D
         return 1;
      }
      // loop through chars array to find index for fas table
      for (int i = 2; i < chars.length; i++)
      {        
         if ( c == chars[i] )
         {
            return i;
         }
      }
      return -1;
   }
   
   /*
      Purpose: after data is processed close the file
      Author:  Timothy Manno
      Date:    2/13/2020
      Version: 1.0
      Inputs:  No Inputs
      Outputs: No Outputs
   */
   private void closeFile()
   {
      file.close();
   }
   
   /*
      Purpose: prints the data stored in the array list
      Author:  Timothy Manno
      Date:    2/13/2020
      Version: 1.0
      Inputs:  No Inputs
      Outputs: No Outputs
   */
   public void print()
   {
      /*for (int i = 0; i < sym.size(); i++)
      {
         System.out.println(sym.get(i));
      }
      for (int i = 0; i < als.size(); i++)
      {
         System.out.println(als.get(i));
      }*/
      for (int i = 0; i < modToks.size(); i++)
      {
         System.out.println(modToks.get(i));
      }
   }
   
   private void processSymbolTable()
   {
      // loop through token array list
      Token tok = new Token();
      String tempCls = "";
      String tempTok = "";
      String tempHold;
      int state = 0;
      int locate = 0;
      int symLen;
      
      for (int i = 0; i < als.size(); i++)
      {
         tok.setToken(als.get(i).getToken());
         tok.setCls(als.get(i).getCls());
         locate = getSymLoc(tok.getCls());
         
         switch (state)
         {
            case 0:
               state = symState[state][locate];
               break;
            case 1:
               state = symState[state][locate];
               tempTok = tok.getToken() + ": ";
               // store class name var (token, class, value, addr, seg)
               sym.add(new Symbol(tok.getToken(),"<CName>",tempTok,String.valueOf(codeAddr),SEGS[1]));
               codeAddr += 2;
               break;
            case 2:
               state = symState[state][locate];
               break;
            case 3:
               state = symState[state][locate];
               break;
            case 4:
               state = symState[state][locate];
               tempTok = tok.getToken();
               tempCls = tok.getCls();
               break;
            case 5:
               state = symState[state][locate];
               break;
            case 6:
               state = symState[state][locate];
               // add new Const (token, class, value, addr, seg)
               sym.add(new Symbol(tempTok,tempCls,tok.getToken(),String.valueOf(dataAddr), SEGS[0]));
               dataAddr += 2;
               break;
            case 7:
               state = symState[state][locate];
               break;
            case 8:
               state = symState[state][locate];
               // store variable name
               sym.add(new Symbol(tok.getToken(),tok.getCls(),"?",String.valueOf(dataAddr),SEGS[0]));
               dataAddr += 2;
               break;
            case 9:
               state = symState[state][locate];
               break;
            case 10:
               state = symState[state][locate];
               if (state == 11)
               {
                  tempTok = "lit" + tok.getToken();
                  tempCls = "<numLit>";
                  sym.add(new Symbol(tempTok,tempCls,tok.getToken(),String.valueOf(dataAddr),SEGS[0]));
                  dataAddr += 2;
               }
               else
               {
                  continue;
               }
               break;
            case 11:
               state = symState[state][locate];
               break;
            case 12:
               state = symState[state][locate];
               return;
            case -1:
               //throw new WrongItemException(tok.getToken() + "\nThis is unexpected token!!");
               //System.err.println("Error");
               break;
            default:
               state = 10;
         }
      }
      // set up temp locations
      symLen = sym.size() / 2 + 1;
      for (int i = 1; i < symLen; i++)
      {
         tempHold = "t" + i;
         sym.add(new Symbol(tempHold,"<temp_int>","?",String.valueOf(dataAddr),SEGS[0]));
         dataAddr += 2;
      }
   }
   private void setUpMod()
   {
      boolean flag = true;
      for (Token t: als)
      {
         if (t.getCls().equals("$Const") || t.getCls().equals("$Var"))
         {
            flag = true;
         }
         if (!flag)
         {
            modToks.add(t);
         }
         if (t.getCls().equals("<semi>"))
         {
            flag = false;
         }
      }
   }
   private int getSymLoc(String s)
   {
      String temp;
      for (int i = 0; i < clsTypes.length; i++)
      {
         // loop through all items in clsTypes array
         temp = clsTypes[i];
         
         if (s.equals(temp))
         {
            return i;
         }
      }
      
      return 0;
   }
   
   public ArrayList<Symbol> getSymTab()
   {
      return sym;
   }
   
   public ArrayList<Token> getTokenList()
   {
      return als;
   }
   
   public int getTokenSize()
   {
      return als.size();
   }
   
   public int getSymbolSize()
   {
      return sym.size();
   }
   
   public ArrayList<Token> getModTokList()
   {
      return modToks;
   }
   
   public int getModSize()
   {
      return modToks.size();
   }
}