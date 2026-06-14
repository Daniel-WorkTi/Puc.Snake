package br.pucgoias.snake;

/**
 * Controla as regras principais do jogo da cobrinha:
 * dimensoes do tabuleiro, a cobra, a posicao da comida,
 * as colisoes (parede e corpo), a pontuacao e o fim de jogo.
 */
public class JogoSnake {

    /** Pontos ganhos a cada comida consumida. */
    private static final int PONTOS_POR_COMIDA = 10;

    private final int largura;
    private final int altura;

    private final Cobra cobra;
    private Posicao comida;

    private int pontuacao;
    private boolean jogoAcabou;

    /**
     * Cria um jogo com o tabuleiro nas dimensoes informadas.
     * A cobra comeca no centro, movendo-se para a direita.
     *
     * @param largura numero de colunas do tabuleiro
     * @param altura  numero de linhas do tabuleiro
     */
    public JogoSnake(int largura, int altura) {
        if (largura <= 0 || altura <= 0) {
            throw new IllegalArgumentException("Largura e altura devem ser positivas.");
        }
        this.largura = largura;
        this.altura = altura;
        this.cobra = new Cobra(new Posicao(altura / 2, largura / 2), Direcao.DIREITA);
        this.pontuacao = 0;
        this.jogoAcabou = false;
    }

    /**
     * Define a direcao desejada para a cobra no proximo passo.
     * Direcoes opostas a atual sao ignoradas pela propria cobra.
     *
     * @param direcao nova direcao
     */
    public void mudarDirecao(Direcao direcao) {
        cobra.mudarDirecao(direcao);
    }

    /**
     * Define a posicao da comida no tabuleiro.
     *
     * @param comida posicao da comida
     */
    public void setComida(Posicao comida) {
        this.comida = comida;
    }

    /**
     * @return a posicao atual da comida (pode ser {@code null} se nao houver)
     */
    public Posicao getComida() {
        return comida;
    }

    /**
     * Executa um passo do jogo:
     * <ol>
     *     <li>verifica se a cobra vai comer a comida;</li>
     *     <li>move a cobra (crescendo se comeu);</li>
     *     <li>verifica colisoes com parede e com o proprio corpo;</li>
     *     <li>atualiza a pontuacao.</li>
     * </ol>
     *
     * <p>Se o jogo ja acabou, o metodo nao faz nada.</p>
     */
    public void atualizar() {
        if (jogoAcabou) {
            return;
        }

        Posicao proximaCabeca = cobra.getCabeca().mover(cobra.getDirecao());
        boolean vaiComer = proximaCabeca.equals(comida);

        if (vaiComer) {
            cobra.crescer();
        }

        cobra.mover();

        if (colidiuComParede() || cobra.colidiuComProprioCorpo()) {
            jogoAcabou = true;
            return;
        }

        if (vaiComer) {
            pontuacao += PONTOS_POR_COMIDA;
            comida = null;
        }
    }

    /**
     * Verifica se a cabeca da cobra saiu dos limites do tabuleiro.
     *
     * @return {@code true} se houve colisao com a parede
     */
    public boolean colidiuComParede() {
        Posicao cabeca = cobra.getCabeca();
        return cabeca.getLinha() < 0
                || cabeca.getColuna() < 0
                || cabeca.getLinha() >= altura
                || cabeca.getColuna() >= largura;
    }

    public boolean isJogoAcabou() {
        return jogoAcabou;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public Cobra getCobra() {
        return cobra;
    }

    public int getLargura() {
        return largura;
    }

    public int getAltura() {
        return altura;
    }
}
