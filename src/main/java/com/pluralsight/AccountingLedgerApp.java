package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AccountingLedgerApp {
    public static void main(String[] args) {
        //We'll start here:
        typeItOut("\n[Welcome to Your Accounting Ledger]");
        while (true){
            mainMenu();
            menuSelector();
        }
    }

    public static void mainMenu(){
        System.out.println();
        typeItOut("     ==[Main Menu]==");
        typeItOut("\nWhat would you like to do?\n");
        pause();
        System.out.println("  [D] Add Deposit");
        System.out.println("  [P] Make Payment (Debit)");
        System.out.println("  [L] Display Ledger");
        System.out.println("  [X] Exit the Application");
        pause();
    }
    public static void menuSelector(){
        Scanner scanner = new Scanner(System.in);

        System.out.print("Type Here: ");
        String choice = scanner.nextLine().trim().toUpperCase();
        ArrayList<Transaction> ledger = loadLedger();
        switch (choice) {
            case "D":
               //add deposit!
               pause();
               addDeposit(scanner);
            break;
           case "P":
              //make payment!
              pause();
              makePayment(scanner);
           break;
           case "L":
              //display ledger!
              pause();
              ledgerMenu();
              ledgerSelector(scanner, ledger);
           break;
           case "X":
              pause();
              typeItOut("\nExiting...\nHave a Nice Day!\n");
              scanner.close();
              System.exit(0);
           break;
           default:
               System.out.println("Input not recognized, try again!");
        }

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
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return ledger;
    }

    public static void addDeposit(Scanner scanner){
        //ask for info, append to file
        boolean keepGoing = true;
        while(keepGoing) {
            try {
                System.out.println("\n         ==[Add a Deposit]==         ");
                System.out.println("Please enter the deposit information:");
                System.out.print("Vendor Name: ");
                String vendor = scanner.nextLine();

                System.out.print("Description: ");
                String description = scanner.nextLine();

                System.out.print("Deposit Amount: $");
                double amount = scanner.nextDouble();
                scanner.nextLine(); //carriage return line feed

                LocalDate date = LocalDate.now();
                LocalTime time = LocalTime.now();
                System.out.printf("\nCurrent Date/Time: %tD %tr\nEdit date and time?\n[Y]Yes [N]No\nType Here: ", date, time);
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
                typeItOut("\n------\n");

                pause();
                System.out.println("\n==[Deposit Entry Preview]==");
                System.out.printf("%tD|%tr|%s|%s|$%.2f", date, time, description, vendor, amount);
                typeItOut("\n------\n");
                System.out.print("Is this information correct?\n[Y]Yes [N]No\nType Here: ");
                String correctChoice = scanner.nextLine().trim().toUpperCase();
                if (correctChoice.startsWith("Y")){
                        BufferedWriter buffWriter = new BufferedWriter(new FileWriter("src/main/resources/transactions.csv", true));
                        buffWriter.newLine();
                        buffWriter.write(String.format("%tF|%tT|%s|%s|%.2f", date, time, description,vendor, amount));
                        buffWriter.close();
                        typeItOut("\n------\n");
                        pause();
                        System.out.print("Ledger Updated!\nRecord another deposit?\n[Y]Yes [N]No\nType Here: ");
                        String anotherChoice = scanner.nextLine().trim().toUpperCase();
                        if(anotherChoice.startsWith("N")){
                            keepGoing = false;
                        }
                }
                typeItOut("\n------\n");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void makePayment(Scanner scanner){
        //ask for info, append to file
        boolean keepGoing = true;
        while(keepGoing) {
            try {
                System.out.println("\n        ==[Record a Payment]==        ");
                System.out.println("Please enter the payment information:");
                System.out.print("Vendor Name: ");
                String vendor = scanner.nextLine();

                System.out.print("Description: ");
                String description = scanner.nextLine();

                System.out.print("Payment Amount: -$");
                double amount = scanner.nextDouble();
                scanner.nextLine(); //carriage return line feed

                LocalDate date = LocalDate.now();
                LocalTime time = LocalTime.now();
                System.out.printf("\nCurrent Date/Time: %tD %tr\nEdit date and time?\n[Y]Yes [N]No\nType Here: ", date, time);
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
                typeItOut("\n------\n");
                pause();
                System.out.println("\n==[Payment Entry Preview]==");
                System.out.printf("%tD|%tr|%s|%s|-$%.2f", date, time, description, vendor, amount);
                typeItOut("\n------\n");
                System.out.print("Is this information correct?\n[Y]Yes [N]No\nType Here: ");
                String correctChoice = scanner.nextLine().trim().toUpperCase();
                if (correctChoice.startsWith("Y")){
                        BufferedWriter buffWriter = new BufferedWriter(new FileWriter("src/main/resources/transactions.csv", true));
                        buffWriter.newLine();
                        buffWriter.write(String.format("%tF|%tT|%s|%s|-%.2f", date, time, description, vendor, amount));
                        buffWriter.close();
                        typeItOut("\n------\n");
                        System.out.print("Ledger Updated!\nRecord another payment?\n[Y]Yes [N]No\nType Here: ");
                        String anotherChoice = scanner.nextLine().trim().toUpperCase();
                        if(anotherChoice.startsWith("N")){
                            keepGoing = false;
                        }
                }
                typeItOut("\n------\n");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
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
            pauseShort();
        }
        System.out.println("------------");
    }

    public static void displayReport(ArrayList<Transaction> ledger, String type,String vendorSearch,String saveAs,String write){
        for(Transaction t : ledger){
            if (type.equals("MTD") && (t.getDate().getYear()==LocalDate.now().getYear() && t.getDate().getMonth()==(LocalDate.now().getMonth()))){
                displayTransaction(t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                writeReport(t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount(),saveAs,write);
            }else if (type.equals("PrevMonth") && t.getDate().isAfter(LocalDate.now().minusMonths(1))){
                displayTransaction(t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                writeReport(t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount(),saveAs,write);
            }else if (type.equals("YTD") && (t.getDate().getYear()==LocalDate.now().getYear())){
                displayTransaction(t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                writeReport(t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount(),saveAs,write);
            }else if (type.equals("Vendor") && (t.getVendor().toLowerCase().equalsIgnoreCase(vendorSearch))) {
                displayTransaction(t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                writeReport(t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount(),saveAs,write);
            }
            pauseShort();
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
        typeItOut("     ==[Ledger Menu]==");
        typeItOut("\nWhat would you like to view?\n");
        pause();
        System.out.println("  [A] All Transactions");
        System.out.println("  [D] Deposits");
        System.out.println("  [P] Payments");
        System.out.println("  [R] Reports");
        System.out.println("  [H] Home");
        pause();
    }
    public static void ledgerSelector(Scanner scanner,ArrayList<Transaction> ledger){
        boolean keepGoing = true;
        while (keepGoing) {

            System.out.print("Type Here: ");
            String choice = scanner.nextLine().trim().toUpperCase();

            switch (choice) {
                case "A":
                    //Display All
                    pause();
                    System.out.println("\n==[All Ledger Transaction Records]==");
                    displayLedger(ledger, "All");
                    pressEnter(scanner);
                    ledgerMenu();
                    break;
                case "D":
                    //Display Deposits
                    pause();
                    System.out.println("\n==[All Ledger Deposit Records]==");
                    displayLedger(ledger, "Deposit");
                    pressEnter(scanner);
                    ledgerMenu();
                    break;
                case "P":
                    //Display Payments
                    pause();
                    System.out.println("\n==[All Ledger Payment Records]==");
                    displayLedger(ledger, "Payment");
                    pressEnter(scanner);
                    ledgerMenu();
                    break;
                case "R":
                    //Reports
                    pause();
                    reportMenu();
                    reportSelector(scanner, ledger);
                    break;
                case "H":
                    pause();
                    typeItOut("\nReturning Home...");
                    keepGoing = false;
                    pressEnter(scanner);
                    break;
                default:
                    System.out.println("[Input not recognized, try again]");
            }
        }
    }

    public static void reportMenu(){
        System.out.println();
        typeItOut("       ==[Run a Report]==       ");
        typeItOut("\nWhich report do you want to run?\n");
        pause();
        System.out.println("  [M] Month to Date");
        System.out.println("  [P] Previous Month");
        System.out.println("  [Y] Previous Year");
        System.out.println("  [V] Search by Vendor");
        System.out.println("  [C] Custom Search");
        System.out.println("  [B] Back");
        pause();
    }
    public static void reportSelector(Scanner scanner,ArrayList<Transaction> ledger) {

        boolean keepGoing = true;

        while(keepGoing){
            System.out.print("Type Here: ");
            String choice = scanner.nextLine().trim().toUpperCase();
            String saveAs = "";
            String write = "";
            if (!choice.equalsIgnoreCase("B")) {
                System.out.print("\nWould you like to save the report? [Y] [N]\nType Here: ");
                write = scanner.nextLine().trim().toUpperCase();
                if (write.contains("Y")){
                    System.out.print("Save As (file name): ");
                    saveAs = scanner.nextLine().trim();
                }
            }

            switch (choice) {
                case "M":
                    //MTD
                    //make payment!
                    typeItOut("\n------\n");
                    pause();
                    System.out.println("\n==[Report: Month-to-Date]==");
                    displayReport(ledger, "MTD", "",saveAs,write);
                    pressEnter(scanner);
                    reportMenu();
                    break;
                case "P":
                    //PrevMonth
                    typeItOut("\n------\n");
                    pause();
                    System.out.println("\n==[Report: Previous Month]==");
                    displayReport(ledger, "PrevMonth", "",saveAs,write);
                    pressEnter(scanner);
                    reportMenu();
                    break;
                case "Y":
                    //YTD
                    typeItOut("\n------\n");
                    pause();
                    System.out.println("\n==[Report: Year-to-Date]==");
                    displayReport(ledger, "YTD", "",saveAs,write);
                    pressEnter(scanner);
                    reportMenu();
                    break;
                case "V":
                    //Vendor
                    pause();
                    System.out.print("What is the name of the vendor?\nType Here: ");
                    String vendorSearch = scanner.nextLine().trim();
                    typeItOut(String.format("Searching for %s...", vendorSearch));
                    typeItOut("\n------\n");
                    System.out.println("\n==[Report: Vendor Search]==");
                    displayReport(ledger, "Vendor", vendorSearch,saveAs,write);
                    pressEnter(scanner);
                    reportMenu();
                    break;
                case "C":
                    pause();
                    customSearch(scanner, ledger,saveAs,write);
                    typeItOut("\n------\n");
                    System.out.println("\n==[Report: Custom Search]==");
                    pressEnter(scanner);
                    reportMenu();
                    break;
                case "B":
                    pause();
                    typeItOut("\nReturning!");
                    keepGoing = false;
                    pressEnter(scanner);
                    ledgerMenu();
                    break;
                default:
                    pause();
                    System.out.println("[Input not recognized, try again]");
                    reportMenu();
            }

        }
    }

    public static void customSearch(Scanner scanner,ArrayList<Transaction> ledger,String saveAs,String write){
        //this one might be a bit chunky
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        //info gathering
        System.out.println("Please enter the following filters:");
        System.out.print("Start Date (MM/dd/yyyy): ");
        String userStartDate = scanner.nextLine();
        LocalDate startDate = LocalDate.parse("01/01/1900", dateFormat);
        if (!userStartDate.isEmpty()) {
            startDate = LocalDate.parse(userStartDate, dateFormat);
        }

        System.out.print("End Date (MM/dd/yyyy): ");
        String userEndDate = scanner.nextLine();
        LocalDate endDate = LocalDate.now();
        if (!userEndDate.isEmpty()) {
            endDate = LocalDate.parse(userEndDate, dateFormat);
        }

        System.out.print("Description: ");
        String userDesc = scanner.nextLine().trim().toLowerCase();

        System.out.print("Vendor: ");
        String userVendor = scanner.nextLine().trim().toLowerCase();

        System.out.print("Amount: $");
        String userAmount = scanner.nextLine();
        double amount = 0;
        if (!userAmount.isEmpty()) {
            amount = Double.parseDouble(userAmount);
        }
        searchFilter(ledger,startDate,endDate,userDesc,userVendor,amount,saveAs,write);
    }
    public static void searchFilter(ArrayList<Transaction> ledger,LocalDate startDate,LocalDate endDate,String userDesc,String userVendor, double userAmount,String saveAs,String write){
        try {
            ArrayList<Transaction> filtered = new ArrayList<>();
           for (Transaction t : ledger) {
                    //Start and end dates, inclusive of boundary dates
                if (((startDate.isEqual(t.getDate()) || startDate.isBefore(t.getDate()))&& (endDate.isEqual(t.getDate())|| endDate.isAfter(t.getDate())))&&
                        //check description for input and against each entry
                        (userDesc.isEmpty() || t.getDescription().toLowerCase().contains(userDesc)) &&
                        //check vendor for input and against each entry
                        (userVendor.isEmpty() || t.getVendor().toLowerCase().contains(userVendor)) &&
                        //check amount for input and against each entry
                        (userAmount == 0 || t.getAmount() == userAmount)) {

                    filtered.add(t);
                }
            }
            typeItOut("\n------\n");

            if (!filtered.isEmpty()) {
                pause();
                System.out.println("\n==[Filtered Search Results]==");
                for (Transaction t : filtered) {
                    displayTransaction(t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                    writeReport(t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount(), saveAs, write);
                }
                typeItOut("\n------\n");

            } else {
                System.out.println("[No results found, try again]");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("[Unable to generate report, please try again]");
        }
    }

    public static void writeReport(LocalDate date,LocalTime time,String description,String vendor,double amount,String saveAs,String write){
       if(write.contains("Y")) {
           try {
               BufferedWriter buffWriter = new BufferedWriter(new FileWriter("src/main/resources/reports/"+saveAs + ".csv",true));
               if (amount >= 0) {
                   buffWriter.write(String.format("%tF|%tT|%s|%s|%.2f", date, time, description, vendor, amount));
                   buffWriter.newLine();
               } else {
                   buffWriter.write(String.format("%tF|%tT|%s|%s|-%.2f", date, time, description, vendor, Math.abs(amount)));
                   buffWriter.newLine();
               }
               buffWriter.close();
           } catch (Exception e) {
               System.out.println(e.getMessage());
           }
       }
    }

    public static void pressEnter(Scanner scanner){
        try {
            pause();
            typeItOut("\nPress [ENTER] to continue...");
            scanner.nextLine();
             //pause for user
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public static void pause(){
        try{
            Thread.sleep(250);
        } catch (Exception e) {
            System.out.printf(e.getMessage());
        }
    }
    public static void pauseShort(){
        try{
            Thread.sleep(60);
        } catch (Exception e) {
            System.out.printf(e.getMessage());
        }
    }
    public static void typeItOut(String message){
        try {
            for (int i = 0; i < message.length(); i++) {
                System.out.print(message.charAt(i));
                System.out.flush();
                Thread.sleep(30);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}