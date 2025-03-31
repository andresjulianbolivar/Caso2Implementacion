public class Marco 
{
    private int paginaReal;
    private int lectura;
    private int escritura;

    public Marco()
    {
        this.paginaReal = -1;
        this.lectura = -1;
        this.escritura = -1;
    }

    public int getPaginaReal()
    {
        return paginaReal;
    }

    public int getLectura()
    {
        return lectura;
    }

    public int getEscritura()
    {
        return escritura;
    }

    public void setLectura(int bit)
    {
        lectura = bit;
    }

    public void setEscritura(int bit)
    {
        escritura =bit;
    }

    public void setPaginaReal(int pagina)
    {
        paginaReal = pagina;
    }
}
