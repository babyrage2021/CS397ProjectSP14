/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EFIniParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;








/**
 *
 * @author Taylor
 */
public class EFIniParser
{
  String directory;
  ArrayList<StatementBit> listOfStatements;
  
  
  public EFIniParser(String directory)
  {
    this.directory = directory;
    listOfStatements = new ArrayList();
    getProperties();
  }
  
  
  
  public String getValue(String statement)
  {
    for(int i = 0; i < listOfStatements.size(); i++)
    {
      if(listOfStatements.get(i).statement.equals(statement))
      {
        return listOfStatements.get(i).value;
      }
    }
    
    return "";
  }
  
  
  private void getProperties()
  {
    BufferedReader br = null;
    int lineNumber = 1;
    
    try
    {
      
      br = new BufferedReader(new FileReader( directory ));
      String line;
      String[] framePairs;
      
      while ((line = br.readLine()) != null) {
        framePairs = line.split("=");
        
        
        if( framePairs[0].length() >= 2 && framePairs[0].substring( 0, 2 ).equals("//") )
        { }
        else if( framePairs.length != 2 )
        {
          System.out.println("\t_$EF_!ERROR: Error in properties file. Syntax must be in the form \"keyword" +
                             "=input\"." );
        }
        else
        {
          listOfStatements.add(new StatementBit(framePairs[0], framePairs[1]));
        }
        lineNumber++;
        
      }
      
      
    }
    catch ( Exception e ) 
    {
      //
    }
    try
    {
      br.close();
    } catch ( Exception e ) {}
    
  }
}







class StatementBit
{
  String statement;
  String value;
  
  public StatementBit(String statement, String value)
  {
    this.statement = statement;
    this.value = value;
  }
}







