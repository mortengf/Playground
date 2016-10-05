using System;
using System.IO;

public class CalculateMaximumPathLength
{
	static string _MaxPath = "";

	static void Main(string[] args)
	{
		RecursePath(@args[0]);
		Console.WriteLine("Maximum path length is " + _MaxPath.Length);
		Console.WriteLine(_MaxPath);
		Console.ReadLine();
	}

	static void RecursePath(string p)
	{
		foreach (string d in Directory.GetDirectories(p))
		{
			try {
				bool ValidPath = IsValidPath(d);
				
				if (ValidPath)
				{
					foreach (string f in Directory.GetFiles(d))
					{
						if (f.Length > _MaxPath.Length)
						{
							_MaxPath = f;
						}
					}
					RecursePath(d);
				}
					
			} catch (PathTooLongException ex) {
				Console.WriteLine("Directory/file " + d + " is too long!");
			}		
		}
	}

	static bool IsValidPath(string p)
	{
		if ((File.GetAttributes(p) & FileAttributes.ReparsePoint) == FileAttributes.ReparsePoint)
		{
			//Console.WriteLine("'" + p + "' is a reparse point. Skipped");
			return false;
		}
		if (!IsReadable(p))
		{
			//Console.WriteLine("'" + p + "' *ACCESS DENIED*. Skipped");
			return false;
		} 
		return true;
	}

	static bool IsReadable(string p)
	{
		try
		{
			string[] s = Directory.GetDirectories(p);            
		}
		catch (UnauthorizedAccessException ex)
		{
			//Console.WriteLine("UnauthorizedAccessException " + ex);
			return false;
		}
		return true;
	}
}