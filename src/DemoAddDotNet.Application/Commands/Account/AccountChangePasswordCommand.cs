using MediatR;
using DemoAddDotNet.Dto.Authentication;

namespace DemoAddDotNet.Application.Commands;

public class AccountChangePasswordCommand : IRequest<Unit>
{
    public PasswordChangeDto PasswordChangeDto { get; set; }
}
