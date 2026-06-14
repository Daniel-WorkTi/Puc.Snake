package br.pucgoias.snake;

import java.util.Objects;

// Uma posicao no tabuleiro (linha e coluna).
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

    // Retorna uma nova posicao andando uma casa na direcao indicada.
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
        if (!(o instanceof Posicao)) {
            return false;
        }
        Posicao outra = (Posicao) o;
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
