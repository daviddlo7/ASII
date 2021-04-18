package PracticaMonitores.Apartado3;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;

class Grid extends JPanel {
  // Table Dimensions

  // Separation between window border and the beginning of the street.
  static final int Margin = 45;

  // Street length before and after the intersection
  static final int Entry = 45;

  // Street Width
  static final int StreetWidth = 30;

  // Street length between intersections
  static final int StreetLength = 150;

  // Factors to accumulate the distances
  static final int M = Margin - 1;
  static final int ME = Margin + Entry - 1;
  static final int MESW = ME + StreetWidth - 1;
  static final int MESWSL = MESW + StreetLength - 1;
  static final int MESWSLSW = MESWSL + StreetWidth - 1;
  static final int MESWSLSWE = MESWSLSW + Entry - 1;
  static final int WindowSize = MESWSLSWE + Margin;

  // Insets
  static int XInset;
  static int YInset;

  // Object to use to perform the drawing operations
  static Graphics graphics;
  static JFrame frame;

  // Constructor. Creates the window and the panel.
  public Grid() {
    // Objects to hold the graphics
    Container content;
    Random rnd;
    frame = new JFrame("Traffic Jam");

    // Add to the frame a method to execute when the window closes. Note the
    // way an object can be defined and initialized in the middle of a method
    // invocation.
    frame.addWindowListener(new WindowAdapter() {
	public void windowClosing(WindowEvent e) {
	  System.exit(0);
	}
      });

    // Get the container of the graphical objects
    content = frame.getContentPane();

    // Create the panel and set its size.
    setPreferredSize(new Dimension(WindowSize, WindowSize));

    // Set the backgroun color for the table
    setBackground(Color.white);
    setForeground(Color.black);

    // Set the position of the table in the frame.
    content.add(this, BorderLayout.CENTER);


    // Make the frame show on the screen.
    frame.pack();
    frame.setVisible(true);

    // Get the graphics object to perform the draw operations
    graphics = frame.getGraphics();

    Insets is = frame.getInsets();
    XInset= is.left;
    YInset= is.top;
    // There is no need to call explicitly the painting method. It is called by
    // the program, since the component has to be drawn at least once.
  }

  // Method to draw the streets and crossings. It is all based on the
  // dimensions given as constants.
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    g.setColor(getForeground());

    // Draw the different lines that make the crossing
    g.drawPolyline(new int[] {ME, ME, M}, 
		   new int[] {M, ME, ME}, 3);
    g.drawPolyline(new int[] {MESW, MESW, MESWSL, MESWSL}, 
		   new int[] {M, ME, ME, M}, 4);
    g.drawPolyline(new int[] {MESWSLSW, MESWSLSW, MESWSLSWE}, 
		   new int[] {M, ME, ME}, 3);
    g.drawPolyline(new int[] {M, ME, ME, M}, 
		   new int[] {MESW, MESW, MESWSL, MESWSL}, 4);

    // This is the inner square
    g.drawPolygon(new int[] {MESW, MESWSL, MESWSL, MESW}, 
		  new int[] {MESW, MESW, MESWSL, MESWSL}, 4);

    g.drawPolyline(new int[] {MESWSLSWE, MESWSLSW, MESWSLSW, MESWSLSWE}, 
		   new int[] {MESW, MESW, MESWSL, MESWSL}, 4);
    g.drawPolyline(new int[] {M, ME, ME}, 
		   new int[] {MESWSLSW, MESWSLSW, MESWSLSWE}, 3);
    g.drawPolyline(new int[] {MESW, MESW, MESWSL, MESWSL}, 
		   new int[] {MESWSLSWE, MESWSLSW, MESWSLSW, MESWSLSWE}, 
		   4);
    g.drawPolyline(new int[] {MESWSLSW, MESWSLSW, MESWSLSWE}, 
		   new int[] {MESWSLSWE, MESWSLSW, MESWSLSW}, 3);

    // Restore the color to background
    g.setColor(getBackground());
  }

}



public class Car extends Thread {

  // Minimun delay between repaints
  static final int MinDelay = 0;

  // Maximum Speed 
  static final int Speed = 5;

  // Random Number Generator
  static Random rnd = new Random();

  // Car Length
  static final int CarSize = Grid.StreetWidth -1;

  // Object to use to perform the drawing operations
  static Graphics graphics;

  // Initial coordinates of the car
  int initPosX;
  int initPosY;

  // Current coordinates of the car
  int posX;
  int posY;

  // Current increase on coordinates
  int incrX;
  int incrY;

  // Delay to make the movement visible.
  int delay;

  // Need this empty constructor to be able to inherit from this class
  public Car() {
  }

