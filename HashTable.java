import java.sql.SQLOutput;
import java.util.*;

public class HashTable{
    public class Customer {
        public int key; // search key
        public String value; // search value
        public String firstName;
        public String lastName;
        public String id;

        Customer(String str) {
            key = determineHashMethod(str, HashTableTest.methodOfHash);
            // filling up the values between the commas
            Scanner scan = new Scanner(str);
            scan.useDelimiter(",");
            while (scan.hasNext()) {
                lastName = scan.next();
                firstName= scan.next();
                id = scan.next();
            }
            value = lastName + firstName;
        }
        public void printHashEntry(Customer hentry) {
            System.out.print(hentry.value);
        }
        public int getKey() {
            return key;
        }
        public String getLastName (){
            return lastName;
        }
        public String getFirstName() {
            return firstName;
        }
        public String getId() {
            return id;
        }
        public String getValue() {
            return value;
        }
    }

    public final int TABLE_SIZE = 400; // Size of the hash table
    public DList<Customer>[] table = new DList[TABLE_SIZE]; // creating hash table array of doubly linked lists
    public static int elementSize = 0; // should be 512
    public int collisions = 0;
    public int unused = 0;
    public Customer hshEntry; // to use with the Customer class
    public static int perfectCounter = 1; // to use with perfect hash function

    public  HashTable () {
        collisions = 0;
        unused = 0;
        table = new DList[TABLE_SIZE];
    }

    public void insert(String str, String methodOfHash) {
        int hash = determineHashMethod(str, methodOfHash);
        hshEntry = new Customer(str);
        table[hash].Append(hshEntry);
    }

    public int determineHashMethod(String str, String methodOfHash) {
        int hash = 0;
        switch (methodOfHash) {
            case "Division":
                hash = hashFunctionDivision(str);
                break;
            case "Multiplication":
                hash = hashFunctionMultiplication(str);
                break;

            case "Mid-Square":
                hash = hashFunctionMidSquare(str);
                break;

            case "Folding":
                hash = hashFunctionFolding(str);
                break;
                /*
            case "Cryptographic":
                hash = hashFunctionCryptographic(str);
                break;
                */
            case "Universal":
                hash = hashFunctionUniversal(str);
                break;
            case "Perfect":
                hash = hashFunctionPerfect(str);
                break;
            default:
                return hash;
        }
        return hash;
    }

    /*
    hash function with the division method "k mod m", where
    k = key & m = table size
     */
    public int hashFunctionDivision(String key) {
        return getKeyValue(key) % TABLE_SIZE; // using the TABLE_SIZE as the modulus because the example does that.
    }


    /*
    hash function with the multiplication method, "[m(k*A mod 1)]", where
    k = key, A = fractional constant 0 < A < 1, m = table size, & [] = flooring function
     */
    public int hashFunctionMultiplication(String key) {
        double fraction = 0.435737163; // randomly chosen fraction
        return (int) Math.floor(TABLE_SIZE * ((getKeyValue(key) * fraction) % 1)); // using the TABLE_SIZE as the modulus because the example does that.
    }


    /*
    hash function with the mid-squares method. This method first squares the key,
    then it finds the middle 3 values of the key using a string. Finally, the middle 3
    values are combined and parsed into an integer and then % TABLE_SIZE to get the hash value
    */
    public int hashFunctionMidSquare(String key) {
        long keyValue = (long) getKeyValue(key) * getKeyValue(key); // using long data type to avoid possible errors
        String str = "" + keyValue;

        // if the string has an odd number of characters, but we want the middle two still, then add leading zeros
        if (str.length() % 2 != 0) {
            str = "0" + str;
        }
        // calculating the middle two values
        int mid = str.length() / 2;
        String middle;
        String middle2;
        String middle3;
        middle = "" + str.charAt(mid - 1);
        middle2 = "" + str.charAt(mid);
        middle3 = "" + str.charAt(mid+1);
        middle += middle2;
        middle +=middle3;

        int val = Integer.parseInt(middle);

        return val % TABLE_SIZE; // using the TABLE_SIZE as the modulus because the example does that.
    }


