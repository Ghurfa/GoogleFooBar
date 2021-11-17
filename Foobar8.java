import java.util.HashMap;
import java.lang.RuntimeException;

public class Foobar8 {
    private static class Angle {
        private int x;
        private int y;
        
        public Angle(int x, int y) {
            for(int factor = 2; factor <= x && factor <= y; ) {
                boolean isCommonFactor = true;
                
                if(x % factor == 0 && y % factor == 0) {
                    x /= factor;
                    y /= factor;
                }
                else {
                    factor++;
                }
            }
            this.x = x;
            this.y = y;
        }
        
        public int getX() {
            return x;
        }
        
        public int getY() {
            return y;
        }
        
        @Override
        public boolean equals(Object other) {
            Angle otherAngle = (Angle)other;
            return otherAngle.x == x && otherAngle.y == y;
        }
        
        @Override
        public int hashCode() {
            return 107 * x + 97 * y;
        }
    }
    
    private static class Position {
        private int x;
        private int y;
        private boolean isTrainer;
        
        public Position(int x, int y, boolean isTrainer) {
            this.x = x;
            this.y = y;
            this.isTrainer = isTrainer;
        }
        
        public int getX() {
            return x;
        }
        
        public int getY() {
            return y;
        }
        
        public boolean isTrainer() {
            return isTrainer;
        }
    }
    
    //Returns whether the point is within maxDist, even if it wasn't added
    private static boolean addPoint(HashMap<Angle, Position> map, int x, int y, boolean isTrainer, int maxDistSq) {
        int distSq = x * x + y * y;
        if(distSq <= maxDistSq) {
            //Assumes that closer points are added first
            Angle angle = new Angle(x, y);
            if (!map.containsKey(angle)) {
                map.put(angle, new Position(x, y, isTrainer));
            }
            return true;
        } else {
            return false;
        }
    }
    
    private static int calculate1DPos(int roomSize, int roomPos, int innerPos) {
        if(roomPos % 2 == 0) {
            return roomSize * roomPos + innerPos;
        } else {
            return roomSize * roomPos + (roomSize - innerPos);  
        }
    }
    
    public static int solution(int[] dimensions, int[] your_position, int[] trainer_position, int distance) {
        int width = dimensions[0];
        int length = dimensions[1];
        int yourX = 0;
        int yourY = 0;
        int trainerX = trainer_position[0] - your_position[0];
        int trainerY = trainer_position[1] - your_position[1];
        int maxDistSq = distance * distance;
        HashMap<Angle, Position> points = new HashMap<>();
        
        //Loop outward in a spiral pattern into mirror rooms
        int roomX = 0;
        int roomY = 0;
        int stepLen = 1;
        int direction = -1;
        boolean foundValid = true;
        while(foundValid) {
            foundValid = false;
            for(int i = 0; i < stepLen; i++) {
                int yrX = calculate1DPos(width, roomX, yourX);
                int yrY = calculate1DPos(width, roomY, yourY);
                int trX = calculate1DPos(width, roomX, trainerX);
                int trY = calculate1DPos(width, roomY, trainerY);
                foundValid |= addPoint(points, yrX, yrY, false, maxDistSq);
                foundValid |= addPoint(points, trX, trY, true, maxDistSq);
                roomY += direction;
            }
            
            for(int i = 0; i < stepLen; i++) {
                int yrX = calculate1DPos(width, roomX, yourX);
                int yrY = calculate1DPos(width, roomY, yourY);
                int trX = calculate1DPos(width, roomX, trainerX);
                int trY = calculate1DPos(width, roomY, trainerY);
                foundValid |= addPoint(points, yrX, yrY, false, maxDistSq);
                foundValid |= addPoint(points, trX, trY, true, maxDistSq);
                roomX += direction;
            }
            direction *= -1;
            stepLen++;
        }
        
        int possibleShotCount = 0;
        for(Angle angle : points.keySet()) {
            Position point = points.get(angle);
            if (point.isTrainer()) {
                possibleShotCount++;
            }
        }
        
        return possibleShotCount;
    }

    public static void main(String[] args) {
        int[] dimensions = new int[] {3, 2};
        int[] your_position = new int[] {1, 1};
        int[] trainer_position = new int[] {2, 1};
        System.out.println(solution(dimensions, your_position, trainer_position, 4));
    }
}