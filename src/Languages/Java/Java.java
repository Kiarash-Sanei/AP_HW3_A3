package Languages.Java;

import Languages.Code;
import Transpiler.AbstractSyntaxTree;

import java.io.StringReader;

public class Java extends Code {

    public Java(String code) {
        super(code);
    }

    public Java(AbstractSyntaxTree ast) {
        super(ast);
    }

    @Override
    public AbstractSyntaxTree parseToAST() {
        /**
         * This function parses the given program code with Java Parser.
         * @return  Parsed AST (Abstract Syntax Tree) of the given Program.
         */
        JavaLexer lexer = new JavaLexer(new StringReader(this.code));
        JavaParser parser = new JavaParser(lexer);
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
         * @return  The generated Java program code for the given AST.
         */
        // TODO: Generate the Code from the given AST
        return "";
    }
}
