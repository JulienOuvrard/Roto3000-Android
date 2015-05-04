package fr.m1alma.ihm.roto3000;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.graphics.Path;
        import android.graphics.PorterDuff;
        import android.graphics.PorterDuffXfermode;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
        import android.view.MotionEvent;
        import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * A drawing zone view
 */

public class zoneDessin extends View implements colorPicker.OnColorChangedListener{

    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint;
    //initial color
    private int paintColor = 0xFF000000;
    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap;
    //brush sizes
    private float brushSize, lastBrushSize;
    //erase flag
    private boolean erase=false;

    public zoneDessin(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();
    }

    //setup drawing
    private void setupDrawing(){

        //prepare for drawing and setup paint stroke properties
        brushSize = 20;
        lastBrushSize = brushSize;
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(brushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    //size assigned to view
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    //draw the view - will be called after touch event
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    //register user touches as drawing action
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        //respond to down, move and up events
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawPath.lineTo(touchX, touchY);
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }
        //redraw
        invalidate();
        return true;

    }

    //update color
    public void setColor(String newColor){
        invalidate();
        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
    }
    
    public int getPaintColor(){
        return paintColor;
    }
    //set brush size
    public void setBrushSize(float newSize){
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        brushSize=pixelAmount;
        drawPaint.setStrokeWidth(brushSize);
    }

    //get and set last brush size
    public void setLastBrushSize(float lastSize){
        lastBrushSize=lastSize;
    }
    public float getLastBrushSize(){
        return lastBrushSize;
    }

    //set erase true or false
    public void setErase(boolean isErase){

        erase=isErase;
        if(erase) drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        else drawPaint.setXfermode(null);
    }

    //start new drawing
    public void startNew(){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    @Override
    public void colorChanged(int color) {
        paintColor=color;
        drawPaint.setColor(paintColor);
    }

    public void saveDraw(Context c, long i){
        //save drawing
        this.setDrawingCacheEnabled(true);


        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Roto3000Drawings");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Drawing", "failed to create directory");
            }
        }

        OutputStream fOut=null;
        this.setBackgroundColor(0xFFFFFFFF);
        Bitmap bm = this.getDrawingCache();
        File f = new File(mediaStorageDir.toString(), "drawing_"+i+".png");
        try {

            fOut = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            Toast.makeText(c,
                    "Drawing saved to Gallery!", Toast.LENGTH_SHORT).show();

        }catch(Exception e){
            e.printStackTrace();
        } finally {
            try{
                if(fOut!=null){
                    fOut.flush();
                    fOut.close();
                }
            }catch(IOException e){
                e.printStackTrace();
            }

        }

        this.destroyDrawingCache();
        startNew();
    }
}
