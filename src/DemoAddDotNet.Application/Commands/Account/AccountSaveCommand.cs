using MediatR;
using DemoAddDotNet.Dto;
using System.Security.Claims;

namespace DemoAddDotNet.Application.Commands;

public class AccountSaveCommand : IRequest<Unit>
{
    public ClaimsPrincipal User { get; set; }
    public UserDto UserDto { get; set; }
}
