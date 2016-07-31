package com.object0r.toortools.helpers;

import com.object0r.toortools.http.HttpHeader;
import com.object0r.toortools.http.HttpHelper;
import com.object0r.toortools.http.HttpRequestInformation;
import com.object0r.toortools.http.HttpResult;
import org.apache.commons.lang.StringEscapeUtils;

import java.util.Vector;

public class CytaSmsHelper
{
    public static boolean sendSms(String message, Vector<String> recipients, String username, String key) throws Exception
    {
        try
        {

            HttpRequestInformation httpRequestInformation = new HttpRequestInformation();
            httpRequestInformation
                    .setMethodPost()
                    .setHeader("Content-Type", "application/xml; charset=\"utf-8\"")
                    .setUrl("https://www.cyta.com.cy/cytamobilevodafone/dev/websmsapi/sendsms.aspx");
            String mobilesText = "<recipients><count>" + recipients.size() + "</count>\n" +
                    "    <mobiles>\n";

            for (String recipient : recipients)
            {
                mobilesText += "      <m>" + StringEscapeUtils.escapeXml(recipient) + "</m>\n";
            }

            mobilesText += "    </mobiles></recipients>";
            String request = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?> \n" +
                    "<websmsapi>\n" +
                    "  <version>1.0</version>\n" +
                    "  <username>" + StringEscapeUtils.escapeXml(username) + "</username>\n" +
                    "  <secretkey>" + StringEscapeUtils.escapeXml(key) + "</secretkey>\n" +
                    mobilesText +
                    "  <message>" + StringEscapeUtils.escapeXml(message) + "</message>\n" +
                    "  <language>en</language>\n" +
                    "</websmsapi>";
            httpRequestInformation.setBody(request);

            HttpResult httpResult = HttpHelper.request(httpRequestInformation);
            if (httpResult.getErrorContent()!=null)
            {
                System.out.println("Error" + httpResult.getErrorContentAsString());
                return false;
            }
            else
            {
                if (httpResult.getContentAsString().contains("<status>0</status>"))
                {

                    return true;
                }
                else
                {
                    System.out.println("Returning false");
                    return false;
                }
            }
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    public static boolean sendSms(String message,String recipient, String username, String key) throws Exception
    {
        Vector v = new Vector<String>();
        v.add(recipient);
        return sendSms(message, v , username, key);
    }
}
