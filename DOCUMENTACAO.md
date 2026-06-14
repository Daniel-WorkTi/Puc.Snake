# Documentação do Projeto — Snake Game em Java

**Disciplina:** Projeto Integrador IV-A
**Tema:** Jogo da cobrinha (Snake) com foco em lógica, testes automatizados (JUnit 5) e controle de versão (Git/GitHub).

Este documento foi escrito para ser fácil de entender e usado como apoio na apresentação. Ele explica **o que** foi feito, **como** funciona e **por quê** cada decisão foi tomada.

---

## 1. Resumo do projeto

O projeto é um **jogo da cobrinha (Snake)** desenvolvido em **Java**. O foco principal não é o visual, e sim a **lógica do jogo**, porque ela pode ser verificada de forma automática com **testes (JUnit 5)**.

Além da lógica, o jogo pode ser jogado de duas formas:

1. **Janela gráfica** (preto e branco, em tempo real) — feita com a biblioteca Swing do Java.
2. **Terminal** (versão simples em texto) — útil como alternativa.

Todo o desenvolvimento foi versionado com **Git**, usando o fluxo de trabalho **Gitflow** (branches `main`, `develop` e várias `feature/*`), simulando como uma equipe trabalharia em conjunto.

---

## 2. Objetivo

Desenvolver um módulo Java chamado *Snake Game*, aplicando:

- **Testes automatizados** com JUnit 5;
- **Controle de versão** com Git/GitHub;
- Um **fluxo colaborativo** com branches, commits e merges.

---

## 3. Justificativa (por que escolhemos o Snake?)

O Snake é um jogo:

- **Conhecido** — todo mundo entende as regras rapidamente.
- **Simples** — dá para implementar a lógica sem complicação.
- **Rico em regras testáveis** — movimentação, crescimento, pontuação e colisões. Cada regra vira um ou mais testes automatizados.

Ou seja, é o equilíbrio perfeito entre "simples de fazer" e "ótimo para demonstrar testes".

---

## 4. Tecnologias utilizadas

| Tecnologia | Para que serve |
|-----------|----------------|
| **Java 17+** | Linguagem de programação do projeto |
| **Maven** | Gerenciador de dependências e de build (compilar, testar, rodar) |
| **JUnit 5** | Framework de testes automatizados |
| **Swing** | Biblioteca gráfica do próprio Java (a janela do jogo) |
| **Git / GitHub** | Controle de versão e repositório remoto |

---

## 5. Estrutura do projeto

```
snake-game-java/
├── README.md                # Apresentação rápida
├── DOCUMENTACAO.md          # Este documento
├── pom.xml                  # Configuração do Maven (dependências e plugins)
└── src/
    ├── main/java/br/pucgoias/snake/   # Código do jogo
    │   ├── Direcao.java     # Direções (CIMA, BAIXO, ESQUERDA, DIREITA)
    │   ├── Posicao.java     # Posição no tabuleiro (linha, coluna)
    │   ├── Cobra.java       # Corpo e movimento da cobra
    │   ├── JogoSnake.java   # Regras principais do jogo
    │   ├── Main.java        # Versão jogável no terminal
    │   └── SnakeGUI.java    # Versão jogável com janela gráfica
    └── test/java/br/pucgoias/snake/   # Testes automatizados
        ├── CobraTest.java
        └── JogoSnakeTest.java
```

**Por que separar `main` e `test`?** É o padrão do Maven: o código do programa fica em `src/main` e os testes em `src/test`. Isso mantém o projeto organizado e permite rodar os testes sem misturar com o código de produção.

---

## 6. Como o jogo funciona (a lógica)

A ideia central é dividir o problema em pequenas peças, cada uma com uma responsabilidade clara. Isso se chama **separação de responsabilidades** e facilita testar cada parte.

### 6.1. `Direcao` (enum)

Representa as quatro direções possíveis. Cada direção guarda o **deslocamento** que provoca em linha e coluna.

- `CIMA` → diminui a linha (sobe).
- `BAIXO` → aumenta a linha (desce).
- `ESQUERDA` → diminui a coluna.
- `DIREITA` → aumenta a coluna.

Também sabe qual é a sua **direção oposta** (ex.: o oposto de `DIREITA` é `ESQUERDA`). Isso é usado para impedir que a cobra dê meia-volta instantânea (o que faria ela bater no próprio corpo).

### 6.2. `Posicao`

Guarda uma posição no tabuleiro: **linha** e **coluna** (ex.: linha 5, coluna 3).

É uma classe **imutável**: quando a cobra anda, em vez de alterar a posição, criamos uma **nova** posição com o método `mover(direcao)`. Isso evita erros difíceis de achar (ninguém altera a posição "por baixo dos panos").

Também tem `equals` e `hashCode`, para conseguir comparar duas posições (ex.: "a cabeça está na mesma casa da comida?").

### 6.3. `Cobra`

