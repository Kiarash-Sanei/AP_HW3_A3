package Languages.Java;

import Languages.Code;
import Transpiler.AbstractSyntaxTree;
import Transpiler.NodeType;
import Transpiler.RuleType;

import java.io.StringReader;

public class Java extends Code {
    StringBuilder result = new StringBuilder();

    public Java(String code) {
        super(code);
    }

    public Java(AbstractSyntaxTree ast) {
        super(ast);
    }

    @Override
    public AbstractSyntaxTree parseToAST() {
        /*
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
        /*
         * This function reverses the parsing process to generate a string code for the given AST.
         * @return  The generated Java program code for the given AST.
         */
        decode(ast);
        return result.toString();
    }

    private String decode(AbstractSyntaxTree ast) {
        if (ast != null) {
            switch (ast.getType()) {
                case ASSIGNMENT:
                    result.append(new Java(ast.getChildren().get(0)).generateCode());
                    result.append(" = ");
                    result.append(new Java(ast.getChildren().get(1)).generateCode());
                    break;
                case ASSIGNMENTS:
                    result.append((new Java(ast.getChildren().get(0))).generateCode());
                    if (ast.getSubType() == RuleType.MULTI) {
                        result.append(", ");
                        result.append((new Java(ast.getChildren().get(1))).generateCode());
                    }
                    break;
                case CASE:
                    result.append("case ");
                    result.append((new Java(ast.getChildren().get(0))).generateCode());
                    result.append(":");
                    result.append((new Java(ast.getChildren().get(1))).generateCode());
                    break;
                case CASES:
                    if (ast.getSubType() == RuleType.MULTI) {
                        if (ast.getChildren().get(0).getType() != NodeType.OPTIONS) {
                            result.append("case ");
                            result.append((new Java(ast.getChildren().get(0))).generateCode());
                            result.append(":\n");
                            result.append(organizer(new Java(ast.getChildren().get(1)).generateCode().split("\n")));
                        } else {
                            String[] cases = new Java(ast.getChildren().get(0)).generateCode().split(",");
                            for (String subCode : cases) {
                                result.append("case ");
                                result.append(subCode);
                                result.append(":\n");
                                result.append(organizer(new Java(ast.getChildren().get(1).getChildren().get(0)).generateCode().split("\n")));
                            }
                        }
                        result.append(new Java(ast.getChildren().get(2)).generateCode());
                    } else {
                        result.append("default:\n");
                        result.append(organizer(new Java(ast.getChildren().get(0)).generateCode().split("\n")));
                    }
                    break;
                case COMPARISON, PRIMARY:
                    result.append((new Java(ast.getChildren().get(0))).generateCode());
                    break;
                case CONJUNCTION:
                    result.append((new Java(ast.getChildren().get(0))).generateCode());
                    if (ast.getSubType() == RuleType.MULTI) {
                        result.append(" && ");
                        result.append((new Java(ast.getChildren().get(1))).generateCode());
                    }
                    break;
                case DECLARATION:
                    result.append("int ");
                    result.append((new Java(ast.getChildren().get(0))).generateCode());
                    break;
                case DISJUNCTION:
                    result.append((new Java(ast.getChildren().get(0))).generateCode());
                    if (ast.getSubType() == RuleType.MULTI) {
                        result.append(" || ");
                        result.append((new Java(ast.getChildren().get(1))).generateCode());
                    }
                    break;
                case EQ:
                    result.append((new Java(ast.getChildren().get(0))).generateCode());
                    result.append(" == ");
                    result.append((new Java(ast.getChildren().get(1))).generateCode());
                    break;
                case FACTOR:
                    switch (ast.getSubType()) {
                        case DEFAULT:
                            result.append((new Java(ast.getChildren().get(0))).generateCode());
                            break;
                        case NEGATIVE:
                            result.append("-");
                            result.append((new Java(ast.getChildren().get(0))).generateCode());
                            break;
                        case PAR:
                            result.append("(");
                            result.append((new Java(ast.getChildren().get(0))).generateCode());
                            result.append(")");
                            break;
                        case POSITIVE:
                            result.append("+");
                            result.append((new Java(ast.getChildren().get(0))).generateCode());
                            break;
                    }
                    break;
                case FOLLOW_STATEMENTS:
                    switch (ast.getSubType()) {
                        case EMPTY:
                            result.append("{\n}\n");
                            break;
                        case MULTI:
                            result.append(organizerBracket(new Java(ast.getChildren().get(0)).generateCode().split("\n")));
                            break;
                        case SINGLE:
                            result.append((new Java(ast.getChildren().get(0))).generateCode());
                            break;
                    }
                    break;
                case GT:
                    result.append((new Java(ast.getChildren().get(0))).generateCode());
                    result.append(" > ");
                    result.append((new Java(ast.getChildren().get(1))).generateCode());
                    break;
                case ID, NUM:
                    result.append(ast.getLexeme());
                    break;
                case IF:
                    result.append("if (");
                    result.append((new Java(ast.getChildren().get(0))).generateCode());
                    result.append(") ");
                    result.append((new Java(ast.getChildren().get(1))).generateCode());
                    result.append("else ");
                    result.append((new Java(ast.getChildren().get(2))).generateCode());
                    break;
                case INVERSION:
                    if (ast.getSubType() == RuleType.MULTI) {
                        result.append("!");
                        result.append((new Java(ast.getChildren().get(0))).generateCode());
                    } else
                        result.append((new Java(ast.getChildren().get(0))).generateCode());
                    break;
                case LT:
                    result.append((new Java(ast.getChildren().get(0))).generateCode());
                    result.append(" < ");
                    result.append((new Java(ast.getChildren().get(1))).generateCode());
                    break;
                case MODULO:
                    result.append((new Java(ast.getChildren().get(0))).generateCode());
                    if (ast.getSubType() == RuleType.MULTI) {
                        result.append(" % ");
                        result.append((new Java(ast.getChildren().get(1))).generateCode());
                    }
                    break;
                case OPTIONS:
                    result.append((new Java(ast.getChildren().get(0))).generateCode());
                    if (ast.getSubType() == RuleType.MULTI) {
                        result.append(",");
                        result.append((new Java(ast.getChildren().get(1))).generateCode());
                    }
                    break;
                case PRINT:
                    result.append((new Java(ast.getChildren().get(0))).generateCode());
                    if (ast.getSubType() == RuleType.MULTI) {
                        result.append(") + ");
                        result.append((new Java(ast.getChildren().get(1))).generateCode());
                    }
                    break;
                case PROGRAM:
                    result.append("public static void main() ");
                    result.append(organizerBracket(new Java(ast.getChildren().get(1)).generateCode().split("\n")));
                    break;
                case STATEMENT:
                    switch (ast.getSubType()) {
                        case ASSIGNMENTS, DECLARE:
                            result.append((new Java(ast.getChildren().get(0))).generateCode());
                            result.append(";\n");
                            break;
                        case BREAK:
                            result.append("break;\n");
                            break;
                        case CONTINUE:
                            result.append("continue;\n");
                            break;
                        case IF, WHILE, SWITCH:
                            result.append((new Java(ast.getChildren().get(0))).generateCode());
                            break;
                        case PRINT:
                            result.append("System.out.print(");
                            result.append((new Java(ast.getChildren().get(0))).generateCode());
                            result.append(");\n");
                            break;
                    }
                    break;

                case STATEMENTS:
                    result.append((new Java(ast.getChildren().get(0))).generateCode());
                    if (ast.getSubType() == RuleType.MULTI)
                        result.append(new Java(ast.getChildren().get(1)).generateCode());
                    break;
                case SUM:
                    switch (ast.getSubType()) {
                        case ADD:
                            result.append((new Java(ast.getChildren().get(0))).generateCode());
                            result.append(" + ");
                            result.append((new Java(ast.getChildren().get(1))).generateCode());
                            break;
                        case DEFAULT:
                            result.append((new Java(ast.getChildren().get(0))).generateCode());
                            break;
                        case SUB:
                            result.append((new Java(ast.getChildren().get(0))).generateCode());
                            result.append(" - ");
                            result.append((new Java(ast.getChildren().get(1))).generateCode());
                            break;
                    }
                    break;
                case SWITCH:
                    result.append("switch (");
                    result.append((new Java(ast.getChildren().get(0))).generateCode());
                    result.append(") {\n");
                    result.append((new Java(ast.getChildren().get(1))).generateCode());
                    result.append("}\n");
                    break;
                case TERM:
                    switch (ast.getSubType()) {
                        case DEFAULT:
                            result.append((new Java(ast.getChildren().get(0))).generateCode());
                            break;
                        case DIVIDES:
                            result.append((new Java(ast.getChildren().get(0))).generateCode());
                            result.append(" / ");
                            result.append((new Java(ast.getChildren().get(1))).generateCode());
                            break;
                        case TIMES:
                            result.append((new Java(ast.getChildren().get(0))).generateCode());
                            result.append(" * ");
                            result.append((new Java(ast.getChildren().get(1))).generateCode());
                            break;
                    }
                    break;
                case WHILE:
                    result.append("while (");
                    result.append((new Java(ast.getChildren().get(0))).generateCode());
                    result.append(") ");
                    result.append((new Java(ast.getChildren().get(1))).generateCode());
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

    private static String organizer(String[] codeLines) {
        StringBuilder result = new StringBuilder();
        for (String subCode : codeLines) {
            result.append("\t");
            result.append(subCode);
            result.append("\n");
        }
        return result.toString();
    }
}
