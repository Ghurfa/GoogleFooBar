import java.util.Random;

public class MarkovMeasurer {
    private static Random random = new Random(1);
    public static int[] measureProbabilities(int[][] markov, int count){
        int terminalCount = 0;
        for(int i = 0; i < markov.length; i++) {
            int[] row = markov[i];
            if(sumRow(row) == 0) {
                terminalCount++;
            }
        }

        int[] measuredProbs = new int[terminalCount + 1];
        measuredProbs[measuredProbs.length - 1] = count;

        for(int i = 0; i < count; i++) {
            int result = testOnce(markov);

            //Determine index in measuredProbs
            int terminalNum = 0;
            for(int j = 0; j < result; j++) {
                if(sumRow(markov[j]) == 0) {
                    terminalNum++;
                }
            }

            measuredProbs[terminalNum]++;
        }
        return measuredProbs;
    }

    public static double calculateSquaredError(int[] expectedProbs, int[] measuredProbs){
        double totalSqError = 0.0;
        for(int i = 0; i < measuredProbs.length; i++) {
            double expected = (double)expectedProbs[i] / expectedProbs[expectedProbs.length - 1];
            double measured = (double)measuredProbs[i] / measuredProbs[measuredProbs.length - 1];
            double error = expected - measured;
            totalSqError += error * error;
        }

        return totalSqError;
    }

    private static int testOnce(int[][] markov) {
        int ptr = 0;
        
        int[] row = markov[0];
        int rowSum = sumRow(row);
        while(rowSum > 0) {
            int selection = random.nextInt(rowSum);
            int cumulProb = 0;
            int i;
            for(i = 0; cumulProb <= selection && i < row.length; i++) {
                cumulProb += row[i];
            }
            ptr = i - 1;
            row = markov[ptr];
            rowSum = sumRow(row);   
        }

        return ptr;
    }

    private static int sumRow(int[] row) {
        int sum = 0;
        for(int i = 0; i < row.length; i++) {
            sum += row[i];
        }
        return sum;
    }
}