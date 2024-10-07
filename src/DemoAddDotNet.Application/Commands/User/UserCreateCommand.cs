using DemoAddDotNet.Domain.Entities;
using MediatR;
using DemoAddDotNet.Dto;

namespace DemoAddDotNet.Application.Commands;

public class UserCreateCommand : IRequest<User>
{
    public UserDto UserDto { get; set; }
}
