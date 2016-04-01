/*
import java.io.Console;

public class ConsoleDemo {
    public static void main(String[] args) {

        Console cnsl = null;
        String name = null;

        try {
            cnsl = System.console();
            if (cnsl == null) {
                System.out.println("没有Console对象");
                return;
            }

            // read line from the user input
            name = cnsl.readLine("Name1: ");

            // prints
            System.out.println("Name entered1 : " + name);
            name = cnsl.readLine("Name2: ");
            System.out.println("Name entered2 : " + name);
            name = cnsl.readLine("Name3: ");
            System.out.println("Name entered3 : " + name);
        } catch (Exception ex) {

            // if any error occurs
            ex.printStackTrace();
        }
    }
}*/
