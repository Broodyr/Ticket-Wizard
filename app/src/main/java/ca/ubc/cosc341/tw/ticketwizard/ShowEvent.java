package ca.ubc.cosc341.tw.ticketwizard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

public class ShowEvent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);

        TextView desc = findViewById(R.id.desc);
        desc.setMovementMethod(new ScrollingMovementMethod());
    }

    public void back(View view){
        Intent intent = new Intent(this, MainActivity.class);
        finishAfterTransition();
        startActivity(intent);
    }
}
