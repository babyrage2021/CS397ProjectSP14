package EFButton;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.io.*;
import EFDrawable.*;
import EFCoord.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import EFHelpTag.*;


//*Custom button class*\\
public class EFButton implements EFHelpTag {
  
  //*Controlls*\\
  
  private int x;
  private int y;
  public boolean shouldDraw;
  public boolean shouldCheckIsCursorOn;
  public DisplayType displayType;
  public BorderType borderType;
  public BackgroundType backgroundType;
  
  private int messageX;
  private int messageY;
  private String message;
  private Font font;
  private FontMetrics fm;
  private int stringWidth;
  private int stringHeight;
  private int stringAscent;
  private int stringDescent;
  public Color mainMessageColor;
  public Color activatedMessageColor;
  
  private int borderBuffer;
  private float borderThickness;
  private int borderX;
  private int borderY;
  private int borderXLength;
  private int borderYLength;
  private boolean isUsingCustomLengths;
  private int customBorderXLength;
  private int customBorderYLength;
  public Color mainBorderColor;
  public Color activatedBorderColor;
  private int numberOfXDashes;
  private int dotXOffset;
  private int numberOfYDashes;
  private int dotYOffset;
  public int dashLength;
  
  private GradientPaint gradient;
  public Color mainBackgroundColor;
  public Color activatedBackgroundColor;
  private int backgroundTransperency;
  private int[] xDashPositions;
  private int[] yDashPositions;
  public Color gradientColor1;
  public Color gradientColor2;
  
  private BufferedImage mainIcon;
  private BufferedImage activatedIcon;
  private Graphics2D g;
  
  private ArrayList<String> presetNames;
  private ArrayList<EFButton> presets;
  
  public static Graphics2D defaultGraphics = (Graphics2D) new BufferedImage(100, 100, 
        BufferedImage.TYPE_4BYTE_ABGR ).getGraphics();
  
  public static DisplayType defaultDisplayType = DisplayType.BOTTOM_LEFT;
  
  public static Font defaultFont = new Font( "Lucidia Console", 0, 20 );
  public static Color defaultMainMessageColor = Color.white;
  public static Color defaultActivatedMessageColor = Color.red;
  
  private static int defaultBorderBuffer = 0;
  private static float defaultBorderThickness = 1F;
  public static BorderType defaultBorderType = BorderType.NONE;
  public static Color defaultMainBorderColor = Color.black;
  public static Color defaultActivatedBorderColor = Color.black;
  private static int defaultDashLength = 6;
  
  public static BackgroundType defaultBackgroundType = BackgroundType.TRANSLUCENT;
  public static Color defaultMainBackgroundColor = Color.black;
  public static Color defaultActivatedBackgroundColor = Color.black;
  private static int defaultBackgroundTransperency = 128;
  public static Color defaultGradientColor1 = Color.lightGray;
  public static Color defaultGradientColor2 = Color.darkGray;
  
  private static BufferedImage defaultIcon = new BufferedImage(100, 100, 
        BufferedImage.TYPE_4BYTE_ABGR );
  
  public static String defaultHelpMessage = "";
  private String helpMessage;
  
  
  
  
  
  
  
  //Pre:
  //  --none--
  //Post:
  //  Creates a default button
  
