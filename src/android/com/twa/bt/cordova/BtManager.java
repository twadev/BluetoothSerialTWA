package com.twa.bt.cordova;

import java.io.IOException;
import java.io.InputStream;
import java.lang.Thread.State;
import java.lang.reflect.Method;
import java.util.ArrayList;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.Handler;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import org.apache.cordova.*;

import com.twa.bt.cordova.BluetoothSerialTWA;
import com.twa.bt.cordova.Const;
import com.twa.bt.cordova.Log;

/**
 * The Class BtManager is responsible for all kind of Communication with RFID
 * Bluetooth device. It includes Connect, Disconnect, Read, Write communications
 */
public class BtManager extends Service
{
	public static final String ACTION = "com.twa.bt.cordova.BlueToothSerial";
	public static final String STATUS = "status";
	public static final int CONNECTING = 1;
	public static final int CONNECTED = 2;
	public static final int FAILED = 3;
	public static final int DATA = 4;
	public static final String VALUE = "value";
	/** The Constant BT_ADAPTER. */
	public static final BluetoothAdapter BT_ADAPTER = BluetoothAdapter
			.getDefaultAdapter();
	public static final ArrayList<String> TAGS = new ArrayList<String>();

	private Thread thread;

	/**
	 * The list that holds the all the values fetched from RFID. After the RFID
	 * device successfully connected, you can use this list any where for
	 * displaying the values. This list is created as Final so that no one can
	 * change it.
	 */
	public final ArrayList<Byte> LIST = new ArrayList<Byte>();

	private BluetoothSocket socket;
//	public static Work work;
	private boolean autoSave;
	private static final boolean DEBUG = false;
        private Handler mHandler;
        private CallbackContext mainCallbackContext;

	public static int count;

	/**
	 * Call this method to connect with RFID device.
	 * 
	 * @param mac
	 *            the mac address of RFID device
	 * @param ctx
	 *            the ctx
	 */
	public boolean connect(Handler handler, CallbackContext cbContext, String mac)
	{
//		Intent i = new Intent(ACTION);
//		i.putExtra(STATUS, CONNECTING);
//		sendBroadcast(i);
            
            mainCallbackContext = cbContext;
            mHandler = handler;
            
//		String mac = PreferenceManager.getDefaultSharedPreferences(this)
//				.getString(Const.BT_DEVICE, "no-data");
//		Log.e("MAC", mac);
                Boolean ret = true;
//                Message msg = mHandler.obtainMessage(BluetoothSerialTWA.MESSAGE_READ, "Hi!!!!");
////                Bundle bundle = new Bundle();
////                bundle.putString(BluetoothSerialTWA.MESSAGE_READ, "Hi!!!!");
////                msg.setData(bundle);
//                mHandler.sendMessage(msg);
		try
		{
			BluetoothDevice device = BtManager.BT_ADAPTER.getRemoteDevice(mac);
			socket = null;
			/*socket = device.createInsecureRfcommSocketToServiceRecord(UUID
					.fromString("00001101-0000-1000-8000-00805F9B34FB"));*/


			Method m = device.getClass().getMethod("createRfcommSocket",
					new Class[] { int.class });
			socket = (BluetoothSocket) m.invoke(device, 1);
			BtManager.BT_ADAPTER.cancelDiscovery();
			// sock = dev.createRfcommSocketToServiceRecord(MY_UUID);
			// sock = dev.createInsecureRfcommSocketToServiceRecord(MY_UUID);

			m = BluetoothDevice.class.getMethod("convertPinToBytes",
					new Class[] { String.class });
			byte[] pin = (byte[]) m.invoke(device, "1234");
			m = device.getClass().getMethod("setPin",
					new Class[] { pin.getClass() });
			m.invoke(device, pin);
		} catch (Exception e)
		{
			e.printStackTrace();
                        ret = false;
		}

		try
		{
			Log.e("SERVICE", "CONNECTING");
			socket.connect();

//			i = new Intent(ACTION);
//			i.putExtra(STATUS, CONNECTED);
//			sendBroadcast(i);

			Log.e("SERVICE", "CONNECTED");

			doStart();

		} catch (IOException e)
		{
                        mainCallbackContext.error("Not connected");
			e.printStackTrace();
			try
			{
				if (socket != null)
					socket.close();
			} catch (IOException e2)
			{
				e2.printStackTrace();
			}
			Log.e("SERVICE", "FAILED");
//			i = new Intent(ACTION);
//			i.putExtra(STATUS, FAILED);
//			sendBroadcast(i);
                        ret = false;

		}
                
                return ret;

	}

