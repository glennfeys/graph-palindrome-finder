package palindrome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Palindromes {

    private ArrayList<Palindrome> palindromes = new ArrayList<>();
    private int palindromeSize = 0;

    ArrayList<Palindrome> getPalindromes() {
        return palindromes;
    }

    void add(List<Integer> palindrome, DirectedGraph graph){
        palindromes.add(new Palindrome(palindrome, graph));
        palindromeSize = palindrome.size();
    }

    void add(int[] palindrome, DirectedGraph graph){
        List<Integer> test = Arrays.stream(palindrome).boxed().collect(Collectors.toList());
        palindromes.add(new Palindrome(test, graph));
        palindromeSize = palindrome.length;
    }

    void add(Palindrome palindrome){
        palindromes.add(palindrome);
        palindromeSize = palindrome.getSize();
    }

    void addAll(Palindromes palindromes){
        for (Palindrome palindrome: palindromes.getPalindromes()){
            add(palindrome);
        }
        palindromeSize = palindromes.getPalindromeSize();
    }

    int getPalindromeSize(){
        return palindromeSize;
    }
}
