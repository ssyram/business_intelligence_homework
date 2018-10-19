package homework_adaptor;

import fpalgorithm.AssociationRuleBuilder;
import fpalgorithm.fsetgenerator.util.result.FrequentSetContainer;
import fpalgorithm.result.AssociationRule;
import homework_adaptor.util.UtilFunc;
import javafx.util.Pair;
import util.GlobalInfo;
import util.adaptor.FpGrowthProjectAdaptor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class First {
    public static void resolve() {
        AssociationRule[] rules = UtilFunc.getRules();
        // descending sort
//        Arrays.sort(rules, (a, b) -> {
//            if (a.getSupport() == b.getSupport()) return 0;
//            if (a.getSupport() - b.getSupport() > 0)
//                return 1;
//            else return -1;
//        });
        Arrays.sort(rules, Comparator.comparing(AssociationRule::getSupport));
        StringBuilder builder = new StringBuilder();
        double ts = -1;
        int len = rules.length - 1;
        double temp;
        for (int i = 0; i <= len; ++i)
            if ((temp = rules[i].getSupport()) > ts) {
                builder.append(temp).append(" ").append(len - i + 1).append("\n");
                ts = temp;
            }
            
        UtilFunc.outputStringToFile(builder.toString(), "q1.txt");
    }
    
}
