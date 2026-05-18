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
public class PersistenteARN {
    private final No[] roots = new No[100];
    private int atualVersao = 0;
    
    public PersistenteARN() {
        roots[0] = null;
    }
    
    // Retorna a cor de um nó (Regra: NIL/Folha sempre vai ser preto)
    private Cor corDoNo(No n, int versao) {
        return (n == null) ? Cor.BLACK : n.getCor(versao);
    }
    
    // Retorna o avô de um nó
    private No avo(No n, int versao) {
        No p = n.getPai(versao);
        return (p == null) ? null : p.getPai(versao);
    }
    
    // Retorna o tio de um nó
    private No tio(No n, int versao) {
        No p = n.getPai(versao);
        No g = avo(n, versao);
        if (g == null) return null;
        return (p == g.getEsquerda(versao)) ? g.getDireita(versao) : g.getEsquerda(versao);
    }
    
    // Rotação à esquerda
    private void girarEsquerda(No x, int versao) {
        No y = x.getDireita(versao);
        x.setDireita(versao, y.getEsquerda(versao));
        if (y.getEsquerda(versao) != null) {
            y.getEsquerda(versao).setPai(versao, x);
        }
        y.setPai(versao, x.getPai(versao));
        if (x.getPai(versao) == null) {
            roots[versao] = y;
        } else if (x == x.getPai(versao).getEsquerda(versao)) {
            x.getPai(versao).setEsquerda(versao, y);
        } else {
            x.getPai(versao).setDireita(versao, y);
        }
        y.setEsquerda(versao, x);
        x.setPai(versao, y);
    }
    
    // Rotação à direita
    private void girarDireita(No x, int versao) {
        No y = x.getEsquerda(versao);
        x.setEsquerda(versao, y.getDireita(versao));
        if (y.getDireita(versao) != null) {
            y.getDireita(versao).setPai(versao, x);
        }
        y.setPai(versao, x.getPai(versao));
        if (x.getPai(versao) == null) {
            roots[versao] = y;
        } else if (x == x.getPai(versao).getDireita(versao)) {
            x.getPai(versao).setDireita(versao, y);
        } else {
            x.getPai(versao).setEsquerda(versao, y);
        }
        y.setDireita(versao, x);
        x.setPai(versao, y);
    }
    
    // Inserção (cria uma nova versão)
    public void inserir(int key) {
        int velhaVersao = atualVersao;
        int novaVersao = atualVersao + 1;
        
        // Inicializa a nova raiz como cópia lógica da raiz atual
        roots[novaVersao] = roots[velhaVersao];
        
        // Busca o local de inserção usando a versão antiga (leitura)
        No pai = null;
        No atual = roots[velhaVersao];
        while (atual != null) {
            pai = atual;
            if (key < atual.key) {
                atual = atual.getEsquerda(velhaVersao);
            } else {
                atual = atual.getDireita(velhaVersao);
            }
        }
        
        // Cria novo nó (vermelho) com a nova versão
        No novoNo = new No(key, novaVersao, null, null, pai, Cor.RED);
        
        // Insere na árvore (modifica o pai na nova versão)
        if (pai == null) {
            roots[novaVersao] = novoNo;
        } else if (key < pai.key) {
            pai.setEsquerda(novaVersao, novoNo);
        } else {
            pai.setDireita(novaVersao, novoNo);
        }
        
        // Balanceamento (usa a nova versão para leitura/escrita)
        No n = novoNo;
        while (n != null && n != roots[novaVersao] && corDoNo(n.getPai(novaVersao), novaVersao) == Cor.RED) {
            No u = tio(n, novaVersao);
            No g = avo(n, novaVersao);
            No p = n.getPai(novaVersao);
            
            if (corDoNo(u, novaVersao) == Cor.RED) {
                // Se o tio for vermelho -> vai recorir
                p.setCor(novaVersao, Cor.BLACK);
                u.setCor(novaVersao, Cor.BLACK);
                g.setCor(novaVersao, Cor.RED);
                n = g;
            } else {
                // Se o tio for preto
                if (n == p.getDireita(novaVersao) && p == g.getEsquerda(novaVersao)) {
                    girarEsquerda(p, novaVersao);
                    n = n.getEsquerda(novaVersao); // atualiza referência após rotação
                } else if (n == p.getEsquerda(novaVersao) && p == g.getDireita(novaVersao)) {
                    girarDireita(p, novaVersao);
                    n = n.getDireita(novaVersao);
                }
                
                // Atualiza p e g após mudança
                p = n.getPai(novaVersao);
                g = avo(n, novaVersao);
                
                p.setCor(novaVersao, Cor.BLACK);
                g.setCor(novaVersao, Cor.RED);
                
                if (n == p.getEsquerda(novaVersao)) {
                    girarDireita(g, novaVersao);
                } else {
                    girarEsquerda(g, novaVersao);
                }
                break;
            }
        }
        
        // Regra: raiz sempre vai ser preta
        roots[novaVersao].setCor(novaVersao, Cor.BLACK);
        atualVersao = novaVersao;
    }
    
