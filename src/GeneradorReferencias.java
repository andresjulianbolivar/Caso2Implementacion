import java.io.FileWriter;
import java.util.ArrayList;

public class GeneradorReferencias 
{
    private int tamañoPagina;
    private int filasImagen;
    private int columnasImagen;
    private int numeroReferencias;
    private int paginasVirtuales;
    private String imagenPath;
    private Imagen imagen;
    private ArrayList<String> referencias = new ArrayList<String>();

    private int[][][][] imagenPaginaDesplazamiento;
    private int[][][] sobelxPaginaDesplazamiento;
    private int[][][] sobelyPaginaDesplazamiento;
    private int[][][][] rtaPaginaDesplazamiento;

    private static final String REFERENCIA_IMAGEN = "Imagen[%d][%d].%s,%d,%d,%s";
    private static final String REFERENCIA_SOBEL_X = "SOBEL_X[%d][%d],%d,%d,%s";
    private static final String REFERENCIA_SOBEL_Y = "SOBEL_Y[%d][%d],%d,%d,%s";
    private static final String REFERENCIA_RTA = "Rta[%d][%d].%s,%d,%d,%s";

    public GeneradorReferencias(int tamañoPagina, String imagenPath)
    {
        this.tamañoPagina = tamañoPagina;
        this.imagenPath = imagenPath;
        imagen = new Imagen(this.imagenPath);
    }

