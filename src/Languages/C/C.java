package Languages.C;

import Languages.Code;
import Transpiler.AbstractSyntaxTree;
import Transpiler.NodeType;
import Transpiler.RuleType;

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
        /*
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
        /*
         * This function reverses the parsing process to generate a string code for the given AST.
         * @param tree: The AST (Abstract Syntax Tree) of the program we want to generate the code for.
         * @return  The generated C program code for the given AST.
         */
        return decode(ast);
    }
    private String decode(AbstractSyntaxTree ast) {
        StringBuilder result = new StringBuilder();
        if (ast != null) {
            switch (ast.getType()) {
                case ASSIGNMENT:
                    result.append(new C(ast.getChildren().get(0)).generateCode());
                    result.append(" = ");
                    result.append(new C(ast.getChildren().get(1)).generateCode());
                    break;
                case ASSIGNMENTS:
                    result.append((new C(ast.getChildren().get(0))).generateCode());
                    if (ast.getSubType() == RuleType.MULTI) {
                        result.append(", ");
                        result.append((new C(ast.getChildren().get(1))).generateCode());
                    }
                    break;
                case CASE:
                    result.append("case ");
                    result.append((new C(ast.getChildren().get(0))).generateCode());
                    result.append(":");
                    result.append((new C(ast.getChildren().get(1))).generateCode());
                    break;
                case CASES:
                    if (ast.getSubType() == RuleType.MULTI) {
                        if (ast.getChildren().get(0).getType() != NodeType.OPTIONS) {
                            result.append("case ");
                            result.append((new C(ast.getChildren().get(0))).generateCode());
                            result.append(":\n");
                            result.append(organizer(new C(ast.getChildren().get(1)).generateCode().split("\n")));
                        } else {
                            String[] cases = new C(ast.getChildren().get(0)).generateCode().split(",");
                            for (String subCode : cases) {
                                result.append("case ");
                                result.append(subCode);
                                result.append(":\n");
                                result.append(organizer(new C(ast.getChildren().get(1).getChildren().get(0)).generateCode().split("\n")));
                            }
                        }
                        result.append(new C(ast.getChildren().get(2)).generateCode());
                    } else {
                        result.append("default:\n");
                        result.append(organizer(new C(ast.getChildren().get(0)).generateCode().split("\n")));
                    }
                    break;
                case COMPARISON, PRIMARY:
                    result.append((new C(ast.getChildren().get(0))).generateCode());
                    break;
                case CONJUNCTION:
                    result.append((new C(ast.getChildren().get(0))).generateCode());
                    if (ast.getSubType() == RuleType.MULTI) {
                        result.append(" && ");
                        result.append((new C(ast.getChildren().get(1))).generateCode());
                    }
                    break;
                case DECLARATION:
                    result.append("int ");
                    result.append((new C(ast.getChildren().get(0))).generateCode());
                    break;
                case DISJUNCTION:
                    result.append((new C(ast.getChildren().get(0))).generateCode());
                    if (ast.getSubType() == RuleType.MULTI) {
                        result.append(" || ");
                        result.append((new C(ast.getChildren().get(1))).generateCode());
                    }
                    break;
                case EQ:
                    result.append((new C(ast.getChildren().get(0))).generateCode());
                    result.append(" == ");
                    result.append((new C(ast.getChildren().get(1))).generateCode());
                    break;
                case FACTOR:
                    switch (ast.getSubType()) {
                        case DEFAULT:
                            result.append((new C(ast.getChildren().get(0))).generateCode());
                            break;
                        case NEGATIVE:
                            result.append("-");
                            result.append((new C(ast.getChildren().get(0))).generateCode());
                            break;
                        case PAR:
                            result.append("(");
                            result.append((new C(ast.getChildren().get(0))).generateCode());
                            result.append(")");
                            break;
                        case POSITIVE:
                            result.append("+");
                            result.append((new C(ast.getChildren().get(0))).generateCode());
                            break;
                    }
                    break;
                case FOLLOW_STATEMENTS:
                    switch (ast.getSubType()) {
                        case EMPTY:
                            result.append("{\n}\n");
                            break;
                        case MULTI:
                            result.append(organizerBracket(new C(ast.getChildren().get(0)).generateCode().split("\n")));
                            break;
                        case SINGLE:
                            result.append((new C(ast.getChildren().get(0))).generateCode());
                            break;
                    }
                    break;
                case GT:
                    result.append((new C(ast.getChildren().get(0))).generateCode());
                    result.append(" > ");
                    result.append((new C(ast.getChildren().get(1))).generateCode());
                    break;
                case ID, NUM:
                    result.append(ast.getLexeme());
                    break;
                case IF:
                    result.append("if (");
                    result.append((new C(ast.getChildren().get(0))).generateCode());
                    result.append(") ");
                    result.append((new C(ast.getChildren().get(1))).generateCode());
                    result.append("else ");
                    result.append((new C(ast.getChildren().get(2))).generateCode());
                    break;
                case INVERSION:
                    if (ast.getSubType() == RuleType.MULTI) {
                        result.append("!");
                        result.append((new C(ast.getChildren().get(0))).generateCode());
                    } else
                        result.append((new C(ast.getChildren().get(0))).generateCode());
                    break;
                case LT:
                    result.append((new C(ast.getChildren().get(0))).generateCode());
                    result.append(" < ");
                    result.append((new C(ast.getChildren().get(1))).generateCode());
                    break;
                case MODULO:
                    result.append((new C(ast.getChildren().get(0))).generateCode());
                    if (ast.getSubType() == RuleType.MULTI) {
                        result.append(" % ");
                        result.append((new C(ast.getChildren().get(1))).generateCode());
                    }
                    break;
                case OPTIONS:
                    result.append((new C(ast.getChildren().get(0))).generateCode());
                    if (ast.getSubType() == RuleType.MULTI) {
                        result.append(",");
                        result.append((new C(ast.getChildren().get(1))).generateCode());
                    }
                    break;
                case PRINT:
                    if (ast.getSubType() == RuleType.MULTI) {
                        result.append((new C(ast.getChildren().get(0))).generateCode());
                        result.append(" << ");
                        result.append((new C(ast.getChildren().get(1))).generateCode());
                    }
                    else {
                        result.append(" << ");
                        result.append((new C(ast.getChildren().get(0))).generateCode());
                    }
                    break;
                case PROGRAM:
                    result.append("void ");
                    result.append((new C(ast.getChildren().get(0))).generateCode());
                    result.append("() ");
                    result.append(organizerBracket(new C(ast.getChildren().get(1)).generateCode().split("\n")));
                    break;
                case STATEMENT:
                    switch (ast.getSubType()) {
                        case ASSIGNMENTS, DECLARE:
                            result.append((new C(ast.getChildren().get(0))).generateCode());
                            result.append(";\n");
                            break;
                        case BREAK:
                            result.append("break;\n");
                            break;
                        case CONTINUE:
                            result.append("continue;\n");
                            break;
                        case IF, WHILE, SWITCH:
                            result.append((new C(ast.getChildren().get(0))).generateCode());
                            break;
                        case PRINT:
                            result.append("cout << ");
                            result.append((new C(ast.getChildren().get(0))).generateCode());
                            result.append(";\n");
                            break;
                    }
                    break;

                case STATEMENTS:
                    result.append((new C(ast.getChildren().get(0))).generateCode());
                    if (ast.getSubType() == RuleType.MULTI)
                        result.append(new C(ast.getChildren().get(1)).generateCode());
                    break;
                case SUM:
                    switch (ast.getSubType()) {
                        case ADD:
                            result.append((new C(ast.getChildren().get(0))).generateCode());
                            result.append(" + ");
                            result.append((new C(ast.getChildren().get(1))).generateCode());
                            break;
                        case DEFAULT:
                            result.append((new C(ast.getChildren().get(0))).generateCode());
                            break;
                        case SUB:
                            result.append((new C(ast.getChildren().get(0))).generateCode());
                            result.append(" - ");
                            result.append((new C(ast.getChildren().get(1))).generateCode());
                            break;
                    }
                    break;
                case SWITCH:
                    result.append("switch (");
                    result.append((new C(ast.getChildren().get(0))).generateCode());
                    result.append(") {\n");
                    result.append((new C(ast.getChildren().get(1))).generateCode());
                    result.append("}\n");
                    break;
                case TERM:
                    switch (ast.getSubType()) {
                        case DEFAULT:
                            result.append((new C(ast.getChildren().get(0))).generateCode());
                            break;
                        case DIVIDES:
                            result.append((new C(ast.getChildren().get(0))).generateCode());
                            result.append(" / ");
                            result.append((new C(ast.getChildren().get(1))).generateCode());
                            break;
                        case TIMES:
                            result.append((new C(ast.getChildren().get(0))).generateCode());
                            result.append(" * ");
                            result.append((new C(ast.getChildren().get(1))).generateCode());
                            break;
                    }
                    break;
                case WHILE:
                    result.append("while (");
                    result.append((new C(ast.getChildren().get(0))).generateCode());
                    result.append(")");
                    result.append((new C(ast.getChildren().get(1))).generateCode());
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
