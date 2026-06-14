package br.pucgoias.snake;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

// Testes da classe Cobra (movimento, crescimento, direcao e colisao).
class CobraTest {

    @Test
    @DisplayName("Deve mover a cobra para a direita")
    void deveMoverCobraParaDireita() {
        Cobra cobra = new Cobra(new Posicao(5, 5), Direcao.DIREITA);

        cobra.mover();

        assertEquals(new Posicao(5, 6), cobra.getCabeca());
    }

    @Test
    @DisplayName("Deve mover a cobra para cima")
    void deveMoverCobraParaCima() {
        Cobra cobra = new Cobra(new Posicao(5, 5), Direcao.CIMA);

        cobra.mover();

        assertEquals(new Posicao(4, 5), cobra.getCabeca());
    }

    @Test
    @DisplayName("Deve crescer quando comer comida")
    void deveCrescerQuandoComerComida() {
        Cobra cobra = new Cobra(new Posicao(5, 5), Direcao.DIREITA);
        assertEquals(1, cobra.getTamanho());

        cobra.crescer();
        cobra.mover();

        assertEquals(2, cobra.getTamanho());
    }

    @Test
    @DisplayName("Nao deve crescer em movimento normal")
    void naoDeveCrescerEmMovimentoNormal() {
        Cobra cobra = new Cobra(new Posicao(5, 5), Direcao.DIREITA);

        cobra.mover();
        cobra.mover();

        assertEquals(1, cobra.getTamanho());
    }

    @Test
    @DisplayName("Nao deve permitir mudar para a direcao oposta imediata")
    void naoDevePermitirDirecaoOpostaImediata() {
        Cobra cobra = new Cobra(new Posicao(5, 5), Direcao.DIREITA);
        cobra.crescer();
        cobra.mover(); // agora tem 2 segmentos, nao pode inverter

        cobra.mudarDirecao(Direcao.ESQUERDA);

        assertEquals(Direcao.DIREITA, cobra.getDirecao());
    }

    @Test
    @DisplayName("Deve permitir mudar para uma direcao perpendicular")
    void devePermitirDirecaoPerpendicular() {
        Cobra cobra = new Cobra(new Posicao(5, 5), Direcao.DIREITA);

        cobra.mudarDirecao(Direcao.CIMA);

        assertEquals(Direcao.CIMA, cobra.getDirecao());
    }

    @Test
    @DisplayName("Deve detectar colisao com o proprio corpo")
    void deveDetectarColisaoComProprioCorpo() {
        // cobra de tamanho 5 faz um quadrado e bate em si mesma
        Cobra cobra = new Cobra(new Posicao(5, 5), Direcao.DIREITA);
        cobra.crescer();
        cobra.mover(); // (5,6)
        cobra.crescer();
        cobra.mover(); // (5,7)
        cobra.crescer();
        cobra.mover(); // (5,8)
        cobra.crescer();
        cobra.mover(); // (5,9) -> corpo: (5,9)(5,8)(5,7)(5,6)(5,5)

        cobra.mudarDirecao(Direcao.CIMA);
        cobra.mover(); // (4,9)
        cobra.mudarDirecao(Direcao.ESQUERDA);
        cobra.mover(); // (4,8)
        cobra.mudarDirecao(Direcao.BAIXO);
        cobra.mover(); // (5,8) -> colide com o corpo

        assertTrue(cobra.colidiuComProprioCorpo());
    }

    @Test
    @DisplayName("Nao deve indicar colisao quando anda em linha reta")
    void naoDeveColidirEmLinhaReta() {
        Cobra cobra = new Cobra(new Posicao(5, 5), Direcao.DIREITA);
        cobra.crescer();
        cobra.mover();
        cobra.mover();

        assertFalse(cobra.colidiuComProprioCorpo());
    }
}
