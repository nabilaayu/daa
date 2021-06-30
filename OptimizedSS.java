import java.util.Arrays;
import java.util.Scanner;

class OptimizedSS {

    // Optimization Algorithm of Edit Distance Based on Column Vector
    static int ld(String strS, String strT) {
        int n = strS.length();
        int m = strT.length();

        if (m == 0) {
            return n;
        } else if (n == 0) {
            return m;
        }

        int[] v0 = new int[m+1];
        int[] v1 = new int[m+1];

        for (int i = 0; i < v0.length; i++) {
            v0[i] = i;
        }

        int cost;
        for (int i = 1; i < n+1; i++) {
            v1[0] = i;

            for (int j = 1; j < m+1; j++) {
                if (strS.charAt(i-1) == strT.charAt(j-1)) {
                    cost = 0;
                } else {
                    cost = 1;
                }
                v1[j] = Math.min(v1[j-1]+1, Math.min(v0[j]+1, v0[j-1]+cost));
            }

            if (i == n) {
                break;
            }

            v0 = v1;
            v1 = new int[m+1];
        }

        return v1[m];
    }

    // Optimization of LCS Algorithm Based on Dimension Reduction
    static int lcs(String strS, String strT) {
        int[] base = new int[strT.length()+1];
        int front = 0;
        int pre = 0;

        for (int i = 1; i < strS.length()+1; i++) {
            for (int j = 1; j < strT.length()+1; j++) {
                if (strS.charAt(i-1) == strT.charAt(j-1)) {
                    pre = base[j-1]+1;
                } else {
                    pre = Math.max(front, base[j]);
                }
                base[j-1] = front;
                front = pre;
                base[strT.length()] = front;
            }
        }

        return base[strT.length()];
    }

    // Compute LCCS
    static int[] lccs(String strS, String strT) {
        int n = strS.length();
        int m = strT.length();
        int[][] L = new int[n+1][m+1];

        int max = 0;
        int firstPosition = 0;
        for (int i = 1; i < n+1; i++) {
            for (int j = 1; j < m+1; j++) {
                if (strS.charAt(i-1) == strT.charAt(j-1)) {
                    L[i][j] = L[i-1][j-1]+1;
                    if (L[i][j]>max) {
                        max = L[i][j];
                        firstPosition = j - max + 1;
                    }        
                } else {
                    L[i][j] = 0;
                }
            }
        }

        return new int[] {max, firstPosition};
    }

    // Optimization of String Similarity Calculation Method Based on The Ld, The Lcs And Lccs
    static float sim(String strS, String strT) {
        float lcs = (float) lcs(strS, strT);
        float ld = (float) ld(strS, strT);
        int[] lccs = lccs(strS, strT);
        float l = (float) lccs[0];
        float p = (float) lccs[1];
        float m = (strS.length()<strT.length())? (float) strS.length() : (float) strT.length();
        float u = (float) 1;

        if (lcs == 0) {
            return 0;
        } else if (ld == 0) {
            return 1;
        } else {
            return lcs/(lcs+ld+p/(l*m)*u);
        }
    }

    // Filter out meaningless symbol characters
    static String filterSign(String str) {
        str = str.replaceAll("[^a-zA-Z0-9]", "");
        return str;
    }
    
    public static void main(String[] args) {
        String strS;
        String strT;

        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter string 1: ");
        String S = sc.nextLine();
        
        System.out.print("Enter string 2: ");
        String T = sc.nextLine();

        if (S.isEmpty() || T.isEmpty()) {
            System.out.println("Either of the strings can't be empty, please try again.");
            return;
        }

        if (S.length() > T.length()) {
            strS = T;
            strT = S;
        } else {
            strS = S;
            strT = T;
        }

        strS = filterSign(strS);
        strT = filterSign(strT);
        
        System.out.printf("The similarity between '%s' and '%s' is %.3f %n", strS, strT, sim(strS, strT));
        
    }
}