using System.Threading.Tasks;
using DemoAddDotNet.Domain.Entities;

namespace DemoAddDotNet.Domain.Services.Interfaces;

public interface IMailService
{
    Task SendPasswordResetMail(User user);
    Task SendActivationEmail(User user);
    Task SendCreationEmail(User user);
}
