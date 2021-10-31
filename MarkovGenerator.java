import java.util.Random;

public class MarkovGenerator {
    private static Random random = new Random();

    public static int[][] GenerateMarkov(int size, int numTerminals) {
        int[][] ret = new int[size][];
        do {
            for(int i = 0; i < size - numTerminals; i++) {
                ret[i] = new int[size];
                for(int j = 0; j < size; j++){
                    ret[i][j] = random.nextInt(2);
                }
            }
            for(int i = numTerminals; i < size; i++){
                ret[i] = new int[size];
            }
        }
        while(!verifyNoDeadEnds(ret));
        return ret;
    }

    public static boolean verifyNoDeadEnds(int[][] markov) {
        boolean[] canTerminate = new boolean[markov.length];

        for(int i = 0; i < markov.length; i++) {
            if(isTerminal(markov[i])) {
                canTerminate[i] = true;
            }
        }

        for(int i = 0; i < canTerminate.length; i++) {
            if(!canTerminate[i]) {
                int[] row = markov[i];
                for(int j = 0; j < row.length; j++) {
                    if(row[j] != 0 && canTerminate[j]) {
                        canTerminate[i] = true;
                        i = -1;
                        break;
                    }
                }
            }
        }

        //Reaches end only when all can terminate or last pass failed to find new terminator-reachers
        for(int i = 0; i < canTerminate.length; i++) {
            if(!canTerminate[i]) {
                return false;
            }
        }
        return true;
    }

    private static boolean isTerminal(int[] row) {
        for(int num : row) {
            if(num != 0) {
                return false;
            }
        }
        return true;
    }
}