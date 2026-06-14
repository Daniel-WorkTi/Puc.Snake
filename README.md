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
├── pom.xml
└── src/
    ├── main/java/br/pucgoias/snake/
    │   ├── Direcao.java      # Direções: CIMA, BAIXO, ESQUERDA, DIREITA
    │   ├── Posicao.java      # Posição no tabuleiro (linha, coluna)
    │   ├── Cobra.java        # Corpo e movimento da cobra
    │   └── JogoSnake.java    # Regras principais do jogo
    └── test/java/br/pucgoias/snake/
        ├── CobraTest.java
        └── JogoSnakeTest.java
```

## Classes

- **Direcao**: enum com as quatro direções e a lógica de direção oposta.
- **Posicao**: representa uma posição (linha e coluna) no tabuleiro.
- **Cobra**: guarda o corpo da cobra, controla movimento, crescimento e colisão com o próprio corpo.
- **JogoSnake**: controla o tabuleiro (largura/altura), a comida, a pontuação, as colisões com parede/corpo e o fim de jogo.

## Como compilar e rodar os testes

Pré-requisitos: **Java 17+** e **Maven 3.8+**.

```bash
# Compilar
mvn compile

# Rodar todos os testes automatizados (JUnit 5)
mvn test
```

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

## Fluxo de versionamento (Gitflow)

```
main
 └── develop
      ├── feature/posicao-direcao
      ├── feature/movimentacao-cobra
      ├── feature/jogo-snake
      └── feature/testes-junit
```

Cada funcionalidade é desenvolvida em uma branch `feature/*`, com merge para `develop` e, ao final, `develop` é integrada à `main`.
