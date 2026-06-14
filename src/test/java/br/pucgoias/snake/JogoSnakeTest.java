package br.pucgoias.snake;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

// Testes da classe JogoSnake (pontuacao, colisoes e fim de jogo).
class JogoSnakeTest {

    @Test
    @DisplayName("Deve aumentar a pontuacao quando comer comida")
    void deveAumentarPontuacaoQuandoComerComida() {
        JogoSnake jogo = new JogoSnake(20, 20);
        Posicao cabeca = jogo.getCobra().getCabeca();
        // comida na proxima casa (a cobra vai para a direita)
        jogo.setComida(new Posicao(cabeca.getLinha(), cabeca.getColuna() + 1));

        int pontuacaoAntes = jogo.getPontuacao();
        jogo.atualizar();

        assertTrue(jogo.getPontuacao() > pontuacaoAntes, "A pontuacao deveria ter aumentado");
        assertEquals(10, jogo.getPontuacao());
    }

    @Test
    @DisplayName("Deve fazer a cobra crescer ao comer comida no jogo")
    void deveCrescerAoComerComidaNoJogo() {
        JogoSnake jogo = new JogoSnake(20, 20);
        Posicao cabeca = jogo.getCobra().getCabeca();
        jogo.setComida(new Posicao(cabeca.getLinha(), cabeca.getColuna() + 1));

        int tamanhoAntes = jogo.getCobra().getTamanho();
        jogo.atualizar();

        assertEquals(tamanhoAntes + 1, jogo.getCobra().getTamanho());
    }

    @Test
    @DisplayName("Deve finalizar o jogo ao bater na parede")
    void deveFinalizarJogoAoBaterNaParede() {
        // tabuleiro pequeno: a cobra logo chega na parede
        JogoSnake jogo = new JogoSnake(5, 5);
        assertFalse(jogo.isJogoAcabou());

        jogo.atualizar();
        jogo.atualizar();
        jogo.atualizar(); // sai do tabuleiro

        assertTrue(jogo.isJogoAcabou());
        assertTrue(jogo.colidiuComParede());
    }

    @Test
    @DisplayName("Deve finalizar o jogo ao bater no proprio corpo")
    void deveFinalizarJogoAoBaterNoProprioCorpo() {
        JogoSnake jogo = new JogoSnake(20, 20);
        Posicao c = jogo.getCobra().getCabeca();
        int linha = c.getLinha();
        int coluna = c.getColuna();

        // cresce ate tamanho 5 comendo para a direita
        for (int i = 1; i <= 4; i++) {
            jogo.setComida(new Posicao(linha, coluna + i));
            jogo.atualizar();
        }

        // faz um quadrado para bater no proprio corpo
        jogo.mudarDirecao(Direcao.CIMA);
        jogo.atualizar();
        jogo.mudarDirecao(Direcao.ESQUERDA);
        jogo.atualizar();
        jogo.mudarDirecao(Direcao.BAIXO);
        jogo.atualizar();

        assertTrue(jogo.isJogoAcabou());
    }

    @Test
    @DisplayName("Nao deve permitir mudar para a direcao oposta imediata")
    void naoDevePermitirDirecaoOpostaImediata() {
        JogoSnake jogo = new JogoSnake(20, 20);
        // indo para a direita, tenta virar para a esquerda
        Posicao cabeca = jogo.getCobra().getCabeca();
        jogo.setComida(new Posicao(cabeca.getLinha(), cabeca.getColuna() + 1));
        jogo.atualizar();

        jogo.mudarDirecao(Direcao.ESQUERDA);

        assertEquals(Direcao.DIREITA, jogo.getCobra().getDirecao());
    }

    @Test
    @DisplayName("Jogo nao deve estar acabado logo apos a criacao")
    void jogoNaoDeveComecarAcabado() {
        JogoSnake jogo = new JogoSnake(10, 10);

        assertFalse(jogo.isJogoAcabou());
        assertEquals(0, jogo.getPontuacao());
    }

    @Test
    @DisplayName("Apos comer, a comida deve ser removida do tabuleiro")
    void comidaDeveSerConsumida() {
        JogoSnake jogo = new JogoSnake(20, 20);
        Posicao cabeca = jogo.getCobra().getCabeca();
        jogo.setComida(new Posicao(cabeca.getLinha(), cabeca.getColuna() + 1));

        jogo.atualizar();

        assertEquals(null, jogo.getComida());
    }
}
