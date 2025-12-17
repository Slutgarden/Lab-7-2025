package threads;

public class Semaphore {
    private boolean writing = false;

    public synchronized void beginWrite() throws InterruptedException {
        while (writing) {
            wait();
        }
        writing = true;
    }

    public synchronized void endWrite() {
        writing = false;
        notifyAll();
    }

    public synchronized void beginRead() throws InterruptedException {
        while (writing) {
            wait();
        }
    }

    public synchronized void endRead() {
        notifyAll();
    }
}