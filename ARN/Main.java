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
 
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Uso: java Main <arquivo_de_entrada.txt>");
            System.exit(1);
        }
        
        PersistenteARN arvore = new PersistenteARN();
        String filename = args[0];
        
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                String[] parts = line.split("\\s+");
                String op = parts[0];
                
                switch (op) {
                    case "INC":
                        if (parts.length == 2) {
                            int val = Integer.parseInt(parts[1]);
                            arvore.inserir(val);
                        }
                        break;
                        
                    case "REM":
                        if (parts.length == 2) {
                            int val = Integer.parseInt(parts[1]);
                            arvore.remover(val);
                        }
                        break;
                        
                    case "SUC":
                        if (parts.length == 3) {
                            int x = Integer.parseInt(parts[1]);
                            int v = Integer.parseInt(parts[2]);
                            String resultado = arvore.sucessor(x, v);
                            System.out.println("SUC " + x + " " + v + "\n" + resultado);
                        }
                        break;
                        
                    case "IMP":
                        if (parts.length == 2) {
                            int v = Integer.parseInt(parts[1]);
                            String resultado = arvore.printVersao(v);
                            System.out.println("IMP " + v + "\n" + resultado);
                        }
                        break;
                        
                    default:
                        System.err.println("Operação desconhecida: " + op);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Erro ao converter número: " + e.getMessage());
        }
    }
}
