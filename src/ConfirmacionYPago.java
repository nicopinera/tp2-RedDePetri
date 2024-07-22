public class ConfirmacionYPago implements Runnable {
    private Monitor monitor = Monitor.getInstance();

    @Override
    public void run() {
        while (true) {
            monitor.fireTransition(6); //Disparo de T6
            try {
                Thread.sleep(30);//Duracion del Proceso Confirmacion
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            monitor.fireTransition(9); //Disparo de T9
            try {
                Thread.sleep(50);//Duracion del Proceso de Pago
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            monitor.fireTransition(10); //Disparo de T10
            monitor.fireTransition(11); //Disparo de T11

        }
    }

    public String toString() {
        return "Proceso de Cconfirmacion";
    }
}
