# Snake Game Java — Projeto Integrador IV-A

Jogo da cobrinha (Snake) desenvolvido em **Java**, com foco na **lógica principal do jogo** e em **testes automatizados** usando **JUnit 5**. O projeto é versionado com **Git/GitHub**, simulando um fluxo de desenvolvimento colaborativo com branches, commits e merges (Gitflow).

## Objetivo

Desenvolver um módulo Java chamado *Snake Game*, aplicando testes automatizados com JUnit 5 e controle de versão com Git/GitHub, simulando um fluxo de desenvolvimento colaborativo.

## Por que o Snake?

O Snake é um jogo conhecido, simples de entender e que permite aplicar testes automatizados em várias regras, como **movimentação**, **crescimento da cobra**, **pontuação** e **colisões**.

## Estrutura do projeto

```
snake-game-java/
├── README.md
├── DOCUMENTACAO.md          # Documentação completa para apresentação
├── pom.xml
└── src/
    ├── main/java/br/pucgoias/snake/
    │   ├── Direcao.java      # Direções: CIMA, BAIXO, ESQUERDA, DIREITA
    │   ├── Posicao.java      # Posição no tabuleiro (linha, coluna)
    │   ├── Cobra.java        # Corpo e movimento da cobra
    │   ├── JogoSnake.java    # Regras principais do jogo
    │   ├── Main.java         # Versão jogável no terminal
    │   └── SnakeGUI.java     # Versão jogável com janela gráfica (Swing)
    └── test/java/br/pucgoias/snake/
        ├── CobraTest.java
        └── JogoSnakeTest.java
```

## Classes principais (a lógica testada)

- **Direcao**: enum com as quatro direções e a lógica de direção oposta.
- **Posicao**: representa uma posição (linha e coluna) no tabuleiro.
- **Cobra**: guarda o corpo da cobra, controla movimento, crescimento e colisão com o próprio corpo.
- **JogoSnake**: controla o tabuleiro (largura/altura), a comida, a pontuação, as colisões com parede/corpo e o fim de jogo.

> `Main` e `SnakeGUI` são apenas formas de **jogar** (terminal e janela). Toda a regra do jogo está em `JogoSnake` e `Cobra`, que são as classes cobertas pelos testes.

## Como compilar e rodar

Pré-requisitos: **Java 17+** e **Maven 3.8+**.

```bash
# Compilar
mvn compile

# Rodar todos os testes automatizados (JUnit 5)
mvn test

# Jogar na janela gráfica (recomendado)
mvn compile exec:java

# Jogar no terminal (alternativa simples)
mvn compile
java -cp target/classes br.pucgoias.snake.Main
```

### Controles da versão gráfica

- Setas (↑ ↓ ← →) ou **W A S D** para mover a cobra.
- A cobra começa com tamanho 3 e anda sozinha em tempo real.
- **ENTER** reinicia após o fim de jogo.
- A janela mostra a caixa de **RECORDE** no topo e a **pontuação** na lateral.

## Testes automatizados (JUnit 5)

| Teste | O que verifica |
|-------|----------------|
| `deveMoverCobraParaDireita` | Movimento para a direita |
| `deveMoverCobraParaCima` | Movimento para cima |
| `deveCrescerQuandoComerComida` | Crescimento ao comer |
| `deveAumentarPontuacaoQuandoComerComida` | Pontuação ao comer |
| `deveFinalizarJogoAoBaterNaParede` | Colisão com a parede |
| `deveFinalizarJogoAoBaterNoProprioCorpo` | Colisão com o próprio corpo |
| `naoDevePermitirDirecaoOpostaImediata` | Bloqueio de direção oposta |

São **15 testes** no total (incluindo casos complementares), todos passando.

## Fluxo de versionamento (Gitflow)

```
main
 └── develop
      ├── feature/posicao-direcao
      ├── feature/movimentacao-cobra
      ├── feature/jogo-snake
      ├── feature/testes-junit
      ├── feature/jogo-terminal
      └── feature/interface-grafica
```

Cada funcionalidade é desenvolvida em uma branch `feature/*`, com merge para `develop` e, ao final, `develop` é integrada à `main`.

A documentação detalhada (para apresentação) está em [DOCUMENTACAO.md](DOCUMENTACAO.md).
