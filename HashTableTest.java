import java.io.*;
import java.sql.SQLOutput;
import java.util.*;

public class HashTableTest {

    public static String methodOfHash = "";

    public static void hash(String methodOfHash) throws Exception {

        HashTable hashTable = new HashTable();

        // initializing doubly linked lists
        for (int i = 0; i < hashTable.TABLE_SIZE; i++) {
            hashTable.table[i] = new DList<>();
        }

        Scanner sc = new Scanner(new File("src/Customer.csv"));

        while (sc.hasNext()) {
            String inputS = "";
            inputS = sc.next();
            HashTable.elementSize++;
            hashTable.insert(inputS, methodOfHash);
        }
        sc.close();
        hashTable.OutputTwo();
        hashTable.statistics();
        HashTable.elementSize = 0;
    }

    public static void main(String[] args) throws Exception{
        System.out.println("This program hashes by last and first name");
        System.out.println("1. Division----\n");
        methodOfHash = "Division";
        hash(methodOfHash);
        System.out.println("\n\n=====================================================================================================================");
        System.out.println("=====================================================================================================================");


        System.out.println("2. Multiplication----\n");
        methodOfHash = "Multiplication";
        hash(methodOfHash);
        System.out.println("\n\n=====================================================================================================================");
        System.out.println("=====================================================================================================================");

        System.out.println("3. Mid-Square----\n");
        methodOfHash = "Mid-Square";
        hash(methodOfHash);
       System.out.println("\n\n=====================================================================================================================");
        System.out.println("=====================================================================================================================");

        System.out.println("4. Folding----\n");
        methodOfHash = "Folding";
        hash(methodOfHash);
        System.out.println("\n\n=====================================================================================================================");
        System.out.println("=====================================================================================================================");
/*
        System.out.println("5. Cryptographic----\n");
        methodOfHash = "Cryptographic";
        hash(methodOfHash);
        System.out.println("\n\n=====================================================================================================================");
        System.out.println("=====================================================================================================================");
*/
        System.out.println("6. Universal----\n");
        methodOfHash = "Universal";
        hash(methodOfHash);
        System.out.println("\n\n=====================================================================================================================");
        System.out.println("=====================================================================================================================");

        System.out.println("7. Perfect----\n");
        methodOfHash = "Perfect";
        hash(methodOfHash);
        System.out.println("\n\n=====================================================================================================================");
        System.out.println("=====================================================================================================================");

        System.out.println("It appears that Perfect hashing seems to be the best with the lowest collisions and " +
                "least unused spaces,\nhowever, due to not knowing if it were 100% implemented correctly, " +
                "the next best ones are Mid-Squares and Division. \nFolding is also efficient, but it's not as consistent" +
                " because it uses random numbers");
    }
}
