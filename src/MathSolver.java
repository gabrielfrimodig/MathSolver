import javax.swing.*;
import java.awt.*;

public class MathSolver extends JFrame{

    public MathSolver(){
        initScreen();
    }

    private void initScreen(){
        setLayout(new BorderLayout());
        add(new GamePanel(), BorderLayout.CENTER);
        setResizable(false);
        setBackground(new Color(0, 82, 93));
        pack();
        setTitle("Math Solver");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args){
        EventQueue.invokeLater(() -> {
            JFrame frame = new MathSolver();
            frame.setVisible(true);
        });
    }
}