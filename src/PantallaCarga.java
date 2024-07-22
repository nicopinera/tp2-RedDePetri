import javax.swing.*;
import java.awt.*;

public class PantallaCarga extends JFrame {
    private static JPanel panel;
    private static int porcentaje = 0;

    public PantallaCarga() {
        setTitle("Ejecutando Transiciones");
        setSize(400, 60);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int totalCuadros = 100;
                int cuadrosLlenos = (int) Math.round(porcentaje * (totalCuadros / 100.0));
                int cuadroAncho = (getWidth() + 17) / totalCuadros;
                for (int i = 0; i < cuadrosLlenos; i++) {
                    g.setColor(Color.GREEN);
                    g.fillRect(i * cuadroAncho, 0, cuadroAncho, getHeight());
                }
            }
        };
        add(panel);
    }

    public static void incrementarPorcentaje(int clientesSalientes, int clientesMax) {
        int nuevoPorcentaje = (clientesSalientes * 100) / clientesMax;
        if (nuevoPorcentaje > porcentaje) {
            porcentaje = nuevoPorcentaje;
            SwingUtilities.invokeLater(() -> panel.repaint());
        }
        if (porcentaje >= 100) {
            panel.setVisible(false);
        }
    }
}
