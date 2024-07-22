import java.util.*;

public class ReachabilityTree {

    private int[][] incidenceMatrix;
    private int[] initialMarking;
    private Set<String> visitedStates;
    private Queue<int[]> queue;
    private List<int[]> reachabilityTree;

    public ReachabilityTree(int[][] incidenceMatrix, int[] initialMarking) {
        this.incidenceMatrix = incidenceMatrix;
        this.initialMarking = initialMarking;
        this.visitedStates = new HashSet<>();
        this.queue = new LinkedList<>();
        this.reachabilityTree = new ArrayList<>();
    }

    public void buildReachabilityTree() {
        // Start with the initial marking
        queue.add(initialMarking);
        visitedStates.add(Arrays.toString(initialMarking));
        reachabilityTree.add(initialMarking.clone());

        while (!queue.isEmpty()) {
            int[] currentMarking = queue.poll();

            // Generate next states
            for (int transition = 0; transition < incidenceMatrix[0].length; transition++) {
                int[] nextMarking = applyTransition(currentMarking, transition);
                if (nextMarking != null && !visitedStates.contains(Arrays.toString(nextMarking))) {
                    visitedStates.add(Arrays.toString(nextMarking));
                    queue.add(nextMarking);
                    reachabilityTree.add(nextMarking.clone());
                }
            }
        }
    }

    private int[] applyTransition(int[] marking, int transition) {
        int[] nextMarking = marking.clone();
        for (int i = 0; i < marking.length; i++) {
            nextMarking[i] += incidenceMatrix[i][transition];
            if (nextMarking[i] < 0) {
                return null; // Transition is not enabled
            }
        }
        return nextMarking;
    }

    public int getSumaMarcados() {
        int[] resultado = new int[reachabilityTree.size()];
        int index = 0;

        for (int[] marcado : reachabilityTree) {
            int suma = 0;
            for (int j = 0; j < marcado.length; j++) {
                if (j == 2 || j == 3 || j == 5 || j == 8 || j == 9 || j == 11 || j == 12 || j == 13 || j == 14) {
                    suma += marcado[j];
                }
            }
            resultado[index] = suma;
            index++;
        }
        int maximo = resultado[0];
        for (int i : resultado) {
            if (i > maximo) {
                maximo = i;
            }
        }
        return maximo;
    }

    public List<int[]> getReachabilityTree() {
        return reachabilityTree;
    }
}
