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
import java.io.FileReader;
import java.io.IOException;

public class Browse extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        drawEvents();
    }

    public void back(View view){
        finish();
    }

    public void drawEvents() {
        String eventPath = "events";
        String imgPath = "images";

        File eventDir = this.getDir(eventPath, Context.MODE_PRIVATE);
        File[] dirListing = eventDir.listFiles();
        File imgDir = this.getDir(imgPath, Context.MODE_PRIVATE);

        LinearLayout ll = findViewById(R.id.content);

        if (dirListing != null) {
            for (File child : dirListing) {
                String imgFileName = child.getName().replaceAll(".dat", "_img.png");
                File imgFile = new File(imgDir, imgFileName);
                try (BufferedReader br = new BufferedReader(new FileReader(child))) {
                    //create views for the event info
                    final LinearLayout lli = new LinearLayout(this);
                    final ImageView iv = new ImageView(this);
                    final TextView tv = new TextView(this);

                    //formatting
                    lli.setPadding(8, 8, 8, 8);
                    lli.setBottom(8);
                    tv.setTextColor(this.getColor(R.color.colorLight));
                    tv.setTextSize(18);
                    tv.setGravity(Gravity.CENTER);
                    tv.setTypeface(null, Typeface.BOLD);
                    iv.setForegroundGravity(Gravity.CENTER);
                    iv.setPadding(4,4,4,4);
                    iv.setBackground(this.getDrawable(R.drawable.border));

                    lli.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 400));
                    iv.setLayoutParams(new LinearLayout.LayoutParams(400, LinearLayout.LayoutParams.MATCH_PARENT));
                    tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

                    //image
                    Bitmap bm = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    iv.setImageBitmap(bm);

                    //event name
                    tv.setText(br.readLine());

                    //price

                    //date

                    //time

                    //description

                    ll.addView(lli);
                    lli.addView(iv);
                    lli.addView(tv);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}