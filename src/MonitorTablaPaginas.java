import java.util.ArrayList;

public class MonitorTablaPaginas 
{
    private int marcos;
    private int paginas;
    private int marcosRam = 0;
    private ArrayList<int[]> tablaDePaginas = new ArrayList<int[]>();
    private int[] memoriaRam;
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

        memoriaRam = new int[marcos];
        for (int i=0; i<memoriaRam.length;i++)
        {
            memoriaRam[i] = -1;
        }

        for (int i = 0; i<this.paginas; i++)
        {
            int[] marco = new int[3];
            marco[0] = -1;
            tablaDePaginas.add(marco);
        }
    }

    public synchronized boolean actualizarEstados()
    {
        for (int i = 0; i<memoriaRam.length;i++)
        {
            int paginaVirtual = memoriaRam[i];
            if (paginaVirtual!=-1)
            {
                if (tablaDePaginas.get(paginaVirtual)[1]==1)
                {
                    if (tablaDePaginas.get(paginaVirtual)[2]==1)
                    {
                        clase4--;
                        clase2++;
                    }
                    else
                    {
                        clase3--;
                        clase1++;
                    }
                    tablaDePaginas.get(paginaVirtual)[1] = 0;
                }
            }
        }
        return terminar;
    }

    public Integer sacarPagina()
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
                        clase1--;
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
                        clase2--;
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
                        clase3--;
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
                        clase4--;
                        tablaDePaginas.get(i)[0] = -1;
                        sacada=true;
                        return realPagina;
                    }
                }
            }
        }
        return -1;
    }

    public synchronized void procesarReferencia(int pagina, String uso)
    {
        if (tablaDePaginas.get(pagina)[0] == -1)
        {
            misses ++;
            if (marcosRam == marcos)
            {
                int paginaRealDestino = sacarPagina();
                tablaDePaginas.get(pagina)[0]=paginaRealDestino;
                memoriaRam[paginaRealDestino] = pagina;
                if (uso.equals("R"))
                {
                    clase3++;
                    tablaDePaginas.get(pagina)[1] = 1;
                    tablaDePaginas.get(pagina)[2] = 0;
                }
                else
                {
                    clase2++;
                    tablaDePaginas.get(pagina)[1] = 0;
                    tablaDePaginas.get(pagina)[2] = 1;
                }
            }
            else
            {
                tablaDePaginas.get(pagina)[0] = marcosRam;
                memoriaRam[marcosRam] = pagina;
                if (uso.equals("R"))
                {
                    clase3++;
                    tablaDePaginas.get(pagina)[1] = 1;
                    tablaDePaginas.get(pagina)[2] = 0;
                }
                else
                {
                    clase2++;
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
                if (tablaDePaginas.get(pagina)[1]==0 && tablaDePaginas.get(pagina)[2]==0)
                {
                    clase1--;
                    clase3++;
                }
                else if (tablaDePaginas.get(pagina)[1]==0 && tablaDePaginas.get(pagina)[2]==1)
                {
                    clase2--;
                    clase3++;
                }
                else if (tablaDePaginas.get(pagina)[1]==1 && tablaDePaginas.get(pagina)[2]==1)
                {
                    clase4--;
                    clase3++;
                }
                tablaDePaginas.get(pagina)[1] = 1;
                tablaDePaginas.get(pagina)[2] = 0;
            }
            else
            {
                if (tablaDePaginas.get(pagina)[1]==0 && tablaDePaginas.get(pagina)[2]==0)
                {
                    clase1--;
                    clase2++;
                }
                else if (tablaDePaginas.get(pagina)[1]==1 && tablaDePaginas.get(pagina)[2]==0)
                {
                    clase3--;
                    clase2++;
                }
                else if (tablaDePaginas.get(pagina)[1]==1 && tablaDePaginas.get(pagina)[2]==1)
                {
                    clase4--;
                    clase2++;
                }
                tablaDePaginas.get(pagina)[1] = 0;
                tablaDePaginas.get(pagina)[2] = 1;
            }
        }
    }

    public int getMisses()
    {
        return misses;
    }

    public int getHits()
    {
        return hits;
    }

    public synchronized void setTerminar()
    {
        terminar=true;
    }
}
