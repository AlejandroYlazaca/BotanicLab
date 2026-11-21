import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BotanicLab extends JFrame {
    private final int MAX_TAMANO = 5;
    private int tamanoActual = 3;

    private Planta[][] matrizPlantas = new Planta[MAX_TAMANO][MAX_TAMANO];
    private JButton[][] botonesMatriz = new JButton[MAX_TAMANO][MAX_TAMANO];

    private int aguaDisponible = 80;
    private int monedas = 60;
    private int turnoActual = 1;

    private List<String> inventarioSemillas = new ArrayList<>();
    private List<String> inventarioCosechas = new ArrayList<>();
    private Set<String> plantasDesbloqueadas = new HashSet<>();

    private JPanel panelMatriz;
    private JLabel lblEstado;
    private JTextArea areaConsola;

    public BotanicLab() {
        plantasDesbloqueadas.add("Maíz");
        plantasDesbloqueadas.add("Papa");

        setTitle("BotanicLab - Gestión de Laboratorio Botánico");
        setSize(880, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelSuperior = new JPanel(new GridLayout(1, 1));
        lblEstado = new JLabel("", JLabel.CENTER);
        lblEstado.setFont(new Font("SansSerif", Font.BOLD, 12));
        actualizarMarcadores();
        panelSuperior.add(lblEstado);
        add(panelSuperior, BorderLayout.NORTH);

        panelMatriz = new JPanel();
        panelMatriz.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        reconstruirMatrizGrafica();
        add(panelMatriz, BorderLayout.CENTER);

        JPanel panelDerecho = new JPanel(new BorderLayout(5, 5));
        panelDerecho.setPreferredSize(new Dimension(330, 0));

        areaConsola = new JTextArea();
        areaConsola.setEditable(false);
        areaConsola.setText("¡Bienvenido a BotanicLab!\n");
        JScrollPane scroll = new JScrollPane(areaConsola);
        panelDerecho.add(scroll, BorderLayout.CENTER);

        JPanel panelBotonesAccion = new JPanel(new GridLayout(4, 1, 5, 5));

        JButton btnMercado = new JButton("🛒 Mercado (Semillas / Cosechas)");
        btnMercado.setBackground(new Color(255, 240, 200));
        btnMercado.addActionListener(e -> abrirMercado());
        panelBotonesAccion.add(btnMercado);

        JButton btnDesbloquear = new JButton("🔓 Desbloquear Plantas");
        btnDesbloquear.setBackground(new Color(225, 225, 250));
        btnDesbloquear.addActionListener(e -> abrirMenuDesbloqueo());
        panelBotonesAccion.add(btnDesbloquear);

        JButton btnMejorarJardin = new JButton("📐 Mejorar Tamaño del Jardín");
        btnMejorarJardin.setBackground(new Color(255, 225, 210));
        btnMejorarJardin.addActionListener(e -> mejorarJardin());
        panelBotonesAccion.add(btnMejorarJardin);

        JButton btnAvanzarTurno = new JButton("⌛ Pasar de Turno");
        btnAvanzarTurno.setBackground(new Color(220, 240, 220));
        btnAvanzarTurno.addActionListener(e -> avanzarTurno());
        panelBotonesAccion.add(btnAvanzarTurno);

        panelDerecho.add(panelBotonesAccion, BorderLayout.SOUTH);
        add(panelDerecho, BorderLayout.EAST);
    }

    private int obtenerCostoMejoraJardin() {
        if (tamanoActual == 3) return 50;
        if (tamanoActual == 4) return 90;
        return -1;
    }

    private void mejorarJardin() {
        if (tamanoActual >= MAX_TAMANO) {
            JOptionPane.showMessageDialog(this, "El jardín ya alcanzó su tamaño máximo (" + MAX_TAMANO + "x" + MAX_TAMANO + ").");
            return;
        }

        int costo = obtenerCostoMejoraJardin();
        int nuevoTamano = tamanoActual + 1;
        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Expandir jardín de " + tamanoActual + "x" + tamanoActual + " a " + nuevoTamano + "x" + nuevoTamano + " por " + costo + " 🪙?",
                "Mejorar Jardín",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (monedas >= costo) {
                monedas -= costo;
                tamanoActual = nuevoTamano;
                reconstruirMatrizGrafica();
                actualizarMarcadores();
                areaConsola.append("📐 ¡Jardín expandido a " + tamanoActual + "x" + tamanoActual + "! (-" + costo + " 🪙).\n");
            } else {
                JOptionPane.showMessageDialog(this, "No tienes suficientes monedas.");
            }
        }
    }

    private void abrirMenuDesbloqueo() {
        List<String> opcionesBloqueadas = new ArrayList<>();
        if (!plantasDesbloqueadas.contains("Tomate")) opcionesBloqueadas.add("Tomate (Coste: 50 🪙)");
        if (!plantasDesbloqueadas.contains("Zanahoria")) opcionesBloqueadas.add("Zanahoria (Coste: 60 🪙)");
        if (!plantasDesbloqueadas.contains("Orquídea")) opcionesBloqueadas.add("Orquídea (Coste: 90 🪙)");

        if (opcionesBloqueadas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "¡Ya desbloqueaste todas las especies disponibles!");
            return;
        }

        String[] arr = opcionesBloqueadas.toArray(new String[0]);
        String seleccion = (String) JOptionPane.showInputDialog(
                this,
                "Monedas: " + monedas + " 🪙\nElige una especie para desbloquear:",
                "Desbloquear Plantas",
                JOptionPane.QUESTION_MESSAGE,
                null,
                arr,
                arr[0]
        );

        if (seleccion != null) {
            if (seleccion.startsWith("Tomate")) {
                procesarDesbloqueo("Tomate", 50);
            } else if (seleccion.startsWith("Zanahoria")) {
                procesarDesbloqueo("Zanahoria", 60);
            } else if (seleccion.startsWith("Orquídea")) {
                procesarDesbloqueo("Orquídea", 90);
            }
        }
    }

    private void procesarDesbloqueo(String especie, int costo) {
        if (monedas >= costo) {
            monedas -= costo;
            plantasDesbloqueadas.add(especie);
            actualizarMarcadores();
            areaConsola.append("🔓 Desbloqueaste la especie " + especie + " (-" + costo + " 🪙).\n");
        } else {
            JOptionPane.showMessageDialog(this, "No tienes suficientes monedas para desbloquear " + especie + ".");
        }
    }

    private void abrirMercado() {
        List<String> opciones = new ArrayList<>();
        List<String> claves = new ArrayList<>();

        if (plantasDesbloqueadas.contains("Maíz")) {
            opciones.add("Semilla Maíz (Coste: 10 🪙)");
            claves.add("Maíz");
        }
        if (plantasDesbloqueadas.contains("Papa")) {
            opciones.add("Semilla Papa (Coste: 8 🪙)");
            claves.add("Papa");
        }
        if (plantasDesbloqueadas.contains("Tomate")) {
            opciones.add("Semilla Tomate (Coste: 12 🪙)");
            claves.add("Tomate");
        }
        if (plantasDesbloqueadas.contains("Zanahoria")) {
            opciones.add("Semilla Zanahoria (Coste: 10 🪙)");
            claves.add("Zanahoria");
        }
        if (plantasDesbloqueadas.contains("Orquídea")) {
            opciones.add("Semilla Orquídea (Coste: 15 🪙)");
            claves.add("Orquídea");
        }
        opciones.add("Vender Cultivos Cosechados 🌾");
        claves.add("VENDER");

        String[] opcionesMercado = opciones.toArray(new String[0]);
        int seleccion = JOptionPane.showOptionDialog(
                this,
                "Mercado - Monedas: " + monedas + " 🪙",
                "Mercado",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcionesMercado,
                opcionesMercado[0]
        );

        if (seleccion >= 0 && seleccion < claves.size()) {
            String clave = claves.get(seleccion);
            switch (clave) {
                case "Maíz":
                    comprarSemilla("Maíz", 10);
                    break;
                case "Papa":
                    comprarSemilla("Papa", 8);
                    break;
                case "Tomate":
                    comprarSemilla("Tomate", 12);
                    break;
                case "Zanahoria":
                    comprarSemilla("Zanahoria", 10);
                    break;
                case "Orquídea":
                    comprarSemilla("Orquídea", 15);
                    break;
                case "VENDER":
                    venderCultivo();
                    break;
            }
        }
        actualizarMarcadores();
    }

    private void comprarSemilla(String nombre, int costo) {
        if (monedas >= costo) {
            monedas -= costo;
            inventarioSemillas.add(nombre);
            areaConsola.append("🛒 Compraste semilla de " + nombre + " (-" + costo + " 🪙).\n");
        } else {
            JOptionPane.showMessageDialog(this, "Sin fondos.");
        }
    }

    private void venderCultivo() {
        if (inventarioCosechas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay cosechas para vender.");
            return;
        }
        String[] arr = inventarioCosechas.toArray(new String[0]);
        String venta = (String) JOptionPane.showInputDialog(this, "Elige cultivo a vender:", "Vender", JOptionPane.QUESTION_MESSAGE, null, arr, arr[0]);
        if (venta != null) {
            inventarioCosechas.remove(venta);
            int ganancia = 20;
            switch (venta) {
                case "Orquídea": ganancia = 35; break;
                case "Maíz": ganancia = 25; break;
                case "Tomate": ganancia = 28; break;
                case "Papa": ganancia = 18; break;
                case "Zanahoria": ganancia = 22; break;
            }
            monedas += ganancia;
            areaConsola.append("💰 Vendiste " + venta + " por +" + ganancia + " 🪙.\n");
        }
    }

    private void interactuarConCasilla(int f, int c) {
        Planta planta = matrizPlantas[f][c];

        if (planta == null) {
            if (inventarioSemillas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Inventario de semillas vacío.");
                return;
            }
            String[] semillas = inventarioSemillas.toArray(new String[0]);
            String elegida = (String) JOptionPane.showInputDialog(this, "Plantar en [" + f + "][" + c + "]:", "Sembrar", JOptionPane.QUESTION_MESSAGE, null, semillas, semillas[0]);

            if (elegida != null) {
                switch (elegida) {
                    case "Orquídea": matrizPlantas[f][c] = new Orquidea(); break;
                    case "Maíz": matrizPlantas[f][c] = new Maiz(); break;
                    case "Tomate": matrizPlantas[f][c] = new Tomate(); break;
                    case "Papa": matrizPlantas[f][c] = new Papa(); break;
                    case "Zanahoria": matrizPlantas[f][c] = new Zanahoria(); break;
                }
                inventarioSemillas.remove(elegida);
                areaConsola.append("🌱 Sembraste " + elegida + " en [" + f + "][" + c + "].\n");
            }
        } else {
            if (planta.getSalud() <= 0) {
                matrizPlantas[f][c] = null;
                areaConsola.append("🧹 Limpiaste planta muerta.\n");
            } else if (planta.isListaParaCosechar()) {
                inventarioCosechas.add(planta.getNombre());
                matrizPlantas[f][c] = null;
                areaConsola.append("🌾 Cosechaste " + planta.getNombre() + ".\n");
            } else {
                if (aguaDisponible >= 10) {
                    aguaDisponible -= 10;
                    planta.regar();
                    areaConsola.append("💧 Regaste " + planta.getNombre() + ".\n");
                } else {
                    JOptionPane.showMessageDialog(this, "No hay agua disponible.");
                }
            }
        }
        actualizarMarcadores();
        actualizarMatrizGrafica();
    }

    private void avanzarTurno() {
        areaConsola.append("\n--- TURNO " + turnoActual + " ---\n");
        for (int i = 0; i < tamanoActual; i++) {
            for (int j = 0; j < tamanoActual; j++) {
                Planta p = matrizPlantas[i][j];
                if (p != null && p.getSalud() > 0) {
                    areaConsola.append(p.procesarTurno());
                }
            }
        }
        turnoActual++;
        aguaDisponible += 20;
        actualizarMarcadores();
        actualizarMatrizGrafica();
    }

    private void reconstruirMatrizGrafica() {
        panelMatriz.removeAll();
        panelMatriz.setLayout(new GridLayout(tamanoActual, tamanoActual, 8, 8));

        for (int i = 0; i < tamanoActual; i++) {
            for (int j = 0; j < tamanoActual; j++) {
                final int f = i;
                final int c = j;
                if (botonesMatriz[i][j] == null) {
                    botonesMatriz[i][j] = new JButton("Vacío");
                    botonesMatriz[i][j].setFont(new Font("SansSerif", Font.PLAIN, 11));
                    botonesMatriz[i][j].addActionListener(e -> interactuarConCasilla(f, c));
                }
                panelMatriz.add(botonesMatriz[i][j]);
            }
        }

        actualizarMatrizGrafica();
        panelMatriz.revalidate();
        panelMatriz.repaint();
    }

    private void actualizarMatrizGrafica() {
        for (int i = 0; i < tamanoActual; i++) {
            for (int j = 0; j < tamanoActual; j++) {
                Planta p = matrizPlantas[i][j];
                if (p == null) {
                    botonesMatriz[i][j].setText("Vacío");
                    botonesMatriz[i][j].setBackground(new Color(230, 220, 200));
                } else if (p.getSalud() <= 0) {
                    botonesMatriz[i][j].setText("Muerta");
                    botonesMatriz[i][j].setBackground(new Color(200, 180, 180));
                } else if (p.isListaParaCosechar()) {
                    botonesMatriz[i][j].setText("¡Cosechar!");
                    botonesMatriz[i][j].setBackground(new Color(255, 230, 150));
                } else {
                    botonesMatriz[i][j].setText(p.getNombre() + " (" + p.getSalud() + "%)");
                    botonesMatriz[i][j].setBackground(new Color(180, 230, 180));
                }
            }
        }
    }

    private void actualizarMarcadores() {
        lblEstado.setText("Turno: " + turnoActual + " | Jardín: " + tamanoActual + "x" + tamanoActual + " | 💧: " + aguaDisponible + " | 🪙: " + monedas + " | 🎒 Semillas: " + inventarioSemillas.size() + " | 📦 Cosechas: " + inventarioCosechas.size());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BotanicLab().setVisible(true));
    }
}
