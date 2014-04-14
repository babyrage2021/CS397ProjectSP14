package EFTimeTracker;

import java.util.*;
import java.io.*;


//*EFTimeTracker Program*\\
//*PURPOSE:*\\
//*1) Provide a functionality that can not only calculate a suggested sleep time based on a goal frame time,
//     but can also track many timed events and flip a boolean when their sleep time has elasped.
//*2) Be effecient as possible to minimize interface time and unreported lag
//*3) Be able to interface with Thread classes

//*HOW TO USE:*\\
//*1) Create an EFTimeTracker

//*GOALS:*\\
//*) Rework blacklist to a makeTimeFor (tentaive name) list which will contain a list of processes that a timer
//    should make time for (adm; don't remove blacklist, just add new functionality)
public class EFTimeTracker 
{
  private Hashtable<String,EFTimeEvent> timeEvents; //*)Holds all time events
  private Vector<String> eventNames; //*) Holds the names of every event in timeEvents
  private Vector<String> blackList; //*) List containing the name of events who's FPS-s are subject to the average lag
  private Vector<String> whiteList;  //*) List containing the name of events who's FPS-s are subject to the average spare
  private Vector<String> invalidNames;
                                     // time
  
  private final String averageLag = "_$EF_AVERAGE_LAG"; //*) Used for lag tracking
  private double lagCount; //*) Value which holds how much the master event is lagging
  
  private final String masterEvent = "_$EF_MASTER_EVENT"; //*) Used for masterEvent tracking
  private double masterFrameLength; //*) Controls the masterFPS
  private double nextExpectedTime;
  
  private final String averageSpareTime = "_$EF_SPARE_TIME"; //Used for whiteList calculations /*\
  private double totalSpareTime;                                                            //    |
  private double spareTimeNeeded;                                                           //    |   
  private EFTimeEvent targetWhiteListEvent;                                               //    |
  private int whiteListCounter;                                                           //   \./
  
  private final int NO_TARGET_WHITELIST_EVENT = -1; //Asssigned to spareTimeNeeded when there are no whitelisted events
  
  private static float defaultMasterFrameLength = 16;
  