  // Constructor to initialize the relevant fields. Only the sign of the two
  // last paramenters are considered, since the increase has to be either 1 or
  // -1
  public Car(int pX, int pY, int inX, int inY) {
    initPosX = pX;
    initPosY = pY;
    
    // Set the value of incrX
    incrX = 0;
    if (inX > 0) {
      incrX = 1;
    }
    if (inX < 0) {
      incrX = -1;
    }

    // Set the value of incrY
    incrY = 0;
    if (inY > 0) {
      incrY = 1;
    }
    if (inY < 0) {
      incrY = -1;
    }
  }

  // Method to generate cars traversing the street. The traversal is divided
  // into five portions:
  // 1.- The street portion before the first crossing,
  // 2.- the first crossing,
  // 3.- the street between the two crossings,
  // 4.- the second crossing, and
  // 5.- The street portion after the second crossing
  //
  // As a last step, the car needs to be erased from the end of the street.
  //
  public void run() {
    // Generate an infinite amount of cars
    while (true) {
      
      // Prepare car coordinates and increments
      setPosition();

      move1();

      move2();

      move3();

      move4();

      move5();

      erase();

      // Wait to generate the next car
      try {
	sleep(rnd.nextInt(1000));
      } catch (Exception e) {}
    }
  }

  // Move car accross the first portion
  public void move1() {
    if (incrX != 0) {
      if (incrX > 0) { // Car moving W-E
	while (posX + incrX < (Grid.ME - CarSize)) {
	  move();
	}
      }
      else { // Car moving E-W
	while (posX + incrX >= Grid.MESWSLSW) {
	  move();
	}
      } // End of negative incrX
    } // End of incrX != 0

    if (incrY != 0) {
      if (incrY > 0) { // Car moving N-S
	while (posY + incrY < (Grid.ME - CarSize)) {
	  move();
	}
      }
      else { // Car moving S-N
	while (posY + incrY >= Grid.MESWSLSW) {
	  move();
	}
      } // End of negative incrY
    } // End of incrY != 0
  }

  // Move car accross the second portion
  public void move2() {
    if (incrX != 0) {
      if (incrX > 0) { // Car moving W-E
	while (posX + incrX <= Grid.MESW) {
	  move();
	}
      }
      else { // Car moving E-W
	while (posX + incrX > (Grid.MESWSL - CarSize)) {
	  move();
	}
      } // End of negative incrX
    } // End of incrX != 0

    if (incrY != 0) {
      if (incrY > 0) { // Car moving N-S
	while (posY + incrY <= Grid.MESW) {
	  move();
	}
      }
      else { // Car moving S-N
	while (posY + incrY > (Grid.MESWSL - CarSize)) {
	  move();
	}
      } // End of negative incrY
    } // End of incrY != 0
  }

  // Move car accross the third portion
  public void move3() {
    if (incrX != 0) {
      if (incrX > 0) { // Car moving W-E
	while (posX + incrX < (Grid.MESWSL - CarSize)) {
	  move();
	}
      }
      else { // Car moving E-W
	while (posX + incrX >= Grid.MESW) {
	  move();
	}
      } // End of negative incrX
    } // End of incrX != 0

    if (incrY != 0) {
      if (incrY > 0) { // Car moving N-S
	while (posY + incrY < (Grid.MESWSL - CarSize)) {
	  move();
	}
      }
      else { // Car moving S-N
	while (posY + incrY >= Grid.MESW) {
	  move();
	}
      } // End of negative incrY
    } // End of incrY != 0
  }

  // Move car accross the fourth portion
  public void move4() {
    if (incrX != 0) {
      if (incrX > 0) { // Car moving W-E
	while (posX + incrX <= Grid.MESWSLSW) {
	  move();
	}
      }
      else { // Car moving E-W
	while (posX + incrX > (Grid.ME - CarSize)) {
	  move();
	}
      } // End of negative incrX
    } // End of incrX != 0

    if (incrY != 0) {
      if (incrY > 0) { // Car moving N-S
	while (posY + incrY <= Grid.MESWSLSW) {
	  move();
	}
      }
      else { // Car moving S-N
	while (posY + incrY > (Grid.ME - CarSize)) {
	  move();
	}
      } // End of negative incrY
    } // End of incrY != 0
  }

  // Move car accross the fifth portion
  public void move5() {
    if (incrX != 0) {
      if (incrX > 0) { // Car moving W-E
	while (posX + incrX < (Grid.MESWSLSWE - CarSize)) {
	  move();
	}
      }
      else { // Car moving E-W
	while (posX + incrX >= Grid.Margin) {
	  move();
	}
      } // End of negative incrX
    } // End of incrX != 0

    if (incrY != 0) {
      if (incrY > 0) { // Car moving N-S
	while (posY + incrY < (Grid.MESWSLSWE - CarSize)) {
	  move();
	}
      }
      else { // Car moving S-N
	while (posY + incrY >= Grid.Margin) {
	  move();
	}
      } // End of negative incrY
    } // End of incrY != 0
  }

