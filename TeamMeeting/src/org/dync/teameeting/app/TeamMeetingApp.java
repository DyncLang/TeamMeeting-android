package org.dync.teameeting.app;

import java.util.ArrayList;

import org.dync.teameeting.receiver.NetWorkReceiver;

import cn.jpush.android.api.JPushInterface;

import android.app.Application;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.Toast;

public class TeamMeetingApp extends Application
{

	public static boolean mIsDebug = true;// 是否需要打印bug，
	private static final String TAG = "Application";
	private static final boolean mDebug = true;
	private static TeamMeetingApp mTeamMeetingApp;
	private static MySelf mMySelf;
	private NetWorkReceiver mNetReceiver;

	@Override
	public void onCreate()
	{
		// TODO Auto-generated method stub
		super.onCreate();
		mMySelf = new MySelf();
		registerReceiver();

		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);

	}

	/**
	 * registerReceiver
	 */
	private void registerReceiver()
	{
		mNetReceiver = new NetWorkReceiver();
		IntentFilter netFilter = new IntentFilter();
		netFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mNetReceiver, netFilter);
	}

	public TeamMeetingApp()
	{
		super();
		// TODO Auto-generated constructor stub
		mTeamMeetingApp = this;
	}

	public static TeamMeetingApp getTeamMeetingApp()
	{

		return mTeamMeetingApp;
	}

	public static MySelf getMyself()
	{
		return mMySelf;
	}

	/**
	 * get the Token from manifest.xml
	 * 
	 * @return the value of Token
	 */
	public String getToken()
	{
		Bundle bundle = null;
		String tokenKey = "";
		try
		{
			ApplicationInfo info = getPackageManager().getApplicationInfo(getPackageName(),
					PackageManager.GET_META_DATA);
			bundle = info.metaData;// 获取metaData标签内容
			if (bundle != null)
			{
				// 获取AndroidManifest.xml文件中ZHUAMOB_APPKEY
				tokenKey = bundle.getString("TOKEN");// 这里获取的就是value值
			}
		} catch (PackageManager.NameNotFoundException localNameNotFoundException1)
		{

		}
		return tokenKey;
	}

	/**
	 * to destroy the memory;
	 * 
	 * this method must be called when the app exit
	 */
	public void Destroy()
	{

		unregisterReceiver(mNetReceiver);
	}

	/**
	 * Judge if device has SD card
	 */
	public boolean hasSdcard()
	{
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED))
		{
			return true;
		} else
		{
			return false;
		}
	}

	public void showToast(String msg)
	{
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	public void showToast(int string)
	{
		Toast.makeText(this, getString(string), Toast.LENGTH_SHORT).show();
	}

	public String getVersionName()
	{
		PackageInfo pkgInfo = null;
		try
		{
			pkgInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pkgInfo.versionName;
	}

	public int getVersionCode()
	{
		PackageInfo pkgInfo = null;
		try
		{
			pkgInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pkgInfo.versionCode;
	}

	/**
	 * get the device id unique
	 * 
	 * @return the device id
	 */
	public String getDevId()
	{
		return Secure.getString(getContentResolver(), Secure.ANDROID_ID);
	}

}
