package magma.com.anshan.service;

import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.ws.rs.core.Response;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import magma.com.anshan.dto.CustomerDTO;
import magma.com.anshan.util.JsonUtil;

import static magma.com.anshan.util.HttpConfig.ADDCUSTOMERURI;
import static magma.com.anshan.util.HttpConfig.LOGINCUSTOMERURI;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Administrator on 2016/11/19 0019.
 */

public class CustomerService {

    public static final String TAG = "TAGmagma";


    public Response addCustomer (CustomerDTO customer) throws IOException {
           InputStream is = connection(ADDCUSTOMERURI, customer);
           if (is != null) {
               String content = getStringFromIS(is);
               if (content != null) {
//                   return parseResponse(content);
               } else {
                   Log.e(TAG, "contentS == null");
               }
           } else {
               Log.e(TAG, "is == nullnoConnection");
           }
           return null;
       }

    public String loginCustomer (CustomerDTO customer) throws IOException {
        InputStream is = connection(LOGINCUSTOMERURI, customer);
        String content = getStringFromIS(is);
        if (content != null) {
//                Log.d(TAG, "loginCustomer: return"+content);
            return content;
        } else {
            Log.e(TAG, "contentS == null");
        }

        return null;
    }

    public CustomerDTO loginCust(CustomerDTO customer){
        InputStream is = connection(LOGINCUSTOMERURI, customer);
        if (is != null) {
            CustomerDTO customerFromIs = customerFromIS(is);
            if (customerFromIs != null) {
//                Log.d(TAG, "loginCustomer: return"+content);
                return customerFromIs;
            } else {
                Log.e(TAG, "customerFromIs == null");
            }
        } else {
            Log.e(TAG, "is == nullnoConnectionWhy");
        }
        return null;
    }

    /**
     * 解析服务器返回的JSON数据
     * @param content JSON数据
     * @return Response对象
     */
    private static Response parseResponse(String content) {
        Log.e(TAG, "state======" + content);
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        return JsonUtil.getEntity(content, Response.class);
    }

    private static InputStream connection(String path, CustomerDTO customer) {
        try {
            URL url = new URL(path);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            StringBuffer sb = new StringBuffer();

            if (customer != null) {
                sb.append(customer);
            }
            Gson gson = new Gson();
            String json = gson.toJson(customer);
            Log.d(TAG, "connection: gson" + json);
            byte[] entityData = json.getBytes();
//            Log.d(TAG, "connection: entityData " + entityData);
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            httpConn.setUseCaches(false);
            httpConn.setRequestMethod("POST");
            //设置请求服务器连接的超时时间
            httpConn.setConnectTimeout(5 * 1000);
            //设置服务器返回数据的超时时间
            httpConn.setReadTimeout(5 * 1000);
            httpConn.setRequestProperty("Connection", "Keep-Alive");
            httpConn.setRequestProperty("Content-Type", "application/json");
            httpConn.connect();
            Log.d(TAG, "connection: httpconn"+httpConn);
            DataOutputStream outStream = new DataOutputStream(httpConn.getOutputStream());
            Log.d(TAG, "connection: outstream = " + outStream);
            outStream.write(entityData);
            outStream.flush();
            outStream.close();
            Log.d(TAG, "connection: responsesersrsrsr"+httpConn.getResponseCode());
            int responseCode = httpConn.getResponseCode();
            if (HttpURLConnection.HTTP_OK == responseCode) {
                InputStream is = httpConn.getInputStream();
                Log.d(TAG, "connection: iswefdfsdf" + String.valueOf(is));
                return is;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d(TAG, "connection: Exception"+ex);
            return null;
        }
        return null;
    }

    public static String getStringFromIS(InputStream is) {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            int len = -1;
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            os.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String reString = new String(os.toByteArray());

//        Log.e(TAG, "geStringFromIS reString======" + reString);

        return reString;
    }

    public CustomerDTO customerFromIS(InputStream is){
        JsonReader jsonReader = new JsonReader( new InputStreamReader( is ) );
        CustomerDTO customerFromIS = new Gson().fromJson(jsonReader, CustomerDTO.class);
        Gson gson = new Gson();
        String json = gson.toJson(customerFromIS);
        Log.d(TAG, "connection: Returngson" + json);
        Log.d(TAG, "customerFromIS:+mobile "+ customerFromIS.getMobile() );
        Log.d(TAG, "customerFromIS: " + customerFromIS.toString());
        return customerFromIS;
    }


}
