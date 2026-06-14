package br.pucgoias.snake;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Testes automatizados (JUnit 5) para a classe {@link JogoSnake}.
 * Cobrem pontuacao, colisao com parede, colisao com o corpo e fim de jogo.
 */
class JogoSnakeTest {

    @Test
    @DisplayName("Deve aumentar a pontuacao quando comer comida")
    void deveAumentarPontuacaoQuandoComerComida() {
        JogoSnake jogo = new JogoSnake(20, 20);
        Posicao cabeca = jogo.getCobra().getCabeca();
        // Coloca a comida exatamente na proxima casa (a cobra anda para a direita).
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
        // Tabuleiro pequeno: cobra comeca em (2,2) indo para a direita.
        JogoSnake jogo = new JogoSnake(5, 5);
        assertFalse(jogo.isJogoAcabou());

        jogo.atualizar(); // (2,3)
        jogo.atualizar(); // (2,4)
        jogo.atualizar(); // (2,5) -> fora do tabuleiro

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

        // Faz a cobra crescer ate o tamanho 5, comendo comida para a direita.
        for (int i = 1; i <= 4; i++) {
            jogo.setComida(new Posicao(linha, coluna + i));
            jogo.atualizar();
        }

        // Faz um quadrado para a cabeca colidir com o corpo.
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
        // A cobra esta indo para a direita; tenta inverter para a esquerda.
        Posicao cabeca = jogo.getCobra().getCabeca();
        jogo.setComida(new Posicao(cabeca.getLinha(), cabeca.getColuna() + 1));
        jogo.atualizar(); // cresce para tamanho 2

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
