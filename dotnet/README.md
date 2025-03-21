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

# Timeline
See https://github.com/ibrahimatay/Csharp-Features for a very nice timeline of C# and .NET, including which features were released in which versions.

# Java <> C# / .NET Map
[Generated](https://chatgpt.com/share/67dd5cfd-4364-8007-98ed-ffd5b26bd70d) by ChatGPT and refined by a human (me) :)

| Feature                                          | Java Ecosystem                    | C# / .NET Ecosystem          |
|------------------------------------------------- |-----------------------------------|------------------------------|
| **Compiler**                                     | `javac` (OpenJDK/Oracle)          | `csc` (Roslyn) / `dotnet`    |
| **Runtime**                                      | JVM (HotSpot, OpenJ9, GraalVM)    | .NET CLR / CoreCLR           |
| **Unit Test Framework**                          | JUnit, TestNG                     | xUnit, NUnit, MSTest         |
| **Mocking Framework**                            | Mockito, EasyMock                 | Moq, FakeItEasy              |
| **Dependency/Package Management / Build Tool**   | Maven / Gradle                    | NuGet / MSBuild              |
| **Build Server**                                 | Jenkins, TeamCity, GitHub Actions | Azure DevOps, TeamCity, GitHub Actions |
| **Containerization & orchestration**             | Docker, Kubernetes                | Docker, Kubernetes           |
| **Code Quality / Static Analysis**               | SonarQube, Checkstyle, PMD        | SonarQube, Roslyn Analyzers, FxCop |
| **Web Framework**                                | Spring, Jakarta EE, Micronaut     | ASP.NET Core                 |
| **ORM (Object-Relational Mapper)**               | Hibernate, JPA, MyBatis           | Entity Framework Core, Dapper |
| **Logging Framework**                            | SLF4J, Log4j, Logback             | Serilog, NLog, log4net       |
| **Dependency Injection**                         | Spring, Guice, CDI                | ASP.NET Core DI, Autofac     |
| **Concurrency / Parallelism**                    | Executors, ForkJoinPool, Akka     | `Task`, `async/await`, TPL   |
| **Serialization**                                | Jackson, Gson, Protobuf           | Newtonsoft.Json, System.Text.Json, Protobuf |
| **Profiling & Monitoring**                       | JVisualVM, JFR, YourKit           | dotTrace, PerfView           |
| **GraphQL**                                      | GraphQL Java, DGS Framework       | HotChocolate, GraphQL.NET    |
| **Message Queuing**                              | Kafka, RabbitMQ, ActiveMQ         | RabbitMQ, Azure Service Bus  |
