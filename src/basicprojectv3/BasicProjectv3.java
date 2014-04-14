/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package basicprojectv3;








/**
 *
 * @author Taylor
 */
public class BasicProjectv3
{

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args)
  {
    GameEngine newGame = new GameEngine();
    Thread newThread = new Thread(newGame);
    newThread.setName("MyGameThread");
    newThread.start();
  }

}







