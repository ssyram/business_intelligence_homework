package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TestIO {
    public static void test() throws IOException {
        File file = new File("samples/_1.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String s = reader.readLine();
        String[] splitedString = s.split(" ");
        for (String ss: splitedString)
            System.out.println(ss);

        System.out.println("------------------------");
        System.out.println(splitedString.length);
    }
}
