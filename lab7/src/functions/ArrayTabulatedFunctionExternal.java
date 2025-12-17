/*package functions;

import java.io.Externalizable;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.IOException;


public class ArrayTabulatedFunctionExternal implements TabulatedFunction, Externalizable {
    private FunctionPoint[] points;
    private int pointsCount;
    private static final double EPS = 1e-9;

    public ArrayTabulatedFunctionExternal() {
        this.points = new FunctionPoint[2];
        this.pointsCount = 2;
        this.points[0] = new FunctionPoint(0.0, 0.0);
        this.points[1] = new FunctionPoint(1.0, 0.0);
    }

    public ArrayTabulatedFunctionExternal(FunctionPoint[] points) {
        if (points == null || points.length < 2) {
            throw new IllegalArgumentException("количество точек должно быть не меньше двух");
        }
        for (int i = 1; i < points.length; i++) {
            if (points[i].getX() <= points[i - 1].getX()) {
                throw new IllegalArgumentException("точки должны быть упорядочены по Х");
            }
        }
        this.pointsCount = points.length;
        this.points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            this.points[i] = new FunctionPoint(points[i]);
        }
    }

    public ArrayTabulatedFunctionExternal(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("левая граница должна быть меньше правой");
        }
        if (pointsCount < 2) {
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

    public ArrayTabulatedFunctionExternal(double leftX, double rightX, double[] values) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("левая граница должна быть меньше правой");
        }
        if (values == null || values.length < 2) {
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

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(pointsCount);

        for (int i = 0; i < pointsCount; i++) {
            out.writeDouble(points[i].getX());
            out.writeDouble(points[i].getY());
        }
        out.writeInt(points.length);
    }

    public void readExternal(ObjectInput in) throws IOException{
        pointsCount = in.readInt();

        if (pointsCount < 2) {
            throw new IOException(" количество точек должно быть не меньше двух");
        }

        points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            points[i] = new FunctionPoint(x, y);
        }

        for (int i = 1; i < pointsCount; i++) {
            if (points[i].getX() <= points[i - 1].getX()) {

                throw new IOException("точки не упорядочены по X");
            }
        }

        int capacity = in.readInt();

        if (capacity > pointsCount) {
            FunctionPoint[] newPoints = new FunctionPoint[capacity];
            System.arraycopy(points, 0, newPoints, 0, pointsCount);
            points = newPoints;
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
            if (Math.abs(x - left) < EPS) {
                return points[0].getY();
            } else {
                return Double.NaN;
            }
        }

        for (int i = 0; i < pointsCount - 1; i++) {
            double x0 = points[i].getX();
            double x1 = points[i + 1].getX();
            if (Math.abs(x - x0) < EPS) return points[i].getY();
            if (Math.abs(x - x1) < EPS) return points[i + 1].getY();
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
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границу");
        }
        return new FunctionPoint(points[index]);
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границу");
        }

        double newX = point.getX();
        if (index > 0 && newX <= points[index - 1].getX()) {
            throw new InappropriateFunctionPointException("Значение Х новой точки должно быть больше Х соседней левой точки ");
        }
        if (index < pointsCount - 1 && newX >= points[index + 1].getX()) {
            throw new InappropriateFunctionPointException("Значение Х новой точки должно быть больше Х соседней правой точки");
        }

        points[index] = new FunctionPoint(point);
    }

    public double getPointX(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границу");
        }
        return points[index].getX();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границу");
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
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границу");
        }
        return points[index].getY();
    }

    public void setPointY(int index, double y) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границу");
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
    public Object clone() {
        try {
            FunctionPoint[] clonedPoints = new FunctionPoint[points.length];
            for (int i = 0; i < pointsCount; i++) {
                clonedPoints[i] = (FunctionPoint) points[i].clone();
            }

            ArrayTabulatedFunctionExternal cloned = (ArrayTabulatedFunctionExternal) super.clone();
            cloned.points = clonedPoints;
            cloned.pointsCount = this.pointsCount;

            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("клонирование не сработало", e);
        }
    }
}
*/