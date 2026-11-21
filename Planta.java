abstract class Planta {
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

class Orquidea extends Planta {
    public Orquidea() {
        super("Orquídea", 20);
    }

    @Override
    public String procesarTurno() {
        StringBuilder reporte = new StringBuilder();
        if (!regadaEnEsteTurno) {
            salud -= 30;
            reporte.append("⚠️ Orquídea se deshidrata severamente (-30 Salud).\n");
        } else {
            reporte.append("💧 Orquídea florece elegantemente bajo riego.\n");
        }
        regadaEnEsteTurno = false;
        turnosCrecimiento++;
        if (salud > 0 && turnosCrecimiento >= 5) listaParaCosechar = true;
        return reporte.toString();
    }
}

class Maiz extends Planta {
    public Maiz() {
        super("Maíz", 10);
    }

    @Override
    public String procesarTurno() {
        StringBuilder reporte = new StringBuilder();
        if (!regadaEnEsteTurno) {
            salud -= 20;
            reporte.append("⚠️ Maíz pierde vigor por sequía (-20 Salud).\n");
        } else {
            reporte.append("💧 Maíz crece fuerte y verde.\n");
        }
        regadaEnEsteTurno = false;
        turnosCrecimiento++;
        if (salud > 0 && turnosCrecimiento >= 4) listaParaCosechar = true;
        return reporte.toString();
    }
}

class Tomate extends Planta {
    public Tomate() {
        super("Tomate", 15);
    }

    @Override
    public String procesarTurno() {
        StringBuilder reporte = new StringBuilder();
        if (!regadaEnEsteTurno) {
            salud -= 25;
            reporte.append("⚠️ Tomate sufre por falta de agua (-25 Salud).\n");
        } else {
            reporte.append("💧 Tomate madura con buen riego.\n");
        }
        regadaEnEsteTurno = false;
        turnosCrecimiento++;
        if (salud > 0 && turnosCrecimiento >= 3) listaParaCosechar = true;
        return reporte.toString();
    }
}

class Papa extends Planta {
    public Papa() {
        super("Papa", 10);
    }

    @Override
    public String procesarTurno() {
        StringBuilder reporte = new StringBuilder();
        if (!regadaEnEsteTurno) {
            salud -= 15;
            reporte.append("⚠️ Papa resiste pero pierde fuerza (-15 Salud).\n");
        } else {
            reporte.append("💧 Papa se desarrolla bajo tierra correctamente.\n");
        }
        regadaEnEsteTurno = false;
        turnosCrecimiento++;
        if (salud > 0 && turnosCrecimiento >= 2) listaParaCosechar = true;
        return reporte.toString();
    }
}

class Zanahoria extends Planta {
    public Zanahoria() {
        super("Zanahoria", 12);
    }

    @Override
    public String procesarTurno() {
        StringBuilder reporte = new StringBuilder();
        if (!regadaEnEsteTurno) {
            salud -= 20;
            reporte.append("⚠️ Zanahoria se debilita por sequía (-20 Salud).\n");
        } else {
            reporte.append("💧 Zanahoria crece sanamente.\n");
        }
        regadaEnEsteTurno = false;
        turnosCrecimiento++;
        if (salud > 0 && turnosCrecimiento >= 3) listaParaCosechar = true;
        return reporte.toString();
    }
}
        
