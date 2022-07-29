package palindrome;

import java.util.*;

public class DAG {

    private Palindromes[][] lookUp;

    public Palindromes findPalindrome(DirectedGraph graph) {
        Palindromes biggestPalindromes;
        lookUp = new Palindromes[graph.getSize()][graph.getSize()];
        ArrayList<int[]> pairs = getNodesBySymbol(graph);

        Palindromes output = new Palindromes();
        for (int i=0; i<graph.getSize(); i++){
            output.add(new int[]{i}, graph);
        }
        biggestPalindromes = output;

        for (int[] pair : pairs) {
            if (lookUp[pair[0]][pair[1]] == null) {
                Palindromes localBiggest = extendPalindrome(graph, pair[0], pair[1]);
                if (lookUp[pair[0]][pair[1]].getPalindromeSize() < 2 && graph.getAdjacenceMatrix()[pair[0]][pair[1]] > 0) {
                    Palindromes palindromes = new Palindromes();
                    palindromes.add(pair, graph);
                    localBiggest = palindromes;
                    lookUp[pair[0]][pair[1]] = palindromes;
                }
                biggestPalindromes = checkPalindrome(localBiggest, biggestPalindromes);
            }
        }

        return biggestPalindromes;
    }

    private Palindromes extendPalindrome(DirectedGraph graph, int begin, int end) {
        Palindromes localBiggest = new Palindromes();
        for (int[] pair : getCommonSymbols(graph.getOutgoing(begin), graph.getIncomming(end), begin, end, graph)) {
            if (lookUp[pair[0]][pair[1]] != null) {
                Palindromes palindromes = new Palindromes();
                for (Palindrome palindrome : lookUp[pair[0]][pair[1]].getPalindromes()) {
                    palindromes.add(concat(begin, palindrome, end), graph);
                }
                localBiggest = checkPalindrome(palindromes, localBiggest);
            } else if (pair[0] == pair[1]) {
                Palindromes palindromes = new Palindromes();
                palindromes.add(concat(begin, new Palindrome(Collections.singletonList(pair[0]), graph), end), graph);
                localBiggest = checkPalindrome(palindromes, localBiggest);
            } else {
                Palindromes differentPalindromes = extendPalindrome(graph, pair[0], pair[1]);
                if (differentPalindromes.getPalindromeSize() < 2 && graph.getAdjacenceMatrix()[pair[0]][pair[1]] > 0) {
                    Palindromes palindromes = new Palindromes();
                    palindromes.add(concat(begin, new Palindrome(pair), end), graph);
                    lookUp[pair[0]][pair[1]] = palindromes;
                    differentPalindromes = palindromes;
                }
                localBiggest = checkPalindrome(differentPalindromes, localBiggest);
            }
        }
        lookUp[begin][end] = localBiggest;
        return localBiggest;
    }

    private Palindromes checkPalindrome(Palindromes local, Palindromes biggest) {
        if (local.getPalindromeSize() > biggest.getPalindromeSize()) {
            Palindromes output = new Palindromes();
            output.addAll(local);
            return output;
        } else if (local.getPalindromeSize() == biggest.getPalindromeSize()) {
            biggest.addAll(local);
        }
        return biggest;
    }

    private ArrayList<int[]> getNodesBySymbol(DirectedGraph G) {
        HashMap<Character, ArrayList<Integer>> nodesBySymbol = new HashMap<>();
        for (int i = 0; i < G.getSize(); i++) {
            nodesBySymbol.putIfAbsent(G.getSymbol(i), new ArrayList<>());
            nodesBySymbol.get(G.getSymbol(i)).add(i);
        }
        ArrayList<int[]> output = new ArrayList<>();
        for (ArrayList<Integer> nodes : nodesBySymbol.values()) {
            for (int i = 0; i < nodes.size(); i++) {
                for (int j = 0; j < nodes.size(); j++) {
                    if (i != j) output.add(new int[]{nodes.get(i), nodes.get(j)});
                }
            }
        }
        return output;
    }

    private ArrayList<int[]> getCommonSymbols(ArrayList<Integer> n1, ArrayList<Integer> n2, int begin, int end, DirectedGraph dg2) {
        ArrayList<int[]> pairs = new ArrayList<>();
        n1.remove(Integer.valueOf(end));
        n2.remove(Integer.valueOf(begin));
        for (int node1 : n1) {
            for (int node2 : n2) {
                if (dg2.getSymbol(node1) == dg2.getSymbol(node2)) {
                    pairs.add(new int[]{node1, node2});
                }
            }
        }
        return pairs;
    }

    private ArrayList<Integer> concat(int n1, Palindrome palindrome, int n2) {
        ArrayList<Integer> output = new ArrayList<>();
        output.add(n1);
        output.addAll(palindrome.getPalindrome());
        output.add(n2);
        return output;
    }

    private String getIntersection(Palindromes palindromes) {
        long bitvector = palindromes.getPalindromes().get(0).getBitvector();
        for (int i=1; i<palindromes.getPalindromes().size(); i++){
            bitvector &= palindromes.getPalindromes().get(i).getBitvector();
        }
        String output = bitvectorToString(bitvector);
        if (output.equals("")){
            return "/";
        }
        return output;
    }

    private String bitvectorToString(long bitvector){
        StringBuilder output = new StringBuilder();
        String symbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i=0; i<symbols.length(); i++){
            long base = 1;
            base <<= i;
            if ((bitvector & base) == base){
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
        DAG dag = new DAG();
        StringBuilder output = new StringBuilder();
        String[] splitInput = input.split("\n");
        int position = 0;
        while (position + 1<splitInput.length ){
            int size = Integer.parseInt(splitInput[position]);
            DirectedGraph directedGraph = new DirectedGraph(size);
            for (int i=0; i < size; i++) {
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
            output.append(palindromesToOutput(dag.findPalindrome(directedGraph))).append("\n");
        }
        if (output.toString().length() == 0) {
            return "0 /";
        }
        return output.toString().substring(0,output.toString().length()-1);
    }

    private String palindromesToOutput(Palindromes palindromes) {
        int size = palindromes.getPalindromeSize();
        if (size == 0) {
            return "0 /";
        }
        String output = size + " ";
        output += getIntersection(palindromes) + " ";
        output += getPalindromeNodes(palindromes);
        return output;
    }

    public static void main(String[] args) {

        StringBuilder output = new StringBuilder();

        DAG dag = new DAG();

        Scanner scanner = new Scanner(System.in);
        StringBuilder tokens = new StringBuilder();

        try {
            while (scanner.hasNextLine()){
                int amountOfNodes = scanner.nextInt();
                tokens.append(amountOfNodes);
                for (int i=0; i<=amountOfNodes*2 ;i++){
                    tokens.append(scanner.nextLine()).append("\n");
                }
                output.append(dag.inputToOutput(tokens.toString().substring(0,tokens.toString().length()-1))).append("\n");
                tokens = new StringBuilder();
            }
            scanner.close();
            System.out.println(output.toString().substring(0,output.toString().length()-1));
        }catch (Exception e){
            System.out.println("de invoer is niet correct !");
        }


    }
}
