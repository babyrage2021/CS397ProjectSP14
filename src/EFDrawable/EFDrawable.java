/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EFDrawable;
import EFCoord.*;
import java.awt.Graphics2D;








/**
 *
 * @author Taylor
 */
public interface EFDrawable
{
  public EFCoord realPositions = new EFCoord();
  
  public int pixelXPos = 0;
  public int pixelYPos = 0;
  public int pixelZPos = 0;
  
  public RealPosJustification justification = RealPosJustification.TO_ORIGIN;
  
  public boolean draw( Graphics2D graphContext, EFCoord cameraPosition );
}







