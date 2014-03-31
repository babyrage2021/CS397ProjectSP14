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
import EFScrollableRegion.*;








public final class GameEngine extends Thread
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
  
  //<editor-fold defaultstate="collapsed" desc="Menu Buttons and Regions">
  
  //<editor-fold defaultstate="collapsed" desc="MAIN_MENU">
  
  private EFButton toDebugFromMain;
  
//</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="DEBUG">
  
  private EFButton toMainFromDebug;
  private EFButton iniStatus;
  
  private EFButton menuBar;
  private EFButton objectList;
  private EFButton acceptButton;
  private EFButton denyButton;
  private EFButton objectAtAGlance;
  private EFButton openDetails;
  private EFButton currentRules;
  
//</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="DEBUG_VIEW_DETAILS">
  
  private EFScrollableRegion testRegion;
  //acceptButton
  //denyButton
  //menuButton
  
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
    graphicsEngine = new GraphicsEngine(this);
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
    
    screenSize = new Dimension(Integer.parseInt(iniParser.getValue("window.width")),
     Integer.parseInt(iniParser.getValue("window.height")));
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
    
    canvasWidth = Integer.parseInt(iniParser.getValue("resolution.width"));
    canvasHeight = Integer.parseInt(iniParser.getValue("resolution.height"));
    canvasSize = new Dimension(canvasWidth, canvasHeight);
    canvas = new BufferedImage(canvasSize.width, canvasSize.height, BufferedImage.TYPE_4BYTE_ABGR );
    canvasGraphics = (Graphics2D) canvas.getGraphics();
    
    resolution = new EFResolution(10.0d, 10.0d, 10.0d);
    
    
    //<editor-fold defaultstate="collapsed" desc="EFButton Declarations">
    
    buttonsToDraw = new ArrayList();
    buttonsScreenContext = new ArrayList();
    
    EFButton.defaultHelpMessage = "";
    
    helpMessageButton = new EFButton(canvasGraphics, 0, 0, "", EFButton.defaultFont, Color.black,
          new Color(0f, 0f, 0f, 0f), DisplayType.TOP_LEFT, BorderType.LINE, 2, 1f, Color.black, 
          new Color(0f,0f,0f,0f), BackgroundType.OPAQUE,  Color.yellow, new Color(0f,0f,0f,0f), 1);
    helpMessageButton.shouldDraw = false;
    
    
    //<editor-fold defaultstate="collapsed" desc="DEBUG">
    EFButton.setDefaultBorderBuffer(5);

    
    EFButton.defaultDisplayType = DisplayType.TOP_LEFT;
    this.iniStatus = new EFButton(canvasGraphics, 1, 1, iniParser.getValue("test.2"));
    
    EFButton.defaultDisplayType = DisplayType.TOP_RIGHT;
    toMainFromDebug = new EFButton(canvasGraphics, canvas.getWidth() - 100, 1, "Main Menu");
    toMainFromDebug.setHelpMessage("Test for toMainFromDebug");
    
    EFButton.defaultMainBackgroundColor = Color.pink;
    EFButton.defaultMainBorderColor = Color.green;
    EFButton.defaultActivatedBackgroundColor = Color.blue;
    EFButton.defaultMainBorderColor = Color.blue;
    EFButton.defaultActivatedBorderColor = Color.yellow;
    EFButton.defaultBorderType = BorderType.LINE;
    EFButton.defaultMainMessageColor = Color.black;
    EFButton.defaultBackgroundType = BackgroundType.OPAQUE;
    
    EFButton.defaultDisplayType = DisplayType.TOP_LEFT;
    menuBar = new EFButton(canvasGraphics, 1, 1, "menu/health/sheilds");
    menuBar.setCustomBorderXLength(getCanvasWidth() - 3);
    menuBar.setCustomBorderYLength(getCanvasHeight() / 6 - 2);
    menuBar.setHelpMessage("Test For menuBar");
    graphicsEngine.loadButtonToDraw(menuBar, CurrentScreen.DEBUG);
    graphicsEngine.loadButtonToDraw(menuBar, CurrentScreen.DEBUG_VIEW_DETAILS);
    
    objectList = new EFButton(canvasGraphics, 1, getCanvasHeight() / 6 + 1, 
          "ObjectList");
    objectList.setCustomBorderXLength(getCanvasWidth() / 5 - 2);
    objectList.setCustomBorderYLength(getCanvasHeight() - (getCanvasHeight() / 6 + 3));
    graphicsEngine.loadButtonToDraw(objectList, CurrentScreen.DEBUG);
    
    acceptButton = new EFButton(canvasGraphics, getCanvasWidth() / 5 + 1, getCanvasHeight() / 6 + 1,
    "Accept");
    acceptButton.setCustomBorderXLength((int)((float)getCanvasWidth() / 2.5f));
    acceptButton.setCustomBorderYLength(getCanvasHeight() / 6 + 2);
    graphicsEngine.loadButtonToDraw(acceptButton, CurrentScreen.DEBUG);
    graphicsEngine.loadButtonToDraw(acceptButton, CurrentScreen.DEBUG_VIEW_DETAILS);
    acceptButton.addNewPreset("Default", acceptButton);
    acceptButton.setX(1);
    acceptButton.setCustomBorderYLength((int)(2.5f * (float)getCanvasHeight()) / 6 - 2);
    acceptButton.setCustomBorderXLength((int)((float)getCanvasWidth() / 4.25f));
    acceptButton.addNewPreset("forViewDetails", acceptButton);
    acceptButton.loadPreset("Default");
    
    denyButton = new EFButton(canvasGraphics, 
          getCanvasWidth() / 5 + 3 + (int)((float)getCanvasWidth() / 2.5f), 
          getCanvasHeight() / 6 + 1, "Deny");
    denyButton.setCustomBorderXLength((int)((float)getCanvasWidth() / 2.5f) - 5);
    denyButton.setCustomBorderYLength(getCanvasHeight() / 6 + 2);
    denyButton.addNewPreset("Default", denyButton);
    denyButton.setX(1);
    denyButton.setY((int)(3.5f * (float)getCanvasHeight()) / 6 + 1);
    denyButton.setCustomBorderYLength((int)(2.5f * (float)getCanvasHeight()) / 6 - 2);
    denyButton.setCustomBorderXLength((int)((float)getCanvasWidth() / 4.25f));
    denyButton.addNewPreset("forViewDetails", denyButton);
    denyButton.loadPreset("Default");
    graphicsEngine.loadButtonToDraw(denyButton, CurrentScreen.DEBUG);
    graphicsEngine.loadButtonToDraw(denyButton, CurrentScreen.DEBUG_VIEW_DETAILS);
    
    objectAtAGlance = new EFButton(canvasGraphics,
    getCanvasWidth() / 5 + 1,
    getCanvasHeight() / 6 + 3 + getCanvasHeight() / 6 + 2,
    "ObjectAtAGlance");
    objectAtAGlance.setCustomBorderXLength((int)((float)getCanvasWidth() / 2.5f));
    objectAtAGlance.setCustomBorderYLength(getCanvasHeight() / 2 + 2);
    graphicsEngine.loadButtonToDraw(objectAtAGlance, CurrentScreen.DEBUG);
    
    openDetails = new EFButton(canvasGraphics,
    getCanvasWidth() / 5 + 1,
    getCanvasHeight() / 6 + 3 + getCanvasHeight() / 6 + 3 + getCanvasHeight() / 2 + 3,
    "openDetails");
    openDetails.setCustomBorderXLength((int)((float)getCanvasWidth() / 2.5f));
    openDetails.setCustomBorderYLength(getCanvasHeight() - (getCanvasHeight() / 2 + 2) - 
          (getCanvasHeight() / 6 + 2) - (getCanvasHeight() / 6 + 2) - 5);
    graphicsEngine.loadButtonToDraw(openDetails, CurrentScreen.DEBUG);
    
    currentRules = new EFButton(canvasGraphics,
    getCanvasWidth() / 5 + 2 + (int)((float)getCanvasWidth() / 2.5f) + 1,
    getCanvasHeight() / 6 + 2 + getCanvasHeight() / 6 + 3,
    "currentRules");
    currentRules.setCustomBorderXLength((int)((float)getCanvasWidth() / 2.5f) - 5);
    currentRules.setCustomBorderYLength((int)((float)getCanvasHeight() / 1.5) - 6);
    graphicsEngine.loadButtonToDraw(currentRules, CurrentScreen.DEBUG);
   
    /*    private EFButton menuBar;
     * private EFButton objectList;
     * private EFButton acceptButton;
     * private EFButton denyButton;
     * private EFButton objectAtAGlance;
     * private EFButton openDetails;
     * private EFButton approveButton;*/
    
    
    
    graphicsEngine.loadButtonToDraw(iniStatus, CurrentScreen.DEBUG);
    graphicsEngine.loadButtonToDraw(toMainFromDebug, CurrentScreen.DEBUG);
    
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="MAIN_MENU">
    
    EFButton.defaultDisplayType = DisplayType.CENTER;
    
    EFButton.defaultBackgroundType = BackgroundType.ICON;
    toDebugFromMain = new EFButton(canvasGraphics, canvas.getWidth() / 2,
          canvas.getHeight() / 2, "Debug");
    toDebugFromMain.setMainIcon("Resources/buttonFrame.png");
    toDebugFromMain.setActivatedIcon("Resources/buttonFrame2.png");
    toDebugFromMain.setCustomBorderXLength(100);
    toDebugFromMain.setCustomBorderYLength(100);
    graphicsEngine.loadButtonToDraw(toDebugFromMain, CurrentScreen.MAIN_SCREEN);
    toDebugFromMain.addNewPreset("test", new EFButton(canvasGraphics, 50, 50, "Hi"));
    toDebugFromMain.addNewPreset("Default", toDebugFromMain);
    
