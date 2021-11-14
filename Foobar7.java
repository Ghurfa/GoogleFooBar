import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class Foobar7 {
    private static class GuardNode implements Comparable{
        public Set<GuardNode> neighbors = new TreeSet();
        public boolean isUsed = false;
        public int value; //for debugging purposes

        public int compareTo(Object other) {
            return value - ((GuardNode)other).value;
        }

        public void addNeighbor(GuardNode node) {
            neighbors.add(node);
        }

        public int degree() {
            return neighbors.size();
        }

        public void disable() {
            for(GuardNode neighbor : neighbors) {
                neighbor.neighbors.remove(this);
            }
            isUsed = true;
        }

        public void enable() {
            for(GuardNode neighbor : neighbors) {
                neighbor.neighbors.add(this);
            }
            isUsed = false;
        }
    }

    private static boolean doesGameEnd(int startA, int startB) {
        int currA = startA;
        int currB = startB;
        while(currA != currB) {
            if(currA % 2 != currB % 2) {
                return false;
            }
            else if(currA % 2 == 0) {
                currA /= 2;
                currB /= 2;
            }
            else { //Both are odd
                if(currA < currB) {
                    currB -= currA;
                    currA *= 2;
                }
                else {
                    currA -= currB;
                    currB *= 2;
                }
            }
        }
        return true;
    }

    private static int selectLowestDegree(GuardNode[] nodes) {
        int lowestDeg = Integer.MAX_VALUE;
        int lowestDegIndex = -1;
        for(int i = 0; i < nodes.length; i++) {
            if(!nodes[i].isUsed && nodes[i].degree() > 0 && nodes[i].degree() < lowestDeg) {
                lowestDegIndex = i;
            }
        }
        return lowestDegIndex;
    }

    private static int countMostPairings(GuardNode[] nodes) {
        int lowestDegIndex = selectLowestDegree(nodes);
        if(lowestDegIndex >= 0) {
            GuardNode loneliest = nodes[lowestDegIndex];
            loneliest.disable();

            int bestScore = 0;
            for(GuardNode neighbor : loneliest.neighbors) {
                if(neighbor.isUsed) {
                    throw new RuntimeException("shouldn't happen?");
                }
                neighbor.disable();
                int score = countMostPairings(nodes) + 1;
                if(score > bestScore) {
                    bestScore = score;
                }
                neighbor.enable();
            }

            loneliest.enable();
            return bestScore;
        }
        else return 0;
    }

    private static <E> boolean hasIntersection(Set<E> a, Set<E> b) {
        for(E item : a) {
            if(b.contains(item)) {
                return true;
            }
        }
        return false;
    }

    public static int solution(int[] banana_list) {
        GuardNode[] nodes = new GuardNode[banana_list.length];

        for(int i = 0; i < nodes.length; i++) {
            nodes[i] = new GuardNode();
            nodes[i].value = banana_list[i];
        }

        //Build graph
        for(int i = 0; i < banana_list.length - 1; i++) {
            int countA = banana_list[i];
            for(int j = i + 1; j < banana_list.length; j++) {
                int countB = banana_list[j];    
                boolean ends = doesGameEnd(countA, countB);
                if(!ends) {
                    nodes[i].addNeighbor(nodes[j]);
                    nodes[j].addNeighbor(nodes[i]);
                }
            }
        }
        
        String out = "";
        for(int i = 0; i < nodes.length; i++) {
            GuardNode node = nodes[i];
            String print = "";
            for(int j = 0; j < nodes.length; j++) {
                GuardNode neighbor = nodes[j];
                if(node.neighbors.contains(neighbor)) {
                    print += "(" + node.value + ", " + neighbor.value+")" + ", ";
                }
            }
            if(print.length() > 0) {
                out += print.substring(0, print.length() - 2) + "\n";
            }
        }

        StringSelection selection = new StringSelection(out);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection); //paste into desmos

        return 1;
    }

    public static void main(String[] args) {
        int[] bananaList = new int[30];
        for(int i = 0; i < bananaList.length; i++) {
            bananaList[i] = i;
        }
        //int[] bananaList = {1, 3, 2, 6};
        solution(bananaList);
    }
}
