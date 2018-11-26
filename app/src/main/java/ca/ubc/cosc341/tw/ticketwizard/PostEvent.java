package ca.ubc.cosc341.tw.ticketwizard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PostEvent extends AppCompatActivity {

    private static final int PICK_EVENT_PHOTO = 0;
    EditText namet, pricet, datet, descriptiont;
    Bitmap photo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_event);

        namet = findViewById(R.id.enterEvent);
        pricet = findViewById(R.id.price);
        datet = findViewById(R.id.date);
        descriptiont = findViewById(R.id.desc);
    }

    public void pickImage(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_EVENT_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_EVENT_PHOTO && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Toast.makeText(this, this.getString(R.string.choose), Toast.LENGTH_LONG).show();
                return;
            }
            try (InputStream is = this.getContentResolver().openInputStream(data.getData())){
                photo = BitmapFactory.decodeStream(is);
                Drawable d = new BitmapDrawable(getResources(), photo);

                ImageView img = findViewById(R.id.photo);
                TextView addImg = findViewById(R.id.addPhoto);

                img.setImageDrawable(d);
                addImg.setText("");
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void back(View view) {
        finish();
    }

    public void upload(View view) {
        String eName = namet.getText().toString();
        String ePrice = pricet.getText().toString();
        String eDate = datet.getText().toString();
        String eDesc = descriptiont.getText().toString();
        String delim = "@@@";
        //if all of the required fields are filled write the data
        if (eName.length() > 0 && ePrice.length() > 0 && eDate.length() > 0 && eDesc.length() > 0 && photo != null) {
//        if (1==1) {
            String fileName = "events.dat";
            String imgPath = "images";
            String imgFileName = eName.replaceAll(" ", "_") + "_img" + ".png";
            String fileData = eName + delim + ePrice + delim + eDate + delim + eDesc + '\n';
            System.out.println(fileData);

            //check if event name already exists
            try {
                File imgDir = this.getDir(imgPath, Context.MODE_PRIVATE);
                File imgFile = new File(imgDir, imgFileName);
                if (!imgFile.exists()) {
                    //writes the data
                    try (FileOutputStream os = openFileOutput(fileName, Context.MODE_APPEND);
                         FileOutputStream os2 = new FileOutputStream(imgFile)) {

                        //write image data
                        photo.compress(Bitmap.CompressFormat.PNG, 85, os2);

                        //write text data
                        os.write(fileData.getBytes());

                        Toast.makeText(this, this.getString(R.string.success), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    finish();

                } else {
                    Toast.makeText(this, this.getString(R.string.exists), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        //checks if user hasn't filled required fields and tells them using a toast
        else
            Toast.makeText(this, this.getString(R.string.req), Toast.LENGTH_LONG).show();
    }
}
