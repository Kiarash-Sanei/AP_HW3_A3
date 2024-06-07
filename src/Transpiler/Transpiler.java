package Transpiler;

import Languages.Runnable;

import java.util.ArrayList;
import java.util.List;

public class Transpiler <T extends Runnable> {

    List<T> runnables = new ArrayList<>();

    public void addCode(T code) {
        // TODO: add the given runnable object to codes list.
    }

    public List<AbstractSyntaxTree> getAbstractSyntaxTrees() {
        /**
         * This function gathers each runnable object's abstract tree and returns them.
         * @return  list of each runnable object's abstract tree.
         */
        // TODO: return AST of all codes.
        return null;
    }

    public List<String> getCodes() {
        /**
         * This function gathers each runnable object's string code and returns them.
         * @return  list of each runnable object's string code.
         */
        // TODO: return codes of all runnable objects.
        return null;
    }

    public List<T> getSimilarRunnables(T runnable) {
        /**
         * This function finds the runnable objects that are similar to the given runnable object
         * and returns them. Two runnable objects are considered similar only if their
         * Abstract Syntax Tree (AST) would be equal.
         * @param runnable: a runnable object (a specific programming language code)
         * @return          the runnable objects similar to the input.
         */
        // TODO: return only the similar runnable objects.
        return null;
    }

    public List<T> getUniqueRunnables() {
        /**
         * This function finds the runnable objects that are unique (non-similar) and returns them.
         * @return  list of unique runnable objects.
         */
        // TODO: return only the unique runnable objects.
        return null;
    }
}
