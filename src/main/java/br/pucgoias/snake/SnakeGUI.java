package br.pucgoias.snake;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

// Tela do jogo em Swing (preto e branco). A cobra anda sozinha e o jogador
// muda a direcao pelas setas ou WASD. Toda a regra fica em JogoSnake/Cobra;
// aqui so desenhamos e lemos o teclado.
public class SnakeGUI extends JPanel {

    private static final int COLUNAS = 24;
    private static final int LINHAS = 18;
    private static final int CELULA = 26;            // tamanho de cada quadradinho (px)
    private static final int TOPO = 56;              // altura da barra de recorde
    private static final int LATERAL = 180;          // largura do painel de pontuacao
    private static final int TAMANHO_INICIAL = 3;
    private static final int VELOCIDADE_MS = 130;    // intervalo entre passos
    private static final Path ARQUIVO_RECORDE = Path.of("recorde.txt");

    // Paleta preto e branco.
    private static final Color FUNDO = Color.BLACK;
    private static final Color FRENTE = Color.WHITE;
    private static final Color CINZA = new Color(120, 120, 120);

    private JogoSnake jogo;
    private Direcao proximaDirecao;
    private final Random random = new Random();
    private final Timer timer;
    private int recorde;

    public SnakeGUI() {
        this.recorde = carregarRecorde();
        int largura = COLUNAS * CELULA + LATERAL;
        int altura = LINHAS * CELULA + TOPO;
        setPreferredSize(new Dimension(largura, altura));
        setBackground(FUNDO);
        setFocusable(true);

        iniciarPartida();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                tratarTecla(e.getKeyCode());
            }
        });

        timer = new Timer(VELOCIDADE_MS, e -> passo());
        timer.start();
    }

    // inicia ou reinicia a partida
    private void iniciarPartida() {
        jogo = new JogoSnake(COLUNAS, LINHAS, TAMANHO_INICIAL);
        proximaDirecao = Direcao.DIREITA;
        gerarComida();
    }

    // chamado pelo timer a cada passo do jogo
    private void passo() {
        if (jogo.isJogoAcabou()) {
            return;
        }
        jogo.mudarDirecao(proximaDirecao);
        jogo.atualizar();

        if (jogo.getComida() == null && !jogo.isJogoAcabou()) {
            gerarComida();
        }

        if (jogo.getPontuacao() > recorde) {
            recorde = jogo.getPontuacao();
            salvarRecorde(recorde);
        }

        repaint();
    }

    // sorteia um lugar livre para a comida
    private void gerarComida() {
        Posicao nova;
        do {
            nova = new Posicao(random.nextInt(LINHAS), random.nextInt(COLUNAS));
        } while (jogo.getCobra().ocupa(nova));
        jogo.setComida(nova);
    }

    // setas ou WASD movem; ENTER reinicia quando o jogo acaba
    private void tratarTecla(int codigo) {
        switch (codigo) {
            case KeyEvent.VK_UP, KeyEvent.VK_W -> proximaDirecao = Direcao.CIMA;
            case KeyEvent.VK_DOWN, KeyEvent.VK_S -> proximaDirecao = Direcao.BAIXO;
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> proximaDirecao = Direcao.ESQUERDA;
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> proximaDirecao = Direcao.DIREITA;
            case KeyEvent.VK_ENTER -> {
                if (jogo.isJogoAcabou()) {
                    iniciarPartida();
                }
            }
            default -> { /* ignora outras teclas */ }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int boardW = COLUNAS * CELULA;

        desenharTopo(g2, boardW);
        desenharLateral(g2, boardW);
        desenharTabuleiro(g2);

        if (jogo.isJogoAcabou()) {
            desenharFimDeJogo(g2, boardW);
        }
    }

    // barra de cima com o recorde
    private void desenharTopo(Graphics2D g2, int boardW) {
        g2.setColor(FRENTE);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(8, 8, boardW - 16, TOPO - 16);
        g2.setFont(new Font("Monospaced", Font.BOLD, 20));
        g2.drawString("RECORDE: " + recorde, 24, TOPO - 22);
        g2.setFont(new Font("Monospaced", Font.BOLD, 16));
        g2.drawString("SNAKE - PUC GOIAS", boardW - 230, TOPO - 22);
    }

    // painel da direita com a pontuacao
    private void desenharLateral(Graphics2D g2, int boardW) {
        int x = boardW + 16;
        g2.setColor(FRENTE);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(x, TOPO + 8, LATERAL - 32, 90);

        g2.setFont(new Font("Monospaced", Font.PLAIN, 16));
        g2.drawString("PONTOS", x + 18, TOPO + 38);
        g2.setFont(new Font("Monospaced", Font.BOLD, 34));
        g2.drawString(String.valueOf(jogo.getPontuacao()), x + 18, TOPO + 78);

        g2.setColor(CINZA);
        g2.setFont(new Font("Monospaced", Font.PLAIN, 13));
        g2.drawString("Setas ou WASD", x + 8, TOPO + 150);
        g2.drawString("para mover", x + 8, TOPO + 170);
        g2.drawString("Tamanho: " + jogo.getCobra().getTamanho(), x + 8, TOPO + 210);
    }

    // desenha o tabuleiro, a cobra e a comida
    private void desenharTabuleiro(Graphics2D g2) {
        int offsetY = TOPO;

        // moldura
        g2.setColor(FRENTE);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(0, offsetY, COLUNAS * CELULA, LINHAS * CELULA);

        // comida
        Posicao comida = jogo.getComida();
        if (comida != null) {
            int cx = comida.getColuna() * CELULA;
            int cy = offsetY + comida.getLinha() * CELULA;
            g2.setColor(FRENTE);
            g2.fillOval(cx + 6, cy + 6, CELULA - 12, CELULA - 12);
        }

        // cobra: cabeca cheia, corpo com borda
        Cobra cobra = jogo.getCobra();
        Posicao cabeca = cobra.getCabeca();
        for (Posicao p : cobra.getCorpo()) {
            int px = p.getColuna() * CELULA;
            int py = offsetY + p.getLinha() * CELULA;
            if (p.equals(cabeca)) {
                g2.setColor(FRENTE);
                g2.fillRect(px + 1, py + 1, CELULA - 2, CELULA - 2);
            } else {
                g2.setColor(FRENTE);
                g2.fillRect(px + 3, py + 3, CELULA - 6, CELULA - 6);
                g2.setColor(FUNDO);
                g2.drawRect(px + 3, py + 3, CELULA - 6, CELULA - 6);
            }
        }
    }

    // tela de fim de jogo
    private void desenharFimDeJogo(Graphics2D g2, int boardW) {
        int offsetY = TOPO;
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, offsetY, boardW, LINHAS * CELULA);

        g2.setColor(FRENTE);
        g2.setFont(new Font("Monospaced", Font.BOLD, 30));
        String fim = "FIM DE JOGO";
        g2.drawString(fim, (boardW - g2.getFontMetrics().stringWidth(fim)) / 2, offsetY + LINHAS * CELULA / 2 - 10);

        g2.setFont(new Font("Monospaced", Font.PLAIN, 16));
        String r = "ENTER para jogar de novo";
        g2.drawString(r, (boardW - g2.getFontMetrics().stringWidth(r)) / 2, offsetY + LINHAS * CELULA / 2 + 24);
    }

    // le e grava o recorde num arquivo simples
    private int carregarRecorde() {
        try {
            if (Files.exists(ARQUIVO_RECORDE)) {
                return Integer.parseInt(Files.readString(ARQUIVO_RECORDE).trim());
            }
        } catch (IOException | NumberFormatException ignorado) {
            // se nao der pra ler, comeca em 0
        }
        return 0;
    }

    private void salvarRecorde(int valor) {
        try {
            Files.writeString(ARQUIVO_RECORDE, String.valueOf(valor));
        } catch (IOException ignorado) {
            // se nao der pra salvar, deixa so na memoria
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame janela = new JFrame("Snake Game - PUC Goias");
            SnakeGUI painel = new SnakeGUI();
            janela.add(painel);
            janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            janela.setResizable(false);
            janela.pack();
            janela.setLocationRelativeTo(null);
            janela.setVisible(true);
            painel.requestFocusInWindow();
        });
    }
}
