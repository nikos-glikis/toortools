package com.object0r.toortools.os;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class FileHelper
{
    public static String getFileMd5(String filename) throws Exception
    {
        FileInputStream fis = new FileInputStream(new File(filename));
        String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(fis);
        fis.close();
        return md5;
    }

    public static long getFileSize(String filename) throws FileNotFoundException
    {
        File f = new File(filename);
        if (!f.exists())
        {
            throw new FileNotFoundException("File does not exist. ");
        }
        return f.length();
    }
}
