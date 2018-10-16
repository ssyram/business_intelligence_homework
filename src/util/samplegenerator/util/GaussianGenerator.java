package util.samplegenerator.util;

import java.util.Random;

public class GaussianGenerator {
    private Random r = new Random();
    private double avg;
    private double stdvar;
    private double max, min;

    public GaussianGenerator(double average, double standard_variance, double min, double max) {
        avg = average;
        stdvar = standard_variance;
        this.max = max;
        this.min = min;
    }

    public double getNext() {
        double d;
        while ((d = Math.sqrt(stdvar) * r.nextGaussian() + avg) > max || d <= min);

        return d;
    }

    public int getNextInt() {
        return (int) getNext();
    }
}
