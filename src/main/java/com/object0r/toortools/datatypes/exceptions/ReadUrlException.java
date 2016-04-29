package com.object0r.toortools.datatypes.exceptions;

public class ReadUrlException extends Exception
{
    private Exception primaryException;

    public String errorOutput;


    public Exception getPrimaryException()
    {
        return primaryException;
    }

    public void setPrimaryException(Exception primaryException)
    {
        this.primaryException = primaryException;
    }

    public String getErrorOutput()
    {
        return errorOutput;
    }

    public void setErrorOutput(String errorOutput)
    {
        this.errorOutput = errorOutput;
    }
}
