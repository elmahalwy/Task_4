package com.example.elkholy.task;

import android.app.Activity;


import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.os.Bundle;

public class MainActivity extends Activity  {




    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv_fragment rv_fragment = new rv_fragment();
        fragmentTransaction.add(R.id.activity_main, rv_fragment);
        fragmentTransaction.commit();




    }


}
