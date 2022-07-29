package palindrome;

import java.util.ArrayList;

public class PalindromesParts {
    private int size;
    private int begin;
    private int end;
    private ArrayList<PalindromesParts> beginParts;
    private ArrayList<PalindromesParts> endParts;
    private boolean isDouble;


    public PalindromesParts() {
        begin = -1;
        end = -1;
        isDouble = false;
        this.size = 0;
    }

    public boolean isDouble() {
        return isDouble;
    }

    public void setDouble(boolean aDouble) {
        isDouble = aDouble;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public ArrayList<PalindromesParts> getBeginParts() {
        return beginParts;
    }

    public void setBeginParts(ArrayList<PalindromesParts> beginParts) {
        this.beginParts = beginParts;
    }

    public ArrayList<PalindromesParts> getEndParts() {
        return endParts;
    }

    public void setEndParts(ArrayList<PalindromesParts> endParts) {
        this.endParts = endParts;
    }

    public void addAll(PalindromesParts parts){
        beginParts.addAll(parts.getBeginParts());
        endParts.addAll(parts.getEndParts());
    }
}
