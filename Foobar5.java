import java.util.PriorityQueue;
import java.lang.Comparable;

public class Foobar5 {
    private enum ProcessedState {
        Unprocessed,
        ProcessedHasBroken,
        ProcessedHasntBroken,
    }

    private static class SearchNode implements Comparable<SearchNode> {
        public int x;
        public int y;
        public int distance;
        public int estFinalDist;
        public boolean hasBrokenWall;

        public SearchNode(int x, int y, int distance, int estFinaLDist, boolean hasBrokenWall) {
            this.x = x;
            this.y = y;
            this.distance = distance;
            this.estFinalDist = estFinaLDist;
            this.hasBrokenWall = hasBrokenWall;
        }

        @Override
        public int compareTo(SearchNode o) {
            return estFinalDist < o.estFinalDist ? -1 : estFinalDist == o.estFinalDist ? 0 : 1;
        }
    }

    private static void addIfPossible(PriorityQueue<SearchNode> queue, ProcessedState[][] processed, int[][] map, int x, int y, int distance, boolean hasBrokenWall) {
        if((hasBrokenWall && processed[y][x] == ProcessedState.Unprocessed) || 
           (!hasBrokenWall && processed[y][x] != ProcessedState.ProcessedHasntBroken)) {
            int height = map.length;
            int width = map[0].length;
            int estFinalDist = distance + manhattan(x, y, width - 1, height - 1);

            if(map[y][x] == 0) { //Space
                queue.add(new SearchNode(x, y, distance, estFinalDist, hasBrokenWall));
            }
            else if(!hasBrokenWall) { //Break wall
                queue.add(new SearchNode(x, y, distance, estFinalDist, true));
            }
        }
    }

    private static int manhattan(int x, int y, int endX, int endY) {
        int xDist = endX - x;
        int yDist = endY - y;
        int absX = xDist < 0 ? -xDist : xDist;
        int absY = yDist < 0 ? -yDist : yDist;
        return absX + absY;
    }
    
    public static int solution(int[][] map) {
        PriorityQueue<SearchNode> queue = new PriorityQueue<>();
        
        ProcessedState[][] processed = new ProcessedState[map.length][]; //0 = unprocessed, 1 = processed (has broken wall), 2 = processed (has not broken wall)
        for(int i = 0; i < processed.length; i++) {
            processed[i] = new ProcessedState[map[i].length];
            for(int j = 0; j < processed[i].length; j++) {
                processed[i][j] = ProcessedState.Unprocessed;
            }
        }

        int height = map.length;
        int width = map[0].length;

        SearchNode start = new SearchNode(0, 0, 1, width + height - 2, false);
        queue.add(start);

        SearchNode curr;
        do {
            curr = queue.remove();
            int x = curr.x;
            int y = curr.y;
            if((curr.hasBrokenWall && processed[y][x] == ProcessedState.Unprocessed) || 
               (!curr.hasBrokenWall && processed[y][x] != ProcessedState.ProcessedHasntBroken)) {
                processed[y][x] = curr.hasBrokenWall ? ProcessedState.ProcessedHasBroken : ProcessedState.ProcessedHasntBroken;
                if(x >= 1) {
                    addIfPossible(queue, processed, map, x - 1, y, curr.distance + 1, curr.hasBrokenWall);
                }
                if(x <= width - 2) {
                    addIfPossible(queue, processed, map, x + 1, y, curr.distance + 1, curr.hasBrokenWall);
                }
                if(y >= 1) {
                    addIfPossible(queue, processed, map, x, y - 1, curr.distance + 1, curr.hasBrokenWall);
                }
                if(y <= height - 2) {
                    addIfPossible(queue, processed, map, x, y + 1, curr.distance + 1, curr.hasBrokenWall);
                }
            }

        } while(curr.x != width - 1 || curr.y != height - 1);

        return curr.distance;
    }

    public static void main(String[] args) {
        //int[][] map = {{0, 1, 1, 0}, {0, 0, 0, 1}, {1, 1, 0, 0}, {1, 1, 1, 0}};
        int[][] map = {
        {0, 0, 0, 0, 0, 0}, 
        {1, 1, 1, 1, 1, 0},
        {0, 0, 0, 0, 0, 0}, 
        {0, 1, 1, 1, 0, 1}, 
        {0, 1, 1, 1, 1, 1}, 
        {0, 0, 0, 1, 0, 0}};
        int solution = solution(map);
        System.out.println(solution);
    }
}