package Languages.C;

import Languages.Code;
import Transpiler.AbstractSyntaxTree;

import java.io.StringReader;

public class C extends Code {

    public C(String code) {
        super(code);
    }

    public C(AbstractSyntaxTree ast) {
        super(ast);
    }

    @Override
    public AbstractSyntaxTree parseToAST() {
        /**
         * This function parses the given program code with C Parser.
         * @return  Parsed AST (Abstract Syntax Tree) of the given Program.
         */
        CLexer lexer = new CLexer(new StringReader(this.code));
        CParser parser = new CParser(lexer);
        try {
            Object result = parser.parse().value;
            this.ast = (AbstractSyntaxTree) result;
            return this.ast;
        } catch (Exception e) {
            System.err.println("Parser error: " + e.getMessage());
            this.ast = null;
            return null;
        }
    }

    @Override
    public String generateCode() {
        /**
         * This function reverses the parsing process to generate a string code for the given AST.
         * @param tree: The AST (Abstract Syntax Tree) of the program we want to generate the code for.
         * @return  The generated C program code for the given AST.
         */
        // TODO: Generate the Code from the given AST
        return "";
    }
}
