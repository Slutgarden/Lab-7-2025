package functions;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.io.Serializable;

public class ArrayTabulatedFunction implements TabulatedFunction, Serializable{

    private FunctionPoint[] points;
    private int pointsCount;
    private static final double EPS = 1e-9;

    public ArrayTabulatedFunction(FunctionPoint[] points){
        if(points == null || points.length < 2){
            throw new IllegalArgumentException("количество точек должно быть не меньше двух");
        }
        for (int i = 1; i < points.length; i++){
            if (points[i].getX() <= points[i -1].getX()){
                throw new IllegalArgumentException("точки должны быть упорядочены по Х");
            }
        }
        this.pointsCount = points.length;
        this.points =  new FunctionPoint[pointsCount];
        for(int i = 0; i < pointsCount; i++){
            this.points[i] = new FunctionPoint(points[i]);
        }
    }

    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {

        if(leftX >= rightX){
            throw new IllegalArgumentException("левая граница должна быть меньше правой");
        }
        if(pointsCount<2){
            throw new IllegalArgumentException("количество точек должно быть не меньше двух");
        }
        this.pointsCount = pointsCount;
        this.points = new FunctionPoint[pointsCount];

        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            this.points[i] = new FunctionPoint(x, 0.0);
        }
    }

    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) {

        if(leftX >= rightX){
            throw new IllegalArgumentException("левая граница должна быть меньше правой");
        }
        if(values == null ||  values.length < 2){
            throw new IllegalArgumentException("количество точек должно быть не меньше двух");
        }

        this.pointsCount = values.length;
        this.points = new FunctionPoint[pointsCount];

        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            double y = values[i];
            this.points[i] = new FunctionPoint(x, y);
        }
    }
    public double getLeftDomainBorder() {
        if (pointsCount == 0) return Double.NaN;
        return points[0].getX();
    }

    public double getRightDomainBorder() {
        if (pointsCount == 0) return Double.NaN;
        return points[pointsCount - 1].getX();
    }

    public double getFunctionValue(double x) {
        if (pointsCount == 0) return Double.NaN;

        double left = getLeftDomainBorder();
        double right = getRightDomainBorder();
        if (x < left || x > right) return Double.NaN;

        if (pointsCount == 1) {
            if (Math.abs(x - left) <EPS) {
                return points[0].getY();
            } else {
                return Double.NaN;
            }
        }

        for (int i = 0; i < pointsCount - 1; i++) {
            double x0 = points[i].getX();
            double x1 = points[i + 1].getX();
            if (Math.abs(x - x0) <EPS) return points[i].getY();
            if (Math.abs(x - x1) <EPS) return points[i + 1].getY();
            if (x > x0 && x < x1) {
                double y0 = points[i].getY();
                double y1 = points[i + 1].getY();
                return y0 + (y1 - y0) * (x - x0) / (x1 - x0);
            }
        }

        return Double.NaN;
    }

    public int getPointsCount() {
        return pointsCount;
    }

    public FunctionPoint getPoint(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс "+ index +" выходит за границу");
        }
        return new FunctionPoint(points[index]);
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("Индекс "+ index +" выходит за границу");
        }

        double newX = point.getX();
        if (index > 0 && newX <= points[index - 1].getX()){
            throw new InappropriateFunctionPointException("Значение Х новой точки должно быть больше Х соседней левой точки ");
        }
        if (index < pointsCount - 1 && newX >= points[index + 1].getX()){
            throw new InappropriateFunctionPointException("Значение Х новой точки должно быть больше Х соседней правой точки");
        }

        points[index] = new FunctionPoint(point);
    }

    public double getPointX(int index){
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс "+ index +" выходит за границу");
        }
        return points[index].getX();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс "+ index +" выходит за границу");
        }

        if (index > 0 && x <= points[index - 1].getX()) {
            throw new InappropriateFunctionPointException("Значение Х новой точки должно быть больше Х соседней левой точки ");
        }
        if (index < pointsCount - 1 && x >= points[index + 1].getX()) {
            throw new InappropriateFunctionPointException("Значение Х новой точки должно быть больше Х соседней правой точки");
        }
        points[index].setX(x);
    }

    public double getPointY(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс "+ index +" выходит за границу");
        }
        return points[index].getY();
    }

    public void setPointY(int index, double y) {
        if (index < 0 || index >= pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("Индекс "+ index +" выходит за границу");
        }
        points[index].setY(y);
    }
    public void deletePoint(int index) {
        if (index < 0 || index >= pointsCount  || pointsCount <= 1){
            throw new FunctionPointIndexOutOfBoundsException("Индекс "+ index +" выходит за границу");
        }
        if(pointsCount <3 ){
            throw new IllegalStateException("слишком мало точек");
        }
        for (int i = index; i < pointsCount - 1; i++) {
            points[i] = points[i + 1];
        }
        points[pointsCount - 1] = null;
        pointsCount--;
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        if (point == null) return;

        for(int i = 0; i < pointsCount; i++){
            if(Math.abs(points[i].getX() - point.getX()) < EPS){
                throw new InappropriateFunctionPointException("Точка с таким Х уже существует");
            }
        }

        if (pointsCount == points.length) {
            FunctionPoint[] newPoints = new FunctionPoint[points.length * 2];
            System.arraycopy(points, 0, newPoints, 0, pointsCount);
            points = newPoints;
        }

        int insertIndex = 0;
        while (insertIndex < pointsCount && points[insertIndex].getX() < point.getX()) {
            insertIndex++;
        }

        for (int i = pointsCount; i > insertIndex; i--) {
            points[i] = points[i - 1];
        }

        points[insertIndex] = new FunctionPoint(point);
        pointsCount++;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < pointsCount; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append("(").append(points[i].getX()).append("; ").append(points[i].getY()).append(")");
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof ArrayTabulatedFunction) {
            ArrayTabulatedFunction other = (ArrayTabulatedFunction) o;
            if (this.pointsCount != other.pointsCount) return false;

            for (int i = 0; i < pointsCount; i++) {
                if (!this.points[i].equals(other.points[i])) {
                    return false;
                }
            }
            return true;
        }
        if (o instanceof TabulatedFunction) {
            TabulatedFunction other = (TabulatedFunction) o;
            if (this.getPointsCount() != other.getPointsCount()) return false;

            for (int i = 0; i < pointsCount; i++) {
                FunctionPoint thisPoint = this.getPoint(i);
                FunctionPoint otherPoint = other.getPoint(i);

                if (!thisPoint.equals(otherPoint)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = pointsCount;
        for (int i = 0; i < pointsCount; i++) {
            hash ^= points[i].hashCode();
        }
        return hash;
    }

    @Override
    public Object clone() {
        try {
            FunctionPoint[] clonedPoints = new FunctionPoint[points.length];
            for (int i = 0; i < pointsCount; i++) {
                clonedPoints[i] = (FunctionPoint) points[i].clone();
            }

            ArrayTabulatedFunction cloned = (ArrayTabulatedFunction) super.clone();
            cloned.points = clonedPoints;
            cloned.pointsCount = this.pointsCount;

            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("клонирование не сработало", e);
        }
    }
    @Override
    public Iterator<FunctionPoint> iterator() {
        return new Iterator<FunctionPoint>() {

            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < pointsCount;
            }

            @Override
            public FunctionPoint next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                FunctionPoint p = points[currentIndex++];
                return new FunctionPoint(p);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
    public static class ArrayTabulatedFunctionFactory
            implements TabulatedFunctionFactory {

        @Override
        public TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
            return new ArrayTabulatedFunction(points);
        }

        @Override
        public TabulatedFunction createTabulatedFunction(
                double leftX, double rightX, int pointsCount) {
            return new ArrayTabulatedFunction(leftX, rightX, pointsCount);
        }

        @Override
        public TabulatedFunction createTabulatedFunction(
                double leftX, double rightX, double[] values) {
            return new ArrayTabulatedFunction(leftX, rightX, values);
        }
    }
}
