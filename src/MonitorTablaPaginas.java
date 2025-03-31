import java.util.HashMap;

public class MonitorTablaPaginas 
{
    private int marcos;
    private int paginas;
    private int marcosRam = 0;
    private HashMap<Integer,Marco> tablaDePaginas = new HashMap<Integer,Marco>();
    private int misses = 0;
    private int hits = 0;

    private boolean terminar = false;

    private int clase1;
    private int clase2;
    private int clase3;
    private int clase4;

    public MonitorTablaPaginas(int marcos, int paginas)
    {
        this.marcos = marcos;
        this.paginas = paginas;

        for (int i = 0; i<this.paginas; i++)
        {
            tablaDePaginas.put(i, new Marco());
        }
    }

    public synchronized void actualizarEstados()
    {
        for (Marco marco : tablaDePaginas.values())
        {
            if (marco.getPaginaReal() != -1)
            {
                marco.setLectura(0);
            }
        }

        actualizarClases();
    }

    public int sacarPagina()
    {
        boolean sacada = false;

        if (clase1 > 0)
        {
            for (Marco marco : tablaDePaginas.values())
            {
                if (!(sacada))
                {
                    int lectura = marco.getLectura();
                    int escritura = marco.getEscritura();

                    if (lectura == 0 && escritura == 0)
                    {
                        int paginaActual = marco.getPaginaReal();
                        marco.setPaginaReal(-1);
                        sacada = true;
                        return paginaActual;
                    }
                }
            }
        }
        else if (clase2 > 0)
        {
            for (Marco marco : tablaDePaginas.values())
            {
                if (!(sacada))
                {
                    int lectura = marco.getLectura();
                    int escritura = marco.getEscritura();

                    if (lectura == 0 && escritura == 1)
                    {
                        int paginaActual = marco.getPaginaReal();
                        marco.setPaginaReal(-1);
                        sacada = true;
                        return paginaActual;
                    }
                }
            }
        }
        else if (clase3 > 0)
        {
            for (Marco marco : tablaDePaginas.values())
            {
                if (!(sacada))
                {
                    int lectura = marco.getLectura();
                    int escritura = marco.getEscritura();

                    if (lectura == 1 && escritura == 0)
                    {
                        int paginaActual = marco.getPaginaReal();
                        marco.setPaginaReal(-1);
                        sacada = true;
                        return paginaActual;
                    }
                }
            }
        }
        else if (clase4 > 0)
        {
            for (Marco marco : tablaDePaginas.values())
            {
                if (!(sacada))
                {
                    int lectura = marco.getLectura();
                    int escritura = marco.getEscritura();

                    if (lectura == 1 && escritura == 1)
                    {
                        int paginaActual = marco.getPaginaReal();
                        marco.setPaginaReal(-1);
                        sacada = true;
                        return paginaActual;
                    }
                }
            }
        }
        return 0;
    }

    public synchronized void procesarReferencia(int pagina, String uso)
    {
        Marco marco = tablaDePaginas.get(pagina);
        if (marco.getPaginaReal() == -1)
        {
            misses ++;
            if (marcosRam == marcos)
            {
                int paginaRealDestino = sacarPagina();
                marco.setPaginaReal(paginaRealDestino);
                if (uso.equals("R"))
                {
                    marco.setLectura(1);
                }
                else
                {
                    marco.setLectura(1);
                    marco.setEscritura(1);
                }
            }
            else
            {
                marco.setPaginaReal(marcosRam);
                if (uso.equals("R"))
                {
                    marco.setLectura(1);
                }
                else
                {
                    marco.setLectura(1);
                    marco.setEscritura(1);
                }
                marcosRam ++;
            }
        }
        else
        {
            hits ++;
            if (uso.equals("R"))
            {
                marco.setLectura(1);
            }
            else
            {
                marco.setLectura(1);
                marco.setEscritura(1);
            }
        }

        actualizarClases();
    }

    public synchronized int getMisses()
    {
        return misses;
    }

    public synchronized int getHits()
    {
        return hits;
    }

    public void actualizarClases()
    {
        for (Marco marco : tablaDePaginas.values())
        {
            if (marco.getPaginaReal() != -1)
            {
                int lectura = marco.getLectura();
                int escritura = marco.getEscritura();

                if (lectura == 0 && escritura == 0)
                {
                    clase1 ++;
                }
                else if (lectura == 0 && escritura == 1)
                {
                    clase2 ++;
                }
                else if (lectura == 1 && escritura == 0)
                {
                    clase3 ++;
                }
                else
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
