package palindrome;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class Palindrome {

    private List<Integer> palindrome;
    private int size;
    private DirectedGraph graph;

    Palindrome(List<Integer> palindrome, DirectedGraph directedGraph) {
        this.palindrome = palindrome;
        this.size = palindrome.size();
        graph = directedGraph;
    }

    Palindrome(int[] palindrome) {
        this.palindrome = Arrays.stream(palindrome).boxed().collect(Collectors.toList());
        this.size = palindrome.length;
    }

    List<Integer> getPalindrome() {
        return palindrome;
    }

    int getSize() {
        return size;
    }

    long getBitvector(){
        long output = 0;
        String symbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int symbol : palindrome){
            Character ch = graph.getSymbol(symbol);
            long base = 1;
            base <<= symbols.indexOf(ch);
            output |= base;
        }
        return output;
    }
}
