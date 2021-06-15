import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Traductor {
    public static void main(String [] args) throws Exception{
        Swift3Lexer lexer = new Swift3Lexer(CharStreams.fromFileName("input/input.txt"));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Swift3Parser parser = new Swift3Parser(tokens);
        ParseTree tree = parser.top_level();

        //MyVisitor<Object> loader = new MyVisitor<Object>();
        //loader.visit(tree);
    }
}
