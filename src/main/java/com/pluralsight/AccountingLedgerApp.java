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
        switch (choice){
            case "D":
                //add deposit!
                addDeposit(scanner);
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
        System.out.println("----");
        System.out.println("Press [ENTER] to continue..");
    }
    public static ArrayList<Transaction> loadLedger(){
        ArrayList<Transaction> ledger = new ArrayList<>();
        try {
            BufferedReader bufReader = new BufferedReader(new FileReader("src/main/resources/transactions.csv"));
            String input;
            while((input = bufReader.readLine()) != null) {
                String[] tokens = input.split("\\|");
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                LocalDate date = LocalDate.parse(tokens[0],dateFormat);
                LocalTime time = ;//Lunchtime notes format time!!




                ledger.add(new Transaction(date,time,description,vendor,amount));
            }

            bufReader.close();
        } catch (IOException e) {
            e.printStackTrace(); //Error chain
        }

    }
    public static void addDeposit(Scanner scanner){


    }
}