    // Busca um nó por chave em uma versão específica
    private No buscaNo(int key, int versao) {
        No atual = roots[versao];
        while (atual != null) {
            if (key == atual.key) {
                return atual;
            } else if (key < atual.key) {
                atual = atual.getEsquerda(versao);
            } else {
                atual = atual.getDireita(versao);
            }
        }
        return null;
    }
    
    // Encontra o nó com a menor chave na subárvore
    private No minimo(No no, int versao) {
        while (no.getEsquerda(versao) != null) {
            no = no.getEsquerda(versao);
        }
        return no;
    }
    
    // Substitui uma subárvore por outra (modifica o pai)
    private void substituicao(No u, No v, int versao) {
        No p = u.getPai(versao);
        if (p == null) {
            roots[versao] = v;
        } else if (u == p.getEsquerda(versao)) {
            p.setEsquerda(versao, v);
        } else {
            p.setDireita(versao, v);
        }
        if (v != null) {
            v.setPai(versao, p);  // backk_pointer versionado
        }
    }
    
    // Balanceamento após remoção
    private void deletarCorrigir(No x, No xPai, int versao) {
        while (x != roots[versao] && corDoNo(x, versao) == Cor.BLACK) {
            No p = (x == null) ? xPai : x.getPai(versao);
            if (x == p.getEsquerda(versao)) {
                No w = p.getDireita(versao);
                if (corDoNo(w, versao) == Cor.RED) {
                    w.setCor(versao, Cor.BLACK);
                    p.setCor(versao, Cor.RED);
                    girarEsquerda(p, versao);
                    w = p.getDireita(versao);
                }
                if (corDoNo(w.getEsquerda(versao), versao) == Cor.BLACK &&
                    corDoNo(w.getDireita(versao), versao) == Cor.BLACK) {
                    w.setCor(versao, Cor.RED);
                    x = p;
                } else {
                    if (corDoNo(w.getDireita(versao), versao) == Cor.BLACK) {
                        w.getEsquerda(versao).setCor(versao, Cor.BLACK);
                        w.setCor(versao, Cor.RED);
                        girarDireita(w, versao);
                        w = p.getDireita(versao);
                    }
                    w.setCor(versao, corDoNo(p, versao));
                    p.setCor(versao, Cor.BLACK);
                    w.getDireita(versao).setCor(versao, Cor.BLACK);
                    girarEsquerda(p, versao);
                    x = roots[versao];
                }
            } else {
                No w = p.getEsquerda(versao);
                if (corDoNo(w, versao) == Cor.RED) {
                    w.setCor(versao, Cor.BLACK);
                    p.setCor(versao, Cor.RED);
                    girarDireita(p, versao);
                    w = p.getEsquerda(versao);
                }
                if (corDoNo(w.getDireita(versao), versao) == Cor.BLACK &&
                    corDoNo(w.getEsquerda(versao), versao) == Cor.BLACK) {
                    w.setCor(versao, Cor.RED);
                    x = p;
                } else {
                    if (corDoNo(w.getEsquerda(versao), versao) == Cor.BLACK) {
                        w.getDireita(versao).setCor(versao, Cor.BLACK);
                        w.setCor(versao, Cor.RED);
                        girarEsquerda(w, versao);
                        w = p.getEsquerda(versao);
                    }
                    w.setCor(versao, corDoNo(p, versao));
                    p.setCor(versao, Cor.BLACK);
                    w.getEsquerda(versao).setCor(versao, Cor.BLACK);
                    girarDireita(p, versao);
                    x = roots[versao];
                }
            }
        }
        if (x != null) {
            x.setCor(versao, Cor.BLACK);
        }
    }
    
