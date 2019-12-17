package Lex;

import java.util.*;
import java.io.*;

public class Lexi
{
   private ArrayList<String> data;
   private ArrayList<Token> symTab;
   private Token [] token;
   private Token tok;
   private Scanner file;
   private boolean comment;
   
   public Lexi(String fname)
   {
      try
      {
         // open file
         file = new Scanner(new File(fname));
         data = new ArrayList<>();
         symTab = new ArrayList<>();
         tok = new Token();
         comment = false;
         //fillArray();
      }
      catch (FileNotFoundException e)
      {
         System.err.println("File not found exception caught..");
      }
   }
   
   public void fillArray()
   {
      ArrayList<Integer> ali = new ArrayList<>();
      boolean flag = false;
      int count = 0;
      String s;
      int i1 = 0;
      int i2 = 0;
      char [] c;
      
      while (file.hasNext())
      {
         // get next line from program turn into char array
         s = file.next();
         c = s.toCharArray();
         count = 0;
         i1 = 0;
         
         // loop through char array locating specific index points
         for (int i = 0; i < c.length; i++)
         {
            // look for commas, semicolons and math symbols
            switch (c[i])
            {
               case '+':
               case '-':
               case '*':
               case '/':
               case '%':
               case ';':
               case ',':
               case '(':
               case ')':
               case '{':
               case '}':
                  i2 = i;
                  addString(s, i1, i2);
                  flag = true;
                  i1 = i;
                  break;
               case '=':
                  if (i + 1 < c.length)
                  {
                     switch (c[i+1])
                     {
                        case '=':
                        case '>':
                        case '<':
                           flag = true;
                           i2 = ++i;
                           addString(s, i1, i2);
                           i1 = i;
                           break;
                     }
                  }
                  break;
               default:
                  
            }
         }
      }
   }
   
   protected void tokenize()
   {
      // initialize token array and initialize each index of token array
      token = new Token[data.size()];
      initTokenArray();
      String s1 = null;
      String s2 = null;      
      
      // loop through data array list
      for (int i = 0; i < data.size(); i++)
      {         
         // add data from array list to string s
         s1 = data.get(i);
         
         // checks to see if reading a comment
         if (comment && s1.equals("*/") == false)
         {
            // continue to next item
            continue;
         }
         
         // run item through a switch statment
         switch (s1)
         {
            case "CLASS":
               s2 = "$CLASS";
               break;
            case "{":
               s2 = "$LB";
               break;
            case "}":
               s2 = "$RB";
               break;
            case "=":
               s2 = "<assign>";
               break;
            case "==":
            case "!=":
            case ">":
            case "<":
            case ">=":
            case "<=":
               s2 = "<relop>";
               break;
            case "CONST":
               s2 = "$CONST";
               break;
            case "PROC":
               s2 = "$PROC";
               break;
            case "WHILE":
               s2 = "$WHILE";
               break;
            case "DO":
               s2 = "$DO";
               break;
            case "IF":
               s2 = "$IF";
               break;
            case "THEN":
               s2 = "$THEN";
               break;
            case "ELSE":
               s2 = "$ELSE";
               break;
            case "/":
            case "*":
               s2 = "<mulop>";
               break;
            case "+":
            case "-":
               s2 = "<addop>";
               break;
            case "CALL":
               s2 = "$CALL";
               break;
            case "(":
               s2 = "$LB";
               break;
            case ")":
               s2 = "$RB";
               break;
            case "/*":
               s2 = "$LCOMMENT";
               comment = true;
               break;
            case "*/":
               s2 = "$RCOMMENT";
               comment = false;
               break;
            default: // checks for vars and const terms
               // make sure index is in bounds
               if (i - 1 > 0)
               {
                  // first check for class name
                  if (token[i-1].getCls().equals("$CLASS"))
                  {
                     // set s2 to <class> and continue
                     s2 = "<class>";
                     symTab.add(new Token(s1, s2));
                     //continue;
                  }
                  // check to see if procedure name
                  if (token[i-1].getCls().equals("$PROC"))
                  {
                     // set s2 to <proc> and continue
                     s2 = "<proc>";
                     symTab.add(new Token(s1, s2));
                     //continue;
                  }
                  // finally check for num literals
                  if (conLitCheck(token[i-2].getCls(), token[i-1].getCls(), s1))
                  {
                     // set s2 variable to <numLit> and continue
                     s2 = "<c_int>";
                     symTab.add(new Token(s1, s2));
                     //continue;
                  }
                  else if (intCheck(s1))
                  {
                     // set s2 variable to <numLit> and continue
                     s2 = "<numLit>";
                     symTab.add(new Token(s1, s2));
                     //continue;
                  }
               }
               else if (i - 4 > 0) // make sure in bounds of array index
               {
                  // check to see if const
                  if (constCheck(token[i-1].getCls(), s1) || constCheck(token[i-4].getCls(), s1))
                  {
                     // set s2 variable to <const> and advance to next item
                     s2 = "<const>";
                     symTab.add(new Token(s1, s2));
                     //continue;
                  }
               }
               else
               {
                  // next check for vars
                  if (varCheck(s1))
                  {
                     // set s2 variable to <var> and continue
                     s2 = "<var>";
                     symTab.add(new Token(s1, s2));
                     //continue;
                  }
               }
         }
         // add token to token array at index i
         token[i] = new Token(s1, s2);
      }
   }
   
