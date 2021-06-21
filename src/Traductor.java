import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Traductor {
    public static void main(String [] args) throws Exception{
        KotlinLexer lexer = new KotlinLexer(CharStreams.fromFileName("input/input.txt"));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        KotlinParser parser = new KotlinParser(tokens);
        ParseTree tree = parser.expression();

        VisitorKotlin<Object> loader = new VisitorKotlin<Object>();
        loader.visit(tree);

    }
}
