package com.iasonas.housebouncer;

import android.app.Activity;
import android.app.IntentService;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v4.graphics.ColorUtils;

/**
 * Created by iasonas on 2/4/2019.
 */

public class Detector extends IntentService {


    int sensitivity;
    double sensitivityL;
    double threshold;
    static public boolean flag;
    int restartT;
    String skypeAdd;
    String skypeURI;

    public Detector() {
        super("test-service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        android.os.Process.setThreadPriority(-20);


        ResultReceiver rec = intent.getParcelableExtra("listener");
        sensitivity = intent.getIntExtra("sensitivity", 5);
        restartT = intent.getIntExtra("restartT", 60);
        skypeAdd = intent.getStringExtra("skypeA");

        skypeURI = "skype:live:" + skypeAdd + "?call&amp;video=true";


        sensitivityL = Double.valueOf(sensitivity);

        threshold = 15.00 - sensitivityL;

        byte[] img1 = intent.getByteArrayExtra("a");
        byte[] img2 = intent.getByteArrayExtra("b");

        Bitmap imgOne = BitmapFactory.decodeByteArray(img1, 0, img1.length, null);
        Bitmap imgTwo = BitmapFactory.decodeByteArray(img2, 0, img2.length, null);

        Bitmap image1 = Bitmap.createScaledBitmap(imgOne, 160, 120, false);
        Bitmap image2 = Bitmap.createScaledBitmap(imgTwo, 160, 120, false);


        int res = average(image1, image2);
        String result = String.format("%d", res);

        if(flag) {
            Bundle bundle = new Bundle();
            bundle.putString("result", result);
            rec.send(Activity.RESULT_OK, bundle);
        }

        if((res > 1000) && flag) {

            flag = false;

            Bundle bundle = new Bundle();
            bundle.putString("result", "CALL");
            rec.send(Activity.RESULT_OK, bundle);


            Uri skypeUri = Uri.parse(skypeURI);
            Intent myIntent = new Intent(Intent.ACTION_VIEW, skypeUri);

            myIntent.setComponent(new ComponentName("com.skype.raider", "com.skype.raider.Main"));
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(myIntent);

            int counter = 0;

            try {

                while (counter < restartT) {

                    Thread.sleep(1000);
                    counter++;
                }

                Intent restart = new Intent(getBaseContext(), Activity21.class);
                restart.putExtra("skypeA", skypeAdd);
                restart.putExtra("sensitivity", sensitivity);
                restart.putExtra("restartT", restartT);
                restart.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplication().startActivity(restart);


            }catch(java.lang.InterruptedException e) {}

        }

    }



    public int average(Bitmap a, Bitmap b){

        int result;
        int counter = 0;
        double distance= 0.00d;


        for(int i = 0; i< 160; i++){
            for(int j = 0; j< 120; j++) {

                int colour1 = a.getPixel(i,j);
                int colour2 = b.getPixel(i,j);
                double[] colourLAB1 = new double[3];
                double[] colourLAB2 = new double[3];

                ColorUtils.colorToLAB(colour1, colourLAB1);
                ColorUtils.colorToLAB(colour2, colourLAB2);

                distance = ColorUtils.distanceEuclidean(colourLAB1, colourLAB2);


                if(distance > threshold) { counter++; }

            }

        }
        result = (int) counter;
        return result;
    }

}
