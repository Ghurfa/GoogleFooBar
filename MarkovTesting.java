public class MarkovTesting {
    private static void printArr(int[] nums) {
        for(int num : nums) {
            System.out.print(num + " ");
        }
        System.out.println();
    }

    private static int[][] copy2dArr(int[][] original) {
        int[][] copy = new int[original.length][];
        for(int i = 0; i < original.length; i++) {
            copy[i] = new int[original[i].length];
            for(int j = 0; j < copy[i].length; j++) {
                copy[i][j] = original[i][j];
            }
        }
        return copy;
    }
    public static void main(String[] args) {
        for(int i = 0; i < 1000; i++){
            int numTerminals =  i % 3 + 1;
            int[][] testCase = MarkovGenerator.GenerateMarkov(numTerminals + i % 3 + 2, numTerminals);
            int[][] copy = copy2dArr(testCase);
            if(!MarkovGenerator.verifyNoDeadEnds(testCase)) {
                throw new RuntimeException();
            }
            int[] solution = Foobar4.solution(testCase);
            //printArr(solution);
            int[] measured = MarkovMeasurer.measureProbabilities(testCase, 10000);
            //printArr(measured);
            double sqErr = MarkovMeasurer.calculateSquaredError(solution, measured);
            if(sqErr > 0.001) 
            {
                int y = 2;
            }
        }
        System.out.print("a");
    }
}