using System;
using System.IO;

public class Program
{
    static void Main(string[] args) 
    {
        // The Three Parts of a LINQ Query:
        // 1. Data source.
        int[] numbers = [ 0, 1, 2, 3, 4, 5, 6 ];

        // 2. Query creation.
        // numQuery is an IEnumerable<int>
        var numQuery = from num in numbers
                    where (num % 2) == 0
                    select num;

        // 3. Query execution.
        foreach (int num in numQuery)
        {
            Console.Write("{0,1}\n", num);
        }

        var evenNumQuery = from num in numbers
                   where (num % 2) == 0
                   select num;

        int evenNumCount = evenNumQuery.Count();
        Console.Write($"Number of even numbers: {evenNumCount}\n");

        Console.Write($"Sum of all numbers: {numbers.Sum()}\n");

    }

}

