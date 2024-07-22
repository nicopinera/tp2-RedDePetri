public class EntradaDeClientes implements Runnable {
    private Monitor monitor = Monitor.getInstance();

    @Override
    public void run() {
        while (true) {
            monitor.fireTransition(0); //Disparo de T0
            try {
                Thread.sleep(160);//Duracion del Proceso
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            monitor.fireTransition(1); //Disparo de T1
        }
    }

    public String toString() {
        return "Proceso de entrar a la agencia";
    }
}