    /*
    hash function with the folding method. This method turns the hash value into a string,
    divides the string into two parts, turns the two strings into integers, and then sums them.
    Finally, the hash value is % TABLE_SIZE to get the final result.

    NEW VERSION: This function partitions the string into random sizes and then adds them together to maximize hashing efficiency.
     */
    public int hashFunctionFolding(String key) {
        int hash = getKeyValue(key);
        String str = "" + hash; // converting hash value into string

        // determining the different parts of the hash value and dividing them into random strings
        for (int i = 0; i < str.length(); i += 3) {
            int splitOne = (int) (Math.random() * (str.length()/2));
            int splitTwo = (int) (Math.random() * (str.length()/2)) + str.length()/2;

            String partition = "";
            if (splitOne > splitTwo && splitTwo == str.length()) {
                System.out.println("Changing splits to avoid error");
                splitOne = 0;
                splitTwo = str.length()/2;
            }

            partition = str.substring(splitOne, splitTwo);
            hash += (int) (Math.pow(Integer.parseInt(partition),2));
        }

        return hash % TABLE_SIZE; // using the TABLE_SIZE as the modulus because the example does that.
    }


    /*
    hash function with the universal method. This method uses a universal hash function
    to minimize collisions. The function is: ((a*k+b) mod p) mod m), where a and b are
    randomly chosen constants and p is a prime number greater than m, k is the key, and
    m is the table size
     */
    public int hashFunctionUniversal(String key) {
        int hash = getKeyValue(key);

        /*
        Found a large prime number such that it will not cause an error by knowing that the largest full name is less than 30 letters, and if each name were consisted of
        15 z's then the ascii value would be 122*30 = 3660. The integer max value divided by this number is around 586744. This gives us the upper bound of the prime number that we
        can multiply by to not get an index out of bounds error. The prime number I chose that was 6 digits is 586733
         */
        final int prime = 586733;
        int a = (int) (Math.random()*(prime-1) + 1);
        int b = (int) (Math.random()*prime);
        return ((a*hash+b) % prime) % TABLE_SIZE; // using the TABLE_SIZE as the modulus because the example does that.
    }


    /*
    hash function with the perfect method. This hash function inserts hash values normally, but if a collision were to happen, it moves on to the next empty value. Once all possible indexes
    are filled and each Doubly Linked List is of Length 1, then collisions will occur and so fill in the hash values as normal knowing there is collisions.
     */
    public int hashFunctionPerfect(String key) {
        int hash = getKeyValue(key);
        hash %= TABLE_SIZE; // using the TABLE_SIZE as the modulus because the example does that.

        int count = 0;
        while (!table[hash].IsEmpty() && table[hash].Count() == perfectCounter && count <400) {
            hash = (hash + 1) % TABLE_SIZE;
            count++;
        }

        /*
        checking to see if all indexes are full. If so, then start checking if the Doubly Linked Lists are size
        of perfectSize or greater than 1.
         */
        if (count == 400) {
            perfectCounter++;
        }
        return hash;
    }
    // this function is created to not repeat the same code of acquiring the key value
    public int getKeyValue(String key) {
        int hash = 0;
        for (int i = 0; i < key.length(); i++) {
            hash+= key.charAt(i);
        }
        return hash;
    }

    /* not used
    public String search(String str) {
        int index = determineHashMethod(str, HashTableTest.methodOfHash);
        int length = table[index].Count();
        for (int i = 0; i < length; i++) {
            if (table[index].Search(hshEntry) != null &&
                    table[index].Search(hshEntry).data.getKey() == hshEntry.getKey()) {
                return str;
            }
        }
        return "-----"; // key not found
    }
      */

    // not implemented
    public int hashFunctionCryptographic(String key) {
        return 0;
    }

    public void Output() {
        for (int i = 0; i < table.length; i++) {
            System.out.print(i + ". ");
            if (table[i].Count() == 0) {
                unused++;
                System.out.println("-----");
                continue;
            }
            if (table[i].Count() == 1) {
                System.out.println(table[i].head.data.getValue()+ "\t\t");
                continue;
            }
            DList.Node head = table[i].head;
            while (head != null) {
                System.out.print(((Customer) head.data).getValue()+ "\t\t");
                collisions++;
                head = head.next;
            }

            System.out.println();
        }
    }

    // a different output function that does not print, just calculates
    public void OutputTwo() {
        for (int i = 0; i < table.length; i++) {
            if (table[i].Count() == 0) {
                unused++;
                continue;
            }
            if (table[i].Count() == 1) {
                continue;
            }
            DList.Node head = table[i].head;
            while (head != null) {
                collisions++;
                head = head.next;
            }
        }
    }

    public void statistics() {
        System.out.println("\nStatistics");
        System.out.println("Elements Size:    \t\t" + elementSize);
        System.out.println("HashTable Size:   \t\t" + TABLE_SIZE);
        System.out.println("Collisions:      \t\t" + collisions);
        System.out.println("Unused Positions:\t\t" + unused);

    }


    public static void main(String[] args) throws Exception{

    }
}
