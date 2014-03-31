/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EFObject;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import EFIniParser.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;








/**
 *
 * @author Taylor
 */
public class EFObject
{
  public int objectIndex;
  public String name;
  public int numberOfAttachedObjects;
  public ArrayList<BufferedImage> attachments;
  public boolean successCondition;
  public String failureReason;
  public int shieldPenalty;
  public int successReward;
  public BufferedImage atAGlance;
  public BufferedImage currentRules;
  
  
  public EFObject(int objectIndex, String name, int numberOfAttachedObjects, String objectDirectory, 
        boolean successCondition, String failureReason,
        int shieldPenalty, int successReward, EFIniParser levelInformation)
  {
    this.objectIndex = objectIndex;
    this.name = name;
    this.numberOfAttachedObjects = numberOfAttachedObjects;
    this.successCondition = successCondition;
    this.failureReason = failureReason;
    this.shieldPenalty = shieldPenalty;
    this.successReward = successReward;
    this.attachments = new ArrayList();
    
    try
    {
      this.atAGlance = ImageIO.read(new File(objectDirectory + 
            levelInformation.getValue("object." + objectIndex + ".atAGlance")));
      this.currentRules = ImageIO.read(new File(objectDirectory + 
            levelInformation.getValue("object." + objectIndex + ".currentRules")));
      
      
      for(int i = 0; i < numberOfAttachedObjects; i++)
      {

        this.attachments.add(ImageIO.read(new File(objectDirectory
              + levelInformation.getValue("object." + objectIndex + ".attachment." + i)))
        );
      }
    } catch (IOException e)
    {
      this.atAGlance = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
      this.currentRules = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
      this.numberOfAttachedObjects = 1;
      this.attachments.add(new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR));
    }
  }
}