  public EFButton( Graphics2D g )
  {
    this( g, 0, 0, "", defaultFont, defaultMainMessageColor, defaultActivatedMessageColor,
         defaultDisplayType, defaultBorderType, defaultBorderBuffer, defaultBorderThickness,
         defaultMainBorderColor, defaultActivatedBorderColor, defaultBackgroundType, defaultMainBackgroundColor, 
         defaultActivatedBackgroundColor, defaultBackgroundTransperency, defaultGradientColor1, defaultGradientColor2,
         defaultDashLength, defaultIcon );
  }
  
  
  public EFButton( int x, int y, String message )
  {
    this( defaultGraphics, x, y, message, defaultFont, defaultMainMessageColor, defaultActivatedMessageColor,
         defaultDisplayType, defaultBorderType, defaultBorderBuffer, defaultBorderThickness, 
         defaultMainBorderColor, defaultActivatedBorderColor, defaultBackgroundType, defaultMainBackgroundColor, 
         defaultActivatedBackgroundColor, defaultBackgroundTransperency, defaultGradientColor1, defaultGradientColor2,
         defaultDashLength, defaultIcon );
  }
  
  
  
  
  public EFButton( Graphics2D g, int x, int y, String message )
  {
    this( g, x, y, message, defaultFont, defaultMainMessageColor, defaultActivatedMessageColor,
         defaultDisplayType, defaultBorderType, defaultBorderBuffer, defaultBorderThickness, 
         defaultMainBorderColor, defaultActivatedBorderColor, defaultBackgroundType, defaultMainBackgroundColor, 
         defaultActivatedBackgroundColor, defaultBackgroundTransperency, defaultGradientColor1, defaultGradientColor2,
         defaultDashLength, defaultIcon );
  }
  
  
  
  
  
  
  
  public EFButton( Graphics2D g, int x, int y, String message, Font font, Color mainMessageColor, 
                  Color activatedMessageColor )
  {
    this( g, x, y, message, font, mainMessageColor, activatedMessageColor,
         defaultDisplayType, defaultBorderType, defaultBorderBuffer, defaultBorderThickness, 
         defaultMainBorderColor, defaultActivatedBorderColor, defaultBackgroundType, defaultMainBackgroundColor, 
         defaultActivatedBackgroundColor, defaultBackgroundTransperency, defaultGradientColor1, defaultGradientColor2,
         defaultDashLength, defaultIcon );
  }
  
  
  
  
  
  
  
  public EFButton( Graphics2D g, int x, int y, String message, Font font, Color mainMessageColor, 
                  Color activatedMessageColor, DisplayType displayType )
  {
    this( g, x, y, message, font, mainMessageColor, activatedMessageColor,
         displayType, defaultBorderType, defaultBorderBuffer, defaultBorderThickness,
         defaultMainBorderColor, defaultActivatedBorderColor, defaultBackgroundType, defaultMainBackgroundColor, 
         defaultActivatedBackgroundColor, defaultBackgroundTransperency, defaultGradientColor1, defaultGradientColor2,
         defaultDashLength, defaultIcon );
  }
  
  
  
  
  
  
  
  public EFButton( Graphics2D g, int x, int y, String message, Font font, Color mainMessageColor,
                  Color activatedMessageColor, DisplayType displayType, BorderType borderType, int borderBuffer, 
                  float borderThickness )
  {
    this( g, x, y, message, font, mainMessageColor, activatedMessageColor,
         displayType, borderType, borderBuffer, borderThickness,  
         defaultMainBorderColor, defaultActivatedBorderColor, defaultBackgroundType, defaultMainBackgroundColor, 
         defaultActivatedBackgroundColor, defaultBackgroundTransperency, defaultGradientColor1, defaultGradientColor2,
         defaultDashLength, defaultIcon );
  }
  
  
  
  
  
  
  
  public EFButton( Graphics2D g, int x, int y, String message, Font font, Color mainMessageColor, 
                  Color activatedMessageColor, DisplayType displayType, BorderType borderType, int borderBuffer, 
                  float borderThickness, Color mainBorderColor, Color activatedBorderColor )
  {
    this( g, x, y, message, font, mainMessageColor, activatedMessageColor,
         displayType, borderType, borderBuffer, borderThickness, 
         mainBorderColor, activatedBorderColor, defaultBackgroundType, defaultMainBackgroundColor, 
         defaultActivatedBackgroundColor, defaultBackgroundTransperency, defaultGradientColor1, defaultGradientColor2,
         defaultDashLength, defaultIcon );
  }
  
  
  
  
  
  
  
