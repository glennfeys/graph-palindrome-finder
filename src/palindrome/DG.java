package palindrome;

import java.util.*;

public class DG {

    private PalindromesParts[][] lookUp;

    public Palindromes findPalindrome(DirectedGraph graph) {
        long startTime = System.nanoTime();
        PalindromesParts biggestPalindromes = new PalindromesParts();
        lookUp = new PalindromesParts[graph.getSize()][graph.getSize()];
        for (int[] paar : getParen(graph)) {
            PalindromesParts localBiggest = extendPalindrome(graph, paar[0], paar[1], new boolean[graph.getSize()][graph.getSize()]);
            if (null == localBiggest) return null;
            PalindromesParts allPalindromes = new PalindromesParts();
            allPalindromes.setSize(localBiggest.getSize());
            allPalindromes.setBeginParts(new ArrayList<>(Collections.singletonList(localBiggest)));
            allPalindromes.setEndParts(new ArrayList<>(Collections.singletonList(localBiggest)));
            allPalindromes.setDouble(localBiggest.isDouble());
            biggestPalindromes = checkPalindrome(allPalindromes, biggestPalindromes);
        }
        Palindromes result = new Palindromes();

        List<List<Integer>> begins = makeBeginPalindrome(biggestPalindromes, new ArrayList<>());
        List<List<Integer>> ends = makeEndPalindrome(biggestPalindromes, new ArrayList<>());

        for (int i=0; i<begins.size(); i++){
            if (begins.get(i).get(0) == ends.get(i).get(0)){
                ends.get(i).remove(0);
            }
            Collections.reverse(ends.get(i));
            ends.get(i).addAll(begins.get(i));
            result.add(ends.get(i), graph);
        }

        long endTime = System.nanoTime();
        System.out.println(endTime - startTime);

        return result;
    }

    private List<List<Integer>> makeBeginPalindrome(PalindromesParts curr, List<Integer> buff){
        List<List<Integer>> result = new ArrayList<>();
        if (curr.getBegin() != -1) buff.add(curr.getBegin());
        if (curr.getBeginParts() == null){
            List<List<Integer>> test2 = new ArrayList<>();
            test2.add(buff);
            return test2;
        }
        for (PalindromesParts p : curr.getBeginParts()){
            result.addAll(makeBeginPalindrome(p, new ArrayList<>(buff)));
        }
        return result;
    }

    private List<List<Integer>> makeEndPalindrome(PalindromesParts curr, List<Integer> buff){
        List<List<Integer>> result = new ArrayList<>();
        if (curr.getEnd() != -1) buff.add(curr.getEnd());
        if (curr.getEndParts() == null){
            List<List<Integer>> test2 = new ArrayList<>();
            test2.add(buff);
            return test2;
        }
        for (PalindromesParts p : curr.getEndParts()){
            result.addAll(makeEndPalindrome(p, new ArrayList<>(buff)));
        }
        return result;
    }

    private PalindromesParts extendPalindrome(DirectedGraph graph, int begin, int end, boolean[][] visitedPairs) {

        if (visitedPairs[begin][end]) return null;
        visitedPairs[begin][end] = true;
        ArrayList<PalindromesParts> localBiggest = new ArrayList<>();
        int localBiggestSize = 0;
        PalindromesParts palindromes;

        for (int[] pair : getCommonSymbols(graph.getOutgoing(begin), graph.getIncomming(end), begin, end, graph)) {
            if (lookUp[pair[0]][pair[1]] != null) {
                palindromes = lookUp[pair[0]][pair[1]];
            } else {
                palindromes = extendPalindrome(graph, pair[0], pair[1], visitedPairs);
                if (palindromes == null) return null;
            }
            if (palindromes.getSize() > localBiggestSize){
                localBiggestSize = palindromes.getSize();
                localBiggest = new ArrayList<>();
                localBiggest.add(palindromes);
            }
            else if (palindromes.getSize() == localBiggestSize) {
                localBiggest.add(palindromes);
            }
        }
        PalindromesParts localBiggestResult = new PalindromesParts();
        if (localBiggestSize == 0){
            localBiggestResult.setSize(1);
            localBiggestResult.setBegin(begin);
            localBiggestResult.setEnd(end);
            localBiggestResult.setBeginParts(null);
            localBiggestResult.setEndParts(null);
            localBiggestResult.setDouble(begin != end);
        }
        else {
            localBiggestResult.setSize(localBiggestSize+1);
            localBiggestResult.setBegin(begin);
            localBiggestResult.setEnd(end);
            localBiggestResult.setBeginParts(new ArrayList<>(localBiggest));
            localBiggestResult.setEndParts(new ArrayList<>(localBiggest));
            localBiggestResult.setDouble(begin != end);
        }

        lookUp[begin][end] = localBiggestResult;
        return localBiggestResult;
    }