É o coração do jogo. Guarda o **corpo da cobra** (uma lista de posições, sendo a primeira sempre a **cabeça**) e sabe:

- **mover()** — anda um passo na direção atual. Adiciona uma nova cabeça na frente e remove a cauda. Se a cobra "deve crescer", a cauda **não** é removida (a cobra fica maior).
- **crescer()** — marca que no próximo movimento a cobra deve aumentar.
- **mudarDirecao(novaDirecao)** — troca a direção, mas **bloqueia** a direção oposta (não deixa dar meia-volta).
- **colidiuComProprioCorpo()** — verifica se a cabeça caiu em cima de alguma parte do corpo.

### 6.4. `JogoSnake`

Junta tudo e aplica as **regras principais**:

- Controla o **tabuleiro** (largura e altura).
- Controla a **cobra** e a **comida**.
- No método **atualizar()** (um passo do jogo):
  1. verifica se a cobra vai comer a comida;
  2. move a cobra (crescendo, se comeu);
  3. verifica colisão com **parede** e com o **próprio corpo**;
  4. atualiza a **pontuação** (+10 por comida);
  5. marca **fim de jogo** se houve colisão.

---

## 7. As regras do jogo (resumo)

1. A cobra anda continuamente na direção atual.
2. O jogador muda a direção (não pode inverter o sentido de uma vez).
3. Quando a cabeça chega na comida, a cobra **cresce** e a **pontuação aumenta** (+10).
4. Uma nova comida aparece em um lugar livre.
5. O jogo **acaba** se a cobra bater na **parede** ou no **próprio corpo**.

---

## 8. Testes automatizados (JUnit 5)

Esta é a parte mais importante para a disciplina. **Teste automatizado** é um código que verifica, sozinho, se o programa funciona como esperado. Toda vez que rodamos `mvn test`, todos os testes são executados e o Maven avisa se algo quebrou.

### 8.1. Como um teste é escrito (padrão AAA)

Cada teste segue a ideia **Arrange – Act – Assert** (Preparar – Agir – Verificar):

1. **Arrange (preparar):** cria a situação (ex.: uma cobra em uma posição).
2. **Act (agir):** executa a ação (ex.: mover a cobra).
3. **Assert (verificar):** confere se o resultado é o esperado (ex.: a cabeça está na casa certa).

Exemplo real do projeto:

```java
@Test
@DisplayName("Deve mover a cobra para a direita")
void deveMoverCobraParaDireita() {
    Cobra cobra = new Cobra(new Posicao(5, 5), Direcao.DIREITA); // Arrange
    cobra.mover();                                               // Act
    assertEquals(new Posicao(5, 6), cobra.getCabeca());          // Assert
}
```

- `@Test` indica que o método é um teste.
- `@DisplayName` dá um nome legível ao teste.
- `assertEquals(esperado, obtido)` falha se os dois valores forem diferentes.

### 8.2. Lista de testes e o que cada um prova

**Sobre a cobra (`CobraTest`):**

| Teste | O que verifica |
|-------|----------------|
| `deveMoverCobraParaDireita` | A cobra anda corretamente para a direita |
| `deveMoverCobraParaCima` | A cobra anda corretamente para cima |
| `deveCrescerQuandoComerComida` | Ao comer, a cobra fica maior |
| `naoDeveCrescerEmMovimentoNormal` | Sem comer, o tamanho não muda |
| `naoDevePermitirDirecaoOpostaImediata` | A cobra não dá meia-volta instantânea |
| `devePermitirDirecaoPerpendicular` | É possível virar para os lados |
| `deveDetectarColisaoComProprioCorpo` | A cobra detecta quando bate em si mesma |
| `naoDeveColidirEmLinhaReta` | Andando reto, não há colisão falsa |

**Sobre o jogo (`JogoSnakeTest`):**

| Teste | O que verifica |
|-------|----------------|
| `deveAumentarPontuacaoQuandoComerComida` | A pontuação sobe ao comer (+10) |
| `deveCrescerAoComerComidaNoJogo` | A cobra cresce dentro da partida |
| `deveFinalizarJogoAoBaterNaParede` | O jogo acaba ao sair do tabuleiro |
| `deveFinalizarJogoAoBaterNoProprioCorpo` | O jogo acaba ao bater no corpo |
| `naoDevePermitirDirecaoOpostaImediata` | A regra da direção oposta vale no jogo |
| `jogoNaoDeveComecarAcabado` | No início, o jogo está ativo e zerado |
| `comidaDeveSerConsumida` | Após comer, a comida some do tabuleiro |

**Total: 15 testes, todos passando.**

### 8.3. Um detalhe interessante para comentar com os professores

No teste de colisão com o próprio corpo, foi preciso usar uma cobra de **tamanho 5**, não 4. Por quê?

