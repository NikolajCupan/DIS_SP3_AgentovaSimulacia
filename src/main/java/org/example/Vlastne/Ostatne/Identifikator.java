package org.example.Vlastne.Ostatne;

public class Identifikator
{
    private static long curID = 0;

    public static long getID()
    {
        return Identifikator.curID++;
    }

    public static void resetujID()
    {
        Identifikator.curID = 0;
    }
}
