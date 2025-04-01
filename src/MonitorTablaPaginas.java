import java.util.ArrayList;

public class MonitorTablaPaginas 
{
    private int marcos;
    private int paginas;
    private int marcosRam = 0;
    private ArrayList<int[]> tablaDePaginas = new ArrayList<int[]>();
    private int misses = 0;
    private int hits = 0;

    private boolean terminar = false;

    private int clase1=0;
    private int clase2=0;
    private int clase3=0;
    private int clase4=0;

    public MonitorTablaPaginas(int marcos, int paginas)
    {
        this.marcos = marcos;
        this.paginas = paginas;

        for (int i = 0; i<this.paginas; i++)
        {
            int[] marco = new int[3];
            marco[0] = -1;
            marco[1] = 0;
            marco[2] = 0;
            tablaDePaginas.add(marco);
        }
    }

    public synchronized void actualizarEstados()
    {
        for (int i = 0; i<tablaDePaginas.size();i++)
        {
            if (tablaDePaginas.get(i)[0] != -1)
            {
                tablaDePaginas.get(i)[1] = 0;
            }
        }

        actualizarClases();
    }

    public synchronized int sacarPagina()
    {
        boolean sacada = false;

        if (clase1 > 0)
        {
            for (int i=0;i<tablaDePaginas.size() && !sacada; i++)
            {
                if (tablaDePaginas.get(i)[0] != -1)
                {
                    if (tablaDePaginas.get(i)[1] == 0 && tablaDePaginas.get(i)[2] == 0)
                    {
                        int realPagina = tablaDePaginas.get(i)[0];
                        tablaDePaginas.get(i)[0] = -1;
                        sacada=true;
                        return realPagina;
                    }
                }
            }
        }
        else if (clase2 > 0)
        {
            for (int i=0;i<tablaDePaginas.size() && !sacada; i++)
            {
                if (tablaDePaginas.get(i)[0] != -1)
                {
                    if (tablaDePaginas.get(i)[1] == 0 && tablaDePaginas.get(i)[2] == 1)
                    {
                        int realPagina = tablaDePaginas.get(i)[0];
                        tablaDePaginas.get(i)[0] = -1;
                        sacada=true;
                        return realPagina;
                    }
                }
            }
        }
        else if (clase3 > 0)
        {
            for (int i=0;i<tablaDePaginas.size() && !sacada; i++)
            {
                if (tablaDePaginas.get(i)[0] != -1)
                {
                    if (tablaDePaginas.get(i)[1] == 1 && tablaDePaginas.get(i)[2] == 0)
                    {
                        int realPagina = tablaDePaginas.get(i)[0];
                        tablaDePaginas.get(i)[0] = -1;
                        sacada=true;
                        return realPagina;
                    }
                }
            }
        }
        else if (clase4 > 0)
        {
            for (int i=0;i<tablaDePaginas.size() && !sacada; i++)
            {
                if (tablaDePaginas.get(i)[0] != -1)
                {
                    if (tablaDePaginas.get(i)[1] == 1 && tablaDePaginas.get(i)[2] == 1)
                    {
                        int realPagina = tablaDePaginas.get(i)[0];
                        tablaDePaginas.get(i)[0] = -1;
                        sacada=true;
                        return realPagina;
                    }
                }
            }
        }
        return 0;
    }

    public synchronized void procesarReferencia(int pagina, String uso)
    {
        int[] marco = tablaDePaginas.get(pagina);
        if (marco[0] == -1)
        {
            misses ++;
            if (marcosRam == marcos)
            {
                int paginaRealDestino = sacarPagina();
                tablaDePaginas.get(pagina)[0]=paginaRealDestino;
                if (uso.equals("R"))
                {
                    tablaDePaginas.get(pagina)[1] = 1;
                    tablaDePaginas.get(pagina)[2] = 0;
                }
                else
                {
                    tablaDePaginas.get(pagina)[1] = 0;
                    tablaDePaginas.get(pagina)[2] = 1;
                }
            }
            else
            {
                tablaDePaginas.get(pagina)[0] = marcosRam;
                if (uso.equals("R"))
                {
                    tablaDePaginas.get(pagina)[1] = 1;
                    tablaDePaginas.get(pagina)[2] = 0;
                }
                else
                {
                    tablaDePaginas.get(pagina)[1] = 0;
                    tablaDePaginas.get(pagina)[2] = 1;
                }
                marcosRam ++;
            }
        }
        else
        {
            hits ++;
            if (uso.equals("R"))
            {
                tablaDePaginas.get(pagina)[1] = 1;
                tablaDePaginas.get(pagina)[2] = 0;
            }
            else
            {
                tablaDePaginas.get(pagina)[1] = 0;
                tablaDePaginas.get(pagina)[2] = 1;
            }
        }

        actualizarClases();
    }

    public int getMisses()
    {
        return misses;
    }

    public int getHits()
    {
        return hits;
    }

    public synchronized void actualizarClases()
    {
        clase1=0;
        clase2=0;
        clase3=0;
        clase4=0;
        for (int i = 0; i < tablaDePaginas.size(); i++)
        {
            if (tablaDePaginas.get(i)[0] != -1)
            {
                if (tablaDePaginas.get(i)[1] == 0 && tablaDePaginas.get(i)[2] == 0)
                {
                    clase1 ++;
                }
                else if (tablaDePaginas.get(i)[1] == 0 && tablaDePaginas.get(i)[2] == 1)
                {
                    clase2 ++;
                }
                else if (tablaDePaginas.get(i)[1] == 1 && tablaDePaginas.get(i)[2] == 0)
                {
                    clase3 ++;
                }
                else if (tablaDePaginas.get(i)[1] == 1 && tablaDePaginas.get(i)[2] == 1)
                {
                    clase4 ++;
                }
            }
        }
    }

    public synchronized boolean getTerminar()
    {
        return terminar;
    }

    public synchronized void setTerminar()
    {
        terminar=true;
    }
}
