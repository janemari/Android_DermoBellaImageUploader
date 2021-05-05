package com.chowis.cdb.skin.handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.chowis.cdb.skin.models.Constants;

import java.io.File;
import java.util.ArrayList;

import timber.log.Timber;

public class DbSkinAdapter {

    public static final String TAG = "[DB]";

    public static final String DATABASE_NAME = Environment
            .getExternalStorageDirectory()
            + File.separator
            + Constants.PROJECTNAME
            + File.separator
            + "data"
            + File.separator
            + Constants.PROJECTNAME + ".db";

    public static final int DATABASE_VERSION = 5;

    // Config table column
    public static final String CONFIG_SEQ = "config_seq";
    public static final String CONFIG_OPTIC_NUMBER = "config_optic_number";
    public static final String CONFIG_OPTIC_MODE = "config_optic_mode";
    public static final String CONFIG_BACKGROUND = "config_background";
    public static final String CONFIG_LOGO = "config_logo";
    public static final String CONFIG_REPORT_LOGO = "config_report_logo";
    public static final String CONFIG_REPORT_SHOP_NAME = "config_report_shop_name";
    public static final String CONFIG_REPORT_SHOP_ADDRESS = "config_report_shop_address";
    public static final String CONFIG_EMAIL_SENDER = "config_email_sender";
    public static final String CONFIG_EMAIL_SMTP = "config_email_smtp";
    public static final String CONFIG_EMAIL_SECURITY = "config_email_security";
    public static final String CONFIG_EMAIL_PORT = "config_email_port";
    public static final String CONFIG_EMAIL_USER_NAME = "config_email_user_name";
    public static final String CONFIG_EMAIL_USER_PASSWORD = "config_email_user_password";
    public static final String CONFIG_LANGUAGE = "config_language";
    public static final String CONFIG_MOIST_MODE = "config_moist_mode";
    public static final String CONFIG_SKINAGE_MODE = "config_skinage_mode";
    public static final String CONFIG_NOLOGO_MODE = "config_nologo_mode";
    public static final String CONFIG_FITZPATRICK_MODE = "config_fitzpatrick_mode";
    public static final String CONFIG_AUTO_CAPTURE_MODE = "config_auto_capture_mode";

    public static final String EMAIL_SEQ = "email_seq";
    public static final String EMAIL_SENDER = "email_sender";
    public static final String EMAIL_SENDER_NAME = "email_sender_name";
    public static final String EMAIL_RECEIVER = "email_receiver";
    public static final String EMAIL_PATH = "email_path";
    public static final String EMAIL_REGISTER_DATE = "email_register_date";

    // Programs table column
    public static final String PROGRAMS_SEQ 		= "programs_seq";
    public static final String PROGRAMS_NAME 		= "programs_name";
    public static final String PROGRAMS_MOISTURE 	= "programs_moisture";
    public static final String PROGRAMS_PORES 		= "programs_pores";
    public static final String PROGRAMS_SPOTS 		= "programs_spots";
    public static final String PROGRAMS_WRINKLES 	= "programs_wrinkles";
    public static final String PROGRAMS_UV 	= "programs_uv";
    public static final String PROGRAMS_KERATIN  = "programs_keratin";

    public static final String SEND_IMAGE_SEQ						= "send_image_seq";
    public static final String SEND_IMAGE_FILE						= "send_image_file";

    public static final String DROPBOX_SEND_IMAGE_SEQ				= "dropbox_send_image_seq";
    public static final String DROPBOX_SEND_IMAGE_FILE				= "dropbox_send_image_file";

    public static final String RECOMMAND_PRODUCT_SEQ 	= "product_seq";
    public static final String RECOMMAND_PRODUCT_1 		= "product_1";
    public static final String RECOMMAND_PRODUCT_2 		= "product_2";
    public static final String RECOMMAND_PRODUCT_3 		= "product_3";
    public static final String RECOMMAND_PRODUCT_4 		= "product_4";
    public static final String RECOMMAND_PRODUCT_5 		= "product_5";
    public static final String RECOMMAND_PRODUCT_6 		= "product_6";
    public static final String RECOMMAND_PRODUCT_7 		= "product_7";
    public static final String RECOMMAND_PRODUCT_8 		= "product_8";
    public static final String RECOMMAND_PRODUCT_9 		= "product_9";
    public static final String RECOMMAND_PRODUCT_10		= "product_10";
    public static final String RECOMMAND_PRODUCT_11		= "product_11";
    public static final String RECOMMAND_PRODUCT_12		= "product_12";
    public static final String RECOMMAND_PRODUCT_13		= "product_13";
    public static final String RECOMMAND_PRODUCT_14		= "product_14";

