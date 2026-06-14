package br.pucgoias.snake;

/**
 * Representa as quatro direcoes possiveis de movimento da cobra no tabuleiro.
 *
 * <p>Cada direcao guarda o deslocamento que ela provoca em linhas e colunas,
 * facilitando o calculo da proxima posicao da cobra.</p>
 */
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

    /**
     * Retorna a direcao oposta a atual.
     * Usado para impedir que a cobra inverta o sentido imediatamente
     * (o que faria a cabeca colidir com o corpo).
     *
     * @return a direcao contraria
     */
    public Direcao oposta() {
        return switch (this) {
            case CIMA -> BAIXO;
            case BAIXO -> CIMA;
            case ESQUERDA -> DIREITA;
            case DIREITA -> ESQUERDA;
        };
    }

    /**
     * Indica se a direcao informada e oposta a direcao atual.
     *
     * @param outra direcao a comparar
     * @return {@code true} se forem opostas
     */
    public boolean ehOposta(Direcao outra) {
        return this.oposta() == outra;
    }
}
