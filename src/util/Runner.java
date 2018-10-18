package util;

import fpalgorithm.AssociationRuleBuilder;
import fpalgorithm.fsetgenerator.util.result.FrequentSetContainer;
import util.adaptor.FpGrowthProjectAdaptor;
import util.adaptor.apriori.AprioriProjectAdaptor;
import fpalgorithm.result.AssociationRule;

import java.io.File;
import java.io.FileWriter;
import java.util.Set;

@FunctionalInterface
interface IAdaptor {
    FrequentSetContainer run();
}

public class Runner {

    public static void runApriori(boolean outputFrequentSets, boolean fileOutput) {
        run(AprioriProjectAdaptor::runApriori, outputFrequentSets, fileOutput);
    }

    public static void runFpGrowth(boolean outputFrequentSets, boolean fileOutput) {
        run(FpGrowthProjectAdaptor::runFpGrowth, outputFrequentSets, fileOutput);
    }

    private static void run(IAdaptor adaptor, boolean debug, boolean fileOutput) {
        FrequentSetContainer frequentSets = adaptor.run();
        if (debug)
            output(frequentSets, fileOutput, "test/frequent_set.txt");
        Set<AssociationRule> rules = AssociationRuleBuilder.build(frequentSets, GlobalInfo.confidence_threshold);
        output(rules, fileOutput, "rules.txt");

    }

    private static void output(
            Object container,
            boolean fileOutput,
            String name
    ) {
        if (!fileOutput) {
            if (container instanceof FrequentSetContainer)
                System.out.println(container.toString());
            else {
                for (AssociationRule rule : (Set<AssociationRule>) container)
                    System.out.println(rule.toString());
                System.out.println("size: " + ((Set<AssociationRule>) container).size() + ".");
            }
            return;
        }

        System.out.println("output start...");

        File file = new File("log/" + name);
        if (file.exists())
            file.delete();

        try {
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file.getName(), true);
            if (container instanceof FrequentSetContainer)
                fileWriter.write(container.toString());
            else {
                for (AssociationRule rule : (Set<AssociationRule>) container)
                    fileWriter.append(rule.toString());
                fileWriter.append("size: " + ((Set<AssociationRule>) container).size() + ".");
            }
            fileWriter.close();

            System.out.println("output finished.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