  // Initialize the car movement
  public void setPosition() {
    posX = initPosX;
    posY = initPosY;
    
    // Select a new speed
    delay = MinDelay + rnd.nextInt(25);
  }

  // Method in charge of moving the car one unit in the appropriate direction
  public void move() {
    
    erase();

    posX += incrX;
    posY += incrY;

    if (posX < Grid.Margin || posX > (Grid.MESWSLSWE - CarSize) ||
	posY < Grid.Margin || posY > (Grid.MESWSLSWE - CarSize)) {
      return;
    }

    paint();

    // Wait the delay
    try {
      sleep(delay);
    } catch (Exception e) {}
  }
  
  // Method to paint the car
  protected void paint() {
    Graphics g;
    Color c;

    g = Grid.graphics;

    // Draw the car
    synchronized (g) {
      c = g.getColor();
      g.setColor(Color.black);
      if (incrX==0){
	  g.fillPolygon(new int [] {posX + Grid.XInset, 
				posX + CarSize/2 + Grid.XInset, 
				posX + Grid.XInset + CarSize/2, 
				posX + Grid.XInset},
		    new int [] {posY + Grid.YInset, 
				posY + Grid.YInset, 
				posY + CarSize + Grid.YInset, 
				posY + CarSize + Grid.YInset},
		    4);
      g.setColor(c);
      }
      else{
	  g.fillPolygon(new int [] {posX + Grid.XInset, 
				posX + CarSize + Grid.XInset, 
				posX + Grid.XInset + CarSize, 
				posX + Grid.XInset},
		    new int [] {posY + Grid.YInset, 
				posY + Grid.YInset, 
				posY + CarSize/2 + Grid.YInset, 
				posY + CarSize/2 + Grid.YInset},
		    4);
      g.setColor(c);	  
      }

    }
    
  }

  // Method to erase the car (paint it in white)
  protected void erase() {
    Graphics g;
    Color c;

    g = Grid.graphics;

    // Erase the car
    synchronized (g) {
      c = g.getColor();
      g.setColor(Color.white);
      if (incrX==0){
      g.fillPolygon(new int [] {posX + Grid.XInset, 
				posX + CarSize/2 + Grid.XInset, 
				posX + CarSize/2 + Grid.XInset, 
				posX + Grid.XInset},
		    new int [] {posY + Grid.YInset, 
				posY + Grid.YInset, 
				posY + CarSize + Grid.YInset, 
				posY + CarSize + Grid.YInset},
		    4);
      }else{
      g.fillPolygon(new int [] {posX + Grid.XInset, 
				posX + CarSize + Grid.XInset, 
				posX + CarSize + Grid.XInset, 
				posX + Grid.XInset},
		    new int [] {posY + Grid.YInset, 
				posY + Grid.YInset, 
				posY + CarSize/2 + Grid.YInset, 
				posY + CarSize/2 + Grid.YInset},
		    4);  
      }


      g.setColor(c);
    }
  }
  
  // Main method to test the application without any kind of traffic control.
  // Eight car objects are created, each of them with the appropiate
  // coordinates and directions. There is no need to keep a reference to all
  // these objects, since there won't be any more reference to them from the
  // main.
  public static void main(String[] args) {
    Grid g;
    Car c;

    g = new Grid();

    // Vertical N-S cars
    c = new Car(Grid.ME + 1, Grid.Margin, 0, 1);
    c.start();
    
    c = new Car(Grid.MESWSL + 1, Grid.Margin, 0, 1);
    c.start();
    
    // Vertical S-N cars
    c = new Car(Grid.ME - 1+CarSize/2, Grid.MESWSLSWE - CarSize, 0, -1);
    c.start();
    
    c = new Car(Grid.MESWSL - 1+CarSize/2, Grid.MESWSLSWE - CarSize, 0, -1);
    c.start();
    
    // Horizontal W-E Cars
    c = new Car(Grid.Margin , Grid.ME + CarSize/2 -1, 1, 0);
    c.start();
    
    c = new Car(Grid.Margin , Grid.MESWSL  + CarSize/2 -1, 1, 0);
    c.start();
    
    // Horizontal E-W Cars
    c = new Car(Grid.MESWSLSWE - CarSize, Grid.ME + 1, -1, 0);
    c.start();
    
    c = new Car(Grid.MESWSLSWE - CarSize, Grid.MESWSL + 1, -1, 0);
    c.start();
  }

}
