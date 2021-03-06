/**
 * BaseAcitvity.java [V 1.0.0]
 * classes:org.dync.teammeeting.BaseAcitvity
 * Zlang Create at 2015-11-30.下午2:48:42 
 */
package org.dync.teameeting.activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.dync.teameeting.R;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

/**
 * 
 * @author ZLang <br/>
 *         create at 2015-11-30 下午2:48:42
 */
public class BaseAcitvity extends Activity
{

	private RelativeLayout mainView;
	Random random = new Random();

	private boolean isShowMessage = true;
	private List<LinearLayout> viewList;
	private int top;
	private int[] colors = new int[] { R.color.skyblue,
			R.color.blue_btn_bg_color, R.color.grey_txt, R.color.right_bg,
			R.color.grayBlue, R.color.blue_btn_bg_color, R.color.skyblue,
			R.color.vifrification, R.color.grayBlue, R.color.mediumslateblue, };

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mainView = (RelativeLayout) View.inflate(this,
				R.layout.activity_meeting, null);
		setContentView(mainView);
		viewList = new ArrayList<LinearLayout>();
		startShowMessage();
	}

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			if (msg.what == 0 && isShowMessage)
			{
				addAutoView();
				sendEmptyMessageDelayed(0, 1000);
			}
		}
	};

	public void addAutoView()
	{

		MoveDownView();
		final LinearLayout showView = (LinearLayout) View.inflate(this,
				R.layout.text_view, null);
		showView.setBackgroundColor(colors[random.nextInt(10)]);

		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		params.topMargin = 0;

		showView.setLayoutParams(params);
		showView.postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				alphaAnimation(showView, viewList);
			}
		}, 1000);
		viewList.add(showView);
		mainView.addView(showView);
	}

	private void alphaAnimation(final View view,
			final List<LinearLayout> viewList)
	{
		view.clearAnimation();
		ObjectAnimator anim = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
		anim.addListener(new AnimatorListenerAdapter()
		{
			@Override
			public void onAnimationEnd(Animator animation)
			{
				super.onAnimationEnd(animation);
				viewList.remove(view);
			}
		});
		anim.setDuration(3000).start();
	}

	private void MoveDownView()
	{
		int topMargin = 0;
		for (int i = 0; i < viewList.size(); i++)
		{
			LinearLayout linearLayout = viewList.get(i);
			topMargin = linearLayout.getHeight() + 20;
			LayoutParams layoutParams = (LayoutParams) linearLayout
					.getLayoutParams();
			layoutParams.topMargin += topMargin;
			linearLayout.setLayoutParams(layoutParams);
		}
	}

	public void startShowMessage()
	{
		isShowMessage = true;
		handler.sendEmptyMessage(0);
	}

	public void stopShowMessage()
	{
		isShowMessage = false;
		// 移除所有的消息
		handler.removeCallbacksAndMessages(null);
	}
}