Quando a cobra anda, ela coloca a cabeça em uma casa nova **e** a cauda libera a casa antiga **no mesmo passo**. Com tamanho 4, ao fazer um quadradinho a cabeça cairia exatamente na casa que a cauda acabou de liberar — então **não há colisão**. Com tamanho 5, a cabeça alcança uma parte do corpo que ainda está ocupada, e a colisão acontece de verdade.

Isso mostra que os testes refletem o **comportamento real** do jogo, e não apenas casos fáceis.

---

## 9. Controle de versão com Git e GitHub

### 9.1. O que é Gitflow

Gitflow é uma forma organizada de usar branches (ramificações) do Git:

- **`main`** — versão estável/final do projeto.
- **`develop`** — onde as funcionalidades são integradas antes de virar versão final.
- **`feature/*`** — uma branch para cada funcionalidade nova. Quando pronta, é unida (`merge`) à `develop`.

No fim, a `develop` é unida à `main`, gerando a versão final.

### 9.2. Branches usadas no projeto

```
main
 └── develop
      ├── feature/posicao-direcao      (classes Posicao e Direcao)
      ├── feature/movimentacao-cobra   (mover e crescer)
      ├── feature/jogo-snake           (regras, colisões e pontuação)
      ├── feature/testes-junit         (testes automatizados)
      ├── feature/jogo-terminal        (versão de terminal)
      └── feature/interface-grafica    (janela gráfica)
```

### 9.3. Comandos Git principais (exemplo de uma feature)

```bash
# Cria a estrutura inicial
git init
git add .
git commit -m "Cria estrutura inicial do projeto Snake Game"

# Cria a branch de desenvolvimento
git branch develop
git checkout develop

# Trabalha em uma funcionalidade
git checkout -b feature/movimentacao-cobra
git add .
git commit -m "Implementa movimentacao da cobra"

# Integra a funcionalidade na develop
git checkout develop
git merge --no-ff feature/movimentacao-cobra

# No final, integra a develop na main
git checkout main
git merge --no-ff develop

# Envia para o GitHub
git remote add origin URL_DO_REPOSITORIO
git push -u origin main
git push -u origin develop
```

> A opção `--no-ff` ("no fast-forward") cria um commit de merge, deixando o histórico bem visível — dá para enxergar claramente onde cada funcionalidade entrou.

### 9.4. Como visualizar o histórico

```bash
git log --oneline --graph --all
```

Esse comando mostra um desenho das branches e merges, ótimo para apresentar a evolução do projeto.

---

## 10. Como executar o projeto

Pré-requisitos: **Java 17+** e **Maven** instalados.

```bash
# 1) Rodar os testes automatizados (prova que a lógica funciona)
mvn test

# 2) Jogar na janela gráfica (recomendado para a apresentação)
mvn compile exec:java

# 3) Jogar no terminal (alternativa simples)
mvn compile
java -cp target/classes br.pucgoias.snake.Main
```

**Controles da janela gráfica:** setas (↑ ↓ ← →) ou **W A S D**. A cobra começa com tamanho 3, há uma caixa de **RECORDE** no topo e a **pontuação** na lateral. Ao perder, **ENTER** reinicia.

---

## 11. Roteiro de defesa (possíveis perguntas dos professores)

**"Por que separou em várias classes?"**
Para deixar cada parte com uma responsabilidade só. Assim fica mais fácil de entender, manter e, principalmente, testar cada regra isoladamente.

**"O que são esses testes e por que são importantes?"**
São verificações automáticas. Se eu mudar o código e quebrar uma regra, o teste falha na hora. Isso dá segurança para evoluir o projeto sem medo.

**"Como você garante que a cobra não atravessa a parede?"**
O método `colidiuComParede()` confere se a cabeça saiu dos limites do tabuleiro. Há um teste (`deveFinalizarJogoAoBaterNaParede`) que prova isso.

**"Por que a cobra não pode dar meia-volta?"**
Porque ela bateria no próprio corpo imediatamente. O `enum Direcao` sabe a direção oposta e a classe `Cobra` bloqueia essa troca. Existe um teste para isso.

**"Para que serve o Gitflow aqui?"**
Para simular um trabalho em equipe: cada funcionalidade em uma branch separada, integrada com merge. O histórico mostra a evolução organizada do projeto.

**"A parte gráfica também é testada?"**
A parte gráfica (`SnakeGUI`) só desenha e captura o teclado. A lógica de verdade está em `JogoSnake` e `Cobra`, que **são** testadas. Ou seja, a janela usa exatamente a mesma lógica validada pelos testes.

---

## 12. Conclusão

O projeto cumpre todos os objetivos propostos:

- Implementa a **lógica completa** do Snake (movimento, crescimento, colisões e pontuação).
- Possui **15 testes automatizados** em JUnit 5, todos passando.
- Foi **versionado com Git/GitHub** seguindo o fluxo **Gitflow**.
- Ainda oferece **duas formas de jogar** (terminal e janela gráfica), reaproveitando a mesma lógica testada.

É um exemplo claro de como organizar código, escrever testes e versionar um projeto de forma profissional.