	/**
	 * Start listen the Input stream. Continuously read the data from Oximter in
	 * each 500 Millisecond If there is a valid value it store that value in
	 * Arraylist
	 * 
	 * @param in
	 *            the input stream
	 */
	public void startListen(final InputStream in)
	{
		Log.e("SERVICE", "LISTEN" + socket);
		LIST.clear();
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				buff.clear();
				while (socket != null)
				{

					Log.e("SERVICE", "LISTEN=WHILE");
//					if (DEBUG)
//					{
						try
						{
							Thread.sleep(10000);
						} catch (Exception e)
						{
							e.printStackTrace();
						}
//						onTagRead(System.currentTimeMillis() + "");
//						continue;
//					}
//							Thread.sleep(10000);
					try
					{
						byte b = (byte) in.read();
						char c = (char) b;
						if (c == '\n' || c == '\r')
						{
							if (buff.size() == 0)
								continue;
							byte buffer[] = getByteArray();
							buff.clear();
							String str = new String(buffer);
							// str=str+"UTF-8: "+new
							// String(buffer,"UTF-8")+"\n";
							// str=str+"US-ASCII: "+new
							// String(buffer,"US-ASCII")+"\n";
							// str=str+"ISO-8859-1: "+new
							// String(buffer,"ISO-8859-1")+"\n";
							System.out.println("MESSAGE: " + str);

							onTagRead(str);
						}
						else if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z'
								|| c >= '0' && c <= '9')
							buff.add(b);

					}
					catch (IOException e)
					{
                                                mainCallbackContext.error("Not connected");
						socket=null;
						e.printStackTrace();
						stopSelf();
						return;
					}
					catch (Exception e)
					{
                                            mainCallbackContext.error("Not connected");
                                            e.printStackTrace();
					}
				}
			}
		}).start();

	}

	private void onTagRead(String tag)
	{
		Log.e("TAG=" + tag);
//		if(!Test.isON)
//			TAGS.add(tag);
		Log.e("TAGLISt=" + TAGS);

                
//		Intent i = new Intent(ACTION);
//		i.putExtra(STATUS, DATA);
//		i.putExtra(VALUE, tag);
//		sendBroadcast(i);
                
//                Message msg = mHandler.obtainMessage(BluetoothSerial.MESSAGE_DEVICE_NAME);
//                Bundle bundle = new Bundle();
//                bundle.putString(BluetoothSerial.DEVICE_NAME, device.getName());
//                msg.setData(bundle);
//                mHandler.sendMessage(tag);
                mHandler.obtainMessage(BluetoothSerialTWA.MESSAGE_READ, tag).sendToTarget();
                
//                mHandler.onTagReaded(tag);
//		Intent i = new Intent(ACTION);
//		i.putExtra(STATUS, DATA);
//		i.putExtra(VALUE, tag);
//		sendBroadcast(i);

//		if(Test.isON)
//		{
//			i = new Intent(Const.ACTION_TAG_SAVED);
//			i.putExtra(Const.EXTRA_DATA, tag);
//			sendBroadcast(i);
//		}
//		else if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
//				Const.AUTO_SAVE, false))
//		{
//			autoSaver();
//		}
//		else if (TAGS.size() == 1)
//		{
//			Log.e("AUTO SAVE OFF");
//			i = new Intent(Const.ACTION_TAG_SAVED);
//			i.putExtra(Const.EXTRA_DATA, true);
//			sendBroadcast(i);
//		}

	}

