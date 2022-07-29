package palindrome;

import java.util.ArrayList;

public class DirectedGraph {
    private int[][] adjacenceMatrix;
    private Character[] symbols;
    private int nodes = 0;
    private int size;

    DirectedGraph(int size) {
        adjacenceMatrix = new int[size][size];
        symbols = new Character[size];
        this.size = size;
    }

    public DirectedGraph(int[][] ajacenceMatrix, int size, Character[] symbols){
        this.adjacenceMatrix = ajacenceMatrix;
        this.size = size;
        this.symbols = symbols;
    }

    void addNode(Character ch, int[] outgoing){
        symbols[nodes] = ch;
        for (int endNode : outgoing){
            adjacenceMatrix[nodes][endNode]++;
        }
        nodes++;
    }

    ArrayList<Integer> getOutgoing(int node){
        ArrayList<Integer> output = new ArrayList<>();
        for (int i=0; i<size; i++){
            if (adjacenceMatrix[node][i] > 0){
                output.add(i);
            }
        }
        return output;
    }

    ArrayList<Integer> getIncomming(int node){
        ArrayList<Integer> output = new ArrayList<>();
        for (int i=0; i<size; i++){
            if (adjacenceMatrix[i][node] > 0){
                output.add(i);
            }
        }
        return output;
    }

    int[][] getAdjacenceMatrix() {
        return adjacenceMatrix;
    }

    Character getSymbol(int node){
        return symbols[node];
    }

    int getSize() {
        return size;
    }
}
