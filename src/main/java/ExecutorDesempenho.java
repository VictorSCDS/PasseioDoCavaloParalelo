import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExecutorDesempenho {

    public static void main(String[] args) throws Exception {
        int n = (args.length > 0) ? Integer.parseInt(args[0]) : 5;
        int rodadasWarmup = (n >= 6) ? 1 : 3;
        int rodadasMedicao = (n >= 6) ? 2 : 10;

        int nucleosDisponiveis = Runtime.getRuntime().availableProcessors();
        int[] configuracoesThreads = {2, 4, 8};

        System.out.printf(" DESEMPENHO PASSEIO DO CAVALO (%dx%d) | Nucleos detectados: %d%n", n, n, nucleosDisponiveis);
        System.out.printf(" Configuracao: %d warm-up(s) e %d rodada(s) de medicao%n", rodadasWarmup, rodadasMedicao);
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Aquecendo a JVM (warm-up)...");

        for (int i = 0; i < rodadasWarmup; i++) {
            PasseioCavaloSequencial.executar(n);
            PasseioCavaloParalelo.executar(n, nucleosDisponiveis);
        }

        List<String> linhasCsv = new ArrayList<>();
        linhasCsv.add("Tabuleiro;Abordagem;Threads;Solucoes;Media_ms;DesvioPadrao_ms;Speedup;Eficiencia_pct");

        double[] temposSeq = new double[rodadasMedicao];
        long solucoesSeq = 0;

        for (int r = 0; r < rodadasMedicao; r++) {
            long inicio = System.nanoTime();
            solucoesSeq = PasseioCavaloSequencial.executar(n);
            temposSeq[r] = (System.nanoTime() - inicio) / 1_000_000.0;
        }

        double mediaSeq = calcularMedia(temposSeq);
        double desvioSeq = calcularDesvioPadrao(temposSeq, mediaSeq);

        System.out.printf("%n%-14s | %-7s | %-9s | %-12s | %-12s | %-8s | %-10s%n",
                "Abordagem", "Threads", "Solucoes", "Media (ms)", "Desvio (ms)", "Speedup", "Eficiencia");
        System.out.println("----------------------------------------------------------------------------------");

        imprimirEAdicionarLinha(linhasCsv, n, "Sequencial", 1, solucoesSeq, mediaSeq, desvioSeq, 1.0, 100.0);

        for (int threads : configuracoesThreads) {
            double[] temposPar = new double[rodadasMedicao];
            long solucoesPar = 0;

            for (int r = 0; r < rodadasMedicao; r++) {
                long inicio = System.nanoTime();
                solucoesPar = PasseioCavaloParalelo.executar(n, threads);
                temposPar[r] = (System.nanoTime() - inicio) / 1_000_000.0;
            }

            if (solucoesPar != solucoesSeq) {
                throw new IllegalStateException("Divergencia na contagem de solucoes!");
            }

            double mediaPar = calcularMedia(temposPar);
            double desvioPar = calcularDesvioPadrao(temposPar, mediaPar);
            double speedup = mediaSeq / mediaPar;
            double eficiencia = (speedup / threads) * 100.0;

            imprimirEAdicionarLinha(linhasCsv, n, "Paralelo", threads, solucoesPar, mediaPar, desvioPar, speedup, eficiencia);
        }

        String nomeArquivo = "resultados_" + n + "x" + n + ".csv";
        salvarCsv(nomeArquivo, linhasCsv);
        System.out.println("\nArquivo '" + nomeArquivo + "' gerado com sucesso.");
    }

    private static void imprimirEAdicionarLinha(List<String> csv, int n, String modo, int threads, long solucoes,
                                                double media, double desvio, double speedup, double eficiencia) {
        System.out.printf(Locale.US, "%-14s | %-7d | %-9d | %-12.2f | %-12.2f | %-7.2fx | %-9.1f%%%n",
                modo, threads, solucoes, media, desvio, speedup, eficiencia);

        csv.add(String.format(Locale.forLanguageTag("pt-BR"), "%dx%d;%s;%d;%d;%.4f;%.4f;%.4f;%.2f",
                n, n, modo, threads, solucoes, media, desvio, speedup, eficiencia));
    }

    private static double calcularMedia(double[] valores) {
        double soma = 0;
        for (double v : valores) {
            soma += v;
        }
        return soma / valores.length;
    }

    private static double calcularDesvioPadrao(double[] valores, double media) {
        double somaQuadrados = 0;
        for (double v : valores) {
            somaQuadrados += Math.pow(v - media, 2);
        }
        return Math.sqrt(somaQuadrados / valores.length);
    }

    private static void salvarCsv(String nomeArquivo, List<String> linhas) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(nomeArquivo))) {
            for (String linha : linhas) {
                writer.println(linha);
            }
        }
    }
}