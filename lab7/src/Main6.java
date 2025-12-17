import functions.*;
import functions.basic.*;
import threads.*;
import java.util.Random;

public class Main6 {
    private static final Random random = new Random();

    public static void main(String[] args) {
        expStep();
        nonThread();
        simpleThreads();
        complicatedThreads();
    }

    public static void expStep() {
        System.out.println("\nПроверка точности интегрирования exp(x) на [0;1]");

        Function exp = new Exp();
        double left = 0.0;
        double right = 1.0;

        double exact = Math.E - 1.0;
        System.out.println("Теоретическое значение: " + exact);

        double step = 0.01;

        while (true) {
            double result = Functions.integrate(exp, left, right, step);
            double error = Math.abs(result - exact);

            System.out.println(
                    "Шаг = " + step + " результат = " + result + " погрешность = " + error
            );
            if (error < 0.0000001) {
                System.out.println(
                        "Достаточная точность достигнута при шаге: " + step
                );
                break;
            }
            step = step / 2;
        }
    }

    public static void nonThread() {
        System.out.println("\nnonThread");

        Task task = new Task();
        int taskCount = 100;
        task.setTaskCount(taskCount);

        for (int i = 0; i < taskCount; i++) {
            double base = 1 + random.nextDouble() * 9;
            double left = random.nextDouble() * 100;
            double right = 100 + random.nextDouble() * 100;
            double step = random.nextDouble();
            if (step == 0) step = 0.0001;

            task.setFunction(new Log(base));
            task.setLeft(left);
            task.setRight(right);
            task.setStep(step);

            System.out.printf("Source %.5f %.5f %.5f%n", left, right, step);

            double result = Functions.integrate(task.getFunction(), left, right, step);
            System.out.printf("Result %.5f %.5f %.5f %.10f%n", left, right, step, result);
        }
    }

    public static void simpleThreads() {
        System.out.println("\nsimpleThreads");

        Task task = new Task();
        task.setTaskCount(100);
        task.setHasTask(false);

        Thread generator = new Thread(new SimpleGenerator(task));
        Thread integrator = new Thread(new SimpleIntegrator(task));

        // Приоритеты
        // ВАРИАНТ 1
        generator.setPriority(Thread.MAX_PRIORITY);
        integrator.setPriority(Thread.MIN_PRIORITY);

        // ВАРИАНТ 2
        //generator.setPriority(Thread.MIN_PRIORITY);
        //integrator.setPriority(Thread.MAX_PRIORITY);

        // ВАРИАНТ 3
        //generator.setPriority(Thread.NORM_PRIORITY);
        //integrator.setPriority(Thread.NORM_PRIORITY);

        generator.start();
        integrator.start();

        try {
            generator.join();
            integrator.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void complicatedThreads() {
        System.out.println("\ncomplicatedThreads ");

        Task task = new Task();
        task.setTaskCount(100);
        Semaphore semaphore = new Semaphore();

        Generator generator = new Generator(task, semaphore);
        Integrator integrator = new Integrator(task, semaphore);
        // Приоритеты
        //  ВАРИАНТ 1
        //generator.setPriority(Thread.MAX_PRIORITY);
        //integrator.setPriority(Thread.MIN_PRIORITY);

        // ВАРИАНТ 2
        //generator.setPriority(Thread.MIN_PRIORITY);
        //integrator.setPriority(Thread.MAX_PRIORITY);

        // ВАРИАНТ 3
        generator.setPriority(Thread.NORM_PRIORITY);
        integrator.setPriority(Thread.NORM_PRIORITY);

        generator.start();
        integrator.start();

        try {
            Thread.sleep(50);

            generator.interrupt();
            integrator.interrupt();
            System.out.println("Потоки прерваны после 50 мс работы");

            generator.join();
            integrator.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}