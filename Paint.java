
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * A GUI that represents a painting program, where the user can change the colour and 
 * thickness of the line being drawn, erase what has been drawn and clear the screen. 
 * 
 * @author Lilit Mesropyan
 * @version April 2018
 */
class Paint
{
    public static void main (String [] args)
    {
        // create a frame 
        DrawingFrame frame = new DrawingFrame();

        // terminate the program when this frame is closed:
        frame.addWindowListener(new WindowAdapter()
            {
                public void windowClosing(WindowEvent e)
                {
                    System.exit(0);
                }
            }
        );

    }
}

/**
 * The frame where Every part of the drawing program is added. 
 */
class DrawingFrame extends JFrame implements ActionListener
{
    // instance variables - varaiabels that we need other methods to have access to
    private JPanel pane; // the main pane 
    private DrawPane drawing; // the drawing pane
    private JTextField thickness; // the TextField where the user can input the thickness of the line 

    /**
     * Creates a Drawing Frame with a drawing panel, and setting in the bottom that change the 
     * color and thickness of the line drawn, erase the line drawn, and clear the screen.
     */
    public DrawingFrame()
    {
        // use the JFrame (DrawingFrame's super class) constructor and pass it the name that will 
        // appear on the title bar along the window
        super("Drawing");

        // assign pane to reference to this frame’s ContentPane
        pane = (JPanel)getContentPane();
        // set Border Layout 
        pane.setLayout(new BorderLayout());

        // add a drawing pane to the center of the main pane
        drawing = new DrawPane();
        pane.add(drawing, BorderLayout.CENTER);
        // set the background to white
        pane.setBackground(Color.WHITE);

        // add settings to bottom:
        JPanel settings = new JPanel(); // settings panel 
        settings.setLayout(new GridLayout(1,7)); // grid layout (with 7 blocks)
        // add buttons that will change the color, and erase button:
        JButton black = new JButton("black");
        JButton red = new JButton("red");
        JButton blue = new JButton("blue");
        JButton green = new JButton("green");
        JButton erase = new JButton("erase");
        // add TextField with a label where the user can enter the thickness of the line drawn:
        JLabel thickLabel = new JLabel("Thickness:");
        thickness = new JTextField(3);
        thickness.setText("5"); // initally set it to 5
        // add button that will clear the screen:
        JButton clear = new JButton("clear");
        // regersiter the DrawingFrame to listen to each of the buttons and the text field
        black.addActionListener(this);
        red.addActionListener(this);
        blue.addActionListener(this);
        green.addActionListener(this);
        erase.addActionListener(this);
        thickness.addActionListener(this);
        clear.addActionListener(this);
        // add each button, label, and text field to the settings pane (in order)
        settings.add(black);
        settings.add(red);
        settings.add(blue);
        settings.add(green);
        settings.add(erase);
        settings.add(thickLabel);
        settings.add(thickness);
        settings.add(clear);
        // add the settings pane to the main pane
        pane.add(settings, BorderLayout.SOUTH);

        // set the size and make the frame visible 
        setSize(600,400);
        setVisible(true);
    }

    /**
     * Decides what to do when any of the buttons are pressed, or a value is entered in a textfield. 
     * 
     * @param e supplied by Java 
     */
    public void actionPerformed (ActionEvent e)
    {
        // if a color changing button is pressed, call the drawing class's method for adding a 
        // new color to draw
        if (e.getActionCommand().equals("black"))
            drawing.addColor(1);

        else if (e.getActionCommand().equals("red"))
            drawing.addColor(2);

        else if (e.getActionCommand().equals("blue"))
            drawing.addColor(3);

        else if (e.getActionCommand().equals("green"))
            drawing.addColor(4);

        // do the same thing for erasing: draw with white 
        // (the same color as the background)
        else if (e.getActionCommand().equals("erase"))
            drawing.addColor(0);

        // if clear screen button is pressed call the drawing class's method for clearing the screen
        else if (e.getActionCommand().equals("clear"))

            drawing.clearScreen();

        // call the drawing class's method for adding a new radius to draw when a value is entered 
        // in the thickness textfield
        drawing.addRadius(Integer.parseInt(thickness.getText().trim()));
    }
}

/**
 * The drawing pane, where the user can  draw a shape.
 */
class DrawPane extends JComponent implements MouseMotionListener
{
    // the x and y coordinates of the circles that are being drawn (when clicking/dragging):
    private int [] xs;
    private int [] ys;
    // whether or not we need to clear the screen:
    private boolean clearScreen;
    // colors[0] is the coordinate/point number (ex 3rd x/y coordinate) where the color changes, 
    // colors[1] is the color it changes to (represented by an int):
    private int [][] colors; 
    // radius[0] is the coordinate/point number where the radius changes
    // radius[1] is the radius it changes to:
    private int [][] radius;

