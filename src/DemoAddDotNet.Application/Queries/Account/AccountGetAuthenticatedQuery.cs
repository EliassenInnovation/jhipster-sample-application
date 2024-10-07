using DemoAddDotNet.Domain.Entities;
using MediatR;
using DemoAddDotNet.Dto;
using System.Security.Claims;

namespace DemoAddDotNet.Application.Commands;

public class AccountGetAuthenticatedQuery : IRequest<string>
{
    public ClaimsPrincipal User { get; set; }
}
