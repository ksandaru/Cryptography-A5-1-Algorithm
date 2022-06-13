//------------------------------------------------
// ---Implementation of cryptography using A5/1---
//----------Encryption - Decryption---------------
//-------EG/2017/3172--Bandara-B.M.K.S------------

import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

//Main function:
// calls the encryption method >> encrypt()
// calls the decryption method >> decrypt()
public class Main {
    public static void main(String[] args) {
        int tmp = 0;
        String put = null;
        boolean flag = true;
        float selection = 0;
        Scanner scan1 = new Scanner(System.in);
        Scanner scan2 = new Scanner(System.in);


        // Repeat encryption and decryption
        do {
            // char array to store cipher text
            char[] citext = new char[64];
            // char array to store plain text
            char[] pltext = new char[64];
            // int array to store the key-stream
            int[] keystream = new int[64];
            System.out.println("═════Name Encryption Operation═════");
            System.out.print("Insert Your Name in PlainText: ");
            put = scan2.nextLine();
            encrypt(tmp, put, citext, pltext, keystream);
            System.out.print("Do you want to decrypt this CipherText?\n1.Yes\n2.No\n3.Exit\n: ");

            //Selection 1: Decrypt with the same key-stream.
            //Selection 2: The previously used key-stream is initialized,and a new encryption is started.
            //Selection 3: Exits the program
            try {
                selection = scan1.nextFloat();
                if (selection == 1) {
                    System.out.println("\n═════Decryption of CipherText═════");
                    decrypt(tmp, citext, pltext, keystream);
                    continue;
                } else if (selection == 2) {
                    System.out.println("You can Encrypt another PlainText. Thank you!");
                    continue;
                } else
                    System.out.println("=====ComeBack Again=====");
                flag = false;
            } catch (NoSuchElementException | IllegalArgumentException e) {
                System.out.println("Error: " + e);
                scan1.next();
            }
        } while (flag);
        scan1.close();
        scan2.close();
    }

    // x, y, z register initialization
    private static void REG_init(int[] x, int[] y, int[] z) {
        Random rnd = new Random();
        for (int i = 0; i < 19; i++) x[i] = rnd.nextInt(2);
        for (int i = 0; i < 22; i++) y[i] = rnd.nextInt(2);
        for (int i = 0; i < 23; i++) z[i] = rnd.nextInt(2);
        System.out.print("x :");
        for (int i = 0; i < 19; i++) System.out.print(x[i]);
        System.out.println("");
        System.out.print("y :");
        for (int i = 0; i < 22; i++) System.out.print(y[i]);
        System.out.println("");
        System.out.print("z :");
        for (int i = 0; i < 23; i++) System.out.print(z[i]);
        System.out.println("");
    }

    // encryption method
    private static void encrypt(int tmp, String put, char[] citext, char[] pltext, int[] keystream) {
        //generate key stream using three shift register arrays (64bit)
        // x register
        int[] x = new int[19];
        // y register
        int[] y = new int[22];
        // z register
        int[] z = new int[23];
        int m, t;

        pltext = put.toCharArray();
        System.out.println("");
        REG_init(x, y, z);

        //XOR operation
        for (int i = 0; i < pltext.length; i++) {
            m = maj(x[8], y[10], z[10]);
            if (x[8] == m) {
                t = x[13] ^ x[16] ^ x[17] ^ x[18];
                System.arraycopy(x, 0, x, 1, 18);
                x[0] = t;
            }

            if (y[10] == m) {
                t = y[20] ^ y[21];
                System.arraycopy(y, 0, y, 1, 21);
                y[0] = t;
            }

            if (z[10] == m) {
                t = z[7] ^ z[20] ^ z[21] ^ z[22];
                System.arraycopy(z, 0, z, 1, 22);
                z[0] = t;
            }
            keystream[i] = x[18] ^ y[21] ^ z[22];
        }

        for (int i = 0; i < pltext.length; i++) {
            tmp = (int) (pltext[i]);
            citext[i] = (char) (tmp ^ keystream[i]);
        }
        // key-stream bit output
        System.out.print("key-stream : ");
        for (int i = 0; i < pltext.length; i++) System.out.print(keystream[i]);
        // Cipher text output
        System.out.println("\n" + "Cipher Text : ");
        System.out.println(String.valueOf(citext) + "\n");
    }

    // decryption method
    private static void decrypt(int tmp, char[] citext, char[] pltext, int[] keystream) {
        for (int i = 0; i < citext.length; i++) {
            tmp = (int) (citext[i]);
            // Key-stream and character XOR operation
            pltext[i] = (char) (tmp ^ keystream[i]);
        }

        System.out.println("Plain Text : ");
        System.out.println(String.valueOf(pltext) + "\n\n\n");
    }

    // Method to select the largest number of 1s and 0s
    private static int maj(int x, int y, int z) {
        int cnt0 = 0, cnt1 = 0;

        if (x == 0) cnt0 += 1;
        else cnt1 += 1;
        if (y == 0) cnt0 += 1;
        else cnt1 += 1;
        if (z == 0) cnt0 += 1;
        else cnt1 += 1;
        if (cnt0 > cnt1) return 0;
        else return 1;
    }
}