    // Analysis table column
    public static final String ANALYSIS_SEQ 					= "analysis_seq";
    public static final String ANALYSIS_CLIENT_ID 				= "analysis_client_id";
    public static final String ANALYSIS_DIAGDATE 				= "analysis_diagdate";
    public static final String ANALYSIS_MOISTURE_T 				= "analysis_moisture_t";
    public static final String ANALYSIS_MOISTURE_U 				= "analysis_moisture_u";
    public static final String ANALYSIS_SEBUM_T 				= "analysis_sebum_t";
    public static final String ANALYSIS_SEBUM_U 				= "analysis_sebum_u";
    public static final String ANALYSIS_PORE 					= "analysis_pore";
    public static final String ANALYSIS_SPOT 					= "analysis_spot";
    public static final String ANALYSIS_WRINKLE 				= "analysis_wrinkle";
    public static final String ANALYSIS_UV		 				= "analysis_uv";
    public static final String ANALYSIS_KERATIN 				= "analysis_keratin";
    public static final String ANALYSIS_COMMENTS 				= "analysis_comments";

    // Analysis table column
    public static final String ANALYSIS_EXPERT_SEQ 					= "analysis_expert_seq";
    public static final String ANALYSIS_EXPERT_CLIENT_ID 				= "analysis_client_id";
    public static final String ANALYSIS_EXPERT_DIAGDATE 				= "analysis_diagdate";
    public static final String ANALYSIS_EXPERT_SENSITIVITY 				= "analysis_expert_sensitivity";
    public static final String ANALYSIS_EXPERT_SENSITIVITY_MOISTURE 				= "analysis_expert_sensitivity_moisture";
    public static final String ANALYSIS_EXPERT_SENSITIVITY_QA 				= "analysis_expert_sensitivity_qa";
    public static final String ANALYSIS_EXPERT_SKINTONE_HEAD 				= "analysis_expert_skintone_head";
    public static final String ANALYSIS_EXPERT_SKINTONE_CHEEK 				= "analysis_expert_skintone_cheek";
    public static final String ANALYSIS_EXPERT_SKINTONE_CHIN 				= "analysis_expert_skintone_chin";
    public static final String ANALYSIS_EXPERT_SKINTONE_NECK 				= "analysis_expert_skintone_neck";
    public static final String ANALYSIS_EXPERT_SKINTONE_AVG 				= "analysis_expert_skintone_avg";
    public static final String ANALYSIS_EXPERT_BLACKHEAD 				= "analysis_expert_blackhead";
    public static final String ANALYSIS_EXPERT_SKINTEXTURE 				= "analysis_expert_skintexture";
    public static final String ANALYSIS_EXPERT_COMMENTS 				= "analysis_expert_comments";
    public static final String ANALYSIS_EXPERT_RESERVE1 				= "analysis_expert_reserve1";
    public static final String ANALYSIS_EXPERT_RESERVE2 				= "analysis_expert_reserve2";

    protected static final String CDBS_CONFIG_DATABASE_TABLE 	= "CDBS_CONFIG";
    protected static final String CDBS_EMAIL_DATABASE_TABLE 	= "CDBS_EMAIL";
    protected static final String CDBS_PROGRAMS_DATABASE_TABLE 	= "CDBS_PROGRAMS";
    protected static final String CDBS_SEND_IMAGE_DATABASE_TABLE = "CDBS_SEND_IMAGE";
    protected static final String CDBS_DROPBOX_SEND_IMAGE_DATABASE_TABLE = "CDBS_DROPBOX_SEND_IMAGE";
    protected static final String CDBS_RECOMMAND_PRODUCT_DATABASE_TABLE = "CDBS_RECOMMAND_PRODUCT";
    protected static final String CDBS_ANALYSIS_DATABASE_TABLE 						= "CDBS_ANALYSIS";
    protected static final String CDBS_ANALYSIS_EXPERT_DATABASE_TABLE 						= "CDBS_ANALYSIS_EXPERT";

