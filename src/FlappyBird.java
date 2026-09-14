import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener{
  int boardWidth=360;
  int boardHeight=640;

  //Images

  Image backgroundImg;
  Image birdImg;
  Image topPipeImg;
  Image bottomPipeImg;

  //Bird
  int birdX = boardWidth/8;
  int birdY = boardHeight/2;
  int birdWidth = 64;
  int birdHeight = 44;

  class Bird{
    int x = birdX;
    int y = birdY;
    int width = birdWidth;
    int height = birdHeight;
    Image img;


     Bird(Image img) {
      this.img = img;
    }

  }
  //Pipes
  int pipeX = boardWidth;
  int pipeY =0;
  int pipeWidth = 64;  
  int pipeHeight = 512;

  class Pipe{
    int x = pipeX;
    int y = pipeY;
    int width = pipeWidth;
    int height = pipeHeight;
    Image img;
    boolean passed = false;

    public Pipe(Image img) {
      this.img = img;
    }
    
  }

  //game logic
  Bird bird;
  int velocityX = -4; //move pipes to the left speed (siulates bird moving right)

  int velocityY = 0;
  int gravity =1;
  ArrayList<Pipe> pipes;
  Random random = new Random();

  Timer gameLoop;
  Timer placePipesTimer;

  boolean gameOver = false;

  double score =0;


  FlappyBird(){
    setPreferredSize(new Dimension(boardWidth, boardHeight));
    // setBackground(Color.blue);
    setFocusable(true);
    addKeyListener(this);

  //load images
  backgroundImg = new ImageIcon(getClass().getResource("./FlappyBirdBG.png")).getImage();

  birdImg = new ImageIcon(getClass().getResource("./flappyBird.png")).getImage();

  topPipeImg = new ImageIcon(getClass().getResource("./TopPipe.png")).getImage();

  bottomPipeImg = new ImageIcon(getClass().getResource("./bottomPipe.png")).getImage();

  //bird
  bird = new Bird(birdImg);
  pipes = new ArrayList<Pipe>();

  //place pipes timer

  placePipesTimer = new Timer(1500, new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e){
      placePipes();
    }
    
  });


  placePipesTimer.start();

  //game timer
gameLoop = new Timer(1000/60, this);

  gameLoop.start();
  

  }

  public void placePipes(){
    int randomPipeY = (int) (pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));
    int openingSpace = 200;
    Pipe topPipe = new Pipe(topPipeImg);
    topPipe.y = randomPipeY;
    pipes.add(topPipe);

    Pipe bottomPipe = new Pipe(bottomPipeImg);
    bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
    pipes.add(bottomPipe);
  }

  public void paintComponent(Graphics g){
    super.paintComponent(g);
    draw(g);
  }

  public void draw(Graphics g){
    //background
    g.drawImage(backgroundImg, 0, 0, boardWidth,boardHeight,null);

    //bird
    g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height,null);


     //pipes
  for(int i=0; i< pipes.size(); i++){
    Pipe pipe = pipes.get(i);
    g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
  }

   //score
 g.setColor(Color.white);
 g.setFont(new Font("Arial", Font.PLAIN, 32));
 if(gameOver){
  g.drawString("Game Over: " + String.valueOf((int) score), 10 , 35);

 }
 else{
  g.drawString(String.valueOf((int) score), 10 , 35);
 }

}




  public void move(){
    //bird
    velocityY += gravity;
    bird.y += velocityY;
    
    bird.y = Math.max(bird.y, 0);

    //pipes
  for(int i=0; i< pipes.size(); i++){
    Pipe pipe = pipes.get(i);
    pipe.x += velocityX;

    if(!pipe.passed && bird.x > pipe.x + pipe.width){
      pipe.passed = true;
      score +=0.5;
    }

    if(collision(bird, pipe)){
      gameOver = true;
    }
  }

  if(bird.y > boardHeight){
    gameOver = true;
  }

}
boolean collision(Bird b, Pipe p){
    return b.x + 10 < p.x + p.width - 10 &&
           b.x + b.width - 10 > p.x + 10 &&
           b.y + 5 < p.y + p.height - 5 &&
           b.y + b.height - 5 > p.y + 5;
}

 @Override
  public void actionPerformed(ActionEvent e) {
    move();
   repaint();
   if(gameOver){
    placePipesTimer.stop();
    gameLoop.stop();
   }
  }


  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_SPACE){
      velocityY = -9;
      if(gameOver){
        //restart the game by resetting the conditions
        bird.y = birdY;
        velocityY = 0;
        pipes.clear();
        score = 0;
        gameOver= false;
        gameLoop.start();
        placePipesTimer.start();
      }
    }
    
  }

  @Override
  public void keyTyped(KeyEvent e) {
   
  }


  @Override
  public void keyReleased(KeyEvent e) {
    
  }
}
