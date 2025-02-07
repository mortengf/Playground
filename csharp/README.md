# Prerequisites
Install .NET 9 SDK.

# Create a Project
Since .NET version ? it is no longer possible - or at least recommended - to run the C# compiler (`csc`) standalone against a single source file.

Instead, create a project. For example, to create a command line (CLI) project:

```
mkdir <ProjectName>
dotnet new console
```

# Build
In the project folder:

```
dotnet build
```

# Run
In the project folder:

```
dotnet run
```

The project is automatically built beforehand.