import greenfoot.*;  // (World, Actor, GreenfootImage, and Greenfoot)

/**
 * A Greep is an alien creature that likes to collect tomatoes.
 * 
 * Rules:
 * 
 * Rule 1 
 * Only change the class �MyGreep�. No other classes may be modi�ed or created. 
 *
 * Rule 2 
 * You cannot extend the Greeps� memory. That is: you are not allowed to add 
 * �elds (other than �nal �elds) to the class. Some general purpose memory is
 * provided. (The ship can also store data.) 
 * 
 * Rule 3 
 * You can call any method de�ned in the "Greep" superclass, except act(). 
 * 
 * Rule 4 
 * Greeps have natural GPS sensitivity. You can call getX()/getY() on any object
 * and get/setRotation() on yourself any time. Friendly greeps can communicate. 
 * You can call getMemory() and getFlag() on another greep to ask what they know. 
 * 
 * Rule 5 
 * No creation of objects. You are not allowed to create any scenario objects 
 * (instances of user-de�ned classes, such as MyGreep). Greeps have no magic 
 * powers � they cannot create things out of nothing. 
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
public class ENGreep
  extends Greep
{
  private static final boolean TOMATO_LOCATION_KNOWN = true;
  
  public ENGreep(Ship ship)
  {
    super(ship);
  }
  
  public void act()
  {
    super.act();
  

    checkFood();
    shareInfo();
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
    else if (getFlag(1) == true && !atWater())
    {
      turnTowards(getMemory(1), getMemory(2));
      move();
    }
    else if ( !carryingTomato() && (numberOfOpponents(false) > numberOfFriends(false)) && ((numberOfOpponents(true) > numberOfFriends(true)) || numberOfFriends(true)==0))
    {
      kablam();
    }
    else
    {
      randomWalk();
    }
  }
  public void shareInfo(){
     Greep friend = getFriend();
     if(getFriend()!= null && getFlag(1) == TOMATO_LOCATION_KNOWN && friend.getFlag(1)!= TOMATO_LOCATION_KNOWN ){
        friend.setFlag(1,TOMATO_LOCATION_KNOWN);
        friend.setMemory(1, getMemory(1));
        friend.setMemory(2, getMemory(2));
    }
}
  public void checkFood()
  {
    TomatoPile tomatoes = getTomatoes();
    if (tomatoes != null)
    {
      loadTomato();
      setFlag(1, TOMATO_LOCATION_KNOWN);
      setMemory(1, tomatoes.getX());
      setMemory(2, tomatoes.getY());
    }
    else if ( distanceTo(getMemory(1), getMemory(2)) < 10 && getFlag(1) == TOMATO_LOCATION_KNOWN){
      setFlag(1, false);
      setMemory(1, 0);
      setMemory(2, 0);
    }
  }
  
  private void randomWalk()
  {

   if ((atWater()||moveWasBlocked())) {
         setMemory(0, getMemory(0)+1);
        if(getMemory(0) > 50){
            kablam();
        }
        if (getMemory(0)> 3 && getFlag(1)== (TOMATO_LOCATION_KNOWN || carryingTomato())){
            setMemory(3,5);
            turn(Greenfoot.getRandomNumber(60)+90);
         }
         else 
            turn(Greenfoot.getRandomNumber(270)+90);
     }
    else {
        setMemory(0,0);
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
      turnTowards(getMemory(1), getMemory(2));
    }
    else
    {
     if ( getMemory(3) > 0){
          setMemory(3, getMemory(3)-1);
     }
     else{
        turnHome();
     }

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

