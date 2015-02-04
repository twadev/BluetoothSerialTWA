package com.cicsystems.siramob.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Utils
{

	public static AlertDialog showDialog(Context ctx, String msg, String btn1,
			String btn2, DialogInterface.OnClickListener listener1,
			DialogInterface.OnClickListener listener2)
	{

		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		// builder.setTitle(R.string.app_name);
		builder.setMessage(msg).setCancelable(false)
				.setPositiveButton(btn1, listener1);
		if (btn2 != null && listener2 != null)
			builder.setNegativeButton(btn2, listener2);

		AlertDialog alert = builder.create();
		alert.show();
		return alert;

	}

	public static AlertDialog showDialog(Context ctx, int msg, int btn1,
			int btn2, DialogInterface.OnClickListener listener1,
			DialogInterface.OnClickListener listener2)
	{

		return showDialog(ctx, ctx.getString(msg), ctx.getString(btn1),
				ctx.getString(btn2), listener1, listener2);

	}

	public static AlertDialog showDialog(Context ctx, String msg, String btn1,
			String btn2, DialogInterface.OnClickListener listener)
	{

		return showDialog(ctx, msg, btn1, btn2, listener,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id)
					{

						dialog.dismiss();
					}
				});

	}

	public static AlertDialog showDialog(Context ctx, int msg, int btn1,
			int btn2, DialogInterface.OnClickListener listener)
	{

		return showDialog(ctx, ctx.getString(msg), ctx.getString(btn1),
				ctx.getString(btn2), listener);

	}

	public static AlertDialog showDialog(Context ctx, String msg,
			DialogInterface.OnClickListener listener)
	{

		return showDialog(ctx, msg, ctx.getString(android.R.string.ok), null,
				listener, null);
	}

	public static AlertDialog showDialog(Context ctx, int msg,
			DialogInterface.OnClickListener listener)
	{

		return showDialog(ctx, ctx.getString(msg),
				ctx.getString(android.R.string.ok), null, listener, null);
	}

	public static AlertDialog showDialog(Context ctx, String msg)
	{

		return showDialog(ctx, msg, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id)
			{

				dialog.dismiss();
			}
		});

	}

	public static AlertDialog showDialog(Context ctx, int msg)
	{

		return showDialog(ctx, ctx.getString(msg));

	}

	public static void showDialog(Context ctx, int title, int msg,
			DialogInterface.OnClickListener listener)
	{

		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setMessage(msg).setCancelable(false)
				.setPositiveButton(android.R.string.ok, listener);
		builder.setTitle(title);
		AlertDialog alert = builder.create();
		alert.show();
	}

	public static final boolean isOnline()
	{

		return isOnline(StaticData.appContext);
	}

	public static final boolean isOnline(Context ctx)
	{

		ConnectivityManager conMgr = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (conMgr.getActiveNetworkInfo() != null

		&& conMgr.getActiveNetworkInfo().isAvailable()

		&& conMgr.getActiveNetworkInfo().isConnected())
			return true;
		return false;
	}

	public static boolean isValidEmail(String email)
	{

		String emailExp = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,10}$";
		Pattern pattern = Pattern.compile(emailExp, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public static boolean isValidPhoneNumber(String number)
	{

		String numExp = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{2,15})$";
		Pattern pattern = Pattern.compile(numExp, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(number);
		return matcher.matches();
	}

	public static boolean isNumeric(String number)
	{

		String numExp = "^[-+]?[0-9]*\\.?[0-9]+$";
		Pattern pattern = Pattern.compile(numExp, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(number);
		return matcher.matches();
	}

	public static final void hideKeyboard(Activity ctx)
	{

		if (ctx.getCurrentFocus() != null)
		{
			InputMethodManager imm = (InputMethodManager) ctx
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(ctx.getCurrentFocus().getWindowToken(),
					0);
		}
	}

	public static final void hideKeyboard(Activity ctx, View v)
	{

		try
		{
			InputMethodManager imm = (InputMethodManager) ctx
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static final void showKeyboard(Activity ctx, View v)
	{

		try
		{
			v.requestFocus();
			InputMethodManager imm = (InputMethodManager) ctx
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
			// imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
			// InputMethodManager.SHOW_IMPLICIT);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static final void makeCall(final Activity act, final String number)
	{

		Utils.showDialog(act, "Call " + number.replace(" ", ""), "Ok",
				"Cancel", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which)
					{

						Intent call = new Intent(Intent.ACTION_CALL);
						call.setData(Uri.parse("tel:" + number.trim()));
						act.startActivity(call);
					}
				}).show();
	}

	public static final Object deSerialiseObj(byte[] obj)
	{

		if (obj != null && obj.length > 0)
		{
			try
			{
				ObjectInputStream in = new ObjectInputStream(
						new ByteArrayInputStream(obj));
				Object o = in.readObject();
				in.close();
				return o;
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	public static final byte[] serialiseObj(Serializable obj)
	{

		try
		{
			ByteArrayOutputStream bArr = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bArr);
			out.writeObject(obj);
			byte b[] = bArr.toByteArray();
			out.close();
			bArr.close();
			return b;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static void copyFile(File src, File dst)
	{

		try
		{
			if (!dst.exists())
				dst.createNewFile();
			FileInputStream in = new FileInputStream(src);
			FileOutputStream out = new FileOutputStream(dst);

			int size = (int) src.length();
			byte[] buf = new byte[size];
			in.read(buf);
			out.write(buf);
			out.flush();
			out.close();
			in.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void createNoMediaFile(File dir)
	{

		try
		{
			File f = new File(dir, ".nomedia");
			if (!f.exists())
				f.createNewFile();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static String getMD5String(String in)
	{

		MessageDigest digest;
		try
		{
			digest = MessageDigest.getInstance("MD5");
			digest.reset();
			digest.update(in.getBytes());
			byte[] a = digest.digest();
			int len = a.length;
			StringBuilder sb = new StringBuilder(len << 1);
			for (int i = 0; i < len; i++)
			{
				sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
				sb.append(Character.forDigit(a[i] & 0x0f, 16));
			}
			// Log.e("MD5", in+"--"+sb.toString());
			return sb.toString();
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public static void changeLocale(Activity act, Locale locale)
	{

		// Locale locale = new Locale(lang);
		// Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		act.getBaseContext()
				.getResources()
				.updateConfiguration(config,
						act.getBaseContext().getResources().getDisplayMetrics());
	}

	public static String getFormattedCount(int count)
	{

		if (count >= 100000)
			return "1M";
		if (count >= 10000)
			return "10K";
		if (count >= 1000)
			return "1K";
		return count + "";
	}

	public static String getBase64ImageString(String file)
	{

		if (file == null)
			return null;
		try
		{
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			BufferedInputStream bin = new BufferedInputStream(
					new FileInputStream(file));
			int b;
			while ((b = bin.read()) != -1)
				bout.write(b);
			byte img[] = bout.toByteArray();
			bout.flush();
			bout.close();
			bin.close();
			return Base64.encodeToString(img, Base64.DEFAULT);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static String getStringFromAsset(String path)
	{
		try
		{
			BufferedReader din = new BufferedReader(new InputStreamReader(
					StaticData.appContext.getAssets().open(path)));

			String str = "";
			String s;
			while ((s = din.readLine()) != null)
				str = str + s;
			din.close();
			return str;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static int parseDrawableId(String str)
	{
		return StaticData.res.getIdentifier(str, "drawable",
				StaticData.appContext.getPackageName());
	}

}
