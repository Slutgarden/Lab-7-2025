package threads;
import functions.*;
public class Task {
    private Function function;
    private double left;
    private double right;
    private double step;
    private int taskCount;
    private boolean hasTask = false;

    public void setFunction(Function function) {
        this.function = function;
    }

    public void setLeft(double left) {
        this.left = left;
    }

    public void setRight(double right) {
        this.right = right;
    }

    public void setStep(double step) {
        this.step = step;
    }

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }

    public Function getFunction() {
        return function;
    }

    public double getLeft() {
        return left;
    }

    public double getRight() {
        return right;
    }

    public double getStep() {
        return step;
    }

    public int getTaskCount() {
        return taskCount;
    }

    public boolean HasTask() {
        return hasTask;
    }
    public void  setHasTask(boolean hasTask){
        this.hasTask = hasTask;
    }
}
