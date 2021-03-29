package jp.co.sakuma.routesearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import jp.co.sakuma.routesearch.routeSearch.RouteSearchActivity;
import jp.co.sakuma.routesearch.search.SearchActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

    }

    void search_onClick(View view) {
        Intent intent = new Intent(getApplication(), SearchActivity.class);
        startActivity(intent);
    }

    void routeSearch_onClick(View view) {
        Intent intent = new Intent(getApplication(), RouteSearchActivity.class);
        startActivity(intent);
    }
}
