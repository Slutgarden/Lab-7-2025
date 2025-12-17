package threads;

import functions.basic.Log;
import java.util.Random;

public class Generator extends Thread {
    private final Task task;
    private final Semaphore semaphore;
    private final Random rnd = new Random();

    public Generator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < task.getTaskCount(); i++) {
                double base = 1 + rnd.nextDouble() * 9;
                double left = rnd.nextDouble() * 100;
                double right = 100 + rnd.nextDouble() * 100;
                double step = rnd.nextDouble();
                if (step == 0) step = 0.0001;

                while (task.HasTask()) {
                    Thread.sleep(1);
                }

                semaphore.beginWrite();
                task.setFunction(new Log(base));
                task.setLeft(left);
                task.setRight(right);
                task.setStep(step);
                task.setHasTask(true);
                semaphore.endWrite();

                System.out.printf("Generated: Source %.5f %.5f %.5f%n", left, right, step);
                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            interrupt();
        }
    }
}