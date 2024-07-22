public enum NumeroDeAgente {

    AGENTE1("1"),
    AGENTE2("2");

    private String numeroDeAgente;

    private NumeroDeAgente(String numeroDeAgente) {
        this.numeroDeAgente = numeroDeAgente;
    }

    public String getnumeroDeAgente() {
        return numeroDeAgente;
    }
}
