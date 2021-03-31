import MathExpression.*;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

/**
 * The type Game logic.
 */
public class GameLogic implements Levels{
    private final int level;

    private final ArrayList<MathExpression> numbers = new ArrayList<MathExpression>();
    private final ArrayList<MathExpression> mathOptions = new ArrayList<MathExpression>();

    private final ArrayList<Integer> answers = new ArrayList<>();

    private final LinkedList<Point> locations = new LinkedList<>();

    StringTranslator translator = new StringTranslator();

    MathExpression currentAnswer, wantedSymbolic;

    private boolean numberNext;
    private final boolean[] correctAnswer = new boolean[6];
    private final boolean[] numberAvailable = new boolean[6];
    private final boolean[] mathOptionAvailable = new boolean[4];
    private String expressionToBeParsed;
    Random random = new Random();

    /**
     * Instantiates a new Game logic.
     *
     * @param level the level
     */
    public GameLogic(int level){
        this.level = level;

        initLevel();
    }

    /**
     * Initiate everything connected to the specific level.
     */
    private void initLevel(){
        /* Init the number options */
        if(level <= 10){
            for(int i  = 0 ; i < 6 ; i++){
                numbers.add(new Constant(numbersLevel[level-1][i]));
            }
        }else if(level <= 20){
            for(int i = 0 ; i  < 6 ; i++){
                numbers.add(new Constant((int)(Math.random() * 11 + 1)));
            }
        }else{
            for(int i = 0 ; i  < 6 ; i++){
                numbers.add(new Constant((int)(Math.random() * 20 + 1)));
            }
        }

        /* */
        mathOptions.add(new Addition());
        mathOptions.add(new Subtraction());
        mathOptions.add(new Multiplication());
        mathOptions.add(new Division());

        numberNext = true;

        Arrays.fill(numberAvailable, true);
        Arrays.fill(mathOptionAvailable, true);
        Arrays.fill(correctAnswer, false);
        expressionToBeParsed = "";


        if(level <= 10){
            for(int i = 0 ; i < 5 ; i++) {
                answers.add(wantedNumbers[level-1][i]);
            }
        }else{
            String expression = "";
            ArrayList<Integer> number = new ArrayList<>();
            ArrayList<Integer> math = new ArrayList<>();

            for(int i = 0 ; i < 5 ; i++){
                expression = "";
                int tmp;

                /* Restore value of number and math */
                for(int j = 0 ; j < 6; j++){
                    number.add(j);
                }
                for(int j = 0 ; j < 4 ; j++){
                    math.add(j);
                }

                /* Random out math expression*/
                for(int j = 0 ; j < 9; j++){
                    if(j % 2 == 0 ){
                        tmp = random.nextInt(number.size());
                        expression += numbers.get(number.get(tmp));
                        number.remove(tmp);
                    }else{
                        tmp = random.nextInt(math.size());
                        expression += mathOptions.get(math.get(tmp)).getName();
                        math.remove(tmp);
                    }
                }

                try {
                    wantedSymbolic = translator.parse(expression);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                answers.add(wantedSymbolic.eval().getValue());
                number.clear();
                math.clear();
            }
        }

    }

    /**
     * Get current answer symbolic expression.
     *
     * @return the symbolic expression
     */
    public MathExpression getCurrentAnswer(){
        return currentAnswer;
    }

    /**
     * Get name of number string.
     *
     * @param index the index
     * @return the string
     */
    public String getNameOfNumber(int index){
        return numbers.get(index).getName();
    }

    /**
     * Get name of math option string.
     *
     * @param index the index
     * @return the string
     */
    public String getNameOfMathOption(int index){
        return mathOptions.get(index).getName();
    }

    /**
     * Size of math options int.
     *
     * @return the int
     */
    public int sizeOfMathOptions(){
        return mathOptions.size();
    }

    /**
     * Get number next boolean.
     *
     * @return the boolean
     */
    public boolean getNumberNext(){
        return numberNext;
    }

    /**
     * Get wanted number integer.
     *
     * @param index the index
     * @return the integer
     */
    public Integer getWantedNumber(int index){
        return answers.get(index);
    }

    public int getWantedSize(){
        return answers.size();
    }

    /**
     * Get correct bool boolean.
     *
     * @param index the index
     * @return the boolean
     */
    public boolean getCorrectBool(int index){
        return correctAnswer[index];
    }

    /**
     * Get number bool boolean.
     *
     * @param index the index
     * @return the boolean
     */
    public boolean getNumberBool(int index){
        return numberAvailable[index];
    }

    public boolean getMathOptionBool(int index){
        return mathOptionAvailable[index];
    }

    /**
     * Get expression to be parsed string.
     *
     * @return the string
     */
    public String getExpressionToBeParsed(){
        return expressionToBeParsed;
    }

    public int getLocationsSize(){
        return locations.size();
    }

    public Point getLocationAt(int index){
        return locations.get(index);
    }

    public void mouseReleased(){
        checkAnswers();
        locations.clear();
        newRound();
    }

    public void checkAnswers(){
        //== Checks if we got any of the answers
        if(currentAnswer != null && locations.size() > 2) { // Needed. If not, currentAnswer.eval() should not be called.
            for(int i = 0 ; i < answers.size() ; i ++){
                if(answers.get(i) == currentAnswer.eval().getValue() && !correctAnswer[i]){

                    // Set the answer alternative to occupied now
                    correctAnswer[i] = true;
                }
            }
        }
    }

    public void newRound(){
        // Clear the currentAnswer
        try{
            currentAnswer = translator.parse("0");
        }catch (IOException e){
            e.printStackTrace();
        }

        // Make all numbers available
        Arrays.fill(numberAvailable, true);
        Arrays.fill(mathOptionAvailable, true);

        // Clear expressionToBeParsed
        expressionToBeParsed = "";

        // Make next input be a number
        numberNext = true;
    }

    /**
     * Handle all the mouseclick.
     *
     * @param x                      the x
     * @param y                      the y
     * @param numberCoordinates      coordinates of numbers
     * @param mathOptionsCoordinates coordinates of math options
     * @param cellSize               the cell size
     */
    public void clickHandler(int x, int y, ArrayList<Point> numberCoordinates, ArrayList<Point> mathOptionsCoordinates, int cellSize) {
        /*
            First we look if the mouse hit a number or math option
         */
        if(numberNext) {    // Every other number / math options.
            for(int i = 0 ; i < numberCoordinates.size(); i++){ // Check if mouseclick is within any number
                if(numberAvailable[i]){  // If that number is available or not
                    if(numberCoordinates.get(i).x < x && numberCoordinates.get(i).y < y &&
                            numberCoordinates.get(i).x + cellSize > x && numberCoordinates.get(i).y + cellSize > y) {

                        expressionToBeParsed += " " + numbers.get(i).getName();
                        numberAvailable[i] = false;
                        numberNext = false;

                        locations.add(new Point(x, y));

                        updateCurrentAnswer();
                        break;
                    }
                }
            }
        }else{  // If not number then math option
            for(int i = 0 ; i < mathOptions.size(); i++){
                if(mathOptionAvailable[i]){
                    if(mathOptionsCoordinates.get(i).x < x && mathOptionsCoordinates.get(i).y < y &&
                            mathOptionsCoordinates.get(i).x + cellSize > x && mathOptionsCoordinates.get(i).y + cellSize > y){

                        expressionToBeParsed += " " + mathOptions.get(i).getName();
                        mathOptionAvailable[i] = false;
                        locations.add(new Point(x, y));
                        numberNext = true;
                    }
                }
            }
        }
    }

    /**
     *  We only arrive here if we got new number input.
     */
    private void updateCurrentAnswer(){
        try{
            currentAnswer = translator.parse(expressionToBeParsed);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public boolean newLevel(){
        return allWantedBool();
    }

    private boolean allWantedBool(){
        for(int i = 0 ; i < answers.size() ; i++){
            if(!correctAnswer[i]) return false;
        }

        return true;
    }

    /**
     * Get level int.
     *
     * @return the int
     */
    public int getLevel(){
        return level;
    }
}