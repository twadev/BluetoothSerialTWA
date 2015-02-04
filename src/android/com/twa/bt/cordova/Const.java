package com.twa.bt.cordova;

import java.io.File;

import android.os.Environment;

public class Const
{
	public static final String LANGUAGE = "language";// 1=eng
	public static final int LANG_ENG = 3;
	public static final int LANG_CH_TRAD = 1;
	public static final int LANG_CH_SIMP = 2;

	public static final int REQ_EDIT_WORK = 1;
	public static final int REQ_ADD_FARM = 2;
	public static final int REQ_EDIT_FARM = 3;

	
	public static final String SERVER_ADDERES = "serverAddress";
	public static final String SERVER_USERNAME = "serverUser";
	public static final String SERVER_PASSWORD = "serverPwd";
	
	public static final String FARM = "farmName";
	
	public static final String USER_SSSN = "userSSSN";
	public static final String HI_RECORD_ID = "hiRecordId";
	
	public static final String EXTRA_DATA = "extraData";
	public static final String EXTRA_DATA1 = "extraData1";
	public static final String WORK_TYPE = "workType";
	public static final String WORK_TYPE_ID = "workTypeId";
	public static final String AUTO_SAVE = "autoSave";
	public static final String SERVICE_RUNNING = "isServiceRunning";
	public static final String BT_DEVICE = "btDeviceMac";
	public static final String BT_DEVICE_NAME = "btDeviceName";

	public static final String ACTION_TAG_SAVED = "com.siramob.tagSaved";
	public static final String ACTION_SAVE_MODE = "com.siramob.saveModeChanged";
	public static final String ACTION_BT_CHANGED = "com.siramob.deviceChanged";
	public static final String ACTION_DATA_CHANGED = "com.siramob.dataChanged";

	public static final int PAGE_SIZE_100 = 100;
	public static final int PAGE_SIZE_30 = 30;

}
