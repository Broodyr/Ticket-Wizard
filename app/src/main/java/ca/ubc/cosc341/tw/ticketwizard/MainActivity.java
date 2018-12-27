package ca.ubc.cosc341.tw.ticketwizard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button browseb = findViewById(R.id.browse);
        final Button postb = findViewById(R.id.postEvent);

        browseb.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                buttonPress(browseb, event);
                return true;
            }
        });

        postb.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                buttonPress(postb, event);
                return true;
            }
        });
    }

    public void browse() {
        Intent intent = new Intent(this, Browse.class);
        startActivity(intent);
    }

    public void postEvent() {
        Intent intent = new Intent(this, PostEvent.class);
        startActivity(intent);
    }

    public void buttonPress(Button b, MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            //b.setTranslationX(4);
            b.setTranslationY(4);
        } else if (e.getAction() == MotionEvent.ACTION_UP) {
            //b.setTranslationX(0);
            b.setTranslationY(0);

            switch (b.getId()) {
                case R.id.browse:
                    browse();
                    break;
                case R.id.postEvent:
                    postEvent();
                    break;
            }
        }
    }
}
