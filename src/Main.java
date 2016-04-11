import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main implements Runnable{
    int cooldownJump;
    long cooldownTimerBullet = 0;
    long cooldownTimerBullets = 0;
    final int WIDTH = 1000;
    final int HEIGHT = 700;
    JFrame frame;
    Canvas canvas;
    BufferStrategy bufferStrategy;

    ArrayList<Enemy> enemies = new ArrayList<Enemy>();
   
    ArrayList<Bullet> bullets = new ArrayList<Bullet>();

    boolean UPressed = false;
    boolean DPressed = false;
    boolean RPressed = false;
    boolean LPressed = false;
    boolean space = false;
    int alive = 3;
    int curLevel = 1;
    
    TreeSet <Integer>tree = new TreeSet<Integer>();
                  
    boolean level1 = false;
    boolean level2 = false;
    boolean level3 = false;
    boolean level4 = false;
    boolean level5 = false;
    boolean level6 = false;
    boolean level7 = false;
    boolean level8 = false;
    boolean level9 = false;
    boolean level10 = false;
    boolean isArrowAlive = true;

    boolean SpeedPowerupAlive = true;

    boolean isArrowOnPowerup = false;

    double speedArrow = 3.0;
    
    int amountKilled = 0;

    private RestartButton restart;
    private QuitButton quit;

    Color arrowColor = Color.blue;
    Color powerColor = Color.green;

    Color enemyColor = Color.orange;

    int arrowScore = 0;

    public Main(){
   	 frame = new JFrame("The Chase");
   	 restart = new RestartButton();
   	 restart.setBounds(700, 650, 100, 36);
   	 frame.add(restart);
   	 restart.setVisible(true);
   	 quit = new QuitButton();
   	 quit.setBounds(800, 650, 100, 36);
   	 frame.add(quit);
   	 quit.setVisible(true);
   	 JPanel panel = (JPanel) frame.getContentPane();
   	 panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
   	 panel.setLayout(null);
   	 canvas = new Canvas();
   	 canvas.setBounds(0, 0, WIDTH, HEIGHT);
   	 canvas.setIgnoreRepaint(true);
   	 panel.add(canvas);
   	 canvas.addKeyListener(new KeyControl());
   	 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   	 frame.pack();
   	 frame.setResizable(false);
   	 frame.setVisible(true);
   	 canvas.createBufferStrategy(2);
   	 bufferStrategy = canvas.getBufferStrategy();
   	 canvas.requestFocus();
    }

    private class KeyControl extends KeyAdapter {
   	 public void keyPressed(KeyEvent e) {
   		 switch(e.getKeyCode()) {
   		 case KeyEvent.VK_UP:
   			 UPressed = true;
   			 break;
   		 case KeyEvent.VK_DOWN:
   			 DPressed = true;
   			 break;
   		 case KeyEvent.VK_RIGHT:
   			 RPressed = true;
   			 break;
   		 case KeyEvent.VK_LEFT:
   			 LPressed = true;
   			 break;
   		 case KeyEvent.VK_SPACE:
   			 space = true;
   			 break;
   		 }

   	 }
   	 public void keyReleased(KeyEvent e) {
   		 switch(e.getKeyCode()) {
   		 case KeyEvent.VK_UP:
   			 UPressed = false;
   			 break;
   		 case KeyEvent.VK_DOWN:
   			 DPressed = false;
   			 break;
   		 case KeyEvent.VK_RIGHT:
   			 RPressed = false;
   			 break;
   		 case KeyEvent.VK_LEFT:
   			 LPressed = false;
   			 break;
   		 case KeyEvent.VK_SPACE:
   			 space = false;
   			 break;
   		 }

   	 }

    }


    long desiredFPS = 100;
    long desiredDeltaLoop = (1000*1000*1000)/desiredFPS;
    boolean running = true;

    public void run(){
   	 long beginLoopTime;
   	 long endLoopTime;
   	 long currentUpdateTime = System.nanoTime();
   	 long lastUpdateTime;
   	 long deltaLoop;
   	 init();

   	 while(running){
   		 beginLoopTime = System.nanoTime();
   		 render();
   		 lastUpdateTime = currentUpdateTime;
   		 currentUpdateTime = System.nanoTime();
   		 update((int) ((currentUpdateTime - lastUpdateTime)/(1000*1000)));
   		 endLoopTime = System.nanoTime();
   		 deltaLoop = endLoopTime - beginLoopTime;

   		 if(deltaLoop > desiredDeltaLoop){
   			 //Do nothing. We are already late.
   		 }else{
   			 try{
   				 Thread.sleep((desiredDeltaLoop - deltaLoop)/(1000*1000));
   			 }catch(InterruptedException e){
   				 //Do nothing
   			 }
   		 }
   	 }
    }
    

    
    private void init()
    {
   	 enemies.clear();
   	 enemies.add(new Enemy(20, 20, 100, 200));
   	 level1 = true;
   	 tree.add(0);
    }
    
    private void whatLevel() {
   	 int left = enemies.size();
     int enemiesNum = (int) Math.pow(2, curLevel); 
   	 if (left == 0) {
   			 curLevel++;
   			 for (int i = 0; i < enemiesNum/4; i++) {
   			 int random1 = (int) (Math.random() * 1000);
   			 int random2 = (int) (Math.random() * 600);
   			 int random3 = (int) (Math.random() * 600);
   			 int random4 = (int) (Math.random() * 1000);
   			 enemies.add(new Enemy(20, 20, random1, 1620));
   			 enemies.add(new Enemy(20, 20, 980, random2));
   			 enemies.add(new Enemy(20, 20, 20, random3));
   			 enemies.add(new Enemy(20, 20, random4, -350));
   			 arrow_X = 500;
   			 arrow_Y = 350;
   			 }
   			 try {
   				 Thread.sleep(500);
   			 } catch (InterruptedException e) {
   				 
   				 e.printStackTrace();
   			 }
   		 }
   		 
   	 }
    
    private void arrowDeath() {
      	 for (int i = enemies.size()-1; i > -1; i--) {
       		 double enemyX = enemies.get(i).x;
       		 double enemyY = enemies.get(i).y;
       			 if (arrow_X <= enemyX+20 && arrow_X >= enemyX-20 ) {
       				 if (arrow_Y <= enemyY + 20 && arrow_Y >= enemyY - 20) {
       					 if (isArrowOnPowerup == false) {
       			    	 enemies.remove(i);
       			    	 //System.out.println("Game over!");
       			    	 //System.exit(0);
       					 }
       					 break;
       			     }
       		     }
       	 
        	} 
    }
   	 
   	

    private void render() {
   	 Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
   	 g.clearRect(0, 0, WIDTH, HEIGHT);
   	 render(g);
   	 g.dispose();
   	 bufferStrategy.show();
    }


    private double arrow_X = 500;
    private double arrow_Y = 350;
    private double PowerX = (int) (Math.random() * 1000);
    private double PowerY = (int) (Math.random() * 600);
    
    int randomEnemyX = (int) (Math.random() * 1000);
    int randomEnemyY = (int) (Math.random() * 600);

    public void isTouchingPowerup() {

   	 if (isArrowAlive == true) {
   		 if (SpeedPowerupAlive == true) {
   			 try {
   				 Thread.sleep(1);
   			 } catch (InterruptedException e) {
   				 
   				 e.printStackTrace();
   			 }

   			 if (arrow_X <= PowerX + 15 && arrow_X >= PowerX - 15) {
   				 if (arrow_Y <= PowerY + 10 && arrow_Y >= PowerY - 10) {
   					 isArrowOnPowerup = true;
   					 SpeedPowerupAlive = false;
   					 speedArrow = 5;
   					 powerColor = Color.black;
   					 Timer t = new Timer();
   					 t.schedule(new TimerTask() {

   						 @Override
   						 public void run() {
   							 isArrowOnPowerup = false;
   							 int random1 = (int) (Math.random() * 975);
   							 int random2 = (int) (Math.random() * 575);
   							 PowerX = random1;
   							 PowerY = random2;
   							 speedArrow = 3;
   							 SpeedPowerupAlive = true;
   							 powerColor = Color.green;
   						 }
   					 }, 5000);

   				 }
   			 }
   		 }


   	 }

    }


    public void areAnyDead() {

   	 for (int i = enemies.size()-1; i > -1; i--) {
   		 double enemyX = enemies.get(i).x;
   		 double enemyY = enemies.get(i).y;
   		 for (int k = bullets.size()-1; k > -1; k--) {
   			 double bulletX = bullets.get(k).X;
   			 double bulletY = bullets.get(k).Y;
   			 if (bulletX <= enemyX+20 && bulletX >= enemyX-20 ) {
   				 if (bulletY <= enemyY + 20 && bulletY >= enemyY - 20) {
   					 if (isArrowOnPowerup == false) {
   						bullets.remove(k);
   					 }
   					enemies.remove(i);
   					 
   					 amountKilled++;
   					 tree.add(amountKilled);
   					 break;
   			     }
   		     }
   		  }
   	 
    	} 
  	 }
   	 

    public void move() {

   	 if (UPressed == true) {
   		 arrow_Y = arrow_Y - speedArrow;
   	 }
   	 if (DPressed == true) {
   		 arrow_Y = arrow_Y + speedArrow;
   	 }
   	 if (LPressed == true) {
   		 arrow_X = arrow_X - speedArrow;
   	 }
   	 if (RPressed == true) {
   		 arrow_X = arrow_X + speedArrow; 
   	 }

   	 if (space == true) {

   		 if (cooldownTimerBullets < System.currentTimeMillis()) {
   			 Bullet b = new Bullet();
   			 bullets.add(b);
   			 b.justStarted=true;
   			 b.X = arrow_X;
   			 b.Y = arrow_Y;
   			 cooldownTimerBullets = System.currentTimeMillis();
   		 }
   	 }
    }

    public void moveEnemies() {
   	 

   	 for (int i = 0; i < enemies.size(); i++) {
   		 int currentArrowLocationX = (int) arrow_X;
   		 int currentArrowLocationY = (int) arrow_Y;
   		 double lx = currentArrowLocationX - enemies.get(i).x;
   		 double ly = currentArrowLocationY - enemies.get(i).y;
   		 double h = Math.sqrt(lx*lx+ly*ly);
   		 double d = 1.5;
   		 double dx = (lx * d)/h;
   		 double dy = (ly * d)/h;
   		 enemies.get(i).move(dx, dy);
   	 }
    }
    public void moveBullets(Bullet bullet) {
   	 bullet.X = bullet.X + bullet.dx;
   	 bullet.Y = bullet.Y + bullet.dy;
    }


    public void checkBoundaries() {
   	 if (arrow_X > WIDTH-20) {
   		 arrow_X = WIDTH-20;
   	 }
   	 if (arrow_X < 0) {
   		 arrow_X = 0;
   	 }
   	 if (arrow_Y > HEIGHT-20) {
   		 arrow_Y = HEIGHT-20;
   	 }
   	 if (arrow_Y < 0) {
   		 arrow_Y = 0;
   	 }
   	 for (int i = bullets.size()-1; i > -1; i--)
   	 {
   		 if(bullets.get(i).X > 979 || bullets.get(i).X < 0 || bullets.get(i).Y > 680 || bullets.get(i).Y < 0)
   		 {
   			 bullets.remove(i);
   			 continue;
   		 }
    

   		 if (bullets.get(i).justStarted == true) {
   			 int clickedX = (int) MouseInfo.getPointerInfo().getLocation().getX() - 10;
   			 int clickedY = (int) MouseInfo.getPointerInfo().getLocation().getY() - 40;
   			 double lx = clickedX - bullets.get(i).X;
   			 double ly = clickedY - bullets.get(i).Y;
   			 double h = Math.sqrt(lx*lx+ly*ly);
   			 double d = 6;
   			 double dx = (lx * d)/h;
   			 double dy = (ly * d)/h;
   			 bullets.get(i).dx = dx;
   			 bullets.get(i).dy = dy;
   			 bullets.get(i).justStarted = false;
   			 moveBullets(bullets.get(i));
   		 }
   		 if (bullets.get(i).justStarted == false) {
   			 moveBullets(bullets.get(i));
   		 }

   	 }   	 

    }

    protected void update(int deltaTime){
   	 move();
   	 checkBoundaries();
   	 moveEnemies();
   	 areAnyDead();
   	 isTouchingPowerup();
   	 whatLevel();
   	 arrowDeath();
    }

    protected void render(Graphics2D g){
   	 g.setColor(Color.black);
   	 g.fillRect(0, 0, 1000, 800);
   	 g.setColor(powerColor);
   	 g.fillRect((int)PowerX, (int)PowerY, 15, 15);
   	 g.setColor(arrowColor);
   	 g.fillRect((int)arrow_X, (int)arrow_Y, 20, 20);
   	 g.setColor(Color.red);
   	 for (Bullet b : bullets)
   	 {
   		 Rectangle Rect = new Rectangle((int)b.X, (int)b.Y, 10, 10);
   		 g.fill(Rect);
   	 }

   	 g.setColor(enemyColor);
   	 for (Enemy en : enemies)
   	 {
   		 Rectangle Rect = new Rectangle((int)en.getX(), (int)en.getY(), 25, 25);
   		 g.fill(Rect);
   	 }


   	 g.setColor(Color.white);
   	 g.drawString("Current level: " + curLevel, 10, 30);
   	 g.drawString("Amount killed: " + tree.last(), 10, 50);
   	 

    }

    public static void main(String [] args){
   	 Main ex = new Main();
   	 new Thread(ex).start();
    }


    private class RestartButton extends JButton implements ActionListener {

    	private static final long serialVersionUID = 1L;
	RestartButton() {
   		 super("Restart");
   		 addActionListener(this);

   		 repaint();
   	 }
   	 public void actionPerformed(ActionEvent arg0) {
   		 Main ex = new Main();
   		 new Thread(ex).start();
   		 //System.exit(0);

   	 }
    }

    private class QuitButton extends JButton implements ActionListener {
 
    	private static final long serialVersionUID = 1L;
	QuitButton() {
   		 super("Quit");
   		 addActionListener(this);

   		 repaint();
   	 }
   	 public void actionPerformed(ActionEvent arg0) {
   		 System.exit(0);

   	 }
    }

}