/*package functions;

import java.io.Externalizable;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.IOException;

public class LinkedListTabulatedFunctionExternal implements TabulatedFunction, Externalizable {
    private static final double EPS = 1e-9;

    public static class FunctionNode {
        private FunctionPoint point;
        private FunctionNode prev;
        private FunctionNode next;

        public FunctionNode(FunctionPoint point) {
            this.point = point;
        }

        public FunctionNode() {}
    }

    private FunctionNode head;
    private int pointsCount;

    public LinkedListTabulatedFunctionExternal() {
        this.head = new FunctionNode(null);
        head.next = head;
        head.prev = head;
        this.pointsCount = 0;
    }

    public LinkedListTabulatedFunctionExternal(FunctionPoint[] points) {
        if (points == null || points.length < 2) {
            throw new IllegalArgumentException("количество точек должно быть не меньше двух");
        }
        for (int i = 1; i < points.length; i++) {
            if (points[i].getX() <= points[i - 1].getX()) {
                throw new IllegalArgumentException("точки должны быть упорядочены по Х");
            }
        }

        this.head = new FunctionNode(null);
        head.next = head;
        head.prev = head;
        this.pointsCount = 0;

        for (int i = 0; i < points.length; i++) {
            addNodeToTail(new FunctionPoint(points[i]));
        }
    }

    public LinkedListTabulatedFunctionExternal(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не меньше двух");
        }

        this.head = new FunctionNode(null);
        head.next = head;
        head.prev = head;
        this.pointsCount = 0;

        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            addNodeToTail(new FunctionPoint(x, 0.0));
        }
    }

    public LinkedListTabulatedFunctionExternal(double leftX, double rightX, double[] values) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не меньше двух");
        }

        this.head = new FunctionNode(null);
        head.next = head;
        head.prev = head;
        this.pointsCount = 0;

        double step = (rightX - leftX) / (values.length - 1);
        for (int i = 0; i < values.length; i++) {
            double x = leftX + i * step;
            addNodeToTail(new FunctionPoint(x, values[i]));
        }
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(pointsCount);

        FunctionNode current = head.next;
        while (current != head) {
            out.writeDouble(current.point.getX());
            out.writeDouble(current.point.getY());
            current = current.next;
        }
    }

    public void readExternal(ObjectInput in) throws IOException {
        pointsCount = in.readInt();

        if (pointsCount < 2) {
            throw new IOException("количество точек должно быть не меньше двух");
        }

        this.head = new FunctionNode(null);
        head.next = head;
        head.prev = head;
        this.pointsCount = 0;

        double prevX = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < pointsCount; i++) {
            double x = in.readDouble();
            double y = in.readDouble();

            if (x <= prevX) {
                throw new IOException("точки не упорядочены по X");
            }

            addNodeToTail(new FunctionPoint(x, y));
            prevX = x;
        }
    }

    private FunctionNode getNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границу");
        }
        FunctionNode current;
        if (index < pointsCount / 2) {
            current = head.next;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = head.prev;
            for (int i = pointsCount - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current;
    }

    private FunctionNode addNodeToTail(FunctionPoint point) {
        FunctionNode node = new FunctionNode(point);
        FunctionNode last = head.prev;

        last.next = node;
        node.prev = last;
        node.next = head;
        head.prev = node;

        pointsCount++;
        return node;
    }

    private FunctionNode addNodeByIndex(int index) {
        if (index < 0 || index > pointsCount) {
            throw new IndexOutOfBoundsException("Индекс " + index + " выходит за границу");
        }
        FunctionNode newNode = new FunctionNode(null);
        FunctionNode nodeAtIndex;
        if (index == pointsCount) {
            nodeAtIndex = head;
        } else {
            nodeAtIndex = getNodeByIndex(index);
        }
        newNode.prev = nodeAtIndex.prev;
        newNode.next = nodeAtIndex;

        nodeAtIndex.prev.next = newNode;
        nodeAtIndex.prev = newNode;
        pointsCount++;
        return newNode;
    }

    private FunctionNode deleteNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new IndexOutOfBoundsException("Индекс " + index + " выходит за границу");
        }
        if (pointsCount < 2) {
            throw new IllegalStateException("слишком мало точек");
        }
        FunctionNode nodeToDelete = getNodeByIndex(index);
        FunctionNode prevNode = nodeToDelete.prev;
        FunctionNode nextNode = nodeToDelete.next;

        prevNode.next = nextNode;
        nextNode.prev = prevNode;

        pointsCount--;

        nodeToDelete.prev = null;
        nodeToDelete.next = null;

        return nodeToDelete;
    }

    public int getPointsCount() {
        return pointsCount;
    }

    public FunctionPoint getPoint(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границу");
        }
        FunctionNode node = getNodeByIndex(index);
        return new FunctionPoint(node.point);
    }

    public void setPoint(int index, FunctionPoint point) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границу");
        }
        double x = point.getX();

        if (index > 0) {
            double leftX = getNodeByIndex(index - 1).point.getX();
            if (x <= leftX) {
                throw new IllegalArgumentException("начение Х новой точки должно быть больше Х соседней левой точки");
            }
        }

        if (index < pointsCount - 1) {
            double rightX = getNodeByIndex(index + 1).point.getX();
            if (x >= rightX) {
                throw new IllegalArgumentException("начение Х новой точки должно быть больше Х соседней правой точки");
            }
        }
        getNodeByIndex(index).point = new FunctionPoint(point);
    }

    public double getRightDomainBorder() {
        if (pointsCount == 0) {
            throw new IllegalStateException("Функция не содержит точек");
        }
        return head.prev.point.getX();
    }

    public double getLeftDomainBorder() {
        if (pointsCount == 0) {
            throw new IllegalStateException("Функция не содержит точек");
        }
        return head.next.point.getX();
    }

    public double getFunctionValue(double x) {
        if (pointsCount == 0) {
            throw new IllegalStateException("Функция не содержит точек");
        }

        double leftBorder = getLeftDomainBorder();
        double rightBorder = getRightDomainBorder();
        if (x < leftBorder || x > rightBorder) {
            throw new IllegalStateException("х вне области определения");
        }

        FunctionNode current = head.next;
        while (current != head) {
            double x1 = current.point.getX();
            double x2 = current.next.point.getX();
            if (x >= x1 && x <= x2) {
                double y1 = current.point.getY();
                double y2 = current.next.point.getY();
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
            current = current.next;
        }

        return Double.NaN;
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        FunctionNode current = head.next;
        while (current != head) {
            if (Math.abs(point.getX() - current.point.getX()) < EPS) {
                throw new InappropriateFunctionPointException("Точка с таким X уже существует");
            }
            current = current.next;
        }

        current = head.next;
        while (current != head && current.point.getX() < point.getX()) {
            current = current.next;
        }

        FunctionNode newNode = new FunctionNode(new FunctionPoint(point));
        newNode.next = current;
        newNode.prev = current.prev;
        current.prev.next = newNode;
        current.prev = newNode;

        pointsCount++;
    }

    public void deletePoint(int index) {
        if (pointsCount <= 3) {
            throw new IllegalStateException("точек должно быть больше 3");
        }
        if (index < 0 || index >= pointsCount) {
            throw new IndexOutOfBoundsException("Индекс вне диапазона");
        }

        FunctionNode current = head.next;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        current.prev.next = current.next;
        current.next.prev = current.prev;

        if (current == head) {
            head = current.next;
        }

        pointsCount--;
    }

    public void setPointY(int index, double y) {
        if (index < 0 || index >= pointsCount) {
            throw new IndexOutOfBoundsException("Индекс вне диапазона");
        }

        FunctionNode current = head.next;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        current.point.setY(y);
    }

    public double getPointY(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new IndexOutOfBoundsException("Индекс вне диапазона");
        }

        FunctionNode current = head.next;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        return current.point.getY();
    }

    public void setPointX(int index, double x) {
        if (index < 0 || index >= pointsCount) {
            throw new IndexOutOfBoundsException("Индекс вне диапазона");
        }

        FunctionNode current = head.next;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        current.point.setX(x);
    }

    public double getPointX(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new IndexOutOfBoundsException("Индекс вне диапазона");
        }

        FunctionNode current = head.next;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        return current.point.getX();
    }
    @Override
    public LinkedListTabulatedFunctionExternal clone() {
        FunctionPoint[] pointsArray = new FunctionPoint[pointsCount];
        FunctionNode current = head.next;
        int index = 0;

        while (current != head) {
            pointsArray[index] = new FunctionPoint(current.point);
            current = current.next;
            index++;
        }
        return new LinkedListTabulatedFunctionExternal(pointsArray);
    }

}*/