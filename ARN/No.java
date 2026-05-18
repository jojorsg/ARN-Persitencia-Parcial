/**
 * Arvore Rubro Negra com Persistência Parcial: 
 * Utilizando o Modelo de Maquina de Ponteiro para implementar persistência parcial
 * 
 * Disciplina: Estrutura de Dados
 * 
 * Alunos:
 * @author Josué Roberto 
 * @author Erisnaldo Machado
 * @version 1.0
 * @since 18/04/2026
 * 
 */
import java.util.ArrayList;
import java.util.List;

public class No {
    final int key;
    private final List<Mod<No>> esquerdaMods; //histórico do filho esquerdo
    private final List<Mod<No>> direitaMods; //histórico do filho direito
    private final List<Mod<No>> paiMods;     //histórico do pai (back_pointers)
    private final List<Mod<Cor>> corMods;  //histórico da cor
    
    public No(int key, int versao, No esquerda, No direita, No pai, Cor cor) {
        this.key = key;
        this.esquerdaMods = new ArrayList<>();
        this.direitaMods = new ArrayList<>();
        this.paiMods = new ArrayList<>();
        this.corMods = new ArrayList<>();
        
        esquerdaMods.add(new Mod<>(versao, esquerda));
        direitaMods.add(new Mod<>(versao, direita));
        paiMods.add(new Mod<>(versao, pai));
        corMods.add(new Mod<>(versao, cor));
    }
    
    // Percorre a lista de Mod até encontrar a última modificação com versão <= a versão consultada
    private <T> T getValor(List<Mod<T>> mods, int versao, T defaultValor) {
        T resultado = defaultValor;
        for (Mod<T> mod : mods) {
            if (mod.versao <= versao) {
                resultado = mod.valor;
            } else {
                break;
            }
        }
        return resultado;
    }
    
    public No getEsquerda(int versao) {
        return getValor(esquerdaMods, versao, null);
    }
    
    public No getDireita(int versao) {
        return getValor(direitaMods, versao, null);
    }
    
    public No getPai(int versao) {
        return getValor(paiMods, versao, null);
    }
    
    public Cor getCor(int versao) {
        return getValor(corMods, versao, Cor.BLACK);
    }
    
    // Cada alteração de ponteiro ou cor vai resultadoar em um novo Mod, que será acrescentado a lista
    public void setEsquerda(int versao, No valor) {
        esquerdaMods.add(new Mod<>(versao, valor));
    }
    
    public void setDireita(int versao, No valor) {
        direitaMods.add(new Mod<>(versao, valor));
    }
    
    public void setPai(int versao, No valor) {
        paiMods.add(new Mod<>(versao, valor));
    }
    
    public void setCor(int versao, Cor valor) {
        corMods.add(new Mod<>(versao, valor));
    }
}
