package MathExpression;

public class Subtraction extends Binary {

    public Subtraction(){}

    public Subtraction(MathExpression lhs, MathExpression rhs){
        super(lhs, rhs);
    }

    public String getName() {
        return "-";
    }

    public MathExpression eval(){
        MathExpression lhs = this.getLhs().eval();
        MathExpression rhs = this.getRhs().eval();

        return new Constant(lhs.getValue() - rhs.getValue());
    }
}