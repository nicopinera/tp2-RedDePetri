import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {


        //int[][] incidenceMatrix = Monitor.getInstance().getMatrizIncidencia();
        //int[] initialMarking = Monitor.getInstance().getMarcado();
        //ReachabilityTree reachabilityTree = new ReachabilityTree(incidenceMatrix, initialMarking);
        // reachabilityTree.buildReachabilityTree();
        // System.out.println("Numero max de hilos (PA): "+reachabilityTree.getSumaMarcados());


        //Se pide por pantalla la seleccion de la politica empleada
        Scanner scanner = new Scanner(System.in);
        int numero = 0;
        System.out.print("politicas:\n1) Politica balanceada\n2) Politica diferenciada\n");
        while (numero != 1 && numero != 2) {
            System.out.println("Ingrese el numero correspondiente a la politica: ");
            try {
                numero = scanner.nextInt();
            } catch (Exception e) {
                scanner.next();
                System.out.println("Valor invalido, debe digitar un numero.");
            }
        }

        //Se establece la Politica
        Monitor.getInstance().setPolitica(numero);

        {
            PantallaCarga pantalla = new PantallaCarga();
            pantalla.setVisible(true);
            pantalla.setResizable(false);
        }//parte grafica de pantalla de carga


        OurThreadFactory factory = new OurThreadFactory();
        ArrayList<Thread> hilos = new ArrayList<>();

        //Creacion de Hilos

        //1 hilo por agente
        hilos.add(factory.newThread(new AtencionAgente(NumeroDeAgente.AGENTE1)));
        hilos.add(factory.newThread(new AtencionAgente(NumeroDeAgente.AGENTE2)));

        //1 Hilo encargado de la cancelacion
        hilos.add(factory.newThread(new Cancelacion()));

        //1 Hilo encargado de la confirmacion y pago
        hilos.add(factory.newThread(new ConfirmacionYPago()));

        //5 hilos encargado de la generacion y entrada de clientes
        hilos.add(factory.newThread(new EntradaDeClientes()));
        hilos.add(factory.newThread(new EntradaDeClientes()));
        hilos.add(factory.newThread(new EntradaDeClientes()));
        hilos.add(factory.newThread(new EntradaDeClientes()));
        hilos.add(factory.newThread(new EntradaDeClientes()));

        //Hilo encargado del Log
        hilos.add(factory.newThread(new Log()));

        //Inicializacion de los hilos
        for (Thread h : hilos) {
            h.start();
        }


        while (true) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (hilos.get(hilos.size() - 1).getState().equals(Thread.State.TERMINATED)) {
                System.exit(0);
            }
        }

    }
}