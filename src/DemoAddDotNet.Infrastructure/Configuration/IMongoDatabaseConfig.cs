namespace DemoAddDotNet.Infrastructure.Configuration;

public interface IMongoDatabaseConfig
{
    string ConnectionString { get; set; }
    string DatabaseName { get; set; }
}
