package MathExpression;

public class Constant extends MathExpression {
    private final int value;

    public Constant(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public String toString() {
        return String.valueOf(this.value);
    }

    public String getName() {
        return String.valueOf(value);
    }

    public MathExpression eval(){
        return new Constant(this.value);
    }
}