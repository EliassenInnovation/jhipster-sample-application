using AutoMapper;
using System.Linq;
using DemoAddDotNet.Domain.Entities;
using DemoAddDotNet.Dto;
using MediatR;
using System.Threading;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;

namespace DemoAddDotNet.Application.Queries;

public class UserGetQueryHandler : IRequestHandler<UserGetQuery, UserDto>
{
    private readonly UserManager<User> _userManager;
    private readonly IMapper _mapper;

    public UserGetQueryHandler(UserManager<User> userManager, IMapper mapper)
    {
        _userManager = userManager;
        _mapper = mapper;
    }

    public async Task<UserDto> Handle(UserGetQuery request, CancellationToken cancellationToken)
    {
        var result = await _userManager.Users
            .Where(user => user.Login == request.Login)
            .Include(it => it.UserRoles)
            .ThenInclude(r => r.Role)
            .SingleOrDefaultAsync();
        return _mapper.Map<UserDto>(result);
    }
}
