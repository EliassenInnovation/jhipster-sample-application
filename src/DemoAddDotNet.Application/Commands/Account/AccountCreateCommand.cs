using DemoAddDotNet.Domain.Entities;
using MediatR;
using DemoAddDotNet.Dto;

namespace DemoAddDotNet.Application.Commands;

public class AccountCreateCommand : IRequest<User>
{
    public ManagedUserDto ManagedUserDto { get; set; }
}
