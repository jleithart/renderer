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
    static int windowHeight = 500;
    static int windowWidth = 500;
    //create 4 viewports;

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

    private static class Window
    {
        public double LeftX;
        public double RightX;
        public double TopY;
        public double BotY;
        public int Quadrant;

        private Window(double LX, double RX, double TY, double BT, int q){
            this.LeftX = LX;
            this.RightX = RX;
            this.TopY = TY;
            this.BotY = BT;
            this.Quadrant = q;
        }
    }


    //class for a pixel point.
    // must be integers
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

    static ViewPort[] arrayViewPort = new ViewPort[4];
    static Window[] WindowList = new Window[3];

    // window current position
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
        ShowViewport(g);
        ShowWindow(g);
        ExponentialGraph(g);
        DiscontinuityGraph(g);
        LoopyGraph(g);
        Branding(g);
     }

     /*
      * Initialization for the graphics engine
      * Creates a window into which all drawing operations are made.
      * Initializes default values
     */
     public static void InitGraphics(JFrame f){
        f.setSize( windowHeight, windowWidth);
        f.getContentPane().add( new Render() );
        f.setVisible( true );
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
        WindowList[2] = new Window(-0.5, 0.5, 0.5, -0.5, 3);//bottom right quadrant
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
                tmpCoordinate.SetCoords( (sizeX * windowPoint.x/sizeX)/sizeX,
                                         (sizeY/2 + (sizeY * windowPoint.y/sizeY))/sizeY);
                break;
            case 1: //will always be in quadrant 3
                sizeX = WindowList[windowIndex].RightX - WindowList[windowIndex].LeftX;
                sizeY = WindowList[windowIndex].TopY - WindowList[windowIndex].BotY;
                tmpCoordinate.SetCoords( (sizeX/2 + (sizeX * windowPoint.x/sizeX))/sizeX,
                                         (sizeY/2 + (sizeY * windowPoint.y/sizeY))/sizeY);
                break;
            case 2: //window 2 will always be in quadrant 4
                sizeX = WindowList[windowIndex].RightX - WindowList[windowIndex].LeftX;
                sizeY = WindowList[windowIndex].TopY - WindowList[windowIndex].BotY;
                tmpCoordinate.SetCoords( (sizeX/2 + (sizeX * windowPoint.x/sizeX))/sizeX,
                                         (sizeY/2 + (sizeY * windowPoint.y/sizeY))/sizeY);
                break;
        }
        return tmpCoordinate;
     }

     /*
     * Moves the starting position to the specified (x,y) location.
     * This is the window point
     */
     public static void MoveTo2D(int x_pos, int y_pos, int windowIndex){
        curPos.SetCoords(x_pos, y_pos);
     }

     /*
     * Draws from the last position to a specified (x,y) location.
     * Utilizes Java's draw line method
     * Last position is changed by MoveTo2D or DrawTo2D
     */
     public static void DrawTo2D(Graphics g, double x_pos, double y_pos, int windowIndex){

        g.drawLine((int)curPos.x, (int)curPos.y, (int)x_pos, (int)y_pos);
        curPos.SetCoords(x_pos, y_pos);
     }

     /*
      * Linear Interpolation
      * Finds the Y value given the known x and y values
     */
     public static int InterpolateY(int ViewIndex, int x_value){
        ViewPort view = arrayViewPort[ViewIndex];
        //int ret_val = (viewportminy + (max y - min y)((x_value - minx)/(maxx - minx)));
        int retval = 0;// (view.BotY + (view.TopY - view.BotY)*(x_value - view.LeftX)/(view.RightX - view.LeftX));
        //return ret_val;
        return retval;
     }

     public static int InterpolateX(int ViewIndex, int y_value){
        ViewPort view = arrayViewPort[ViewIndex];
        int retval = 0;//(view.LeftX + (view.RightX - view.LeftX)*(y_value - view.BotY)/(view.TopY - view.BotY));
        return retval;
     }

     /*
      * Converts the viewport coordinates to actual display frame coordinates
     */
     public static drawPoint ViewPortToFrameWindow(Point coordinate, int index){

        drawPoint tmpCoordinate = new drawPoint(0, 0);
        // frame upper left (0,0)
        // frame bottom right (500,500)
        // given a viewport coordinate, where should we draw it?
        switch(index){
            case 0:
                tmpCoordinate.SetCoords((int) (windowWidth/2 + (windowWidth/2 * coordinate.x)),
                                        (int) (windowHeight/2 - (windowHeight/2*coordinate.y)));
                break;
            case 1:
                tmpCoordinate.SetCoords((int) (0 + (windowWidth/2 * coordinate.x)),
                                        (int) (windowHeight/2 - (windowHeight/2*coordinate.y)));
                break;
            case 2:
                tmpCoordinate.SetCoords((int) (0 + (windowWidth/2 * coordinate.x)),
                                        (int) (windowHeight - (windowHeight/2*coordinate.y)));
                break;
            case 3:
                tmpCoordinate.SetCoords((int) (windowWidth/2 + (windowWidth/2 * coordinate.x)),
                                        (int) (windowHeight - (windowHeight/2*coordinate.y)));
                break;    
        }
        

        //first quadrant is 250,250 to 500, 0
        //second quadrant is 250,250, to 0, 0
        
        return tmpCoordinate;
     }

     /*
     * Displays the (x,y) viewport coordinates
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

     public static void LabelAxis(Graphics g, int window){
        Point first = new Point(0, 0);
        Point second = new Point(0, 0);

        Point firstVP;  //viewPort point
        Point secondVP; //viewPort point
        double sizeX = (WindowList[window].RightX + WindowList[window].LeftX);
        double inc = sizeX/10;

        for(double i = WindowList[window].LeftX; i < sizeX; i+=inc){
            first.SetCoords(i, inc/2);
         //   second.SetCoords(i, )

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
     }

     /*
     * Displays the (x,y) window coordinates
     */
     public static void ShowWindow(Graphics g){
        //draw all the axis
        for(int i = 0; i < 3; i++){
            DrawAxis(g, i);
        }
     }

     // drawn only in window 0
     public static void ExponentialGraph(Graphics g){
        //loop thorugh the function and draw it
        double x_value;
     }

     // drawn only in window 1
     public static void DiscontinuityGraph(Graphics g){
        //loop through the given function and draw it
     }

     //drawn only in window 2
     public static void LoopyGraph(Graphics g){
        //loop through the given function and draw it
     }

     public static void Branding(Graphics g){
        g.drawString("Jordan Leithart", 5, 10);
        g.drawString("CS 324", 5, 20);
        g.drawString("February 14, 2014", 5, 30);
        g.drawString("Assignment 2", 5, 40);
     }

}
