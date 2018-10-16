import fpalgorithm.util.result.FrequentSetContainer;
import util.AssociationRuleBuilder;
import util.result.AssociationRule;

import java.util.Set;

@FunctionalInterface
interface IAdaptor {
    FrequentSetContainer run();
}

public class Runner {
    public static void runApriori() {
    }

    private static void run(IAdaptor adaptor, boolean debug, boolean fileOutput) {
        FrequentSetContainer frequentSets = adaptor.run();
        if (debug)
            outputFrequentSetContainer(frequentSets, fileOutput);
        Set<AssociationRule> rules = AssociationRuleBuilder.build(frequentSets);
    }

    private static void outputFrequentSetContainer(
            FrequentSetContainer container,
            boolean fileOutput
    ) {
        if (!fileOutput) {
            System.out.println(container.toString());
            return;
        }
    }
}
