package MathExpression;

public abstract class Binary extends MathExpression {
        private MathExpression lhs = null;
        private MathExpression rhs = null;

        public Binary(){}

        public Binary(MathExpression lhs, MathExpression rhs) {
                this.lhs = lhs;
                this.rhs = rhs;
        }

        public MathExpression getLhs() {
                return this.lhs;
        }

        public MathExpression getRhs() {
                return this.rhs;
        }
}

