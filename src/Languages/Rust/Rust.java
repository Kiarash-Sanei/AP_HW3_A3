package Languages.Rust;

import Languages.Code;
import Transpiler.AbstractSyntaxTree;
import Transpiler.RuleType;

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
        /*
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
        /*
         * This function reverses the parsing process to generate a string code for the given AST.
         * @return  The generated Rust program code for the given AST.
         */
        return decode(ast);
    }
    private String decode(AbstractSyntaxTree ast) {
        StringBuilder result = new StringBuilder();
        if (ast != null) {
            switch (ast.getType()) {
                case ASSIGNMENT:
                    result.append(new Rust(ast.getChildren().get(0)).generateCode());
                    result.append(" = ");
                    result.append(new Rust(ast.getChildren().get(1)).generateCode());
                    break;
                case ASSIGNMENTS:
                    result.append((new Rust(ast.getChildren().get(0))).generateCode());
                    if (ast.getSubType() == RuleType.MULTI) {
                        result.append(", ");
                        result.append((new Rust(ast.getChildren().get(1))).generateCode());
                    }
                    break;
                case CASE:
                    result.append("case ");
                    result.append((new Rust(ast.getChildren().get(0))).generateCode());
                    result.append(":");
                    result.append((new Rust(ast.getChildren().get(1))).generateCode());
                    break;
                case CASES:
                    if (ast.getSubType() == RuleType.MULTI) {
                        result.append(new Rust(ast.getChildren().get(0)).generateCode());
                        result.append(" => ");
                        result.append(new Rust(ast.getChildren().get(1)).generateCode());
                        result.replace(result.length() - 1, result.length(), ",\n");
                        result.append(new Rust(ast.getChildren().get(2)).generateCode());
                    } else {
                        result.append("_ => ");
                        result.append(new Rust(ast.getChildren().get(0)).generateCode());
                    }
                    break;
                case COMPARISON, PRIMARY:
                    result.append((new Rust(ast.getChildren().get(0))).generateCode());
                    break;
                case CONJUNCTION:
                    result.append((new Rust(ast.getChildren().get(0))).generateCode());
                    if (ast.getSubType() == RuleType.MULTI) {
                        result.append(" && ");
                        result.append((new Rust(ast.getChildren().get(1))).generateCode());
                    }
                    break;
                case DECLARATION:
                    String[] variables = new Rust(ast.getChildren().get(0)).generateCode().split(",");
                    for (int i = 0; i < variables.length - 1; i++) {
                        result.append("let");
                        result.append(variables[i]);
                        result.append(";\n");
                    }
                    result.append("let ");
                    result.append(variables[variables.length - 1]);
                    break;
                case DISJUNCTION:
                    result.append((new Rust(ast.getChildren().get(0))).generateCode());
                    if (ast.getSubType() == RuleType.MULTI) {
                        result.append(" || ");
                        result.append((new Rust(ast.getChildren().get(1))).generateCode());
                    }
                    break;
                case EQ:
                    result.append((new Rust(ast.getChildren().get(0))).generateCode());
                    result.append(" == ");
                    result.append((new Rust(ast.getChildren().get(1))).generateCode());
                    break;
                case FACTOR:
                    switch (ast.getSubType()) {
                        case DEFAULT:
                            result.append((new Rust(ast.getChildren().get(0))).generateCode());
                            break;
                        case NEGATIVE:
                            result.append("-");
                            result.append((new Rust(ast.getChildren().get(0))).generateCode());
                            break;
                        case PAR:
                            result.append("(");
                            result.append((new Rust(ast.getChildren().get(0))).generateCode());
                            result.append(")");
                            break;
                        case POSITIVE:
                            result.append("+");
                            result.append((new Rust(ast.getChildren().get(0))).generateCode());
                            break;
                    }
                    break;
                case FOLLOW_STATEMENTS:
                    switch (ast.getSubType()) {
                        case EMPTY:
                            result.append("{\n}\n");
                            break;
                        case MULTI:
                            result.append(organizerBracket(new Rust(ast.getChildren().get(0)).generateCode().split("\n")));
                            break;
                        case SINGLE:
                            result.append((new Rust(ast.getChildren().get(0))).generateCode());
                            break;
                    }
                    break;
                case GT:
                    result.append((new Rust(ast.getChildren().get(0))).generateCode());
                    result.append(" > ");
                    result.append((new Rust(ast.getChildren().get(1))).generateCode());
                    break;
                case ID, NUM:
                    result.append(ast.getLexeme());
                    break;
                case IF:
                    result.append("if (");
                    result.append((new Rust(ast.getChildren().get(0))).generateCode());
                    result.append(") ");
                    result.append((new Rust(ast.getChildren().get(1))).generateCode());
                    result.append("else ");
                    result.append((new Rust(ast.getChildren().get(2))).generateCode());
                    break;
                case INVERSION:
                    if (ast.getSubType() == RuleType.MULTI) {
                        result.append("!");
                        result.append((new Rust(ast.getChildren().get(0))).generateCode());
                    } else
                        result.append((new Rust(ast.getChildren().get(0))).generateCode());
                    break;
                case LT:
                    result.append((new Rust(ast.getChildren().get(0))).generateCode());
                    result.append(" < ");
                    result.append((new Rust(ast.getChildren().get(1))).generateCode());
                    break;
                case MODULO:
                    result.append((new Rust(ast.getChildren().get(0))).generateCode());
                    if (ast.getSubType() == RuleType.MULTI) {
                        result.append(" % ");
                        result.append((new Rust(ast.getChildren().get(1))).generateCode());
                    }
                    break;
                case OPTIONS:
                    result.append((new Rust(ast.getChildren().get(0))).generateCode());
                    if (ast.getSubType() == RuleType.MULTI) {
                        result.append("|");
                        result.append((new Rust(ast.getChildren().get(1))).generateCode());
                    }
                    break;
                case PRINT:
                    if (ast.getSubType() == RuleType.MULTI) {
                        result.append((new Rust(ast.getChildren().get(0))).generateCode());
                        result.append(") + ");
                        result.append((new Rust(ast.getChildren().get(1))).generateCode());
                    }
                    else {
                        result.append((new Rust(ast.getChildren().get(0))).generateCode());
                    }
                    break;
                case PROGRAM:
                    result.append("fn main() ");
                    result.append(organizerBracket(new Rust(ast.getChildren().get(1)).generateCode().split("\n")));
                    break;
                case STATEMENT:
                    switch (ast.getSubType()) {
                        case ASSIGNMENTS, DECLARE:
                            result.append((new Rust(ast.getChildren().get(0))).generateCode());
                            result.append(";\n");
                            break;
                        case BREAK:
                            result.append("break;\n");
                            break;
                        case CONTINUE:
                            result.append("continue;\n");
                            break;
                        case IF, WHILE, SWITCH:
                            result.append((new Rust(ast.getChildren().get(0))).generateCode());
                            break;
                        case PRINT:
                            result.append("println!(");
                            result.append((new Rust(ast.getChildren().get(0))).generateCode());
                            result.append(");\n");
                            break;
                    }
                    break;

                case STATEMENTS:
                    result.append((new Rust(ast.getChildren().get(0))).generateCode());
                    if (ast.getSubType() == RuleType.MULTI)
                        result.append(new Rust(ast.getChildren().get(1)).generateCode());
                    break;
                case SUM:
                    switch (ast.getSubType()) {
                        case ADD:
                            result.append((new Rust(ast.getChildren().get(0))).generateCode());
                            result.append(" + ");
                            result.append((new Rust(ast.getChildren().get(1))).generateCode());
                            break;
                        case DEFAULT:
                            result.append((new Rust(ast.getChildren().get(0))).generateCode());
                            break;
                        case SUB:
                            result.append((new Rust(ast.getChildren().get(0))).generateCode());
                            result.append(" - ");
                            result.append((new Rust(ast.getChildren().get(1))).generateCode());
                            break;
                    }
                    break;
                case SWITCH:
                    result.append("match ");
                    result.append((new Rust(ast.getChildren().get(0))).generateCode());
                    result.append("{\n");
                    result.append((new Rust(ast.getChildren().get(1))).generateCode());
                    result.append("}\n");
                    break;
                case TERM:
                    switch (ast.getSubType()) {
                        case DEFAULT:
                            result.append((new Rust(ast.getChildren().get(0))).generateCode());
                            break;
                        case DIVIDES:
                            result.append((new Rust(ast.getChildren().get(0))).generateCode());
                            result.append(" / ");
                            result.append((new Rust(ast.getChildren().get(1))).generateCode());
                            break;
                        case TIMES:
                            result.append((new Rust(ast.getChildren().get(0))).generateCode());
                            result.append(" * ");
                            result.append((new Rust(ast.getChildren().get(1))).generateCode());
                            break;
                    }
                    break;
                case WHILE:
                    result.append("while (");
                    result.append((new Rust(ast.getChildren().get(0))).generateCode());
                    result.append(") ");
                    result.append((new Rust(ast.getChildren().get(1))).generateCode());
                    break;
            }
        }
        return result.toString();
    }

    private static String organizerBracket(String[] codeLines) {
        StringBuilder result = new StringBuilder();
        result.append("{\n");
        for (String subCode : codeLines) {
            result.append("\t");
            result.append(subCode);
            result.append("\n");
        }
        result.append("}\n");
        return result.toString();
    }
}
