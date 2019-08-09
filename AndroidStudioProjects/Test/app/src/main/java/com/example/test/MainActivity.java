package com.example.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void buttonOnClick(View v){
        new FtpTask(MainActivity.this).execute();
    }

    public void rstBtnOnClick(View v){
        TextView tv = (TextView) findViewById(R.id.textdeb);
        tv.setText("Ready.");
        TextView fatv = (TextView) findViewById(R.id.textareaf);
        fatv.setText("Area of floor: - ft^2");
        TextView tatv = (TextView) findViewById(R.id.textareat);
        tatv.setText("Area of image: - ft^2");
        ImageView iv = findViewById(R.id.img1);
        iv.setImageResource(0);
    }
}

