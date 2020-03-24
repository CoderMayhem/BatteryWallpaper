package com.example.batterywallpaper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.Paint;
import android.os.BatteryManager;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;

public class BatteryWallpaperService extends WallpaperService {

    @Override
    public BatteryWallpaperService.Engine onCreateEngine() {
        BatteryManager bm = (BatteryManager)getSystemService(BATTERY_SERVICE);
        int batPerc = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        return new BatteryWallpaperEngine(batPerc);

    }

    private class BatteryWallpaperEngine extends BatteryWallpaperService.Engine{

        private SurfaceHolder holder;
        private int batPerc;
        private boolean visible;
        private Handler handler;


        BatteryWallpaperEngine(int batPerc){
            this.batPerc = batPerc;
            handler = new Handler();
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            this.holder = surfaceHolder;
        }

        private Runnable colorChange = new Runnable() {
            @Override
            public void run() {
                draw();
                paint();
            }
        };

        private void draw(){
            if(visible){
                Canvas canvas = holder.lockCanvas();
                canvas.save();
                //Adjust size and position

                canvas.scale(4f,4f);
                canvas.restore();
                holder.unlockCanvasAndPost(canvas);
                handler.removeCallbacks(colorChange);

            }
        }
        private void paint(){
            if(visible){
                float b;
                long r, g;

                b = (batPerc)/100;
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                paint.setColor(Color.rgb((1-b)*225,b*225,0));

                handler.removeCallbacks(colorChange);

            }
        }


        @Override
        public void onVisibilityChanged(boolean visible) {
            this.visible = visible;
            if (visible){
                handler.post(colorChange);
            }else{
                handler.removeCallbacks(colorChange);
            }
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            handler.removeCallbacks(colorChange);
        }
    }
}
