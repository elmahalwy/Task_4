package com.example.elkholy.task;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements ItemClickListner {
    public Items[] links = {
            new Items("Task Floor"),
            new Items("Epoxy Floor"),
            new Items("Nova Floor")
    };
    RequestQueue requestQueue;
    String URL = "http://bbioon.com/uploads/api.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        my_adapter my_adapter = new my_adapter(links, getApplicationContext());
        recyclerView.setAdapter(my_adapter);
        my_adapter.setClickListener(this);
        requestQueue = Volley.newRequestQueue(this);

    }

    @Override
    public void onClick(View view, final int position) {
        // Toast.makeText(getApplicationContext(),"good",Toast.LENGTH_LONG).show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("files");
                            JSONObject url_json = jsonArray.getJSONObject(position);
                            String Url_real =  url_json.getString("url");
                            

                            //  Url_real += "";
                            // Toast.makeText(getApplicationContext(), Url_real , Toast.LENGTH_SHORT).show();
                            DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(Url_real ));
                            request.setTitle("File Download");
                            request.setDescription("File is being downloaded...");
                            request.setMimeType("task/pdf");
                            request.allowScanningByMediaScanner();
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);

                            //String name_of_file = URLUtil.guessFileName(Url_real, null, MimeTypeMap.getFileExtensionFromUrl(Url_real));
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS, "task.pdf");

                            manager.enqueue(request);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Voltile", "Error");

                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

}