  public EFButton( Graphics2D g, int x, int y, String message, Font font, Color mainMessageColor, 
                  Color activatedMessageColor, DisplayType displayType, BorderType borderType, int borderBuffer, 
                  float borderThickness, Color mainBorderColor, Color activatedBorderColor, 
                  BackgroundType backgroundType )
  {
    this( g, x, y, message, font, mainMessageColor, activatedMessageColor,
         displayType, borderType, borderBuffer, borderThickness,  
         mainBorderColor, activatedBorderColor, backgroundType, defaultMainBackgroundColor, 
         defaultActivatedBackgroundColor, defaultBackgroundTransperency, defaultGradientColor1, defaultGradientColor2,
         defaultDashLength, defaultIcon );
  }
  
  
  
  
  
  
  
  public EFButton( Graphics2D g, int x, int y, String message, Font font, Color mainMessageColor, 
                  Color activatedMessageColor, DisplayType displayType, BorderType borderType, int borderBuffer, 
                  float borderThickness, Color mainBorderColor, Color activatedBorderColor, 
                  BackgroundType backgroundType, Color mainBackgroundColor, Color activatedBackgroundColor, 
                  int backgroundTransperency  )
  {
    this( g, x, y, message, font, mainMessageColor, activatedMessageColor,
         displayType, borderType, borderBuffer, borderThickness, 
         mainBorderColor, activatedBorderColor, backgroundType, mainBackgroundColor, 
         activatedBackgroundColor, backgroundTransperency, defaultGradientColor1, defaultGradientColor2,
         defaultDashLength, defaultIcon );
  }
  
  
  
  
  
  
  
  public EFButton( Graphics2D g, int x, int y, String message, Font font, Color mainMessageColor, 
                  Color activatedMessageColor, DisplayType displayType, BorderType borderType, int borderBuffer, 
                  float borderThickness, Color mainBorderColor, Color activatedBorderColor, 
                  BackgroundType backgroundType, Color mainBackgroundColor, Color activatedBackgroundColor, 
                  int backgroundTransperency, Color gradientColor1, Color gradientColor2 )
  {
    this( g, x, y, message, font, mainMessageColor, activatedMessageColor,
         displayType, borderType, borderBuffer, borderThickness, 
         mainBorderColor, activatedBorderColor, backgroundType, mainBackgroundColor, 
         activatedBackgroundColor, backgroundTransperency, gradientColor1, gradientColor2,
         defaultDashLength, defaultIcon );
  }
  
  
  
  
  
  
  
  public EFButton( Graphics2D g, int x, int y, String message, Font font, Color mainMessageColor, 
                  Color activatedMessageColor, DisplayType displayType, BorderType borderType, 
                  int borderBuffer, 
                  float borderThickness, Color mainBorderColor, Color activatedBorderColor, 
                  BackgroundType backgroundType, Color mainBackgroundColor, Color activatedBackgroundColor, 
                  int backgroundTransperency, Color gradientColor1, Color gradientColor2, int dashLength, 
                  BufferedImage icon )
  {
  
    this(g, x, y, message, font, mainMessageColor, activatedMessageColor, displayType, borderType,
          borderBuffer, borderThickness,
          mainBorderColor, activatedBorderColor, backgroundType, mainBackgroundColor,
          activatedBackgroundColor, backgroundTransperency, gradientColor1, gradientColor2,
          dashLength, icon, null, true, defaultHelpMessage, new ArrayList(), new ArrayList(), false, 0, 0
    );
    
    
    shouldCheckIsCursorOn = true;
    shouldDraw = true;
    presetNames = new ArrayList();
    presets = new ArrayList();
    
    
    addNewPreset("Default",
          new EFButton(g, x, y, message, font, mainMessageColor, activatedMessageColor, displayType,
                borderType,
                borderBuffer, borderThickness,
                mainBorderColor, activatedBorderColor, backgroundType, mainBackgroundColor,
                activatedBackgroundColor, backgroundTransperency, gradientColor1, gradientColor2,
                dashLength, mainIcon, activatedIcon, shouldDraw, helpMessage, new ArrayList(), new ArrayList(),
                isUsingCustomLengths, customBorderXLength, customBorderYLength)
    );
  }
  
  
  
  
  
