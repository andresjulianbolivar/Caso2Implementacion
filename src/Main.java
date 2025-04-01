import java.util.ArrayList;
import java.util.Scanner;

public class Main 
{
    public static void main(String[] args) throws Exception 
    {
        boolean terminar = false;
        while(!terminar)
        {
            System.out.println("Elija una opción para ejecutar:");
            System.out.println("- 1) Opción 1 (generar referencias).");
            System.out.println("- 2) Opción 2 (calcular datos buscados).");
            System.out.println("- 3) Salir.");

            Scanner scanner = new Scanner(System.in);

            String opcion  = scanner.nextLine();

            if (opcion.equals("1"))
            {
                System.out.println("Ingrese el tamaño de página (TP) en bytes:");
                String tp = scanner.nextLine();
                int tamañoPagina = Integer.parseInt(tp);

                System.out.println("Ingrese el nombre del archivo con la imagen a la que se le va a aplicar el filtro de Sobel:");
                String imagenFile = scanner.nextLine();

                GeneradorReferencias generadorReferencias = new GeneradorReferencias(tamañoPagina,imagenFile);
                generadorReferencias.generarReferencias();
            }
            else if(opcion.equals("2"))
            {
                System.out.println("Ingrese el número de marcos de página:");
                String mp = scanner.nextLine();
                int marcos = Integer.parseInt(mp);

                System.out.println("Ingrese el nombre del archivo con las refrencias (por defecto se guarda como \"referencias.in\"):");
                String referenciasFile = scanner.nextLine();

                ProcesadorInformacion procesadorInformacion = new ProcesadorInformacion(referenciasFile);

                int paginas = procesadorInformacion.extraerPaginas();
                ArrayList<String> referencias = procesadorInformacion.extraerReferencias();

                MonitorTablaPaginas monitorTablaPaginas = new MonitorTablaPaginas(marcos, paginas);

                Actualizador.inicializarMonitor(monitorTablaPaginas);
                LectorReferencias.inicializarMonitor(monitorTablaPaginas);

                Actualizador actualizador = new Actualizador();
                LectorReferencias lectorReferencias = new LectorReferencias(referencias);

                actualizador.start();
                lectorReferencias.start();

                lectorReferencias.join();
            }
            else if(opcion.equals("3"))
            {
                terminar =true;
                scanner.close();
            }
            else
            {
                System.out.println("Elija una opción válida.");
            }
        }
    }
}