//	private void autoSaver()
//	{
//		Log.e("AUTO SAVE ON");
//		autoSave = true;
//		if (thread == null || thread.getState() == State.TERMINATED)
//		{
//			thread = new Thread(new Runnable() {
//
//				@Override
//				public void run()
//				{
//					synchronized (TAGS)
//					{
//						while (autoSave && TAGS.size() > 0)
//						{
//							saveWork(BtManager.this);
//						}
//						Log.e("ALL SAVED");
//					}
//				}
//			});
//			thread.start();
//		}
//	}
//
//	public static boolean saveWork(Context c)
//	{
//		if (TAGS.size() == 0)
//			return false;
//		while(TAGS.size()>0&&Commons.isEmpty(TAGS.get(0)))
//		{
//			TAGS.remove(0);
//		}
//		if (TAGS.size() == 0)
//			return false;
//
//		if (work == null)
//			work = DbHelper.getLastSavedWork(PreferenceManager
//					.getDefaultSharedPreferences(c).getString(
//							Const.WORK_TYPE_ID, null));
//		work.getFields().put(work.getTagPos(), TAGS.remove(0));
//		work.setDatetime(System.currentTimeMillis());
//		DbHelper.saveWork(work);
//
//		Intent i = new Intent(Const.ACTION_TAG_SAVED);
//		c.sendBroadcast(i);
//		return true;
//	}

	private final ArrayList<Byte> buff = new ArrayList<Byte>();

	public byte[] getByteArray()
	{
		byte[] data = new byte[buff.size()];
		for (int i = 0; i < buff.size(); i++)
		{
			data[i] = buff.get(i);
		}

		return data;
	}

	/**
	 * Call this method to close the socket and connection with RFID.
	 */
	public void close()
	{

		Log.e("SERVICE", "CLOSE");
		try
		{
			if (socket != null)
				socket.close();
			socket = null;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		return START_STICKY;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();

//		PreferenceManager.getDefaultSharedPreferences(this).edit()
//				.putBoolean(Const.SERVICE_RUNNING, true).commit();
//
//		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
//				this).setSmallIcon(R.drawable.icon_logo)
//				.setContentTitle(getString(R.string.app_name))
//				.setContentText(getString(R.string.msg_scan_started))
//				.setAutoCancel(false).setOngoing(true);
//		Intent resultIntent = new Intent(this, MainActivity.class);
//		resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//		resultIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//
//		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
//				resultIntent, 0);
//		mBuilder.setContentIntent(pendingIntent);
//		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//		mNotificationManager.notify(1, mBuilder.build());

		Log.e("SERVICE", "CREAT");

//		connect();

//		registerReceiver(autoRcvr, new IntentFilter(Const.ACTION_SAVE_MODE));
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();

//		PreferenceManager.getDefaultSharedPreferences(this).edit()
//				.putBoolean(Const.SERVICE_RUNNING, false).commit();

		Log.e("SERVICE", "DESTROY");
		stopForeground(true);
		close();

//		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//		mNotificationManager.cancel(1);
//
//		unregisterReceiver(autoRcvr);
	}

	private BroadcastReceiver autoRcvr = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent)
		{
//			if (intent.getBooleanExtra(Const.AUTO_SAVE, false))
//				autoSaver();
//			else
//				autoSave = false;
		}
	};

	private void doStart()
	{
		Log.e("SERVICE", "STARt" + socket);
		try
		{
			if (socket != null)
			{
				// sendData(socket.getOutputStream());
				startListen(socket.getInputStream());
			}
		} catch (IOException e)
		{
			e.printStackTrace();
			stopSelf();
		}

	}

}
