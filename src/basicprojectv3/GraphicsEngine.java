//MainPanel.java
//Author: Taylor Cogdill
//Purpose: Creates a JPanel that the game will run in and be drawn on
//Quick use guide: Just construct a new MainPanel and call start()
package basicprojectv3;

import EFButton.*;
import EFCoord.*;
import EFHelpTag.*;
import EFResolution.*;
import EFScrollableRegion.EFScrollableRegion;
import EFTimeTracker.*;
import Entities.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.util.Date;
import javax.swing.*;






public class GraphicsEngine
{
  
  //*Program Flow variables*\\
  private GameEngine gameEngine;
  private JFrame mainFrame;
  private CurrentScreen currentScreen;
  private int mouseX;
  private int mouseY;
  private boolean isAlive;
  
  //*Graphical variables*\\
  private JPanel centerJPanel;
  
  private int panelWidth;
  private int panelHeight;
  
  private int frameWidth;
  private int frameHeight;
  
  private int ballDiameter;
  
  private EFCoord cameraPosition;
  private EFResolution resolution;
  private EFButton helpMessageButton;
  
  ArrayList<EFButton> buttonsToDraw;
  ArrayList<CurrentScreen> buttonsScreenContext;
  
  ArrayList<EFScrollableRegion> regionsToDraw;
  ArrayList<CurrentScreen> regionsScreenContext;
  
  //*Graphics context for double buffering*\\
  private Graphics visableGraphics;
  private Dimension dimension;
  private BufferedImage canvas;
  private BufferedImage finalImage;
  private Graphics2D finalImageGraphics;
  private Graphics2D oDG; //*'g' is the graphics name*\\
  private Graphics2D g;
  
  
  
  
  
  
  
  public GraphicsEngine( GameEngine gameEngine)
  {
    this.gameEngine = gameEngine;
    buttonsToDraw = new ArrayList();
    buttonsScreenContext = new ArrayList();
    regionsToDraw = new ArrayList();
    regionsScreenContext = new ArrayList();
    
    mouseX = 0;
    mouseY = 0;
    
    ballDiameter = 30;
    
  }
  
  
  
  

  
  
  public void drawEverything (CurrentScreen currentScreen, Container container, BufferedImage offScreenCanvas,
        EFCoord cameraPosition, EFResolution resolution) 
  {
    mouseX = gameEngine.getMouseXPosition();
    mouseY = gameEngine.getMouseYPosition();
    
    Graphics2D context = (Graphics2D)offScreenCanvas.getGraphics();
    
    if( currentScreen == CurrentScreen.MAIN_SCREEN)
    {
      drawForMainMenu(context);
    }
    else if(currentScreen == CurrentScreen.DEBUG)
    {
      drawForDebug(context);
    }
    else if(currentScreen == CurrentScreen.DEBUG_VIEW_DETAILS)
    {
      drawForDebugViewDetails(context);
    }
    else if(currentScreen == CurrentScreen.LOADING_BEFORE_LEVEL)
    {
      drawForLoadingBeforeLevel(context);
    }
    else if(currentScreen == CurrentScreen.IN_GAME_MAIN)
    {
      drawForInGameMain(context);
    }
    else if(currentScreen == CurrentScreen.IN_GAME_VIEW_DETAILS)
    {
      drawForInGameViewDetails(context);
    }
    
    //helpMessageButton.drawButton(false, context, cameraPosition);
    
    visableGraphics = container.getGraphics();
    visableGraphics.drawImage (offScreenCanvas, 0, 0, container.getWidth(), container.getHeight(), null); 
    
  }
  
  
  
  //------------------------------------------------------------------------------------------------------------------\\
  
  
  public void loadButtonToDraw( EFButton button, CurrentScreen screenContext )
  {
    this.buttonsToDraw.add(button);
    this.buttonsScreenContext.add(screenContext);
  }
  
  private void drawForMainMenu(Graphics2D c)
  {
    c.setColor(Color.black);
    c.fillRect(0, 0, gameEngine.getCanvasWidth(), gameEngine.getCanvasHeight());
    for( int i = 0; i < buttonsToDraw.size(); i++ )
    {
      if( buttonsScreenContext.get(i) == CurrentScreen.MAIN_SCREEN || 
            buttonsScreenContext.get(i) == CurrentScreen.UNIVERSAL )
      {
        buttonsToDraw.get(i).drawButton(buttonsToDraw.get(i).isCursorOn(mouseX, mouseY), c,
              cameraPosition);
      }
    }
  }
  

