using DemoAddDotNet.Crosscutting.Constants;

namespace DemoAddDotNet.Crosscutting.Exceptions;

public class EmailNotFoundException : BaseException
{
    public EmailNotFoundException() : base(ErrorConstants.EmailNotFoundType, "Email address not registered")
    {
    }
}
