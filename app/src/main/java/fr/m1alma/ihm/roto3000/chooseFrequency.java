package fr.m1alma.ihm.roto3000;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

/**
 * Choose frequency activity
 * Here you can choose the frequency of your animation
 */

public class chooseFrequency extends Activity {
    public String videoPath;
    public String frequency="24"; //default frequency is 24fps

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_frequency);

        Bundle bundle = getIntent().getExtras();
        videoPath = bundle.getString("path_to_vid");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_frequency, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Function called to go to the drawing area
     * @param view
     */
    public void goDraw(View view) {
        Intent drawingArea = new Intent(this, roto3000.class);
        drawingArea.putExtra("frequency", frequency);
        drawingArea.putExtra("resourcePath", videoPath);
        startActivity(drawingArea);
    }

    /**
     * Listener on the radio buttons
     * @param view
     */
    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.six_fps:
                if (checked) {
                    frequency = "6";
                }
                break;
            case R.id.height_fps:
                if (checked) {
                    frequency = "8";
                }
                break;
            case R.id.twelve_fps:
                if (checked) {
                    frequency = "12";
                }
                break;
            case R.id.twentyfour_fps:
                if (checked) {
                    frequency = "24";
                }
                break;
        }
    }
}
