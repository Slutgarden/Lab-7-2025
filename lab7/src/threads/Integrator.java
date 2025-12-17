package threads;

import functions.Function;
import functions.Functions;

public class Integrator extends Thread {
    private final Task task;
    private final Semaphore semaphore;

    public Integrator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < task.getTaskCount(); i++) {

                while (!task.HasTask()) {
                    Thread.sleep(1);
                }

                semaphore.beginRead();
                Function f = task.getFunction();
                double left = task.getLeft();
                double right = task.getRight();
                double step = task.getStep();
                task.setHasTask(false);
                semaphore.endRead();

                double result = Functions.integrate(f, left, right, step);
                System.out.printf("Integrated: Result %.5f %.5f %.5f %.10f%n",
                        left, right, step, result);
            }
        } catch (InterruptedException e) {
            interrupt();
        }
    }
}