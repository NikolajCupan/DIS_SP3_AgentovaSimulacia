package org.example.Vlastne.Generatory;

import OSPRNG.UniformContinuousRNG;

public class GenerovanieVyberFrontu
{
    private final UniformContinuousRNG rngVyberFrontu;

    public GenerovanieVyberFrontu(GeneratorNasad generatorNasad)
    {
        this.rngVyberFrontu = new UniformContinuousRNG(0.0, 1.0, generatorNasad.generator());
    }

    public int getIndexFrontu(int pocetFrontov)
    {
        double sample = this.rngVyberFrontu.sample();
        double dlzkaBloku = 1.0 / pocetFrontov;

        for (int i = 0; i < pocetFrontov; i++)
        {
            if (sample < dlzkaBloku * (i + 1))
            {
                return i;
            }
        }

        throw new RuntimeException("Chyba pri generovani indexu frontu!");
    }
}
