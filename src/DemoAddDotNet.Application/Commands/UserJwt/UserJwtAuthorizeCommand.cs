using MediatR;
using DemoAddDotNet.Dto.Authentication;
using System.Security.Principal;

namespace DemoAddDotNet.Application.Commands;

public class UserJwtAuthorizeCommand : IRequest<IPrincipal>
{
    public LoginDto LoginDto { get; set; }
}
