import java.util.LinkedList;

public class Foobar2 {
    public static int solution(int src, int dest) {
        LinkedList<Integer> queue = new LinkedList<Integer>();
        
        int[] distances = new int[64];

        queue.addLast(src);
        distances[src] = 0;

        while(true) {
            int square = queue.removeFirst();
            int distance = distances[square];
            
            if(square == dest) {
                return distance;
            }
            
            if(square >= 17 && square % 8 >= 1) {
                int upLeft = square - 17; //Up two, left one
                addSquare(queue, distances, upLeft, distance + 1);
            }
            if(square >= 16 && square % 8 <= 6) {
                int upRight = square - 15;
                addSquare(queue, distances, upRight, distance + 1);
            }
            if(square >= 8 && square % 8 <= 5) {
                int rightUp = square - 6;
                addSquare(queue, distances, rightUp, distance + 1);
            }
            if(square <= 53 && square % 8 <= 5) {
                int rightDown = square + 10;
                addSquare(queue, distances, rightDown, distance + 1);
            }
            if(square <= 46 && square % 8 <= 6) {
                int downRight = square + 17;
                addSquare(queue, distances, downRight, distance + 1);
            }
            if(square <= 47 && square % 8 >= 1) {
                int downLeft = square + 15;
                addSquare(queue, distances, downLeft, distance + 1);
            }
            if(square <= 55 && square % 8 >= 2) {
                int leftDown = square + 6;
                addSquare(queue, distances, leftDown, distance + 1);
            }
            if(square >= 10 && square % 8 >= 2) {
                int leftUp = square - 10;
                addSquare(queue, distances, leftUp, distance + 1);
            }
        }
    }

    private static void addSquare(LinkedList<Integer> queue, int[] distances, int square, int distance) {
        if(distances[square] == 0) {
            distances[square] = distance;
            queue.addLast(square);
        }
    }

    public static void main(String[] args) {
        System.out.println(solution(0, 63));
    }
}