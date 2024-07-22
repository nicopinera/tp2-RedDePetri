import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Log implements Runnable {
    private int[] marcado;
    private long tiempo;
    private static FileWriter file;
    private static PrintWriter pw;

    static {
        try {
            file = new FileWriter("log.txt");
            pw = new PrintWriter(file, true); // autoflush activado
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Log() {
        tiempo = System.currentTimeMillis();
    }

    private void imprimirTransiciones() {
        pw.println("secuencia de transiciones:\n" + Monitor.getInstance().getSecuencia());
    }

    @Override
    public void run() {
        while (true) {
            if (Monitor.getInstance().termino) {
                pw.println("tiempo en milis: " + (System.currentTimeMillis() - tiempo));
                pw.println("clientes atendidos por el agente 1: " + Monitor.getInstance().getClientesA1());
                pw.println("clientes atendidos por el agente 1: " + Monitor.getInstance().getClientesA2());
                pw.println("Cantidad de clientes que confirmaron: " + Monitor.getInstance().getClientesConf());
                pw.println("Cantidad de clientes que Cancelaron: " + Monitor.getInstance().getClientesCanc());
                pw.println("clientes que salieron en total: "+ Monitor.getInstance().getMarcado()[14]);
                imprimirTransiciones();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
               return;
            }
            marcado= Monitor.getInstance().getMarcado();
            pw.println("Clientes por entrar: "+ marcado[0]);
            pw.println("Clientes en puerta: "+ marcado[2]);
            pw.println("Clientes esperando para reservar: "+ marcado[3]);
            pw.println("Clientes atendiendose por agente 1: "+ marcado[5]);
            pw.println("Clientes atendiendose por agente 2: "+ marcado[8]);
            pw.println("Clientes esperando para cancelar o confirmar reserva: "+ marcado[9]);
            pw.println("Clientes confirmando: "+ marcado[11]);
            pw.println("Clientes pagando: "+ marcado[13]);
            pw.println("Clientes cancelando: "+ marcado[12]);
            pw.println("Clientes saliente: "+marcado[14]);
            pw.println("");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
