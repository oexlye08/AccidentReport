package com.osa.accidentreport;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class tampilSemuaLaporan  extends AppCompatActivity implements ListView.OnItemClickListener{

    private ListView listView;
    private String JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_semua_laporan);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        getJSON();
    }

    private void showEmployee(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Konfigurasi.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(Konfigurasi.TAG_ID);
                String name = jo.getString(Konfigurasi.TAG_NAMA);
                //String vehicle = jo.getString(Konfigurasi.TAG_VEHICLE);
                //String note = jo.getString(Konfigurasi.TAG_NOTE);
                //String location = jo.getString(Konfigurasi.TAG_LOCATION);

                HashMap<String,String> employees = new HashMap<>();
                employees.put(Konfigurasi.TAG_ID,id);
                employees.put(Konfigurasi.TAG_NAMA,name);
                //employees.put(Konfigurasi.TAG_VEHICLE, vehicle);
                //employees.put(Konfigurasi.TAG_NOTE, note);
                //employees.put(Konfigurasi.TAG_LOCATION, location);
                list.add(employees);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(tampilSemuaLaporan.this, list, R.layout.list_item,
                new String[]{Konfigurasi.TAG_ID,Konfigurasi.TAG_NAMA}, //Konfigurasi.TAG_VEHICLE, Konfigurasi.TAG_NOTE, Konfigurasi.TAG_LOCATION},
                new int[]{R.id.id, R.id.name});//, R.id.vehicle, R.id.note, R.id.location});

        listView.setAdapter(adapter);
    }

    private void getJSON() {
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(
                        tampilSemuaLaporan.this,
                        "Mengambil Data",
                        "Mohon Tunggu...",
                        false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showEmployee();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Konfigurasi.URL_GET_ALL);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, tampilLaporan.class);
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
        String empId = map.get(Konfigurasi.TAG_ID).toString();
        intent.putExtra(Konfigurasi.EMP_ID,empId);
        startActivity(intent);
    }

}
