package MathExpression;

public abstract class MathExpression {

    public String getName() {
        throw new RuntimeException("getName() called on expression with no operator");
    }

    public int getValue() {
        throw new RuntimeException("getValue() called on a non-constant");
    }

    public abstract MathExpression eval();
}