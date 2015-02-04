package com.twa.bt.cordova;

public class Log
{
	public static void e(String tag, Object msg)
	{

		android.util.Log.e(tag, "" + msg);
	}

	public static void e(Object msg)
	{

		e("TAG", "" + msg);
	}

	public static void e(String tag, Object[] msg)
	{

		StringBuffer sb = new StringBuffer();
		for (Object o : msg)
			sb.append(o + "__");

		e(tag, sb);
	}

	public static void e(Object[] msg)
	{

		e("TAG", msg);
	}

	/*public static void e(String msg)
	{
		e("TAG", ""+msg);
	}*/

}
