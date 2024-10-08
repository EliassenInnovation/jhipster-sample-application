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
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using Microsoft.EntityFrameworkCore;

namespace DemoAddDotNet.Controllers;

[Authorize]
[Route("api/admin/[controller]")]
[ApiController]
public class UsersController : ControllerBase
{
    private readonly ILogger<UsersController> _log;
    private readonly IMediator _mediator;

    public UsersController(ILogger<UsersController> log, IMediator mediator)
    {
        _log = log;
        _mediator = mediator;
    }

    [HttpPost]
    public async Task<ActionResult<User>> CreateUser([FromBody] UserDto userDto)
    {
        _log.LogDebug($"REST request to save User : {userDto}");
        if (!string.IsNullOrEmpty(userDto.Id))
            throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement",
                "idexists");
        var newUser = await _mediator.Send(new UserCreateCommand { UserDto = userDto });
        return CreatedAtAction(nameof(GetUser), new { login = newUser.Login }, newUser)
            .WithHeaders(HeaderUtil.CreateEntityCreationAlert("userManagement.created", newUser.Login));
    }

    [HttpPut]
    public async Task<IActionResult> UpdateUser([FromBody] UserDto userDto)
    {
        _log.LogDebug($"REST request to update User : {userDto}");
        var updatedUser = await _mediator.Send(new UserUpdateCommand { UserDto = userDto });

        return ActionResultUtil.WrapOrNotFound(updatedUser)
            .WithHeaders(HeaderUtil.CreateAlert("userManagement.updated", userDto.Login));
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> UpdateUser(string id, [FromBody] UserDto userDto)
    {
        return await UpdateUser(userDto);
    }

    [HttpGet]
    public async Task<ActionResult<IEnumerable<User>>> GetAllUsers(IPageable pageable)
    {
        _log.LogDebug("REST request to get a page of Users");
        (var headers, var userDtos) = await _mediator.Send(new UserGetAllQuery { Page = pageable });
        return Ok(userDtos).WithHeaders(headers);
    }

    [HttpGet("authorities")]
    [Authorize(Roles = RolesConstants.ADMIN)]
    public async Task<ActionResult<IEnumerable<string>>> GetAuthorities()
    {
        return Ok(await _mediator.Send(new UserGetAuthoritiesQuery()));
    }

    [HttpGet("{login}")]
    public async Task<IActionResult> GetUser([FromRoute] string login)
    {
        _log.LogDebug($"REST request to get User : {login}");
        var userDto = await _mediator.Send(new UserGetQuery { Login = login });
        return ActionResultUtil.WrapOrNotFound(userDto);
    }

    [HttpDelete("{login}")]
    [Authorize(Roles = RolesConstants.ADMIN)]
    public async Task<IActionResult> DeleteUser([FromRoute] string login)
    {
        _log.LogDebug($"REST request to delete User : {login}");
        await _mediator.Send(new UserDeleteCommand { Login = login });
        return NoContent().WithHeaders(HeaderUtil.CreateEntityDeletionAlert("userManagement.deleted", login));
    }
}
