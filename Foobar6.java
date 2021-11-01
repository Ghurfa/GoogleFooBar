import java.math.BigInteger;

public class Foobar6 {
    //Group of zero digits followed by higher-significance group of ones
    private static class Island {
        public int zeroCount;
        public int oneCount;
        public Island nextGreater;

        public Island(int zeros, int ones, Island greater) {
            zeroCount = zeros;
            oneCount = ones;
            nextGreater = greater;
        }
    }

    private static Island toArchipelago(BigInteger num) {
        int zeroCount;
        for(zeroCount = 0; num.mod(BigInteger.TWO).equals(BigInteger.ZERO); zeroCount++) {
            num = num.divide(BigInteger.TWO);
        }
        int oneCount;
        for(oneCount = 0; num.mod(BigInteger.TWO).equals(BigInteger.ONE); oneCount++) {
            num = num.divide(BigInteger.TWO);
        }
        Island leastSignificant = new Island(zeroCount, oneCount, null);
        Island last = leastSignificant;

        while(!num.equals(BigInteger.ZERO)) {
            for(zeroCount = 0; num.mod(BigInteger.TWO).equals(BigInteger.ZERO); zeroCount++) {
                num = num.divide(BigInteger.TWO);
            }
            for(oneCount = 0; num.mod(BigInteger.TWO).equals(BigInteger.ONE); oneCount++) {
                num = num.divide(BigInteger.TWO);
            }
            Island newIsland = new Island(zeroCount, oneCount, null);
            last.nextGreater = newIsland;
            last = newIsland;
        }
        return leastSignificant;
    }

    public static int solution(String x) {
        Island curr = toArchipelago(new BigInteger(x));

        int opCount = 0;
        while(curr.nextGreater != null) {
            opCount += curr.zeroCount; //Shift
            if(curr.oneCount == 1) { //Subtract and shift
                opCount += 2;
            }
            else if(curr.nextGreater.zeroCount == 1) { //Add and shift
                opCount += curr.oneCount + 1;
                curr.nextGreater.zeroCount = 0;
                curr.nextGreater.oneCount++;
            }
            else { //Add, shift, subtract
                opCount += curr.oneCount + 2;
            }
            curr = curr.nextGreater;
        }

        opCount += curr.zeroCount;
        opCount += curr.oneCount == 1 ? 0 : curr.oneCount == 2 ? 2 : curr.oneCount + 1;

        return opCount;
    }
    
    public static void main(String[] args) {
        //System.out.println(solution("19"));
        for(int i = 1; i <= 256; i++) {
            System.out.println(i + ": " + solution(new Integer(i).toString()) + (i % 2 == 0 ? "\t" + solution(new Integer(i/2).toString()) : ""));
        }
    }
}
