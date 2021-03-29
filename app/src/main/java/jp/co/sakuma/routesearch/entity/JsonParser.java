package jp.co.sakuma.routesearch.entity;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.util.List;

public class JsonParser {

    public JsonParser() {
    }

    public Station parseStation(String json) {
        Station station = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            station = mapper.readValue(json, Station.class);
        } catch (Exception e) {
            Log.e(this.toString(), e.toString());
        }
        return station;
    }

    public List<String> parseAllStation(String json) {
        List<String> list = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            list = mapper.readValue(json, new TypeReference<List<String>>(){});
        } catch (Exception e) {
            Log.e(this.toString(), e.toString());
        }
        return list;
    }

//    public Station getStation() {
//        return station;
//    }
//    public void setStation(Station station) {
//        this.station = station;
//    }
}
