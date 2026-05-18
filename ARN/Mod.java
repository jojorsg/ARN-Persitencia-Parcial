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
public class Mod<T> {
    int versao;
    T valor;
    
    public Mod(int versao, T valor) {
        this.versao = versao;
        this.valor = valor;
    }
}
