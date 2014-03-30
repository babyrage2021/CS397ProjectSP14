/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.*;
import java.io.File;
import java.io.FilenameFilter;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;

import EFTimeTracker.*;
import EFSpriteAnimation.*;
import EFCoord.*;
import EFDrawable.*;
import EFResolution.*;








/**
 *
 * @author tjc5h4
 */
public class Entity implements EFDrawable
{

  //  <editor-fold desc="Class Variables">
  //  <editor-fold desc="Physical/Theoryetical positions">
  public int dummy;
  //  </editor-fold>

  //  <editor-fold desc="Graphical variables">
  private EFTimeTracker timeTracker;
  private EFSpriteAnimation animatedSprite;
  private final String ANIMATED_SPRITE_TAG;
  private EFResolution resolution;
  private int graphXCoord;
  private int graphYCoord;
  private boolean isVisible;
  //  </editor-fold>
  //  </editor-fold>




  //  <editor-fold desc="Methods">
  //  <editor-fold desc="Constructors">
  public Entity(EFTimeTracker timeTracker, EFResolution resolution, String spriteTag, String spritePath)
  {
    this.timeTracker = timeTracker;
    this.realPositions.xPosition = 0.0d;
    this.realPositions.yPosition = 0.0d;
    this.realPositions.zPosition = 0.0d;
    this.realPositions.theta = 0.0d;
    this.resolution = resolution;
    graphXCoord = 0;
    graphYCoord = 0;

    isVisible = true;

    this.ANIMATED_SPRITE_TAG = spriteTag;
    animatedSprite = new EFSpriteAnimation(spritePath, this.timeTracker, 100, resolution);
  }
  //  </editor-fold>




  //  <editor-fold desc="Getter/Setter Methods">
  //  Getters/Setters for xCoord    
  public boolean setXCoord(double coord)
  {
    this.realPositions.xPosition = coord;
    return true;
  }




  public double getXCoord()
  {
    return realPositions.xPosition;
  }




  //  Getters/Setters for yCoord
  public boolean setYCoord(double coord)
  {
    this.realPositions.yPosition = coord;
    return true;
  }




  public double getYCoord()
  {
    return realPositions.yPosition;
  }




  //  Getters/Setters for zCoord
  public boolean setZCoord(double coord)
  {
    this.realPositions.zPosition = coord;
    return true;
  }




  public double getZCoord()
  {
    return realPositions.zPosition;
  }




  //  Getters/Setters for isVisible
  public boolean setIsVisible(boolean isVisible)
  {
    this.isVisible = isVisible;
    return true;
  }




  public boolean getIsVisible()
  {
    return isVisible;
  }




  // Getters for GraphCoords 
  /**
   * Purpose: multiplies graphXCoord by xResolution to get an accurate xPosition by converting xCoord into pixels
   */
  public int getPixelXCoord(EFCoord cameraPosition)
  {
    if (justification == RealPosJustification.TO_CAMERA)
    {
      graphXCoord = Math.round((float) (realPositions.xPosition * resolution.realXToPixelX));
    } else if( justification == RealPosJustification.TO_ORIGIN)
    {
      graphXCoord = Math.round((float) ((realPositions.xPosition - cameraPosition.xPosition) * 
            resolution.realXToPixelX));
    } else
    {
      graphXCoord = pixelXPos;
    }
    return graphXCoord;
  }




  /**
   * Purpose: multiplies graphYCoord by yResolution and moves it up by zCoord multiplied by zResolution to get an
   * accurate yPosition by converting yCoord and zCoord into pixels
   */
  public int getPixelYCoord( EFCoord cameraPosition )
  {
    if (justification == RealPosJustification.TO_CAMERA)
    {
      graphYCoord = Math.round((float) (realPositions.yPosition * resolution.realYToPixelY))
            - Math.round((float) (realPositions.zPosition * resolution.realZToPixelZ));
    } else if( justification == RealPosJustification.TO_ORIGIN)
    {
      graphYCoord = Math.round((float) ((realPositions.yPosition - cameraPosition.yPosition) * 
            resolution.realYToPixelY)) - 
            Math.round((float) ((realPositions.zPosition - cameraPosition.zPosition) * 
                  resolution.realZToPixelZ));
    } else
    {
      graphYCoord = pixelYPos - pixelZPos;
    }
    return graphYCoord;
  }
  //  </editor-fold>




  //  <editor-fold desc="Utility Methods">
  /**
   * Draws the loaded sprite onto the specified graphics context, g
   */
  public boolean draw(Graphics2D g, EFCoord cameraPosition)
  {
    getPixelXCoord(cameraPosition);
    getPixelYCoord(cameraPosition);
    animatedSprite.drawFrame(g, (int) graphXCoord - animatedSprite.getWidthInPixels() / 2,
          (int) graphYCoord - animatedSprite.getHeightInPixels() / 2);

    return true;
  }
  //  </editor-fold>
  //  </editor-fold>
}