    protected static final String CDBS_ANALYSIS_EXPERT_DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS " +
            CDBS_ANALYSIS_EXPERT_DATABASE_TABLE + " (" +
            ANALYSIS_EXPERT_SEQ + 				" INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ANALYSIS_EXPERT_CLIENT_ID + 		" INTEGER NOT NULL DEFAULT -1, " +
            ANALYSIS_EXPERT_DIAGDATE + 		" TEXT, " +
            ANALYSIS_EXPERT_SENSITIVITY + 		" INTEGER NOT NULL DEFAULT -1, " +
            ANALYSIS_EXPERT_SENSITIVITY_MOISTURE + 		" INTEGER NOT NULL DEFAULT -1, " +
            ANALYSIS_EXPERT_SENSITIVITY_QA + 		" TEXT, " +
            ANALYSIS_EXPERT_SKINTONE_HEAD + 		" TEXT, " +
            ANALYSIS_EXPERT_SKINTONE_CHEEK + 		" TEXT, " +
            ANALYSIS_EXPERT_SKINTONE_CHIN + 		" TEXT, " +
            ANALYSIS_EXPERT_SKINTONE_NECK + 				" TEXT, " +
            ANALYSIS_EXPERT_SKINTONE_AVG + 				" TEXT, " +
            ANALYSIS_EXPERT_BLACKHEAD + 		" INTEGER NOT NULL DEFAULT -1, " +
            ANALYSIS_EXPERT_SKINTEXTURE + 					" INTEGER NOT NULL DEFAULT -1, " +
            ANALYSIS_EXPERT_COMMENTS + 		" TEXT, " +
            ANALYSIS_EXPERT_RESERVE1 + 		" TEXT, " +
            ANALYSIS_EXPERT_RESERVE2 + 	" TEXT);";

    protected static final String CDBS_ANALYSIS_DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS " +
            CDBS_ANALYSIS_DATABASE_TABLE + " (" +
            ANALYSIS_SEQ + 				" INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ANALYSIS_CLIENT_ID + 		" INTEGER, " +
            ANALYSIS_DIAGDATE + 		" TEXT, " +
            ANALYSIS_MOISTURE_T + 		" INTEGER NOT NULL DEFAULT -1, " +
            ANALYSIS_MOISTURE_U + 		" INTEGER NOT NULL DEFAULT -1, " +
            ANALYSIS_SEBUM_T + 		" INTEGER NOT NULL DEFAULT -1, " +
            ANALYSIS_SEBUM_U + 		" INTEGER NOT NULL DEFAULT -1, " +
            ANALYSIS_PORE + 				" INTEGER NOT NULL DEFAULT -1, " +
            ANALYSIS_SPOT + 				" INTEGER NOT NULL DEFAULT -1, " +
            ANALYSIS_WRINKLE + 		" INTEGER NOT NULL DEFAULT -1, " +
            ANALYSIS_UV + 					" INTEGER NOT NULL DEFAULT -1, " +
            ANALYSIS_KERATIN + 		" INTEGER NOT NULL DEFAULT -1, " +
            ANALYSIS_COMMENTS + 	" TEXT);";

    protected static final String CDBS_SEND_IMAGE_DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS " +
            CDBS_SEND_IMAGE_DATABASE_TABLE + " (" +
            SEND_IMAGE_SEQ + 				" INTEGER PRIMARY KEY AUTOINCREMENT, " +
            SEND_IMAGE_FILE + 	" TEXT);";

