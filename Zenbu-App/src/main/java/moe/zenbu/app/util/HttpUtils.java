package moe.zenbu.app.util;

import java.util.ArrayList;
import java.util.List;

import moe.zenbu.app.beans.Setting;
import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtils
{
    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    private static CloseableHttpClient httpClient = HttpClients.createDefault();

    public static CloseableHttpResponse executeHttpRequest(HttpUriRequest request)
    {
        if(request == null)
        {
            log.error("Request is null, not executing");
        }

        try
        {
            return httpClient.execute(request);
        }
        catch(Exception e)
        {
            log.error("Could not execute request to {}", request.getURI(), e);
            return null;
        }
    }

    public static HttpPost setEntityQuietly(final HttpPost post, final List<NameValuePair> params)
    {
        try
        {
            post.setEntity(new UrlEncodedFormEntity(params));
        }
        catch(Exception e)
        {
            log.error("Could not set params as entity", e);
        }

        return post;
    }

    public static CloseableHttpClient getHttpClient()
    {
        return httpClient;
    }

    public static String getHbAuthToken(final String authKey)
    {
        log.info("Getting Hb auth token");

        SqlSession db = DbUtils.getSqlSession();

        HttpPost post = new HttpPost("https://hummingbirdv1.p.mashape.com/users/authenticate");
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("username", ((Setting) db.selectOne("db.mappers.settingmapper.selectSetting", "hb_username")).getValue()));
        params.add(new BasicNameValuePair("password", ((Setting) db.selectOne("db.mappers.settingmapper.selectSetting")).getValue()));

        post.addHeader("X-Mashape-Key", authKey);

        String authToken = "";
        try
        {
            post.setEntity(new UrlEncodedFormEntity(params));
            CloseableHttpResponse response = executeHttpRequest(post);
            authToken = IOUtils.toString(response.getEntity().getContent());
            HttpClientUtils.closeQuietly(response);
        }
        catch(Exception e)
        {
            log.error("Could not get Hb auth token", e);
        }

        db.close();

        return authToken.substring(1, authToken.length() - 1);
    }
}
