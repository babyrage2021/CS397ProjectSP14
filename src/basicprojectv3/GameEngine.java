//MainPanel.java
//Author: Taylor Cogdill
//Purpose: Creates a JPanel that the game will run in and be drawn on
//Quick use guide: Just construct a new MainPanel and call start()
package basicprojectv3;

import EFButton.*;
import EFCoord.*;
import EFHelpTag.*;
import EFIniParser.*;
import EFObject.EFObject;
import EFResolution.*;
import EFScrollableRegion.*;
import EFTimeTracker.*;
import Entities.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.util.Date;
import javax.swing.*;








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
  private EFIniParser buttonParser;
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
  private EFScrollableRegion regionToShift;
  private int regionXOffset;
  private int regionYOffset;
  
  //<editor-fold defaultstate="collapsed" desc="Menu Buttons and Regions">
  
  //<editor-fold defaultstate="collapsed" desc="MAIN_MENU">
  
  private EFButton toDebugFromMain;
  private EFButton toTestLevelOneFromMain;
  
//</editor-fold>
  
  
  //<editor-fold defaultstate="collapsed" desc="LOADING_BEFORE_LEVEL">
  
  private EFButton loadingMessage;
  
//</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="DEBUG">
  
  private EFButton toMainFromDebug;
  private EFButton iniStatus;
  
  private EFButton menuBar;
  private EFButton objectList;
  private EFButton acceptButton;
  private EFButton denyButton;
  private EFButton objectAtAGlance;
  private EFScrollableRegion objectAtAGlanceImage;
  private EFButton openDetails;
  private EFButton currentRules;
  private EFScrollableRegion currentRulesImage;
  private EFButton objectListUpButton;
  private EFButton objectListDownButton;
  private ArrayList<EFButton> objectButtons;
  private final int numberOfObjects;
  private int currentlySelectedObject;
  
//</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="DEBUG_VIEW_DETAILS">
  
  private EFScrollableRegion objectImageDisplay;
  //acceptButton
  //denyButton
  //menuButton
  //objectButtons
  
//</editor-fold>
  
