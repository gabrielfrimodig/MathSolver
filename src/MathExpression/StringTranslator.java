package MathExpression;

import java.io.StreamTokenizer;
import java.io.StringReader;
import java.io.IOException;

public class StringTranslator {
    private StreamTokenizer st;

    public MathExpression parse(String inputString) throws IOException {
        this.st = new StreamTokenizer(new StringReader(inputString));
        this.st.ordinaryChar('-');
        this.st.ordinaryChar('/');
        this.st.eolIsSignificant(true);

        this.st.nextToken();

        return expression();
    }

    private MathExpression expression() throws IOException {
        MathExpression result = term();
        this.st.nextToken();

        while (this.st.ttype == '+' || this.st.ttype == '-') {
            int operation = st.ttype;
            this.st.nextToken();
            if (operation == '+') {
                result = new Addition(result, term());
            } else {
                result = new Subtraction(result, term());
            }
            this.st.nextToken();
        }
        this.st.pushBack();
        return result;
    }

    private MathExpression term() throws IOException {
        MathExpression result = primary();
        this.st.nextToken();

        while (this.st.ttype == '*' || this.st.ttype == '/') {
            int operation = st.ttype;
            this.st.nextToken();

            if (operation == '*') result = new Multiplication(result, primary());
            else result = new Division(result, primary());

            this.st.nextToken();
        }
        this.st.pushBack();
        return result;
    }

    private MathExpression primary() throws IOException {
        this.st.pushBack();

        return number();
    }

    private MathExpression number() throws IOException {
        this.st.nextToken();
        if (this.st.ttype == StreamTokenizer.TT_NUMBER) {
            return new Constant((int) this.st.nval);
        } else {
            throw new SyntaxErrorException("Error: Expected number");
        }
    }

    private static class SyntaxErrorException extends RuntimeException {
        public SyntaxErrorException(String msg) {
            super(msg);
        }
    }
}