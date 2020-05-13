package pl.lambda.scpcore.utils.exceptions;

import pl.lambda.scpcore.SCPCore;

public class LambdaException extends Exception
{
    public LambdaException(String message)
    {
        SCPCore.getInstance().getLogger().error(message);
    }
}
