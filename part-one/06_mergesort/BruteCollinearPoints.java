import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private final ArrayList<LineSegment> segments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Points array cannot be null");
        }

        Point[] copyOfPoints = new Point[points.length];
        Point currentPoint = null;
        for (int i = 0; i < points.length; i++) {
            currentPoint = points[i];
            if (currentPoint == null) {
                throw new IllegalArgumentException("Points array cannot contain null points");
            }
            copyOfPoints[i] = currentPoint;
        }

        Arrays.sort(copyOfPoints);
        for (int i = 0; i < copyOfPoints.length - 1; i++) {
            if (copyOfPoints[i].compareTo(copyOfPoints[i + 1]) == 0) {
                throw new IllegalArgumentException("Points array cannot contain duplicate points");
            }
        }

        segments = findCollinearPoints(copyOfPoints);
    }

    /**
     * Finds all line segments containing 4 points that are collinear.
     * This method checks every combination of 4 points to see if they are collinear.
     * 
     * Note that `points` should be "sorted" so that the <b>(line) segments</b> will contain
     * all points (otherwise, only their <b>lines</b> will contain all points)
     * @param points
     * @return
     */
    private static ArrayList<LineSegment> findCollinearPoints(Point[] points) {
        ArrayList<LineSegment> segments = new ArrayList<>();
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                for (int k = j + 1; k < points.length; k++) {
                    for (int l = k + 1; l < points.length; l++) {
                        if (points[i].slopeTo(points[j]) == points[j].slopeTo(points[k]) &&
                            points[j].slopeTo(points[k]) == points[k].slopeTo(points[l])) {
                            // Found a collinear segment
                            segments.add(new LineSegment(points[i], points[l]));
                        }
                    }
                }
            }
        }

        return segments;
    }

    /**
     * Returns the number of line segments containing 4 points
     * @return the number of line segments
     */
    public int numberOfSegments() {
        return segments.size();
    }

    /**
     * Returns the line segments that containing 4 points
     * @return the array of line segments
     */
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[0]);
    }
}
