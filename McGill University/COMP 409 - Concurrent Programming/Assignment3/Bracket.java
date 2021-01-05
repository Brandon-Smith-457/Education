import java.util.Random;

/* Template code for making a random, sometimes matching bracket sequence. */
public class Bracket  {
    public static int size;
    public static char[] array;

    // Static function to construct the sequence.  Randomization is based on the given seed value.
    public static char[] construct(int _size,long seed) {
        Random r = new Random(seed);
        size = _size; // record size for easier validation later
        array = new char[size];

        int b = 0; // current bracket count

        for (int i=0;i<size;) {
            int c = r.nextInt(3);

            // we choose the char randomly from (,),x, but with some constraints
            switch(c) {
            case 0:
                // don't generate an opening bracket if there's not enough chars left to close it
                if (size-i>b) {
                    array[i] = '[';
                    b++;
                    i++;
                }
                break;
            case 1:
                // dont' generate a closing bracket if there are no open ones pending
                if (b>0) {
                    array[i] = ']';
                    b--;
                    i++;
                }
                break;
            default:
                // don't generate a non-bracket if there's not enough chars left to close the brackets
                if (size-i>b) {
                    array[i] = '*';
                    i++;
                }
            }
        }
        return array;
    }

    // A static, sequential verifier for the sequence.
    public static boolean verify() {
        int b = 0;
        for (int i=0;i<size;i++) {
            if (array[i]=='[') {
                b++;
            } else if (array[i]==']') {
                b--;
                if (b<0) return false;
            }
        }
        if (b!=0)
            return false;
        return true;
    }

    // For debugging (of small arrays), show the array as a string.
    public static void print() {
        System.out.println(new String(array));
    }

    // Just a debug driver stub for testing it
    public static void main(String[] args) {
        try {
            int i = (args.length>0) ? Integer.parseInt(args[0]) : 1000;
            long j = (args.length>1) ? Integer.parseInt(args[1]) : System.currentTimeMillis();
            Bracket.construct(i,j);
            Bracket.print();
            System.out.println(Bracket.verify());
        } catch(NumberFormatException nfe) {
            System.err.println("Error in parsing arguments");
        }
    }
}
