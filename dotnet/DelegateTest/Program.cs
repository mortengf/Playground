

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
        TheBestDelegate myDelegate = new TheBestDelegate(PrintTheBestMessage);

        // Invoke the delegate
        int literNissebrusTilbage = myDelegate("Nissebrus");
        Console.WriteLine("Der er " + literNissebrusTilbage + " liter Nissebrus tilbage");

        // Instantiate the delegate - lambda syntax (reuse existing object)
        myDelegate = (message) => { 
            Console.WriteLine(message + " og lambda-style delegates er det bedste!");
            return 8;
        };

        // Invoke the delegate again
        int chiliCheeseRingsTilbage = myDelegate("Chili-Cheese Rings");
        Console.WriteLine("Der er " + chiliCheeseRingsTilbage + " poser Chili-Cheese Rings tilbage");        
    }

}

