package com.example.test;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FtpTask extends AsyncTask<Void, Void, FTPClient> {
    private WeakReference<Context> contextRef;
    private String status = "Success";
    private String fpath = "";
    private File textFile;

    public FtpTask(Context context){
        contextRef = new WeakReference<>(context);
    }

    protected FTPClient doInBackground(Void... args) {
        FTPClient client = new FTPClient();
        FileOutputStream fos = null;

        try {
            client.connect("192.168.2.51",21);
            client.login("ftp-acct", "ese");
            client.setFileType(FTP.BINARY_FILE_TYPE);
            client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
            client.setSoTimeout(10000);
            client.enterLocalPassiveMode();


            File sdCard = Environment.getExternalStorageDirectory();
            File filedir = new File(sdCard.getAbsolutePath() + "/FtpData");
            filedir.mkdirs();

            File outputFile = new File(filedir,"room.png");
            fos = new FileOutputStream(outputFile);
            client.retrieveFile("./files/room.png",fos); //remote directory
            //text.setText("Success?");

            textFile = new File(filedir,"room.txt");
            fos = new FileOutputStream(textFile);
            client.retrieveFile("./files/room.txt",fos); //remote directory

            status = "Image stored in; " + outputFile + ", text stored in;" + textFile;
            fpath = "" + outputFile;

        } catch (Exception e) {
            e.printStackTrace();
            //text.setText(e.getMessage());
            status = e.getMessage();
        } finally {
            try {
                if (fos != null){
                    fos.close();
                }
                client.disconnect();
            } catch (Exception e){
                e.printStackTrace();
                status = e.getMessage();
            }
        }
        return null;
    }

    protected void onPostExecute(FTPClient result) {
        Context con = contextRef.get();
        if (con != null) {
            try {
                FileInputStream fin = new FileInputStream(textFile);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fin));
                StringBuilder sb = new StringBuilder();
                String line = null;
                try {
                    //while ((line = reader.readLine()) != null) {
                        //sb.append(line).append("\n");
                    //}
                    String farea = reader.readLine().toString();
                    String tarea = reader.readLine().toString();

                    reader.close();
                    //String area =  sb.toString();

                    TextView tv = (TextView) ((MainActivity) con).findViewById(R.id.textdeb);
                    tv.setText(status);
                    TextView fatv = (TextView) ((MainActivity) con).findViewById(R.id.textareaf);
                    fatv.setText("Area of floor: "+ farea+" ft^2");
                    TextView tatv = (TextView) ((MainActivity) con).findViewById(R.id.textareat);
                    tatv.setText("Area of image: "+ tarea+" ft^2");
                    ImageView iv = (ImageView)((MainActivity) con).findViewById(R.id.img1);

                    Bitmap myBitmap = BitmapFactory.decodeFile(fpath);
                    //ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);
                    iv.setImageBitmap(myBitmap);
                } catch(IOException ex){
                    TextView tv = (TextView) ((MainActivity) con).findViewById(R.id.textdeb);
                    tv.setText("Failed reading text file.");
                }
            } catch(FileNotFoundException fileEx) {
                TextView tv = (TextView) ((MainActivity) con).findViewById(R.id.textdeb);
                tv.setText("Failed to open text file.");
            }
        }
    }
}