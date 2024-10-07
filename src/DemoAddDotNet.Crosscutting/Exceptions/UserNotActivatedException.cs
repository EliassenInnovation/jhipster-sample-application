using System.Security.Authentication;

namespace DemoAddDotNet.Crosscutting.Exceptions;

public class UserNotActivatedException : AuthenticationException
{
    public UserNotActivatedException(string message) : base(message)
    {
    }
}
