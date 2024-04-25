package org.example.Vlastne;

public class ObsluzneMiesto
{
    private final TypOkno typOkno;

    private boolean obsadene;
    private boolean prestavka;

    public ObsluzneMiesto(TypOkno typOkno)
    {
        this.typOkno = typOkno;

        this.obsadene = false;
        this.prestavka = false;
    }

    public boolean obsluzneMiestoDostupne()
    {
        if (!this.obsadene && !this.prestavka)
        {
            return true;
        }

        return false;
    }

    public void setObsadene(boolean obsadene)
    {
        if (obsadene)
        {
            if (this.obsadene || this.prestavka)
            {
                throw new RuntimeException("Dane obsluzne miesto nemoze byt obsadene!");
            }
        }
        else
        {
            if (!this.obsadene)
            {
                throw new RuntimeException("Dane obsluzne miesto nemoze byt uvolnene!");
            }
        }

        this.obsadene = obsadene;
    }

    public TypOkno getTypOkna()
    {
        return this.typOkno;
    }
}
