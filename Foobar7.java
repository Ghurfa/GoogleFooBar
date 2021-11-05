import java.util.Set;
import java.util.HashSet;

public class Foobar7 {
    private static class GuardNode {
        public Set<GuardNode> neighbors = new HashSet();
        public boolean isUsed = false;
        public int value; //for debugging purposes

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

        return banana_list.length - 2 * countMostPairings(nodes);
    }

    public static void main(String[] args) {
        int[] bananaList = new int[17];
        for(int i = 0; i < 17; i++) {
            bananaList[i] = i;
        }
        //int[] bananaList = {16, 2, 6, 1};
        System.out.println(solution(bananaList));
    }
}
