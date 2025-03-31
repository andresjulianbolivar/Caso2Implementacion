public class Actualizador extends Thread
{
    private static MonitorTablaPaginas monitor;

    public static void inicializarMonitor(MonitorTablaPaginas nMonitor)
    {
        monitor = nMonitor;
    }

    public void run()
    {
        while(!monitor.getTerminar())
        {
            try 
            {
                Thread.sleep(1);
            } 
            catch (InterruptedException e) 
            {
                e.printStackTrace();
            }

            monitor.actualizarEstados();
        }
    }
}
