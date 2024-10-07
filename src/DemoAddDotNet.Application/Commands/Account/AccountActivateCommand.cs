using DemoAddDotNet.Domain.Entities;
using MediatR;

namespace DemoAddDotNet.Application.Commands;

public class AccountActivateCommand : IRequest<User>
{
    public string Key { get; set; }
}
