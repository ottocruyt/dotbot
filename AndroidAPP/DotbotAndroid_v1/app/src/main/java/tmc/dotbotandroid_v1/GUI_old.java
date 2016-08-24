package tmc.dotbotandroid_v1;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * Created by Steven on 4/06/2016.
 */
public class GUI_old extends AppCompatActivity{


    private Controller mController;
    private Handler mHandler = new Handler();

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

    @Override
    protected void onPause() {
        super.onPause();
        mController.Pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int a = 5;

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
      /* do what you need to do */
                refreshGUI(mController.mCache.motorLeft, mController.mCache.motorRight, mController.mCache.power, mController.mCache.steering);
      /* and here comes the "trick" */
                mHandler.postDelayed(this, 100);
            }
        };
        mHandler.postDelayed(runnable, 100);
        //mController.Resume();
    }

    public void setController(Controller mController){
        this.mController = mController;
    }

    public void refreshGUI(float motorLeft, float motorRight, float power, float steering) { //gui
        TextView text = (TextView)findViewById(R.id.number_1);

        // Format so decimals are not shown
        DecimalFormat nd = new DecimalFormat("###");
        text.setText("Motor Left: " + nd.format(motorLeft));
        text = (TextView)findViewById(R.id.number_2);
        text.setText("Motor Right: " + nd.format(motorRight));
        text = (TextView)findViewById(R.id.number_3);
        text.setText(nd.format(power));
        text = (TextView)findViewById(R.id.number_4);
        text.setText(nd.format(steering));
    }

}
