package jp.co.sakuma.routesearch.search;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
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

import jp.co.sakuma.routesearch.R;
import jp.co.sakuma.routesearch.entity.JsonParser;
import jp.co.sakuma.routesearch.entity.Station;
import jp.co.sakuma.routesearch.search.SearchActivity;
import jp.co.sakuma.routesearch.search.SearchResultActivity;

public class AsyncNetworkTask extends AsyncTask<String, Integer, String> {

    private SearchResultActivity activity;
    private TextView stationName;
    private TableLayout routeList;
    private Button backMain;

    private final String DEBUG_LOG = "DEBUG_LOG";

    private String searchURL = "http://route-search.herokuapp.com/search?station=";

    public String result = "";

    //結果を反映させるTextView
    public AsyncNetworkTask(Context context) {
        super();
        activity = (SearchResultActivity) context;
        stationName = (TextView) activity.findViewById(R.id.stationName);
        routeList = (TableLayout) activity.findViewById(R.id.routeList);
        backMain = (Button) activity.findViewById(R.id.backMain);


        backMain.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SearchActivity.BACK_FLG != 1) {
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
        Station station = json.parseStation(result);
        if (station == null) {
            activity.finish();
            // エラーの場合
            return;
        } else if (station.getStationId() == 0) {
            // 検索結果なしの場合
            SearchActivity.BACK_FLG = 2;
            SearchActivity.TOAST_MESSAGE = "検索結果がありません。";
            activity.finish();
            return;

        }
        stationName.setText(station.getStationName());

        for (String route: station.getRouteList()) {
            TableRow row = new TableRow(activity);
            TextView view = new TextView(activity);
            view.setText(route);
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            view.setPadding(0,5,0,5);
            view.setGravity(Gravity.CENTER);

            row.addView(view);
            row.setGravity(Gravity.CENTER);
            routeList.addView(row, new TableLayout.LayoutParams());
        }

        // 検索結果ありの場合
        SearchActivity.BACK_FLG = 1;

    }

    // 非同期処理をキャンセルしたとき、TextViewに反映
    @Override
    protected void onCancelled() {
        SearchActivity.BACK_FLG = 2;
        SearchActivity.TOAST_MESSAGE = "検索がキャンセルされました。";
    }

    private String doRequest(String... params) {

        searchURL = searchURL + params[0];

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
