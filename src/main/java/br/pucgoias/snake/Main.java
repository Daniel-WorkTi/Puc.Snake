package br.pucgoias.snake;

import java.util.Random;
import java.util.Scanner;

/**
 * Demonstracao do jogo no terminal.
 *
 * <p>Esta classe NAO faz parte da logica testada: ela apenas usa as classes
 * {@link JogoSnake} e {@link Cobra} para mostrar uma partida jogavel no console,
 * util para apresentar o projeto ao vivo.</p>
 *
 * <p>Controles: digite W (cima), A (esquerda), S (baixo), D (direita) e ENTER.
 * Cada comando avanca um passo do jogo. Digite Q para sair.</p>
 */
public class Main {

    private static final int LARGURA = 20;
    private static final int ALTURA = 10;

    public static void main(String[] args) {
        JogoSnake jogo = new JogoSnake(LARGURA, ALTURA);
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);

        gerarComida(jogo, random);

        System.out.println("=== SNAKE GAME - PUC GOIAS ===");
        System.out.println("Controles: W = cima | A = esquerda | S = baixo | D = direita");
        System.out.println("Digite a tecla e pressione ENTER. Q para sair.\n");

        desenhar(jogo);

        while (!jogo.isJogoAcabou()) {
            System.out.print("Movimento (W/A/S/D, Q sai): ");
            if (!scanner.hasNextLine()) {
                break;
            }
            String entrada = scanner.nextLine().trim().toLowerCase();

            if (entrada.equals("q")) {
                System.out.println("Saindo do jogo. Ate logo!");
                scanner.close();
                return;
            }

            Direcao direcao = lerDirecao(entrada);
            if (direcao != null) {
                jogo.mudarDirecao(direcao);
            }

            jogo.atualizar();

            // Se a cobra comeu a comida, gera uma nova.
            if (jogo.getComida() == null && !jogo.isJogoAcabou()) {
                gerarComida(jogo, random);
            }

            desenhar(jogo);
        }

        System.out.println("\n=== FIM DE JOGO ===");
        System.out.println("Pontuacao final: " + jogo.getPontuacao());
        scanner.close();
    }

    /**
     * Converte a tecla digitada em uma {@link Direcao}.
     */
    private static Direcao lerDirecao(String entrada) {
        return switch (entrada) {
            case "w" -> Direcao.CIMA;
            case "a" -> Direcao.ESQUERDA;
            case "s" -> Direcao.BAIXO;
            case "d" -> Direcao.DIREITA;
            default -> null;
        };
    }

    /**
     * Sorteia uma posicao livre (sem cobra) para colocar a comida.
     */
    private static void gerarComida(JogoSnake jogo, Random random) {
        Posicao nova;
        do {
            int linha = random.nextInt(jogo.getAltura());
            int coluna = random.nextInt(jogo.getLargura());
            nova = new Posicao(linha, coluna);
        } while (jogo.getCobra().ocupa(nova));
        jogo.setComida(nova);
    }

    /**
     * Desenha o tabuleiro no terminal:
     * # = parede, O = cabeca, o = corpo, * = comida.
     */
    private static void desenhar(JogoSnake jogo) {
        System.out.println();
        Cobra cobra = jogo.getCobra();
        Posicao cabeca = cobra.getCabeca();
        Posicao comida = jogo.getComida();

        // Borda superior
        System.out.println("#".repeat(jogo.getLargura() + 2));

        for (int linha = 0; linha < jogo.getAltura(); linha++) {
            StringBuilder sb = new StringBuilder("#");
            for (int coluna = 0; coluna < jogo.getLargura(); coluna++) {
                Posicao atual = new Posicao(linha, coluna);
                if (atual.equals(cabeca)) {
                    sb.append('O');
                } else if (cobra.ocupa(atual)) {
                    sb.append('o');
                } else if (atual.equals(comida)) {
                    sb.append('*');
                } else {
                    sb.append(' ');
                }
            }
            sb.append('#');
            System.out.println(sb);
        }

        // Borda inferior
        System.out.println("#".repeat(jogo.getLargura() + 2));
        System.out.println("Pontuacao: " + jogo.getPontuacao() + " | Tamanho: " + cobra.getTamanho());
    }
}
