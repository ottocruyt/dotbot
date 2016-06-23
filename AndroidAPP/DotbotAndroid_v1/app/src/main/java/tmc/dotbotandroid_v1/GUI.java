package tmc.dotbotandroid_v1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

/**
 * Created by Steven on 4/06/2016.
 */
public class GUI extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gui);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // Functionality that keeps the screen on while being in the app


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
