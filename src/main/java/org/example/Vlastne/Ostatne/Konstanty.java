package org.example.Vlastne.Ostatne;

public class Konstanty
{
    // Trvanie simulacie
    public static final int HODINA_OTVORENIA = 9;
    public static final int HODINA_ZATVORENIA = 17;

    private static final int ZACIATOK_CAS_SEKUNDY = Konstanty.HODINA_OTVORENIA * 60 * 60;
    private static final int KONIEC_CAS_SEKUNDY = Konstanty.HODINA_ZATVORENIA * 60 * 60;
    public static final int TRVANIE_CAS_SEKUNDY = Konstanty.KONIEC_CAS_SEKUNDY - Konstanty.ZACIATOK_CAS_SEKUNDY;


    // Prestavka
    private static final double HODINA_ZACIATOK_PRESTAVKA = 12;
    private static final double HODINA_KONIEC_PRESTAVKA = 12.50;

    public static final double ZACIATOK_PRESTAVKA_OD_OTVORENIA_CAS_SEKUNDY =
        Konstanty.HODINA_ZACIATOK_PRESTAVKA * 60 * 60 - ZACIATOK_CAS_SEKUNDY;
    public static final double KONIEC_PRESTAVKA_OD_OTVORENIA_CAS_SEKUNDY =
        Konstanty.HODINA_KONIEC_PRESTAVKA * 60 * 60 - ZACIATOK_CAS_SEKUNDY;


    // Generatory
    private static final int POCET_BEZNI_ZA_HODINU = 15;
    private static final int POCET_ZMLUVNI_ZA_HODINU = 5;
    private static final int POCET_ONLINE_ZA_HODINU = 10;
    private static final double ZVYSENY_TOK = 0.30;

    public static final double PRIEMER_BEZNI_ZAKAZNICI = 60.0 / Konstanty.POCET_BEZNI_ZA_HODINU * 60.0;
    public static final double PRIEMER_ZMLUVNI_ZAKAZNICI = 60.0 / Konstanty.POCET_ZMLUVNI_ZA_HODINU * 60.0;
    public static final double PRIEMER_ONLINE_ZAKAZNICI = 60.0 / Konstanty.POCET_ONLINE_ZA_HODINU * 60.0;

    public static final double ZVYSENY_PRIEMER_BEZNI_ZAKAZNICI = 60.0
        / (Konstanty.POCET_BEZNI_ZA_HODINU + Konstanty.POCET_BEZNI_ZA_HODINU * ZVYSENY_TOK) * 60.0;
    public static final double ZVYSENY_PRIEMER_ZMLUVNI_ZAKAZNICI = 60.0
            / (Konstanty.POCET_ZMLUVNI_ZA_HODINU + Konstanty.POCET_ZMLUVNI_ZA_HODINU * ZVYSENY_TOK) * 60.0;
    public static final double ZVYSENY_PRIEMER_ONLINE_ZAKAZNICI = 60.0
            / (Konstanty.POCET_ONLINE_ZA_HODINU + Konstanty.POCET_ONLINE_ZA_HODINU * ZVYSENY_TOK) * 60.0;


    // Ostatne
    public static final int KAPACITA_FRONT_OBSLUZNE_MIESTA = 9;


    // Debug
    public static final boolean DEBUG_VYPISY = false;
    public static final boolean DEBUG_VYPISY_ZAKAZNIK = false;
}
