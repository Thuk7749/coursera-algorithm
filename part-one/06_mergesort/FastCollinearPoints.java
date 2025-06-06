import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {
    private final ArrayList<LineSegment> segments;
    private static final int MIN_POINTS = 4;

    public FastCollinearPoints(Point[] points) {
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

    private static ArrayList<LineSegment> findCollinearPoints(Point[] sortedPoints) {
        ArrayList<LineSegment> segments = new ArrayList<>();
        for (int i = 0; i < sortedPoints.length; i++) {
            Point origin = sortedPoints[i];
            Point[] copyOfPoints = Arrays.copyOf(sortedPoints, sortedPoints.length);
            ArrayList<LineSegment> collinearSegments = findCollinearPointsFromOnePoint(
                origin, copyOfPoints
            );

            for (LineSegment segment : collinearSegments) {
                segments.add(segment);
            }
        }

        return segments;
    }

    private static ArrayList<LineSegment> findCollinearPointsFromOnePoint(Point origin, Point[] sortedPoints) {
        Comparator<Point> slopeComparator = origin.slopeOrder();
        Arrays.sort(sortedPoints, slopeComparator);

        ArrayList<LineSegment> segments = new ArrayList<>();
        Point startPoint = null;
        int currentCount = 0;
        for (int i = 0; i < sortedPoints.length; i++) {
            if (i == 0 || slopeComparator.compare(sortedPoints[i], sortedPoints[i - 1]) == 0) {
                currentCount++;
                if (startPoint == null) {
                    startPoint = sortedPoints[i];
                }
            } else {
                if (currentCount >= MIN_POINTS - 1 && origin.compareTo(startPoint) < 0) {
                    segments.add(new LineSegment(origin, sortedPoints[i - 1]));
                }
                currentCount = 1;
                startPoint = sortedPoints[i];
            }
        }
        if (currentCount >= MIN_POINTS - 1 && origin.compareTo(startPoint) < 0) {
            segments.add(new LineSegment(origin, sortedPoints[sortedPoints.length - 1]));
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
