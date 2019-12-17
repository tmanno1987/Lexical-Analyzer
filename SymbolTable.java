package Lex;

import java.util.*;

public class SymbolTable
{
   private class Symbol
   {
      private String tok;
      private String cls;
      private int value;
      private int addr;
      private String seg;
      
      public Symbol()
      {
         tok = null;
         cls = null;
         value = -1;
         addr = -1;
         seg = null;
      }
      
      public String toString()
      {
         return tok + "\t" + cls + "\t" + value + "\t" + addr + "\t" + seg;
      }
   }
   private ArrayList<String> als;
   private HashMap<String,Integer> sym;
   private Symbol [] data;
   private Token [] token;
   
   public SymbolTable(Token [] tok)
   {
      als = new ArrayList<>();
      sym = new HashMap<>();
      token = tok;
   }
   
   public void populate()
   {
      String s1, s2, s3;
      boolean check = false;
      // search through array list data for new variables
      for (int i = 0; i < token.length-1; i++)
      {
         // add data to string var
         s1 = token[i].getCls();
         s2 = token[i+1].getToken();
         // check for Const, Var or Class terms
         check = caseCheck(s1);
         // search for cases involving var, var and Num literals
         if (s1.equals("<comma>"))
         {
            if (token[i-2].equals("<assign>"))
            {
               // check to see if item is a const
               s3 = token[i-4].getCls();
               check = caseCheck(s3);
            }
            else
            {
               // check to see if item is var
               s3 = token[i-2].getCls();
               check = caseCheck(s3);
            }
         }
         s3 = token[i].getCls();
         // if check is true add to symbol table array list
         if (check)
         {
            // check to see if token is already in table
            if ( onlyOne(s2) )
            {
               als.add(s2);
               sym.put(s2, new Integer(i));
               sym.put(s3, new Integer(i));
            }
         }
      }
   }
   
   // add data into symbol table
   public void addData()
   {
      // fully initialize Symbol table array
      data = new Symbol[sym.size()/2];
      fillSym();
      int z = 0;
      
      // loop through and add data
      for (int i = 0; i < data.length; i+=2)
      {
         z = i + 1;
         data[i].tok = sym.get(als.get(i));
         data[i].cls = sym.get(als.get(z));
      }
   }
   
   private void fillSym()
   {
      for (int i = 0; i < data.length; i++)
      {
         data[i] = new Symbol();
      }
   }
   
   private boolean caseCheck(String s)
   {
      boolean check = false;
      
      switch(s)
      {
         case "$CONST":
            check = true;
            break;
         case "$VAR":
            check = true;
            break;
         case "$CLASS":
            check = true;
            break;
         case "<var>":
            check = true;
            break;
         case "<const>":
            check = true;
      }
      return check;
   }
   
   // check to see if there are more than one of the same variables so duplicates aren't added to symbol table
   private boolean onlyOne(String s)
   {
      // create boolean value to return and string to hold data
      boolean b = true;
      String d;
      // loop through symbol arrayList and check to see if string is there
      for (int i = 0; i < als.size(); i++)
      {
         d = als.get(i);
         // check to see if value is present
         if (d.equals(s))
         {
            // set value to false since there are 2
            b = false;
         }
      }
      // return boolean value
      return b;
   }
   
   private boolean validateNumLit(String num, String prev)
   {
      return false; //if (num.matches("[0-9]+") && prev.compareTo(
   }
}