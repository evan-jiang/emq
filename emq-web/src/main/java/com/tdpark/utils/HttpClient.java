package com.tdpark.utils;

import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BestMatchSpecFactory;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class HttpClient {

    private CloseableHttpClient client = null;
    private static final int CONNECTTIMEOUT = 10000;
    private static final int READTIMEOUT = 100000;

    private static final Logger logger = LoggerFactory
            .getLogger(HttpClient.class);
    

    private void getHttpClient() {
        CookieStore cookieStore = new BasicCookieStore();

        CookieSpecProvider easySpecProvider = new CookieSpecProvider() {

            @Override
            public CookieSpec create(HttpContext context) {
                return new BrowserCompatSpec() {
                    @Override
                    public void validate(Cookie cookie, CookieOrigin origin)
                            throws MalformedCookieException {
                        // Oh, I am easy
                    }
                };
            }
        };

        Registry<CookieSpecProvider> r = RegistryBuilder
                .<CookieSpecProvider> create()
                .register(CookieSpecs.BEST_MATCH, new BestMatchSpecFactory())
                .register(CookieSpecs.BROWSER_COMPATIBILITY,
                        new BrowserCompatSpecFactory())
                .register("easy", easySpecProvider).build();

        RequestConfig requestConfig = RequestConfig.custom()
                .setCookieSpec("easy").setSocketTimeout(READTIMEOUT)
                .setConnectTimeout(CONNECTTIMEOUT).build();

        CloseableHttpClient client = HttpClients.custom()
                .setDefaultCookieSpecRegistry(r)
                .setDefaultRequestConfig(requestConfig)
                .setDefaultCookieStore(cookieStore).build();

        setClient(client);
    }
    @SuppressWarnings("unchecked")
	public <T> T doGet(String url, Class<T> clazz){
    	String response = null;
        try {
        	HttpGet get = new HttpGet(url);
            if (client == null) {
                getHttpClient();
            }
            response = EntityUtils.toString(client.execute(get).getEntity());
            if(String.class == clazz){
            	return (T) response;
            }
            return new Gson().fromJson(response, clazz);
        } catch (UnsupportedCharsetException e) {
            logger.info("======>{}", e);
            throw new RuntimeException(response);
        } catch (JsonSyntaxException e) {
            logger.info("======>{}", e);
            throw new RuntimeException(response);
        } catch (ClientProtocolException e) {
            logger.info("======>{}", e);
            throw new RuntimeException(response);
        } catch (ParseException e) {
            logger.info("======>{}", e);
            throw new RuntimeException(response);
        } catch (IOException e) {
            logger.info("======>{}", e);
            throw new RuntimeException(response);
        } catch (Exception e) {
            logger.info("======>{}", e);
            throw new RuntimeException(response);
        } finally{
        	if(client != null){
        		try {
					client.close();
				} catch (IOException e) {
					logger.info("======>{}", e);
				}
        	}
        }
    }
    @SuppressWarnings("unchecked")
	public <T> T post(String url, Map<String, String> map, Class<T> clazz) {
    	String response = null;
        try {
            HttpPost post = new HttpPost(url);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
                String key = it.next();
                String value = map.get(key);
                nvps.add(new BasicNameValuePair(key, value));
            }
            post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            if (client == null) {
                getHttpClient();
            }

            response = EntityUtils.toString(client.execute(post)
                    .getEntity());
            if(String.class == clazz){
            	return (T) response;
            }
            return new Gson().fromJson(response, clazz);
        } catch (UnsupportedCharsetException e) {
            logger.info("======>{}", e);
            throw new RuntimeException(response);
        } catch (JsonSyntaxException e) {
            logger.info("======>{}", e);
            throw new RuntimeException(response);
        } catch (ClientProtocolException e) {
            logger.info("======>{}", e);
            throw new RuntimeException(response);
        } catch (ParseException e) {
            logger.info("======>{}", e);
            throw new RuntimeException(response);
        } catch (IOException e) {
            logger.info("======>{}", e);
            throw new RuntimeException(response);
        } catch (Exception e) {
            logger.info("======>{}", e);
            throw new RuntimeException(response);
        } finally{
        	if(client != null){
        		try {
					client.close();
				} catch (IOException e) {
					logger.info("======>{}", e);
				}
        	}
        }
    }

    /**/
    @SuppressWarnings("unchecked")
	public <T> T postJson(String url, Map<String, Object> map, Class<T> clazz) {
    	String response = null;
        try {
        	String json = null;
        	if(map != null){
        		json = new Gson().toJson(map);
        	}
            HttpPost method = new HttpPost(url);
            StringEntity entity = new StringEntity(json, "utf-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            method.setEntity(entity);
            if (client == null) {
                getHttpClient();
            }
            HttpResponse result = client.execute(method);

            response = EntityUtils.toString(result.getEntity());
            if(String.class == clazz){
            	return (T) response;
            }
            return new Gson().fromJson(response, clazz);
        } catch (UnsupportedCharsetException e) {
            logger.info("======>{}", e);
            throw new RuntimeException(response);
        } catch (JsonSyntaxException e) {
            logger.info("======>{}", e);
            throw new RuntimeException(response);
        } catch (ClientProtocolException e) {
            logger.info("======>{}", e);
            throw new RuntimeException(response);
        } catch (ParseException e) {
            logger.info("======>{}", e);
            throw new RuntimeException(response);
        } catch (IOException e) {
            logger.info("======>{}", e);
            throw new RuntimeException(response);
        } catch (Exception e) {
            logger.info("======>{}", e);
            throw new RuntimeException(response);
        }
    }

    public CloseableHttpClient getClient() {
        if (client == null) {
            getHttpClient();
        }

        return client;
    }

    public void setClient(CloseableHttpClient client) {
        this.client = client;
    }

    public static void main(String[] args) {
        /*HttpClient client = new HttpClient();
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", "100089135");
        params.put("uid", "pt1449028219165-15420541");
        params.put("scope", "MOVIE");
        params.put("orderNo", "[from new coupon system]");
        params.put("markPrice", "10000");
        params.put("price", "10000");
        params.put("productBiz", "10000");*/
    }
}
