public class Lab4 {

    // =============================================
    // 1. Sum of n natural numbers using recursion
    // =============================================
    public static int sumOfNaturalNumbers(int n) {
        // Base case: if n is 0 or 1
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        // Recursive case: n + sum of (n-1) natural numbers
        return n + sumOfNaturalNumbers(n - 1);
    }

    // =============================================
    // 2. Reverse a string using recursion
    // =============================================
    public static String reverseString(String str) {
        // Base case: if string is empty or has one character
        if (str == null || str.length() <= 1) {
            return str;
        }
        // Recursive case: last character + reverse of remaining string
        return str.charAt(str.length() - 1) + reverseString(str.substring(0, str.length() - 1));
    }

    // =============================================
    // 3. Power of a number using recursion (base^exponent)
    // =============================================
    public static double power(double base, int exponent) {
        // Handle negative exponent
        if (exponent < 0) {
            return 1.0 / power(base, -exponent);
        }
        
        // Base case: exponent is 0
        if (exponent == 0) {
            return 1;
        }
        
        // Recursive case: base * power(base, exponent-1)
        return base * power(base, exponent - 1);
    }

    // =============================================
    // 4. Convert decimal to binary using recursion
    // =============================================
    public static String decimalToBinary(int decimal) {
        // Handle special case
        if (decimal == 0) {
            return "0";
        }
        // Base case: if decimal is 1
        if (decimal == 1) {
            return "1";
        }
        // Recursive case: decimal/2 + last bit (decimal % 2)
        return decimalToBinary(decimal / 2) + (decimal % 2);
    }

    // =============================================
    // Bonus: Print all methods with examples
    // =============================================
    public static void main(String[] args) {
        System.out.println("======================================");
        System.out.println("LAB 4 - RECURSION DEMONSTRATION");
        System.out.println("======================================\n");

        // ===== Test 1: Sum of n natural numbers =====
        System.out.println("--- 1. Sum of n natural numbers ---");
        int n = 10;
        System.out.println("Sum of first " + n + " natural numbers = " + sumOfNaturalNumbers(n));
        
        n = 5;
        System.out.println("Sum of first " + n + " natural numbers = " + sumOfNaturalNumbers(n));
        
        n = 1;
        System.out.println("Sum of first " + n + " natural number = " + sumOfNaturalNumbers(n));
        
        n = 0;
        System.out.println("Sum of first " + n + " natural numbers = " + sumOfNaturalNumbers(n));
        System.out.println();

        // ===== Test 2: Reverse a string =====
        System.out.println("--- 2. Reverse a string ---");
        String text = "Hello";
        System.out.println("Original: \"" + text + "\" -> Reversed: \"" + reverseString(text) + "\"");
        
        text = "Recursion";
        System.out.println("Original: \"" + text + "\" -> Reversed: \"" + reverseString(text) + "\"");
        
        text = "A";
        System.out.println("Original: \"" + text + "\" -> Reversed: \"" + reverseString(text) + "\"");
        
        text = "";
        System.out.println("Original: \"" + text + "\" -> Reversed: \"" + reverseString(text) + "\"");
        System.out.println();

        // ===== Test 3: Power of a number =====
        System.out.println("--- 3. Power of a number ---");
        double base = 2;
        int exponent = 5;
        System.out.println(base + "^" + exponent + " = " + power(base, exponent));
        
        base = 3;
        exponent = 4;
        System.out.println(base + "^" + exponent + " = " + power(base, exponent));
        
        base = 5;
        exponent = 0;
        System.out.println(base + "^" + exponent + " = " + power(base, exponent));
        
        base = 2;
        exponent = -3;
        System.out.println(base + "^" + exponent + " = " + power(base, exponent));
        System.out.println();

        // ===== Test 4: Decimal to Binary =====
        System.out.println("--- 4. Decimal to Binary ---");
        int decimal = 10;
        System.out.println(decimal + " (decimal) = " + decimalToBinary(decimal) + " (binary)");
        
        decimal = 7;
        System.out.println(decimal + " (decimal) = " + decimalToBinary(decimal) + " (binary)");
        
        decimal = 25;
        System.out.println(decimal + " (decimal) = " + decimalToBinary(decimal) + " (binary)");
        
        decimal = 1;
        System.out.println(decimal + " (decimal) = " + decimalToBinary(decimal) + " (binary)");
        
        decimal = 0;
        System.out.println(decimal + " (decimal) = " + decimalToBinary(decimal) + " (binary)");

        System.out.println("\n======================================");
        System.out.println("RECURSION EXPLANATION FOR EACH METHOD");
        System.out.println("======================================\n");

        // ===== Explanation for Sum =====
        System.out.println("1. Sum of n natural numbers:");
        System.out.println("   Example: sumOfNaturalNumbers(5)");
        System.out.println("   = 5 + sumOfNaturalNumbers(4)");
        System.out.println("   = 5 + 4 + sumOfNaturalNumbers(3)");
        System.out.println("   = 5 + 4 + 3 + sumOfNaturalNumbers(2)");
        System.out.println("   = 5 + 4 + 3 + 2 + sumOfNaturalNumbers(1)");
        System.out.println("   = 5 + 4 + 3 + 2 + 1");
        System.out.println("   = 15\n");

        // ===== Explanation for Reverse =====
        System.out.println("2. Reverse a string:");
        System.out.println("   Example: reverseString(\"Hello\")");
        System.out.println("   = 'o' + reverseString(\"Hell\")");
        System.out.println("   = 'o' + 'l' + reverseString(\"Hel\")");
        System.out.println("   = 'o' + 'l' + 'l' + reverseString(\"He\")");
        System.out.println("   = 'o' + 'l' + 'l' + 'e' + reverseString(\"H\")");
        System.out.println("   = 'o' + 'l' + 'l' + 'e' + 'H'");
        System.out.println("   = \"olleH\"\n");

        // ===== Explanation for Power =====
        System.out.println("3. Power of a number:");
        System.out.println("   Example: power(2, 5)");
        System.out.println("   = 2 * power(2, 4)");
        System.out.println("   = 2 * 2 * power(2, 3)");
        System.out.println("   = 2 * 2 * 2 * power(2, 2)");
        System.out.println("   = 2 * 2 * 2 * 2 * power(2, 1)");
        System.out.println("   = 2 * 2 * 2 * 2 * 2");
        System.out.println("   = 32\n");

        // ===== Explanation for Decimal to Binary =====
        System.out.println("4. Decimal to Binary:");
        System.out.println("   Example: decimalToBinary(10)");
        System.out.println("   = decimalToBinary(5) + '0'");
        System.out.println("   = (decimalToBinary(2) + '1') + '0'");
        System.out.println("   = ((decimalToBinary(1) + '0') + '1') + '0'");
        System.out.println("   = (('1' + '0') + '1') + '0'");
        System.out.println("   = \"1010\"");
    }
}