//</editor-fold>
    
    testRegion = new EFScrollableRegion(50, 50, 200, 200, "Resources/ScrollableRegionTest.png");
    graphicsEngine.loadRegionToDraw(testRegion, CurrentScreen.DEBUG_VIEW_DETAILS);
    
    graphicsEngine.loadButtonToDraw(helpMessageButton, CurrentScreen.UNIVERSAL);
    
//</editor-fold>
    
    
    centerJPanel.addMouseListener(this);
    centerJPanel.addMouseMotionListener(this);
    mainFrame.addKeyListener(this);
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
        graphicsEngine.drawEverything(currentScreen, centerJPanel, canvas, cameraPosition, resolution);
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
      else if( openDetails.isCursorOn(mouseX, mouseY))
      {
        acceptButton.loadPreset("forViewDetails");
        denyButton.loadPreset("forViewDetails");
        currentScreen = CurrentScreen.DEBUG_VIEW_DETAILS;
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
        if (helpMessageButton.shouldDraw)
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
        for(int i = graphicsEngine.getButtonsScreenContext().size() - 1; i >= 0; i-- )
        {
          ArrayList<CurrentScreen> buttonContexts = graphicsEngine.getButtonsScreenContext();
          ArrayList<EFButton> buttons = graphicsEngine.getButtonsToDraw();
          
          if (buttonContexts.get(i) == currentScreen)
          {
            if (buttons.get(i).isCursorOn(mouseX, mouseY) && !helpMessageButton.shouldDraw &&
                  !buttons.get(i).getHelpMessage().isEmpty())
            {
              helpMessageButton.setMessage(buttons.get(i).getHelpMessage());
              helpMessageButton.setX(mouseX + 5);
              helpMessageButton.setY(mouseY + 5);
              helpMessageButton.shouldDraw = true;
            }
          }
        }
      }
      
      if (e.getKeyCode() == 80) //p
      {
      
        toDebugFromMain.loadPreset("test");
      }
      if (e.getKeyCode() == 79) //o
      {
      
        toDebugFromMain.loadPreset("Default");
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







