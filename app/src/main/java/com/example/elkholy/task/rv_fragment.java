package com.example.elkholy.task;


import android.os.Bundle;
import android.app.Fragment;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;




/**
 * A simple {@link Fragment} subclass.
 */
public class rv_fragment extends Fragment {


    public String URL = "http://bbioon.com/uploads/api.php";
    public String name = null;
    RequestQueue requestQueue;
    ArrayList<String> mylist = new ArrayList<String>();
    public my_adapter my_adapter;


    public rv_fragment() {

    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        requestQueue = Volley.newRequestQueue(getActivity());
        final View root_view = inflater.inflate(R.layout.fragment_rv_fragment, null);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("files");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        name = jsonObject.getString("name");
                        mylist.add(name);
                    }
                    RecyclerView recyclerView = (RecyclerView) root_view.findViewById(R.id.rv_items);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    my_adapter = new my_adapter(mylist, getActivity());
                    recyclerView.setAdapter(my_adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Voltile", "Error");
            }
        });


        requestQueue.add(jsonObjectRequest);
        return root_view;


    }


}
/*
new Thread(new Runnable() {
@Override
public void run() {

        while (progress_status < 100) {
        progressBar.setVisibility(View.VISIBLE);
        progress_status += 1;
        handler.post(new Runnable() {
@Override
public void run() {
        progressBar.setProgress(progress_status);
        tv_progress_bar.setText(progress_status + "/" + progressBar.getMax());
        }
        });
        try {
        Thread.sleep(200);
        } catch (InterruptedException e) {
        e.printStackTrace();
        }
        }
        }
        }).start();
        */