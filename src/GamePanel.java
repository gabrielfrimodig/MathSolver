
import MathExpression.MathExpression;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * The type Game panel.
 */
public class GamePanel extends JPanel implements MouseListener, MouseMotionListener {
    private final int cellSize = 60;

    ArrayList<Point> numberCoords = new ArrayList<>();  // Coordinates for all numbers
    ArrayList<Point> mathCoords = new ArrayList<>();    // Coordinates for all math operations

    Point current = new Point();    // Point for current mouse position

    private MathExpression lastAnswer;

    GameLogic gl;

    Color backCol = new Color(0, 82, 93);
    Color answerCol = new Color(120, 188, 173);
    Color mathCol = new Color(196, 118, 106);
    Color numberCol = new Color(196, 144, 131);
    Color correctAnswerCol = new Color(142, 212, 57);

    /**
     * Instantiates a new Game panel.
     */
    public GamePanel(){
        initPanel();
    }

    private void initPanel(){
        addMouseMotionListener(this);
        addMouseListener(this);
        setBackground(backCol);
        setFocusable(true);

        gl = new GameLogic(1);

        setPreferredSize(new Dimension(600, 600));

        initCoordinates();
    }

    private void initCoordinates(){
        mathCoords.add(new Point(150, 460));
        mathCoords.add(new Point(230, 460));
        mathCoords.add(new Point(310, 460));
        mathCoords.add(new Point(390, 460));

        numberCoords.add(new Point(480, 320));
        numberCoords.add(new Point(60, 320));
        numberCoords.add(new Point(130, 240));
        numberCoords.add(new Point(220, 190));
        numberCoords.add(new Point(320, 190));
        numberCoords.add(new Point(410, 240));
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        /* Settings for the text format and baseline */
        Font font = new Font("SansSerif", Font.PLAIN, 30);
        Font answerFont = new Font("SansSerif", Font.PLAIN, 25);
        g2d.setFont(font);
        g2d.setStroke(new BasicStroke(2));

        /* The answer numbers at the top. */
        for(int i = 0 ; i < gl.getWantedSize() ; i++) {
            if(!gl.getCorrectBool(i)) g2d.setColor(answerCol);
            else g2d.setColor(correctAnswerCol);

            g2d.fillRoundRect(110 + 80*i, 20, cellSize, cellSize, 40, 40);
            g2d.setColor(Color.black);
            if(gl.getWantedNumber(i) > 9 || gl.getWantedNumber(i) < 0){
                g2d.drawString(""+gl.getWantedNumber(i), 120+80*i, 60);
            }else if(gl.getWantedNumber(i) > 99 || gl.getWantedNumber(i) < -9){
                g2d.drawString(""+gl.getWantedNumber(i), 100+80*i, 60);
            }
            else{
                g2d.drawString(""+gl.getWantedNumber(i), 130+80*i, 60);
            }
        }

        /* Number alternatives */
        for(int i = 0 ; i < numberCoords.size() ; i++){
            if(gl.getNumberBool(i)) g2d.setColor(numberCol);
            else g2d.setColor(correctAnswerCol);

            g2d.fillRoundRect(numberCoords.get(i).x, numberCoords.get(i).y, cellSize, cellSize, 40, 40);
            g2d.setColor(Color.black);
            g2d.drawString(""+gl.getNameOfNumber(i), numberCoords.get(i).x + 18, numberCoords.get(i).y + 40);
        }

        /* Math options */
        for(int i = 0 ; i < gl.sizeOfMathOptions() ; i++){
            if(gl.getMathOptionBool(i)) g2d.setColor(mathCol);
            else g2d.setColor(correctAnswerCol);

            g2d.fillRoundRect(mathCoords.get(i).x, mathCoords.get(i).y, cellSize, cellSize, 40, 40);
            g2d.setColor(Color.WHITE);
            g2d.drawString(""+gl.getNameOfMathOption(i), mathCoords.get(i).x+20, mathCoords.get(i).y+40);
        }

        /* Current expression we will evaluate */
        g2d.setColor(Color.BLACK);
        if(gl.getExpressionToBeParsed().length() > 0) {
            if(!gl.getNumberNext()) lastAnswer = gl.getCurrentAnswer().eval();
            g2d.drawString(gl.getExpressionToBeParsed()+" = "+lastAnswer, 120, 140);
        }

        /* Draw line between all nodes */
        g2d.setColor(correctAnswerCol);
        if(gl.getLocationsSize() > 1) {
            for(int i = 0 ; i < gl.getLocationsSize()-1 ; i++) {
                g2d.drawLine(gl.getLocationAt(i).x, gl.getLocationAt(i).y, gl.getLocationAt(i+1).x, gl.getLocationAt(i+1).y);
            }
            g2d.drawLine(gl.getLocationAt(gl.getLocationsSize()-1).x, gl.getLocationAt(gl.getLocationsSize()-1).y, current.x, current.y);
        }else if(gl.getLocationsSize() == 1) {
            g2d.drawLine(gl.getLocationAt(0).x, gl.getLocationAt(0).y, current.x, current.y);
        }

        /* Writing out current level */
        g2d.drawString("Level: "+gl.getLevel(), 10, 590);
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        gl.clickHandler(mouseEvent.getX(), mouseEvent.getY(), numberCoords, mathCoords, cellSize);

        current.setLocation(mouseEvent.getX(), mouseEvent.getY());

        repaint();
    }

    public void mouseReleased(MouseEvent mouseEvent) {
        gl.mouseReleased();

        /* Initialize a new level if needed */
        if(gl.newLevel()){
            int level = gl.getLevel() + 1;
            gl = null;
            gl = new GameLogic(level);
        }

        repaint();
    }

    public void mousePressed(MouseEvent mouseEvent) {}
    public void mouseClicked(MouseEvent mouseEvent) {}
    public void mouseEntered(MouseEvent mouseEvent) {}
    public void mouseExited(MouseEvent mouseEvent) {}
    public void mouseMoved(MouseEvent mouseEvent) {}
}