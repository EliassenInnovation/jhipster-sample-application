using MediatR;
using System.Collections.Generic;

namespace DemoAddDotNet.Application.Queries;

public class UserGetAuthoritiesQuery : IRequest<IEnumerable<string>>
{
}
