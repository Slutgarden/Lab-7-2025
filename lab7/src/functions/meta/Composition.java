package functions.meta;
import functions.Function;
public class Composition implements Function{
    private  Function outerFunction;
    private  Function innerFunction;

    public Composition(Function outerFunction, Function innerFunction){
        this.outerFunction = outerFunction;
        this.innerFunction = innerFunction;
    }
    public double getLeftDomainBorder(){
        return innerFunction.getLeftDomainBorder();
    }
    public double getRightDomainBorder(){
        return innerFunction.getRightDomainBorder();
    }
    public double getFunctionValue(double x){
        return outerFunction.getFunctionValue(innerFunction.getFunctionValue(x));
    }
}
