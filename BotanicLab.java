import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BotanicLab extends JFrame {
    private final int FILAS = 3;
    private final int COLUMNAS = 3;
    private Planta[][] matrizPlantas = new Planta[FILAS][COLUMNAS];
    private JButton[][] botonesMatriz = new JButton[FILAS][COLUMNAS];
    private int aguaDisponible = 60;
    private int monedas = 30;
    private int turnoActual = 1;
    private List<String> inventarioSemillas = new ArrayList<>();
    private List<String> inventarioCosechas = new ArrayList<>();
    private JLabel lblEstado;
    private JTextArea areaConsola;
    public BotanicLab() {
        setTitle("BotanicLab - Gestión de Laboratorio Botánico");
        setSize(780, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));


        JPanel panelSuperior = new JPanel(new GridLayout(1, 1));
        lblEstado = new JLabel("", JLabel.CENTER);
        lblEstado.setFont(new Font("SansSerif", Font.BOLD, 12));
        actualizarMarcadores();
        panelSuperior.add(lblEstado);
        add(panelSuperior, BorderLayout.NORTH);

        JPanel panelMatriz = new JPanel(new GridLayout(FILAS, COLUMNAS, 8, 8));
        panelMatriz.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                final int f = i;
                final int c = j;
                botonesMatriz[i][c] = new JButton("Vacío");
                botonesMatriz[i][c].setFont(new Font("SansSerif", Font.PLAIN, 11));
                botonesMatriz[i][c].addActionListener(e -> interactuarConCasilla(f, c));
                panelMatriz.add(botonesMatriz[i][c]);
            }
        }
        add(panelMatriz, BorderLayout.CENTER);

        JPanel panelDerecho = new JPanel(new BorderLayout(5, 5));
        panelDerecho.setPreferredSize(new Dimension(310, 0));
      
        areaConsola = new JTextArea();
        areaConsola.setEditable(false);
        areaConsola.setText("¡Bienvenido a BotanicLab!\n");
        JScrollPane scroll = new JScrollPane(areaConsola);
        panelDerecho.add(scroll, BorderLayout.CENTER);

        JPanel panelBotonesAccion = new JPanel(new GridLayout(2, 1, 5, 5));

        JButton btnMercado = new JButton("🛒 Ir al Mercado (Comprar/Vender)");
        btnMercado.setBackground(new Color(255, 240, 200));
        btnMercado.addActionListener(e -> abrirMercado());
        panelBotonesAccion.add(btnMercado);

        JButton btnAvanzarTurno = new JButton("⌛ Pasar de Turno");
        btnAvanzarTurno.setBackground(new Color(220, 240, 220));
        btnAvanzarTurno.addActionListener(e -> avanzarTurno());
        panelBotonesAccion.add(btnAvanzarTurno);

        panelDerecho.add(panelBotonesAccion, BorderLayout.SOUTH);
        add(panelDerecho, BorderLayout.EAST);
       
        actualizarMatrizGrafica();
  }
private void abrirMercado() {
        String[] opcionesMercado = {
            "Comprar Semilla de Orquídea (Coste: 15 🪙)",
            "Comprar Semilla de Maíz (Coste: 10 🪙)",
            "Vender Cultivos Cosechados 🌾"
        };
        int seleccion = JOptionPane.showOptionDialog(
                this, "Mercado - Monedas: " + monedas + " 🪙", "Mercado",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, opcionesMercado, opcionesMercado[0]
        );
        switch (seleccion) {
            case 0:
                if (monedas>=15){ monedas-=15; inventarioSemillas.add("Orquídea"); areaConsola.append("🛒 Compraste Orquídea.\n"); }
                else JOptionPane.showMessageDialog(this, "Sin fondos.");
                break;
            case 1:
                if (monedas>=10){ monedas-=10; inventarioSemillas.add("Maíz"); areaConsola.append("🛒 Compraste Maíz.\n"); }
                else JOptionPane.showMessageDialog(this, "Sin fondos.");
                break;
            case 2:
                if (inventarioCosechas.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No hay cosechas para vender.");
                } else {
                    String[] arr=inventarioCosechas.toArray(new String[0]);
                    String venta = (String) JOptionPane.showInputDialog(this, "Elige cultivo a vender:", "Vender", JOptionPane.QUESTION_MESSAGE, null, arr, arr[0]);
                    if (venta != null) {
                        inventarioCosechas.remove(venta);
                        int ganancia = venta.equals("Orquídea") ? 35 : 25;
                        monedas += ganancia;
                        areaConsola.append("💰 Vendiste " + venta + " por +" + ganancia + " 🪙.\n");
                    }
                }
                break;
        }
        actualizarMarcadores();
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
                if (elegida.equals("Orquídea")) {
                    matrizPlantas[f][c] = new Orquidea();
                } else {
                    matrizPlantas[f][c] = new Maiz();
                }
                inventarioSemillas.remove(elegida);
                areaConsola.append("🌱 Sembraste " + elegida + ".\n");
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
    
	}
    private void actualizarMatrizGrafica() {
      
  }
  private void actualizarMarcadores() {
    
  }
  public static void main(String[] args) {
          SwingUtilities.invokeLater(() -> new BotanicLab().setVisible(true));
      }
  }

