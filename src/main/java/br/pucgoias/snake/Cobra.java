package br.pucgoias.snake;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

// A cobra: guarda o corpo e cuida do movimento e do crescimento.
// A cabeca fica sempre no inicio da lista.
public class Cobra {

    private final Deque<Posicao> corpo;
    private Direcao direcao;
    private boolean deveCrescer;

    public Cobra(Posicao inicio, Direcao direcaoInicial) {
        this(inicio, direcaoInicial, 1);
    }

    public Cobra(Posicao inicio, Direcao direcaoInicial, int tamanhoInicial) {
        if (tamanhoInicial < 1) {
            throw new IllegalArgumentException("O tamanho inicial deve ser pelo menos 1.");
        }
        this.corpo = new ArrayDeque<>();
        this.direcao = direcaoInicial;
        this.deveCrescer = false;

        // monta o corpo para tras da cabeca
        Direcao oposta = direcaoInicial.oposta();
        Posicao parte = inicio;
        for (int i = 0; i < tamanhoInicial; i++) {
            corpo.addLast(parte);
            parte = parte.mover(oposta);
        }
    }

    public void mover() {
        Posicao novaCabeca = getCabeca().mover(direcao);
        corpo.addFirst(novaCabeca);

        // so tira a cauda se nao for crescer
        if (deveCrescer) {
            deveCrescer = false;
        } else {
            corpo.removeLast();
        }
    }

    public void crescer() {
        this.deveCrescer = true;
    }

    public void mudarDirecao(Direcao novaDirecao) {
        if (novaDirecao == null) {
            return;
        }
        // nao deixa dar meia-volta (bateria no proprio corpo)
        if (corpo.size() > 1 && direcao.ehOposta(novaDirecao)) {
            return;
        }
        this.direcao = novaDirecao;
    }

    public boolean colidiuComProprioCorpo() {
        Posicao cabeca = getCabeca();
        boolean primeira = true;
        for (Posicao parte : corpo) {
            if (primeira) {
                primeira = false;
                continue;
            }
            if (parte.equals(cabeca)) {
                return true;
            }
        }
        return false;
    }

    public Posicao getCabeca() {
        return corpo.peekFirst();
    }

    public int getTamanho() {
        return corpo.size();
    }

    public Direcao getDirecao() {
        return direcao;
    }

    public List<Posicao> getCorpo() {
        return new ArrayList<>(corpo);
    }

    public boolean ocupa(Posicao posicao) {
        return corpo.contains(posicao);
    }
}
