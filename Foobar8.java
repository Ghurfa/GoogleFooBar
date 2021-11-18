import java.util.HashMap;

public class Foobar8 {
    private static class Angle {
        private int x;
        private int y;

        public Angle(int x, int y) {
            // Reduce
            int factor = 2;
            int posX = x < 0 ? -x : x;
            int posY = y < 0 ? -y : y;
            int stopAt = posX > posY ? posX : posY;
            while (factor <= stopAt) {
                if (x % factor == 0 && y % factor == 0) {
                    x /= factor;
                    y /= factor;
                } else {
                    factor++;
                }
            }
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object other) {
            Angle otherAngle = (Angle) other;
            return otherAngle.x == x && otherAngle.y == y;
        }

        @Override
        public int hashCode() {
            return 107 * x + y;
        }
    }

    // Returns whether the point is within maxDist regardless of whether it was added
    private static boolean addPoint(HashMap<Angle, Boolean> points, int x, int y, boolean isTrainer, int maxDistSq) {
        int distSq = x * x + y * y;
        if (distSq <= maxDistSq) {
            Angle angle = new Angle(x, y);
            if (!points.containsKey(angle)) {
                points.put(angle, isTrainer);
            }
            // Cannot be closer than a previous value because of the spiral adding order
            return true;
        } else {
            return false;
        }
    }

    private static int calculate1DPos(int roomSize, int roomPos, int innerPos, int offset) {
        if (roomPos % 2 == 0) {
            return roomSize * roomPos + innerPos - offset;
        } else {
            return roomSize * roomPos + (roomSize - innerPos) - offset;
        }
    }

    public static int solution(int[] dimensions, int[] your_position, int[] trainer_position, int distance) {
        int width = dimensions[0];
        int length = dimensions[1];
        int yourX = your_position[0];
        int yourY = your_position[1];
        int trainerX = trainer_position[0];
        int trainerY = trainer_position[1];
        int maxDistSq = distance * distance;
        HashMap<Angle, Boolean> points = new HashMap<>();

        // Loop outward in a spiral pattern
        int roomX = 0;
        int roomY = 0;
        int stepLen = 1;
        int direction = -1;
        boolean withinRange = true;
        while (withinRange) {
            withinRange = false;
            // For each "mirror" room, calculate your and the trainer's mirrored positions
            // Add the positions to the map if they are within range & have a unique angle
            for (int i = 0; i < stepLen; i++) {
                int yrX = calculate1DPos(width, roomX, yourX, yourX);
                int yrY = calculate1DPos(length, roomY, yourY, yourY);
                int trX = calculate1DPos(width, roomX, trainerX, yourX);
                int trY = calculate1DPos(length, roomY, trainerY, yourY);
                withinRange |= addPoint(points, yrX, yrY, false, maxDistSq);
                withinRange |= addPoint(points, trX, trY, true, maxDistSq);
                roomY += direction;
            }

            for (int i = 0; i < stepLen; i++) {
                int yrX = calculate1DPos(width, roomX, yourX, yourX);
                int yrY = calculate1DPos(length, roomY, yourY, yourY);
                int trX = calculate1DPos(width, roomX, trainerX, yourX);
                int trY = calculate1DPos(length, roomY, trainerY, yourY);
                withinRange |= addPoint(points, yrX, yrY, false, maxDistSq);
                withinRange |= addPoint(points, trX, trY, true, maxDistSq);
                roomX += direction;
            }
            direction *= -1;
            stepLen++;
        }

        int possibleShotCount = 0;
        for (Angle angle : points.keySet()) {
            if (points.get(angle)) { // Is a trainer
                possibleShotCount++;
            }
        }

        return possibleShotCount;
    }

    public static void main(String[] args) {
        int[] dimensions = new int[] { 300, 275 };
        int[] your_position = new int[] { 150, 150 };
        int[] trainer_position = new int[] { 105, 100 };
        System.out.println(solution(dimensions, your_position, trainer_position, 500));
    }
}
