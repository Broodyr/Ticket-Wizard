package ca.ubc.cosc341.tw.ticketwizard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import java.text.DecimalFormat;

public class PostEvent extends AppCompatActivity {

    private static final int PICK_EVENT_PHOTO = 0;
    EditText namet, pricet, datet, timet, descriptiont;
    Bitmap photo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_event);

        namet = findViewById(R.id.enterEvent);
        pricet = findViewById(R.id.price);
        datet = findViewById(R.id.date);
        timet = findViewById(R.id.time);
        descriptiont = findViewById(R.id.desc);

        //formatting for Price field
        pricet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                priceFormat(v, hasFocus);
            }
        });

        //validation and formatting for Date field
        datet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                dateFormat(v, hasFocus);
            }
        });

        //validation and formatting for Time field
        timet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                timeFormat(v, hasFocus);
            }
        });
    }

    public void pickImage(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_EVENT_PHOTO);
    }

    //image picker
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_EVENT_PHOTO && resultCode == Activity.RESULT_OK) {
            if (data == null || data.getData() == null) {
                Toast.makeText(this, this.getString(R.string.choose), Toast.LENGTH_LONG).show();
            } else try (InputStream is = this.getContentResolver().openInputStream(data.getData())){
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
        String eTime = timet.getText().toString();
        String eDesc = descriptiont.getText().toString();

        Toast.makeText(this, this.getString(R.string.wait), Toast.LENGTH_LONG).show();

        //if all of the required fields are filled write the data
        if (eName.length() > 0 && ePrice.length() > 0 && eDate.length() > 0 && eDesc.length() > 0 && photo != null) {
            String filePath = "events";
            String fileName = eName.replaceAll(" ", "_") + ".dat";
            String imgPath = "images";
            String imgFileName = eName.replaceAll(" ", "_") + "_img" + ".png";
            String fileData = eName + '\n' + ePrice + '\n' + eDate + '\n' + eTime + '\n' + eDesc;

            //check if event name already exists
            try {
                File fileDir = this.getDir(filePath, Context.MODE_PRIVATE);
                File file = new File(fileDir, fileName);
                File imgDir = this.getDir(imgPath, Context.MODE_PRIVATE);
                File imgFile = new File(imgDir, imgFileName);
                if (!imgFile.exists()) {
                    //writes the data
                    try (FileOutputStream os = new FileOutputStream(file);
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

    public void priceFormat(View v, boolean hasFocus) {
        String current = pricet.getText().toString();

        if (current.matches("^\\$?\\d+(.\\d{2})?$")) {
            //prevent errors with Free text
            if ("Free".equals(current))
                pricet.setText("0");

            if (!hasFocus && !"".equals(current)) {
                String priceText;

                //prevents errors caused by dollar sign
                if (current.charAt(0) == '$')
                    current = current.substring(1, current.length());

                //if price is 0, make it Free, otherwise format decimal
                if (Double.parseDouble(current) == 0.0) {
                    priceText = "Free";
                } else {
                    DecimalFormat df = new DecimalFormat("0.00");
                    priceText = "$" + df.format(Double.parseDouble(current));
                }
                pricet.setText(priceText);
            }
        } else if (!"".equals(current)){
            Toast.makeText(v.getContext(), v.getContext().getString(R.string.invPrice), Toast.LENGTH_LONG).show();
            pricet.setText("");
        }
    }

    public void dateFormat(View v, boolean hasFocus) {
        String current = datet.getText().toString();

        if (current.matches("[0-2]?[0-9][/][0-2]?[0-9][/][0-9]{2}")) {
            if (!hasFocus) {
                //split date
                String[] currentSplit = current.split("/");

                //add padding zeroes
                for (int i = 0; i < 2; ++i)
                    if (currentSplit[i].length() == 1)
                        currentSplit[i] = 0 + currentSplit[i];

                //recombine date
                current = currentSplit[0] + '/' + currentSplit[1] + '/' + currentSplit[2];

                //gets day and month
                int day = Integer.parseInt(currentSplit[0]);
                int month = Integer.parseInt(currentSplit[1]);

                //date validation
                if (!(day > 0 && day <= 31 && month > 0 && month <= 12)) {
                    Toast.makeText(v.getContext(), v.getContext().getString(R.string.invDate), Toast.LENGTH_LONG).show();
                    datet.setText("");
                } else {
                    datet.setText(current);
                }
            }
        } else if (!"".equals(current)){ //if date doesn't match regex format
            Toast.makeText(v.getContext(), v.getContext().getString(R.string.invDate), Toast.LENGTH_LONG).show();
            datet.setText("");
        }
    }

    public void timeFormat(View v, boolean hasFocus) {
        String current = timet.getText().toString();

        if (current.matches("[0-2]?[0-9][:][0-5][0-9][A|P]?M?")) {
            //split time
            String[] currentSplit = current.split(":");

            //add padding zero for minutes
            if (currentSplit[1].length() == 1)
                currentSplit[1] = 0 + currentSplit[1];

            //gets hours
            int hours = Integer.parseInt(currentSplit[0]);

            if (!hasFocus) {
                //gets minutes
                int minutes = Integer.parseInt(currentSplit[1]);

                String timeText = "";

                //checks if time entered is valid
                //if valid, convert to AM/PM
                if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59) {
                    Toast.makeText(v.getContext(), v.getContext().getString(R.string.invTime), Toast.LENGTH_LONG).show();
                } else if (hours >= 13) {
                    hours -= 12;
                    timeText = hours + ":" + currentSplit[1] + "PM";
                } else if (hours == 12) {
                    timeText = hours + ":" + currentSplit[1] + "PM";
                } else if (hours == 0) {
                    hours += 12;
                    timeText = hours + ":" + currentSplit[1] + "AM";
                } else {
                    timeText = hours + ":" + currentSplit[1] + "AM";
                }

                timet.setText(timeText);
            } else { //has focus
                //prevents errors caused by AM/PM suffix
                String suffix = current.substring(current.length() - 2, current.length());
                if ("PM".equals(suffix) && hours != 12) {
                    hours += 12;
                    current = hours + current.substring(current.length()-5, current.length());
                } else if ("AM".equals(suffix) && hours == 12) {
                    hours -= 12;
                    current = hours + current.substring(current.length()-5, current.length());
                }
                timet.setText(current.substring(0, current.length() - 2));
            }
        } else if (!"".equals(current)) { //if time doesn't match regex format
            Toast.makeText(v.getContext(), v.getContext().getString(R.string.invTime), Toast.LENGTH_LONG).show();
            timet.setText("");
        }
    }
}
