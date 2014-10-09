package com.coba.arcmenu;


import com.capricorn.ArcLayout;
import com.capricorn.ArcMenu;
import com.capricorn.RayMenu;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final int[] ITEM_DRAWABLES = { R.drawable.composer_camera, R.drawable.composer_music,
		R.drawable.composer_place/*, R.drawable.composer_sleep, R.drawable.composer_thought, R.drawable.composer_with*/ };
	
	int x, y;
	ArcMenu arcMenu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final RelativeLayout rlBase = (RelativeLayout)findViewById(R.id.rlBase);
		rlBase.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				final int action = event.getAction();
				switch(action & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:
					x = (int)event.getX();
					y = (int)event.getY();
					Log.d("location touch","x:"+x+" y:"+y+" w:"+rlBase.getWidth()+" h:"+rlBase.getHeight());
					break;
				}
				return false;
			}
		});
		
		rlBase.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				return false;
			}
		});
		
		final ImageView ivButton = (ImageView)findViewById(R.id.ivButton);
		ivButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(arcMenu.isItemExpanded())
					arcMenu.opencloseItem();
				else
					Toast.makeText(MainActivity.this, "click", Toast.LENGTH_SHORT).show();
			}
		});
		
		ivButton.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				final int itemCount = ITEM_DRAWABLES.length;
				final int childSize = dpToPx(70);
				rlBase.removeView(arcMenu);
				arcMenu = new ArcMenu(MainActivity.this, ArcMenu.RAINBOW, ivButton, rlBase.getWidth(), rlBase.getHeight(), childSize, itemCount);

				for (int i = 0; i < itemCount; i++) {
		            ImageView item = new ImageView(MainActivity.this);
		            item.setImageResource(ITEM_DRAWABLES[i]);

		            final int position = i;
		            arcMenu.addItem(item, new OnClickListener() {

		                @Override
		                public void onClick(View v) {
		                    Toast.makeText(MainActivity.this, childSize+" position:" + position, Toast.LENGTH_SHORT).show();
		                }
		            });
		        }
	        	arcMenu.opencloseItem();
		        rlBase.addView(arcMenu);
				return true;
			}
		});
		
	}
	
	private int dpToPx(int dp)  {
    	DisplayMetrics displayMetrics = MainActivity.this.getResources().getDisplayMetrics();
        return (int) ((dp*displayMetrics.density)+0.5);
        
//        float density = mContext.getResources().getDisplayMetrics().density;
//        return Math.round((float)dp * density);
//    	return dp;
    }

}
