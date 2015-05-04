package fr.m1alma.ihm.roto3000;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Welcome activity
 * Welcome screen with 2 options : New Project or Open Project
 */

public class welcome extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
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
     * Function called to open a project
     * @param view
     */
    public void openProject(View view) {

        Intent intent = new Intent(this, openProject.class);
        startActivity(intent);
    }

    /**
     * Function called to create a new project
     * @param view
     */
    public void newProject(View view) {

        Intent intent = new Intent(this, chooseVideo.class);
        startActivity(intent);
    }
}
