package test;

import org.junit.Assert;
import org.junit.Test;
import palindrome.DG;
import palindrome.DirectedGraph;

import java.util.Random;

public class PalindromeTests {
    private DG dag = new DG();

    //We testen niet meerdere palindromen achter elkaar omdat dit vrij nutteloos is
    // Ze worden namelijk als apparte palindromen afgehandeld.

    //test graaf met 1 top
    @Test
    public void testZero() {
        String output = dag.inputToOutput("2\nA 0\n\nB 0\n");
        Assert.assertEquals(output, "1 / 0");
    }

    @Test
    public void testOne() {
        String output = dag.inputToOutput("5\nA 2\n2 3\nB 0\n\nB 1\n3\nA 2\n1 4\nC 0\n");
        Assert.assertEquals(output, "3 AB 0 2 3");
    }

    @Test
    public void testTwo() {
        String output = dag.inputToOutput("5\nA 2\n1 4\nB 2\n2 3\nB 1\n3\nA 0\n\nC 2\n1 3");
        Assert.assertEquals(output, "4 AB 0 1 2 3");
    }

    @Test
    public void testThree() {
        String output = dag.inputToOutput("4\nA 2\n1 3\nA 1\n2\nA 0\n\nA 1\n2");
        Assert.assertEquals(output, "3 A 0 1 2");
    }

    @Test
    public void testFour() {
        String output = dag.inputToOutput("5\nA 1\n1\nC 0\n\nA 2\n0 3\nB 1\n0\nC 2\n2 3");
        Assert.assertEquals(output, "5 ABC 4 2 3 0 1");
    }

    //lege graaf
    @Test
    public void testFive() {
        String output = dag.inputToOutput("0");
        Assert.assertEquals(output, "0 /");
    }

    //lege intersection + palindromen lengte 1
    @Test
    public void testSix() {
        String output = dag.inputToOutput("3\nA 1\n1\nB 1\n2\nC 0\n");
        Assert.assertEquals(output, "1 / 0");
    }

    @Test
    public void testSeven() {
        String output = dag.inputToOutput("6\nA 1\n1\nB 1\n2\nA 0\n\nA 1\n4\nB 1\n5\nA 0\n");
        Assert.assertEquals(output, "3 AB 0 1 2");
    }

    @Test
    public void randomTest1() {
        dag.findPalindrome(makeRandomGraph(100));
    }

    @Test
    public void randomTest2() {
         dag.findPalindrome(makeRandomGraph(200));
    }

    @Test
    public void randomTest3() {
        dag.findPalindrome(makeRandomGraph(300));
    }

    @Test
    public void randomTest4() {
        dag.findPalindrome(makeRandomGraph(400));
    }

    @Test
    public void randomTest5() {
        dag.findPalindrome(makeRandomGraph(500));
    }

    @Test
    public void randomTest6() {
        dag.findPalindrome(makeRandomGraph(600));
    }

    @Test
    public void randomTest7() {
        dag.findPalindrome(makeRandomGraph(700));
    }

    //als we enkel rechter bovendriehoeksmatrix opvullen dan weten we zeker dat het acyclish is.
    private DirectedGraph makeRandomGraph(int size){
        int[][] ajacenceMatrix = new int[size][size];
        Character[] symbols = new Character[size];
        String allSymbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i=0; i<size; i++){
            int randomInt = getRandomInt(allSymbols.length()-1);
            symbols[i] = allSymbols.charAt(randomInt);
        }
        for (int i=0; i<size; i++){
            for (int j=i; j<size; j++){
                if (i != j){
                    ajacenceMatrix[i][j] = getRandomInt(1);
                }
            }
        }
        return new DirectedGraph(ajacenceMatrix, size, symbols);
    }

    private Random rand = new Random();

    private int getRandomInt(int max){
        return rand.nextInt((max) + 1);
    }
}
