package br.pucgoias.snake;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Representa a cobra do jogo. Guarda o corpo (lista de posicoes),
 * controla o movimento, o crescimento ao comer comida e verifica
 * a colisao com o proprio corpo.
 *
 * <p>O primeiro elemento do corpo e sempre a cabeca.</p>
 */
public class Cobra {

    /** Corpo da cobra. A cabeca fica no inicio (first) e a cauda no fim (last). */
    private final Deque<Posicao> corpo;

    /** Direcao atual em que a cobra esta se movendo. */
    private Direcao direcao;

    /** Indica que a cobra deve crescer no proximo movimento (comeu comida). */
    private boolean deveCrescer;

    /**
     * Cria a cobra com uma cabeca na posicao inicial e uma direcao inicial.
     *
     * @param inicio         posicao inicial da cabeca
     * @param direcaoInicial direcao inicial do movimento
     */
    public Cobra(Posicao inicio, Direcao direcaoInicial) {
        this(inicio, direcaoInicial, 1);
    }

    /**
     * Cria a cobra com um tamanho inicial. Os segmentos do corpo sao
     * posicionados atras da cabeca (no sentido oposto ao da direcao),
     * para que a cobra ja comece esticada.
     *
     * @param inicio         posicao inicial da cabeca
     * @param direcaoInicial direcao inicial do movimento
     * @param tamanhoInicial quantidade de segmentos da cobra (>= 1)
     */
    public Cobra(Posicao inicio, Direcao direcaoInicial, int tamanhoInicial) {
        if (tamanhoInicial < 1) {
            throw new IllegalArgumentException("O tamanho inicial deve ser pelo menos 1.");
        }
        this.corpo = new ArrayDeque<>();
        this.direcao = direcaoInicial;
        this.deveCrescer = false;

        Direcao oposta = direcaoInicial.oposta();
        Posicao parte = inicio;
        for (int i = 0; i < tamanhoInicial; i++) {
            corpo.addLast(parte); // cabeca fica no inicio, cauda no fim
            parte = parte.mover(oposta);
        }
    }

    /**
     * Move a cobra um passo na direcao atual.
     * Se a cobra deve crescer (comeu comida), a cauda nao e removida.
     */
    public void mover() {
        Posicao novaCabeca = getCabeca().mover(direcao);
        corpo.addFirst(novaCabeca);

        if (deveCrescer) {
            deveCrescer = false;
        } else {
            corpo.removeLast();
        }
    }

    /**
     * Marca a cobra para crescer no proximo movimento.
     */
    public void crescer() {
        this.deveCrescer = true;
    }

    /**
     * Altera a direcao da cobra. Nao permite inverter o sentido
     * imediatamente (direcao oposta a atual), pois isso faria a
     * cabeca colidir com o corpo.
     *
     * @param novaDirecao nova direcao desejada
     */
    public void mudarDirecao(Direcao novaDirecao) {
        if (novaDirecao == null) {
            return;
        }
        // Se a cobra tem mais de um segmento, nao pode inverter o sentido.
        if (corpo.size() > 1 && direcao.ehOposta(novaDirecao)) {
            return;
        }
        this.direcao = novaDirecao;
    }

    /**
     * Verifica se a cabeca da cobra colidiu com alguma parte do corpo.
     *
     * @return {@code true} se houver colisao com o proprio corpo
     */
    public boolean colidiuComProprioCorpo() {
        Posicao cabeca = getCabeca();
        boolean primeira = true;
        for (Posicao parte : corpo) {
            if (primeira) {
                primeira = false;
                continue; // pula a propria cabeca
            }
            if (parte.equals(cabeca)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return a posicao da cabeca da cobra
     */
    public Posicao getCabeca() {
        return corpo.peekFirst();
    }

    /**
     * @return o tamanho atual da cobra (quantidade de segmentos)
     */
    public int getTamanho() {
        return corpo.size();
    }

    /**
     * @return a direcao atual da cobra
     */
    public Direcao getDirecao() {
        return direcao;
    }

    /**
     * @return uma copia da lista do corpo (cabeca primeiro)
     */
    public List<Posicao> getCorpo() {
        return new ArrayList<>(corpo);
    }

    /**
     * Verifica se a cobra ocupa a posicao informada.
     *
     * @param posicao posicao a verificar
     * @return {@code true} se alguma parte do corpo estiver na posicao
     */
    public boolean ocupa(Posicao posicao) {
        return corpo.contains(posicao);
    }
}
