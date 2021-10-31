import java.util.ArrayList;

public class Solution{
    private static boolean isPrime(ArrayList<Integer> primes, int n) {
        for(Integer prime : primes) {
            if(n % prime == 0) {
                return false;   
            }
            else if(prime * prime > n) {
                return true;
            }
        }
        return true;
    }

    public static String solution(int i) {
        ArrayList<Integer> primes = new ArrayList<Integer>();
        int n = 2;
        int width = 1;
        int nextPowOfTen = 10;

        int lastPrime = 2;
        int digit = 0;
        while(digit < i) {
            if(isPrime(primes, n)) {
                primes.add(n);
                digit += width;
                lastPrime = n;
            }
            n++;
            if(n >= nextPowOfTen) {
                width++;
                nextPowOfTen *= 10;
            }
        }
        
        String segment = ((Integer)lastPrime).toString();
        while(digit < i + 5) {
            if(isPrime(primes, n)) {
                primes.add(n);
                digit += width;
                segment += ((Integer)n).toString();
            }
            n++;
            if(n >= nextPowOfTen) {
                width++;
                nextPowOfTen *= 10;
            }
        }

        int retStart = segment.length() - (digit - i);
        return segment.substring(retStart, retStart + 5);
    }
}