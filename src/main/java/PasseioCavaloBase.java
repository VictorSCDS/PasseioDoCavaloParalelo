public abstract class PasseioCavaloBase {

    protected static final int[] MOV_X = {2, 1, -1, -2, -2, -1, 1, 2};
    protected static final int[] MOV_Y = {1, 2, 2, 1, -1, -2, -2, -1};
    protected static long contarPasseios(int[][] tabuleiro, int n, int x, int y, int passo) {
        if (passo > n * n) {
            return 1;
        }
        long solucoes = 0;
        for (int i = 0; i < 8; i++) {
            int proxX = x + MOV_X[i];
            int proxY = y + MOV_Y[i];

            if (ehValido(tabuleiro, n, proxX, proxY)) {
                tabuleiro[proxX][proxY] = passo;
                solucoes += contarPasseios(tabuleiro, n, proxX, proxY, passo + 1);
                tabuleiro[proxX][proxY] = 0;
            }
        }
        return solucoes;
    }
    private static boolean ehValido(int[][] tabuleiro, int n, int x, int y) {
        return x >= 0 && x < n && y >= 0 && y < n && tabuleiro[x][y] == 0;
    }
}