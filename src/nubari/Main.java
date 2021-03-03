package nubari;


import javax.print.PrintException;
import javax.print.attribute.standard.OrientationRequested;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static int sentinel = 1;
    private static final Scanner scanner = new Scanner(System.in);
    private static final PrintingService printingService = new PrintingService();

    public static void main(String[] args) throws IOException, PrintException {

        while (sentinel != -1) {
            System.out.println("Welcome to nubari printing service\n" +
                    "enter 1 to Test the printer\n" +
                    "enter 2 to configure print options\n" +
                    "enter 3 to print\n" +
                    "enter 4 to exit");
            int userInput = scanner.nextInt();
            switch (userInput) {
                case 1 -> {
                    testPrinter();
                }
                case 2 -> {
                    configurePrintOptions();
                }
                case 3 -> {
                    print();
                }
                case 4 -> {
                    sentinel = -1;
                }
            }
        }

    }

    private static void testPrinter() {
        System.out.println("Printing out test message ....");
        try {
            printingService.print("D:/Documents/Semicolon/Sofware_Engineering/JavaPracticeFiles/Self_Exercises/PrintingService/testFile.txt");
        } catch (IOException | PrintException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private static void configurePrintOptions() {
        System.out.println("Enter 1 to change print orientation\n" +
                "Enter 2 to set copies\n" +
                "Enter 3 to go back to the main menu \n");
        int userInput = scanner.nextInt();
        if (userInput == 1) {
            System.out.println("Enter 1 to set orientation to portrait\nEnter 2 to set orientation to landscape");
            userInput = scanner.nextInt();
            if (userInput == 1) {
                printingService.changeOrientation(OrientationRequested.PORTRAIT);
                System.out.println("Orientation set to portrait ");
            } else if (userInput == 2) {
                printingService.changeOrientation(OrientationRequested.LANDSCAPE);
                System.out.println("Orientation set to landscape ");
            }
        } else if (userInput == 2) {
            System.out.println("Enter number of copies ");
            int copies = scanner.nextInt();
            printingService.setCopies(copies);
        }
    }

    private static void print() {
        System.out.println("Enter 1 to enter path for file we want to print\n" +
                "Enter 2 to select file to print via a file chooser\n" +
                "Enter 3 to print newly created file\n" +
                "Enter 4 to go back to the main menu\n");
        int userInput = scanner.nextInt();
        switch (userInput) {
            case 1 -> {
                System.out.println("Enter the file path ");
                String filePath = scanner.next();
                try {
                    printingService.print(filePath);
                } catch (IOException exception) {
                    System.out.println("Something went wrong ...");
                    System.out.println(exception.getMessage());
                } catch (PrintException printException) {
                    System.out.println(printException.getMessage());
                }
            }
            case 2 -> {
                try {
                    printingService.print();
                    System.out.println("Printing Selected File .....");
                    System.out.println();
                } catch (IOException | PrintException exception) {
                    System.out.println("Something went wrong");
                    System.out.println(exception.getMessage());
                } catch (NullPointerException nullPointerException){
                    System.out.println("Closing File Chooser");
                }
            }
            case 3 -> {
                System.out.println("Functionality not yet implemented");
            }
            case 4 -> {
            }
        }
    }

}
