package ca.ubc.cosc341.tw.ticketwizard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Browse extends AppCompatActivity {
    int i = 0;
    int y = 0;
    String data[] = new String[100];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        String fileName = "events.dat";
        String imgPath = "images";
        String line;

        try (FileInputStream fis = openFileInput(fileName);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {
            while ((line = br.readLine()) != null) {
                data[i] = line;
                i++;
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        LinearLayout ll = findViewById(R.id.lnr);
        while(y < i && i > 0){
            String s = data[y];
            String parsedS[] = s.split("@@@");
            String name = parsedS[0];
            String imgFileName = name.replaceAll(" ", "_") + "_img" + ".png";

            final LinearLayout lli = new LinearLayout(this);
            final ImageView iv = new ImageView(this);
            final TextView tv = new TextView(this);

            lli.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 400));
            iv.setLayoutParams(new LinearLayout.LayoutParams(
                    400, LinearLayout.LayoutParams.MATCH_PARENT));
            tv.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

            try {
                File imgDir = this.getDir(imgPath, Context.MODE_PRIVATE);
                File imgFile = new File(imgDir, imgFileName);
                Bitmap bm = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                iv.setImageBitmap(bm);
            } catch (Exception e) {
                e.printStackTrace();
            }

            lli.setId(y);
            lli.setPadding(8, 8, 8, 8);
            lli.setBottom(8);
            tv.setText(name);
            tv.setTextColor(this.getColor(R.color.colorLight));
            tv.setTextSize(18);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(null, Typeface.BOLD);
            iv.setForegroundGravity(Gravity.CENTER);
            iv.setBackgroundColor(getColor(R.color.colorBlack));

            ll.addView(lli);
            lli.addView(iv);
            lli.addView(tv);

            if (y%2 == 0) {
                lli.setBackgroundColor(getColor(R.color.colorLight));
                lli.getBackground().setAlpha(128);
            }
            y++;
        }
    }

    public void back(View view){
        finish();
    }
}