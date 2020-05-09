package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Scanner;

public class TaskManager {
    private static String[][] tab = new String[0][0];
    private static String[] menu = {"add", "remove", "list", ConsoleColors.RED + "exit" + ConsoleColors.RESET};
    private static Path path = null;

    public static void main(String[] args) {
        tab = loadDataToTab(tab);
        menu(menu);
        choice();
    }

    public static void menu(String[] menu) {
        System.out.println(ConsoleColors.BLUE_BOLD + "Please select an option:");
        for (String element : menu) System.out.println(ConsoleColors.RESET + element);
    }

    public static String[][] loadDataToTab(String[][] tabel) {
        Scanner consoleScan = new Scanner(System.in);
        System.out.println("Please enter the filename (or path) to open.");
        path = Paths.get(consoleScan.next());
        if (!Files.exists(path)) {
            System.out.println("File doesn't exist! Do you want to create new file? (" + ConsoleColors.RED + "Y" + ConsoleColors.RESET + "/" + ConsoleColors.RED + "N" + ConsoleColors.RESET + ").");
            while (!consoleScan.hasNext("Y") && !consoleScan.hasNext("N")) {
                System.out.println("Incorrect argument passed. Enter the correct value (" + ConsoleColors.RED + "Y" + ConsoleColors.RESET + "/" + ConsoleColors.RED + "N" + ConsoleColors.RESET + ").");
                consoleScan.next();
            }
            if (consoleScan.hasNext("Y")) {
                try {
                    Files.createFile(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println(ConsoleColors.RED + "Bye, bye!");
                System.exit(0);
            }
        } else {
            try {
                for (String line : Files.readAllLines(path)) {
                    tabel = ArrayUtils.add(tabel, line.split(","));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tabel;
    }

    private static void choice() {
        Scanner consoleScan = new Scanner(System.in);
        while (consoleScan.hasNext()) {
            while (!consoleScan.hasNext("add") && !consoleScan.hasNext("remove") && !consoleScan.hasNext("list") && !consoleScan.hasNext("exit")) {
                System.out.println("Please select a correct option.");
                consoleScan.next();
            }
            switch (consoleScan.next()) {
                case "add":
                    add();
                    break;
                case "remove":
                    tab=remove(tab);
                    break;
                case "list":
                    list(tab);
                    break;
                case "exit":
                    saveTabToFile(path, tab);
            }
            menu(menu);
        }
    }

    private static void add() {
        String[] line = new String[3];
        Scanner consoleScan = new Scanner(System.in);
        System.out.println("Please add task description.");
        line[0] = consoleScan.nextLine();
        System.out.println("Please add task due date (YYYY-MM-DD).");
        line[1] = consoleScan.next();
        System.out.println("Is your task important (" + ConsoleColors.RED + "true" + ConsoleColors.RESET + "/" + ConsoleColors.RED + "false" + ConsoleColors.RESET + ")?");
        line[2] = consoleScan.next();
        tab = ArrayUtils.add(tab, line);
    }

    public static void list(String[][] tabel) {
        if (tabel.length == 0) {
            System.out.println("File is empty.");
        } else {
            for (int i = 0; i < tabel.length; i++) {
                System.out.print(i + 1 + " : " + StringUtils.join(tabel[i], " ") + "\n");
            }
        }
    }

    public static String[][] remove(String[][] tabel) {
        Scanner consoleScan = new Scanner(System.in);
        if (tabel.length == 0) {
            System.out.println("File is empty.");
        } else {
            System.out.println("Please select number to remove.");
            boolean correct = true;
            String taskNumber = "";
            while (correct) {
                taskNumber = consoleScan.next();
                if (NumberUtils.isParsable(taskNumber) && Integer.valueOf(taskNumber) > 0 && Integer.valueOf(taskNumber) <= tabel.length) {
                    correct = false;
                } else {
                    System.out.println("Incorrect argument passed. Please enter number greater 0.");
                }
            }
            tabel = ArrayUtils.remove(tabel, Integer.valueOf(taskNumber) - 1);
        }
        return tabel;
    }

    public static void saveTabToFile(Path path, String[][] tab) {
        try (FileWriter fileWriter = new FileWriter(String.valueOf(path))) {
            for (int i = 0; i < tab.length; i++) {
                fileWriter.append(StringUtils.join(tab[i], ",") + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(ConsoleColors.RED + "Bye, bye!");
        System.exit(0);

    }
}
