package functions;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.io.*;
import java.util.StringJoiner;

public class TabulatedFunctions {

    private static TabulatedFunctionFactory factory =
            new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();

    private TabulatedFunctions() {}

    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory newFactory) {
        if (newFactory == null) {
            throw new IllegalArgumentException("Фабрика не может быть null");
        }
        factory = newFactory;
    }

    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
        return factory.createTabulatedFunction(points);
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
        return factory.createTabulatedFunction(leftX, rightX, pointsCount);
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
        return factory.createTabulatedFunction(leftX, rightX, values);
    }

    public static void outputTabulatedFunction(
            TabulatedFunction function,
            OutputStream out) throws IOException {

        DataOutputStream dataOut = new DataOutputStream(out);

        int count = function.getPointsCount();
        dataOut.writeInt(count);
        for (int i = 0; i < count; i++) {
            dataOut.writeDouble(function.getPointX(i));
            dataOut.writeDouble(function.getPointY(i));
        }
        dataOut.flush();
    }

    public static TabulatedFunction inputTabulatedFunction(
            InputStream in) throws IOException {

        DataInputStream dataIn = new DataInputStream(in);

        int count = dataIn.readInt();
        if (count < 2) {
            throw new IllegalArgumentException("количество точек должно быть не меньше двух");
        }

        FunctionPoint[] points = new FunctionPoint[count];
        for (int i = 0; i < count; i++) {
            double x = dataIn.readDouble();
            double y = dataIn.readDouble();
            points[i] = new FunctionPoint(x, y);
        }

        return createTabulatedFunction(points);
    }

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {

        int count = function.getPointsCount();
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add(String.valueOf(count));

        for (int i = 0; i < count; i++) {
            joiner.add(String.valueOf(function.getPointX(i)));
            joiner.add(String.valueOf(function.getPointY(i)));
        }

        out.write(joiner.toString());
        out.flush();
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {

        StreamTokenizer tokenizer = new StreamTokenizer(in);

        tokenizer.nextToken();
        int count = (int) tokenizer.nval;
        if (count < 2) {
            throw new IllegalArgumentException("количество точек должно быть не меньше двух");
        }

        FunctionPoint[] points = new FunctionPoint[count];
        for (int i = 0; i < count; i++) {
            tokenizer.nextToken();
            double x = tokenizer.nval;
            tokenizer.nextToken();
            double y = tokenizer.nval;
            points[i] = new FunctionPoint(x, y);
        }

        return createTabulatedFunction(points);
    }

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {

        if (leftX < function.getLeftDomainBorder()
                || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("границы выходят за область определения функции");
        }

        if (pointsCount < 2) {
            throw new IllegalArgumentException("количество точек должно быть не меньше двух");
        }

        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            values[i] = function.getFunctionValue(x);
        }

        return createTabulatedFunction(leftX, rightX, values);
    }

    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> clazz, FunctionPoint[] points) {
        try {
            Constructor<? extends TabulatedFunction> constructor = clazz.getConstructor(FunctionPoint[].class);
            return constructor.newInstance((Object) points);

        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> clazz, double leftX, double rightX, int pointsCount) {
        try {
            Constructor<? extends TabulatedFunction> constructor = clazz.getConstructor(double.class, double.class, int.class);
            return constructor.newInstance(leftX, rightX, pointsCount);

        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }
    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> clazz, double leftX, double rightX, double[] values) {
        try {
            Constructor<? extends TabulatedFunction> constructor = clazz.getConstructor(double.class, double.class, double[].class);
            return constructor.newInstance(leftX, rightX, values);

        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }
    public static TabulatedFunction tabulate(Class<? extends TabulatedFunction> clazz, Function function, double leftX, double rightX, int pointsCount) {

        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("границы выходят за область определения функции");
        }

        if (pointsCount < 2) {
            throw new IllegalArgumentException("количество точек должно быть не меньше двух");
        }

        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            values[i] = function.getFunctionValue(x);
        }
        return createTabulatedFunction(clazz, leftX, rightX, values);
    }
}