package fr.m1alma.ihm.roto3000;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * Main activity
 */

public class roto3000 extends Activity implements OnClickListener{

    //buttons
    private ImageButton menuBtn, drawBtn, eraseBtn, colorBtn, sizeBtn, layersViewBtn;
    private ToggleButton bkgToggle;
    private Button suivBtn, prevBtn;

    private zoneDessin zoneDess;
    private long draw_index, freq, framerate;
    private MediaMetadataRetriever retriever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        draw_index=0;

        Bundle bundle = getIntent().getExtras();
        String video= bundle.getString("resourcePath");

        String frequency= bundle.getString("frequency");
        freq= Long.valueOf(frequency).longValue();

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_roto3000);

        zoneDess = (zoneDessin) findViewById(R.id.drawzone);

        menuBtn = (ImageButton) findViewById(R.id.button_menu);
        menuBtn.setOnClickListener(this);

        drawBtn = (ImageButton) findViewById(R.id.button_pen);
        drawBtn.setOnClickListener(this);

        eraseBtn = (ImageButton) findViewById(R.id.button_erase);
        eraseBtn.setOnClickListener(this);

        colorBtn = (ImageButton) findViewById(R.id.button_color);
        colorBtn.setOnClickListener(this);

        sizeBtn = (ImageButton) findViewById(R.id.button_size);
        sizeBtn.setOnClickListener(this);

        bkgToggle = (ToggleButton) findViewById(R.id.toggleButton_background);
        bkgToggle.setOnClickListener(this);

        layersViewBtn = (ImageButton) findViewById(R.id.layers_view);
        layersViewBtn.setOnClickListener(this);

        suivBtn = (Button) findViewById(R.id.next_frame);
        suivBtn.setOnClickListener(this);

        prevBtn = (Button) findViewById(R.id.prev_frame);
        prevBtn.setOnClickListener(this);
        retriever = new MediaMetadataRetriever();
        retriever.setDataSource(video);

        MediaPlayer mp = MediaPlayer.create(this,  Uri.parse(video));
        int duration = mp.getDuration();
        mp.release();

        long dur= (long) duration;
        long frame_nbr=dur*freq;
        framerate=(long)((float) dur/frame_nbr*100);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_roto3000, menu);
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

    @Override
    public void onClick(View view){
        if(view.getId()==R.id.button_menu){
            PopupMenu popup = new PopupMenu(this, view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_roto3000, popup.getMenu());
            popup.show();
        }
        if(view.getId()==R.id.button_pen){
            zoneDess.setErase(false);
        }
        if(view.getId()==R.id.button_size){
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Brush size:");
            brushDialog.setContentView(R.layout.size_chooser);

            final NumberPicker size = (NumberPicker)brushDialog.findViewById(R.id.brushSize);
            size.setMinValue(5);
            size.setMaxValue(50);
            size.setValue((int) zoneDess.getLastBrushSize());
            size.setOnValueChangedListener(new NumberPicker.OnValueChangeListener(){
                @Override
                public void onValueChange(NumberPicker np,int old, int n) {
                    zoneDess.setErase(false);
                    Float s= Float.valueOf(n);
                    zoneDess.setBrushSize(s);
                    zoneDess.setLastBrushSize(s);
                }
            });
            brushDialog.show();
        }
        if(view.getId()==R.id.button_color){
            new colorPicker(this,zoneDess,zoneDess.getPaintColor()).show();
        }
        if(view.getId()==R.id.button_erase){
            zoneDess.setBrushSize(zoneDess.getLastBrushSize());
            zoneDess.setErase(true);
        }
        if(view.getId()==R.id.toggleButton_background){
            if(bkgToggle.isChecked()){
                long time=10000*framerate*draw_index;
                Bitmap bm=retriever.getFrameAtTime(time,MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                zoneDess.setBackground(new BitmapDrawable(bm));
            }else{
                zoneDess.setBackgroundColor(0xFFFFFFFF);
            }
        }
        if(view.getId()==R.id.next_frame){
            bkgToggle.setChecked(false);
            zoneDess.saveDraw(this.getApplicationContext(),draw_index);
            draw_index++;
        }
    }
}
