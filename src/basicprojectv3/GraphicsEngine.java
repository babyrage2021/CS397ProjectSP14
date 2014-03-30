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
  private int cursorX;
  private int cursorY;
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
    
    cursorX = 0;
    cursorY = 0;
    this.cameraPosition = cameraPosition;
    this.resolution = resolution;
    this.helpMessageButton = helpMessageButton;
    this.buttonsToDraw = buttonsToDraw;
    this.buttonsScreenContext = buttonsScreenContext;
    
    visableGraphics = centerJPanel.getGraphics();
    
    this.canvas = canvas;
    oDG = (Graphics2D) canvasGraphics;
    g = oDG;
    
    ballDiameter = 30;
    
  }
  
  
  
  
  
  //------------------------------------------------------------------------------------------------------------------\\

  
  
  public void drawEverything (CurrentScreen currentScreen) 
  {
    
    
    g = oDG;
    
    if( currentScreen == CurrentScreen.MAIN_SCREEN)
    {
      drawForMainMenu();
    }
    else if(currentScreen == CurrentScreen.DEBUG)
    {
      drawForDebug();
    }
    
    
    for( int i = 0; i < buttonsToDraw.size(); i++ )
    {
      if( buttonsScreenContext.get(i) == currentScreen )
      {
        buttonsToDraw.get(i).drawButton(buttonsToDraw.get(i).isCursorOn(cursorX, cursorY), g, cameraPosition);
      }
    }
    
    
    helpMessageButton.drawButton(true, g, cameraPosition);
    
    
    visableGraphics = centerJPanel.getGraphics();
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
  
  
  
  
  
  
  
  public void updateCursors( int cursorX, int cursorY )
  {
    this.cursorX = cursorX;
    this.cursorY = cursorY;
  }
  
  
  public void loadButtonToDraw( EFButton button, CurrentScreen screenContext )
  {
    this.buttonsToDraw.add(button);
    this.buttonsScreenContext.add(screenContext);
  }
  
  private void drawForMainMenu()
  {
    g = oDG;
    g.setColor(Color.black);
    g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
  }
  

  private void drawForDebug()
  {
    g = oDG;
    g.setColor(Color.black);
    g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    g.setColor(Color.red);
    g.fillOval(cursorX - (ballDiameter / 2), cursorY - (ballDiameter / 2), ballDiameter, ballDiameter);
  }
  
}




  