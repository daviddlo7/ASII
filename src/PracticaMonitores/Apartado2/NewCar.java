package PracticaMonitores.Apartado2;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;

//
// Class Intersection is the class that controls the intersection.
// All the synchronization should be made here.
//
// It simulates intersection using four methods:
//             (2 used to access intersection)
//             x2(car from northSouth direction or from eastWest Direction)
//
//xxxIN method:
// You must call this method before crossing the intersection. 
//
//xxxOUT method:
// You must call this method after crossing the intersection. 
//
//northSouthXX method:
// the car direction is  north-south.
//eastWestXX method:
//the car direction is  east-west
//
//The class Intersection must avoid:
//
//    * 2 cars crossing with different directions (collision).
//    
//
class Intersection{
    //Current direction
    //0 none
    // 1 N-S
    // -1 E-W
    final static int NS=1;
    final static int EW=-1;
    final static int NONE=0;


    /* Variables to use if you want */
    private int currentDirection=NONE; 
    //indicates: NONE=free crossing EW= East West direction only NS= North South direction only.
    private int carCrossing=0;//number of cars crossing 


    public void northSouthDirectionIN(){
    }

    public void northSouthDirectionOUT(){
    }
	
    
    public void eastWestDirectionIN(){
    }
	
   
    public void eastWestDirectionOUT(){
    }
    

}//End of class Intersection



//
// Class needs to inherit from Car where all the code is to draw the cars in
// the grid.
//
public class NewCar extends Car {
    //New variables:
    Intersection fInt,sInt;

    //Modified constructor 
    //
    //fInt: first intersection in car's route, 
    //for example: Car NS(West Street) (1-6) first intersection is North-West.(it[0]) 
    //             Car NS(West Street) (6-1) first intersection is South-West.(it[4])
    //sInt: second intersection in car's route, 
    //for example: Car West Street,(1-6) second intersection is South-West. (it[4])
    public NewCar(int pX,int pY, int inX, int inY, 
		  Intersection fInt, Intersection sInt){
    
	super( pX, pY, inX, inY);
	this.fInt=fInt;
	this.sInt=sInt;
    }
    
    //
    // Models the car movement in the grid.
    //
    
  public void run() {
    // Generate an infinite amount of cars
    while (true) {
      
      // Prepare car coordinates and increments
      setPosition();

      // Cross the first section
      move1();

      //Entering the first intersection
      //incrX show us car's direction if incrX=0 direction is northSouth
      
      if(incrX!=0){
	  
	  fInt.eastWestDirectionIN();
      }else {
	  fInt.northSouthDirectionIN();
      }

      // Cross the second section
      move2();

      //Leaving the first intersection
       if(incrX!=0){
	  fInt.eastWestDirectionOUT();
      }else {
	  fInt.northSouthDirectionOUT();
      }

      //
      // Cross the third section
      move3();

      //Entering the second intersection
      if(incrX!=0){//if car's direction is east West one
	  sInt.eastWestDirectionIN();
      }else { //else North South
	  sInt.northSouthDirectionIN();
      }
      // Cross the fourth section
      move4();
      //Leaving the second intesection
      if(incrX!=0){
	  sInt.eastWestDirectionOUT();
      }else {
	  sInt.northSouthDirectionOUT();
      }

      // Cross the fifth section
      move5();

      // Erase the car at the end of the street
      erase();

      // Wait to generate the next car
      try {
	sleep(rnd.nextInt(1000));
      } catch (InterruptedException e) {}
    }
  }

  //
  // Program to test the application. Eight objects are created and one thread
  // is started with each one of them. If the application is correctly coded,
  // the cars should not collide at the intersections.
  public static void main(String[] args) {
    Grid g;
    Car c;
    //NEW
    Intersection it[]=new Intersection[4]; //Intersection's vector.
    it[0]=new Intersection();// NW intersection 
    it[1]=new Intersection();// NE intersection 
    it[2]=new Intersection();// SE intersection 
    it[3]=new Intersection();// SW intersection 

    g = new Grid();
    

    // Vertical N-S cars
    c = new NewCar(Grid.ME +1, Grid.Margin , 0, 1,it[0] ,it[3]);//the same but including the intersections
    c.start();
    
    c = new NewCar(Grid.MESWSL +1,Grid.Margin, 0, 1, it[1],it[2]);
    c.start();
    
    // Vertical S-N cars
    c = new NewCar(Grid.ME -CarSize/2 -1  + Grid.StreetWidth , Grid.MESWSLSWE - CarSize, 0, -1,it[3],it[0]);
    c.start();
    
    c = new NewCar(Grid.MESWSL -CarSize/2 -1+ Grid.StreetWidth , Grid.MESWSLSWE - CarSize, 0, -1,it[2],it[1]);
    c.start();
    
    // Horizontal W-E Cars
    c = new NewCar(Grid.Margin, Grid.ME + 1, 1, 0,it[0],it[1]);
    c.start();
    
    c = new NewCar(Grid.Margin, Grid.MESWSL + 1, 1, 0,it[3],it[2]);
    c.start();
    
    // Horizontal E-W Cars
    c = new NewCar(Grid.MESWSLSWE - CarSize, Grid.ME +CarSize/2 - 1, -1, 0,it[1],it[0]);
    c.start();
    
    c = new NewCar(Grid.MESWSLSWE - CarSize, Grid.MESWSL+ CarSize/2  - 1, -1, 0,it[2],it[3]);
    c.start();
  } // End of main
} // End of class











