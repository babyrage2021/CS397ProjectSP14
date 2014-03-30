package EFSpriteAnimation;

import java.io.*;
import java.io.File;
import java.io.FilenameFilter;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import EFTimeTracker.*;
import EFResolution.*;







//*EFTimeTracker Program*\\
//*PURPOSE:*\\
//*) Will grab all .png's in a given directory and store them in an array
//*) Using the EFTimeTracker class, a current frame is selected and can be getted

//*HOW TO USE:*\\
//*1) Create an EFTimeTracker

//*GOALS:*\\
//*) make function to change eventname
//*) make function to change firerateps
public class EFSpriteAnimation 
{
  private String directory;
  private BufferedImage[] spriteArray;
  private int numberOfFrames;
  private int frameCounter;
  private String targetExtension;
  private EFTimeTracker timeTracker;
  private String timeTrackerName;
  private long frameLength;
  private EFSpriteFrameChecker frameChecker;
  private long[] specificFrameLengths;
  private EFResolution resolution;
  private float meterLengthInPixels;
  private double widthInMeters;
  private double heightInMeters;
  private boolean isUsingManualMetrics;
  
  //private final Object lock = new Object();
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  public EFSpriteAnimation( String directory )
  {
    this.directory = directory;
    frameCounter = 0;
    targetExtension = ".png";
    
    spriteArray = finder(directory);
    numberOfFrames = spriteArray.length;
    
    timeTrackerName = "";
    frameLength = 1;
    timeTracker = null;
    resolution = new EFResolution();
    meterLengthInPixels = 1;
    widthInMeters = getCurrentFrame().getWidth();
    heightInMeters = getCurrentFrame().getHeight();
    isUsingManualMetrics = false;
  }
  
  
  
  
  
  
  
  public EFSpriteAnimation( String directory, EFTimeTracker timeTracker, long frameLength )
  {
    this.directory = directory;
    frameCounter = 0;
    targetExtension = ".png";
    
    spriteArray = finder(directory);
    numberOfFrames = spriteArray.length;
    
    meterLengthInPixels = 1;
    resolution = new EFResolution();
    widthInMeters = getCurrentFrame().getWidth() / meterLengthInPixels;
    heightInMeters = getCurrentFrame().getHeight() / meterLengthInPixels;
    isUsingManualMetrics = false;
    
    this.timeTracker = timeTracker;
    this.frameLength = frameLength;
    registerAnimation();
    getProperties();
    
    
    //frameChecker = new EFSpriteFrameChecker( timeTrackerName, timeTracker, timeTrackerName, this );
  }
  
  
  
  
  
  
  
