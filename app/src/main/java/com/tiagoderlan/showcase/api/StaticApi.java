package com.tiagoderlan.showcase.api;

import com.tiagoderlan.showcase.models.Category;
import com.tiagoderlan.showcase.models.collections.CategoryCollection;
import com.tiagoderlan.showcase.models.collections.ProductCollection;
import com.tiagoderlan.showcase.utils.Strings;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Tiago on 28/07/2016.
 */
public class StaticApi
{
    private static final int IO_BUFFER_SIZE = 10240;

    private static byte[] buffer = new byte[IO_BUFFER_SIZE];

    public static ProductCollection getProductCollection() throws JSONException
    {
        String url = "https://gist.githubusercontent.com/ronanrodrigo/b95b75cfddc6b1cb601d7f806859e1dc/raw/dc973df65664f6997eeba30158d838c4b716204c/products.json";

        String jsondata = requestUrl(url);

        ProductCollection result = new ProductCollection();

        JSONArray array = new JSONArray(jsondata);

        result.fromJson(array);

        return result;
    }

    public static CategoryCollection getCategoryCollection() throws JSONException
    {
        String url = "https://gist.githubusercontent.com/ronanrodrigo/e84d0d969613fd0ef8f9fd08546f7155/raw/a0611f7e765fa2b745ad9a897296e082a3987f61/categories.json";

        String jsondata = requestUrl(url);

        CategoryCollection result = new CategoryCollection();

        JSONArray array = new JSONArray(jsondata);

        result.fromJson(array);

        return result;
    }

    public static byte[] downloadUrlData(String url)  throws Exception
    {
        if(!Strings.isNullOrEmpty(url))
        {
            byte[] data = downloadData(url);

            if (data != null)
            {
               return data;
            }
        }

        return null;
    }

    private static byte[] downloadData(String url) throws Exception
    {
        InputStream in = new BufferedInputStream(new URL(url).openStream(), IO_BUFFER_SIZE);

        ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
        BufferedOutputStream out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);

        int read = 0;

        while ((read = in.read(buffer)) > 0)
            out.write(buffer, 0, read);

        out.flush();

        byte[] data = dataStream.toByteArray();

        in.close();

        out.close();

        return data;
    }


    private static String requestUrl(String url)
    {
        HttpsURLConnection urlConnection = null;

        try
        {
            URL urlToRequest = new URL(url);
            urlConnection = (HttpsURLConnection) urlToRequest.openConnection();

            urlConnection.setConnectTimeout(500000);
            urlConnection.setReadTimeout(500000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(false);

            urlConnection.connect();

            int statusCode = urlConnection.getResponseCode();

            if (statusCode != HttpURLConnection.HTTP_OK)
            {
                return null;
            }

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            String value = convertInputStreamToString(in);

            return value;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        finally
        {
            if (urlConnection != null)
            {
                urlConnection.disconnect();
            }
        }

        return null;

    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException
    {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";

        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }
}
