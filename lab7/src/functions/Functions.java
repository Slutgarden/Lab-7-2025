package functions;
import functions.meta.*;
public class Functions {
    private Functions(){}

    public static  Function shift(Function f, double shiftX, double shiftY){
        return new Shift(f, shiftX, shiftY);
    }
    public static Function scale(Function f, double scaleX, double scaleY){
       return new Scale(f, scaleX, scaleY);
    }
    public static Function power(Function f, double power){
        return new Power(f, power);
    }
    public static Function sum(Function f1, Function f2){
        return new Sum(f1, f2);
    }
    public static Function mult(Function f1, Function f2){
        return new Mult(f1, f2);
    }
    public static Function composition(Function f1, Function f2){
        return new Composition(f1, f2);
    }

    public static double integrate(Function f, double left, double right, double step) {
        if (step <= 0) {
            throw new IllegalArgumentException("Шаг должен быть положительным");
        }
        if (left > right) {
            throw new IllegalArgumentException("Левая граница больше правой");
        }

        if (left < f.getLeftDomainBorder() || right > f.getRightDomainBorder()) {
            throw new IllegalArgumentException("Интервал интегрирования выходит за границы области определения функции");
        }

        double sum = 0.0;
        double x = left;

        while (x + step < right) {
            double y1 = f.getFunctionValue(x);
            double y2 = f.getFunctionValue(x + step);
            sum += 0.5 * (y1 + y2) * step;
            x += step;
        }

        if (x < right) {
            double y1 = f.getFunctionValue(x);
            double y2 = f.getFunctionValue(right);
            sum += 0.5 * (y1 + y2) * (right - x);
        }

        return sum;
    }
}
