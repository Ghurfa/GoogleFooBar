import java.util.ArrayList;
import java.util.Stack;

public class Foobar7 {
    private static class Pairing {
        public int indexA;
        public int indexB;
        
        public Pairing(int a, int b) {
            indexA = a;
            indexB = b;
        }
    }

    private static boolean doesGameEnd(int startA, int startB) {
        int slowA = startA;
        int slowB = startB;
        int fastA = startA;
        int fastB = startB;
        if(fastA > fastB) {
            fastA -= fastB;
            fastB *= 2;
        }
        else {
            fastB -= fastA;
            fastA *= 2;
        }
        while(slowA != slowB && slowA != fastA) {
            if(slowA > slowB) {
                slowA -= slowB;
                slowB *= 2;
            }
            else {
                slowB -= slowA;
                slowA *= 2;
            }
            if(fastA > fastB) {
                fastA -= fastB;
                fastB *= 2;
            }
            else {
                fastB -= fastA;
                fastA *= 2;
            }
            if(fastA > fastB) {
                fastA -= fastB;
                fastB *= 2;
            }
            else {
                fastB -= fastA;
                fastA *= 2;
            }
        }
        return slowA == slowB;
    }

    private static int getMostPairings(ArrayList<Pairing> pairings, boolean[] usedGuards, int startAt) {
        int best = 0;
        for(int i = startAt; i < pairings.size(); i++) {
            Pairing pair = pairings.get(i);
            if(!usedGuards[pair.indexA] && !usedGuards[pair.indexB]) {
                usedGuards[pair.indexA] = true;
                usedGuards[pair.indexB] = true;
                int score = getMostPairings(pairings, usedGuards, i + 1) + 1;
                if(best < score) {
                    best = score;
                }
                usedGuards[pair.indexA] = false;
                usedGuards[pair.indexB] = false;
            }
        }
        return best;
    }

    public static int solution(int[] banana_list) {
        ArrayList<Pairing> infinitePairings = new ArrayList();

        for(int i = 0; i < banana_list.length - 1; i++) {
            int countA = banana_list[i];
            for(int j = i + 1; j < banana_list.length; j++) {
                int countB = banana_list[j];
                if(!doesGameEnd(countA, countB)) {
                    infinitePairings.add(new Pairing(i, j));
                }
            }
        }

        boolean[] usedGuards = new boolean[banana_list.length];
        return banana_list.length - 2 * getMostPairings(infinitePairings, usedGuards, 0);
    }

    public static void main(String[] args) {
        int[] bananaList = {2, 10};
        System.out.println(solution(bananaList));
    }
}
