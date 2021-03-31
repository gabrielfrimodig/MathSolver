package MathExpression;

public class Multiplication extends Binary {

    public Multiplication(){}

    public Multiplication(MathExpression lhs, MathExpression rhs){
        super(lhs, rhs);
    }

    public String getName() {
        return "*";
    }

    public MathExpression eval(){
        MathExpression lhs = this.getLhs().eval();
        MathExpression rhs = this.getRhs().eval();

        return new Constant(lhs.getValue() * rhs.getValue());
    }
}