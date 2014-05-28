import greenfoot.*;  // (World, Actor, GreenfootImage, and Greenfoot)

/**
 * A Greep is an alien creature that likes to collect tomatoes.
 * 
 * Rules:
 * 
 * Rule 1 
 * Only change the class ÔMyGreepÕ. No other classes may be modiÞed or created. 
 *
 * Rule 2 
 * You cannot extend the GreepsÕ memory. That is: you are not allowed to add 
 * Þelds (other than Þnal Þelds) to the class. Some general purpose memory is
 * provided. (The ship can also store data.) 
 * 
 * Rule 3 
 * You can call any method deÞned in the "Greep" superclass, except act(). 
 * 
 * Rule 4 
 * Greeps have natural GPS sensitivity. You can call getX()/getY() on any object
 * and get/setRotation() on yourself any time. Friendly greeps can communicate. 
 * You can call getMemory() and getFlag() on another greep to ask what they know. 
 * 
 * Rule 5 
 * No creation of objects. You are not allowed to create any scenario objects 
 * (instances of user-deÞned classes, such as MyGreep). Greeps have no magic 
 * powers Ð they cannot create things out of nothing. 
 * 
 * Rule 6 
 * You are not allowed to call any methods (other than those listed in Rule 4)
 * of any other class in this scenario (including Actor and World). 
 *  
 * If you change the name of this class you should also change it in
 * Ship.createGreep().
 * 
 * Please do not publish your solution anywhere. We might want to run this
 * competition again, or it might be used by teachers to run in a class, and
 * that would be ruined if solutions were available.
 * 
 * 
 * @author (your name here)
 * @version 0.1
 */
public class MyGreep
  extends Greep
{
  private static final int TOMATO_LOCATION_KNOWN = 1;
  
  public MyGreep(Ship ship)
  {
    super(ship);
  }
  
  public void act()
  {
    super.act();
  

    checkFood();
    if (carryingTomato())
    {
      bringTomatoHome();
    }
    else if (getTomatoes() != null)
    {
      TomatoPile tomatoes = getTomatoes();
      if (!blockAtPile(tomatoes))
      {
        turnTowards(tomatoes.getX(), tomatoes.getY());
        move();
      }
    }
    else if (getMemory(0) == 1)
    {
      turnTowards(getMemory(1), getMemory(2));
      move();
    }
    else if ((numberOfOpponents(false) > numberOfFriends(false)) && ((numberOfOpponents(true) > numberOfFriends(true)) || numberOfFriends(true)==0))
    {
      kablam();
    }
    else
    {
      randomWalk();
    }
    if ((atWater()) || (moveWasBlocked()))
    {
      int r = getRotation();
      setRotation(r + Greenfoot.getRandomNumber(2) * 180 - 90);
      move();
    }
  }
  
  public void checkFood()
  {
    TomatoPile tomatoes = getTomatoes();
    if (tomatoes != null)
    {
      loadTomato();
    }
  }
  
  private void randomWalk()
  {
    if ((atWater()||moveWasBlocked())) {
      if ( getMemory(3) > 20){
          kablam();
          setMemory(3,0);
     }
     else{
            turn(33);
     }
      setMemory(3, getMemory(3)+1);
    }
    else{
        setMemory(3,0);    
    }
    move();
  }
  
  /*private int[] pointInFront(){
    getRoation()
  }*/
  
  private void bringTomatoHome()
  {
    if (atShip())
    {
      dropTomato();
      turn(180);
    }
    else
    {
      turnHome();
      if (atWater()||moveWasBlocked()) {
        randomWalk();
      }
      else{
        move();
      }
    }
  }
  
  private boolean blockAtPile(TomatoPile tomatoes)
  {
    boolean atPileCentre = (tomatoes != null) && (distanceTo(tomatoes.getX(), tomatoes.getY()) < 4);
    if ((atPileCentre) && (getFriend() == null))
    {
      block();
      return true;
    }
    return false;
  }
  
  private int distanceTo(int x, int y)
  {
    int deltaX = getX() - x;
    int deltaY = getY() - y;
    return (int)Math.sqrt(deltaX * deltaX + deltaY * deltaY);
  }
  
  public String getName()
  {
    return "EhsanandNiels";
  }
}

