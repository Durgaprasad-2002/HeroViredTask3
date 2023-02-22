import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.io.File;
import java.time.LocalDateTime;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

class FoodClass {
    File NewFile = new File("billingDetails.csv");
    static Scanner sc1 = new Scanner(System.in);
    static ArrayList<ArrayList<String>> ItemsMemu = new ArrayList<ArrayList<String>>();
    static ArrayList<ArrayList<String>> FoodPlan = new ArrayList<ArrayList<String>>();

    static void Readingfile(String fileName, int fn) {
        try {
            String line = "";
            Scanner br = new Scanner(new FileReader(fileName));
            while ((br.hasNext())) {
                line = br.nextLine();
                String[] FoodItems1 = line.split(",");
                List<String> fixedLenghtList = Arrays.asList(FoodItems1);
                ArrayList<String> Fooditems = new ArrayList<String>(fixedLenghtList);
                if (fn == 0)
                    ItemsMemu.add(Fooditems);
                else
                    FoodPlan.add(Fooditems);
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Runtime Error");
        }
    }

    static double ClaculateAmount(int id, int q) {
        double v = 0;
        for (int i = 0; i < ItemsMemu.size(); i++) {
            ArrayList<String> dump = ItemsMemu.get(i);
            if (id == Integer.parseInt(dump.get(0))) {
                v += (q * Integer.parseInt(dump.get(2)));
            }
        }
        return v;
    }

    static String ConfirmOrder(String m[]) {
        int Amount = 0;
        System.out.println();
        System.out.println("Ordered Items---------->>>");
        for (int k = 0; k < m.length; k += 2) {
            for (int j = 0; j < ItemsMemu.size(); j++) {
                ArrayList<String> dump = ItemsMemu.get(j);
                if (Integer.parseInt(m[k]) == Integer.parseInt(dump.get(0))) {
                    System.out.println("[ " + dump.get(1) + "    ||  Cost  :  Rs."
                            + Integer.parseInt(m[k + 1]) * Integer.parseInt(dump.get(2)) + " ||  Qty : "
                            + Integer.parseInt(m[k + 1]) + " ]");
                    Amount += Integer.parseInt(m[k + 1]) * Integer.parseInt(dump.get(2));
                }
            }
        }
        System.out.println();
        System.out.println("Total Bill : Rs." + Amount);
        System.out.println();
        System.out.println("Enter 'y' to Confirm order");
        char c = sc1.next().charAt(0);
        if (c == 'y')
            return ",Approved";
        return ",Cancelled";
    }

    static void CreatingNewOrder() {
        String st = "\n", Food = "";
        ArrayList<String> dummy = FoodPlan.get(FoodPlan.size() - 1);
        int lastIndex = Integer.parseInt(dummy.get(0));
        double totalAmount = 0;
        int i = 1, it, count;
        while (true) {
            System.out.println();
            System.out.println("Enter Order Details--->");
            System.out.print("Item:" + i + "  Enter ItemId : ");
            it = sc1.nextInt();
            System.out.print("Item:" + i + "  Quantity : ");
            count = sc1.nextInt();
            i++;
            totalAmount += ClaculateAmount(it, count);
            Food += String.valueOf(it) + " " + String.valueOf(count) + " ";
            System.out.println();
            System.out.println("Press y to palce Another order");
            char c = sc1.next().charAt(0);
            if (c != 'y')
                break;
        }
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-dd-MMM");
        String formattedDate = myDateObj.format(myFormatObj);
        st += String.valueOf((lastIndex + 1)) + "," + formattedDate + "," + String.valueOf(totalAmount) + ',' + Food;
        String[] m = Food.split(" ");
        st += ConfirmOrder(m);
        sc1.nextLine();
        try {
            byte[] ByteInput = st.getBytes();
            FileOutputStream FileWrite = new FileOutputStream("billingDetails.csv", true);
            FileWrite.write(ByteInput);
            FoodPlan.clear();
            Readingfile("billingDetails.csv", 1);
            FileWrite.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void AddingIntoFile() {
        try {
            NewFile.delete();
            if (NewFile.createNewFile()) {
                System.out.println("Status updated in file");
            }
            for (ArrayList<String> arrayList : FoodPlan) {
                String listString = String.join(",", arrayList);
                listString += "\n";
                byte[] ByteInput = listString.getBytes();
                FileOutputStream FileWrite = new FileOutputStream("billingDetails.csv",
                        true);
                FileWrite.write(ByteInput);
                FileWrite.close();
            }
        } catch (Exception e) {
            System.out.println();
        }
    }

    void CheckBillStatus() {
        System.out.print("Enter order Id : ");
        int n = sc1.nextInt();
        for (int j = 0; j < FoodPlan.size(); j++) {
            ArrayList<String> dump = FoodPlan.get(j);
            if (n == Integer.parseInt(dump.get(0))) {
                System.out.println(dump);
                System.out.println("press 'a' to place Order");
                char c = sc1.next().charAt(0);
                if (c == 'a')
                    FoodPlan.get(j).set(4, "Approved");
                else
                    FoodPlan.get(j).set(4, "Cancelled");
            }
        }
        sc1.nextLine();
        AddingIntoFile();
    }

    void CollectionOfDay() {
        System.out.print("Enter the date in Format year-day-month :");
        String date = sc1.nextLine();
        double Coll = 0.0;
        for (ArrayList<String> dump : FoodPlan) {
            if ((dump.get(1)).equals(date)) {
                Coll += (Double.parseDouble(dump.get(2)));
            }
        }
        System.out.println(date + " day's Total Collection is  Rs." + Coll);
    }

    FoodClass() {
        Readingfile("menuList.csv", 0);
        Readingfile("billingDetails.csv", 1);
    }
}

// main class
class RestaurantApplicationTask {
    static Scanner sc = new Scanner(System.in);
    static String menuItems[] = { "Enter New Order", "Edit Bill Status", "See Collection of a day" };
    static FoodClass orderA = new FoodClass();

    static void Menulist() {
        System.out.println();
        System.out.println("Welcome to 'Driven Cafe' , Hyderabad");
        System.out.println("------------------------------------");
        for (int i = 0; i < 3; i++)
            System.out.println((i + 1) + "." + menuItems[i]);
    }

    static void ExecutingMenu() {
        System.out.println();
        System.out.print("Enter your Choice : ");
        int item = sc.nextInt();
        if (item == 1)
            orderA.CreatingNewOrder();
        else if (item == 2)
            orderA.CheckBillStatus();
        else if (item == 3) {
            orderA.CollectionOfDay();
        } else
            System.out.println("Entered Choice not in List");
    }

    public static void main(String[] args) {
        while (true) {
            Menulist();
            ExecutingMenu();
            System.out.println("Press 'y' to return 'Main_Menu'");
            char ch = sc.next().charAt(0);
            if (ch != 'y')
                break;
        }
    }
}