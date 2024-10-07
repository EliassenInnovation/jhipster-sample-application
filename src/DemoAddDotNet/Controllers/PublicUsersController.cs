using MediatR;
using AutoMapper;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using JHipsterNet.Core.Pagination;
using JHipsterNet.Core.Pagination.Extensions;
using DemoAddDotNet.Domain.Entities;
using DemoAddDotNet.Security;
using DemoAddDotNet.Domain.Services.Interfaces;
using DemoAddDotNet.Dto;
using DemoAddDotNet.Web.Extensions;
using DemoAddDotNet.Web.Rest.Utilities;
using DemoAddDotNet.Crosscutting.Constants;
using DemoAddDotNet.Crosscutting.Exceptions;
using DemoAddDotNet.Infrastructure.Web.Rest.Utilities;
using DemoAddDotNet.Application.Queries;
using DemoAddDotNet.Application.Commands;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;

namespace DemoAddDotNet.Controllers;


[Route("api/users")]
[ApiController]
public class PublicUsersController : ControllerBase
{
    private readonly ILogger<UsersController> _log;
    private readonly IMediator _mediator;

    public PublicUsersController(ILogger<UsersController> log, IMediator mediator)
    {
        _log = log;
        _mediator = mediator;
    }


    [HttpGet]
    public async Task<ActionResult<IEnumerable<UserDto>>> GetAllPublicUsers(IPageable pageable)
    {
        _log.LogDebug("REST request to get a page of Users");
        (var headers, var userDtos) = await _mediator.Send(new UserGetAllPublicUsersQuery { Page = pageable });
        return Ok(userDtos).WithHeaders(headers);
    }

}
