package Lex;

public class Symbol
{
   // hidden data fields for Symbol class
   private String tok;
   private String cls;
   private String value;
   private String addr;
   private String seg;
   
   // default constructor for Symbol class
   public Symbol()
   {
      tok = null;
      cls = null;
      value = null;
      addr = null;
      seg = null;
   }
   
   // overloaded constructor for Symbol class
   public Symbol(String t, String c, String v, String a, String s)
   {
      tok = t;
      cls = c;
      value = v;
      addr = a;
      seg = s;
   }
   
   // mutator methods for Symbol class
   public void setToken(String t)
   {
      tok = t;
   }
   
   public void setCls(String c)
   {
      cls = c;
   }
   
   public void setValue(String v)
   {
      value = v;
   }
   
   public void setAddress(String a)
   {
      addr = a;
   }
   
   public void setSegment(String s)
   {
      seg = s;
   }
   
   // accessor methods for Symbol class
   public String getToken()
   {
      return tok;
   }
   
   public String getCls()
   {
      return cls;
   }
   
   public String getValue()
   {
      return value;
   }
   
   public String getAddress()
   {
      return addr;
   }
   
   public String getSegment()
   {
      return seg;
   }
   
   // to string method for printing purposes
   public String toString()
   {
      if (cls.equals("<numLit>"))
      {
         return tok + "\t\t" + cls + "\t" + value + "\t\t" + addr + "\t\t" + seg;
      }
      else
      {
         return tok + "\t\t" + cls + "\t\t" + value + "\t\t" + addr + "\t\t" + seg;
      }
   }
}