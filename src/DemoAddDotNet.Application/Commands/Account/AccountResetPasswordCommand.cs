using MediatR;

namespace DemoAddDotNet.Application.Commands;

public class AccountResetPasswordCommand : IRequest<Unit>
{
    public string Mail { get; set; }
}
