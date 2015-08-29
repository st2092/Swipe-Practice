package com.example.sony.swipepractice;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set up OnSwipeListener for text view
        TextView text_view = (TextView) findViewById(R.id.text_view_swipe);
        text_view.setOnTouchListener(new OnSwipeListener(this) {
            {
                setDragHorizontal(true);
                setExitScreenOnSwipe(true);
                setAnimationDelay(2000);    // 2 seconds
                setDistanceThreshold(200);
                setVelocityThreshold(200);
            }

            @Override
            public void
            onSwipeLeft(float distance) {
                Toast.makeText(MainActivity.this, "Swiped left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void
            onSwipeRight(float distance) {
                Toast.makeText(MainActivity.this, "Swiped right", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void
            onSwipeUp(float distance)
            {
                Toast.makeText(MainActivity.this, "Swiped up", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void
            onSwipeDown(float distance)
            {
                Toast.makeText(MainActivity.this, "Swiped down", Toast.LENGTH_SHORT).show();
            }
        });

        // set up OnSwipeListener for ImageButton
        ImageButton image_button = (ImageButton) findViewById(R.id.image_button_swipe);
        image_button.setOnTouchListener(new OnSwipeListener(this) {
            {
                setDragHorizontal(true);
                setDragVertical(true);
                setExitScreenOnSwipe(true);
                setAnimationDelay(1000);    // 1 seconds
                setDragSnapBack(true);
                setAnimated(true);
                setDistanceThreshold(100);
                setVelocityThreshold(100);
            }

            @Override
            public void
            onSwipeLeft(float distance)
            {
                Toast.makeText(MainActivity.this, "Swiped image left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void
            onSwipeRight(float distance)
            {
                Toast.makeText(MainActivity.this, "Swiped image right", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void
            onSwipeUp(float distance)
            {
                Toast.makeText(MainActivity.this, "Swiped image up", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void
            onSwipeDown(float distance)
            {
                Toast.makeText(MainActivity.this, "Swiped image down", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
     * This function gets call when the ImageButton gets press.
     * A short toast is display to indicate the ImageButton was pressed.
     */
    public void
    onImageButtonClick(View view)
    {
        Toast.makeText(this, "Image button clicked", Toast.LENGTH_SHORT).show();
    }
}
