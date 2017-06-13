package com.example.thread.databasepract;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.thread.databasepract.database.BaseDB;

/**
 * Created by Thread on 6/14/2017.
 */
public class BaseActivity extends AppCompatActivity {
    protected BaseDB baseDB;
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseDB=new BaseDB(this);
    }
}