  public double _calcedTime;
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  public EFTimeTracker()
  {
    timeEvents = new Hashtable<String,EFTimeEvent>();
    eventNames = new Vector<String>();
    blackList = new Vector<String>();
    whiteList = new Vector<String>();
    invalidNames = new Vector<String>();
    
    invalidNames.add(averageLag);
    invalidNames.add(masterEvent);
    invalidNames.add(averageSpareTime);
    
    lagCount = 0;
    
    masterFrameLength = defaultMasterFrameLength;
    totalSpareTime = 0;
    spareTimeNeeded = 0;
    whiteListCounter = NO_TARGET_WHITELIST_EVENT;
    
    addEFTimeEvent( averageLag );
    addEFTimeEvent( masterEvent, masterFrameLength );
    
    addEFTimeEvent( averageSpareTime );
    
    _calcedTime = System.currentTimeMillis();
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
//Pre:
  //  eventName - any String != null
  //  eventFPS - any FPS > 0
  //
  //Post:
  //  Adds a new EFTimeEvent to timeEvents or does nothing at all if that event name is already registered
  public void addEFTimeEvent( String eventName )
  {
    
    if( !eventNames.contains(eventName) )
    {
      timeEvents.put( eventName, new EFTimeEvent( eventName ) );
      eventNames.add( eventName );
    }
  }
  
  
  
  
  
  
  
  //Pre:
  //  eventName - any String != null
  //  eventFPS - any FPS > 0
  //
  //Post:
  //  Adds a new EFTimeEvent to timeEvents or does nothing at all if that event name is already registered
  public void addEFTimeEvent( String eventName, double eventFrameLength )
  {
    
    if( !eventNames.contains(eventName) )
    {
      timeEvents.put( eventName, new EFTimeEvent( eventName, eventFrameLength ) );
      eventNames.add( eventName );
    }
  }
  
  
  
  
  
  
  
  //Pre:
  //  eventName - any String != null
  //
  //Post:
  //  removes that event from timeEvents if it exists. If not, it does nothing
  public void removeEFTimeEvent( String eventName )
  {
    
    if( eventNames.contains(eventName) && !invalidNames.contains(eventName) )
    {
      timeEvents.remove( eventName );
      eventNames.remove( eventName );
      whiteList.remove( eventName );
      blackList.remove( eventName );
    }
  }
  
  
  
  
  
  
  
  //Pre:
  //  eventName - any String != null
  //
  //Post:
  //  if the event exists in timeEvents, return whether that event is triggered or not; otherwise, return false
  public boolean getIsTriggered( String eventName )
  {
    
    if( eventNames.contains(eventName) )
    {
      
      EFTimeEvent tempTimeEvent = timeEvents.get( eventName );
      
      return tempTimeEvent.getIsTriggered();
    }
    else
    {
      return false;
    }
  }
  
  
  
  
  
  
  
  //Pre:
  //  eventName - any String != 0
  //
  //Post:
  //  If the event exists in timeEvents, return if that event is triggered or not and flip the event off if it is.
  //   otherwise, return false
  public boolean getAndFlipIsTriggered( String eventName )
  {
    
    if( eventNames.contains(eventName) )
    {
      
      EFTimeEvent tempTimeEvent = timeEvents.get( eventName );
      
      return tempTimeEvent.getAndFlipIsTriggered();
    }
    else
    {
      return false;
    }
  }
  
  
  
  
  
  
  
  //Pre:
  //  eventName - any String != null
  //
  //Post:
  //  if the event exists in timeEvents, return whether that event is triggered or not; otherwise, return false
  public int getNumberOfTriggers( String eventName )
  {
    
    if( eventNames.contains(eventName) )
    {
      
      EFTimeEvent tempTimeEvent = timeEvents.get( eventName );
      
      return tempTimeEvent.getNumberOfTriggers();
    }
    else
    {
      return 0;
    }
  }
  
  
  
  
  
  
  
  //Pre:
  //  eventName - any String != 0
  //
  //Post:
  //  If the event exists in timeEvents, return if that event is triggered or not and flip the event off if it is.
  //   otherwise, return false
  public int getAndFlipNumberOfTriggers( String eventName )
  {
    
    if( eventNames.contains(eventName) )
    {
      
      EFTimeEvent tempTimeEvent = timeEvents.get( eventName );
      
      return tempTimeEvent.getAndFlipNumberOfTriggers();
    }
    else
    {
      return 0;
    }
  }
  
  
  
  
  
  
  
  //Pre:
  // eventName - any String != null
  // eventFPS > 0
  //
  //Post:
  // changes the FPS of the specified event to eventFPS. If the event does not exist, do nothing
  public void setEventFrameLength( String eventName, double eventFrameLength )
  {
    
    if( eventNames.contains(eventName) )
    {
      
      EFTimeEvent tempTimeEvent = timeEvents.get( eventName );
      timeEvents.remove( eventName );
      
      tempTimeEvent.setFrameLength( eventFrameLength );
      timeEvents.put( eventName, tempTimeEvent );
    }
  }
  
  
  
  
  
  
  
  //Pre:
  // eventName - any String != null
  // newName - any String != null that does not contain the name of an event already registered
  //
  //Post:
  // If the event exists, it's name is changed and it is re-registered in timeEvents, eventNames, and blackList
  //  and/or whitelist if it was registered there as well
  public void setEventName( String eventName, String newName )
  {
    if( eventNames.contains(newName) ) return;
    
    if( eventNames.contains(eventName) && ( !invalidNames.contains( eventName ) && !invalidNames.contains( newName ) ) )
    {
      { //Controls interactions with timeEvents
        EFTimeEvent tempTimeEvent = timeEvents.get( eventName );
        timeEvents.remove(eventName);
        
        tempTimeEvent.setEventName(newName);
        timeEvents.put( newName, tempTimeEvent );
      }
      
      { //Controls interactions with special lists
        eventNames.remove(eventName);
        eventNames.add(newName);
        
        if( whiteList.contains(eventName) )
        {
          whiteList.remove(eventName);
          whiteList.add(newName);
        }
        
        if( blackList.contains(eventName) )
        {
          blackList.remove(eventName);
          blackList.add(newName);
        }
      }
    }
  }
  
  
  
  
  
  
  
  //Pre:
  // eventName - any String != null
  //
  //Post:
  // returns the averageRuntime for the specified event or 0 if it does not exist
  public double getAverageRuntime( String eventName )
  {
    
    double averageRuntime = 0;
    if( eventNames.contains(eventName) )
    {
      EFTimeEvent tempTimeEvent = timeEvents.get(eventName);
      timeEvents.remove(eventName);
      
      averageRuntime = tempTimeEvent.getAverageRuntime();
      timeEvents.put( eventName, tempTimeEvent );
    }
    else
    {
      System.out.println("PROBLEMS");
    }
    
    return averageRuntime;
  }
  
  
  
  
  
  
  
  //Pre:
  // eventName - any String != null
  //
  //Post:
  // gets the last runtime of the specified event or nothing if it does not exist
  public double getLastRuntime( String eventName )
  {
    double lastRuntime = 0;
    
    EFTimeEvent tempTimeEvent = timeEvents.get(eventName);
    timeEvents.remove(eventName);
    
    lastRuntime = tempTimeEvent.getLastRuntime();
    timeEvents.put( eventName, tempTimeEvent );
    
    
    return lastRuntime;
  }
  
  
  
  
  
  
  
  //Pre:
  // eventName - any String != null
  //
  //Post:
  // adds the specified event to the white list if it exists
  public void addTimerToWhiteList( String eventName )
  {
    if( eventNames.contains(eventName) && !invalidNames.contains( eventName ) ) 
    {
      whiteList.add(eventName);
      
      if( whiteList.size() == 1 ) getNextWhiteListedEvent();
    }
  }
  
  
  
  
  
  
  
  //Pre:
  // eventName - any String != null
  //
  //Post:
  // adds the specified event to the white list if it exists
  public void addTimerToBlackList( String eventName )
  {
    if( eventNames.contains(eventName) && !invalidNames.contains( eventName ) ) blackList.add(eventName);
  }
  
  
  
  
  
  
  
  //Pre:
  // eventName - any String != 0
  //
  //Post:
  // returns the FPS of the specified event or 0 if it does not exist
  public double getFrameLength( String eventName )
  {
    
    if( eventNames.contains(eventName) )
    {
      EFTimeEvent tempTimeEvent = timeEvents.get(eventName);
      
      return tempTimeEvent.getFrameLength();
    } else { return 0; }
  }
  
  
  
  
  
  
  
  public void setFrameLength( String eventName, long frameLength )
  {
    
    if( eventNames.contains(eventName) )
    {
      EFTimeEvent tempTimeEvent = timeEvents.get(eventName);
      
      tempTimeEvent.setFrameLength(frameLength);
    }
  }
  
  
  
  
  
  
  //Pre:
  //  fPS - any int
  //
  //Post:
  //  sets the Master FPS to fPS
  public void setMasterFrameLength( long masterFrameLength )
  {
    if( masterFrameLength <= 0 ) masterFrameLength = 1;
    
    EFTimeEvent tempTimeEvent = timeEvents.get(masterEvent);
    
    tempTimeEvent.setFrameLength(masterFrameLength);
    //tempTimeEvent.clearRuntimeSamples();
    
    this.masterFrameLength = masterFrameLength;
  }
  
  
  
  
  
  
  
  //Pre:
  //  fPS - any int
  //
  //Post:
  //  sets the Master FPS to fPS
  public double getTimeSinceLastTrigger( String eventName )
  {
    if( eventNames.contains(eventName) )
    {
      EFTimeEvent tempTimeEvent = timeEvents.get(eventName);
      
      return tempTimeEvent.getTimeSinceLastTrigger();
    }
    return (-1);
  }







//Pre:
  // None
  //
  //Post:
  // Calls sleepThread(int,long) with the fps and runtime from the masterEvent and returns the lag from
  //  sleepThread(int,long)
  public double sleepThread()
  {
    double difference = nextExpectedTime - System.currentTimeMillis();
    
    if( Math.abs(difference) > 1000 ) 
    {
      nextExpectedTime = System.currentTimeMillis() - 1;
      difference = -1;
    }
    
    if( difference > 0 )
    {
      try
      {
        Thread.sleep((long)difference);
      } catch ( Exception e ) {}
      getNextWhiteListedEvent();
    } 
    addFrameLengthToAllEvents();
    nextExpectedTime += masterFrameLength;
    return difference;
  } 







//Pre:
  // None
  //
  //Post:
  // Calls sleepThread(int,long) with the fps and runtime from the masterEvent and returns the lag from
  //  sleepThread(int,long)
  public double _sleepThread()
  {
    EFTimeEvent tempTimeEvent = null;
    double difference = nextExpectedTime - System.currentTimeMillis();
    
    if( Math.abs(difference) > 1000 ) 
    {
      nextExpectedTime = System.currentTimeMillis() - 1;
      difference = -1;
    }
    
    if( difference > 0 )
    {
      try
      {
        Thread.sleep((long)difference);
      } catch ( Exception e ) {}
      getNextWhiteListedEvent();
      
      tempTimeEvent = timeEvents.get(averageSpareTime);
      tempTimeEvent.feedInitialTime(0);
      tempTimeEvent.feedFinalTime(difference);
    } else {
      tempTimeEvent = timeEvents.get(averageLag);
      tempTimeEvent.feedInitialTime(0);
      tempTimeEvent.feedFinalTime(Math.abs(difference));
    }
      
    addFrameLengthToAllEvents();
    nextExpectedTime += masterFrameLength;
    return difference;
  } 







//Pre:
  //  fPS > 0
  //  
  //Post:
  //  adds 1000/fPS ms-s to all events in timeEvents
  public void addFrameLengthToAllEvents( double frameLength )
  {
    if( frameLength <= 0 ) frameLength = 1;
    double timeToAdd = frameLength;
    EFTimeEvent tempTimeEvent = null;
    
    for( int i = 0; i < eventNames.size(); i++ )
    {
      tempTimeEvent = timeEvents.get( eventNames.get(i) );
      
      if( !blackList.contains(tempTimeEvent.getEventName()) ) tempTimeEvent.addTimeSinceLastTrigger(timeToAdd);
    }
  }
  
  
  
  
  
  
  
  public void addFrameLengthToAllEvents()
  {
    addFrameLengthToAllEvents(masterFrameLength);
  }
  
  
  
  
  
  
  
  //Pre:
  // eventName - any String != null
  // time - any long
  //
  //Post:
  // calls feedInitialTime to the specified event if it exists
  public void feedInitialTime( String eventName, double time )
  {
    
    if( eventNames.contains(eventName) )
    {
      EFTimeEvent tempTimeEvent = timeEvents.get(eventName);
      timeEvents.remove(eventName);
      
      tempTimeEvent.feedInitialTime(time);
      timeEvents.put( eventName, tempTimeEvent );
    }
  }
  
  
  
  
  
  
  
  //Pre:
  // eventName - any String != 0
  // time - any long
  //
  //Post:
  // calls feedFinalTime for the specified event if it exists
  public void feedFinalTime( String eventName, double time )
  {
    
    if( eventNames.contains(eventName) )
    {
      EFTimeEvent tempTimeEvent = timeEvents.get(eventName);
      timeEvents.remove(eventName);
      
      tempTimeEvent.feedFinalTime(time);
      timeEvents.put( eventName, tempTimeEvent );
    }
  }
  
  
  
  
  
  
  
  //Pre:
  // time - any long
  //
  //Post:
  // calls feedInitialTime for the master event
  public void feedMasterInitialTime( long time )
  {
    EFTimeEvent tempTimeEvent = timeEvents.get(masterEvent);
    tempTimeEvent.feedInitialTime((double)time);
  }
  
  
  
  
  
  
  
  //Pre:
  // time - any long
  //
  //Post:
  // calls feedFinalTime for the master event
  public void feedMasterFinalTime( long time )
  {
    EFTimeEvent tempTimeEvent = timeEvents.get(masterEvent);
    tempTimeEvent.feedFinalTime((double)time);
  }
  
  
  
  
  
  
  
  //Pre:
  // eventName - any String
  //
  //Post:
  // returns true if the eventName is registered; false if not
  public boolean isNameRegistered( String eventName )
  {
    if( eventNames.contains(eventName) ) return true;
    return false;
  }
  
  
  
  
  
  
  
  public double _getLagCount()
  {
    return lagCount;
  }
  
  
  
  
  
  
  
  public void setDefaultMasterFrameLength( long defaultMasterFrameLength )
  {
    this.defaultMasterFrameLength = defaultMasterFrameLength;
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  //Pre:
  // none
  //
  //Post:
  // Gets the next white listed event and its spare time needed
  private void getNextWhiteListedEvent()
  {
    whiteListCounter++;
    
    if( whiteListCounter >= whiteList.size() ) whiteListCounter = 0;
    
    targetWhiteListEvent = timeEvents.get(whiteList.get(whiteListCounter));
    targetWhiteListEvent.addTimeSinceLastTrigger(targetWhiteListEvent.getFrameLength());
    
  }
}








  