    /**
     * Creates a drawing pane, where the user can drag the mouse or click to paint. 
     */
    public DrawPane ()
    {
        // register this pane to listen to mouse events
        addMouseMotionListener(this);
        // register this pane to listen to mouse events for the new ClickHandler class
        addMouseListener(new ClickHandler());

        // initialize x and y coordinates, initialy set length to 0 (no point drawn yet)
        xs = new int[0];
        ys = new int[0];

        // initialize colours and radius, initial length[2][1]
        // (2 rows: 1 for location other for radius/color, 1 coloumn: only 1 radius/color initially) 
        colors = new int[2][1];
        radius = new int[2][1];
        // start at 1st coordinate
        colors[0][0] = 1;
        radius[0][0] = 1;
        // set initial color / radius 
        colors[1][0] = 1; // black (1 represents black)
        radius[1][0] = 5; // 5 
    }

    /**
     * Adds a new color to the colors array, with the coordinate number that it starts at.
     * 
     * @param color an integer that represents the new color to add 
     * (black - 1, red - 2, blue - 3, green - 4, white - 0)
     */
    public void addColor (int color)
    {
        // create a new 2D array that has an extra column 
        int [][]newColors = new int[2][colors[0].length + 1];
        // copy everything from the old array to the new one 
        System.arraycopy(colors[0], 0, newColors[0], 0, colors[0].length);
        System.arraycopy(colors[1], 0, newColors[1], 0, colors[1].length);
        // add the coordinate number, which is the length of the xs or ys array
        newColors[0][newColors[0].length-1] = xs.length;
        // add the new color as an int
        newColors[1][newColors[0].length-1] = color;
        // make the old array point to the new one 
        colors = newColors;
    }

    /**
     * Returns the color that the integer represents.
     * (black - 1, red - 2, blue - 3, green - 4, white - 0)
     * 
     * @param color the integer that represent a color 
     * @return the color (variable type Color) that the integer represents
     */
    private Color getColor(int color)
    {
        if (color == 1) 
            return Color.BLACK;
        else if (color == 2)
            return Color.RED;
        else if (color == 3)
            return Color.BLUE;
        else if (color == 4)
            return Color.GREEN;
        else // color == 0 erase
            return Color.WHITE; 
    }

    /**
     * Adds a new radius to the radius array, with the coordinate number that it starts at.
     * 
     * @param rad the new radius / thickness to add 
     */
    public void addRadius (int rad)
    {
        // create a new 2D array that has an extra column 
        int [][]newRadius = new int[2][radius[0].length + 1];
        // copy everything from the old array to the new one 
        System.arraycopy(radius[0], 0, newRadius[0], 0, radius[0].length);
        System.arraycopy(radius[1], 0, newRadius[1], 0, radius[1].length);
        // add the coordinate number, which is the length of the xs or ys array
        newRadius[0][newRadius[0].length-1] = xs.length;
        // add the new radius / thickness 
        newRadius[1][newRadius[0].length-1] = rad;
        // make the old array point to the new one 
        radius = newRadius;
    }

    /**
     * Clear the DrawPane of any drawings. This is done by drawing a rectangle of the same color 
     * as the background overtop the drawing and resetting the x/y coordinates, colors and radius
     * arrays. 
     */
    public void clearScreen()
    {
        // we now have to clear the screen
        clearScreen = true;

        // reinitialize x/y values of circles
        xs = new int[0];
        ys = new int[0];

        // reinitialize color array 
        int oldColor = colors[1][colors[0].length-1]; // the old color 
        colors = new int[2][1];
        colors[0][0] = 0;
        colors[1][0] = oldColor; // keep the old color

        // reinitialize radius array
        int oldRadius = radius[1][radius[0].length-1]; // the old radius 
        radius = new int[2][1];
        radius[0][0] = 0;
        radius[1][0] = oldRadius; // keep the old radius

        // call repaint method, that will paint the white rectangle
        repaint();
    }

