public class Geometry {
    final static double eps = 1e-9;

    class Point {
        double x, y;

        public Point(double x, double y) {
            super();
            this.x = x;
            this.y = y;
        }

        double dist(Point an) {
            double dx = an.x - x;
            double dy = an.y - y;
            return Math.sqrt(dx * dx + dy * dy);
        }
    }

    class Line {
        double A, B, C;
        boolean normed;

        Line(Point p1, Point p2) {
            A = p2.y - p1.y;
            B = p1.x - p2.x;
            C = -A * p1.x - B * p1.y;
        }

        void norm() {
            double z = Math.sqrt(A * A + B * B);
            A /= z;
            B /= z;
            C /= z;
            normed = true;
        }

        double dist(Point p) {
            if (!normed)
                norm();
            return Math.abs(A * p.x + B * p.y + C);
        }

        Point intersec(Line another) {
            double zn = A * another.B - another.A * B;
            if (Math.abs(zn) <= eps)
                return null;
            double x = another.C * B - another.B * C;
            double y = another.A * C - another.C * A;
            return new Point(x / zn, y / zn);
        }
    }

    boolean insideSorted(double x, double xLeft, double xRight) {
        return x >= xLeft - eps && x <= xRight + eps;
    }

    boolean inside(double x, double xLeft, double xRight) {
        return insideSorted(x, Math.min(xLeft, xRight), Math.max(xLeft, xRight));
    }

    class Segment {
        Point p1, p2;
        Line l;

        public Segment(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
            l = new Line(p1, p2);
        }

        boolean onSegment(Point p) {
            if (l.dist(p) > eps)
                return false;
            return inside(p.x, p1.x, p2.x) && inside(p.y, p1.y, p2.y);
        }

        boolean isPoint() {
            return p1.dist(p2) <= eps;
        }
    }

    class IntersectionObject {
        boolean isPoint;
        Point point;
        Segment segment;

        IntersectionObject(Point point) {
            isPoint = true;
            this.point = point;
        }

        IntersectionObject(Segment segment) {
            isPoint = false;
            this.segment = segment;
        }
    }

    boolean intersectSorted(double xLeft, double xRight, double yLeft,
                            double yRight) {
        return Math.max(xLeft, yLeft) - eps <= Math.min(xRight, yRight);
    }

    boolean intersect(double xLeft, double xRight, double yLeft, double yRight) {
        return intersectSorted(Math.min(xLeft, xRight),
                Math.max(xLeft, xRight), Math.min(yLeft, yRight),
                Math.max(yLeft, yRight));
    }

    IntersectionObject intersect(Segment s1, Segment s2) {
        if (!intersect(s1.p1.x, s1.p2.x, s2.p1.x, s2.p2.x)
                || !intersect(s1.p1.y, s1.p2.y, s2.p1.y, s2.p2.y))
            return null;
        boolean isPoint1 = s1.isPoint();
        boolean isPoint2 = s2.isPoint();
        if (isPoint1 && isPoint2)
            return new IntersectionObject(s1.p1);
        if (isPoint1) {
            if (s2.onSegment(s1.p1))
                return new IntersectionObject(s1.p1);
            return null;
        }
        if (isPoint2) {
            if (s1.onSegment(s2.p1))
                return new IntersectionObject(s2.p1);
            return null;
        }
        Point intersecton = s1.l.intersec(s2.l);
        if (intersecton == null) {
            if (Math.abs(s1.l.dist(s2.p1)) > eps
                    || Math.abs(s2.l.dist(s1.p1)) > eps)
                return null;
            double xLeft1 = Math.min(s1.p1.x, s1.p2.x);
            double xLeft2 = Math.min(s2.p1.x, s2.p2.x);
            double xRight1 = Math.max(s1.p1.x, s1.p2.x);
            double xRight2 = Math.max(s2.p1.x, s2.p2.x);
            double yLeft1 = Math.min(s1.p1.y, s1.p2.y);
            double yLeft2 = Math.min(s2.p1.y, s2.p2.y);
            double yRight1 = Math.max(s1.p1.y, s1.p2.y);
            double yRight2 = Math.max(s2.p1.y, s2.p2.y);
            Point p1 = new Point(Math.max(xLeft1, xLeft2), Math.max(yLeft1,
                    yLeft2));
            Point p2 = new Point(Math.min(xRight1, xRight2), Math.min(yRight1,
                    yRight2));
            if (s1.l.dist(p1) > eps || s1.l.dist(p2) > eps) {
                p1 = new Point(Math.max(xLeft1, xLeft2), Math.min(yRight1,
                        yRight2));
                p2 = new Point(Math.min(xRight1, xRight2), Math.max(yLeft1,
                        yLeft2));
            }
            if (p1.dist(p2) <= eps)
                return new IntersectionObject(p1);
            Segment newSeg = new Segment(p1, p2);
            return new IntersectionObject(newSeg);
        } else {
            if (!s1.onSegment(intersecton) || !s2.onSegment(intersecton))
                return null;
            return new IntersectionObject(intersecton);
        }
    }
}
