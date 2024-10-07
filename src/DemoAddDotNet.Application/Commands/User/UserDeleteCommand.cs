using DemoAddDotNet.Domain.Entities;
using MediatR;

namespace DemoAddDotNet.Application.Commands;

public class UserDeleteCommand : IRequest<Unit>
{
    public string Login { get; set; }
}
