import java.util.Arrays;

public class Foobar3 {
    public static int solution(int[] l) {
        Arrays.sort(l);

        //Count modulos
        int sum = 0;
        int oneModCount = 0; //# of digits that are congruent to 1 (modulo 3)
        int twoModCount = 0;
        for(int i = 0; i < l.length; i++) {
            sum += l[i];
            if(l[i] % 3 == 1) {
                oneModCount++;
            }
            else if(l[i] % 3 == 2) {
                twoModCount++;
            }
        }

        //Remove smallest possible digits to make the sum divisible by 3
        if(sum % 3 == 1){
            if(oneModCount >= 1) {
                remove(l, 1, 1);
            }
            else if(twoModCount >= 2) {
                remove(l, 2, 2);
            }
            else return 0;
        }
        else if(sum % 3 == 2){
            if(twoModCount >= 1) {
                remove(l, 2, 1);
            }
            else if(oneModCount >= 2) {
                remove(l, 1, 2);
            }
            else return 0;
        }

        //Build number
        int number = 0;
        for(int i = l.length - 1; i >= 0; i--) {
            if(l[i] != -1) {
                number = number * 10 + l[i];
            }
        }
        return number;
    }

    private static void remove(int[] digits, int mod, int count) {
        int removed = 0;
        for(int i = 0; i < digits.length; i++) {
            if(digits[i] % 3 == mod) {
                digits[i] = -1;
                removed++;
                if(removed == count) {
                    return;
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(solution(new int[]{3, 1, 4, 1}));
    }
}