using DemoAddDotNet.Domain.Entities;
using MediatR;
using DemoAddDotNet.Dto;

namespace DemoAddDotNet.Application.Commands;

public class UserUpdateCommand : IRequest<User>
{
    public UserDto UserDto { get; set; }
}
