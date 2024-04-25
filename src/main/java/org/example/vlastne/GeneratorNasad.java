package org.example.vlastne;

import java.util.Random;

public final class GeneratorNasad
{
    private static int NASADA_GENERATORA_NASAD;
    private static boolean PEVNA_STANOVENA_NASADA;
    private static Random RANDOM;

    private static boolean GENERATOR_INICIALIZOVANY = false;

    public GeneratorNasad()
    {
        if (!GeneratorNasad.GENERATOR_INICIALIZOVANY)
        {
            throw new RuntimeException("Generator nasad nie je inicializovany!");
        }

        if (GeneratorNasad.PEVNA_STANOVENA_NASADA)
        {
            System.out.println("Bola vytvorena instancia generatora nasad s pevne stanovenou nasadou: "
                + GeneratorNasad.NASADA_GENERATORA_NASAD + "!");
        }
        else
        {
            System.out.println("Bola vytvorena instancia generatora nasad s nahodnou nasadou!");
        }
    }

    public int nasada()
    {
        return GeneratorNasad.RANDOM.nextInt();
    }

    public Random generator()
    {
        return GeneratorNasad.RANDOM;
    }

    public static void inicializujGeneratorNasad(int nasada, boolean pouziNasadu)
    {
        GeneratorNasad.vypisStatus();
        GeneratorNasad.inicializacia(nasada, pouziNasadu);
    }

    private static void vypisStatus()
    {
        if (GeneratorNasad.GENERATOR_INICIALIZOVANY)
        {
            StringBuilder sprava = new StringBuilder();
            sprava.append("Generator nasad uz bol inicializovany, ");

            if (GeneratorNasad.PEVNA_STANOVENA_NASADA)
            {
                sprava.append("bola pouzita pevna stanovena nasada: ").append(GeneratorNasad.NASADA_GENERATORA_NASAD).append("!");
            }
            else
            {
                sprava.append("bola pouzita nahodna nasada!");
            }

            System.out.println(sprava);
        }
        else
        {
            System.out.println("Prva inicializacia generatora nasad!");
        }
    }

    private static void inicializacia(int nasada, boolean pouziNasadu)
    {
        if (pouziNasadu)
        {
            System.out.println("Bol inicializovany generator nasad s pevne stanovenou nasadou: " + nasada + "!");

            GeneratorNasad.NASADA_GENERATORA_NASAD = nasada;
            GeneratorNasad.PEVNA_STANOVENA_NASADA = true;
            GeneratorNasad.RANDOM = new Random(nasada);
        }
        else
        {
            System.out.println("Bol inicializovany generator nasad s nahodnou nasadou!");

            GeneratorNasad.NASADA_GENERATORA_NASAD = -1;
            GeneratorNasad.PEVNA_STANOVENA_NASADA = false;
            GeneratorNasad.RANDOM = new Random();
        }

        GeneratorNasad.GENERATOR_INICIALIZOVANY = true;
    }
}
