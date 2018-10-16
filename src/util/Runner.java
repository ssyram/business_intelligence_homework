package util;

import fpalgorithm.util.result.FrequentSetContainer;
import util.adaptor.FpGrowthProjectAdaptor;
import util.adaptor.apriori.AprioriProjectAdaptor;
import util.result.AssociationRule;

import java.io.File;
import java.io.FileWriter;
import java.util.Set;

@FunctionalInterface
interface IAdaptor {
    FrequentSetContainer run();
}

public class Runner {

    public static void runApriori(boolean debug, boolean fileOutput) {
        run(AprioriProjectAdaptor::runApriori, debug, fileOutput);
    }

    public static void runFpGrowth(boolean debug, boolean fileOutput) {
        run(FpGrowthProjectAdaptor::runFpGrowth, debug, fileOutput);
    }

    private static void run(IAdaptor adaptor, boolean debug, boolean fileOutput) {
        FrequentSetContainer frequentSets = adaptor.run();
        if (debug)
            output(frequentSets, fileOutput, "test/frequent_set.txt");
        Set<AssociationRule> rules = AssociationRuleBuilder.build(frequentSets);
        output(rules, fileOutput, "rules.txt");
    }

    private static void output(
            Object container,
            boolean fileOutput,
            String name
    ) {
        if (!fileOutput) {
            System.out.println(container.toString());
            return;
        }

        System.out.println("output start...");

        File file = new File("log/" + name);
        if (file.exists())
            file.delete();

        try {
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file.getName(), true);
            fileWriter.write(container.toString());
            fileWriter.close();

            System.out.println("output finished.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
