public class TimeUtils {
    /**
     * Declara um método nativo que será implementado em C++.
     * Este método buscará os tempos de processo diretamente do sistema operacional.
     * @return um array de long com 2 posições: [0] = user time, [1] = sys time
     * em intervalos de 100 nanossegundos.
     */
    public static native long[] getProcessTimes();

    // Bloco estático para carregar nossa biblioteca nativa (a DLL que vamos criar)
    static {
        System.loadLibrary("TimeUtils");
    }
}