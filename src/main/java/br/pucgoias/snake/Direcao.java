package br.pucgoias.snake;

// Direcoes que a cobra pode seguir. Cada uma guarda o quanto
// muda na linha e na coluna ao andar.
public enum Direcao {

    CIMA(-1, 0),
    BAIXO(1, 0),
    ESQUERDA(0, -1),
    DIREITA(0, 1);

    private final int deltaLinha;
    private final int deltaColuna;

    Direcao(int deltaLinha, int deltaColuna) {
        this.deltaLinha = deltaLinha;
        this.deltaColuna = deltaColuna;
    }

    public int getDeltaLinha() {
        return deltaLinha;
    }

    public int getDeltaColuna() {
        return deltaColuna;
    }

    public Direcao oposta() {
        switch (this) {
            case CIMA:
                return BAIXO;
            case BAIXO:
                return CIMA;
            case ESQUERDA:
                return DIREITA;
            default:
                return ESQUERDA;
        }
    }

    public boolean ehOposta(Direcao outra) {
        return this.oposta() == outra;
    }
}
