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
public enum Cor {
    RED, BLACK;
    
    @Override
    public String toString() {
        return this == RED ? "R" : "N";
    }
}