    // Remoção (cria nova versão)
    public void remover(int key) {
        int velhaVersao = atualVersao;
        int novaVersao = atualVersao + 1;
        
        // Inicializa a nova versão como cópia lógica
        roots[novaVersao] = roots[velhaVersao];
        
        No z = buscaNo(key, velhaVersao);
        if (z == null) {
            // Chave não encontrada, então a nova versão é igual à anterior
            atualVersao = novaVersao;
            return;
        }
        
        No y = z;
        Cor yOriginalCor = corDoNo(y, velhaVersao);
        No x;
        No xPai;
        
        if (z.getEsquerda(velhaVersao) == null) {
            x = z.getDireita(velhaVersao);
            substituicao(z, x, novaVersao);
            xPai = z.getPai(velhaVersao);
        } else if (z.getDireita(velhaVersao) == null) {
            x = z.getEsquerda(velhaVersao);
            substituicao(z, x, novaVersao);
            xPai = z.getPai(velhaVersao);
        } else {
            y = minimo(z.getDireita(velhaVersao), velhaVersao);
            yOriginalCor = corDoNo(y, velhaVersao);
            x = y.getDireita(velhaVersao);
            if (y.getPai(velhaVersao) == z) {
                xPai = y;
            } else {
                substituicao(y, x, novaVersao);
                y.setDireita(novaVersao, z.getDireita(velhaVersao));
                y.getDireita(novaVersao).setPai(novaVersao, y);
                xPai = y.getPai(velhaVersao);
            }
            substituicao(z, y, novaVersao);
            y.setEsquerda(novaVersao, z.getEsquerda(velhaVersao));
            y.getEsquerda(novaVersao).setPai(novaVersao, y);
            y.setCor(novaVersao, z.getCor(velhaVersao));
        }
        
        if (yOriginalCor == Cor.BLACK) {
            deletarCorrigir(x, xPai, novaVersao);
        }
        
        atualVersao = novaVersao;
    }
    
    // Sucessor de x na versão especificada ou na última versão, se a versão informada no arquivo não existir
    public String sucessor(int x, int versao) {
        int v = (versao <= atualVersao) ? versao : atualVersao;
        No atual = roots[v];
        Integer suc = null;
        while (atual != null) {
            if (x < atual.key) {
                suc = atual.key;
                atual = atual.getEsquerda(v);
            } else {
                atual = atual.getDireita(v);
            }
        }
        return (suc == null) ? "infinito" : String.valueOf(suc);
    }
    
    // Impressão em ordem com profundidade e cor
    public String printVersao(int versao) {
        int v = (versao <= atualVersao) ? versao : atualVersao;
        StringBuilder sb = new StringBuilder();
        printEmOrdem(roots[v], v, 0, sb);
        return sb.toString().trim();
    }
    
    private void printEmOrdem(No no, int versao, int depth, StringBuilder sb) {
        if (no == null) return;
        printEmOrdem(no.getEsquerda(versao), versao, depth + 1, sb);
        sb.append(no.key).append(",").append(depth).append(",")
          .append(no.getCor(versao).toString()).append(" ");
        printEmOrdem(no.getDireita(versao), versao, depth + 1, sb);
    }
}







