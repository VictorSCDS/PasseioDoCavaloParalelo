import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PasseioCavaloParalelo extends PasseioCavaloBase {

    public static long executar(int n, int numThreads) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Callable<Long>> tarefas = new ArrayList<>();

        try {
            for (int x = 0; x < n; x++) {
                for (int y = 0; y < n; y++) {
                    final int inicioX = x;
                    final int inicioY = y;

                    tarefas.add(() -> {
                        int[][] tabuleiroLocal = new int[n][n];
                        tabuleiroLocal[inicioX][inicioY] = 1;
                        return contarPasseios(tabuleiroLocal, n, inicioX, inicioY, 2);
                    });
                }
            }
            List<Future<Long>> resultados = executor.invokeAll(tarefas);
            long totalSolucoes = 0;

            for (Future<Long> futuro : resultados) {
                totalSolucoes += futuro.get();
            }
            return totalSolucoes;
        } finally {
            executor.shutdown();
        }
    }
}