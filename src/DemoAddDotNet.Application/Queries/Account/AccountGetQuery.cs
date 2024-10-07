using MediatR;
using DemoAddDotNet.Dto;

namespace DemoAddDotNet.Application.Commands;

public class AccountGetQuery : IRequest<UserDto>
{
}
