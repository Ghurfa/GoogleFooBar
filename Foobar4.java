public class Foobar4 {
    public static int[] solution(int[][] m) {
        //Count terminals
        int terminalCount = 0;
        for(int i = 0; i < m.length; i++) {
            if(isTerminal(m[i])) {
                terminalCount++;
            }
        }
        
        if(isTerminal(m[0])) {
            int[] ret = new int[terminalCount + 1];
            ret[0] = 1;
            ret[terminalCount] = 1;
            return ret;
        }
        else {
            //Calculate probability distribution of each row recursively
            int[] rowNumStack = new int[m.length];
            rowNumStack[0] = 0;
            int sp = 1;
            calculateRow(m, 0, rowNumStack, sp);

            //Build return value
            int denom = 0;
            int j = 0;
            int[] ret = new int[terminalCount + 1];
            for(int i = 0; i < m.length; i++) {
                if(isTerminal(m[i])) {
                    int numerator = m[0][i];
                    ret[j++] = numerator;
                    denom += numerator;
                }
            }

            ret[ret.length - 1] = denom;
            return ret;
        }
    }

    private static boolean isTerminal(int[] row) {
        for(int val : row) {
            if(val != 0) {
                return false;
            }
        }
        return true;
    }

    private static void calculateRow(int[][] rows, int rowNum, int[] rowNumStack, int sp) {
        int[] row = rows[rowNum];
        row[rowNum] = 0;
        for(int i = 0; i < row.length; i++) {
            if(row[i] > 0 && !isInStack(i, rowNumStack, sp)) {
                rowNumStack[sp] = i;
                calculateRow(rows, i, rowNumStack, sp + 1);
                substituteAndSimplify(rows, rowNum, i);
            }
        }
    }

    private static void substituteAndSimplify(int[][] rows, int parentIndex, int childIndex) {
        int[] parentRow = rows[parentIndex];
        int[] childRow = rows[childIndex];

        int childCommonDenom = 0;
        for(int numer : childRow) {
            childCommonDenom += numer;
        }

        if(childCommonDenom != 0) { //Non-terminal child
            //1. Scale up parent numerators by child denominator
            //2. Redistribute child numerator in parent according to the child's distribution
            int childNumer = parentRow[childIndex];
            for(int i = 0; i < parentRow.length; i++) {
                parentRow[i] = parentRow[i] * childCommonDenom + childNumer * childRow[i];
            }

            parentRow[childIndex] = 0;
            parentRow[parentIndex] = 0;

            reduceRow(parentRow);
        }
    }

    private static boolean isInStack(int num, int[] stack, int sp) {
        for(int i = 0; i < sp; i++) {
            if(stack[i] == num) {
                return true;
            }
        }
        return false;
    }

    //Simplifies fraction
    private static void reduceRow(int[] row) {
        int smallestNonZero = row[0];
        for(int i = 1; i < row.length; i++) {
            if(row[i] != 0 && row[i] < smallestNonZero || smallestNonZero == 0) {
                smallestNonZero = row[i];
            }
        }

        //Efficiency be damned - too tired
        for(int factor = 2; factor <= smallestNonZero; ) {
            boolean isCommonFactor = true;
            for(int i = 0; i < row.length; i++) {
                if(row[i] % factor != 0) {
                    isCommonFactor = false;
                    break;
                }
            }
            if(isCommonFactor) {
                for(int i = 0; i < row.length; i++) {
                    row[i] /= factor;
                }
            }
            else {
                factor++;
            }
        }
    }

    public static void main(String[] args) {
        //int[][] input = {{0, 2, 1, 0, 0}, {0, 0, 0, 3, 4}, {0, 0, 0, 0, 0}, {0, 0, 0, 0,0}, {0, 0, 0, 0, 0}};
        int[][] input = {{0, 2, 0, 1}, {1, 0, 2, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}};
        int[] output = solution(input);

        for(int i : output) {
            System.out.print(i + ", ");
        }
        System.out.println();
    }
}