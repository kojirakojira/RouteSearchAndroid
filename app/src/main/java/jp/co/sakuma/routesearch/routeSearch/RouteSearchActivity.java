package jp.co.sakuma.routesearch.routeSearch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import jp.co.sakuma.routesearch.R;

public class RouteSearchActivity extends AppCompatActivity {

    public static int BACK_FLG = 0;

    public List<String> allStationList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    }
}
