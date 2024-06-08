package Tests;

import Languages.C.C;
import Languages.Code;
import Languages.Java.Java;
import Languages.Rust.Rust;
import Transpiler.AbstractSyntaxTree;
import Transpiler.Transpiler;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class Testcases {

    @Test
    public void testCTranslation() throws IOException, URISyntaxException {
        String cCode = new String(Files.readAllBytes(Paths.get(
                Objects.requireNonNull(getClass().getResource("test1.c")).toURI())));
        C c = new C(cCode);
        AbstractSyntaxTree tree = c.parseToAST();
        String javaCode = new Java(tree).generateCode();
        String rustCode = new Rust(tree).generateCode();
        System.out.println("C:");
        System.out.println(cCode);
        System.out.println("Java:");
        System.out.println(javaCode);
        System.out.println("Rust:");
        System.out.println(rustCode);
        Java java = new Java(javaCode);
        Rust rust = new Rust(rustCode);
        Transpiler<Code> transpiler = new Transpiler();
        transpiler.addCode(c);
        transpiler.addCode(java);
        transpiler.addCode(rust);
        List<Code> runnables = transpiler.getSimilarRunnables(c);
        Assert.assertEquals(runnables.size(), 3);
    }

    @Test
    public void testJavaTranslation() throws IOException, URISyntaxException {
        String javaCode = new String(Files.readAllBytes(Paths.get(
                Objects.requireNonNull(getClass().getResource("test2.j")).toURI())));
        Java java = new Java(javaCode);
        AbstractSyntaxTree tree = java.parseToAST();
        String cCode = new C(tree).generateCode();
        String rustCode = new Rust(tree).generateCode();
        System.out.println("C:");
        System.out.println(cCode);
        System.out.println("Java:");
        System.out.println(javaCode);
        System.out.println("Rust:");
        System.out.println(rustCode);
        C c = new C(cCode);
        Rust rust = new Rust(rustCode);
        Transpiler<Code> transpiler = new Transpiler();
        transpiler.addCode(c);
        transpiler.addCode(java);
        transpiler.addCode(rust);
        List<Code> runnables = transpiler.getSimilarRunnables(c);
        Assert.assertEquals(runnables.size(), 3);
    }

    @Test
    public void testRustTranslation() throws IOException, URISyntaxException {
        String rustCode = new String(Files.readAllBytes(Paths.get(
                Objects.requireNonNull(getClass().getResource("test3.rs")).toURI())));
        Rust rust = new Rust(rustCode);
        AbstractSyntaxTree tree = rust.parseToAST();
        String cCode = new C(tree).generateCode();
        String javaCode = new Java(tree).generateCode();
        System.out.println("C:");
        System.out.println(cCode);
        System.out.println("Java:");
        System.out.println(javaCode);
        System.out.println("Rust:");
        System.out.println(rustCode);
        Java java = new Java(javaCode);
        C c = new C(cCode);
        Transpiler<Code> transpiler = new Transpiler();
        transpiler.addCode(c);
        transpiler.addCode(java);
        transpiler.addCode(rust);
        List<Code> runnables = transpiler.getSimilarRunnables(c);
        Assert.assertEquals(runnables.size(), 3);
    }

    @Test
    public void testDifferentCodes() throws IOException, URISyntaxException {
        String cCode = new String(Files.readAllBytes(Paths.get(
                Objects.requireNonNull(getClass().getResource("test4.c")).toURI())));
        String javaCode = new String(Files.readAllBytes(Paths.get(
                Objects.requireNonNull(getClass().getResource("test4.j")).toURI())));
        String rustCode = new String(Files.readAllBytes(Paths.get(
                Objects.requireNonNull(getClass().getResource("test4.rs")).toURI())));
        C c = new C(cCode);
        Java java = new Java(javaCode);
        Rust rust = new Rust(rustCode);
        System.out.println("C:");
        System.out.println(cCode);
        System.out.println("Java:");
        System.out.println(javaCode);
        System.out.println("Rust:");
        System.out.println(rustCode);
        AbstractSyntaxTree cTree = c.parseToAST();
        AbstractSyntaxTree javaTree = java.parseToAST();
        AbstractSyntaxTree rustTree = rust.parseToAST();
        Transpiler<Code> transpiler = new Transpiler();
        transpiler.addCode(c);
        transpiler.addCode(java);
        transpiler.addCode(rust);
        List<Code> uniques = transpiler.getUniqueRunnables();
        Assert.assertEquals(cTree, javaTree);
        Assert.assertEquals(cTree, rustTree);
        Assert.assertEquals(javaTree, rustTree);
        Assert.assertEquals(1, uniques.size());
    }
}