  public EFButton( Graphics2D g, int x, int y, String message, Font font, Color mainMessageColor, 
                  Color activatedMessageColor, DisplayType displayType, BorderType borderType, 
                  int borderBuffer, 
                  float borderThickness, Color mainBorderColor, Color activatedBorderColor, 
                  BackgroundType backgroundType, Color mainBackgroundColor, Color activatedBackgroundColor, 
                  int backgroundTransperency, Color gradientColor1, Color gradientColor2, int dashLength,
                  BufferedImage icon, BufferedImage activatedIcon, boolean shouldDraw, String helpMessage, 
                  ArrayList<String> presetNames, 
                  ArrayList<EFButton> presets, boolean isUsingCustomLengths, int customBorderXLength, 
                  int customBorderYLength )
  {
    this.g = g;
    this.x = x;
    this.y = y;
    this.isUsingCustomLengths = isUsingCustomLengths;
    this.customBorderXLength = customBorderXLength;
    this.customBorderYLength = customBorderYLength;
    this.message = message;
    this.font = font;
    this.mainMessageColor = mainMessageColor;
    this.activatedMessageColor = activatedMessageColor;
    this.displayType = displayType;
    
    this.borderType = borderType;
    this.borderThickness = borderThickness;
    this.mainBorderColor = mainBorderColor;
    this.activatedBorderColor = activatedBorderColor;
    
    this.mainBackgroundColor = mainBackgroundColor;
    this.activatedBackgroundColor = activatedBackgroundColor;
    this.backgroundTransperency = backgroundTransperency;
    this.backgroundType = backgroundType;
    this.gradientColor1 = gradientColor1;
    this.gradientColor2 = gradientColor2;
    
    this.dashLength = dashLength;
    this.getFontMetrics();
    setBorderBuffer(borderBuffer);
    gradient = new GradientPaint( borderX + (int)( (float)borderXLength / 2F ), 
          borderY, new Color( 0.9F, 0.9F, 0.9F ),
                                 borderX + (int)( (float)borderXLength / 2F ),  
          borderY + borderYLength, Color.darkGray, true );
    
    if(icon != null)
    {
      this.mainIcon = icon.getSubimage(0, 0, icon.getWidth(), icon.getHeight());
    } else
    {
      this.mainIcon = null;
    }
    
    if(activatedIcon != null)
    {
      this.activatedIcon = activatedIcon.getSubimage(0, 0, activatedIcon.getWidth(), 
            activatedIcon.getHeight());
    } else
    {
      this.activatedIcon = null;
    }
    this.shouldDraw = shouldDraw;
    this.helpMessage = helpMessage;
    this.presetNames = presetNames;
    this.presets = presets;
  }
  
  
  
  
  
  
  public void setX( int x ) 
  {
    this.x = x;
    
    switch( displayType )
    {
      
      case CENTER: case CENTER_BOTTOM: case CENTER_TOP:
        //==
        
        this.messageX = x - (int)( (float)stringWidth / 2F );
        this.borderX = x - (int)( (float)borderXLength / 2F );
        
        break;
        
        
        
      case CENTER_LEFT: case TOP_LEFT: case BOTTOM_LEFT:
        //==
        
        this.messageX = x + borderBuffer;
        this.borderX = x;
        
        break;
        
        
        
      case CENTER_RIGHT: case TOP_RIGHT: case BOTTOM_RIGHT:
        //==
        
        this.messageX = x - stringWidth - borderBuffer;
        this.borderX = x - borderXLength;
        
        break;
        
        
        
      default:
        //==
        
        this.messageX = x + borderBuffer;
        this.borderX = x;
        
        break; 
    }
    calculateLineSegments();
    gradient = new GradientPaint( borderX + (int)( (float)borderXLength / 2F ), borderY, new Color( 0.9F, 0.9F, 0.9F ),
                                 borderX + (int)( (float)borderXLength / 2F ),  borderY + borderYLength, Color.darkGray, 
                                 true );
  }
  
  
  
  
  
  
  
