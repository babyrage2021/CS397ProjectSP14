/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EFCoord;








/**
 *
 * @author Taylor
 */
public class EFCoord
{
  public double xPosition;
  public double yPosition;
  public double zPosition;
  public double theta;
  
  
  public EFCoord()
  {
    this.xPosition = 0.0d;
    this.yPosition = 0.0d;
    this.zPosition = 0.0d;
    this.theta = 0.0d;
  }
  
  
  public EFCoord( double xPosition, double yPosition )
  {
    this();
    this.xPosition = xPosition;
    this.yPosition = yPosition;
  }
  
  
  public EFCoord( double xPosition, double yPosition, double zPosition )
  {
    this( xPosition, yPosition );
    this.zPosition = zPosition;
  }
  
  
  public EFCoord( double xPosition, double yPosition, double zPosition, double theta )
  {
    this( xPosition, yPosition, zPosition );
    this.theta = theta;
  }
}







