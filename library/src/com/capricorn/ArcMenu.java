/*
 * Copyright (C) 2012 Capricorn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.capricorn;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * A custom view that looks like the menu in <a href="https://path.com">Path
 * 2.0</a> (for iOS).
 * 
 * @author Capricorn
 * 
 */
public class ArcMenu extends RelativeLayout {
    private ArcLayout mArcLayout;

    private ImageView mHintView;
    
    public ViewGroup controlLayout;
    
    private LayoutParams params;

    public ArcMenu(Context context) {
        super(context);
        init(context);
    }

    public ArcMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        applyAttrs(attrs);
    }
    
    /* fas nambah*/
    
    private int mMenuMode = -1;
	public static final int FAN = 0;
	public static final int RAINBOW = 1;
	public static final int UPWARD = 2;
	public static final int CUSTOM = 3;
	
	private int mX, mY;
	private int baseWidth, baseHeight;
	private int childSize, childCount;
	private View v;
    
    public ArcMenu(Context context, int menuMode, ArcLayout layout, int x, int y, int baseWidth, int baseHeight, int childSize) {
    	super(context);
    	mX = x;
    	mY = y;
    	this.baseWidth = baseWidth;
    	this.baseHeight = baseHeight;
    	setChildSize(childSize);
    	mMenuMode = menuMode;
    	init(context);
    	applyArcLayout(layout);
    }
    
    public ArcMenu(Context context, int menuMode, int x, int y, int baseWidth, int baseHeight, int childSize) {
    	super(context);
    	mX = x;
    	mY = y;
    	this.baseWidth = baseWidth;
    	this.baseHeight = baseHeight;
    	setChildSize(childSize);
    	mMenuMode = menuMode;
    	init(context);
    	applyLayout(menuMode);
    }
    
    public ArcMenu(Context context, int menuMode, View view, int baseWidth, int baseHeight, int childSize, int childCount) {
    	super(context);
    	removeAllViews();
    	mX = view.getLeft() + (view.getWidth()/2);
    	mY = view.getTop() + (view.getHeight()/2);
    	v = view;
    	this.baseWidth = baseWidth;
    	this.baseHeight = baseHeight;
    	setChildSize(childSize);
    	this.childCount = childCount;
    	mMenuMode = menuMode;
    	init(context);
    	applyLayout(menuMode);
    }
    
    public ArcMenu(Context context, ArcLayout layout, View view, int baseWidth, int baseHeight, int childCount) {
    	super(context);
    	removeAllViews();
    	mX = view.getLeft() + (view.getWidth()/2);
    	mY = view.getTop() + (view.getHeight()/2);
    	v = view;
    	this.baseWidth = baseWidth;
    	this.baseHeight = baseHeight;
    	this.childCount = childCount;
    	mMenuMode = CUSTOM;
    	init(context);
    	applyArcLayout(layout);
    }
    
    private void applyLayoutParams () {
    	params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    	switch (mMenuMode) {
    	case FAN:
    		params.leftMargin = mX - (controlLayout.getMeasuredWidth()/2);
            params.topMargin = mY - (controlLayout.getMeasuredHeight()/2) - mArcLayout.getMeasuredHeight();
            break;
    	case RAINBOW:
    		int padding = (mArcLayout.getChildPadding() + mArcLayout.getLayoutPadding()) * (childCount+1);
    		params.leftMargin = mX - ((childSize*childCount)/2) - padding/*mArcLayout.getMeasuredWidth()*/;
//            params.bottomMargin = baseHeight - mY;
    		Log.d(childSize+"."+childCount,"test"+(childSize*childCount)+" measure"+mArcLayout.getMeasuredWidth()+"/"+mArcLayout.getMeasuredHeight());
    		params.topMargin = mY - ((childSize*childCount)/2) + /*mArcLayout.getMeasuredHeight()*/ - (controlLayout.getMeasuredHeight());
    		break;
    	case -1 :
			params.leftMargin = mX;
			params.topMargin = mY;
    		break;	
    	}
    	Log.d("2",params.bottomMargin+".."+((View)mArcLayout.getParent()).getWidth());
    }
    
    private void applyLayout(int menuMode) {
    	switch (menuMode) {
    	case FAN:
    		mArcLayout.setArc(270.0f, 360.0f);
    		break;
    	case RAINBOW:
    		mArcLayout.setArc(210.0f, 330.0f);
    		break;
    	}
    	mArcLayout.setChildSize(childSize);
    }
    
    public void setChildSize(int childSize) {
    	this.childSize = childSize;
    }
    
    public void opencloseItem() {
    	mHintView.startAnimation(createHintSwitchAnimation(mArcLayout.isExpanded()));
		mArcLayout.switchState(true);
		Log.d("","after open item:"+mArcLayout.getMeasuredWidth());
    }
    
    public boolean isItemExpanded() {
    	return mArcLayout.isExpanded();
    }
    
    /* end of fas nambah */

    private void init(Context context) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.arc_menu, this);

        mArcLayout = (ArcLayout) findViewById(R.id.item_layout);
        controlLayout = (ViewGroup) findViewById(R.id.control_layout);

        /* fas */
        mArcLayout.measure(0, 0);
        controlLayout.measure(0, 0);
        applyLayoutParams();
        
        LayoutParams test = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        switch (mMenuMode) {
        case FAN:
        	test.leftMargin = params.leftMargin;
        	test.topMargin = mY - controlLayout.getMeasuredHeight();
        	break;
        case RAINBOW:
        	test.leftMargin = /*mX - controlLayout.getMeasuredWidth()*/ v.getLeft();
            test.topMargin = /*params.topMargin + mArcLayout.getRadius()*//*mY - controlLayout.getMeasuredHeight()*/ v.getTop();
            break;
        }
        
        /*v.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mArcLayout.isExpanded()) 
					openItem();
			}
		});*/
        /* eof */
        
        mArcLayout.setLayoutParams(params);

        controlLayout.setClickable(true);
        controlLayout.setLayoutParams(test);
        controlLayout.setVisibility(View.GONE);
        controlLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Log.d("click","click");
				if(mArcLayout.isExpanded()) 
					opencloseItem();
			}
		});

        /*controlLayout.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mHintView.startAnimation(createHintSwitchAnimation(mArcLayout.isExpanded()));
                    mArcLayout.switchState(true);
                }

                return false;
            }
        });*/

        mHintView = (ImageView) findViewById(R.id.control_hint);
    }

    private void applyAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ArcLayout, 0, 0);

            float fromDegrees = a.getFloat(R.styleable.ArcLayout_fromDegrees, ArcLayout.DEFAULT_FROM_DEGREES);
            float toDegrees = a.getFloat(R.styleable.ArcLayout_toDegrees, ArcLayout.DEFAULT_TO_DEGREES);
            mArcLayout.setArc(fromDegrees, toDegrees);

            int defaultChildSize = mArcLayout.getChildSize();
            int newChildSize = a.getDimensionPixelSize(R.styleable.ArcLayout_childSize, defaultChildSize);
            mArcLayout.setChildSize(newChildSize);

            a.recycle();
        }
    }
    
    private void applyArcLayout(ArcLayout layout) {
    	mArcLayout.setArc(layout.getStartDegree(), layout.getEndDegree());
    	mArcLayout.setChildSize(layout.getChildSize());
    }

    public void addItem(View item, OnClickListener listener) {
        mArcLayout.addView(item);
        item.setOnClickListener(getItemClickListener(listener));
    }

    private OnClickListener getItemClickListener(final OnClickListener listener) {
        return new OnClickListener() {

            @Override
            public void onClick(final View viewClicked) {
                Animation animation = bindItemAnimation(viewClicked, true, 400);
                animation.setAnimationListener(new AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                itemDidDisappear();
                            }
                        }, 0);
                    }
                });

                final int itemCount = mArcLayout.getChildCount();
                for (int i = 0; i < itemCount; i++) {
                    View item = mArcLayout.getChildAt(i);
                    if (viewClicked != item) {
                        bindItemAnimation(item, false, 300);
                    }
                }

                mArcLayout.invalidate();
                mHintView.startAnimation(createHintSwitchAnimation(true));

                if (listener != null) {
                    listener.onClick(viewClicked);
                }
            }
        };
    }

    private Animation bindItemAnimation(final View child, final boolean isClicked, final long duration) {
        Animation animation = createItemDisapperAnimation(duration, isClicked, mArcLayout);
        child.setAnimation(animation);

        return animation;
    }

    private void itemDidDisappear() {
        final int itemCount = mArcLayout.getChildCount();
        for (int i = 0; i < itemCount; i++) {
            View item = mArcLayout.getChildAt(i);
            item.clearAnimation();
        }
        mArcLayout.removeAllViews();
        mArcLayout.switchState(false);
    }

    private static Animation createItemDisapperAnimation(final long duration, final boolean isClicked, ArcLayout mArcLayout) {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(new ScaleAnimation(1.0f, isClicked ? 2.0f : 0.0f, 1.0f, isClicked ? 2.0f : 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f));
        animationSet.addAnimation(new AlphaAnimation(1.0f, 0.0f));

        animationSet.setDuration(duration);
        animationSet.setInterpolator(new DecelerateInterpolator());
        animationSet.setFillAfter(true);
        
        return animationSet;
    }

    private static Animation createHintSwitchAnimation(final boolean expanded) {
        Animation animation = new RotateAnimation(expanded ? 45 : 0, expanded ? 0 : 45, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setStartOffset(0);
        animation.setDuration(100);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setFillAfter(true);

        return animation;
    }
}