    protected static final String CDBS_DROPBOX_SEND_IMAGE_DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS " +
            CDBS_DROPBOX_SEND_IMAGE_DATABASE_TABLE + " (" +
            DROPBOX_SEND_IMAGE_SEQ + 				" INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DROPBOX_SEND_IMAGE_FILE + 	" TEXT);";

    protected static final String CDBS_CONFIG_DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + CDBS_CONFIG_DATABASE_TABLE + " ("
            + CONFIG_SEQ + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CONFIG_OPTIC_NUMBER + " TEXT, "
            + CONFIG_OPTIC_MODE + " INTEGER NOT NULL DEFAULT 0, "
            + CONFIG_BACKGROUND + " TEXT, "
            + CONFIG_LOGO + " TEXT, "
            + CONFIG_REPORT_LOGO + " TEXT, "
            + CONFIG_REPORT_SHOP_NAME + " TEXT, "
            + CONFIG_REPORT_SHOP_ADDRESS + " TEXT, "
            + CONFIG_EMAIL_SENDER + " TEXT, "
            + CONFIG_EMAIL_SMTP + " TEXT, "
            + CONFIG_EMAIL_SECURITY + " INTEGER NOT NULL DEFAULT 1, "
            + CONFIG_EMAIL_PORT + " TEXT, "
            + CONFIG_EMAIL_USER_NAME + " TEXT, "
            + CONFIG_EMAIL_USER_PASSWORD + " TEXT, "
            + CONFIG_LANGUAGE + " TEXT NOT NULL DEFAULT 'ko',"
            + CONFIG_MOIST_MODE + " INTEGER NOT NULL DEFAULT 0,"
            + CONFIG_SKINAGE_MODE + " INTEGER NOT NULL DEFAULT 1,"	// 1이 사용 안함
            + CONFIG_FITZPATRICK_MODE + " INTEGER NOT NULL DEFAULT 0,"
            + CONFIG_AUTO_CAPTURE_MODE + " INTEGER NOT NULL DEFAULT 1);";

    protected static final String CDBS_EMAIL_DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + CDBS_EMAIL_DATABASE_TABLE + " ("
            + EMAIL_SEQ + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + EMAIL_SENDER + " TEXT, "
            + EMAIL_SENDER_NAME + " TEXT, "
            + EMAIL_RECEIVER + " TEXT, "
            + EMAIL_PATH + " TEXT, "
            + EMAIL_REGISTER_DATE + " TEXT);";

    protected static final String CDBS_PROGRAMS_DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS " +
            CDBS_PROGRAMS_DATABASE_TABLE + " (" +
            PROGRAMS_SEQ + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PROGRAMS_NAME + " TEXT, " +
            PROGRAMS_MOISTURE + " INTEGER NOT NULL DEFAULT 0, " +
            PROGRAMS_PORES + " INTEGER NOT NULL DEFAULT 0, " +
            PROGRAMS_SPOTS + " INTEGER NOT NULL DEFAULT 0, " +
            PROGRAMS_WRINKLES + " INTEGER NOT NULL DEFAULT 0, " +
            PROGRAMS_UV + " INTEGER NOT NULL DEFAULT 0, " +
            PROGRAMS_KERATIN + " INTEGER NOT NULL DEFAULT 0);";

    protected static final String CDBS_RECOMMAND_PRODUCT_DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + CDBS_RECOMMAND_PRODUCT_DATABASE_TABLE
            + " ("
            + RECOMMAND_PRODUCT_SEQ + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + RECOMMAND_PRODUCT_1 + " TEXT, "
            + RECOMMAND_PRODUCT_2 + " TEXT, "
            + RECOMMAND_PRODUCT_3 + " TEXT, "
            + RECOMMAND_PRODUCT_4 + " TEXT, "
            + RECOMMAND_PRODUCT_5 + " TEXT, "
            + RECOMMAND_PRODUCT_6 + " TEXT, "
            + RECOMMAND_PRODUCT_7 + " TEXT, "
            + RECOMMAND_PRODUCT_8 + " TEXT, "
            + RECOMMAND_PRODUCT_9 + " TEXT, "
            + RECOMMAND_PRODUCT_10 + " TEXT, "
            + RECOMMAND_PRODUCT_11 + " TEXT, "
            + RECOMMAND_PRODUCT_12 + " TEXT, "
            + RECOMMAND_PRODUCT_13 + " TEXT, "
            + RECOMMAND_PRODUCT_14 + " TEXT);";


    private SQLiteOpenHelper mDbHelper;

    private SQLiteDatabase mDb;

    private final Context mContext;

    protected Context getContext() {
        return mContext;
    }

    protected SQLiteDatabase getDB() {
        return mDb;
    }

    public static DbSkinAdapter getInstance(Context context) {
        return new DbSkinAdapter(context);
    }

    protected DbSkinAdapter(Context context) {
        this.mContext = context;
    }

    protected SQLiteOpenHelper getDbHelper() {
        return new DatabaseHelper(mContext, this);
    }

    public DbSkinAdapter open() {
        mDbHelper = getDbHelper();
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public boolean isExist(String db_name) {
        boolean is = false;
        File f = mContext.getDatabasePath(db_name);

        is = f.exists();

        return is;
    }

    public void deleteDB(String db_name) {
        try {
            mContext.deleteDatabase(db_name);
        } catch (Exception e) {
            Timber.d( "Failed Delete DB!");
        }
    }

    public long getLastSendImage() {
        long seq = -1;

        Cursor c = mDb.query(CDBS_SEND_IMAGE_DATABASE_TABLE, new String[] {SEND_IMAGE_SEQ}, null, null, null, null, null);
        if(c != null)
        {
            if (c.moveToLast()) {
                seq = c.getLong(c.getColumnIndex(SEND_IMAGE_SEQ));
            }
            c.close();
        }
        return seq;
    }

    public long addSendImage(String path) {
        Timber.d( "addSendImage");
        if(path == null)
        {
            return -1;
        }
        ContentValues values = new ContentValues();

        values.put(SEND_IMAGE_FILE, path);

        return mDb.insert(CDBS_SEND_IMAGE_DATABASE_TABLE, null, values);
    }

    public boolean onExistFile(String file){
        Timber.d( "onExistFile");

        boolean isExist = false;

        String where = SEND_IMAGE_FILE + "='" + file + "'";

        Cursor cursor = mDb.query(CDBS_SEND_IMAGE_DATABASE_TABLE, new String[] {SEND_IMAGE_SEQ}, where, null, null, null, null);

        if(cursor.moveToFirst()){
            do{
                isExist = cursor.getLong(cursor.getColumnIndex(SEND_IMAGE_SEQ)) > -1;

            }while(cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return isExist;
    }

    public ArrayList<String> getSendImages() {
        Timber.d("getSendImages");

        ArrayList<String> arrayDataSet = new ArrayList<String>();

        Cursor cursor = mDb.query(CDBS_SEND_IMAGE_DATABASE_TABLE, new String[] {
                SEND_IMAGE_SEQ, SEND_IMAGE_FILE }, null, null, null,null, null);

        if (cursor.moveToFirst()) {
            do {
                String file = cursor.getString(cursor.getColumnIndex(SEND_IMAGE_FILE));
                arrayDataSet.add(file);

            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return arrayDataSet;
    }

    // save sended images to dropbox
    // jhong
    // since 160129
    public long getLastDropboxSendImage() {
        long seq = -1;

        Cursor c = mDb.query(CDBS_DROPBOX_SEND_IMAGE_DATABASE_TABLE, new String[] {DROPBOX_SEND_IMAGE_SEQ}, null, null, null, null, null);
        if(c != null)
        {
            if (c.moveToLast()) {
                seq = c.getLong(c.getColumnIndex(DROPBOX_SEND_IMAGE_SEQ));
            }
            c.close();
        }
        return seq;
    }

    public void close() {
        mDbHelper.close();
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, DbSkinAdapter parent) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(CDBS_CONFIG_DATABASE_CREATE);
            db.execSQL(CDBS_EMAIL_DATABASE_CREATE);
            db.execSQL(CDBS_SEND_IMAGE_DATABASE_CREATE);
            db.execSQL(CDBS_PROGRAMS_DATABASE_CREATE);
            db.execSQL(CDBS_RECOMMAND_PRODUCT_DATABASE_CREATE);
            db.execSQL(CDBS_ANALYSIS_DATABASE_CREATE);
            db.execSQL(CDBS_DROPBOX_SEND_IMAGE_DATABASE_CREATE);
            db.execSQL(CDBS_ANALYSIS_EXPERT_DATABASE_CREATE);

            Timber.d("DATABASE_CREATE");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Timber.d( "Database Upgrade!");

            if (oldVersion < 2) {
                String add_column = "ALTER TABLE " + CDBS_EMAIL_DATABASE_TABLE + " ADD COLUMN " + EMAIL_SENDER_NAME + " TEXT;";
                db.execSQL(add_column);
                db.execSQL(CDBS_DROPBOX_SEND_IMAGE_DATABASE_CREATE);

            }
            if(oldVersion < 3){
                String add_column = "ALTER TABLE " + CDBS_CONFIG_DATABASE_TABLE + " ADD COLUMN " + CONFIG_MOIST_MODE + " INTEGER NOT NULL DEFAULT 0;";
                db.execSQL(add_column);
                db.execSQL(CDBS_CONFIG_DATABASE_CREATE);
            }

            if(oldVersion < 4){
                String add_column = "ALTER TABLE " + CDBS_CONFIG_DATABASE_TABLE + " ADD COLUMN " + CONFIG_SKINAGE_MODE + " INTEGER NOT NULL DEFAULT 1;";
                db.execSQL(add_column);
                db.execSQL(CDBS_CONFIG_DATABASE_CREATE);
            }

            if(oldVersion < 5){
                String add_column = "ALTER TABLE " + CDBS_CONFIG_DATABASE_TABLE + " ADD COLUMN " + CONFIG_FITZPATRICK_MODE + " INTEGER NOT NULL DEFAULT 0;";
                db.execSQL(add_column);
                db.execSQL(CDBS_CONFIG_DATABASE_CREATE);
            }
        }
    }
}
