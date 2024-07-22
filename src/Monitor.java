import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Monitor implements MonitorInterface {
    private final int numeroMaxClientes = 186; // Numero maximo de
    private static Monitor m; //Instancia del monitor
    private static final Semaphore mutex = new Semaphore(1); // Semaforo para exclusion mutua
    private final ArrayList<Locks> llaves = new ArrayList<>(); //Lista de llaves
    private int[][] matrizIncidencia; //Matriz de incidencia de la red
    private int[] marcado; //Marcado actual
    public boolean termino = false; //Saber si termino la red

    //Variables para la politica
    private int clientesA1 = 0, clientesA2 = 0; //Clientes atendidos por cada agente (superior e inferior)
    private boolean a1Esperando = false, a2Esperando = false; //Verificar si los agentes estan trabajando
    private int clientesConf = 0, clientesCanc = 0; //Cantidad de clientes confirmados y cancelados
    private String secuencia = ""; //Secuencia de disparo
    private Politica politica; //Politica seleccionada
    private int simulaT11 = 0;

    //Metodo para obtener la instanica del monitor
    public static Monitor getInstance() {
        if (m == null) {
            m = new Monitor();
        }
        return m;
    }

    //Constructor
    private Monitor() {
        generarMatrizIncidencia();
        generarLlaves();
        generarMarcadoInicial();
    }

    //Marcado inicial
    private void generarMarcadoInicial() {
        marcado = new int[]{numeroMaxClientes, 1, 0, 0, 5, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0};

    }

    //Matriz de Incidencia
    private void generarMatrizIncidencia() {
        matrizIncidencia = new int[][]{
                {-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {-1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0},
                {-1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, -1, 0, 0, 0, 0, 0, 0},
                {0, 0, -1, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, -1, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, -1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 1, -1, -1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, -1, -1, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 0, -1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, -1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, -1}
        };
    }

    //Laves
    private void generarLlaves() {
        llaves.add(new Locks("Atencion Agente1"));
        llaves.add(new Locks("Cancelacion"));
        llaves.add(new Locks("Confirmacion "));
        llaves.add(new Locks("Entrada de clientes"));
        llaves.add(new Locks("Atencion Agente2"));
    }

    //Getters y setters
    public int[] getMarcado() {
        return marcado;
    }

    public int getClientesA1() {
        return clientesA1;
    }

    public int getClientesA2() {
        return clientesA2;
    }

    public int getClientesConf() {
        return clientesConf;
    }

    public int getClientesCanc() {
        return clientesCanc;
    }

    public String getSecuencia() {
        return secuencia;
    }

    public int[][] getMatrizIncidencia() {
        return matrizIncidencia;
    }

    public void setPolitica(int politica) {
        this.politica = new Politica(politica);
    }

    //Disparar Transicion
    public boolean fireTransition(int transition) {
        while (true) {
            try {
                mutex.acquire(); //Toma el mutex
                if (!sensibilizado(transition)) { //Verifica si no esta sensibilizada
                    dormirHilo(transition);
                    continue;
                }
                disparar(transition); //Disparo de la transicion
                if (marcado[14] == numeroMaxClientes && simulaT11 == numeroMaxClientes) {
                    termino = true;
                }
                mutex.release(); //Devuelve el mutex
                return true;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //Dormir al hilo
    private void dormirHilo(int transition) throws InterruptedException {
        if (transition == 11) {
            return;
        }
        if (transition == 2) {
            a1Esperando = true;
        }
        if (transition == 3) {
            a2Esperando = true;
        }
        mutex.release();
        synchronized (lLaveDormirHilo(transition)) {
            lLaveDormirHilo(transition).wait();
        }
        if (transition == 2) {
            a1Esperando = false;
        }
        if (transition == 3) {
            a2Esperando = false;
        }
    }

    private Locks lLaveDormirHilo(int transition) {
        switch (transition) {
            case 0, 1:
                return llaves.get(3);
            case 2, 5:
                return llaves.get(0);
            case 3, 4:
                return llaves.get(4);
            case 7, 8:
                return llaves.get(1);
            case 6, 9:
                return llaves.get(2);
        }
        return null;
    }

    //Disparar Transicion
    private void disparar(int transition) {
        secuencia += "T" + transition;
        if (transition == 11) {
            simulaT11++;
            PantallaCarga.incrementarPorcentaje(simulaT11, numeroMaxClientes);
            return;
        }
        if (transition == 2) {
            clientesA1++;
        }
        if (transition == 3) {
            clientesA2++;
        }
        if (transition == 6) {
            clientesConf++;
        }
        if (transition == 7) {
            clientesCanc++;
        }
        marcado = nuevoMarcado(transition);
        despertarHilos(transition);
    }

    //Despertar Hilos
    private void despertarHilos(int transition) {

        if (transition == 1 || transition == 4 || transition == 5 || transition == 10 || transition == 8) {
            politica.llamadaApolitica(transition);
        }
        switch (transition) {
            case 1, 2, 3:
                synchronized (llaves.get(3)) {
                    llaves.get(3).notifyAll(); //lock de entrada de clientes
                }
                break;
        }
    }

    //Nuevo Marcado
    private int[] nuevoMarcado(int transition) {
        int[] S = new int[matrizIncidencia[0].length];//Vector de disparo
        S[transition] = 1;
        int[] resultado = new int[matrizIncidencia.length]; //Nuevo marcado

        for (int i = 0; i < matrizIncidencia.length; i++) {
            int suma = 0;
            for (int j = 0; j < matrizIncidencia[0].length; j++) {
                suma += matrizIncidencia[i][j] * S[j]; //Producto matricial
            }
            resultado[i] = suma;
        }
        for (int i = 0; i < resultado.length; i++) {
            resultado[i] += marcado[i];
        }
        return resultado;
    }

    //Verificar si esta sencibilizada la transicion
    private boolean sensibilizado(int transition) {
        if (transition == 11) {
            return true;
        }
        int[] aux = nuevoMarcado(transition);
        for (int i : aux) {
            if (i < 0) { //Numero negativo
                return false; //No esta sensibilizada
            }
        }
        return true;
    }

    //Clase Politica
    private class Politica {

        private final int numeroPolitica;

        //Constructor
        public Politica(int politica) {
            numeroPolitica = politica;
        }

        //Llamado de politica
        public void llamadaApolitica(int transition) {
            if (numeroPolitica == 1) {
                if (transition == 1) {
                    politicaBalanceada1();
                } else {
                    politicaBalanceada2();
                }
            }
            if (numeroPolitica == 2) {
                if (transition == 1) {
                    politicaPriorizada1();
                } else {
                    politicaPriorizada2();
                }

            }
        }

        //Politica 2
        private void politicaPriorizada1() { //Prioriza al agente superior
            if (a1Esperando && !a2Esperando) {
                synchronized (llaves.get(0)) {
                    llaves.get(0).notifyAll();
                }
                return;
            }

            if (!a1Esperando && a2Esperando) {
                synchronized (llaves.get(4)) {
                    llaves.get(4).notifyAll();
                }
                return;
            }

            if (a1Esperando && a2Esperando) {
                if (clientesA1 > ((clientesA2 + clientesA1) * 75) / 100) {
                    synchronized (llaves.get(4)) {
                        llaves.get(4).notifyAll();
                    }
                } else {
                    synchronized (llaves.get(0)) {
                        llaves.get(0).notifyAll();
                    }
                }
            }

        }

        public void politicaPriorizada2() { //Prioriza las confirmaciones
            if (clientesConf > ((clientesCanc + clientesConf) * 80) / 100) {
                synchronized (llaves.get(1)) {
                    llaves.get(1).notifyAll();
                }
            } else {
                synchronized (llaves.get(2)) {
                    llaves.get(2).notifyAll();
                }
            }
        }

        //Politica 1
        private void politicaBalanceada1() { //Se encarga de balancear los agentes
            if (a1Esperando && !a2Esperando) {
                synchronized (llaves.get(0)) {
                    llaves.get(0).notifyAll();
                }
                return;
            }
            if (!a1Esperando && a2Esperando) {
                synchronized (llaves.get(4)) {
                    llaves.get(4).notifyAll();
                }
                return;
            }

            if (a1Esperando && a2Esperando) {
                if (clientesA1 > clientesA2) {
                    synchronized (llaves.get(4)) {
                        llaves.get(4).notifyAll();
                    }
                } else {
                    synchronized (llaves.get(0)) {
                        llaves.get(0).notifyAll();
                    }
                }
            }
        }

        public void politicaBalanceada2() { //Se encarga de balancear las cancelaciones


            if (clientesCanc > clientesConf) {
                synchronized (llaves.get(2)) {
                    llaves.get(2).notifyAll();
                }
            } else {
                synchronized (llaves.get(1)) {
                    llaves.get(1).notifyAll();
                }
            }

        }
    }

}
