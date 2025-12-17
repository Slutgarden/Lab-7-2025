package functions.meta;
import functions.Function;
public class Scale  implements Function{
    private Function baseFunction;
    private double  scaleX;
    private double  scaleY;


    public Scale(Function baseFunction, double scaleX, double scaleY){
        this.baseFunction = baseFunction;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }
    public double getLeftDomainBorder(){
        double left = baseFunction.getLeftDomainBorder();
        double right = baseFunction.getRightDomainBorder();
        if(scaleX>=0){
            return left * scaleX;
        }
        else{
            return right * scaleX;
        }
    }
    public double getRightDomainBorder(){

        double left = baseFunction.getLeftDomainBorder();
        double right = baseFunction.getRightDomainBorder();
        if(scaleX>=0){
            return right * scaleX;
        }
        else{
            return left * scaleX;
        }
    }
    public double getFunctionValue(double x){
        return scaleY * baseFunction.getFunctionValue(x/scaleX);
    }
}