  public int getX()
  {
    return x;
  }
  
  
  
  
  
  
  
  public void setY( int y ) 
  {
    this.y = y;
    
    switch( displayType )
    {
      
      case CENTER: case CENTER_LEFT: case CENTER_RIGHT:
        //==
        
        this.messageY = y + (stringAscent / 2) - stringDescent;
        this.borderY = y - (int)( (float)borderYLength / 2F );
        
        break;
        
        
        
      case CENTER_BOTTOM: case BOTTOM_LEFT: case BOTTOM_RIGHT:
        //==
        
        this.messageY = y - stringDescent - borderBuffer;
        this.borderY = y - borderYLength;
        
        break;
        
        
        
      case CENTER_TOP: case TOP_LEFT: case TOP_RIGHT:
        //==
        
        this.messageY = y + stringAscent + borderBuffer;
        this.borderY = y;
        
        break;
        
        
        
      default:
        //==
        
        this.messageY = y + borderBuffer;
        this.borderY = y;
        
        break; 
    }
    calculateLineSegments();
    gradient = new GradientPaint( borderX + (int)( (float)borderXLength / 2F ), borderY, new Color( 0.9F, 0.9F, 0.9F ),
                                 borderX + (int)( (float)borderXLength / 2F ),  borderY + borderYLength, Color.darkGray, 
                                 true );
    
  }
  
  
  
  
  
  
  
  public int getY()
  {
    return y;
  }
  
  
  
  public boolean getIsUsingCustomLengths()
  {
    return this.isUsingCustomLengths;
  }
  
  
  
  
  public void setIsUsingCustomLengths(boolean value)
  {
    this.isUsingCustomLengths = value;
    if(this.isUsingCustomLengths)
    {
      setBorderBuffer(borderBuffer);
      setX(x);
      setY(y);
    }
  }
  
  
  
  
  public void setCustomBorderXLength(int customBorderXLength)
  {
    this.isUsingCustomLengths = true;
    this.customBorderXLength = customBorderXLength;
      setBorderBuffer(borderBuffer);
    setX(x);
    setY(y);
  }
  
  
  
  public int getCustomBorderXLength()
  {
    return this.customBorderXLength;
  }
  
  
  
  public void setCustomBorderYLength(int customBorderYLength)
  {
    this.isUsingCustomLengths = true;
    this.customBorderYLength = customBorderYLength;
      setBorderBuffer(borderBuffer);
    setX(x);
    setY(y);
  }
  
  
  
  public int getCustomBorderYLength()
  {
    return this.customBorderYLength;
  }
  
  
  
  
  
  
  public void setMessage( String message ) 
  {
    this.message = message;
    getFontMetrics();
    setBorderBuffer(borderBuffer);
  }
  
  
  
  
  
  
  
  public String getMessage()
  {
    return message;
  }
  
  
  
  
  
  
  
  public void setFont( Font font )
  {
    this.font = font;
    getFontMetrics();
    setX(x);
    setY(y);
  }
  
  
  
  
  
  
  
  public void setBorderBuffer( int borderBuffer )
  {
    if( borderBuffer < 0 ) borderBuffer = 0;
    this.borderBuffer = borderBuffer;
    
    if(!this.isUsingCustomLengths)
    {
    this.borderXLength = borderBuffer * 2 + stringWidth;
    this.borderYLength = borderBuffer * 2 + stringHeight;
    }
    else
    {
      this.borderXLength = this.customBorderXLength;
      this.borderYLength = this.customBorderYLength;
    }
    
    
    setX(x);
    setY(y);
    
    calculateLineSegments();
  }
  
  
  
  
  
  
  
  public int getBorderBuffer()
  {
    return borderBuffer;
  }
  
  
  
  
  
  
  
  public void setBorderThickness( float borderThickness )
  {
    if( borderThickness <= 0 ) borderThickness = 1F;
    this.borderThickness = borderThickness;
  }
  
  
  
  
  
  
  
