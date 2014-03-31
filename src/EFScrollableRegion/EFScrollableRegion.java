/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EFScrollableRegion;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;








/**
 *
 * @author Taylor
 */
public class EFScrollableRegion
{
  private int x;
  private int y;
  private int width;
  private int height;
  
  private int regionX;
  private int regionY;
  
  private BufferedImage image;
  
  
  
  public EFScrollableRegion(int x, int y, int width, int height, String directory)
  {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    
    this.regionX = 0;
    this.regionY = 0;
    
    try
    {
      this.image = ImageIO.read(new File(directory));
    } catch (IOException e)
    {
      this.image = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
    }
  }
  
  
  
  public void drawRegion(Graphics2D c)
  {
    c.setColor(Color.white);
    c.fillRect(x, y, width, height);
    int regionWidth = regionX + width;
    int regionHeight = regionY + height;
    if( regionWidth > image.getWidth() )
    {
      regionWidth = image.getWidth();
    }
    if( regionHeight > image.getHeight())
    {
      regionHeight = image.getHeight();
    }
    c.drawImage(image.getSubimage(regionX, regionY, regionWidth, regionHeight), x, y, null);
  }
  
  
  
  public boolean isMouseOn(int mouseX, int mouseY)
  {
    return(mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height);
  }
}







