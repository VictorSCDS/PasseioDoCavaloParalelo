public class PasseioCavaloSequencial {

    private static final int N = 5;
    private static final int[] MOV_X = {2, 1, -1, -2, -2, -1, 1, 2};
    private static final int[] MOV_Y = {1, 2, 2, 1, -1, -2, -2, -1};

    public static void main(String[] args) {
        int rodadas = 5;
        executarSequencial();

        System.out.println(" Passeio do Cavalo SEQUENCIAL (" + N + "x" + N + ") ");

        double somaTemposMs = 0;
        long totalSolucoes = 0;

        for (int r = 1; r <= rodadas; r++) {
            long inicio = System.nanoTime();
            totalSolucoes = executarSequencial();
            long fim = System.nanoTime();

            double tempoMs = (fim - inicio) / 1_000_000.0;
            somaTemposMs += tempoMs;

            System.out.printf("Rodada %d -> Soluções: %d | Tempo: %.2f ms%n", r, totalSolucoes, tempoMs);
        }

        double mediaMs = somaTemposMs / rodadas;
        System.out.printf("%nTempo Médio Sequencial (Ts): %.2f ms%n", mediaMs);
    }

    public static long executarSequencial() {
        long totalSolucoes = 0;

        for (int x = 0; x < N; x++) {
            for (int y = 0; y < N; y++) {
                int[][] tabuleiro = new int[N][N];
                tabuleiro[x][y] = 1;
                totalSolucoes += contarPasseios(tabuleiro, x, y, 2);
            }
        }

        return totalSolucoes;
    }
    private static long contarPasseios(int[][] tabuleiro, int x, int y, int passo) {
        if (passo > N * N) {
            return 1;
        }

        long solucoes = 0;

        for (int i = 0; i < 8; i++) {
            int proxX = x + MOV_X[i];
            int proxY = y + MOV_Y[i];

            if (ehValido(tabuleiro, proxX, proxY)) {
                tabuleiro[proxX][proxY] = passo;
                solucoes += contarPasseios(tabuleiro, proxX, proxY, passo + 1);
                tabuleiro[proxX][proxY] = 0;
            }
        }
        return solucoes;
    }
    
    private static boolean ehValido(int[][] tabuleiro, int x, int y) {
        return x >= 0 && x < N && y >= 0 && y < N && tabuleiro[x][y] == 0;
    }
}
