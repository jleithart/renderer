/*  Render.java
  * Programming assignment #2
  * CS 324 Bruce Bolden
  * Due February 14, 2014
  */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Render extends JComponent
{
    static int FRAME_HEIGHT = 500;
    static int FRAME_WIDTH = 500;


    // A viewport struct/class
    private static class ViewPort
    {
        public double LeftX;
        public double RightX;
        public double TopY;
        public double BotY;

        private ViewPort(double LX, double RX, double TY, double BT){
            this.LeftX = LX;
            this.RightX = RX;
            this.TopY = TY;
            this.BotY = BT;
        }

    }

    // the Window Struct/Class
    private static class Window
    {
        public double LeftX;
        public double RightX;
        public double TopY;
        public double BotY;
        public int Quadrant;    //which viewport is it in?

        private Window(double LX, double RX, double TY, double BY, int q){
            this.LeftX = LX;
            this.RightX = RX;
            this.TopY = TY;
            this.BotY = BY;
            this.Quadrant = q;
        }
    }


    // This is the class/struct for the Frame Point
    // This must be in integers to correlate to the pixels
    private static class drawPoint
    {
        public int x;
        public int y;
        private drawPoint(int iX, int iY){
            this.x = iX;
            this.y = iY;
        }

        public void SetCoords(int iX, int iY){
            this.x = iX;
            this.y = iY;
        }
    }

    //My point Class/Struct
    private static class Point
    {
        public double x;
        public double y;
        private Point(double iX, double iY){
            this.x = iX;
            this.y = iY;
        }

        public void SetCoords(double iX, double iY){
            this.x = iX;
            this.y = iY;
        }
    }

    // The 4 viewports that I need to make up the 4 quadrants on my frame
    static ViewPort[] arrayViewPort = new ViewPort[4];
    // 3 Windows that will have things drawn in them
    static Window[] WindowList = new Window[3];

    // The current position of the "pen"
    // This is in relation to the window
    static Point curPos = new Point(0, 0);



     public static void main( String[] args )
     {
        JFrame f = new JFrame( "Render" );
        InitGraphics(f);

        //  Exit application when the window is closed
         f.addWindowListener( new WindowAdapter() {
             public void windowClosing( WindowEvent e )
             {  System.exit(0); }
             }
         );

     }

     public void paintComponent( Graphics g )
     {
        //allow resizing over everything drawn
        FRAME_HEIGHT = getHeight();
        FRAME_WIDTH = getWidth();

        ShowViewport(g);        // draw the quadrilaterals for each viewport
        ShowWindow(g);          // draw the axes in each window

        ExponentialGraph(g);    // Plot the exponential graph
        DiscontinuityGraph(g);  // Plot the discontinuous graph
        LoopyGraph(g, 0.5);     // Plot the loopy graph where b = 0.5
        LoopyGraph(g, 1);       // Plot the loopy graph where b = 1.0
        LoopyGraph(g, 2);       // Plot the loopy graph where b = 2.0

        Branding(g);            // Label the Frame with my name
     }

     /*
      * Initialization for the graphics engine
      * Creates a window into which all drawing operations are made.
      * Initializes default values
     */
     public static void InitGraphics(JFrame f){
        f.setSize( FRAME_HEIGHT, FRAME_WIDTH);
        f.getContentPane().add( new Render() );
        f.setVisible( true );

        // set the values of each viewport
        SetViewport();
        SetWindow();

     }

     /*
      * Defines the viewport that will be used for subsequent drawing commands.
      */
     public static void SetViewport(){
        arrayViewPort[0] = new ViewPort(0, 1, 1, 0);    //first quadrant
        arrayViewPort[1] = new ViewPort(-1, 0, 1, 0);   //second quadrant
        arrayViewPort[2] = new ViewPort(-1, 0, 0, -1);  //third quadrant
        arrayViewPort[3] = new ViewPort(0, 1, 0, -1);   //fourth quadrant
     }

     /*
     * Defines the window coordinates that will be used.
     */

     public static void SetWindow()
     { 
        WindowList[0] = new Window(0, 3*3.14, 5, -5, 0);  // top right quadrant
        WindowList[1] = new Window(-9, 9, 10, -10, 2);  // bottom left quadrant
        WindowList[2] = new Window(-1.0, 1.0, 1.0, -1.0, 3);//bottom right quadrant
     }

     /*
     * Converts (x,y) window coordinates to (x,y) viewport coordinates
     */

     public static Point WindowToViewPort(Point windowPoint, int windowIndex){
        Point tmpCoordinate = new Point(0, 0);
        double sizeX;
        double sizeY;
        switch(windowIndex){
            case 0: //will always be in quadrant 1
                sizeX = WindowList[windowIndex].RightX - WindowList[windowIndex].LeftX;
                sizeY = WindowList[windowIndex].TopY - WindowList[windowIndex].BotY;
                tmpCoordinate.SetCoords( (windowPoint.x)/sizeX,
                                         (sizeY/2 + (windowPoint.y))/sizeY);
                break;
            case 1: //will always be in quadrant 3
                sizeX = WindowList[windowIndex].RightX - WindowList[windowIndex].LeftX;
                sizeY = WindowList[windowIndex].TopY - WindowList[windowIndex].BotY;
                tmpCoordinate.SetCoords( (sizeX/2 + (windowPoint.x))/sizeX,
                                         (sizeY/2 + (windowPoint.y))/sizeY);
                break;
            case 2: //window 2 will always be in quadrant 4
                sizeX = WindowList[windowIndex].RightX - WindowList[windowIndex].LeftX;
                sizeY = WindowList[windowIndex].TopY - WindowList[windowIndex].BotY;
                tmpCoordinate.SetCoords( (sizeX/2 + (windowPoint.x))/sizeX,
                                         (sizeY/2 + (windowPoint.y))/sizeY);
                break;
        }
        return tmpCoordinate;
     }

     /*
     * Moves the starting position to the specified (x,y) location.
     * This is a window point
     */
     public static void MoveTo2D(double x_pos, double y_pos){
        curPos.SetCoords(x_pos, y_pos);
     }

     /*
     * Draws from the last position to a specified (x,y) location.
     * Utilizes Java's draw line method
     * Last position is changed by MoveTo2D or DrawTo2D
     */
     public static void DrawTo2D(Graphics g, double x_pos, double y_pos, int windowIndex){

        Point firstPoint = new Point(curPos.x,curPos.y);
        Point secondPoint = new Point(x_pos, y_pos);

        // the translated point to viewpoint
        Point firstViewPoint;
        Point secondViewPoint;

        // the window point
        drawPoint drawFirstPoint;
        drawPoint drawSecondPoint;

        //Translate the window point to the viewpoint
        firstViewPoint = WindowToViewPort(firstPoint, windowIndex);
        secondViewPoint = WindowToViewPort(secondPoint, windowIndex);

        //now draw it
        drawFirstPoint = ViewPortToFrameWindow(firstViewPoint, WindowList[windowIndex].Quadrant);
        drawSecondPoint = ViewPortToFrameWindow(secondViewPoint, WindowList[windowIndex].Quadrant);
        g.drawLine(drawFirstPoint.x, drawFirstPoint.y, drawSecondPoint.x, drawSecondPoint.y);

        MoveTo2D(x_pos, y_pos);
     }

     /*
      * Converts the viewport coordinates to actual display frame coordinates
     */
     public static drawPoint ViewPortToFrameWindow(Point coordinate, int index){

        drawPoint tmpCoordinate = new drawPoint(0, 0);

        // given a viewport coordinate, find the pixel placement
        switch(index){
            case 0:
                // Top Right quadrant
                tmpCoordinate.SetCoords((int) (FRAME_WIDTH/2 + (FRAME_WIDTH/2 * coordinate.x)),
                                        (int) (FRAME_HEIGHT/2 - (FRAME_HEIGHT/2*coordinate.y)));
                break;
            case 1:
                // Top Left Quadrant
                tmpCoordinate.SetCoords((int) (0 + (FRAME_WIDTH/2 * coordinate.x)),
                                        (int) (FRAME_HEIGHT/2 - (FRAME_HEIGHT/2*coordinate.y)));
                break;
            case 2:
                // Bottom Left quadrant
                tmpCoordinate.SetCoords((int) (0 + (FRAME_WIDTH/2 * coordinate.x)),
                                        (int) (FRAME_HEIGHT - (FRAME_HEIGHT/2*coordinate.y)));
                break;
            case 3:
                // Bottom Left qudrant
                tmpCoordinate.SetCoords((int) (FRAME_WIDTH/2 + (FRAME_WIDTH/2 * coordinate.x)),
                                        (int) (FRAME_HEIGHT - (FRAME_HEIGHT/2*coordinate.y)));
                break;    
        }
        

        
        return tmpCoordinate;
     }

     /*
     * Draws a rectangle around each viewport
     */
     public static void ShowViewport(Graphics g){
        //draws the viewports
        drawPoint drawFirstPoint;
        drawPoint drawSecondPoint;
        Point viewPointBL = new Point(0, 0);    //point in the bottom left of the viewport
        Point viewPointTR = new Point(1, 1);    //point in the top right of the viewport
        for(int i = 0; i < 4; i++){
            drawFirstPoint = ViewPortToFrameWindow(viewPointBL, i);
            drawSecondPoint = ViewPortToFrameWindow(viewPointTR, i);

            //draw counter clockwise from the bottom left of the viewport
            g.drawLine(drawFirstPoint.x, drawFirstPoint.y, drawSecondPoint.x, drawFirstPoint.y);
            g.drawLine(drawSecondPoint.x, drawFirstPoint.y, drawSecondPoint.x, drawSecondPoint.y);
            g.drawLine(drawSecondPoint.x, drawSecondPoint.y, drawFirstPoint.x, drawSecondPoint.y);
            g.drawLine(drawFirstPoint.x, drawSecondPoint.y, drawFirstPoint.x, drawFirstPoint.y);
        }

     }

     /*
     * Draws the axis for the window passed in
     */
     public static void DrawAxis(Graphics g, int window)
     {
        Point firstAxisPoint = new Point(0,0);
        Point secondAxisPoint = new Point(0,0);
        Point firstViewPoint;
        Point secondViewPoint;

        drawPoint drawFirstPoint;
        drawPoint drawSecondPoint;

        //x-axis
        firstAxisPoint.SetCoords(WindowList[window].LeftX, (WindowList[window].BotY + WindowList[window].TopY)/2);
        secondAxisPoint.SetCoords(WindowList[window].RightX, (WindowList[window].BotY + WindowList[window].TopY)/2);

        firstViewPoint = WindowToViewPort(firstAxisPoint, window);
        secondViewPoint = WindowToViewPort(secondAxisPoint, window);


        drawFirstPoint = ViewPortToFrameWindow(firstViewPoint, WindowList[window].Quadrant);
        drawSecondPoint = ViewPortToFrameWindow(secondViewPoint, WindowList[window].Quadrant);
        g.drawLine(drawFirstPoint.x, drawFirstPoint.y, drawSecondPoint.x, drawSecondPoint.y);
        //end x-axis

        //y-axis
        // the 1st quadrant has no negative axis
        if(window != 0){
            firstAxisPoint.SetCoords((WindowList[window].LeftX + WindowList[window].RightX)/2, WindowList[window].BotY);
            secondAxisPoint.SetCoords((WindowList[window].LeftX + WindowList[window].RightX)/2, WindowList[window].TopY);
        }
        else{
            firstAxisPoint.SetCoords(WindowList[window].LeftX, WindowList[window].BotY);
            secondAxisPoint.SetCoords(WindowList[window].LeftX, WindowList[window].TopY);
        }

        firstViewPoint = WindowToViewPort(firstAxisPoint, window);
        secondViewPoint = WindowToViewPort(secondAxisPoint, window);


        drawFirstPoint = ViewPortToFrameWindow(firstViewPoint, WindowList[window].Quadrant);
        drawSecondPoint = ViewPortToFrameWindow(secondViewPoint, WindowList[window].Quadrant);
        g.drawLine(drawFirstPoint.x, drawFirstPoint.y, drawSecondPoint.x, drawSecondPoint.y);
        //end y axis
     }

     /*
     * Draws the axis for each window
     */
     public static void ShowWindow(Graphics g){
        //draw all the axis
        for(int i = 0; i < 3; i++){
            DrawAxis(g, i);
        }
     }

     /*
     * Draws the exponential graph 
     * This plot takes place only in quadrant 1, window-0
     */
     public static void ExponentialGraph(Graphics g){
        //loop thorugh the function and draw it
        curPos.SetCoords(0, ExponentFunction(0));
        int dotted = 0;
        double xVal = WindowList[0].LeftX;
        double xInc = 4.0/100;
        MoveTo2D(xVal, ExponentFunction(xVal));
        for(int i = 0; i < 1000; i++){
            // move the pen every two times
            if(dotted % 2 == 1){
                MoveTo2D(xVal, ExponentFunction(xVal));
            }
            else{
                DrawTo2D(g, xVal, ExponentFunction(xVal), 0);
            }
            dotted++;
            xVal += xInc;

        }
     }

     /*
     * Returns the y-value for the function given
     */
     public static double ExponentFunction(double inValue)
     {
        return 4.0*Math.exp(-0.25*inValue)*Math.cos(4*inValue);
     }

     /*
     * Draws the Discontinuous graph
     * This plot is drawn only in quadrant 3, window-1
     */
     public static void DiscontinuityGraph(Graphics g){
        //loop through the given function and draw it
        int dotted = 0;
        double xVal = WindowList[1].LeftX;
        double xInc = 4.0/100;
        double yVal = DiscontinuityFunction(xVal);
        MoveTo2D(xVal, yVal);
        for(int i = 0; i < 1000; i++){
            yVal = DiscontinuityFunction(xVal);

            if(yVal <= WindowList[1].TopY && yVal >= WindowList[1].BotY){
                DrawTo2D(g, xVal, yVal, 1);
            }
            else{
                if(yVal > WindowList[1].TopY) yVal = WindowList[1].TopY;
                else if(yVal < WindowList[1].BotY) yVal = WindowList[1].BotY;
                MoveTo2D(xVal, yVal);
            }
            xVal += xInc;
            if(xVal > WindowList[1].RightX) break;
        }
     }

     /*
     * Returns the y value for the function given
     * If the y-value is outside the bounds of the window, return the window point
     */
     public static double DiscontinuityFunction(double inValue)
     {
        double retval = 2/(0.5 - Math.sin(inValue/2));
        return retval;
     }

     /*
     * Draws the Loopy graph for some constant given
     * This plot is drawn only in quadrant 4, window-2
     */
     public static void LoopyGraph(Graphics g, double bValue){
        double LoopValue = LoopyFunction(WindowList[2].LeftX, bValue);
        MoveTo2D(WindowList[2].LeftX, LoopValue);

        Color lineColor;

        // change the color depending on which loop it is
        if(bValue == 0.5){
            lineColor = Color.red;
        }
        else if(bValue == 1.0){
            lineColor = Color.green;
        }
        else{
            lineColor = Color.blue;
        }

        g.setColor(lineColor);

        // draw in the positive Y-axis
        for(double i = WindowList[2].LeftX; i < WindowList[2].RightX; i+=0.01){            

            LoopValue = LoopyFunction(i, bValue);
            if(!Double.isNaN(LoopValue)){
                if(LoopValue < 0){
                    LoopValue = -Math.sqrt(LoopValue);
                }
                else{
                    LoopValue = Math.sqrt(LoopValue);
                }
                if(LoopValue > WindowList[2].BotY && LoopValue < WindowList[2].TopY){

                    //positive y
                    DrawTo2D(g, i, LoopValue, 2);

                }
                else{
                    MoveTo2D(i, 0);
                }
            }
        }   //end positive Y

        // Begin negative Y-axis
        LoopValue = LoopyFunction(WindowList[2].LeftX, bValue);
        MoveTo2D(WindowList[2].LeftX, -LoopValue);
        for(double i = WindowList[2].LeftX; i < WindowList[2].RightX; i+=0.01){
            LoopValue = LoopyFunction(i, bValue);
            if(!Double.isNaN(LoopValue)){
                if(LoopValue < 0){
                    LoopValue = -Math.sqrt(LoopValue);
                }
                else{
                    LoopValue = Math.sqrt(LoopValue);
                }
                if(LoopValue > WindowList[2].BotY && LoopValue < WindowList[2].TopY){

                    //positive y
                    DrawTo2D(g, i, -LoopValue, 2);

                }
                else{
                    MoveTo2D(i, 0);
                }
            }
        }

     }

     /*
     *  Returns the square of the value calculated by the function
     */

     public static double LoopyFunction(double inValue, double bValue)
     {
        double retval = (bValue*(1.0)*(1.0)*(inValue*inValue*inValue) + (0.5*1.0*1.0*inValue*inValue));
        return retval;
     }

     /*
     * Draws my name, class, date, and assignment #
     * Drawn in Quadrant 1, Top Left corner
     */
     public static void Branding(Graphics g){
        g.setColor(Color.black);
        g.drawString("Jordan Leithart", 5, 10);
        g.drawString("CS 324", 5, 20);
        g.drawString("February 14, 2014", 5, 30);
        g.drawString("Assignment 2", 5, 40);
     }

}
