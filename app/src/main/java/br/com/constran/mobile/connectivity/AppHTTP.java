package br.com.constran.mobile.connectivity;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import com.google.gson.Gson;

/**
 * Created by moises_santana on 19/05/2015.
 */
public class AppHTTP {

    private static final int READ_TIMEOUT = 15000;
    private static final int CONNECT_TIMEOUT = 15000;

    public static final String POST = "POST";
    public static final String GET = "GET";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";
    public static final String HEAD = "HEAD";

    private URL url = null;
    private HttpURLConnection  conn = null;
    private OutputStream os = null;
    private BufferedWriter writer = null;
    private InputStream is = null;
    private int responseCode = 0;
    private RequestProperty requestProperty = null;



    public AppHTTP(){

    }


    private class RequestProperty{

        protected String key;
        protected String value;

        public RequestProperty(String key, String value){
            this.key = key;
            this.value = value;
        }
    }

    public void setRequestProperty(String key, String value){
        requestProperty = new RequestProperty(key,value);
    }


    public String connect(String method, String url) throws IOException {
        String response = this.request(method,url,null,null);
        return response;
    }

    public String connect(String method, String url, Map<String,String> params) throws IOException {
        String response = this.request(method,url,params,null);
        return response;
    }

    public String connect(String method, String url, Object typeToJson) throws IOException {
        String response = this.request(method,url,null,typeToJson);
        return response;
    }


    public <T> T connect(String method, String url, Class<T> type) throws IOException {
        return type.cast(new Gson().fromJson(request(method,url,null,null),type));
    }

    public <T> T connect(String method, String url, Class<T> type, Map<String,String> params) throws IOException {
        return type.cast(new Gson().fromJson(request(method,url,params,null),type));
    }

    public <T> T connect(String method, String url, Class<T> type, Object objToJson) throws IOException {
        return type.cast(new Gson().fromJson(request(method,url,null,objToJson),type));
    }


    public <T> T connect(String method, String url, Class<T> type, Map<String,String> params,Object objToJson) throws IOException {
        return type.cast(new Gson().fromJson(request(method,url,null,objToJson),type));
    }

    private String getStringFromInputStream() throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        if (br != null) {
            br.close();
        }

        return sb.toString();
    }

	public int getResponseCode() {
		return responseCode;
	}
	
	
	@SuppressWarnings("rawtypes")
	public String getQuery(Map<String, String> map) throws UnsupportedEncodingException {
		
		boolean first = true;
	    Iterator it = map.entrySet().iterator();
	    StringBuilder uri = new StringBuilder();
	    Map.Entry pair = null;
	    
	    while (it.hasNext()) {
	        
	        if(first){
	        	first = false;
	        }else{
	        	uri.append("&");
	        }
	        
	        pair = (Map.Entry)it.next();
	        uri.append(URLEncoder.encode(pair.getKey().toString(),"UTF-8"));
	        uri.append("=");
	        uri.append(URLEncoder.encode(pair.getValue().toString(),"UTF-8"));	        
	    }

	    map.clear();
	    return uri.toString();
	}

    private String request(String method, String url, Map<String,String> params, Object obj) throws IOException {

        if(params != null && method.equals(GET)){
            url = url.concat("?");
            url = url.concat(getQuery(params));
        }

        this.url = new URL(url);
        conn = (HttpURLConnection) this.url.openConnection();

        if(requestProperty != null && (requestProperty.key != null && requestProperty.value != null)){
            conn.setRequestProperty(requestProperty.key,requestProperty.value);
        }

        conn.setRequestMethod(method);
        conn.setReadTimeout(READ_TIMEOUT);
        conn.setConnectTimeout(CONNECT_TIMEOUT);

        if( (params != null || obj != null) && method.equals(POST)){
            os = conn.getOutputStream();
            writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            if(params != null){
                writer.write(getQuery(params));
            }else if(obj != null){
                writer.write(new Gson().toJson(obj));
            }

            writer.flush();
            writer.close();
            os.close();
        }

        conn.connect();
        responseCode = conn.getResponseCode();

        is = conn.getInputStream();
        String contentAsString = getStringFromInputStream();

        return contentAsString;
    }
}
