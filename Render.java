/*  Render.java
  * Programming assignment #2
  * CS 324 Bruce Bolden
  * Due February 14, 2014
  */

import java.awt.Graphics;
import javax.swing.*;

public class Render extends JComponent
{
     public static void main( String[] args )
     {
         InitGraphics();
     }

     public void paintComponent( Graphics g )
     {
         g.drawString( "Hello, Java!", 100, 100 );
     }

     /*
      * Initialization for the graphics engine
      * Creates a window into which all drawing operations are made.
      * Initializes default values
     */
     public static void InitGraphics(){
        JFrame f = new JFrame( "Render" );
        f.setSize( 800, 800 );
        f.getContentPane().add( new Render() );
        f.setVisible( true );

     }

     /*
      * Defines the viewport that will be used for subsequent drawing commands.
      */
     public static void SetViewport(){

     }

     /*
     * Defines the window coordinates that will be used.
     */

     public static void SetWindow(){
     }

     /*
     * Converts (x,y) window coordinates to (x,y) viewport coordinates
     */

     public static void WindowToViewPort(){

     }

     /*
     * Moves the starting position to the specified (x,y) location.
     */
     public static void MoveTo2D(int x_pos, int y_pos){

     }

     /*
     * Draws from the last position to a specified (x,y) location.
     * Utilizes Java's draw line method
     * Last position is changed by MoveTo2D or DrawTo2D
     */
     public static void DrawTo2D(int x_pos, int y_pos){


     }

     /*
      * Linear Interpolation
      * Finds the Y value given the known x and y values
     */
     public static void Interpolate(int x_pos, int y_pos){


     }

     /*
      * Converts the viewport coordinates to actual display frame coordinates
     */
     public static void ViewPortToFrameWindow(){

     }

     /*
     * Displays the (x,y) viewport coordinates
     */
     public static void ShowViewport(){

     }

     /*
     * Displays the (x,y) window coordinates
     */
     public static void ShowWindow(){

     }
}