   // method to check to see if integer value
   private boolean intCheck(String s)
   {
      boolean flag = false;
      
      // check to see if any letters appear
      if (varCheck(s))
      {
         // if any letters found then token is NOT integer value
         return false;
      }
      else
      {
         // if none found then search for numbers
         for (int i = 0; i < s.length(); i++)
         {
            // check to see if string value is an integer value
            if (s.charAt(i) >= '0' && s.charAt(i) <= '9')
            {
               flag = true;
            }
         }
      }
      // return either true or false depending on checks given
      return flag;
      //return s.matches("[0-9]+");
   }
   
   // method to check to see if variable
   private boolean varCheck(String s)
   {
      boolean flag = false;
      
      for (int i = 0; i < s.length(); i++)
      {
         if ((s.charAt(i) >= 'a' && s.charAt(i) <= 'z') || (s.charAt(i) >= 'A' && s.charAt(i) <= 'Z'))
         {
            flag = true;
         }
      }
      
      return flag;
      //return s.matches("[a-zA-Z]+");
   }
   
   // method to see if token is a num literal that is part of a const
   private boolean conLitCheck(String p1, String p2, String c)
   {
      // check to see if (p)rev is equal to = and p2 is equal to <const>
      return intCheck(c) && (p1.equals("<const>") || p2.equals("<assign>"));
   }
   
   // method to check to see if const
   private boolean constCheck(String prev, String curr)
   {
      // check to see if prev is a const
      if (prev.equals("$CONST") || prev.equals("<const>"))
      {
         // check to see if it passes var check
         if (varCheck(curr))
         {
            // if passed both checks return true
            return true;
         }
      }
      
      // if fails both checks then case is false
      return false;
   }
   
   private void initTokenArray()
   {
      // loop through token array
      for (int i = 0; i < token.length; i++)
      {
         // initialize token array at index i
         token[i] = new Token();
      }
   }
   
   private void addString(String s, int a, int b)
   {
      // add new string to the arraylist
      System.out.println(s + " " + a + " " + b);
      data.add(s.substring(a,b));
   }
   
   public void print()
   {
      for (int i = 0; i < token.length; i++)
      {
         System.out.println(token[i]);
      }
   }
   
   public void closeFile()
   {
      file.close();
   }
}

class Test
{
   public static void main(String [] args)
   {
      //Scanner sc = new Scanner(System.in);
      //System.out.print("Enter File Name: ");
      String fname = "Pgm1.txt";
      Lexi lex = new Lexi(fname);
      lex.fillArray();
      lex.tokenize();
      lex.print();
      lex.closeFile();
   }
}