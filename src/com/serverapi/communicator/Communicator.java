package com.serverapi.communicator;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created on 07/11/2013
 * 
 * @author Daniel Abitbul <abitbul6@gmail.com>
 * @author Omer Shpigelman <omer.shpigelman@gmail.com>
 * 
 */

public class Communicator {

  public static String execute(String... strs) {
    String $ = "";
    try {
      URL url = new URL("https://android-236504-i.appspot.com/" + strs[0]);
      String param = encodeParams(strs);
      HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
      con.setDoOutput(true);
      con.setRequestMethod("POST");
      con.setFixedLengthStreamingMode(param.getBytes().length);
      con.setRequestProperty("Content-Type",
          "application/x-www-form-urlencoded");
      con.addRequestProperty("Auth", "123");
      PrintWriter out = new PrintWriter(con.getOutputStream());
      out.print(param);
      out.close();
      Scanner inStream = new Scanner(con.getInputStream());
      while (inStream.hasNextLine())
        $ += inStream.nextLine();
      inStream.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return $;
  }

  private static String encodeParams(String[] strs)
      throws UnsupportedEncodingException {
    String $ = "";
    int len = strs.length;
    for (int i = 1; i < len; i += 2) {
      $ += $ + strs[i] + "=" + URLEncoder.encode(strs[i + 1], "UTF-8");
      $ = len > i + 2 ? $ + "&" : $;
    }
    return $;
  }

}