  private void drawForDebug(Graphics2D c)
  {
    c.setColor(Color.black);
    c.fillRect(0, 0, gameEngine.getCanvasWidth(), gameEngine.getCanvasHeight());
    
    c.setColor(Color.red);
    c.fillOval(mouseX - (ballDiameter / 2), mouseY - (ballDiameter / 2), ballDiameter, ballDiameter);
    
    c.setColor(Color.yellow);
    
    drawButtons(c, CurrentScreen.DEBUG);
    
    c.setColor(new Color( Color.red.getRed(), Color.red.getGreen(), Color.red.getBlue(), 64));
    c.fillOval(mouseX - (ballDiameter / 2), mouseY - (ballDiameter / 2), ballDiameter, ballDiameter);
  }
  
  
  private void drawForDebugViewDetails(Graphics2D c)
  {
    c.setColor(Color.black);
    c.fillRect(0, 0, gameEngine.getCanvasWidth(), gameEngine.getCanvasHeight());
    
    c.setColor(Color.red);
    c.fillOval(mouseX - (ballDiameter / 2), mouseY - (ballDiameter / 2), ballDiameter, ballDiameter);
    
    drawButtons(c, CurrentScreen.DEBUG_VIEW_DETAILS);
    drawRegions(c, CurrentScreen.DEBUG_VIEW_DETAILS);
    
    c.setColor(new Color( Color.red.getRed(), Color.red.getGreen(), Color.red.getBlue(), 64));
    c.fillOval(mouseX - (ballDiameter / 2), mouseY - (ballDiameter / 2), ballDiameter, ballDiameter);
  }
  
  
  
  private void drawForLoadingBeforeLevel(Graphics2D c)
  {
    c.setColor(Color.black);
    c.fillRect(0, 0, gameEngine.getCanvasWidth(), gameEngine.getCanvasHeight());
    
    c.setColor(Color.red);
    c.fillOval(mouseX - (ballDiameter / 2), mouseY - (ballDiameter / 2), ballDiameter, ballDiameter);
    
    drawButtons(c, CurrentScreen.LOADING_BEFORE_LEVEL);
    drawRegions(c, CurrentScreen.LOADING_BEFORE_LEVEL);
    
    c.setColor(new Color( Color.red.getRed(), Color.red.getGreen(), Color.red.getBlue(), 64));
    c.fillOval(mouseX - (ballDiameter / 2), mouseY - (ballDiameter / 2), ballDiameter, ballDiameter);
  }
  

  private void drawForInGameMain(Graphics2D c)
  {
    c.setColor(Color.black);
    c.fillRect(0, 0, gameEngine.getCanvasWidth(), gameEngine.getCanvasHeight());
    
    c.setColor(Color.red);
    c.fillOval(mouseX - (ballDiameter / 2), mouseY - (ballDiameter / 2), ballDiameter, ballDiameter);
    
    c.setColor(Color.yellow);
    
    drawButtons(c, CurrentScreen.IN_GAME_MAIN);
    drawRegions(c, CurrentScreen.IN_GAME_MAIN);
    
    c.setColor(new Color( Color.red.getRed(), Color.red.getGreen(), Color.red.getBlue(), 64));
    c.fillOval(mouseX - (ballDiameter / 2), mouseY - (ballDiameter / 2), ballDiameter, ballDiameter);
  }
  
  
  private void drawForInGameViewDetails(Graphics2D c)
  {
    c.setColor(Color.black);
    c.fillRect(0, 0, gameEngine.getCanvasWidth(), gameEngine.getCanvasHeight());
    
    c.setColor(Color.red);
    c.fillOval(mouseX - (ballDiameter / 2), mouseY - (ballDiameter / 2), ballDiameter, ballDiameter);
    
    drawButtons(c, CurrentScreen.IN_GAME_VIEW_DETAILS);
    drawRegions(c, CurrentScreen.IN_GAME_VIEW_DETAILS);
    
    c.setColor(new Color( Color.red.getRed(), Color.red.getGreen(), Color.red.getBlue(), 64));
    c.fillOval(mouseX - (ballDiameter / 2), mouseY - (ballDiameter / 2), ballDiameter, ballDiameter);
  }
  
  
  
  public void drawButtons(Graphics2D c, CurrentScreen currentScreen)
  {
    
    for( int i = 0; i < buttonsToDraw.size(); i++ )
    {
      if( buttonsScreenContext.get(i) == currentScreen || 
            buttonsScreenContext.get(i) == CurrentScreen.UNIVERSAL )
      {
        buttonsToDraw.get(i).drawButton(buttonsToDraw.get(i).isCursorOn(mouseX, mouseY), c,
              cameraPosition);
      }
    }
  }
  
  
  
  public void drawRegions(Graphics2D c, CurrentScreen currentScreen)
  {
    
    for( int i = 0; i < regionsToDraw.size(); i++ )
    {
      if( regionsScreenContext.get(i) == currentScreen || 
            regionsScreenContext.get(i) == CurrentScreen.UNIVERSAL )
      {
        regionsToDraw.get(i).drawRegion(c);
      }
    }
  }
  
  
  
  public ArrayList<EFButton> getButtonsToDraw()
  {
    return buttonsToDraw;
  }
  
  
  public ArrayList<CurrentScreen> getButtonsScreenContext()
  {
    return buttonsScreenContext;
  }
  
  
  public void loadRegionToDraw(EFScrollableRegion region, CurrentScreen context)
  {
    this.regionsToDraw.add(region);
    this.regionsScreenContext.add(context);
  }
  
  public ArrayList<EFScrollableRegion> getRegionsToDraw()
  {
    return regionsToDraw;
  }
  
  
  public ArrayList<CurrentScreen> getRegionsScreenContext()
  {
    return regionsScreenContext;
  }
  
  
  
}




  