    private List<int[]> getParen(DirectedGraph graph){
        ArrayList<int[]> result = new ArrayList<>();
        for (int i = 0; i < graph.getSize(); i++){
            char ci = graph.getSymbol(i);
            result.add(new int[]{i,i});
            for (int k : graph.getOutgoing(i)){
                if (ci == graph.getSymbol(k)){
                    result.add(new int[]{k,i});
                }
            }
        }
        return result;
    }

    private PalindromesParts checkPalindrome(PalindromesParts local, PalindromesParts biggest) {
        if (local.getSize() > biggest.getSize()) {
            return local;
        } else if (local.getSize() == biggest.getSize()) {
            if (local.isDouble() && !biggest.isDouble()) return local;
            else if (local.isDouble() == biggest.isDouble()){
                biggest.addAll(local);
            }
        }
        return biggest;
    }

    private ArrayList<int[]> getCommonSymbols(ArrayList<Integer> n1, ArrayList<Integer> n2, int begin, int end, DirectedGraph dg2) {
        ArrayList<int[]> pairs = new ArrayList<>();
        for (int node1 : n1) {
            for (int node2 : n2) {
                if (dg2.getSymbol(node1) == dg2.getSymbol(node2) && !(begin == end && node1 == node2)) {
                    pairs.add(new int[]{node1, node2});
                }
            }
        }
        return pairs;
    }

    private String getIntersection(Palindromes palindromes) {
        long bitvector = palindromes.getPalindromes().get(0).getBitvector();
        for (int i = 1; i < palindromes.getPalindromes().size(); i++) {
            bitvector &= palindromes.getPalindromes().get(i).getBitvector();
        }
        String output = bitvectorToString(bitvector);
        if (output.equals("")) {
            return "/";
        }
        return output;
    }

    private String bitvectorToString(long bitvector) {
        StringBuilder output = new StringBuilder();
        String symbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < symbols.length(); i++) {
            long base = 1;
            base <<= i;
            if ((bitvector & base) == base) {
                output.append(symbols.charAt(i));
            }
        }
        return output.toString();
    }

    private String getPalindromeNodes(Palindromes palindromes) {
        StringBuilder str = new StringBuilder();
        for (int node : palindromes.getPalindromes().get(0).getPalindrome()) {
            str.append(node).append(" ");
        }
        return str.substring(0, str.length() - 1);
    }

    public String inputToOutput(String input) {
        DG dg = new DG();
        StringBuilder output = new StringBuilder();
        String[] splitInput = input.split("\n");
        int position = 0;
        while (position + 1 < splitInput.length) {
            int size = Integer.parseInt(splitInput[position]);
            DirectedGraph directedGraph = new DirectedGraph(size);
            for (int i = 0; i < size; i++) {
                String[] args = splitInput[1 + i * 2].split(" ");
                int[] outgoing = new int[Integer.parseInt(args[1])];
                int j = 0;
                if (Integer.parseInt(args[1]) > 0) {
                    for (String arg : splitInput[2 + i * 2].split(" ")) {
                        outgoing[j] = Integer.parseInt(arg);
                        j++;
                    }
                }
                directedGraph.addNode(args[0].charAt(0), outgoing);
                position += 2;
            }
            output.append(palindromesToOutput(dg.findPalindrome(directedGraph))).append("\n");
        }
        if (output.toString().length() == 0) {
            return "0 /";
        }
        return output.toString().substring(0, output.toString().length() - 1);
    }

    private String palindromesToOutput(Palindromes palindromes) {
        if (palindromes == null) return "0 /";
        int size = palindromes.getPalindromeSize();
        if (size == 0) return "0 /";
        String output = size + " ";
        output += getIntersection(palindromes) + " ";
        output += getPalindromeNodes(palindromes);
        return output;
    }

    public static void main(String[] args) {
        StringBuilder output = new StringBuilder();

        DG dg = new DG();

        Scanner scanner = new Scanner(System.in);
        StringBuilder tokens = new StringBuilder();

        try {
            while (scanner.hasNextLine()) {
                int amountOfNodes = scanner.nextInt();
                tokens.append(amountOfNodes);
                for (int i = 0; i <= amountOfNodes * 2; i++) {
                    tokens.append(scanner.nextLine()).append("\n");
                }
                output.append(dg.inputToOutput(tokens.toString().substring(0, tokens.toString().length() - 1))).append("\n");
                tokens = new StringBuilder();
            }
            scanner.close();
            System.out.println(output.toString().substring(0, output.toString().length() - 1));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}

