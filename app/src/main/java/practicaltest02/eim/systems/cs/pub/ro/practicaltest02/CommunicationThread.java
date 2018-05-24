package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;
import cz.msebera.android.httpclient.util.EntityUtils;

public class CommunicationThread extends Thread {

    private MyServerThread serverThread;
    private Socket socket;
    private EditText serverTextEditText;
    private String link = "https://www.wunderground.com/cgi-bin/findweather/getForecast?query=";
    private Spinner dropdown;
    private String city;
    private String informationType;
    String retStr;

    public CommunicationThread(Socket socket, EditText serverTextEditText, Spinner dropdown, MyServerThread serverThread) {
        this.socket = socket;
        this.serverTextEditText = serverTextEditText;
        this.dropdown = dropdown;
        this.serverThread = serverThread;

    }

    @Override
    public void run() {
        try {
            Log.v(Constants.TAG, "Connection opened with " + socket.getInetAddress() + ":" + socket.getLocalPort());
            PrintWriter printWriter = Utilities.getWriter(socket);
            BufferedReader bufferedReader = Utilities.getReader(socket);


            city = bufferedReader.readLine();
            informationType = bufferedReader.readLine();




            String msg = getInfo();
 //           printWriter.println(retStr);
            //printWriter.println(getInfo());
            socket.close();
            Log.v(Constants.TAG, "Conenction closed");
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }

    public String getInfo() {
        this.retStr = "";
        String humidity = "";
        String temperature = "";
        if (!(informationType).equals("POST")) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGetXKCD = new HttpGet(link + city);//serverTextEditText.getText().toString());
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String pageSourceCode = null;
            Document document = new Document("Page empty");
            Element htmlTag = new Element("Empty");
            HttpResponse httpResponse;
            try {
                pageSourceCode = httpClient.execute(httpGetXKCD, responseHandler);

            } catch (ClientProtocolException clientProtocolException) {
                Log.e(Constants.TAG, clientProtocolException.getMessage());
                if (Constants.DEBUG) {
                    clientProtocolException.printStackTrace();
                }
            } catch (IOException ioException) {
                Log.e(Constants.TAG, ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
            if (pageSourceCode != null) {
                document = Jsoup.parse(pageSourceCode);
                htmlTag = document.child(0);
                List<Element> elements = htmlTag.getElementsByTag("script");
                for (Element el : elements) {
                    if (el.toString().contains("heatindex")) {

                        humidity = "Humidity: " + el.toString().split("\"humidity\"")[1].substring(1,3) + "%\n";
                        temperature = "Temperature: " + el.toString().split("\"temperature\"")[1].substring(1, 3) + "F\n";

                        retStr += humidity;
                        retStr += temperature;

                        //serverThread.setData(city, new WeatherForecastInformation(humidity, temperature));
                        break;
                    }
                }
                PrintWriter printWriter = null;
                try {
                    printWriter = Utilities.getWriter(socket);
                    printWriter.println(humidity);
                    printWriter.println(temperature);
                    printWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                /*  EXAMPLE JSON PARSE



                   int position = scriptData.indexOf(Constants.SEARCH_KEY) + Constants.SEARCH_KEY.length();
                  scriptData = scriptData.substring(position);
                  JSONObject content = new JSONObject(scriptData);
                  JSONObject currentObservation = content.getJSONObject(Constants.CURRENT_OBSERVATION);
                  String temperature = currentObservation.getString(Constants.TEMPERATURE);
                 */
//                retStr += "Heat index: " + htmlTag.getElementsByAttributeValue("data-variable", "heatindex").first().toString() + "\n";
//                retStr += "Dew point: " + htmlTag.getElementsByAttributeValue("data-variable", "dewpoint").first().toString() + "\n";
//                retStr += "Humidity: " + htmlTag.getElementsByAttributeValue("data-variable", "humidity").first().toString() + "\n";





                // EXAMPLE CARTOON

                // cartoon title
                //            Element divTagIdCtitle = htmlTag.getElementsByAttributeValue(Constants.ID_ATTRIBUTE, Constants.CTITLE_VALUE).first();
                //            xkcdCartoonInformation.setCartoonTitle(divTagIdCtitle.ownText());
                //
                //            // cartoon url
                //            Element divTagIdComic = htmlTag.getElementsByAttributeValue(Constants.ID_ATTRIBUTE, Constants.COMIC_VALUE).first();
                //            String cartoonInternetAddress = divTagIdComic.getElementsByTag(Constants.IMG_TAG).attr(Constants.SRC_ATTRIBUTE);
                //            String cartoonUrl = Constants.HTTP_PROTOCOL + cartoonInternetAddress;
                //            xkcdCartoonInformation.setCartoonUrl(cartoonUrl);
                //
                //            try {
                //                HttpGet httpGetCartoon = new HttpGet(cartoonUrl);
                //                HttpResponse httpResponse = httpClient.execute(httpGetCartoon);
                //                HttpEntity httpEntity = httpResponse.getEntity();
                //                if (httpEntity != null) {
                //                    xkcdCartoonInformation.setCartoonBitmap(BitmapFactory.decodeStream(httpEntity.getContent()));
                //                }
                //            } catch (ClientProtocolException clientProtocolException) {
                //                Log.e(Constants.TAG, clientProtocolException.getMessage());
                //                if (Constants.DEBUG) {
                //                    clientProtocolException.printStackTrace();
                //                }
                //            } catch (IOException ioException) {
                //                Log.e(Constants.TAG, ioException.getMessage());
                //                if (Constants.DEBUG) {
                //                    ioException.printStackTrace();
                //                }
                //            }








            }

            return retStr;
        } else {
            // EXAMPLE POST


            try {
                HttpClient httpClientPost = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://httpbin.org/post");

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("attribute1", "value1"));
                // ...
                params.add(new BasicNameValuePair("attributen", "valuen"));

                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                httpPost.setEntity(urlEncodedFormEntity);

                HttpResponse httpPostResponse = httpClientPost.execute(httpPost);
                HttpEntity httpPostEntity = httpPostResponse.getEntity();
                if (httpPostEntity != null) {
                    // do something with the response

                    //Toast.makeText(MainActivity.context, httpPostEntity.toString(), Toast.LENGTH_LONG).show();
                    retStr = httpPostEntity.getContent().toString();
                    Log.i(Constants.TAG, EntityUtils.toString(httpPostEntity));
                    return retStr;
                }
            } catch (Exception exception) {
                Log.e(Constants.TAG, exception.getMessage());
                if (Constants.DEBUG) {
                    exception.printStackTrace();
                }
            }
        }
        return "Empty";
    }

}
