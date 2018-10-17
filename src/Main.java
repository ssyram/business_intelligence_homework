import test.TestGeneric;
import test.TestRunner;
import test.TestSamplesGenerator;

public class Main {

    public static void main(String []args) {
        System.out.println("run Apriori:");
        TestRunner.runApriori(1);
        System.out.println("----------------------------");
        System.out.println("run Fp Growth:");
        TestRunner.runFpGrowth(1);
//        TestSamplesGenerator.test();
//        TestGeneric.test();
    }
}
