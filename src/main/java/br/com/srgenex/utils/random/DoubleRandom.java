package br.com.srgenex.utils.random;

import java.util.Random;

public class DoubleRandom {

    public static double getRandom(){
        double rangeMin = 0.0;
        double rangeMax = 100.0;
        Random random = new Random();
        return rangeMin + (rangeMax - rangeMin) * random.nextDouble();
    }

}
