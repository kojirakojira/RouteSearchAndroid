package jp.co.sakuma.routesearch.routeSearch;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;

import jp.co.sakuma.routesearch.MainActivity;
import jp.co.sakuma.routesearch.R;
import jp.co.sakuma.routesearch.entity.JsonParser;
import jp.co.sakuma.routesearch.entity.Station;
import jp.co.sakuma.routesearch.routeSearch.RouteSearchActivity;
import jp.co.sakuma.routesearch.search.SearchActivity;
import jp.co.sakuma.routesearch.search.SearchResultActivity;

public class AsyncNetworkTaskForAllStation extends AsyncTask<String, Integer, String> {

    private EditText inputText1;
    private EditText inputText2;
    private Button inputButton;
    private Button backMain;
    private TextView existenceCheck1;
    private TextView existenceCheck2;

    private RouteSearchActivity activity;

    private final String DEBUG_LOG = "DEBUG_LOG";

    private String searchURL = "http://route-search.herokuapp.com/allSearch";


    public String result = "";

    //結果を反映させるTextView
    public AsyncNetworkTaskForAllStation(Context context) {
        super();
        activity = (RouteSearchActivity) context;

        inputText1 = activity.findViewById(R.id.input_box1);
        inputText2 = activity.findViewById(R.id.input_box2);
        inputButton = activity.findViewById(R.id.input_button1);
        backMain = (Button) activity.findViewById(R.id.backMain);
        existenceCheck1 = activity.findViewById(R.id.existence_check1);
        existenceCheck2 = activity.findViewById(R.id.existence_check2);

        backMain.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (RouteSearchActivity.BACK_FLG != 1) {
                            // 検索が完了していない場合は通信をキャンセルする
                            onCancelled();
                        }
                        activity.finish();
                    }
                }
        );
    }

    // 非同期でHTTP POSTリクエストを送信
    @Override
    protected String doInBackground(String... params) {
        publishProgress(30);

        publishProgress(60);

        String result = doRequest(params);

        publishProgress(100);

        return result;
    }

    // 進捗状況をログに出力
    @Override
    protected void onProgressUpdate(Integer... values) {
        Log.d("url", values[0].toString());
    }

    // 非同期処理を終了した後、TextViewに反映
    @Override
    protected void onPostExecute(String result) {
        JsonParser json = new JsonParser();
        List<String> stationList = json.parseAllStation(result);
        if (stationList == null) {
            activity.finish();
            // エラーの場合
            return;
        }

        activity.allStationList = stationList;

    }

    // 非同期処理をキャンセルしたとき、TextViewに反映
    @Override
    protected void onCancelled() {
        SearchActivity.BACK_FLG = 2;
        SearchActivity.TOAST_MESSAGE = "検索がキャンセルされました。";
    }

    private String doRequest(String... params) {

        String returnText = "";

        try {
            URL url = new URL(searchURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setReadTimeout(5000);
            con.connect();
            int status = con.getResponseCode();
            if (status != 200) {
                throw new IOException("ステータスコード" + status);
            }

            returnText = toOutputString(con);


        } catch(SocketTimeoutException ex) {
            Log.e(DEBUG_LOG, "タイムアウト", ex);
            SearchActivity.BACK_FLG = 2;
            SearchActivity.TOAST_MESSAGE = "タイムアウト";
        } catch(MalformedURLException ex) {
            Log.e(DEBUG_LOG, "URL変換失敗", ex);
            SearchActivity.BACK_FLG = 2;
            SearchActivity.TOAST_MESSAGE = "URL変換失敗";
        } catch(IOException ex) {
            Log.e(DEBUG_LOG, "通信失敗", ex);
            SearchActivity.BACK_FLG = 2;
            SearchActivity.TOAST_MESSAGE = "通信失敗";
        } catch (Exception e) {
            Log.e(DEBUG_LOG, "それ以外のエラー");
            SearchActivity.BACK_FLG = 2;
            SearchActivity.TOAST_MESSAGE = "それ以外のエラー";
        }

        return returnText;
    }

    // WebAPIから受け取った値を文字列に変換する
    private String toOutputString(HttpURLConnection con) throws IOException {

        BufferedReader reader = new BufferedReader( new InputStreamReader(con.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String data = "";
        while ((data = reader.readLine()) != null) {
            sb.append(data);
        }
        return sb.toString();
    }

}
