

public class Program {
    public delegate int TheBestDelegate(string message);

    public static void Main(string[] args) {
        // Standard syntax
        TheBestDelegate myDelegate = new TheBestDelegate(PrintTheBestMessage);
        int literNissebrusTilbage = myDelegate("Nissebrus");
        Console.WriteLine("Der er " + literNissebrusTilbage + " liter Nissebrus tilbage");

        // Lambda syntax
        myDelegate = (message) => { 
            Console.WriteLine(message + " og lambda-style delegates er det bedste!");
            return 8;
        };
        int chiliCheeseRingsTilbage = myDelegate("Chili-Cheese Rings");
        Console.WriteLine("Der er " + chiliCheeseRingsTilbage + " poser Chili-Cheese Rings tilbage");        
    }
    
    public static int PrintTheBestMessage(string message) {
        Console.WriteLine(message + " og delegates er det bedste!");
        return 10;
    }

}

