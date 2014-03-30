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
import EFCoord.*;
import EFResolution.*;
import Entities.*;
import EFButton.*;
import EFHelpTag.*;
import EFIniParser.*;








public class GameEngine extends Thread
      implements MouseMotionListener,
      MouseListener,
      KeyListener
{
  private GraphicsEngine graphicsEngine;

  //*Program Flow variables*\\
  private boolean masterBool;
  private CurrentScreen currentScreen;
  private EFIniParser iniParser;
  private int mouseX;
  private int mouseY;
  
  private final int goalFPS = 60;
  private EFTimeTracker timeTracker;
  private Date timeGetter;
  
  private Dimension screenSize;
  private JPanel centerJPanel;
  private JFrame mainFrame;
  private EFCoord cameraPosition;
  private EFResolution resolution;
  private EFButton helpMessageButton;
  
  //<editor-fold defaultstate="collapsed" desc="Menu Buttons">
  
  //<editor-fold defaultstate="collapsed" desc="MAIN_MENU">
  
  private EFButton toDebugFromMain;
  
//</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="DEBUG">
  
  private EFButton toMainFromDebug;
  private EFButton iniStatus;
  
//</editor-fold>
  
//</editor-fold>
  
  private ArrayList<EFButton> buttonsToDraw;
  private ArrayList<CurrentScreen> buttonsScreenContext;

  private final String GRAPHICS_TAG = "Graphics Timer";
  
  private final String CANVAS_TAG = "Offscreen canvas";
  private Dimension canvasSize;
  private int canvasWidth;
  private int canvasHeight;
  private BufferedImage canvas;
  private Graphics2D canvasGraphics;
  
  private Graphics visableGraphics;
  private Dimension dimension;




  public GameEngine()
  {
    masterBool = true;
    currentScreen = CurrentScreen.MAIN_SCREEN;
    iniParser = new EFIniParser("Resources/Settings.ini");
    mouseX = 0;
    mouseY = 0;
    cameraPosition = new EFCoord();

    timeTracker = new EFTimeTracker();
    timeTracker.addEFTimeEvent(GRAPHICS_TAG, 1000);
    timeTracker.addTimerToWhiteList(GRAPHICS_TAG);

    timeGetter = new Date();
    
    screenSize = new Dimension(1024, 768);
    mainFrame = new JFrame();
    centerJPanel = new JPanel();
    
    mainFrame.getContentPane().setLayout(new BorderLayout());
    mainFrame.getContentPane().add(centerJPanel, BorderLayout.CENTER);
    
    //Setting the version number from file VersionNumber.txt
    String versionNumber = null;
    BufferedReader brIn = null; 
    try
    {
      brIn = new BufferedReader( new FileReader("Resources/VersionNumber.txt") );
      versionNumber = brIn.readLine();
    } catch (Exception e) 
    {
      e.printStackTrace();
    } 
    
    mainFrame.setTitle ("BasicProject - " + versionNumber); //*Please don't change this*\\
    mainFrame.setResizable(true);  
    mainFrame.setSize( screenSize.width, screenSize.height );
    mainFrame.setVisible(true);
    
    if( centerJPanel.getWidth() != screenSize.width )
    {
      mainFrame.setSize( screenSize.width + (screenSize.width - centerJPanel.getWidth()), 
            mainFrame.getHeight() );
    }
    if( centerJPanel.getHeight() != screenSize.height )
    {
      mainFrame.setSize( mainFrame.getWidth(), 
            screenSize.height + ( screenSize.height - centerJPanel.getHeight() ));
    }
    
    visableGraphics = centerJPanel.getGraphics();
    
    canvasWidth = 1024;
    canvasHeight = 768;
    canvasSize = new Dimension(canvasWidth, canvasHeight);
    canvas = new BufferedImage(canvasSize.width, canvasSize.height, BufferedImage.TYPE_4BYTE_ABGR );
    canvasGraphics = (Graphics2D) canvas.getGraphics();
    
    resolution = new EFResolution(10.0d, 10.0d, 10.0d);
    
    
    //<editor-fold defaultstate="collapsed" desc="EFButton Declarations">
    
    buttonsToDraw = new ArrayList();
    buttonsScreenContext = new ArrayList();
    
    helpMessageButton = new EFButton(canvasGraphics, 0, 0, "", EFButton.defaultFont, new Color(0f, 0f, 0f, 0f), 
                  Color.black, DisplayType.TOP_LEFT, BorderType.LINE, 2, 
                  1f, new Color(0f,0f,0f,0f), Color.black, BackgroundType.OPAQUE, new Color(0f,0f,0f,0f), Color.yellow, 
                  1);
    helpMessageButton.shouldDraw = false;
    
    //<editor-fold defaultstate="collapsed" desc="DEBUG">
    EFButton.setDefaultBorderBuffer(5);

    
    EFButton.defaultDisplayType = DisplayType.TOP_LEFT;
    this.iniStatus = new EFButton(canvasGraphics, 1, 1, iniParser.getValue("test"));
    buttonsToDraw.add(iniStatus);
    buttonsScreenContext.add(CurrentScreen.DEBUG);
    
    EFButton.defaultDisplayType = DisplayType.TOP_RIGHT;
    toMainFromDebug = new EFButton(canvasGraphics, canvas.getWidth() - 1, 1, "Main Menu");
    buttonsToDraw.add(toMainFromDebug);
    buttonsScreenContext.add(CurrentScreen.DEBUG);
  
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="MAIN_MENU">
    
    EFButton.defaultDisplayType = DisplayType.CENTER;
    
    EFButton.defaultBackgroundType = BackgroundType.ICON;
    toDebugFromMain = new EFButton(canvasGraphics, canvas.getWidth() / 2,
          canvas.getHeight() / 2, "Debug");
    toDebugFromMain.setMainIcon("Resources/buttonFrame.png");
    toDebugFromMain.setActivatedIcon("Resources/buttonFrame2.png");
    toDebugFromMain.setIsUsingCustomLengths(true);
    toDebugFromMain.setCustomBorderXLength(100);
    toDebugFromMain.setCustomBorderYLength(100);
    buttonsToDraw.add(toDebugFromMain);
    buttonsScreenContext.add(CurrentScreen.MAIN_SCREEN);
    
//</editor-fold>
    
//</editor-fold>
    
    
    centerJPanel.addMouseListener(this);
    centerJPanel.addMouseMotionListener(this);
    mainFrame.addKeyListener(this);
    
    graphicsEngine = new GraphicsEngine(this, mainFrame, centerJPanel, canvas, canvasGraphics, cameraPosition,
          resolution, buttonsToDraw, buttonsScreenContext, helpMessageButton);
  }




  @Override
  public void run()
  {
    if (masterBool)
    {
      masterLoop();
    }
    System.exit(0);
  }




  public void masterLoop()
  {

    while (masterBool)
    {

      if (timeTracker.getAndFlipIsTriggered(GRAPHICS_TAG)) //Controls graphics and feeds time for the graphics timer
      {
        
        timeTracker.feedInitialTime(GRAPHICS_TAG, System.currentTimeMillis());
        graphicsEngine.drawEverything(currentScreen, centerJPanel, canvas);
        timeTracker.feedFinalTime(GRAPHICS_TAG, System.currentTimeMillis());
        
      }
      
      

      {
        timeTracker.sleepThread();
      }

      if (!mainFrame.isVisible())
      {
        masterBool = false;
      }
    }
  }




  public void mouseMoved(MouseEvent e)
  {
    calculateMousePositions(e);
  }




  public void mouseDragged(MouseEvent e)
  {
    calculateMousePositions(e);
  }




  public void mouseExited(MouseEvent e)
  {
    calculateMousePositions(e);
  }




  public void mouseEntered(MouseEvent e)
  {
    calculateMousePositions(e);
  }




  public void mouseReleased(MouseEvent e)
  {
    calculateMousePositions(e);
  }




  public void mousePressed(MouseEvent e)
  {
    calculateMousePositions(e);
    
    if(currentScreen == CurrentScreen.MAIN_SCREEN)
    {
      if( toDebugFromMain.isCursorOn(mouseX, mouseY) )
      {
        currentScreen = CurrentScreen.DEBUG;
      }
    }
    else if(currentScreen == CurrentScreen.DEBUG)
    {
      if( toMainFromDebug.isCursorOn(mouseX, mouseY))
      {
        currentScreen = CurrentScreen.MAIN_SCREEN;
      }
    }
  }




  public void mouseClicked(MouseEvent e)
  {
    calculateMousePositions(e);
  }




  public void keyReleased(KeyEvent e)
  {
    if( e.getSource() == mainFrame )
    {
      if (e.getKeyCode() == 72)
      {
        if (currentScreen == CurrentScreen.MAIN_SCREEN)
        {
          helpMessageButton.shouldDraw = false;
        }
      }
    }
  }




  public void keyTyped(KeyEvent e)
  {
    
  }




  public void keyPressed(KeyEvent e)
  {
    if( e.getSource() == mainFrame )
    {
      if (e.getKeyCode() == 72) //h
      {
        if (currentScreen == CurrentScreen.MAIN_SCREEN)
        {
          if (toDebugFromMain.isCursorOn(mouseX, mouseY) && !helpMessageButton.shouldDraw)
          {
            helpMessageButton.setMessage(toDebugFromMain.getHelpMessage());
            helpMessageButton.setX(mouseX + 5);
            helpMessageButton.setY(mouseY + 5);
            helpMessageButton.shouldDraw = true;
          }
        }
      }
    }
  }
  
  
  public int getMouseXPosition()
  {
    return mouseX;
  }
  
  
  
  public int getMouseYPosition()
  {
    return mouseY;
  }
  
  
  
  public int getCanvasWidth()
  {
    return canvas.getWidth();
  }
  
  
  public int getCanvasHeight()
  {
    return canvas.getHeight();
  }
  
  
  
  private void calculateMousePositions(MouseEvent e)
  {
    if (e.getSource() == centerJPanel)
    {
      mouseX = Math.round((float)e.getX() * ((float)canvas.getWidth() / (float)centerJPanel.getWidth()));
      mouseY = Math.round((float)e.getY() * ((float)canvas.getHeight() / (float)centerJPanel.getHeight()));
      //graphicsEngine.updateCursors(mouseX, mouseY);
    }
  }

}







