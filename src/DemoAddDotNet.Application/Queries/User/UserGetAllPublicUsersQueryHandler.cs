using AutoMapper;
using System.Linq;
using DemoAddDotNet.Domain.Entities;
using DemoAddDotNet.Dto;
using DemoAddDotNet.Domain.Services.Interfaces;
using DemoAddDotNet.Infrastructure.Web.Rest.Utilities;
using MediatR;
using System.Threading;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Microsoft.AspNetCore.Http;
using System.Collections.Generic;
using JHipsterNet.Core.Pagination.Extensions;

namespace DemoAddDotNet.Application.Queries;

public class UserGetAllPublicUsersQueryHandler : IRequestHandler<UserGetAllPublicUsersQuery, (IHeaderDictionary, IEnumerable<UserDto>)>
{
    private readonly UserManager<User> _userManager;
    private readonly IMapper _mapper;

    public UserGetAllPublicUsersQueryHandler(UserManager<User> userManager, IUserService userService,
        IMapper mapper, IMailService mailService)
    {
        _userManager = userManager;
        _mapper = mapper;
    }

    public async Task<(IHeaderDictionary, IEnumerable<UserDto>)> Handle(UserGetAllPublicUsersQuery request, CancellationToken cancellationToken)
    {
        var page = await _userManager.Users
            .Where(it => it.Activated == true && !string.IsNullOrEmpty(it.Id))
            .UsePageableAsync(request.Page);
        var userDtos = page.Content.Select(user => _mapper.Map<UserDto>(user));
        var headers = page.GeneratePaginationHttpHeaders();
        return (headers, userDtos);
    }
}