    public void generarReferencias()
    {
        String outputFile = "referencias.in";

        filasImagen = imagen.imagen.length;
        columnasImagen = imagen.imagen[0].length;

        calcularPaginasDesplazamientos();
    
        for (int i=1; i<filasImagen-1; i++)
        {
            for (int j=1; j<columnasImagen-1;j++)
            {
                for (int ki=-1;ki<=1;ki++)
                {
                    for (int kj=-1;kj<=1;kj++)
                    {
                        referencias.add(String.format(REFERENCIA_IMAGEN, i+ki, j+kj, "r", imagenPaginaDesplazamiento[i+ki][j+kj][0][0],imagenPaginaDesplazamiento[i+ki][j+kj][0][1],"R"));
                        referencias.add(String.format(REFERENCIA_IMAGEN, i+ki, j+kj, "g", imagenPaginaDesplazamiento[i+ki][j+kj][1][0],imagenPaginaDesplazamiento[i+ki][j+kj][1][1],"R"));
                        referencias.add(String.format(REFERENCIA_IMAGEN, i+ki, j+kj, "b", imagenPaginaDesplazamiento[i+ki][j+kj][2][0],imagenPaginaDesplazamiento[i+ki][j+kj][2][1],"R"));

                        referencias.add(String.format(REFERENCIA_SOBEL_X, ki+1, kj+1, sobelxPaginaDesplazamiento[ki+1][kj+1][0], sobelxPaginaDesplazamiento[ki+1][kj+1][1],"R"));
                        referencias.add(String.format(REFERENCIA_SOBEL_X, ki+1, kj+1, sobelxPaginaDesplazamiento[ki+1][kj+1][0], sobelxPaginaDesplazamiento[ki+1][kj+1][1],"R"));
                        referencias.add(String.format(REFERENCIA_SOBEL_X, ki+1, kj+1, sobelxPaginaDesplazamiento[ki+1][kj+1][0], sobelxPaginaDesplazamiento[ki+1][kj+1][1],"R"));

                        referencias.add(String.format(REFERENCIA_SOBEL_Y, ki+1, kj+1, sobelyPaginaDesplazamiento[ki+1][kj+1][0], sobelyPaginaDesplazamiento[ki+1][kj+1][1],"R"));
                        referencias.add(String.format(REFERENCIA_SOBEL_Y, ki+1, kj+1, sobelyPaginaDesplazamiento[ki+1][kj+1][0], sobelyPaginaDesplazamiento[ki+1][kj+1][1],"R"));
                        referencias.add(String.format(REFERENCIA_SOBEL_Y, ki+1, kj+1, sobelyPaginaDesplazamiento[ki+1][kj+1][0], sobelyPaginaDesplazamiento[ki+1][kj+1][1],"R"));
                    }
                }

                referencias.add(String.format(REFERENCIA_RTA, i, j, "r", rtaPaginaDesplazamiento[i][j][0][0], rtaPaginaDesplazamiento[i][j][0][1],"W"));
                referencias.add(String.format(REFERENCIA_RTA, i, j, "g", rtaPaginaDesplazamiento[i][j][1][0], rtaPaginaDesplazamiento[i][j][1][1],"W"));
                referencias.add(String.format(REFERENCIA_RTA, i, j, "b", rtaPaginaDesplazamiento[i][j][2][0], rtaPaginaDesplazamiento[i][j][2][1],"W"));
            }
        }

        numeroReferencias = (filasImagen-2)*(columnasImagen-2)*(3*3*9+3);

        try (FileWriter escritor = new FileWriter(outputFile))
        {
            escritor.write("TP="+tamañoPagina+"\n");
            escritor.write("NF="+filasImagen+"\n");
            escritor.write("NC="+columnasImagen+"\n");
            escritor.write("NR="+numeroReferencias+"\n");
            escritor.write("NP="+paginasVirtuales+"\n");
            for (int i=0; i<numeroReferencias;i++)
            {
                escritor.write(referencias.get(i)+"\n");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void calcularPaginasDesplazamientos()
    {
        int pagina = 0;
        int desplazamiento = 0;
        imagenPaginaDesplazamiento = new int[filasImagen][columnasImagen][3][2];
        for (int i=0; i<filasImagen;i++)
        {
            for (int j=0; j<columnasImagen; j++)
            {
                for (int k=0; k<3;k++)
                {
                    if (desplazamiento==tamañoPagina)
                    {
                        desplazamiento = 0;
                        pagina ++;
                    }
                    imagenPaginaDesplazamiento[i][j][k][0] = pagina;
                    imagenPaginaDesplazamiento[i][j][k][1] = desplazamiento;
                    desplazamiento ++;
                }
            }
        }

        sobelxPaginaDesplazamiento = new int[3][3][2];
        for (int i=0;i<3;i++)
        {
            for (int j=0;j<3;j++)
            {
                if (desplazamiento>=tamañoPagina || tamañoPagina-desplazamiento<4) 
                {
                    desplazamiento = 0;
                    pagina ++;
                }
                sobelxPaginaDesplazamiento[i][j][0] = pagina;
                sobelxPaginaDesplazamiento[i][j][1] = desplazamiento;
                desplazamiento += 4;
            }
        }

        sobelyPaginaDesplazamiento = new int[3][3][2];
        for (int i=0;i<3;i++)
        {
            for (int j=0;j<3;j++)
            {
                if (desplazamiento>=tamañoPagina || tamañoPagina-desplazamiento<4)
                {
                    desplazamiento = 0;
                    pagina ++;
                }
                sobelyPaginaDesplazamiento[i][j][0] = pagina;
                sobelyPaginaDesplazamiento[i][j][1] = desplazamiento;
                desplazamiento +=4;
            }
        }

        rtaPaginaDesplazamiento = new int[filasImagen][columnasImagen][3][2];
        for (int i=0; i<filasImagen;i++)
        {
            for (int j=0; j<columnasImagen; j++)
            {
                for (int k=0; k<3;k++)
                {
                    if (desplazamiento==tamañoPagina)
                    {
                        desplazamiento = 0;
                        pagina ++;
                    }
                    rtaPaginaDesplazamiento[i][j][k][0] = pagina;
                    rtaPaginaDesplazamiento[i][j][k][1] = desplazamiento;
                    desplazamiento ++;
                }
            }
        }

        paginasVirtuales = pagina+1;
    }
}
