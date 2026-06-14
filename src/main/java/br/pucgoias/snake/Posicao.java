package br.pucgoias.snake;

import java.util.Objects;

/**
 * Representa uma posicao no tabuleiro do jogo, identificada por
 * uma linha e uma coluna. Exemplo: linha 5, coluna 3.
 *
 * <p>A classe e imutavel: ao mover, gera-se uma nova {@link Posicao}.</p>
 */
public final class Posicao {

    private final int linha;
    private final int coluna;

    public Posicao(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    /**
     * Cria uma nova posicao deslocada de acordo com a direcao informada.
     *
     * @param direcao direcao do deslocamento
     * @return uma nova {@link Posicao} resultante do movimento
     */
    public Posicao mover(Direcao direcao) {
        return new Posicao(
                linha + direcao.getDeltaLinha(),
                coluna + direcao.getDeltaColuna()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Posicao outra)) {
            return false;
        }
        return linha == outra.linha && coluna == outra.coluna;
    }

    @Override
    public int hashCode() {
        return Objects.hash(linha, coluna);
    }

    @Override
    public String toString() {
        return "Posicao(linha=" + linha + ", coluna=" + coluna + ")";
    }
}
