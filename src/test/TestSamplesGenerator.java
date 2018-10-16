package test;

import database.DatabaseOperator;
import util.samplegenerator.SamplesGenerator;

public class TestSamplesGenerator {
    public static void test() {
        DatabaseOperator.emptyDatabase();

        SamplesGenerator.generate(10, 100);
    }
}
