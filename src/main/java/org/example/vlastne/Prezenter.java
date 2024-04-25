package org.example.vlastne;

import java.text.DecimalFormat;

public class Prezenter
{
    public static DecimalFormat FORMATOVAC = new DecimalFormat("#.##");

    public static String naformatujCas(double casOdZaciatku)
    {
        if (casOdZaciatku < 0)
        {
            return "n/a";
        }

        int pocetHodin = (int)Math.floor(casOdZaciatku / 3600);
        int pocetMinut = (int)Math.floor((casOdZaciatku - pocetHodin * 3600) / 60);
        int pocetSekund = (int)Math.round(casOdZaciatku - pocetHodin * 3600 - pocetMinut * 60);

        final int hodinaOtvorenia = Konstanty.HODINA_OTVORENIA;
        pocetHodin += hodinaOtvorenia;

        return Prezenter.casNaString(pocetHodin) + ":" + Prezenter.casNaString(pocetMinut) + ":" + Prezenter.casNaString(pocetSekund);
    }

    private static String casNaString(int cas)
    {
        String casString = String.valueOf(cas);

        if (casString.length() == 1)
        {
            return '0' + casString;
        }
        else
        {
            return casString;
        }
    }

    private static String zaokruhli(double cislo)
    {
        if (cislo < 0)
        {
            return "n/a";
        }

        return Prezenter.FORMATOVAC.format(cislo);
    }
}
