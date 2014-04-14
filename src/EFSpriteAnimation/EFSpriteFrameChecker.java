package EFSpriteAnimation;

import java.io.*;
import java.io.File;
import java.io.FilenameFilter;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import EFTimeTracker.*;







//*EFTimeTracker Program*\\
//*PURPOSE:*\\
//*) Will grab all .png's in a given directory and store them in an array
//*) Using the EFTimeTracker class, a current frame is selected and can be getted

//*HOW TO USE:*\\
//*1) Create an EFTimeTracker

//*GOALS:*\\
//*) make function to change eventname
//*) make function to change firerateps
class EFSpriteFrameChecker implements Runnable
{
  private String threadName;
  private EFTimeTracker timeTracker;
  private String timeTrackerName;
  private EFSpriteAnimation spriteAnimation;
  private Thread thread;
  private boolean keepChecking;
  
  
  
  
  
  
  
  public EFSpriteFrameChecker( String threadName, EFTimeTracker timeTracker, String timeTrackerName, 
                              EFSpriteAnimation spriteAnimation )
  {
    this.threadName = threadName;
    this.timeTracker = timeTracker;
    this.timeTrackerName = timeTrackerName;
    this.spriteAnimation = spriteAnimation;
    keepChecking = true;
    
    thread = new Thread( this, threadName );
    thread.start(); // (2) Start the thread.
  
 }







  public void run() 
  {
    int counter = 0;
    while( keepChecking )
    {
      if( timeTracker.getIsTriggered(timeTrackerName) )
      {
        int frameAdvances = timeTracker.getAndFlipNumberOfTriggers(timeTrackerName);
        for( int i = 0; i < frameAdvances; i++ )
        {
          spriteAnimation.advanceFrame();
        }
        
        timeTracker.setFrameLength( timeTrackerName, spriteAnimation.getCurrentFrameLength() );
      }
      //System.out.println(counter++);
      try
      {
        this.wait(100);
      } catch ( Exception e ) {}
    }
  }
  
}