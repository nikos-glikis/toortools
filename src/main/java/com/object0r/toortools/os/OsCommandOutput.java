package com.object0r.toortools.os;


public class OsCommandOutput
{
    String standardOutput;
    String errorOutput;
    int exitCode;


    public OsCommandOutput(String s, String e)
    {
        this.standardOutput = s;
        this.errorOutput = e;
    }
    public OsCommandOutput()
    {

    }
    public boolean hasError()
    {
        return exitCode != 0;
    }

    public int getExitCode()
    {
        return exitCode;
    }

    public void setExitCode(int exitCode)
    {
        this.exitCode = exitCode;
    }

    public String getStandardOutput()
    {
        return standardOutput;
    }

    public void setStandardOutput(String standardOutput)
    {
        this.standardOutput = standardOutput;
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