  public float getBorderThickness()
  {
    return borderThickness;
  }
  
  
  
  
  
  
  
  public void setBackgroundTransperency( int backgroundTransperency )
  {
    if( backgroundTransperency <= 0 ) backgroundTransperency = 1;
    if( backgroundTransperency > 255 ) backgroundTransperency = 255;
    this.backgroundTransperency = backgroundTransperency;
  }
  
  
  
  
  
  
  
  public void setDashLength( int dashLength )
  {
    if( dashLength <= 0 ) dashLength = 1;
    this.dashLength = dashLength;
    calculateLineSegments();
  }
  
  
  
  public void setMainIcon( String directory )
  {
    try
    {
      mainIcon = ImageIO.read(new File(directory));
    } catch (IOException e)
    {
    }
  }
  
  
  public void setActivatedIcon( String directory )
  {
    try
    {
      activatedIcon = ImageIO.read(new File(directory));
    } catch (IOException e)
    {
    }
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  public boolean isCursorOn (int cursorX, int cursorY) 
  {
    if(!this.shouldCheckIsCursorOn)
    {
      return false;
    }
    if( cursorX >= borderX - (int)Math.rint( borderThickness / 2 ) & cursorX <= borderX + borderXLength +
       (int)Math.rint( borderThickness / 2 ) & 
       cursorY >= borderY - (int)Math.rint( borderThickness / 2 ) & cursorY <= borderY + borderYLength +
       (int)Math.rint( borderThickness / 2 )) 
    {
      return true;
    }
    return false;
  }
  
  
  
  
  
  
  
  public void drawButton (boolean isActivated, EFCoord cameraPosition) 
  {
    drawButton( isActivated, g, cameraPosition );
  }
  
  
  
  
  
  
  
  public void drawButton( boolean isActivated, Graphics2D g, EFCoord cameraPosition ) 
  {
    if(!shouldDraw)
    {
      return;
    }
    g.setColor(mainBackgroundColor);
    if( isActivated ) g.setColor(activatedBackgroundColor);
    switch( backgroundType )
    {
      case TRANSLUCENT:
        
        g.setColor( new Color( g.getColor().getRed(), g.getColor().getGreen(), g.getColor().getBlue(), backgroundTransperency ) );
        g.fillRect( borderX, borderY, 
                   borderXLength + 1, 
                   borderYLength + 1); 
        
        break;
        
        
        
      case OPAQUE:
        
        g.fillRect( borderX, borderY, borderXLength, borderYLength );  
        
        break;
        
        
        
      case THREE_D:
        
        g.setPaint(gradient);
        
        g.fillRect( borderX, borderY, borderXLength, borderYLength ); 
        
        break;
        
        
        
      case ICON:
        
        
        if(!isActivated)
        {
          g.drawImage (mainIcon, borderX, borderY, borderXLength, borderYLength, null);
        }
        else if(activatedIcon != null)
        {
          g.drawImage (activatedIcon, borderX, borderY, borderXLength, borderYLength, null);
        }
        else 
        {
          g.drawImage (mainIcon, borderX, borderY, borderXLength, borderYLength, null);
        }
        
        
      default:
        break;
    }
    
    
    g.setColor (mainMessageColor);
    if (isActivated) g.setColor (activatedMessageColor); 
    g.setFont (font);
    g.drawString (message, messageX, messageY);
    
    
    g.setColor(mainBorderColor);
    if( isActivated ) g.setColor(activatedBorderColor);
    g.setStroke(new BasicStroke(0F));
    switch( borderType )
    {
      case LINE:
        
        g.setStroke(new BasicStroke(borderThickness));
        g.drawRect( borderX, borderY, borderXLength, borderYLength ); 
        
        break;
        
        
        
      case DOTS:
        //==
        
        for( int i = 0; i < numberOfXDashes; i++ )
      {
        g.setColor(Color.black);
        g.fillOval( xDashPositions[i] - (int)Math.rint( dashLength / 2 ) - 
                   (int)Math.rint( (float)dotXOffset / (float)numberOfXDashes * i ), 
                   
                   borderY - (int)Math.rint( dashLength / 2 ), dashLength, dashLength );
        
        g.fillOval( xDashPositions[i] - (int)Math.rint( dashLength / 2 ) - 
                   (int)Math.rint( (float)dotXOffset / (float)numberOfXDashes * i ), 
                   
                   borderY + borderYLength - (int)Math.rint( dashLength / 2 ), dashLength, dashLength );
          
      }
        
        g.fillOval( borderX + borderXLength - (int)Math.floor( dashLength / 2 ), 
                   
                   borderY + borderYLength - (int)Math.rint( dashLength / 2 ), dashLength, dashLength );
        
        
        for( int i = 0; i < numberOfYDashes; i++ )
      {
        g.setColor(Color.black);
        g.fillOval( borderX - (int)Math.rint( dashLength / 2 ), 
                   yDashPositions[i] - (int)Math.rint( dashLength / 2 ) - 
                   (int)Math.rint((float)dotYOffset / (float)numberOfYDashes * i), 
                   
                   dashLength, dashLength );
        
        g.fillOval( borderX + borderXLength - (int)Math.rint( dashLength / 2 ), 
                   yDashPositions[i] - (int)Math.rint( dashLength / 2 ) - 
                   (int)Math.rint((float)dotYOffset / (float)numberOfYDashes * i), 
                   
                   dashLength, dashLength );
          
      }
        
        break;
        
        
        
      case DASHES:
        
        
        
        for( int i = 0; i < numberOfXDashes; i++ )
      {
        g.setColor(Color.black);
        g.drawLine( xDashPositions[i], borderY, xDashPositions[i] + dashLength, borderY );
        g.drawLine( xDashPositions[i], borderY + borderYLength, xDashPositions[i] + dashLength, borderY + borderYLength );
          
      }
        
        
        for( int i = 0; i < numberOfYDashes; i++ )
      {
        g.setColor(Color.black);
        g.drawLine( borderX, yDashPositions[i], borderX, yDashPositions[i] + dashLength );
        
        g.drawLine( borderX + borderXLength, yDashPositions[i], borderX + borderXLength, yDashPositions[i] + dashLength );
          
      }
        
        
        
        
        
        
      default:
        break;
    }
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  public static void setDefaultBorderBuffer( int borderBuffer )
  {
    if( borderBuffer < 0 ) borderBuffer = 0;
    defaultBorderBuffer = borderBuffer;
  }
  
  
  
  
  
  
  
  public static void setDefaultBorderThickness( float thickness )
  {
    if( thickness <= 0 ) thickness = 1F;
    defaultBorderThickness = thickness;
  }
  
  
  
  
  
  
  
  public static void setDefaultBackgroundTransperency( int backgroundTransperency )
  {
    if( backgroundTransperency <= 0 ) backgroundTransperency = 1;
    if( backgroundTransperency > 255 ) backgroundTransperency = 255;
    defaultBackgroundTransperency = backgroundTransperency;
  }
  
  
  
  
  
  
  
  public static void setDefaultDashLength( int dashLength )
  {
    if( dashLength <= 0 ) dashLength = 1;
    defaultDashLength = dashLength;
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  private void getFontMetrics()
  {
    fm = g.getFontMetrics(font); //Used for the Font's Metrics
    stringWidth = (int)fm.getStringBounds( message, g ).getWidth();
    stringHeight = (int)fm.getStringBounds( message, g ).getHeight();
    stringAscent = (int)fm.getLineMetrics( message, g ).getAscent();
    stringDescent = (int)fm.getLineMetrics( message, g ).getDescent();
  }
  
  
  
  
  
  
  
  private void calculateLineSegments()
  {
    
    numberOfXDashes = (int)Math.rint( ( (float)borderXLength ) / ( (float)dashLength * 2F ) );
    dotXOffset = (int)Math.rint( borderXLength - ( ( numberOfXDashes - 1 ) * dashLength * 2 + dashLength ) );
    
    xDashPositions = new int[numberOfXDashes];
    
    for( int i = 0; i < numberOfXDashes; i++ )
    {
      xDashPositions[i] = (int)Math.rint( (float)borderX + (float)( dashLength * 2 * i ) +
        ( ( (float)dotXOffset / (float)( numberOfXDashes - 1 ) ) * i ) );
    }
    
    
    numberOfYDashes = (int)Math.rint( ( (float)borderYLength ) / ( (float)dashLength * 2F ) );
    dotYOffset = (int)Math.rint( borderYLength - ( ( numberOfYDashes - 1 ) * dashLength * 2 + dashLength ) );
    
    yDashPositions = new int[numberOfYDashes];
    
    for( int i = 0; i < numberOfYDashes; i++ )
    {
      yDashPositions[i] = (int)Math.rint( (float)borderY + (float)( dashLength * 2 * i ) +
        ( ( (float)dotYOffset / (float)( numberOfYDashes - 1 ) ) * i ) );
    }
  }
  
  
  
  public String getHelpMessage()
  {
    return this.helpMessage;
  }
  
  
  
  public void setHelpMessage(String helpMessage)
  {
    this.helpMessage = helpMessage;
  }
  
  
  
  public void addNewPreset(String presetName, EFButton newPreset)
  {
    if (presetNames.contains(presetName))
    {
      presets.remove(presetNames.indexOf(presetName));
      presetNames.remove(presetName);
    }
    if(newPreset == this)
    {
      newPreset = new EFButton(g, x, y, message, font, mainMessageColor, activatedMessageColor, displayType,
                borderType,
                borderBuffer, borderThickness,
                mainBorderColor, activatedBorderColor, backgroundType, mainBackgroundColor,
                activatedBackgroundColor, backgroundTransperency, gradientColor1, gradientColor2,
                dashLength, mainIcon, activatedIcon, shouldDraw, helpMessage, new ArrayList(), new ArrayList(),
                isUsingCustomLengths, customBorderXLength, customBorderYLength);
    }
    presetNames.add(presetName);
    presets.add(newPreset);
  }
  
  
  public void loadPreset(String presetName)
  {
    if( presetNames.contains(presetName))
    {
      presetNames.indexOf(presetName);
      
      EFButton template = presets.get(presetNames.indexOf(presetName));
      
      this.g = template.g;
      this.x = template.x;
      this.y = template.y;
      this.isUsingCustomLengths = template.isUsingCustomLengths;
      this.customBorderXLength = template.customBorderXLength;
      this.customBorderYLength = template.customBorderYLength;
      this.message = template.message;
      this.font = template.font;
      this.mainMessageColor = template.mainMessageColor;
      this.activatedMessageColor = template.activatedMessageColor;
      this.displayType = template.displayType;

      this.borderType = template.borderType;
      this.borderThickness = template.borderThickness;
      this.mainBorderColor = template.mainBorderColor;
      this.activatedBorderColor = template.activatedBorderColor;

      this.mainBackgroundColor = template.mainBackgroundColor;
      this.activatedBackgroundColor = template.activatedBackgroundColor;
      this.backgroundTransperency = template.backgroundTransperency;
      this.backgroundType = template.backgroundType;
      this.gradientColor1 = template.gradientColor1;
      this.gradientColor2 = template.gradientColor2;

      this.dashLength = template.dashLength;
      this.getFontMetrics();
      setBorderBuffer(template.borderBuffer);
      this.gradient = template.gradient;

      if (template.mainIcon != null)
      {
        this.mainIcon = template.mainIcon;
      } else
      {
        this.mainIcon = null;
      }
      if (template.activatedIcon != null)
      {
        this.activatedIcon = template.activatedIcon;
      } else
      {
        this.activatedIcon = null;
      }
      this.shouldDraw = template.shouldDraw;
      this.helpMessage = template.helpMessage;
      setX(x);
      setY(y);
    }
  }
}





