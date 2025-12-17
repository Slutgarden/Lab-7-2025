package threads;

import functions.Function;
import functions.Functions;

public class SimpleIntegrator implements Runnable {
    private final Task task;

    public SimpleIntegrator(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        for (int i = 0; i < task.getTaskCount(); i++) {
            Function func;
            double left, right, step;

            synchronized (task) {
                while (!task.HasTask()) {
                    try {
                        task.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }

                func = task.getFunction();
                left = task.getLeft();
                right = task.getRight();
                step = task.getStep();
            }

            double result = Functions.integrate(func, left, right, step);

            synchronized (task) {
                System.out.printf("Integrated: Result %.5f %.5f %.5f %.10f%n",
                        left, right, step, result);
                task.setHasTask(false);
                task.notifyAll();
            }

            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}