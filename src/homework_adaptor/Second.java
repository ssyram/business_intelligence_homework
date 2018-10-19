package homework_adaptor;

import fpalgorithm.result.AssociationRule;
import homework_adaptor.util.UtilFunc;

import java.util.Arrays;
import java.util.Comparator;

public class Second {
    public static void resolve() {
        AssociationRule[] rules = UtilFunc.getRules();
        Arrays.sort(rules, Comparator.comparing(AssociationRule::getConfidence));
        StringBuilder builder = new StringBuilder();
        double tc = -1;
        int len = rules.length - 1;
        double temp;
        for (int i = 0; i <= len; ++i)
            if ((temp = rules[i].getConfidence()) > tc) {
                builder.append(temp).append(" ").append(len - i + 1).append("\n");
                tc = temp;
            }

        UtilFunc.outputStringToFile(builder.toString(), "q2.txt");
    }
}
