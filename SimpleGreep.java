/*     */ import greenfoot.Greenfoot;
/*     */ 
/*     */ public class SimpleGreep
/*     */   extends Greep
/*     */ {
/*     */   private static final int TOMATO_LOCATION_KNOWN = 1;
/*     */   
/*     */   public SimpleGreep(Ship ship)
/*     */   {
/*  39 */     super(ship);
/*     */   }
/*     */   
/*     */   public void act()
/*     */   {
/*  47 */     super.act();
/*     */     
/*     */ 
/*  50 */     checkFood();
/*  52 */     if (carryingTomato())
/*     */     {
/*  53 */       bringTomatoHome();
/*     */     }
/*  55 */     else if (getTomatoes() != null)
/*     */     {
/*  56 */       TomatoPile tomatoes = getTomatoes();
/*  57 */       if (!blockAtPile(tomatoes))
/*     */       {
/*  59 */         turnTowards(tomatoes.getX(), tomatoes.getY());
/*  60 */         move();
/*     */       }
/*     */     }
/*  63 */     else if (getMemory(0) == 1)
/*     */     {
/*  65 */       turnTowards(getMemory(1), getMemory(2));
/*  66 */       move();
/*     */     }
/*  68 */     else if (numberOfOpponents(false) > 3)
/*     */     {
/*  70 */       kablam();
/*     */     }
/*     */     else
/*     */     {
/*  73 */       randomWalk();
/*     */     }
/*  77 */     if ((atWater()) || (moveWasBlocked()))
/*     */     {
/*  79 */       int r = getRotation();
/*  80 */       setRotation(r + Greenfoot.getRandomNumber(2) * 180 - 90);
/*  81 */       move();
/*     */     }
/*     */   }
/*     */   
/*     */   public void checkFood()
/*     */   {
/*  90 */     TomatoPile tomatoes = getTomatoes();
/*  91 */     if (tomatoes != null)
/*     */     {
/*  92 */       loadTomato();
/*     */       
/*     */ 
/*     */ 
/*  96 */       setMemory(0, 1);
/*  97 */       setMemory(1, tomatoes.getX());
/*  98 */       setMemory(2, tomatoes.getY());
/*     */     }
/*     */   }
/*     */   
/*     */   private void randomWalk()
/*     */   {
/* 108 */     if (randomChance(3)) {
/* 109 */       turn((Greenfoot.getRandomNumber(3) - 1) * 100);
/*     */     }
/* 112 */     move();
/*     */   }
/*     */   
/*     */   private void bringTomatoHome()
/*     */   {
/* 120 */     if (atShip())
/*     */     {
/* 121 */       dropTomato();
/*     */     }
/*     */     else
/*     */     {
/* 124 */       turnHome();
/* 125 */       move();
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean blockAtPile(TomatoPile tomatoes)
/*     */   {
/* 137 */     boolean atPileCentre = (tomatoes != null) && (distanceTo(tomatoes.getX(), tomatoes.getY()) < 4);
/* 138 */     if ((atPileCentre) && (getFriend() == null))
/*     */     {
/* 140 */       block();
/* 141 */       return true;
/*     */     }
/* 144 */     return false;
/*     */   }
/*     */   
/*     */   private int distanceTo(int x, int y)
/*     */   {
/* 150 */     int deltaX = getX() - x;
/* 151 */     int deltaY = getY() - y;
/* 152 */     return (int)Math.sqrt(deltaX * deltaX + deltaY * deltaY);
/*     */   }
/*     */   
/*     */   public String getName()
/*     */   {
/* 160 */     return "Simple";
/*     */   }
/*     */ }


/* Location:           C:\Users\Ehsan\git\Greep\
 * Qualified Name:     SimpleGreep
 * JD-Core Version:    0.7.0.1
 */