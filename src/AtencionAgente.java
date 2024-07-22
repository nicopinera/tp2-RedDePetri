public class AtencionAgente implements Runnable {
    private NumeroDeAgente agente;
    private Monitor monitor = Monitor.getInstance();

    //Constructor
    public AtencionAgente(NumeroDeAgente agente) {
        this.agente = agente;
    }

    @Override
    public void run() {
        while (true) {
            if (agente.equals(NumeroDeAgente.AGENTE1)) { //Agente Numero 1 o Superor
                monitor.fireTransition(2); //Disparo de T2
                try {
                    Thread.sleep(150);//Duracion del proceso
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                monitor.fireTransition(5); // Disparo de T5
            } else { //Agente Numero 2 o Inferior
                monitor.fireTransition(3); //Disparo de T3
                try {
                    Thread.sleep(150);//Duracion del proceso
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                monitor.fireTransition(4); //Disparo de T4
            }
        }
    }

    public String toString() {
        return "Proceso de Atencion por agente " + agente.getnumeroDeAgente();
    }
}
