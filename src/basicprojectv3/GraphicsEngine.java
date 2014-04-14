//MainPanel.java
//Author: Taylor Cogdill
//Purpose: Creates a JPanel that the game will run in and be drawn on
//Quick use guide: Just construct a new MainPanel and call start()
package basicprojectv3;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Date;
import java.awt.image.*;
import EFTimeTracker.*;
import Entities.*;
import EFCoord.*;
import EFResolution.*;
import EFButton.*;
import EFHelpTag.*;
import java.awt.geom.AffineTransform;






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
    /*c.drawRect(1, 1, gameEngine.getCanvasWidth() - 2, gameEngine.getCanvasHeight() / 6);
     * 
     * c.drawRect(1, gameEngine.getCanvasHeight() / 6 + 2, gameEngine.getCanvasWidth() / 5,
     * gameEngine.getCanvasHeight() - (gameEngine.getCanvasHeight() / 6 + 2));
     * 
     * c.drawRect(gameEngine.getCanvasWidth() / 5 + 2, gameEngine.getCanvasHeight() / 6 + 2,
     * (int)((float)gameEngine.getCanvasWidth() / 2.5f),
     * gameEngine.getCanvasHeight() / 6 + 2);
     * 
     * c.drawRect(gameEngine.getCanvasWidth() / 5 + 2 + (int)((float)gameEngine.getCanvasWidth() / 2.5f),
     * gameEngine.getCanvasHeight() / 6 + 2,
     * (int)((float)gameEngine.getCanvasWidth() / 2.5f) - 1,
     * gameEngine.getCanvasHeight() / 6 + 2);
     * 
     * c.drawRect(gameEngine.getCanvasWidth() / 5 + 2,
     * gameEngine.getCanvasHeight() / 6 + 2 + gameEngine.getCanvasHeight() / 6 + 2,
     * (int)((float)gameEngine.getCanvasWidth() / 2.5f),
     * gameEngine.getCanvasHeight() / 2 + 2);
     * 
     * c.drawRect(gameEngine.getCanvasWidth() / 5 + 2 + (int)((float)gameEngine.getCanvasWidth() / 2.5f),
     * gameEngine.getCanvasHeight() / 6 + 2 + gameEngine.getCanvasHeight() / 6 + 2,
     * (int)((float)gameEngine.getCanvasWidth() / 2.5f),
     * (int)((float)gameEngine.getCanvasHeight() / 1.5) - 5);*/
    
    for( int i = 0; i < buttonsToDraw.size(); i++ )
    {
      if( buttonsScreenContext.get(i) == CurrentScreen.DEBUG || 
            buttonsScreenContext.get(i) == CurrentScreen.UNIVERSAL )
      {
        buttonsToDraw.get(i).drawButton(buttonsToDraw.get(i).isCursorOn(mouseX, mouseY), c,
              cameraPosition);
      }
    }
    
    c.setColor(new Color( Color.red.getRed(), Color.red.getGreen(), Color.red.getBlue(), 64));
    c.fillOval(mouseX - (ballDiameter / 2), mouseY - (ballDiameter / 2), ballDiameter, ballDiameter);
  }
  
}




  