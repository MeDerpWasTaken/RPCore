package me.mederp.rpcore.utils.exceptions;

import me.mederp.rpcore.RPCore;

public class LambdaException extends Exception
{
    public LambdaException(String message)
    {
        RPCore.getInstance().getLogger().error(message);
    }
}
