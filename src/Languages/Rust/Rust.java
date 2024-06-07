package Languages.Rust;

import Languages.Code;
import Transpiler.AbstractSyntaxTree;

import java.io.StringReader;

public class Rust extends Code {

    public Rust(String code) {
        super(code);
    }

    public Rust(AbstractSyntaxTree ast) {
        super(ast);
    }

    @Override
    public AbstractSyntaxTree parseToAST() {
        /**
         * This function parses the given program code with Rust Parser.
         * @return  Parsed AST (Abstract Syntax Tree) of the given Program.
         */
        RustLexer lexer = new RustLexer(new StringReader(this.code));
        RustParser parser = new RustParser(lexer);
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
         * @return  The generated Rust program code for the given AST.
         */
        // TODO: Generate the Code from the given AST
        return "";
    }
}
