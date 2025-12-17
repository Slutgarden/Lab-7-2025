import functions.*;
import functions.basic.*;

public class Main7 {

    public static void main(String[] args) {
        try {
            task1();
            task2();
            task3();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ИТЕРАТОР
    private static void task1() {
        System.out.println("Задание 1");

        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(new FunctionPoint[] {
                new FunctionPoint(0, 0),
                new FunctionPoint(1, 1),
                new FunctionPoint(2, 4)
        });

        System.out.println("ArrayTabulatedFunction:");
        for (FunctionPoint p : arrayFunc) {
            System.out.println(p);
        }

        TabulatedFunction linkedFunc = new LinkedListTabulatedFunction(new FunctionPoint[] {
                new FunctionPoint(0, 0),
                new FunctionPoint(1, 1),
                new FunctionPoint(2, 4)
        });

        System.out.println("LinkedListTabulatedFunction:");
        for (FunctionPoint p : linkedFunc) {
            System.out.println(p);
        }
    }
    // ФАБРИКИ
    private static void task2() {
        System.out.println("\nЗадание 2");

        TabulatedFunction f = TabulatedFunctions.tabulate(new Cos(), 0, Math.PI, 11);
        System.out.println("Исходная фабрика: " + f.getClass());

        TabulatedFunctions.setTabulatedFunctionFactory(new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());
        f = TabulatedFunctions.tabulate(new Cos(), 0, Math.PI, 11);
        System.out.println("Изменение: " + f.getClass());

        TabulatedFunctions.setTabulatedFunctionFactory(new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());
        f = TabulatedFunctions.tabulate(new Cos(), 0, Math.PI, 11);
        System.out.println("Изменение: " + f.getClass());
    }

    // РЕФЛЕКСИЯ
    private static void task3() {
        System.out.println("\nЗадание 3");

        TabulatedFunction f;

        f = TabulatedFunctions.createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, 3);
        System.out.println(f.getClass());
        System.out.println(f);

        f = TabulatedFunctions.createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, new double[]{0, 10});
        System.out.println(f.getClass());
        System.out.println(f);

        f = TabulatedFunctions.createTabulatedFunction(LinkedListTabulatedFunction.class, new FunctionPoint[] {new FunctionPoint(0, 0), new FunctionPoint(10, 10)});
        System.out.println(f.getClass());
        System.out.println(f);

        f = TabulatedFunctions.tabulate(LinkedListTabulatedFunction.class, new Sin(), 0, Math.PI, 11);
        System.out.println(f.getClass());
        System.out.println(f);
    }
}