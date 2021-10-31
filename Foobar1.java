import java.util.ArrayList;
//import java.io.FileOutputStream;

public class Foobar1{

    public static String generatePrimes() {
        ArrayList<Integer> primes = new ArrayList<Integer>();
        int n = 2;
        String ret = "";
        while(ret.length() < 10000) {
            if(isPrime(primes, n)) {
                primes.add(n);
                ret += n;
            }
            n++;
        }
        return ret.substring(0, 10000);
    }

    private static String primesString = "23571113171923293137414347535961677173798389971011031071091131271311"; //This was 10,000 characters but vscode kept freezing
    public static String megaStringSolution(int i) {
        return primesString.substring(i, i + 5);
    }

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

    public static void main(String[] args) {
        for(int i = 0; i < 10; i++) {
            System.out.println(solution(i));
        }
    }
}