using DemoAddDotNet.Infrastructure.Data;
using DemoAddDotNet.Domain.Entities;
using DemoAddDotNet.Test.Setup;

namespace DemoAddDotNet.Test;

public static class Fixme
{
    public static User ReloadUser<TEntryPoint>(AppWebApplicationFactory<TEntryPoint> factory, User user)
        where TEntryPoint : class, IStartup, new()
    {
        var applicationDatabaseContext = factory.GetRequiredService<ApplicationDatabaseContext>();
        applicationDatabaseContext.Entry(user).Reload();
        return user;
    }
}
