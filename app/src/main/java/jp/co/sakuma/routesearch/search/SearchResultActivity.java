package jp.co.sakuma.routesearch.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import jp.co.sakuma.routesearch.R;

public class SearchResultActivity extends AppCompatActivity {

    private String inputText = "";

    AsyncNetworkTask task;

    private Button backMain = null;
    private TextView stationName = null;

    static final int RESULT_SUBACTIVITY = 1000;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_activity);

        stationName = findViewById(R.id.stationName);

        // 前画面からの値を取得
        Intent intent = getIntent();
        inputText = intent.getStringExtra(SearchActivity.RETURN_TEXT);

        // WebAPI呼び出し
        task = new AsyncNetworkTask(this);
        task.execute(inputText);

    }

    // 戻るボタン押下時の処理
    public void backSearch_onClick(View view) {
        Log.d(this.toString(), "検索画面に戻ります");
        finish();
    }
}
