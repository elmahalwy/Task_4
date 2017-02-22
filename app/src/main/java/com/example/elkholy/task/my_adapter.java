package com.example.elkholy.task;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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

import java.io.File;
import java.util.ArrayList;


/**
 * Created by elkholy on 16/02/2017.
 */

public class my_adapter extends RecyclerView.Adapter<my_adapter.ViewHolder> {
    public ArrayList<String> mylist;
    public Context mcontext;
    public ItemClickListner clickListener;
    RequestQueue requestQueue;
    TextView tv_progress_bar;
    public JSONArray jsonArray;
    public JSONObject jsonObject;

    public String Url_real;
    public String URL = "http://bbioon.com/uploads/api.php";
    public String name = null;
    int status;


    public my_adapter(ArrayList<String> mylist, Context mcontext) {
        this.mylist = mylist;
        this.mcontext = mcontext;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mcontext).inflate(R.layout.link, null);
        ViewHolder viewHolder = new ViewHolder(v);
        requestQueue = Volley.newRequestQueue(mcontext);
        return viewHolder;
    }

    public String uriEncoder(String query) {
        final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
        String encodedURL = "";
        encodedURL = Uri.encode(query, ALLOWED_URI_CHARS);
        return encodedURL;
    }


    @Override
    public void onBindViewHolder(final my_adapter.ViewHolder holder, final int position) {

        holder.tv_name.setText(mylist.get(position).toString());
        holder.tv_download.setText("Download");
        holder.progressBar.setVisibility(View.INVISIBLE);
        holder.textView.setText("");
        holder.tv_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null
                        , new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            jsonArray = response.getJSONArray("files");
                            jsonObject = jsonArray.getJSONObject(position);
                            Url_real = jsonObject.getString("url");
                            String url_path = uriEncoder(Url_real);

                            final DownloadManager manager;
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                            manager = (DownloadManager) mcontext.getSystemService(Context.DOWNLOAD_SERVICE);
                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url_path));
                            request.setMimeType("application/pdf");
                            request.allowScanningByMediaScanner();
                            request.setVisibleInDownloadsUi(false);

                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS,
                                    mylist.get(position).toString() + ".pdf");


                            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
                            //String name_of_file = URLUtil.guessFileName(Url_real, null, MimeTypeMap.getFileExtensionFromUrl(Url_real));
                            //   request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS, "task.pdf");


                            final long download_id = manager.enqueue(request);

                            holder.progressBar.setVisibility(View.VISIBLE);
                            Thread thread = null;
                            final Activity activity = (Activity) mcontext;

                            thread = new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    boolean downloading = true;
                                    while (downloading) {
                                        DownloadManager.Query q = new DownloadManager.Query();

                                        q.setFilterById(download_id);
                                        Cursor cursor = manager.query(q);
                                        cursor.moveToFirst();
                                        status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                                        int bytes_downloaded = cursor.getInt(cursor
                                                .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                                        int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                                        if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {

                                            downloading = false;

                                        }


                                        final double dl_progress = (int) ((bytes_downloaded * 100l) / bytes_total);

                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                holder.progressBar.setProgress((int) dl_progress);

                                                //holder.textView.setText("Downloaded");

                                            }
                                        });
                                        cursor.close();


                                    }
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            holder.textView.setText("Downloaded.");

                                            holder.tv_download.setText("Open");
                                            holder.tv_download.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath()
                                                            + File.separator + mylist.get(position).toString() + ".pdf");
                                                    if (file.exists()) {
                                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                                                        Intent i = Intent.createChooser(intent, "Open File");
                                                        mcontext.startActivity(i);
                                                    } else {
                                                        Toast.makeText(mcontext, "There is a problem in opening file", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                        }
                                    });

                                }
                            });
                            thread.start();


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


            }
        });


    }

    @Override
    public int getItemCount() {
        return mylist.size();
    }

    public void setClickListener(ItemClickListner itemClickListener) {
        this.clickListener = itemClickListener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        TextView tv_download = (TextView) itemView.findViewById(R.id.tv_download);
        ProgressBar progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        TextView textView = (TextView) itemView.findViewById(R.id.tv_progress_bar);
        Items[] links;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setTag(itemView);
            tv_download.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            clickListener.onClick(tv_download, getPosition());

        }
    }
}