    /**
     * Decides what to do when the mouse is clicked on the pane. 
     */
    class ClickHandler extends MouseAdapter
    {
        /**
         * When the mouse is clicked, a circle of the set radius and color is drawn at the 
         * x/y coordinate where the mouse was clicked. 
         * 
         * @param e supplied by Java
         */
        public void mouseClicked(MouseEvent e)
        {
            // since we have clicked on the screen, we no longer need to clear the screen
            clearScreen = false;

            // create news x/y arrays, with one extra index and 
            // copy the contents of the old array to the new one 
            int [] newXs = new int[xs.length + 1];
            System.arraycopy(xs, 0, newXs, 0, xs.length);
            int [] newYs = new int[ys.length + 1];
            System.arraycopy(ys, 0, newYs, 0, ys.length);

            // add the new x and y coordinate:
            newXs[newXs.length-1] = e.getX();
            newYs[newYs.length-1] = e.getY();

            // make the old arrays point to the new ones
            xs = newXs;
            ys = newYs;

            // call the repaint method to draw the new circle
            repaint();
        }
    }

    /**
     * When the mouse is dragged, circles are drawn wherever it was dragged. 
     * 
     * @param e supplied by Java
     */
    public void mouseDragged(MouseEvent e)
    {
        // since we have dragged the mouse, we no longer need to clear the screen 
        clearScreen = false;

        // create news x/y arrays, with one extra index and 
        // copy the contents of the old array to the new one 
        int [] newXs = new int[xs.length + 1];
        System.arraycopy(xs, 0, newXs, 0, xs.length);
        int [] newYs = new int[ys.length + 1];
        System.arraycopy(ys, 0, newYs, 0, ys.length);

        // add the new x and y coordinate(s):
        newXs[newXs.length-1] = e.getX();
        newYs[newYs.length-1] = e.getY();

        // make the old arrays point to the new ones
        xs = newXs;
        ys = newYs;

        // call the repaint method to draw the new circle(s)
        repaint();
    }

    /**
     * When the mouse is moved over the pane, nothing happens. 
     * We need to have this implemented since we are implementing MouseMotionListener
     * 
     * @param e supplied by Java
     */
    public void mouseMoved(MouseEvent e)
    {
    }

    /**
     * Paints the circles generated from clicking the pane, or dragging the mouse on it, or paints 
     * a white rectangle that covers the pane if the screen needs to be cleared. 
     * 
     * @param g supplied by Java
     */
    public void paint(Graphics g)
    {
        if (clearScreen)
        // if we need to clear the screen
        {
            // draw a white rectangle over the pane to cover the old drawn circles 
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        else if (!clearScreen)
        // if we don't need to clear the screen, draw any circles from dragging/clicking the pane 
        {
            // the  point/coordinate number to start/stop drawing circles for each section
            // (each section has a specific color and radius)
            int startP = 0; // (inclusive)
            int endP = 0; // (exclusive)
            // the index of the colors / radius arrays that are being drawn 
            int c = 0; 
            int r = 0;
            // the radius that is being drawn
            int rad; 

            while (endP < xs.length)
            // continues until the endP equals xs.length (all the circles are drawn)
            {
                // setup endP
                if (colors[0].length > c + 1 && radius[0].length > r + 1)
                // if there is another color AND radius (they both change), the end point is the 
                // point number of whichever one that has the smallest point number 
                    endP = Math.min (colors[0][c+1], radius[0][r+1]);
                else if (colors[0].length > c +1)
                // if there is only another color (the radius doesn't change anymore),
                // the end point is the point number of the next color
                    endP = colors[0][c+1];
                else if (radius[0].length > r + 1)
                // if there is only another radius (the color doesn't change anymore),
                // the end point is the point number of the next radius
                    endP = radius[0][r+1];
                else 
                // if there isn't another color NOR radius (they both stop changing), 
                // the end point is xs.length (the last point that will be drawn)
                    endP = xs.length;

                // set color and radius 
                g.setColor(getColor(colors[1][c]));
                rad = radius[1][r];

                // print circles in correct color and size
                for (int i = startP; i < endP; i++)
                {
                    g.fillOval(xs[i] - rad, ys[i] - rad, 2*rad, 2*rad);
                }

                // update r and/or c
                if (colors[0].length > c + 1 && radius[0].length > r + 1)
                // if there is another color AND radius (both change)
                {
                    if (colors[0][c+1] > radius[0][r+1])
                    // if the point number of the next color is greater (radius changes sooner), 
                    // use the next index of the radius array
                        r ++;
                    else if (colors[0][c+1] < radius[0][r+1])
                    // if the point number of the next radius is greater (color changes sooner), 
                    // use the next index of the colors array
                        c ++;
                    else 
                    // if the point numbers of the next color and radius are the same 
                    // (the change at the same time), 
                    // use the next index of the radius and colors array
                    {
                        r++;
                        c++;
                    }
                }
                else if (colors[0].length > c +1)
                // if only the colors change, use the next index of the colors array
                    c++;
                else if (radius[0].length > r + 1)
                // if only the radius change, use the next index of the radius array
                    r++;

                // update startP
                startP = endP;
            }

        }

    }
}