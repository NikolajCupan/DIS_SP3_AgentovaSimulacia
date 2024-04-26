package org.example.Vlastne;

public class ObsluzneMiesto
{
    private final TypOkno typOkno;

    private boolean obsadene;

    public ObsluzneMiesto(TypOkno typOkno)
    {
        this.typOkno = typOkno;

        this.obsadene = false;
    }

    public boolean obsluzneMiestoDostupne()
    {
        if (!this.obsadene)
        {
            return true;
        }

        return false;
    }

    public void setObsadene(boolean obsadene)
    {
        if (obsadene && this.obsadene)
        {
            throw new RuntimeException("Dane obsluzne miesto je uz obsadene!");
        }
        if (!obsadene && !this.obsadene)
        {
            throw new RuntimeException("Dane obsluzne miesto je uz uvolnene!");
        }

        this.obsadene = obsadene;
    }

    public TypOkno getTypOkna()
    {
        return this.typOkno;
    }
}
