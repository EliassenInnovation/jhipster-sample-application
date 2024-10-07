using DemoAddDotNet.Dto;
using MediatR;

namespace DemoAddDotNet.Application.Queries;

public class UserGetQuery : IRequest<UserDto>
{
    public string Login { get; set; }
}
