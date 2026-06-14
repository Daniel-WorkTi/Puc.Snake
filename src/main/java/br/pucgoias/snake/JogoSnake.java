package br.pucgoias.snake;

// Regras do jogo: tabuleiro, cobra, comida, pontuacao e fim de jogo.
public class JogoSnake {

    private static final int PONTOS_POR_COMIDA = 10;

    private final int largura;
    private final int altura;

    private final Cobra cobra;
    private Posicao comida;

    private int pontuacao;
    private boolean jogoAcabou;

    public JogoSnake(int largura, int altura) {
        this(largura, altura, 1);
    }

    public JogoSnake(int largura, int altura, int tamanhoInicial) {
        if (largura <= 0 || altura <= 0) {
            throw new IllegalArgumentException("Largura e altura devem ser positivas.");
        }
        this.largura = largura;
        this.altura = altura;
        this.cobra = new Cobra(new Posicao(altura / 2, largura / 2), Direcao.DIREITA, tamanhoInicial);
        this.pontuacao = 0;
        this.jogoAcabou = false;
    }

    public void mudarDirecao(Direcao direcao) {
        cobra.mudarDirecao(direcao);
    }

    public void setComida(Posicao comida) {
        this.comida = comida;
    }

    public Posicao getComida() {
        return comida;
    }

    // Um passo do jogo: anda a cobra, trata a comida e as colisoes.
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
