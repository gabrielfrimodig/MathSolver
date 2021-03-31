package MathExpression;

public class Addition extends Binary {

    public Addition(){}

    public Addition(MathExpression lhs, MathExpression rhs){
        super(lhs, rhs);
    }

    public String getName() {
        return "+";
    }

    public MathExpression eval(){
        MathExpression lhs = this.getLhs().eval();
        MathExpression rhs = this.getRhs().eval();

        return new Constant(lhs.getValue() + rhs.getValue());
    }
}
