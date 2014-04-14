/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EFResolution;








/**
 *
 * @author Taylor
 */
public class EFResolution
{
  public double realXToPixelX;
  public double realYToPixelY;
  public double realZToPixelZ;
  
  
  public EFResolution()
  {
    this.realXToPixelX = 1.0d;
    this.realYToPixelY = 1.0d;
    this.realZToPixelZ = 1.0d;
  }
  
  
  public EFResolution( double realXToPixelX, double realYToPixelY, double realZToPixelZ )
  {
    this.realXToPixelX = realXToPixelX;
    this.realYToPixelY = realYToPixelY;
    this.realZToPixelZ = realZToPixelZ;
  }
}







