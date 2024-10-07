using DemoAddDotNet.Crosscutting.Constants;

namespace DemoAddDotNet.Crosscutting.Exceptions;

public class InternalServerErrorException : BaseException
{
    public InternalServerErrorException(string message) : base(ErrorConstants.DefaultType, message)
    {
    }
}
