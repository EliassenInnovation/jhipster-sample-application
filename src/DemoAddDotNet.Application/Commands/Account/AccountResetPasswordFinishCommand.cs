using DemoAddDotNet.Domain.Entities;
using MediatR;
using DemoAddDotNet.Dto.Authentication;

namespace DemoAddDotNet.Application.Commands;

public class AccountResetPasswordFinishCommand : IRequest<User>
{
    public KeyAndPasswordDto KeyAndPasswordDto { get; set; }
}
