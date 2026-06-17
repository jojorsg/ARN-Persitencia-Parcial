--------------------------------------------
ÁRVORE RUBRO NEGRA COM PERSISTÊNCIA PARCIAL        
--------------------------------------------
Implementação em Java (openjdk 21.0.10) de uma árvore rubro-negra que utiliza máquina de ponteiros usando listas de modificações (mods) e back_pointers em cada nó. Cada alteração em um campo (esquerda, direita, pai, cor) é registrada com o número da nova versão, preservando o histórico. A busca recupera o valor cuja versão seja <= à versão consultada.

ESTRUTURA DO PROJETO
--------------------
- Cor.java                 -> Enumeração das cores (RED, BLACK)
- Mod.java                 -> Classe genérica para modificações versionadas
- No.java                  -> Nó da árvore com listas de modificações
- PersistenteARN.java      -> Implementação da árvore rubro-negra persistente
- Main.java                -> Classe principal com leitura do arquivo e execução
- Makefile                 -> Automação de compilação e execução
- README.md                -> Este arquivo

DESCRIÇÃO DAS CLASSES E MÉTODOS
-------------------------------
1. Cor (enum)
   - RED  : representado por 'R' na impressão
   - BLACK: representado por 'N' na impressão

2. Mod<T> (classe genérica)
   - int versao  : versão em que a modificação ocorreu
   - T valor     : valor do campo naquela versão

3. No
   - int key                                           : chave imutável
   - List<Mod<No>> esquerdaMods, direitaMods e paiMods : histórico de ponteiros
   - List<Mod<Cor>> colorMods                          : histórico da cor
   - No(int key, int versao, ...)                      : construtor que já registra os valores iniciais
   - gatValor(List<Mod<T>>)                            : obtém a última modificação
   - getEsquerda(int versao)                           : obtém filho esquerdo na versão
   - getDireita(int versao)                            : obtém filho direito na versão
   - getPai(int versao)                                : obtém pai na versão
   - getCor(int versao)                                : obtém cor na versão
   - setEsquerda(int versao, No valor)                 : registra modificação do filho esquerdo
   - setDireita(int versao, No valor)                  : registra modificação do filho direito
   - setPai(int versao, No valor)                      : registra modificação do pai
   - setCor(int versao, No valor)                      : registra modificação da cor

4. PersistenteARN
   - No[] roots = new Node[100]        : vetor com raízes de cada versão (máx 100 versões)
   - int atualVersao                   : versão atual
   - inserir(int key)                  : insere chave e cria nova versão
   - remover(int key)                  : remove chave, cria nova versão
   - sucessor(int x, int version)      : retorna sucessor ou "infinito"
   - printVersao(int version)          : retorna uma string com impressão ordenada
   - Métodos auxiliares                : girarEsquerda, girarDireita, corDoNo, avo, tio, substituicao, deletarCorrigir, buscaNo, minimo, printEmOrdem.

5. Main
   - Lê arquivo de entrada (passado como argumento)
   - Para cada linha identifica operação (INC, REM, SUC, IMP)
   - Chama método correspondente da árvore
   - Imprime resultados conforme especificado

COMO EXECUTAR
-------------
1. Compilar:
   - Linux: make ou make build
   - Windows: javac Main.java

2. Executar com arquivo de entrada:
   - Linux: java Main "NOME_DO_ARQUIVO".txt ou make run INPUT="NOME_DO_ARQUIVO".txt (por padrão, se não for passado um arquivo INPUT, o comando make run irá executar o arquivo de entrada "entrada.txt")
   - Windows: java Main "NOME_DO_ARQUIVO".txt

3. Limpar arquivos .class:
   make clean

FORMATO DA ENTRADA
------------------
Cada linha contém uma operação:
   - INC <int>           -> insere inteiro e gera nova versão
   - REM <int>           -> remove inteiro (se existir) e gera nova versão
   - SUC <x> <versao>    -> sucessor de x na versão indicada
   - IMP <versao>        -> imprime árvore na versão indicada

FORMATO DA SAÍDA
----------------
- SUC:  
       SUC X versão  
       resultado
- IMP:  
       IMP versão  
       Árvore <chave,profundidade,cor> (cada nó no formato "chave,profundidade,R/N" separados por espaço)

AUTORES/DESENVOLVEDORES
-----------------------
- Josué Roberto Santana Gomes - 603238
- Erisnaldo Machado Pedrosa - 603131

--------------------------------------------------------------
