package homework_adaptor;

import fpalgorithm.result.AssociationRule;
import homework_adaptor.util.UtilFunc;

import java.util.Arrays;
import java.util.Comparator;

public class Third {
    public static void resolve() {
        AssociationRule[] rules = UtilFunc.getRules();
        Arrays.sort(rules, Comparator.comparing(AssociationRule::getSupport));
        StringBuilder builder = new StringBuilder();
        int t = 20, len = rules.length;
        double ts = rules[len - 21].getSupport();
        builder.append("target support: ").append(ts).append("\n");
        ++t;
        while (rules[len - t].getSupport() == ts) ++t;
        for (int i = 1; i < t; ++i)
            builder.append(rules[len - i].toString()).append("\n");
        
        UtilFunc.outputStringToFile(builder.toString(), "q3.txt");
    }
}
