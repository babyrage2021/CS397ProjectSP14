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
    c.setColor(Color.pink);
    c.fillRect(x, y, width, height);
    
    int regionWidth = width;
    int regionHeight = height;
    if( regionX + regionWidth > image.getWidth() )
    {
      regionWidth = (image.getWidth() - regionX);
    }
    if( regionY + regionHeight > image.getHeight())
    {
      regionHeight = (image.getHeight() - regionY);
    }
    c.drawImage(image.getSubimage(regionX, regionY, regionWidth, regionHeight), x, y, null);
  }
  
  
  
  public boolean isMouseOn(int mouseX, int mouseY)
  {
    return(mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height);
  }
  
  
  
  public int getX()
  {
    return x;
  }
  
  
  
  public int getY()
  {
    return y;
  }
  
  
  
  public void setRegionX(int regionX)
  {
    if( regionX + width < image.getWidth() && regionX >= 0 )
    {
      this.regionX = regionX;
    }
  }
  
  
  
  public void setRegionY(int regionY)
  {
    if( regionY + height < image.getHeight() && regionY >= 0 )
    {
      this.regionY = regionY;
    }
  }
  
  
  
  public int getRegionX()
  {
    return regionX;
  }
  
  
  
  public int getRegionY()
  {
    return regionY;
  }
  
  
  
  public void setImage(String directory)
  {
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
  
  
  
  public void setImage(BufferedImage image)
  {
    regionX = 0;
    regionY = 0;
    this.image = image;
  }
}







