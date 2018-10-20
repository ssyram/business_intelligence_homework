import database.DatabaseOperator;
import fpalgorithm.fsetgenerator.util.result.FrequentSet;
import fpalgorithm.fsetgenerator.util.result.FrequentSetContainer;
import homework_adaptor.First;
import homework_adaptor.Second;
import homework_adaptor.Third;
import sun.security.x509.FreshestCRLExtension;
import test.TestGeneric;
import test.TestRunner;
import test.TestSamplesGenerator;
import util.GlobalInfo;
import util.Runner;
import util.adaptor.FpGrowthProjectAdaptor;
import util.adaptor.apriori.AprioriProjectAdaptor;
import util.samplegenerator.SamplesGenerator;

import java.io.IOException;

public class Main {
    
    private static void regenerateSamples() {
        DatabaseOperator.emptyDatabase();
        SamplesGenerator.generate(120, 12000, 60, 60, 1.98 * 80, 1.98 * 20, 40, 80);
    }

    public static void main(String []args) {
//        System.out.println("run Apriori:");
//        TestRunner.runApriori(6);
//        System.out.println("----------------------------");
//        System.out.println("run Fp Growth:");
//        TestRunner.runFpGrowth(6);
//        TestSamplesGenerator.test();
//        TestGeneric.test();
        
        for (int mode = 0; mode != 5; ) {
            System.out.println("Please choose a mode:");
            System.out.println("0: to regenerate samples.");
            System.out.println("1 ~ 3: to resolve the 1st ~ 3rd question.");
            System.out.println("Please note that the result will be put out on log/q<1 ~ 3>.txt");
            System.out.println("5: to quit");

            try {
                mode = System.in.read() - '0';
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (mode == 0)
                regenerateSamples();
            else if (mode == 1) {
                if (GlobalInfo.confidence_threshold != 0.8 || GlobalInfo.Supportive != 0.3)
                DatabaseOperator.loadGlobalInfo(0.3, 0.8);
                First.resolve();
                try {
                    Runtime.getRuntime().exec("python python_ploter/q1.py");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if (mode == 2) {
                if (GlobalInfo.confidence_threshold != 0 || GlobalInfo.Supportive != 0.3)
                    DatabaseOperator.loadGlobalInfo(0.3, 0);
                Second.resolve();
                try {
                    Runtime.getRuntime().exec("python python_ploter/q2.py");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if (mode == 3) {
                if (GlobalInfo.Supportive != 0.6 || GlobalInfo.confidence_threshold != 0.8)
                    DatabaseOperator.loadGlobalInfo(0.6, 0.8);
                Third.resolve();
                try {
                    Runtime.getRuntime().exec("open log/q3.txt");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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
