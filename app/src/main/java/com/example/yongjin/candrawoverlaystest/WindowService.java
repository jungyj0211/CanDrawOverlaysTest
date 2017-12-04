package com.example.yongjin.candrawoverlaystest;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class WindowService extends Service {

    WindowManager windowManager;
    WindowManager.LayoutParams layoutParams;
    ClipData dragData;
    ImageView imageView;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(getApplicationContext(), "WindowService", Toast.LENGTH_SHORT).show();

        imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.ic_add_circle_black_24dp);
        imageView.setTag("plusIcon");

        layoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_PHONE);
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.format = PixelFormat.TRANSLUCENT;
        layoutParams.gravity = Gravity.TOP|Gravity.LEFT;

        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(imageView,layoutParams);

        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "터치됨", Toast.LENGTH_SHORT).show();

            }
        });

        imageView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {

                String[] description = {ClipDescription.MIMETYPE_TEXT_PLAIN};

                ClipData.Item item = new ClipData.Item(String.valueOf(view.getTag()));

                // Create a new ClipData using the tag as a label, the plain text MIME type, and
                // the already-created item. This will create a new ClipDescription object within the
                // ClipData, and set its MIME type entry to "text/plain"
                dragData = new ClipData(view.getTag().toString(), description, item);

                // Instantiates the drag shadow builder.
                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(imageView);

                // Starts the drag

                view.startDrag(dragData,  // the data to be dragged
                        myShadow,  // the drag shadow builder
                        view,      // no need to use local data
                        0          // flags (not currently used, set to 0)
                );

               // view.setVisibility(View.INVISIBLE);

                return true;
            }
        });

        imageView.setOnDragListener(new View.OnDragListener() {

            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onDrag(View v, DragEvent event) {

                switch (event.getAction()) {

                    case DragEvent.ACTION_DRAG_STARTED:
                        Log.d("DragClickListener", "ACTION_DRAG_STARTED");
                        initialX = layoutParams.x;
                        initialY = layoutParams.y;

                        //get the touch location
                        initialTouchX = event.getX();
                        initialTouchY = event.getY();

                        break;

                    case DragEvent.ACTION_DRAG_ENTERED:
                        Log.d("DragClickListener", "ACTION_DRAG_ENTERED");
                        break;

                    case DragEvent.ACTION_DRAG_EXITED:
                        Log.d("DragClickListener", "ACTION_DRAG_EXITED");
                        break;

                    case DragEvent.ACTION_DROP:
                        Log.d("DragClickListener", "ACTION_DROP");



                        break;

                    case DragEvent.ACTION_DRAG_ENDED:
                        Log.d("DragClickListener", "ACTION_DRAG_ENDED");
                        layoutParams.x = initialX + (int) (event.getX() - initialTouchX);
                        layoutParams.y = initialY + (int) (event.getY() - initialTouchY);


                        //Update the layout with new X & Y coordinate
                        windowManager.updateViewLayout(imageView, layoutParams);

                    default:
                        break;
                }
                return true;

            }
        });

        return super.onStartCommand(intent, flags, startId);
    }

    /*
        imageView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View view, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:


                        //remember the initial position.
                        initialX = layoutParams.x;
                        initialY = layoutParams.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        layoutParams.x = initialX + (int) (event.getRawX() - initialTouchX);
                        layoutParams.y = initialY + (int) (event.getRawY() - initialTouchY);


                        //Update the layout with new X & Y coordinate
                        windowManager.updateViewLayout(imageView, layoutParams);
                        return true;
                }
                return false;
            }
        });
*/
/*
    class CanvasShadow extends View.DragShadowBuilder{

        int mWidth, mHeight;

        int mX, mY;

        public CanvasShadow(View v, int x, int y){
            super(v);

            //좌표를 저장해둠
            mWidth = v.getWidth();
            mHeight = v.getHeight();
            mX = x;
            mY = y;
        }

        public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint){
            shadowSize.set(mWidth, mHeight);//섀도우 이미지 크기 지정함
            shadowTouchPoint.set(mX, mY);//섀도우 이미지 중심점을 지정함.
        }

        public void onDrawShadow(Canvas canvas){
            super.onDrawShadow(canvas);//이미지 복사
        }
    }*/

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        windowManager.removeView(imageView);
        super.onDestroy();
    }

}


