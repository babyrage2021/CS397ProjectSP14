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
  
  
  
  
  
  
  
  public GraphicsEngine( GameEngine gameEngine, JFrame mainFrame, JPanel centerJPanel, 
        BufferedImage canvas, Graphics2D canvasGraphics,
        EFCoord cameraPosition, EFResolution resolution, ArrayList<EFButton> buttonsToDraw, 
        ArrayList<CurrentScreen> buttonsScreenContext, EFButton helpMessageButton)
  {
    this.gameEngine = gameEngine;
    this.mainFrame = mainFrame;
    this.centerJPanel = centerJPanel;
    isAlive = true;
    this.currentScreen = currentScreen;
    
    
    panelWidth = centerJPanel.getWidth();
    panelHeight = centerJPanel.getHeight();
    
    mouseX = 0;
    mouseY = 0;
    this.cameraPosition = cameraPosition;
    this.resolution = resolution;
    this.helpMessageButton = helpMessageButton;
    this.buttonsToDraw = buttonsToDraw;
    this.buttonsScreenContext = buttonsScreenContext;
    
    visableGraphics = centerJPanel.getGraphics();
    
    this.canvas = canvas;
    oDG = (Graphics2D) canvasGraphics;
    
    ballDiameter = 30;
    
  }
  
  
  
  

  
  
  public void drawEverything (CurrentScreen currentScreen, Container container, BufferedImage offScreenCanvas) 
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
    
    
    for( int i = 0; i < buttonsToDraw.size(); i++ )
    {
      if( buttonsScreenContext.get(i) == currentScreen )
      {
        buttonsToDraw.get(i).drawButton(buttonsToDraw.get(i).isCursorOn(mouseX, mouseY), context,
              cameraPosition);
      }
    }
    
    
    helpMessageButton.drawButton(true, context, cameraPosition);
    
    
    visableGraphics = container.getGraphics();
    visableGraphics.drawImage (canvas, 0, 0, centerJPanel.getWidth(), centerJPanel.getHeight(), null); 
    
    
    
    if( !mainFrame.isVisible() )
    {
        isAlive = false;
    }
  }
  
  
  
  //------------------------------------------------------------------------------------------------------------------\\
  
  
  
  public boolean getIsAlive()
  {
      return isAlive;
  }
  
  
  public void loadButtonToDraw( EFButton button, CurrentScreen screenContext )
  {
    this.buttonsToDraw.add(button);
    this.buttonsScreenContext.add(screenContext);
  }
  
  private void drawForMainMenu(Graphics2D c)
  {
    c.setColor(Color.black);
    c.fillRect(0, 0, gameEngine.getCanvasWidth(), gameEngine.getCanvasHeight());
  }
  

  private void drawForDebug(Graphics2D c)
  {
    c.setColor(Color.black);
    c.fillRect(0, 0, gameEngine.getCanvasWidth(), gameEngine.getCanvasHeight());
    
    c.setColor(Color.red);
    c.fillOval(mouseX - (ballDiameter / 2), mouseY - (ballDiameter / 2), ballDiameter, ballDiameter);
  }
  
}




  