//</editor-fold>
  
  private ArrayList<EFButton> buttonsToDraw;
  private ArrayList<CurrentScreen> buttonsScreenContext;

  private final String GRAPHICS_TAG = "Graphics Timer";
  private final String LOADING_TIMER_TAG = "Loading Timer";
  
  private final String CANVAS_TAG = "Offscreen canvas";
  private Dimension canvasSize;
  private int canvasWidth;
  private int canvasHeight;
  private BufferedImage canvas;
  private Graphics2D canvasGraphics;
  
  private Graphics visableGraphics;
  private Dimension dimension;
  
  
  
  private String levelName;
  private int numberOfObjectsInCurrentLevel;
  private ArrayList<EFObject> objectsInCurrentLevel;
  private int numberOfShields;
  private int currentlySelectedAttachment;
  EFButton shieldsRemainingTag;




  public GameEngine()
  {
    graphicsEngine = new GraphicsEngine(this);
    masterBool = true;
    currentScreen = CurrentScreen.MAIN_SCREEN;
    iniParser = new EFIniParser("Resources/Settings.ini");
    buttonParser = new EFIniParser("Resources/ButtonData/Settings.ini");
    mouseX = 0;
    mouseY = 0;
    cameraPosition = new EFCoord();

    timeTracker = new EFTimeTracker();
    timeTracker.addEFTimeEvent(GRAPHICS_TAG, 1000);
    timeTracker.addTimerToWhiteList(GRAPHICS_TAG);
    timeTracker.addEFTimeEvent(LOADING_TIMER_TAG, 5000);

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
    
    
    //<editor-fold defaultstate="collapsed" desc="EFButton and Region Declarations">
    
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
    graphicsEngine.loadButtonToDraw(menuBar, CurrentScreen.IN_GAME_MAIN);
    graphicsEngine.loadButtonToDraw(menuBar, CurrentScreen.IN_GAME_VIEW_DETAILS);
    
    shieldsRemainingTag = new EFButton(canvasGraphics, 1, 30, "3");
    graphicsEngine.loadButtonToDraw(shieldsRemainingTag, CurrentScreen.IN_GAME_MAIN);
    graphicsEngine.loadButtonToDraw(shieldsRemainingTag, CurrentScreen.IN_GAME_VIEW_DETAILS);
    
    objectList = new EFButton(canvasGraphics, 1, getCanvasHeight() / 6 + 1, 
          "ObjectList");
    objectList.setCustomBorderXLength(getCanvasWidth() / 5 - 2);
    objectList.setCustomBorderYLength(getCanvasHeight() - (getCanvasHeight() / 6 + 3));
    //graphicsEngine.loadButtonToDraw(objectList, CurrentScreen.DEBUG);
    
    
    numberOfObjects = 5;
    objectButtons = new ArrayList();
    currentlySelectedObject = -1;
    
    for( int i = 0; i < numberOfObjects; i++ )
    {
      objectButtons.add(new EFButton(canvasGraphics, 1, 
            getCanvasHeight() / 6 + 1 + getCanvasWidth() / 30 + 
                  (int)( (((float)getCanvasHeight() * (11f / 15f) / 
                  (float)numberOfObjects) * (float)i)),
            "object " + i ));
      objectButtons.get(i).setCustomBorderXLength(getCanvasWidth() / 5 - 2);
      objectButtons.get(i).setCustomBorderYLength((int)((float)getCanvasHeight() * (11f / 15f) / 
            (float)numberOfObjects - 2));
      objectButtons.get(i).addNewPreset("Default", objectButtons.get(i));
      objectButtons.get(i).mainBackgroundColor = Color.green;
      objectButtons.get(i).activatedBackgroundColor = Color.ORANGE;
      objectButtons.get(i).addNewPreset("forWhenSelected", objectButtons.get(i));
      objectButtons.get(i).loadPreset("Default");
      graphicsEngine.loadButtonToDraw(objectButtons.get(i), CurrentScreen.DEBUG);
      graphicsEngine.loadButtonToDraw(objectButtons.get(i), CurrentScreen.DEBUG_VIEW_DETAILS);
      graphicsEngine.loadButtonToDraw(objectButtons.get(i), CurrentScreen.IN_GAME_MAIN);
      graphicsEngine.loadButtonToDraw(objectButtons.get(i), CurrentScreen.IN_GAME_VIEW_DETAILS);
    }
    
    EFButton.defaultBackgroundType = BackgroundType.ICON;
    objectListUpButton = new EFButton(canvasGraphics, 1,
    getCanvasHeight() / 6 + 1,
    "Up");
    objectListUpButton.setMainIcon(buttonParser.getValue("objectListUpButton.mainIcon"));
    objectListUpButton.setActivatedIcon(buttonParser.getValue("objectListUpButton.activatedIcon"));
    objectListUpButton.setCustomBorderXLength(getCanvasWidth() / 5 - 2);
    objectListUpButton.setCustomBorderYLength(getCanvasWidth() / 30 - 2);
    graphicsEngine.loadButtonToDraw(objectListUpButton, CurrentScreen.DEBUG);
    graphicsEngine.loadButtonToDraw(objectListUpButton, CurrentScreen.DEBUG_VIEW_DETAILS);
    graphicsEngine.loadButtonToDraw(objectListUpButton, CurrentScreen.IN_GAME_MAIN);
    graphicsEngine.loadButtonToDraw(objectListUpButton, CurrentScreen.IN_GAME_VIEW_DETAILS);
    
    objectListDownButton = new EFButton(canvasGraphics, 1, 
          (int)(19f * (float)getCanvasHeight() / 20f),
          "Down");
    objectListDownButton.setMainIcon(buttonParser.getValue("objectListDownButton.mainIcon"));
    objectListDownButton.setActivatedIcon(buttonParser.getValue("objectListDownButton.activatedIcon"));
    objectListDownButton.setCustomBorderXLength(getCanvasWidth() / 5 - 2);
    objectListDownButton.setCustomBorderYLength(getCanvasWidth() / 30 - 2);
    graphicsEngine.loadButtonToDraw(objectListDownButton, CurrentScreen.DEBUG);
    graphicsEngine.loadButtonToDraw(objectListDownButton, CurrentScreen.DEBUG_VIEW_DETAILS);
    graphicsEngine.loadButtonToDraw(objectListDownButton, CurrentScreen.IN_GAME_MAIN);
    graphicsEngine.loadButtonToDraw(objectListDownButton, CurrentScreen.IN_GAME_VIEW_DETAILS);
    
    acceptButton = new EFButton(canvasGraphics, getCanvasWidth() / 5 + 1, getCanvasHeight() / 6 + 1,
    "Accept");
    acceptButton.setMainIcon(buttonParser.getValue("acceptButton.mainIcon"));
    acceptButton.setActivatedIcon(buttonParser.getValue("acceptButton.activatedIcon"));
    acceptButton.setCustomBorderXLength((int)((float)getCanvasWidth() / 2.5f));
    acceptButton.setCustomBorderYLength(getCanvasHeight() / 6 + 2);
    graphicsEngine.loadButtonToDraw(acceptButton, CurrentScreen.DEBUG);
    graphicsEngine.loadButtonToDraw(acceptButton, CurrentScreen.DEBUG_VIEW_DETAILS);
    graphicsEngine.loadButtonToDraw(acceptButton, CurrentScreen.IN_GAME_MAIN);
    graphicsEngine.loadButtonToDraw(acceptButton, CurrentScreen.IN_GAME_VIEW_DETAILS);
    acceptButton.addNewPreset("Default", acceptButton);
    acceptButton.setX(getCanvasWidth() / 5 + 2 + (int)(717f * (float)getCanvasWidth() / 1000f) - 2);
    acceptButton.setCustomBorderYLength((int)(2.5f * (float)getCanvasHeight()) / 6 - 2);
    acceptButton.setCustomBorderXLength((int)((float)getCanvasWidth() / 12f) - 2);
    acceptButton.addNewPreset("forViewDetails", acceptButton);
    acceptButton.loadPreset("Default");
    
    denyButton = new EFButton(canvasGraphics, 
          getCanvasWidth() / 5 + 3 + (int)((float)getCanvasWidth() / 2.5f), 
          getCanvasHeight() / 6 + 1, "Deny");
    denyButton.setMainIcon(buttonParser.getValue("denyButton.mainIcon"));
    denyButton.setActivatedIcon(buttonParser.getValue("denyButton.activatedIcon"));
    denyButton.setCustomBorderXLength((int)((float)getCanvasWidth() / 2.5f) - 5);
    denyButton.setCustomBorderYLength(getCanvasHeight() / 6 + 2);
    denyButton.addNewPreset("Default", denyButton);
    denyButton.setX(getCanvasWidth() / 5 + 2 + (int)(717f * (float)getCanvasWidth() / 1000f) - 2);
    denyButton.setY((int)(3.5f * (float)getCanvasHeight()) / 6 + 1);
    denyButton.setCustomBorderYLength((int)(2.5f * (float)getCanvasHeight()) / 6 - 2);
    denyButton.setCustomBorderXLength((int)((float)getCanvasWidth() / 12f) - 2);
    denyButton.addNewPreset("forViewDetails", denyButton);
    denyButton.loadPreset("Default");
    graphicsEngine.loadButtonToDraw(denyButton, CurrentScreen.DEBUG);
    graphicsEngine.loadButtonToDraw(denyButton, CurrentScreen.DEBUG_VIEW_DETAILS);
    graphicsEngine.loadButtonToDraw(denyButton, CurrentScreen.IN_GAME_MAIN);
    graphicsEngine.loadButtonToDraw(denyButton, CurrentScreen.IN_GAME_VIEW_DETAILS);
    
    objectAtAGlance = new EFButton(canvasGraphics,
    getCanvasWidth() / 5 + 1,
    getCanvasHeight() / 6 + 3 + getCanvasHeight() / 6 + 2,
    "ObjectAtAGlance");
    objectAtAGlance.setMainIcon(buttonParser.getValue("objectAtAGlance.mainIcon"));
    objectAtAGlance.setActivatedIcon(buttonParser.getValue("objectAtAGlance.activatedIcon"));
    objectAtAGlance.setCustomBorderXLength((int)((float)getCanvasWidth() / 2.5f));
    objectAtAGlance.setCustomBorderYLength(getCanvasHeight() / 2 + 2);
    graphicsEngine.loadButtonToDraw(objectAtAGlance, CurrentScreen.DEBUG);
    graphicsEngine.loadButtonToDraw(objectAtAGlance, CurrentScreen.IN_GAME_MAIN);
    
    EFButton.defaultBackgroundType = BackgroundType.OPAQUE;
    objectAtAGlanceImage = new EFScrollableRegion(getCanvasWidth() / 5 + 1,
    getCanvasHeight() / 6 + 3 + getCanvasHeight() / 6 + 2,
    (int)((float)getCanvasWidth() / 2.5f),
    getCanvasHeight() / 2 + 2,
    "");
    graphicsEngine.loadRegionToDraw(objectAtAGlanceImage, CurrentScreen.IN_GAME_MAIN);
    
    openDetails = new EFButton(canvasGraphics,
    getCanvasWidth() / 5 + 1,
    getCanvasHeight() / 6 + 3 + getCanvasHeight() / 6 + 3 + getCanvasHeight() / 2 + 3,
    "openDetails");
    openDetails.setCustomBorderXLength((int)((float)getCanvasWidth() / 2.5f));
    openDetails.setCustomBorderYLength(getCanvasHeight() - (getCanvasHeight() / 2 + 2) - 
          (getCanvasHeight() / 6 + 2) - (getCanvasHeight() / 6 + 2) - 5);
    graphicsEngine.loadButtonToDraw(openDetails, CurrentScreen.DEBUG);
    graphicsEngine.loadButtonToDraw(openDetails, CurrentScreen.IN_GAME_MAIN);
    
    currentRules = new EFButton(canvasGraphics,
    getCanvasWidth() / 5 + 2 + (int)((float)getCanvasWidth() / 2.5f) + 1,
    getCanvasHeight() / 6 + 2 + getCanvasHeight() / 6 + 3,
    "currentRules");
    currentRules.setCustomBorderXLength((int)((float)getCanvasWidth() / 2.5f) - 5);
    currentRules.setCustomBorderYLength((int)((float)getCanvasHeight() / 1.5) - 6);
    graphicsEngine.loadButtonToDraw(currentRules, CurrentScreen.DEBUG);
    graphicsEngine.loadButtonToDraw(currentRules, CurrentScreen.IN_GAME_MAIN);
    
    currentRulesImage = new EFScrollableRegion(getCanvasWidth() / 5 + 2 + (int)((float)getCanvasWidth() / 2.5f) + 1,
    getCanvasHeight() / 6 + 2 + getCanvasHeight() / 6 + 3,
          (int)((float)getCanvasWidth() / 2.5f) - 5,
          (int)((float)getCanvasHeight() / 1.5) - 6,
          ""
    );
    
    graphicsEngine.loadRegionToDraw(currentRulesImage, CurrentScreen.IN_GAME_MAIN);
   
    
    
    
    graphicsEngine.loadButtonToDraw(iniStatus, CurrentScreen.DEBUG);
    graphicsEngine.loadButtonToDraw(toMainFromDebug, CurrentScreen.DEBUG);
    
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="MAIN_MENU">
    
    EFButton.defaultDisplayType = DisplayType.CENTER;
    
    EFButton.defaultBackgroundType = BackgroundType.ICON;
    toDebugFromMain = new EFButton(canvasGraphics, canvas.getWidth() / 2,
          canvas.getHeight() / 2, "Debug");
    toDebugFromMain.setMainIcon(buttonParser.getValue("toDebugFromMain.mainIcon"));
    toDebugFromMain.setActivatedIcon(buttonParser.getValue("toDebugFromMain.activatedIcon"));
    toDebugFromMain.setCustomBorderXLength(100);
    toDebugFromMain.setCustomBorderYLength(100);
    graphicsEngine.loadButtonToDraw(toDebugFromMain, CurrentScreen.MAIN_SCREEN);
    toDebugFromMain.addNewPreset("test", new EFButton(canvasGraphics, 50, 50, "Hi"));
    toDebugFromMain.addNewPreset("Default", toDebugFromMain);
    
    EFButton.defaultBackgroundType = BackgroundType.OPAQUE;
    this.toTestLevelOneFromMain = new EFButton(canvasGraphics, canvas.getWidth() / 2,
    canvas.getHeight() / 2 - canvas.getHeight() / 6, "Test Level 1");
    graphicsEngine.loadButtonToDraw(toTestLevelOneFromMain, CurrentScreen.MAIN_SCREEN);
    
//</editor-fold>
    
    EFButton.defaultDisplayType = DisplayType.CENTER;
    EFButton.defaultBackgroundType = BackgroundType.TRANSPARENT;
    EFButton.defaultBorderType = BorderType.NONE;
    EFButton.defaultMainMessageColor = Color.red;
    loadingMessage = new EFButton(canvasGraphics, getCanvasWidth() / 2, getCanvasHeight() / 2, 
    "Now loading level one...");
    graphicsEngine.loadButtonToDraw(loadingMessage, CurrentScreen.LOADING_BEFORE_LEVEL);
    
    //<editor-fold defaultstate="collapsed" desc="DEBUG_VIEW_DETAILS">
    
    EFButton.defaultMainBackgroundColor = Color.pink;
    EFButton.defaultMainBorderColor = Color.green;
    EFButton.defaultActivatedBackgroundColor = Color.blue;
    EFButton.defaultMainBorderColor = Color.blue;
    EFButton.defaultActivatedBorderColor = Color.yellow;
    EFButton.defaultBorderType = BorderType.LINE;
    EFButton.defaultMainMessageColor = Color.black;
    EFButton.defaultBackgroundType = BackgroundType.OPAQUE;

    EFButton.defaultDisplayType = DisplayType.TOP_LEFT;

    objectImageDisplay = new EFScrollableRegion(getCanvasWidth() / 5 + 2, 
          getCanvasHeight() / 6 + 2, 
          (int)(717f * (float)getCanvasWidth() / 1000f) - 4, 
          5 * getCanvasHeight() / 6 - 3, 
          "Resources/ScrollableRegionTest.png");
    graphicsEngine.loadRegionToDraw(objectImageDisplay, CurrentScreen.DEBUG_VIEW_DETAILS);
    graphicsEngine.loadRegionToDraw(objectImageDisplay, CurrentScreen.IN_GAME_VIEW_DETAILS);
    
//</editor-fold>
    
    graphicsEngine.loadButtonToDraw(helpMessageButton, CurrentScreen.UNIVERSAL);
    this.regionToShift = null;
    this.regionXOffset = -1;
    this.regionYOffset = -1;
    
//</editor-fold>
    
    
    
    this.levelName = "";
    this.numberOfObjectsInCurrentLevel = 0;
    this.objectsInCurrentLevel = new ArrayList();
    numberOfShields = 3;
    currentlySelectedAttachment = 0;
    
    
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
      
      
      
      if(currentScreen == CurrentScreen.LOADING_BEFORE_LEVEL)
      {
        
        if(timeTracker.getAndFlipIsTriggered(this.LOADING_TIMER_TAG))
        {
          currentScreen = CurrentScreen.IN_GAME_MAIN;
        }
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
    
    if( regionToShift != null)
    {
      regionToShift.setRegionX(regionXOffset - (mouseX - regionToShift.getX()));
      regionToShift.setRegionY(regionYOffset - (mouseY - regionToShift.getY()));
    }
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
    
    regionToShift = null;
      regionXOffset = -1;
      regionYOffset = -1;
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
      else if( this.toTestLevelOneFromMain.isCursorOn(mouseX, mouseY))
      {
        timeTracker.getAndFlipNumberOfTriggers(this.LOADING_TIMER_TAG);
        loadLevel(1);
        currentScreen = CurrentScreen.LOADING_BEFORE_LEVEL;
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
      
      else if( objectListUpButton.isCursorOn(mouseX, mouseY))
      {
        if (currentlySelectedObject >= 0 && currentlySelectedObject < objectButtons.size())
        {
          objectButtons.get(currentlySelectedObject).loadPreset("Default");
        }
        currentlySelectedObject++;
        if (currentlySelectedObject >= 0 && currentlySelectedObject < objectButtons.size())
        {
          objectButtons.get(currentlySelectedObject).loadPreset("forWhenSelected");
        }
      }
      
      else if( objectListDownButton.isCursorOn(mouseX, mouseY))
      {
        if (currentlySelectedObject >= 0 && currentlySelectedObject < objectButtons.size())
        {
          objectButtons.get(currentlySelectedObject).loadPreset("Default");
        }
        currentlySelectedObject--;
        if (currentlySelectedObject >= 0 && currentlySelectedObject < objectButtons.size())
        {
          objectButtons.get(currentlySelectedObject).loadPreset("forWhenSelected");
        }
      }
      
      else
      {
        for(int i = 0; i < objectButtons.size(); i++)
        {
          if (objectButtons.get(i).isCursorOn(mouseX, mouseY))
          {
            if (currentlySelectedObject >= 0 && currentlySelectedObject < objectButtons.size())
            {
              objectButtons.get(currentlySelectedObject).loadPreset("Default");
            }
            currentlySelectedObject = i;
            objectButtons.get(i).loadPreset("forWhenSelected");
          }
        }
      }
    }
    
    else if(currentScreen == CurrentScreen.DEBUG_VIEW_DETAILS)
    {
      if( acceptButton.isCursorOn(mouseX, mouseY))
      {
        acceptButton.loadPreset("Default");
        denyButton.loadPreset("Default");
        currentScreen = CurrentScreen.DEBUG;
      }
    }
    
    
    else if(currentScreen == CurrentScreen.IN_GAME_MAIN)
    {
      if( acceptButton.isCursorOn(mouseX, mouseY) && currentlySelectedObject != -1)
      {
        checkAnswer(true);
      }
      else if(denyButton.isCursorOn(mouseX, mouseY) && currentlySelectedObject != -1)
      {
        checkAnswer(false);
      }
      else if (openDetails.isCursorOn(mouseX, mouseY) && currentlySelectedObject != -1)
      {
        acceptButton.loadPreset("forViewDetails");
        denyButton.loadPreset("forViewDetails");
        currentlySelectedAttachment = 0;

        for (int i = 0; i < this.numberOfObjects; i++)
        {
          this.objectButtons.get(currentlySelectedObject).shouldDraw = false;
          this.objectButtons.get(i).shouldCheckIsCursorOn = false;
        }

        for (int i = 0; i < this.objectsInCurrentLevel.get(currentlySelectedObject).attachments.size() && 
              i < this.numberOfObjects; i++)
        {
          this.objectButtons.get(i).setMessage(Integer.toString(i));
          this.objectButtons.get(i).shouldDraw = true;
          this.objectButtons.get(i).shouldCheckIsCursorOn = true;
        }
        
        this.objectImageDisplay.setImage(
              this.objectsInCurrentLevel.get(currentlySelectedObject).attachments.get(currentlySelectedAttachment));
        currentScreen = CurrentScreen.IN_GAME_VIEW_DETAILS;
      }
      
      
      
      
      else if( objectListUpButton.isCursorOn(mouseX, mouseY))
      {
        if (currentlySelectedObject >= 0 && currentlySelectedObject < this.numberOfObjectsInCurrentLevel - 1)
        {
          String message = objectButtons.get(currentlySelectedObject).getMessage();
          objectButtons.get(currentlySelectedObject).loadPreset("Default");
          objectButtons.get(currentlySelectedObject).setMessage(message);
          currentlySelectedObject++;
        }
        if (currentlySelectedObject >= 0 && currentlySelectedObject < this.numberOfObjectsInCurrentLevel)
        {
          String message = objectButtons.get(currentlySelectedObject).getMessage();
          objectButtons.get(currentlySelectedObject).loadPreset("forWhenSelected");
          objectButtons.get(currentlySelectedObject).setMessage(message);

          this.objectAtAGlanceImage.setImage(this.objectsInCurrentLevel.get(currentlySelectedObject).atAGlance);
        }
      }
      
      else if( objectListDownButton.isCursorOn(mouseX, mouseY))
      {
        if (currentlySelectedObject > 0 && currentlySelectedObject <= this.numberOfObjectsInCurrentLevel)
        {
          String message = objectButtons.get(currentlySelectedObject).getMessage();
          objectButtons.get(currentlySelectedObject).loadPreset("Default");
          objectButtons.get(currentlySelectedObject).setMessage(message);
          currentlySelectedObject--;
        }
        if (currentlySelectedObject > 0 && currentlySelectedObject <= this.numberOfObjectsInCurrentLevel)
        {
          String message = objectButtons.get(currentlySelectedObject).getMessage();
          objectButtons.get(currentlySelectedObject).loadPreset("forWhenSelected");
          objectButtons.get(currentlySelectedObject).setMessage(message);
          this.objectAtAGlanceImage.setImage(this.objectsInCurrentLevel.get(currentlySelectedObject).atAGlance);
        }
      }
      
      else
      {
        for(int i = 0; i < objectButtons.size(); i++)
        {
          if (objectButtons.get(i).isCursorOn(mouseX, mouseY))
          {
            if (currentlySelectedObject >= 0 && currentlySelectedObject < objectButtons.size())
            {
              String message = objectButtons.get(currentlySelectedObject).getMessage();
              objectButtons.get(currentlySelectedObject).loadPreset("Default");
              objectButtons.get(currentlySelectedObject).setMessage(message);
            }
            currentlySelectedObject = i;
              String message = objectButtons.get(currentlySelectedObject).getMessage();
            objectButtons.get(i).loadPreset("forWhenSelected");
              objectButtons.get(currentlySelectedObject).setMessage(message);
              this.objectAtAGlanceImage.setImage(this.objectsInCurrentLevel.get(currentlySelectedObject).atAGlance);
              this.currentRulesImage.setImage(this.objectsInCurrentLevel.get(currentlySelectedObject).currentRules);
          }
        }
      }
    }
    
    else if (currentScreen == CurrentScreen.IN_GAME_VIEW_DETAILS)
    {
      if (acceptButton.isCursorOn(mouseX, mouseY) && currentlySelectedObject != -1)
      {
        checkAnswer(true);

        for (int i = 0; i < this.numberOfObjects; i++)
        {
          this.objectButtons.get(i).shouldDraw = false;
          this.objectButtons.get(i).shouldCheckIsCursorOn = false;
        }

        for (int i = 0; i < this.numberOfObjectsInCurrentLevel && i < this.numberOfObjects; i++)
        {
          this.objectButtons.get(i).setMessage(objectsInCurrentLevel.get(i).name);
          this.objectButtons.get(i).shouldDraw = true;
          this.objectButtons.get(i).shouldCheckIsCursorOn = true;
        }

        acceptButton.loadPreset("Default");
        denyButton.loadPreset("Default");
        currentScreen = CurrentScreen.IN_GAME_MAIN;
      } else if (denyButton.isCursorOn(mouseX, mouseY) && currentlySelectedObject != -1)
      {
        checkAnswer(false);

        for (int i = 0; i < this.numberOfObjects; i++)
        {
          this.objectButtons.get(i).shouldDraw = false;
          this.objectButtons.get(i).shouldCheckIsCursorOn = false;
        }

        for (int i = 0; i < this.numberOfObjectsInCurrentLevel && i < this.numberOfObjects; i++)
        {
          this.objectButtons.get(i).setMessage(objectsInCurrentLevel.get(i).name);
          this.objectButtons.get(i).shouldDraw = true;
          this.objectButtons.get(i).shouldCheckIsCursorOn = true;
        }

        acceptButton.loadPreset("Default");
        denyButton.loadPreset("Default");

        currentScreen = CurrentScreen.IN_GAME_MAIN;
      }
      else if( objectListUpButton.isCursorOn(mouseX, mouseY))
      {
        if (currentlySelectedAttachment >= 0 && currentlySelectedAttachment < 
              this.objectsInCurrentLevel.get(currentlySelectedObject).attachments.size() - 1)
        {
          String message = objectButtons.get(currentlySelectedAttachment).getMessage();
          objectButtons.get(currentlySelectedAttachment).loadPreset("Default");
          objectButtons.get(currentlySelectedAttachment).setMessage(message);
          currentlySelectedAttachment++;
        }
        if (currentlySelectedAttachment >= 0 && currentlySelectedAttachment < 
              this.objectsInCurrentLevel.get(currentlySelectedObject).attachments.size())
        {
          String message = objectButtons.get(currentlySelectedAttachment).getMessage();
          objectButtons.get(currentlySelectedAttachment).loadPreset("forWhenSelected");
          objectButtons.get(currentlySelectedAttachment).setMessage(message);

          this.objectImageDisplay.setImage(this.objectsInCurrentLevel.get(currentlySelectedObject).attachments.get(currentlySelectedAttachment));
        }
      }
      
      else if( objectListDownButton.isCursorOn(mouseX, mouseY))
      {
        if (currentlySelectedAttachment > 0 && currentlySelectedAttachment <= 
              this.objectsInCurrentLevel.get(currentlySelectedObject).attachments.size())
        {
          String message = objectButtons.get(currentlySelectedAttachment).getMessage();
          objectButtons.get(currentlySelectedAttachment).loadPreset("Default");
          objectButtons.get(currentlySelectedAttachment).setMessage(message);
          currentlySelectedAttachment--;
        }
        if (currentlySelectedAttachment > 0 && currentlySelectedAttachment <= 
              this.objectsInCurrentLevel.get(currentlySelectedObject).attachments.size())
        {
          String message = objectButtons.get(currentlySelectedAttachment).getMessage();
          objectButtons.get(currentlySelectedAttachment).loadPreset("forWhenSelected");
          objectButtons.get(currentlySelectedAttachment).setMessage(message);
          this.objectImageDisplay.setImage(this.objectsInCurrentLevel.get(currentlySelectedObject).attachments.get(currentlySelectedAttachment));
        }
      }
      
      else
      {
        for(int i = 0; i < objectButtons.size(); i++)
        {
          if (objectButtons.get(i).isCursorOn(mouseX, mouseY))
          {
            if (currentlySelectedAttachment >= 0 && currentlySelectedAttachment < objectButtons.size())
            {
              String message = objectButtons.get(currentlySelectedAttachment).getMessage();
              objectButtons.get(currentlySelectedAttachment).loadPreset("Default");
              objectButtons.get(currentlySelectedAttachment).setMessage(message);
            }
            currentlySelectedAttachment = i;
              String message = objectButtons.get(currentlySelectedAttachment).getMessage();
            objectButtons.get(i).loadPreset("forWhenSelected");
              objectButtons.get(currentlySelectedAttachment).setMessage(message);
          this.objectImageDisplay.setImage(this.objectsInCurrentLevel.get(currentlySelectedObject).attachments.get(currentlySelectedAttachment));
          }
        }
      }
    }
    
    else if(currentScreen == CurrentScreen.DEBUG_VIEW_DETAILS)
    {
      if( acceptButton.isCursorOn(mouseX, mouseY))
      {
        acceptButton.loadPreset("Default");
        denyButton.loadPreset("Default");
        currentScreen = CurrentScreen.DEBUG;
      }
    }

    ArrayList<CurrentScreen> regionContexts = graphicsEngine.getRegionsScreenContext();
    ArrayList<EFScrollableRegion> regions = graphicsEngine.getRegionsToDraw();
    for (int i = 0; i < graphicsEngine.getRegionsScreenContext().size(); i++)
    {

      if (regionContexts.get(i) == currentScreen)
      {
        if (regions.get(i).isMouseOn(mouseX, mouseY) && regionToShift == null)
        {
          regionToShift = regions.get(i);
          regionXOffset = mouseX - regionToShift.getX() + regionToShift.getRegionX();
          regionYOffset = mouseY - regionToShift.getY() + regionToShift.getRegionY();
          System.out.println(regionXOffset);
          System.out.println(regionYOffset);
        }
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
  
  
  
  private void loadLevel(int level)
  {
    this.objectsInCurrentLevel.clear();
    
    EFIniParser levelInformation = new EFIniParser("Resources/Levels/" + level + "/Properties.ini");
    levelName = levelInformation.getValue("level.name");
    numberOfObjectsInCurrentLevel = Integer.parseInt(levelInformation.getValue("level.numberOfObjects"));
    
    for(int i = 0; i < numberOfObjectsInCurrentLevel; i++)
    {
      objectsInCurrentLevel.add(new EFObject(
            i, 
            levelInformation.getValue("object." + i + ".name"), 
            Integer.parseInt(levelInformation.getValue("object." + i + ".numberOfAttachedObjects")), 
            "Resources/Levels/" + level + "/", 
            levelInformation.getValue("object." + i + ".successCondition").equals("approve"), 
            levelInformation.getValue("object." + i + ".failureReason"), 
            Integer.parseInt(levelInformation.getValue("object." + i + ".shieldPenalty")), 
            Integer.parseInt(levelInformation.getValue("object." + i + ".successReward")),
            levelInformation
            ));
    }
    
    for(int i = 0; i < this.numberOfObjects; i++ )
    {
      this.objectButtons.get(i).shouldDraw = false;
      this.objectButtons.get(i).shouldCheckIsCursorOn = false;
    }
    
    for(int i = 0; i < this.numberOfObjectsInCurrentLevel && i < this.numberOfObjects; i++)
    {
      this.objectButtons.get(i).setMessage(objectsInCurrentLevel.get(i).name);
      this.objectButtons.get(i).shouldDraw = true;
      this.objectButtons.get(i).shouldCheckIsCursorOn = true;
    }
    
  }
  
  
  
  public void checkAnswer(boolean answer)
  {
    if(this.objectsInCurrentLevel.get(currentlySelectedObject).successCondition == answer)
    {
      System.out.println("YouWin!");
      this.numberOfShields += this.objectsInCurrentLevel.get(currentlySelectedObject).successReward;
    }
    else
    {
      System.out.println("YouLose!");
      this.numberOfShields -= this.objectsInCurrentLevel.get(currentlySelectedObject).shieldPenalty;
    }
    this.shieldsRemainingTag.setMessage(Integer.toString(numberOfShields));
    if(numberOfShields <= 0)
    {
      currentScreen = CurrentScreen.MAIN_SCREEN;
      numberOfShields = 3;
    }
  }

}







