package jjlive;

import java.io.Console;

public class RPEL {
    public static void main(String[] args) {

        Console cnsl;
        String name;
        try {
            cnsl = System.console();
            if (cnsl == null) {
                System.out.println("没有Console对象");
                return;
            }

            // read line from the user input
            name = cnsl.readLine("jjlive>>: ");


            // prints
            System.out.println("Name entered1 : " + name);
            name = cnsl.readLine("Name2: ");
            System.out.println("Name entered2 : " + name);
            name = cnsl.readLine("Name3: ");
            System.out.println("Name entered3 : " + name);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}