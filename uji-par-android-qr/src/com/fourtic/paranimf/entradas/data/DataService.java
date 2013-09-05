package com.fourtic.paranimf.entradas.data;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import android.content.Context;
import android.util.Log;

import com.fourtic.paranimf.entradas.R;
import com.fourtic.paranimf.entradas.constants.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

public class DataService
{
    private static String BASE_SECURE_URL = "http://192.168.3.102:8081/par-public/rest";
    private static String BASE_UNSECURE_URL = BASE_SECURE_URL;

    private AsyncHttpClient client;
    private Gson gson;

    private final Context context;

    public DataService(Context context)
    {
        this.context = context;
        this.client = new AsyncHttpClient();
        this.gson = new Gson();

        client.setTimeout(10000);
        initCookieStore(context);
        initSsl();
    }

    private void initSsl()
    {
        SSLSocketFactory sf = newSslSocketFactory();
        client.setSSLSocketFactory(sf);
    }

    private void initCookieStore(Context context)
    {
        PersistentCookieStore myCookieStore = new PersistentCookieStore(context);
        client.setCookieStore(myCookieStore);
    }

    private SSLSocketFactory newSslSocketFactory()
    {
        try
        {
            KeyStore trusted = KeyStore.getInstance("BKS");
            InputStream in = context.getResources().openRawResource(R.raw.mystore);
            try
            {
                trusted.load(in, "perico".toCharArray());
            }
            catch (Exception e)
            {
                Log.e(Constants.TAG, "Error opening keystore", e);
            }
            finally
            {
                in.close();
            }
            return new SSLSocketFactory(trusted);
        }
        catch (Exception e)
        {
            throw new AssertionError(e);
        }
    }

    private Map<String, Object> createMap(Object... args)
    {
        Map<String, Object> result = new HashMap<String, Object>();

        for (int i = 0; i < args.length; i += 2)
        {
            result.put((String) args[i], args[i + 1]);
        }

        return result;
    }

    private HttpEntity mapToEntity(Map<String, Object> map) throws UnsupportedEncodingException
    {
        return new StringEntity(gson.toJson(map), "UTF-8");
    }

    /*
    public void authenticate(String username, String password, final ResultCallback<Void> responseHandler)
    {
        try
        {
            Map<String, Object> data = createMap("email", username, "contrasenya", password);

            postJSON(BASE_SECURE_URL + "/loginTablet", data, new AsyncHttpResponseHandler(context, true)
            {
                @Override
                public void onSuccess(int status, String response, boolean fromCache)
                {
                    responseHandler.onSuccess(null);
                };

                @Override
                public void onFailure(Throwable error, String errorBody)
                {
                    responseHandler.onError(error, getErrorMessage(errorBody));
                }
            });
        }
        catch (Exception e)
        {
            responseHandler.onError(e, "");
        }
    }
    */

    protected String getErrorMessage(String errorBody)
    {
        try
        {
            return parseErrorBody(errorBody).getError();
        }
        catch (Exception e)
        {
            return "";
        }
    }

    protected RestError parseErrorBody(String errorBody)
    {
        Type collectionType = new TypeToken<RestError>()
        {
        }.getType();

        return gson.fromJson(errorBody, collectionType);
    }

    protected ResponseEventos parseEventos(String json)
    {
        Type collectionType = new TypeToken<ResponseEventos>()
        {
        }.getType();

        return gson.fromJson(json, collectionType);
    }

    public void getEventos(final ResultCallback<List<Evento>> responseHandler)
    {
        get(BASE_SECURE_URL + "/evento", new AsyncHttpResponseHandler(context, true)
        {
            @Override
            public void onSuccess(String result)
            {
                ResponseEventos response = parseEventos(result);
                responseHandler.onSuccess(response.getEventos());
            }

            @Override
            public void onFailure(Throwable error, String errorBody)
            {
                responseHandler.onError(error, getErrorMessage(errorBody));
            }
        });
    }

    private void get(final String url, final AsyncHttpResponseHandler responseHandler)
    {
        client.get(context, url, defaultHeaders(), null, new AsyncHttpResponseHandler(context, true)
        {
            @Override
            public void onSuccess(int status, String result, boolean fromCache)
            {
                responseHandler.onSuccess(status, result, fromCache);

                // Si la respuesta ha venido de caché probamos a hacer una petición sin caché por detrás para obtener datos actualizados
                if (fromCache)
                {
                    client.get(context, url, defaultHeaders(), null, new AsyncHttpResponseHandler(context, false)
                    {
                        @Override
                        public void onSuccess(int status, String result, boolean fromCache)
                        {
                            responseHandler.onSuccess(status, result, fromCache);
                        }

                        @Override
                        public void onFailure(Throwable throwable, String message)
                        {
                            responseHandler.onFailure(throwable, message);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Throwable throwable, String message)
            {
                responseHandler.onFailure(throwable, message);
            }
        });
    }

    private void postJSON(String url, Map<String, Object> data, final AsyncHttpResponseHandler responseHandler)
    {
        try
        {
            HttpEntity entity = mapToEntity(data);

            client.post(context, url, defaultHeaders(), entity, "application/json", responseHandler);
        }
        catch (Exception e)
        {
            responseHandler.onFailure(e, "");
        }
    }

    private void postWithFileUpload(String url, InputStream fileInputStream, String fileName, Map<String, Object> data,
            final AsyncHttpResponseHandler responseHandler)
    {
        try
        {
            RequestParams params = new RequestParams();

            if (data != null)
            {
                for (Entry<String, Object> entry : data.entrySet())
                    params.put(entry.getKey(), (String) entry.getValue());
            }

            if (fileInputStream != null)
                params.put("file", fileInputStream, fileName);

            client.post(context, url, defaultHeaders(), params, null, responseHandler);
        }
        catch (Exception e)
        {
            responseHandler.onFailure(e, "");
        }
    }

    private void delete(String url, final AsyncHttpResponseHandler responseHandler)
    {
        client.delete(context, url, defaultHeaders(), responseHandler);
    }

    private Header[] defaultHeaders()
    {
        return new Header[] { new BasicHeader("X-Requested-With", "XMLHttpRequest") };
    }

    public interface ResultCallback<T>
    {
        public void onSuccess(T successData);

        public void onError(Throwable throwable, String errorMessage);
    }

    /*
    public void saveNewAppointment(String fecha, String hora, String descripcion, int avisar,
            final ResultCallback<Void> responseHandler)
    {
        try
        {
            Map<String, Object> data = createMap("fecha", fecha, "hora", hora, "descripcion", descripcion, "avisar",
                    avisar);

            postJSON(BASE_SECURE_URL + "/citas", data, new AsyncHttpResponseHandler(context, true)
            {
                @Override
                public void onSuccess(int status, String response, boolean fromCache)
                {
                    responseHandler.onSuccess(null);
                };

                @Override
                public void onFailure(Throwable error, String errorBody)
                {
                    responseHandler.onError(error, getErrorMessage(errorBody));
                }
            });
        }
        catch (Exception e)
        {
            responseHandler.onError(e, "");
        }
    }
    */

}
