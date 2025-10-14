package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
                makePayment(scanner,ledger);
                break;
            case "L":
                //display ledger!
                ledgerMenu();
                ledgerSelector(scanner,ledger);
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
        scanner.nextLine();
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

            Collections.sort(ledger);

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
            String vendor = scanner.nextLine();

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
                System.out.print("\nWhat is the date of the deposit?\nType Here (MM/dd/yyyy): ");
                String userDate = scanner.nextLine();

                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");

                date = LocalDate.parse(userDate, dateFormat);

                System.out.print("What is the time of the deposit?\nType Here (e.g. 14:45): ");
                String userTime = scanner.nextLine().trim().toUpperCase();
                DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
                time = LocalTime.parse(userTime, timeFormat);

            }


            System.out.println("\n==[Deposit Entry Preview]==");
            System.out.printf("%tD|%tr|%s|%s|$%.2f", date, time, description, vendor, amount);
            System.out.println("\n------\n");
            System.out.print("Is this information correct?\n[Y]Yes [N]No\nType Here: ");
            String correctChoice = scanner.nextLine().trim().toUpperCase();
            if (correctChoice.startsWith("Y")){

                try {
                    BufferedWriter buffWriter = new BufferedWriter(new FileWriter("src/main/resources/transactions.csv", true));
                    buffWriter.newLine();
                    buffWriter.write(String.format("%tF|%tT|%s|%s|%.2f", date, time, description,vendor, amount));
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
            System.out.println("\n----");



        }




    }

    public static void makePayment(Scanner scanner,ArrayList<Transaction> ledger){
        //ask for info, append to file
        boolean keepGoing = true;
        while(keepGoing) {
            System.out.println("        ==[Record a Payment]==        ");
            System.out.println("Please enter the payment information:");
            System.out.print("Vendor Name: ");
            String vendor = scanner.nextLine();

            System.out.print("Description: ");
            String description = scanner.nextLine();

            System.out.print("Payment Amount: -$");
            double amount = scanner.nextDouble();
            scanner.nextLine(); //crlf

            LocalDate date = LocalDate.now();
            LocalTime time = LocalTime.now();
            System.out.printf("Current Date/Time: %tD %tr\nEdit date and time?\n[Y]Yes [N]No\nType Here: ", date, time);
            String editDateChoice = scanner.nextLine().trim().toUpperCase();

            if(editDateChoice.startsWith("Y")) {
                System.out.print("\nWhat is the date of the payment?\nType Here (MM/dd/yyyy): ");
                String userDate = scanner.nextLine();

                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");

                date = LocalDate.parse(userDate, dateFormat);

                System.out.print("What is the time of the payment?\nType Here (e.g. 16:30): ");
                String userTime = scanner.nextLine().trim().toUpperCase();
                DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
                time = LocalTime.parse(userTime, timeFormat);

            }

            System.out.println("\n==[Payment Entry Preview]==");
            System.out.printf("%tD|%tr|%s|%s|-$%.2f", date, time, description, vendor, amount);
            System.out.println("\n------\n");
            System.out.print("Is this information correct?\n[Y]Yes [N]No\nType Here: ");
            String correctChoice = scanner.nextLine().trim().toUpperCase();
            if (correctChoice.startsWith("Y")){

                try {
                    BufferedWriter buffWriter = new BufferedWriter(new FileWriter("src/main/resources/transactions.csv", true));
                    buffWriter.newLine();
                    buffWriter.write(String.format("%tF|%tT|%s|%s|-%.2f", date, time, description, vendor, amount));
                    buffWriter.close();
                    System.out.print("----\nLedger Updated!\nRecord another payment?\n[Y]Yes [N]No\nType Here: ");
                    String anotherChoice = scanner.nextLine().trim().toUpperCase();
                    if(anotherChoice.startsWith("N")){
                        keepGoing = false;
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }

            }
            System.out.println("\n----");

        }




    }

    public static void displayLedger(ArrayList<Transaction> ledger, String type){
        //Let's not make three separate methods:

        for(Transaction t : ledger){
            if (type.equals("All")) {
                displayTransaction(t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
            }else if (type.equals("Deposit") && t.getAmount()>0){
                displayTransaction(t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
            }else if (type.equals("Payment") && t.getAmount()<0){
                displayTransaction(t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
            }

        }
        System.out.println("------------");
    }
    public static void displayReport(ArrayList<Transaction> ledger, String type,String vendorSearch){
        //Let's not make three separate methods:

        for(Transaction t : ledger){
            if (type.equals("MTD") && (t.getDate().getYear()==LocalDate.now().getYear() && t.getDate().getMonth()==(LocalDate.now().getMonth()))){
                displayTransaction(t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
            }else if (type.equals("PrevMonth") && t.getDate().isAfter(LocalDate.now().minusMonths(1))){
                displayTransaction(t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
            }else if (type.equals("YTD") && (t.getDate().getYear()==LocalDate.now().getYear())){
                displayTransaction(t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
            }else if (type.equals("Vendor") && (t.getVendor().toLowerCase().equalsIgnoreCase(vendorSearch))){
                displayTransaction(t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
            }
        }
        System.out.println("------------");
    }

    public static void displayTransaction(LocalDate date,LocalTime time,String description,String vendor,double amount){
        if (amount >= 0){
            System.out.printf("%tD|%tr|%s|%s|$%.2f\n", date, time, description, vendor, amount);
        }else{
            System.out.printf("%tD|%tr|%s|%s|-$%.2f\n", date, time, description, vendor, Math.abs(amount));
        }


    }
    public static void ledgerMenu(){
        System.out.println();
        System.out.println("     ==[Ledger Menu]==");
        System.out.println("What would you like to view?");
        System.out.println("  [A] All Transactions");
        System.out.println("  [D] Deposits");
        System.out.println("  [P] Payments");
        System.out.println("  [R] Reports");
        System.out.println("  [H] Home");
    }
    public static void ledgerSelector(Scanner scanner,ArrayList<Transaction> ledger){

        System.out.print("Type Here: ");
        String choice = scanner.nextLine().trim().toUpperCase();

        switch (choice) {
            case "A":
                //Display All
                System.out.println("\n==[All Ledger Transaction Records]==");
                displayLedger(ledger,"All");
                break;
            case "D":
                //Display Deposits
                System.out.println("\n==[All Ledger Deposit Records]==");
                displayLedger(ledger,"Deposit");

                break;
            case "P":
                //Display Payments
                System.out.println("\n==[All Ledger Payment Records]==");
                displayLedger(ledger,"Payment");

                break;
            case "R":
                //Reports
                reportMenu();
                reportSelector(scanner,ledger);


                break;
            case "H":
                System.out.println("Returning [HOME]!");
                break;
            default:
                System.out.println("Input not recognized, try again!");
        }
    }
    public static void reportMenu(){
        System.out.println();
        System.out.println("       ==[Run a Report]==       ");
        System.out.println("Which report do you want to run?");
        System.out.println("  [M] Month to Date");
        System.out.println("  [P] Previous Month");
        System.out.println("  [Y] Previous Year");
        System.out.println("  [V] Search by Vendor");
        System.out.println("  [S] Custom Search");
        System.out.println("  [B] Back");
        //actually numbered options might be better - less mental load by typists
    }
    public static void reportSelector(Scanner scanner,ArrayList<Transaction> ledger){


        System.out.print("Type Here: ");
        String choice = scanner.nextLine().trim().toUpperCase();

        switch (choice) {
            case "M":
                //MTD
                System.out.println("\n==[Report: Month-to-Date]==");
                displayReport(ledger,"MTD","");
                break;
            case "P":
                //PrevMonth
                System.out.println("\n==[Report: Previous Month]==");
                displayReport(ledger,"PrevMonth","");

                break;
            case "Y":
                //YTD
                System.out.println("\n==[Report: Year-to-Date]==");
                displayReport(ledger,"YTD","");

                break;
            case "V":
                //Vendor
                System.out.print("What is the name of the vendor?\nType Here: ");
                String vendorSearch = scanner.nextLine().trim();
                System.out.printf("Searching for %s...",vendorSearch);
                System.out.println("\n==[Report: Vendor Search]==");
                displayReport(ledger,"Vendor",vendorSearch);
                break;
            case "S":
                //Custom Search
                //String type = scanner.nextLine();
                //System.out.printf("\n==[Report: %s Search]==",type);
                System.out.println("Custom functionality coming soon");
                break;
            case "B":
                System.out.println("Returning [HOME]!");
                break;
            default:
                System.out.println("Input not recognized, try again!");
        }
    }


}