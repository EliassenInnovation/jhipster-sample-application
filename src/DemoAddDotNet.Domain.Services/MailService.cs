using System.Threading.Tasks;
using DemoAddDotNet.Domain.Entities;
using DemoAddDotNet.Domain.Services.Interfaces;
using Microsoft.Extensions.Options;

namespace DemoAddDotNet.Domain.Services;

public class MailService : IMailService
{
    public MailService()
    {
    }

    // private readonly SecuritySettings _securitySettings;

    // public MailService(IOptions<SecuritySettings> securitySettings)
    // {
    //     _securitySettings = securitySettings.Value;
    // }

    public virtual Task SendPasswordResetMail(User user)
    {
        //TODO send reset Email
        return Task.FromResult(Task.CompletedTask);
    }

    public virtual Task SendActivationEmail(User user)
    {
        //TODO Activation Email
        return Task.FromResult(Task.CompletedTask);
    }

    public virtual Task SendCreationEmail(User user)
    {
        //TODO Creation Email
        return Task.FromResult(Task.CompletedTask);
    }
}
