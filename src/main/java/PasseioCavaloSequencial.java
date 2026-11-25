public class PasseioCavaloSequencial extends PasseioCavaloBase {

    public static long executar(int n) {
        long totalSolucoes = 0;

        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                int[][] tabuleiro = new int[n][n];
                tabuleiro[x][y] = 1;
                totalSolucoes += contarPasseios(tabuleiro, n, x, y, 2);
            }
        }
        return totalSolucoes;
    }
}