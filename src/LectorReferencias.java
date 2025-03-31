import java.util.ArrayList;

public class LectorReferencias extends Thread
{
    private ArrayList<String> referencias;
    private static MonitorTablaPaginas monitor;

    public LectorReferencias(ArrayList<String> referencias)
    {
        this.referencias=referencias;
    }

    public static void inicializarMonitor(MonitorTablaPaginas nMonitor)
    {
        monitor = nMonitor;
    }

    public int obtenerPagina(String referencia)
    {
        String[] informacion = referencia.split(",");
        int pagina = Integer.parseInt(informacion[1]);
        return pagina;
    }

    public String obtenerUso(String referencia)
    {
        String[] informacion = referencia.split(",");
        return informacion[3];
    }

    public void run()
    {
        for (int i=0;i<referencias.size();i++)
        {
            String referencia = referencias.get(i);
            int pagina = obtenerPagina(referencia);
            String uso = obtenerUso(referencia);
            monitor.procesarReferencia(pagina,uso);
            if ((i+1)%10000 == 0)
            {
                try 
                {
                    Thread.sleep(1);
                } 
                catch (InterruptedException e) 
                {
                    e.printStackTrace();
                }
            }
        }

        monitor.setTerminar();

        int misses = monitor.getMisses();
        int hits = monitor.getHits();
        System.out.println("Misses: "+misses);
        System.out.println("Hits: "+hits);
        System.out.println(String.format("Porcentaje de hits: %.2f%%", (double) hits / (hits + misses) * 100));
        System.out.println(String.format("Tiempo de lectura para datos en RAM (ns): %d", hits*50));
        System.out.println(String.format("Tiempo de lectura para datos en SWAP (ms): %d", misses*10));
        System.out.println(String.format("Tiempo total de lectura (ms): %.2f", (double) hits*50/1000000 + misses*10));
    }
}
