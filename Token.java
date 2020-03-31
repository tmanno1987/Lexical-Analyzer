package Lex;

public class Token
{
   private String tok;
   private String cls;
   
   public Token()
   {
      tok = null;
      cls = null;
   }
   
   public Token(String tok, String cls)
   {
      this.tok = tok;
      this.cls = cls;
   }
   
   public String getToken()
   {
      return tok;
   }
   
   public String getCls()
   {
      return cls;
   }
   
   public void setToken(String t)
   {
      tok = t;
   }
   
   public void setCls(String c)
   {
      cls = c;
   }
   
   public String toString()
   {
      return tok + "\t\t" + cls;
   }
}