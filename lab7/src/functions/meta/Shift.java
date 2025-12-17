package functions.meta;
import functions.Function;
public class Shift implements Function {
    private Function baseFunction;
    private double  shiftX;
    private double  shiftY;

    public Shift(Function baseFunction, double shiftX, double shiftY){
        this.baseFunction = baseFunction;
        this.shiftX = shiftX;
        this.shiftY = shiftY;
    }
    public double getLeftDomainBorder(){
        return baseFunction.getLeftDomainBorder() + shiftX;
    }
    public double getRightDomainBorder(){
        return baseFunction.getRightDomainBorder() +shiftX;
    }
    public double getFunctionValue(double x){
        return baseFunction.getFunctionValue(x - shiftX) + shiftY;
    }
}

