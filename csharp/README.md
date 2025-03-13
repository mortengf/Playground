# Prerequisites
Install .NET 9 SDK.

# Compiling Source Code
Since .NET Core 1.0 (June, 2016) it is no longer recommended to run the C# compiler (`csc`) standalone against a single source file (only if low-level compiler control is needed).

Instead, use a project as described below.

# Projects
1. Create the project. 
   * For example, to create a command line (CLI) project: `dotnet new console --name <ProjectName>`
1. Build the project: `dotnet build`
1. Test the project: `dotnet test`
   * Testing a project will automatically build (`restore`) the project beforehand.
1. Run the project: `dotnet run`
   * Running a project will automatically build (`restore`) the project beforehand.

