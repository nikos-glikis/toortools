package com.object0r.toortools;

import com.object0r.toortools.datatypes.exceptions.ReadUrlException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Utilities
{
    public static Connection createSqliteConnection(String path)
    {
        Connection c = null;
        try
        {
            Class.forName("org.sqlite.JDBC");
            if (!new File(new File(path).getParent()).exists())
            {
                new File(new File(path).getParent()).mkdirs();
            }

            c = DriverManager.getConnection("jdbc:sqlite:" + path);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return c;
    }

    public static void downloadFile(String url, String outputPath) throws Exception
    {
        downloadFile(url, outputPath, true);

    }

    public static void downloadFile(String url, String outputPath, boolean createIfNotExists) throws Exception
    {
        URL website = new URL(url);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        if (createIfNotExists)
        {
            if (!new File(outputPath).getParentFile().exists())
            {
                new File(outputPath).getParentFile().mkdirs();
            }
        }
        FileOutputStream fos = new FileOutputStream(outputPath);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    }

    public static String postRequest(String targetURL, String postParams)
    {
        return postRequest(targetURL, postParams, false, "");
    }

    public static String postRequest(String targetURL, String postParams, boolean returnCookies)
    {
        return postRequest(targetURL, postParams, returnCookies, "");
    }

    public static String postRequest(String targetURL, String postParams, boolean returnCookies, String givenCookies)
    {
        URL url;
        HttpURLConnection connection = null;
        try
        {

            // Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            if (givenCookies != null && !givenCookies.equals(""))
            {
                givenCookies = givenCookies.replace("Cookie: ", "");
                connection.setRequestProperty("Cookie", givenCookies);
            }
            connection.setRequestProperty("Content-Length", ""
                    + Integer.toString(postParams.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            if (returnCookies)
            {
                connection.setInstanceFollowRedirects(false);
            }
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            // Send request
            DataOutputStream wr = new DataOutputStream(connection
                    .getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();

            // Get Response
            InputStream is = connection.getInputStream();
            Scanner sc = new Scanner(is);
            StringBuffer response = new StringBuffer();
            while (sc.hasNext())
            {
                response.append(sc.nextLine() + "\n");
            }

            if (!returnCookies)
            {
                return response.toString();
            } else
            {
                String cookies = "";
                Map<String, List<String>> map = connection.getHeaderFields();
                for (Map.Entry<String, List<String>> entry : map.entrySet())
                {
                    //System.out.println(entry.getKey() + "/" + entry.getValue());
                    if (entry.getKey() != null && entry.getKey().equals("Set-Cookie"))
                    {
                        List<String> l = entry.getValue();
                        Iterator<String> iter = l.iterator();
                        for (String c : l)
                        {
                            cookies += c + "; ";
                        }
                    }
                }
                if (cookies.equals(""))
                {
                    return null;
                } else
                {
                    return cookies;
                }
            }

        } catch (Exception e)
        {

            e.printStackTrace();
            System.out.println("e");
            return null;

        } finally
        {

            if (connection != null)
            {
                connection.disconnect();
            }
        }
    }

    public static void writeStringToFile(String filename, String content) throws Exception
    {
        writeBytesToFile(filename, content.getBytes());
    }

    public static void writeBytesToFile(String filename, byte[] content) throws Exception
    {
        FileUtils.writeByteArrayToFile(new File(filename), content);
    }

    static public void trustEverybody()
    {

        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager()
        {
            public X509Certificate[] getAcceptedIssuers()
            {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType)
            {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType)
            {
            }
        }
        };

        // Install the all-trusting trust manager
        try
        {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e)
        {
            ;
        }
        SSLUtilities.trustAllHostnames();
        SSLUtilities.trustAllHttpsCertificates();
    }

    public static String readFile(String path) throws IOException
    {
        return readFileEncoding(path, Charset.forName("UTF-8"));
    }

    public static String readFileEncoding(String path, Charset encoding) throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }


    public static String readUrl(String url, String cookie) throws Exception
    {
        return readUrl(url, cookie, "utf-8");
    }

    public static String readUrl(String url) throws Exception
    {
        return readUrl(url, "", "utf-8");
    }

    public static String readUrl(String url, String cookie, String encoding) throws ReadUrlException
    {

        HttpURLConnection conn = null;
        try
        {
            URL oracle = new URL(url);
            cookie = cookie.replace("Cookie: ", "");
            conn = (HttpURLConnection) oracle.openConnection();
            conn.setRequestProperty("User-Agent", getBrowserUserAgent());
            conn.setRequestProperty("Cookie", cookie);

            StringWriter writer = new StringWriter();
            IOUtils.copy(conn.getInputStream(), writer, encoding);

            return writer.toString();
        } catch (Exception e)
        {
            ReadUrlException readUrlException = new ReadUrlException();
            readUrlException.setPrimaryException(e);

            if (conn != null && conn.getErrorStream() != null)
            {
                try
                {
                    readUrlException.setErrorOutput(IOUtils.toString(conn.getErrorStream(), encoding));
                } catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
            throw readUrlException;
        }
    }

    public static String cut(String from, String to, String t)
    {
        String text = new String(t);
        text = text.substring(text.indexOf(from) + from.length(), text.length());
        text = text.substring(0, text.indexOf(to));
        return text;
    }

    public static String stringToMD5(String data) throws Exception
    {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");

        messageDigest.update(data.getBytes());
        byte[] digest = messageDigest.digest();

        StringBuffer sb = new StringBuffer();
        for (byte b : digest)
        {
            sb.append(Integer.toHexString((int) (b & 0xff)));
        }
        return sb.toString();
    }

    public static String getBrowserUserAgent()
    {
        return "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:45.0) Gecko/20100101 Firefox/45.0";
    }

    public static String getIp()
    {
        return Utilities.getIp(Proxy.NO_PROXY);
    }

    public static  String getIp(Proxy proxy)
    {
        try
        {
            HttpURLConnection connection =(HttpURLConnection)new URL("http://cpanel.com/showip.shtml").openConnection(proxy);
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            StringWriter writer = new StringWriter();
            IOUtils.copy(connection.getInputStream(), writer, "UTF-8");
            return writer.toString();
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
            return null;
        }
    }
}
