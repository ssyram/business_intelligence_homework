import fpalgorithm.fsetgenerator.util.result.FrequentSet;
import fpalgorithm.fsetgenerator.util.result.FrequentSetContainer;
import sun.security.x509.FreshestCRLExtension;
import test.TestGeneric;
import test.TestRunner;
import test.TestSamplesGenerator;
import util.Runner;
import util.adaptor.FpGrowthProjectAdaptor;
import util.adaptor.apriori.AprioriProjectAdaptor;

import java.io.IOException;

public class Main {

    public static void main(String []args) {
        System.out.println("run Apriori:");
        TestRunner.runApriori(4);
        System.out.println("----------------------------");
        System.out.println("run Fp Growth:");
        TestRunner.runFpGrowth(4);
        TestSamplesGenerator.test();
        TestGeneric.test();

//        try {
//            TestRunner.generateSampleTransactions(4);
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.exit(-1);
//        }
//        Object o = new Object();
//        synchronized (o) {
//            try {
//                o.wait(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

//        FrequentSetContainer ca = AprioriProjectAdaptor.runApriori();
//        FrequentSetContainer cf = FpGrowthProjectAdaptor.runFpGrowth();
//
//        for (FrequentSet set: cf)
//            if (ca.find(set.getSet()) == null)
//                System.out.println(set);
//
//        System.out.println("----------------------");
//
//        System.out.println(ca.toString());
//        System.out.println(cf.toString());
    }
}
