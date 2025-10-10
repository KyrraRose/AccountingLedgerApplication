package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class AccountingLedgerApp {
    public static void main(String[] args) {
        //We'll start here:
        System.out.println("=== Welcome to the Accounting Ledger ===");

        while (true){
            mainMenu();
            menuSelector();
        }

    }
    public static void mainMenu(){
        System.out.println();
        System.out.println("     ==[Main Menu]==");
        System.out.println("What would you like to do?");
        System.out.println("  [D] Add Deposit");
        System.out.println("  [P] Make Payment (Debit)");
        System.out.println("  [L] Display Ledger");
        System.out.println("  [X] Exit the Application");
    }

    public static void menuSelector(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Type Here: ");
        String choice = scanner.nextLine().trim().toUpperCase();
        ArrayList<Transaction> ledger = loadLedger();
        switch (choice){
            case "D":
                //add deposit!
                addDeposit(scanner,ledger);
                break;
            case "P":
                //make payment!
                break;
            case "L":
                //display ledger!
                break;
            case "X":
                System.out.println("Exiting - Have a Nice Day!");
                scanner.close();
                System.exit(0);
                break;
            default:
                System.out.println("Input not recognized, try again!");
        }
        System.out.println("\n----");
        System.out.println("Press [ENTER] to continue..");
    }
    public static ArrayList<Transaction> loadLedger(){
        ArrayList<Transaction> ledger = new ArrayList<>();
        try {
            BufferedReader bufReader = new BufferedReader(new FileReader("src/main/resources/transactions.csv"));
            String input;
            while((input = bufReader.readLine()) != null) {
                String[] tokens = input.split("\\|");
                if(!tokens[0].equals("date")) {
                    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

                    LocalDate date = LocalDate.parse(tokens[0], dateFormat);
                    LocalTime time = LocalTime.parse(tokens[1], timeFormat);
                    String description = tokens[2];
                    String vendor = tokens[3];
                    double amount = Double.parseDouble(tokens[4]);

                    ledger.add(new Transaction(date, time, description, vendor, amount));
                }

            }

            bufReader.close();
        } catch (IOException e) {
            e.printStackTrace(); //Error chain
        }
        return ledger;

    }
    public static void addDeposit(Scanner scanner,ArrayList<Transaction> ledger){
        //ask for info, append to file
        boolean keepGoing = true;
        while(keepGoing) {
            System.out.println("         ==[Add a Deposit]==         ");
            System.out.println("Please enter the deposit information:");
            System.out.print("Vendor Name: ");
            String name = scanner.nextLine();

            System.out.print("Description: ");
            String description = scanner.nextLine();

            System.out.print("Deposit Amount: $");
            double amount = scanner.nextDouble();
            scanner.nextLine(); //crlf

            LocalDate date = LocalDate.now();
            LocalTime time = LocalTime.now();
            System.out.printf("Current Date/Time: %tD %tr\nEdit date and time?\n[Y]Yes [N]No\nType Here: ", date, time);
            String editDateChoice = scanner.nextLine().trim().toUpperCase();

            if(editDateChoice.startsWith("Y")) {
                System.out.print("\nWhat is the date of the deposit?\nType Here (yyyy-MM-dd): ");
                String userDate = scanner.nextLine();

                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm a");
                date = LocalDate.parse(userDate, dateFormat);

                System.out.print("What is the time of the deposit?\nType Here (HH:mm a): ");
                String userTime = scanner.nextLine();
                time = LocalTime.parse(userTime, timeFormat);

            }

            System.out.println("\n==[Deposit Entry Preview]==");
            System.out.printf("%tD|%tr|%s|%s|%f", date, time, description, name, amount);
            System.out.println("\n------\n");
            System.out.print("Is this information correct?\n[Y]Yes [N]No\nType Here: ");
            String correctChoice = scanner.nextLine().trim().toUpperCase();
            if (correctChoice.startsWith("Y")){

                try {
                    BufferedWriter buffWriter = new BufferedWriter(new FileWriter("src/main/resources/transactions.csv", true));
                    buffWriter.newLine();
                    buffWriter.write(String.format("%tF|%tT|%s|%s|$%.2f", date, time, description, name, amount));
                    buffWriter.close();
                    System.out.print("----\nLedger Updated!\nRecord another deposit?\n[Y]Yes [N]No\nType Here: ");
                    String anotherChoice = scanner.nextLine().trim().toUpperCase();
                    if(anotherChoice.startsWith("N")){
                        keepGoing = false;
                    }
                } catch (IOException e) {
                        System.out.println(e.getMessage());
                }

            }


        }




    }
}
