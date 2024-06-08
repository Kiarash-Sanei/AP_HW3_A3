package Transpiler;

import Languages.Runnable;

import java.util.ArrayList;
import java.util.List;

public class Transpiler<T extends Runnable> {

    List<T> runnables = new ArrayList<>();

    public void addCode(T code) {
        runnables.add(code);
    }

    public List<AbstractSyntaxTree> getAbstractSyntaxTrees() {
        /*
         * This function gathers each runnable object's abstract tree and returns them.
         * @return list of each runnable object's abstract tree.
         */
        List<AbstractSyntaxTree> abstractSyntaxTrees = new ArrayList<>();
        for (T runnable : runnables)
            abstractSyntaxTrees.add(runnable.parseToAST());
        return abstractSyntaxTrees;
    }

    public List<String> getCodes() {
        /*
         * This function gathers each runnable object's string code and returns them.
         * @return list of each runnable object's string code.
         */
        List<String> codes = new ArrayList<>();
        for (T runnable : runnables)
            codes.add(runnable.generateCode());
        return codes;
    }

    public List<T> getSimilarRunnables(T runnable) {
        /*
         * This function finds the runnable objects that are similar to the given runnable object
         * and returns them. Two runnable objects are considered similar only if their
         * Abstract Syntax Tree (AST) would be equal.
         * @param runnable: a runnable object (a specific programming language code)
         * @return the runnable objects similar to the input.
         */
        List<T> similarRunnables = new ArrayList<>();
        AbstractSyntaxTree ast = runnable.parseToAST();
        if (ast == null)
            return similarRunnables;
        for (T runnable2 : runnables)
            if (ast.equals(runnable2.parseToAST()))
                similarRunnables.add(runnable2);
        return similarRunnables;
    }

    public List<T> getUniqueRunnables() {
        /*
         * This function finds the runnable objects that are unique (non-similar) and returns them.
         * @return list of unique runnable objects.
         */
        List<T> uniqueRunnables = new ArrayList<>();
        for (T runnable : runnables) {
            boolean isUnique = true;
            AbstractSyntaxTree ast = runnable.parseToAST();
            if (ast == null)
                continue;
            for (T runnable2 : runnables)
                if (ast.equals(runnable2.parseToAST())) {
                    isUnique = false;
                    break;
                }
            if (isUnique)
                uniqueRunnables.add(runnable);
        }
        return uniqueRunnables;
    }
}
