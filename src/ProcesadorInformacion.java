import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class ProcesadorInformacion 
{
    private String referenciasFile;

    public ProcesadorInformacion(String referenciasFile)
    {
        this.referenciasFile = referenciasFile;
    }

    public int extraerPaginas()
    {
        try (BufferedReader br = new BufferedReader(new FileReader(referenciasFile)))
        {
            String linea = "";
            for (int i = 0; i<5; i++)
            {
                linea = br.readLine();
            }
            String[] np = linea.split("=");
            int numeroPaginas = Integer.parseInt(np[1]);
            return numeroPaginas;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return 0;
        }
    }

    public ArrayList<String> extraerReferencias()
    {
        try (BufferedReader br = new BufferedReader(new FileReader(referenciasFile)))
        {
            String linea = "";
            for (int i = 0; i<5; i++)
            {
                linea = br.readLine();
            }
            
            ArrayList<String> referencias = new ArrayList<String>();
            while ((linea = br.readLine()) != null)
            {
                referencias.add(linea);
            }
            return referencias;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
