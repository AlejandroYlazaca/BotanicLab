public abstract class Planta {
    protected String nombre;
    protected int salud;
    protected boolean regadaEnEsteTurno;
    protected boolean listaParaCosechar;
    protected int turnosCrecimiento;
    protected int costoAguaRequerida;

    public Planta(String nombre, int costoAguaRequerida) {
        this.nombre = nombre;
        this.salud = 100;
        this.regadaEnEsteTurno = true;
        this.listaParaCosechar = false;
        this.turnosCrecimiento = 0;
        this.costoAguaRequerida = costoAguaRequerida;
    }

    public void regar() {
        this.regadaEnEsteTurno = true;
        this.salud = Math.min(100, this.salud + 10);
    }

    public abstract String procesarTurno();

    public String getNombre() { return nombre; }
    public int getSalud() { return salud; }
    public boolean isListaParaCosechar() { return listaParaCosechar; }
}
