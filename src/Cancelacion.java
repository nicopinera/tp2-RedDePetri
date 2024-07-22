public class Cancelacion implements Runnable {
    private Monitor monitor = Monitor.getInstance();

    @Override
    public void run() {
        while (true) {
            monitor.fireTransition(7); //Disparo de T7
            try {
                Thread.sleep(40);//Duracion del proceso
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            monitor.fireTransition(8); //Disparo de T8
            monitor.fireTransition(11); //Disparo de T11
        }
    }

    public String toString() {
        return "Proceso de Cancelacion";
    }
}
