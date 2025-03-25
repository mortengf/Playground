

public class Program {
    
    // Declare the delegate aka callback function
    public delegate int TheBestDelegate(string message);

    // Implement the delegate/callback function
    public static int PrintTheBestMessage(string message) {
        Console.WriteLine(message + " og delegates er det bedste!");
        return 10;
    }

    public static void Main(string[] args) {
        // Instantiate the delegate - standard syntax
        TheBestDelegate nisseBrusDelegate = new TheBestDelegate(PrintTheBestMessage);

        // Invoke the delegate
        int litersOfNissebrusRemaining = nisseBrusDelegate("Nissebrus");
        Console.WriteLine("Der er " + litersOfNissebrusRemaining + " liter Nissebrus tilbage");

        // Instantiate the delegate - lambda syntax
        TheBestDelegate chiliCheeseRingsDelegate = (message) => { 
            Console.WriteLine(message + " og lambda-style delegates er det bedste!");
            return 8;
        };

        // Invoke the delegate again
        int numberOfChiliCheeseRingsRemaining = chiliCheeseRingsDelegate("Chili-Cheese Rings");
        Console.WriteLine("Der er " + numberOfChiliCheeseRingsRemaining + " poser Chili-Cheese Rings tilbage");        
    }

}

