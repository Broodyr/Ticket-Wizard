package ca.ubc.cosc341.tw.ticketwizard;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void browse(View view){
        Intent intent = new Intent(this, Browse.class);
        finishAfterTransition();
        startActivity(intent);
    }

    public void postEvent(View view){
        Intent intent = new Intent(this, PostEvent.class);
        finishAfterTransition();
        startActivity(intent);
    }
}
