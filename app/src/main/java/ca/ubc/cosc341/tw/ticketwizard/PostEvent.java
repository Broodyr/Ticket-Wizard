package ca.ubc.cosc341.tw.ticketwizard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

public class PostEvent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_event);

        TextView paragraph = findViewById(R.id.paragraph);
        paragraph.setMovementMethod(new ScrollingMovementMethod());
    }

    public void back(View view){
        Intent intent = new Intent(this, MainActivity.class);
        finishAfterTransition();
        startActivity(intent);
    }
}
