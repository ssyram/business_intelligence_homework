package homework_adaptor.util;

import fpalgorithm.AssociationRuleBuilder;
import fpalgorithm.fsetgenerator.util.result.FrequentSetContainer;
import fpalgorithm.result.AssociationRule;
import util.GlobalInfo;
import util.adaptor.FpGrowthProjectAdaptor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class UtilFunc {

    public static void outputStringToFile(String toOutput, String filePath) {
        File file = new File("log/" + filePath);
        try {
            if (file.exists())
                file.delete();
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(toOutput);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static AssociationRule[] getRules() {
        FrequentSetContainer container = FpGrowthProjectAdaptor.runFpGrowth();
        return AssociationRuleBuilder.build(container, GlobalInfo.confidence_threshold).toArray(new AssociationRule[0]);
    }
    
}
