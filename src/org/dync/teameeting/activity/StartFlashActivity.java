package org.dync.teameeting.activity;

import org.dync.teameeting.R;
import org.dync.teameeting.app.NetWork;
import org.dync.teameeting.app.TeamMeetingApp;
import org.dync.teameeting.helper.dlg.DyncInternetDialog;
import org.dync.teameeting.helper.dlg.DyncProgressiveDialog;
import org.dync.teameeting.helper.dlg.DyncInternetDialog.InternetOnClickListener;
import org.dync.teameeting.structs.EventType;
import org.dync.teameeting.structs.NetType;
import org.dync.teameeting.utils.LocalUserInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.ypy.eventbus.EventBus;

/**
 * 
 * @author zhangqilu org.dync.teammeeting.activity StartFlashActivity create at
 *         2015-12-11 17:00:42
 */

public class StartFlashActivity extends Activity
{

	private static final String TAG = "StartFlashActivity";
	private boolean mDebug = TeamMeetingApp.mIsDebug;
	private NetWork mNetWork;
	private DyncProgressiveDialog mProgressiveDialog;
	private DyncInternetDialog mInternetDialog;
	private ImageView mView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_flash);
		mView = (ImageView) findViewById(R.id.splash_image);
		inint();

	}

	/**
	 * inint
	 */
	private void inint()
	{
		EventBus.getDefault().register(this);
		mNetWork = new NetWork(this);
		
		Animation loadAnimation = AnimationUtils.loadAnimation(this, R.anim.splash);
		loadAnimation.setAnimationListener(mAnimationListener);
		mView.setAnimation(loadAnimation);

	}

	private InternetOnClickListener mInternetListener = new InternetOnClickListener()
	{

		@Override
		public void internetOnClick(View v)
		{
			// TODO Auto-generated method stub
			if (mDebug)
			{
				Log.e(TAG, "internetOnClick");
				mInternetDialog.dismiss();

			}
		}
	};

	private AnimationListener mAnimationListener = new AnimationListener()
	{

		@Override
		public void onAnimationStart(Animation arg0)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationRepeat(Animation arg0)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationEnd(Animation arg0)
		{
			// TODO Auto-generated method stub
			// startProgress();
			String userid = TeamMeetingApp.getTeamMeetingApp().getDevId();
			mNetWork.inint(userid, "2", "2", "2", "TeamMeeting");
		}
	};

	/**
	 * open Progress
	 */
	private void startProgress()
	{

		mProgressiveDialog = new DyncProgressiveDialog(this);
		// mProgressiveDialog.setMessage(R.string.str_p2p_connecting);
		mProgressiveDialog.setCancelable(false);
		mProgressiveDialog.show();
	}

	/**
	 * close Progress
	 */
	private void stopProgress()
	{
		if (mProgressiveDialog != null)
		{
			mProgressiveDialog.dismiss();
			mProgressiveDialog = null;
		}
	}

	/**
	 * interfacejump
	 */
	private void interfacejump()
	{

		boolean firstLogin = LocalUserInfo.getInstance(StartFlashActivity.this).getUserInfoBoolean(
				"firstLogin");
		Intent intent;
		if (firstLogin == false)
		{
			intent = new Intent(StartFlashActivity.this, GuideActivity.class);
			LocalUserInfo.getInstance(StartFlashActivity.this).setUserInfoBoolean("firstLogin",
					true);
		} else
		{
			intent = new Intent(StartFlashActivity.this, MainActivity.class);
		}

		startActivity(intent);
		finish();
	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
	}

	/**
	 * For EventBus callback.
	 */
	public void onEventMainThread(Message msg)
	{
		switch (EventType.values()[msg.what])
		{
		case MSG_ININT_SUCCESS:
			if (mDebug)
				Log.e(TAG, "MSG_ININT_SUCCESS");
			String sign = TeamMeetingApp.getMyself().getmAuthorization();
			mNetWork.getRoomList(sign, 1 + "", 20 + "");
			break;

		case MSG_ININT_FAILED:
			if (mDebug)
				Log.e(TAG, "MSG_ININT_FAILED");

			break;
		case MSG_SIGNOUT_SUCCESS:
			if (mDebug)
				Log.e(TAG, "MSG_SIGNOUT_SUCCESS");
			finish();
			System.exit(0);
			break;

		case MSG_SIGNOUT_FAILED:
			if (mDebug)
				Log.e(TAG, "MSG_SIGNOUT_FAILED");
			break;

		case MSG_GET_ROOM_LIST_SUCCESS:
			if (mDebug)
				Log.e(TAG, "MSG_GET_ROOM_LIST_SUCCESS");
			stopProgress();
			interfacejump();
			break;

		case MSG_GET_ROOM_LIST_FAILED:
			if (mDebug)
				Log.e(TAG, "MSG_GET_ROOM_LIST_FAILED");
			break;

		case MSG_NET_WORK_TYPE:

			if (mDebug)
				Log.e(TAG, "MSG_NET_WORK_TYPE");

			int type = msg.getData().getInt("net_type");
			if (type == NetType.TYPE_NULL.ordinal())
			{
				stopProgress();
				mInternetDialog = new DyncInternetDialog(this, mInternetListener);
				mInternetDialog.show();

			} else
			{

			}

			break;

		default:
			break;
		}
	}

}
