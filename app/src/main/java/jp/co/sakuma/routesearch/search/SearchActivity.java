package jp.co.sakuma.routesearch.search;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import jp.co.sakuma.routesearch.MainActivity;
import jp.co.sakuma.routesearch.R;

public class SearchActivity extends AppCompatActivity {

    private EditText inputText = null;
    private Button inputButton = null;

    private final String DEBUG_TAG = "PostAccess";

    public static final String RETURN_TEXT = "RETURN_TEXT";

    static final int RESULT_SUBACTIVITY = 1000;

    public static int BACK_FLG = 0;
    public static String TOAST_MESSAGE = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        inputText = findViewById(R.id.input_box);
        inputButton = findViewById(R.id.input_button);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (BACK_FLG == 2) {
            Toast.makeText(this, TOAST_MESSAGE, Toast.LENGTH_LONG).show();
        }
        // 初期化
        BACK_FLG = 0;
    }

    // 検索ボタン押下時のイベント
    void startResult_onClick(View view) {
        String param = inputText.getText().toString();

        Intent intent = new Intent(getApplication(), SearchResultActivity.class);
        intent.putExtra(RETURN_TEXT, param);
        startActivityForResult(intent, RESULT_SUBACTIVITY);
    }

    // 戻るボタン押下時のイベント
    void backMain_onClick(View view) {
        Intent intent = new Intent(getApplication(), MainActivity.class);
        startActivityForResult(intent, RESULT_SUBACTIVITY);
    }


}