  public EFSpriteAnimation( String directory, EFTimeTracker timeTracker, long frameLength, EFResolution resolution )
  {
    this.directory = directory;
    frameCounter = 0;
    targetExtension = ".png";
    
    spriteArray = finder(directory);
    numberOfFrames = spriteArray.length;
    
    this.meterLengthInPixels = 1;
    this.resolution = resolution;
    widthInMeters = getCurrentFrame().getWidth() / resolution.realXToPixelX;
    heightInMeters = getCurrentFrame().getHeight() / resolution.realYToPixelY;
    isUsingManualMetrics = false;
    
    this.timeTracker = timeTracker;
    this.frameLength = frameLength;
    registerAnimation();
    getProperties();
    
    //frameChecker = new EFSpriteFrameChecker( timeTrackerName, timeTracker, timeTrackerName, this );
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  public synchronized BufferedImage getCurrentFrame()
  {
    return spriteArray[frameCounter];
  }
  
  
  
  
  
  
  
  public synchronized void advanceFrame( int number )
  {
    for( int i = 0; i < number; i++ )
    {
      advanceFrame();
    }
  }
  
  
  
  
  
  
  
  public synchronized void advanceFrame()
  {
    frameCounter++;
    if( frameCounter > numberOfFrames - 1 ) frameCounter = 0;
    frameLength = specificFrameLengths[frameCounter];
    
    if (!isUsingManualMetrics)
    {
      widthInMeters = getCurrentFrame().getWidth() / resolution.realXToPixelX;
      heightInMeters = getCurrentFrame().getHeight() / resolution.realYToPixelY;
    }
  }


  
  
  
  
  
  public void drawFrame( Graphics2D g, int x, int y )
  {
    if( timeTracker.getIsTriggered(timeTrackerName) )
      {
        int frameAdvances = timeTracker.getAndFlipNumberOfTriggers(timeTrackerName);
        for( int i = 0; i < frameAdvances; i++ )
        {
          advanceFrame();
        }
        
        timeTracker.setFrameLength( timeTrackerName, getCurrentFrameLength() );
      }
    
    BufferedImage currentFrame = getCurrentFrame();
    int xScale = (int)( widthInMeters * resolution.realXToPixelX );
    int yScale = (int)( heightInMeters * resolution.realYToPixelY );
    
    g.drawImage( currentFrame, x, y, x + xScale, y + yScale, 0, 0, currentFrame.getWidth(), 
                currentFrame.getHeight(), null );
  }
  
  
  
  
  
  
  
  public synchronized long getCurrentFrameLength()
  {
    return specificFrameLengths[frameCounter];
  }
  
  
  
  
  
  
  
  public String getName()
  {
    return timeTrackerName;
  }
  
  
  
  
  
  
  
  public double getWidthInMeters()
  {
    return widthInMeters;
  }
  
  
  
  
  
  
  
  public double getHeightInMeters()
  {
    return heightInMeters;
  }
  
  
  
  
  
  
  
  public int getWidthInPixels()
  {
    return (int)( widthInMeters * resolution.realXToPixelX );
  }
  
  
  
  
  
  
  
  public int getHeightInPixels()
  {
    return (int)( heightInMeters * resolution.realYToPixelY );
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  private BufferedImage[] finder( String dirName )
  {
     File dir = new File(dirName);
     File[] fileArray = null;
     BufferedImage[] imageArray = null;

     fileArray = dir.listFiles(new FilenameFilter() { 
              public boolean accept(File dir, String filename)
                   { return filename.endsWith(targetExtension); }
     } );
     
     imageArray = new BufferedImage[fileArray.length];
     
     for( int i = 0; i < fileArray.length; i++ )
     {
       try {
         imageArray[i] = ImageIO.read(fileArray[i]);
       } catch (IOException e) {
       }
     }
     return imageArray;
  }
  
  
  
  
  
  
  
  private void registerAnimation()
  {
    if( timeTrackerName != "" )
    {
      timeTracker.removeEFTimeEvent(timeTrackerName);
    }
    
    int animationCounter = 0;
    
    while( timeTracker.isNameRegistered("_$EF_SPRITE_ANIMATION_#" + animationCounter ) )
    {
      animationCounter++;
    }
    timeTracker.addEFTimeEvent( "_$EF_SPRITE_ANIMATION_#" + animationCounter, frameLength );
    timeTrackerName = "_$EF_SPRITE_ANIMATION_#" + animationCounter;
    
  }
  
  
  
  
  
  
  
  private void registerAnimation( String animationName )
  {
    if( timeTrackerName != "" )
    {
      timeTracker.removeEFTimeEvent(timeTrackerName);
    }
    
    if( !timeTracker.isNameRegistered(animationName) )
    {
      timeTracker.addEFTimeEvent( animationName, frameLength );
      timeTrackerName = animationName;
    }
    else
    {
      
      int animationCounter = 1;
      
      while( timeTracker.isNameRegistered(animationName + animationCounter ) )
      {
        animationCounter++;
      }
      
      timeTracker.addEFTimeEvent( animationName + animationCounter, frameLength );
      timeTrackerName = animationName + animationCounter;
    }
    //frameChecker = new EFSpriteFrameChecker( timeTrackerName, timeTracker, timeTrackerName, this );
    
  }
  
  
  
  
  
  
  
  private void getProperties()
  {
    specificFrameLengths = new long[numberOfFrames];
    
    for( int i = 0; i < numberOfFrames; i++ )
    {
      specificFrameLengths[i] = frameLength;
    }
    
    BufferedReader br = null;
    int lineNumber = 1;
    
    try
    {
      
      br = new BufferedReader(new FileReader( directory + "properties.txt" ));
      String line;
      String[] framePairs;
      
      while ((line = br.readLine()) != null) {
        framePairs = line.split(";");
        
        
        if( framePairs[0].length() >= 2 && framePairs[0].substring( 0, 2 ).equals("//") )
        { }
        else if( framePairs.length != 2 )
        {
          System.out.println("\t_$EF_!ERROR: Error in properties file for animation \"" + timeTrackerName + 
                             "\". \n\t--Error at line " + lineNumber + ". Syntax must be in the form \"keyword" +
                             ";input\"." );
        }
        else
        {
          
          if( framePairs[0].equals("Name") )
          {
            registerAnimation(framePairs[1]);
          }
          else if( framePairs[0].equals("Width") )
          {
            widthInMeters = Float.parseFloat(framePairs[1]);
            isUsingManualMetrics = true;
          }
          else if( framePairs[0].equals("Height") )
          {
            heightInMeters = Float.parseFloat(framePairs[1]);
            isUsingManualMetrics = true;
          }
          else if( isInteger(framePairs[0]) )
          {
            registerSpecificFrameLength( Integer.parseInt(framePairs[0]), Long.parseLong(framePairs[1]) );
          }
          else
          {
            System.out.println("\t_$EF_!ERROR: Error in properties file for animation \"" + timeTrackerName + 
                               "\". \n\t--Error at line " + lineNumber + " with syntax \"" + framePairs [0] + ";" + 
                               framePairs[1] + "\"." );
          }
        }
        lineNumber++;
        
      }
      
      
    }
    catch ( Exception e ) 
    {
      //
    }
    try
    {
      br.close();
    } catch ( Exception e ) {}
    
  }

  
  
  
  
  
  
  
  private synchronized void registerSpecificFrameLength( int index, long frameLength )
  {
    if( index >= 0 && index < numberOfFrames )
    {
      specificFrameLengths[index] = frameLength;
      
      if( index == frameCounter )
      {
        timeTracker.setFrameLength( timeTrackerName, frameLength );
      }
    }
  }
  
  
  
  
  
  
  
  public boolean isInteger( String input )  
  {  
    try  
    {  
      Integer.parseInt( input );  
      return true;  
    }  
    catch( Exception e )  
    {  
      return false;  
    }  
  }
  
  
  
  
  
  
  
  private void frameCounterCheck()
  {
    if( frameCounter > numberOfFrames ) frameCounter = 0;
  }  
}