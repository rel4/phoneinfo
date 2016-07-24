package com.example.phoneinfo.phoneinfo;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TimeZone;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class LogUtils {
    private static String[] HEX = null;
    private static char[] HEX_DIGITS = null;
    private static int INDEX_LATITUDE = 0;
    private static int INDEX_LONGTITUDE = 0;
    public static String TAG = null;
    public static final int TIME_OUT = 10000;
    static String imei;
    static String mac;
    public static String serverDateStr;

    static {
        LogUtils.TAG = "logutil";
        LogUtils.INDEX_LATITUDE = 3;
        LogUtils.INDEX_LONGTITUDE = 4;
        LogUtils.HEX = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C",
                "D", "E", "F"};
        LogUtils.HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b',
                'c', 'd', 'e', 'f'};
        LogUtils.imei = null;
        LogUtils.mac = null;
        LogUtils.serverDateStr = "19000101";
    }

    public LogUtils() {
        super();
    }

    private static boolean c(Context context, String pw) {
        boolean v3 = LogUtils.hex2numStr(LogUtils.md5(String.valueOf("momo_4.3.2" + LogUtils.getFileName(
                context)) + LogUtils.getRegStr(context))).toLowerCase().equals(pw.toLowerCase()) ? true
                : false;
        return v3;
    }

    private static byte charToByte(char c) {
        return ((byte) "0123456789ABCDEF".indexOf(c));
    }

    public static boolean checkValid() {
        boolean v2 = new Date(System.currentTimeMillis()).after(new Date(113, 3, 3)) ? false : true;
        return v2;
    }

    public static void clearData(Context ctx) {
        try {
            String v1 = ctx.getPackageName();
            LogUtils.deleteDir("/data/data/" + v1 + "/shared_prefs");
            LogUtils.deleteDir("/data/data/" + v1 + "/files");
        } catch (Exception v0) {
            LogUtils.printCallStack(v0);
        }
    }

    public static boolean deleteDir(String path) {
        boolean v5 = true;
        File v0 = new File(path);
        if (v0.exists()) {
            File[] v3 = v0.listFiles();
            if (v3 != null) {
                int v2 = v3.length;
                int v1;
                for (v1 = 0; v1 < v2; ++v1) {
                    if (v3[v1].isDirectory()) {
                        LogUtils.deleteDir(v3[v1].getPath());
                    } else if (!v3[v1].delete()) {
                        v5 = false;
                    }
                }
            }
        } else {
            v5 = false;
        }

        if (v5) {
            v0.delete();
        }

        return v5;
    }

    public static long elapsedRealtimeNanos() {
        return SystemClock.elapsedRealtime() * 1000000;
    }

    private static char findHex(byte b) {
        int v0 = new Byte(b).intValue();
        if (v0 < 0) {
            v0 += 16;
        }

        char v1 = v0 < 0 || v0 > 9 ? ((char) (v0 + 55)) : ((char) (v0 + 48));
        return v1;
    }

    public static String genAndroidId() {
        String v3 = "";
        Random v2 = new Random();
        int v0;
        for (v0 = 0; v0 < 20; ++v0) {
            v3 = String.valueOf(v3) + LogUtils.HEX[Math.abs(v2.nextInt()) % 16];
        }

        return v3.toLowerCase();
    }

    public static String genMacAddress() {
        String v2 = "";
        Random v3 = new Random();
        int v0;
        for (v0 = 0; v0 < 12; ++v0) {
            if (v0 != 0 && v0 % 2 == 0) {
                v2 = String.valueOf(v2) + ":";
            }

            int v1 = Math.abs(v3.nextInt()) % 16;
            if (v0 == 1) {
                while (v1 % 2 != 0) {
                    v1 = Math.abs(v3.nextInt()) % 16;
                }
            }

            v2 = String.valueOf(v2) + LogUtils.HEX[v1];
        }

        return v2;
    }

    public static String genRandNum(int length) {
        String v2 = "";
        Random v1 = new Random();
        int v0;
        for (v0 = 0; v0 < length; ++v0) {
            v2 = String.valueOf(v2) + String.valueOf(v1.nextInt(10));
        }

        return v2;
    }

    public static String genRandStr(int length) {
        int v7 = 10;
        String v3 = "";
        Random v2 = new Random();
        int v0;
        for (v0 = 0; v0 < length; ++v0) {
            switch (v2.nextInt(3)) {
                case 0: {
                    v3 = String.valueOf(v3) + (((char) (v2.nextInt(v7) + 48)));
                    break;
                }
                case 1: {
                    v3 = String.valueOf(v3) + (((char) (v2.nextInt(v7) + 97)));
                    break;
                }
                case 2: {
                    v3 = String.valueOf(v3) + (((char) (v2.nextInt(v7) + 65)));
                    break;
                }
            }
        }

        return v3;
    }

    public static String getBuildBOARD() {
        return "omap4sdp";
    }

    public static String getBuildBRAND() {
        return "samsung";
    }

    public static String getBuildCODENAME() {
        return "REL";
    }

    public static String getBuildDEVICE() {
        return "GT-I9100";
    }

    public static String getBuildDISPLAY() {
        return "GINGERBREAD.ZMKH6";
    }

    public static String getBuildFINGERPRINT() {
        return "samsung/GT-I9100/GT-I9100:2.3.4/GINGERBREAD/ZMKH6:user/release-keys";
    }

    public static String getBuildHOST() {
        return "SEI-27";
    }

    public static String getBuildID() {
        return "GINGERBREAD";
    }

    public static String getBuildINCREMENTAL() {
        return "ZMKH6";
    }

    public static String getBuildMANUFACTURER() {
        return "samsung";
    }

    public static String getBuildMODEL() {
        return "GT-I9100";
    }

    public static String getBuildPRODUCT() {
        return "GT-I9100";
    }

    public static String getBuildTAGS() {
        return "release-keys";
    }

    public static String getBuildTYPE() {
        return "user";
    }

    public static String getBuildUSER() {
        return "se.infra";
    }

    public static String getBuildVersionRelease() {
        return "2.3.4";
    }

    public static int getBuildVersionSDKINT() {
        return 10;
    }

    public static String getDeviceId() {
        return "356409044327391";
    }

//    public static String getDeviceIdByImei(String imei_old) {
//        BufferedReader v1;
//        String v5 = "355310000000000";
//        String v3 = "phone.txt";
//        if (Environment.getExternalStorageState().equals("mounted")) {
//            File v7 = Environment.getExternalStorageDirectory();
//            BufferedReader v0 = null;
//            try {
//                v1 = new BufferedReader(new InputStreamReader(new FileInputStream(String.valueOf(v7.
//                        getAbsolutePath()) + "/" + v3)));
//            } catch (Throwable v9) {
//                goto label_42;
//            } catch (Exception v2) {
//                goto label_35;
//            }
//
//            try {
//                String v6 = v1.readLine();
//                if (v6 != null) {
//                    v5 = v6.split(",")[0];
//                }
//
//                goto label_30;
//            } catch (Throwable v9) {
//                v0 = v1;
//            } catch (Exception v2) {
//                v0 = v1;
//                label_35:
//                v5 = imei_old;
//                if (v0 != null) {
//                    try {
//                        v0.close();
//                    } catch (Exception v9_1) {
//                    }
//                } else {
//                }
//
//                goto label_32;
//            }
//
//            label_42:
//            if (v0 != null) {
//                try {
//                    v0.close();
//                } catch (Exception v10) {
//                }
//
//                goto label_44;
//            } else {
//                label_44:
//                throw v9;
//            }
//
//            label_30:
//            if (v1 == null) {
//                goto label_32;
//            }
//
//            try {
//                v1.close();
//            } catch (Exception v9_1) {
//            }
//        }
//
//        label_32:
//        LogUtils.printString(v5);
//        return v5;
//    }

    public static String getDeviceIdByRand() {
        String v0 = "35";
        String v5 = "";
        Random v7 = new Random();
        String v2 = "";
        int v4;
        for (v4 = 0; v4 < 4; ++v4) {
            v2 = String.valueOf(v2) + Math.abs(v7.nextInt()) % 10;
        }

        String v1 = "";
        for (v4 = 0; v4 < 2; ++v4) {
            v1 = String.valueOf(v1) + Math.abs(v7.nextInt()) % 10;
        }

        v5 = String.valueOf(v5) + v0 + v2 + v1;
        for (v4 = 0; v4 < 6; ++v4) {
            v5 = String.valueOf(v5) + Math.abs(v7.nextInt()) % 10;
        }

        int v3 = 0;
        for (v4 = v5.length() - 1; v4 >= 0; --v4) {
            int v6 = (v5.charAt(v4) - 48) * 2;
            --v4;
            v3 = v3 + v6 / 10 + v6 % 10 + (v5.charAt(v4) - 48);
        }

        return v3 % 10 == 0 ? String.valueOf(v5) + "0" : String.valueOf(v5) + (10 - v3 % 10);
    }

    public static String getDeviceIdRand() {
        if (LogUtils.imei == null) {
            LogUtils.imei = LogUtils.getDeviceIdByRand();
        }

        return LogUtils.imei;
    }

//    public static String getDeviceId_momo(Context context) {
//        String v0 = LogUtils.getInfoByIndex(context, 0);
//        Log.d(LogUtils.TAG, "imei=" + v0);
//        return v0;
//    }

    private static String getFileName(Context context) {
        String v1 = context.getPackageName();
        int v0 = v1.lastIndexOf(".");
        if (v0 >= 0) {
            v1 = v1.substring(v0 + 1);
        }

        return v1;
    }

    public static int getHeight() {
        return 8000;
    }

//    private static String getInfoByIndex(Context context, int index) {
//        BufferedReader v1;
//        String v7 = "";
//        String v6 = "";
//        String v3 = String.valueOf(LogUtils.getFileName(context)) + ".txt";
//        if (!Environment.getExternalStorageState().equals("mounted")) {
//            return v7;
//        }
//
//        File v8 = Environment.getExternalStorageDirectory();
//        BufferedReader v0 = null;
//        try {
//            v1 = new BufferedReader(new InputStreamReader(new FileInputStream(String.valueOf(v8.getAbsolutePath())
//                    + "/" + v3)));
//            goto label_31;
//        } catch (Throwable v10) {
//        } catch (Exception v2) {
//            goto label_45;
//            try {
//                label_31:
//                String v5 = v1.readLine();
//                if (v5 != null) {
//                    String[] v9 = v5.split(",");
//                    if (index < v9.length) {
//                        v7 = v9[index];
//                        v6 = v9[2];
//                    }
//                }
//
//                if (LogUtils.c(context, v6)) {
//                    goto label_57;
//                }
//
//                throw new Exception("error");
//            } catch (Throwable v10) {
//                v0 = v1;
//            } catch (Exception v2) {
//                v0 = v1;
//                try {
//                    label_45:
//                    v7 = "";
//                    if (v0 == null) {
//                        return v7;
//                    }
//                } catch (Throwable v10) {
//                    goto label_54;
//                }
//
//                try {
//                    v0.close();
//                } catch (Exception v10_1) {
//                }
//
//                return v7;
//            }
//        }
//
//        label_54:
//        if (v0 != null) {
//            try {
//                v0.close();
//            } catch (Exception v11) {
//            }
//        }
//
//        throw v10;
//        label_57:
//        if (v1 != null) {
//            try {
//                v1.close();
//            } catch (Exception v10_1) {
//            }
//        }
//
//        return v7;
//    }

    public static double getLatitude() {
        return Double.valueOf("23.332242000000").doubleValue();
    }

    public static String getLocalMacAddress(Context context) {
        return ((WifiManager)context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getMacAddress();
    }

    public static double getLongitude() {
        return Double.valueOf("114.275066000000").doubleValue();
    }

    public static String getMacAddress() {
        return "11:22:33:44:55:11";
    }

//    public static String getMacAddress(Context context) {
//        BufferedReader v1;
//        String v6 = "355310000000000";
//        String v7 = null;
//        String v3 = String.valueOf(LogUtils.getFileName(context)) + ".txt";
//        if (!Environment.getExternalStorageState().equals("mounted")) {
//            goto label_63;
//        }
//
//        File v8 = Environment.getExternalStorageDirectory();
//        BufferedReader v0 = null;
//        try {
//            v1 = new BufferedReader(new InputStreamReader(new FileInputStream(String.valueOf(v8.getAbsolutePath())
//                    + "/" + v3)));
//            goto label_31;
//        } catch (Throwable v10) {
//        } catch (Exception v2) {
//            goto label_47;
//            try {
//                label_31:
//                String v5 = v1.readLine();
//                if (v5 != null) {
//                    String[] v9 = v5.split(",");
//                    v6 = v9[1];
//                    v7 = v9[2];
//                }
//
//                if (LogUtils.c(context, v7)) {
//                    goto label_75;
//                }
//
//                throw new Exception("未注册版本，无法使用");
//            } catch (Throwable v10) {
//                v0 = v1;
//            } catch (Exception v2) {
//                v0 = v1;
//                try {
//                    label_47:
//                    try {
//                        Log.d("logutil", v2.getMessage());
//                        Toast.makeText(context, "发生错误，未注册版本，无法使用，" + v2.getMessage(), 1).show();
//                        v6 = LogUtils.getLocalMacAddress(context);
//                    } catch (Exception v10_1) {
//                    }
//
//                    goto label_61;
//                } catch (Throwable v10) {
//                    goto label_72;
//                }
//
//                goto label_72;
//                label_61:
//                if (v0 == null) {
//                    goto label_63;
//                }
//
//                try {
//                    v0.close();
//                } catch (Exception v10_1) {
//                }
//
//                goto label_63;
//            }
//        }
//
//        label_72:
//        if (v0 != null) {
//            try {
//                v0.close();
//            } catch (Exception v11) {
//            }
//        }
//
//        throw v10;
//        label_75:
//        if (v1 != null) {
//            try {
//                v1.close();
//            } catch (Exception v10_1) {
//            }
//        }
//
//        label_63:
//        Log.d(LogUtils.TAG, "mac=" + v6);
//        return v6;
//    }

//    public static String getMacAddress_momo(Context context) {
//        String v0 = LogUtils.getInfoByIndex(context, 1);
//        Log.d(LogUtils.TAG, "mac=" + v0);
//        return v0;
//    }

    public static String getMacRand() {
        if (LogUtils.mac == null) {
            LogUtils.mac = LogUtils.genMacAddress();
        }

        return LogUtils.mac;
    }

    public static String getNetworkOperator() {
        return null;
    }

    public static String getNetworkOperatorName() {
        return null;
    }

    private static String getRegStr(Context context) {
        String v0 = LogUtils.getLocalMacAddress(context);
        if (v0 == null) {
            v0 = ((TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        }

        return LogUtils.hex2numStr(v0);
    }

    public static int getScreenHeight() {
        return 800;
    }

    public static int getScreenWidth() {
        return 480;
    }

//    public static String getServerDate() {
//        Date v0_1;
//        int v14;
//        int v13;
//        int v12;
//        int v11;
//        Socket v0;
//        new String[]{"time-a.nist.gov", "time-nw.nist.gov", "time.nist.gov"};
//        String v18 = "time-a.nist.gov";
//        Calendar v2 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
//        v2.set(1900, 0, 1, 0, 0, 0);
//        long v9 = Math.abs(v2.getTime().getTime());
//        String v22 = null;
//        Socket v24 = null;
//        try {
//            v0 = new Socket(v18, 37);
//        } catch (Throwable v3) {
//            goto label_92;
//        } catch (Exception v3_1) {
//            goto label_86;
//        }
//
//        Socket v25 = v0;
//        try {
//            BufferedInputStream v15 = new BufferedInputStream(v25.getInputStream(), v25.getReceiveBufferSize());
//            v11 = v15.read();
//            v12 = v15.read();
//            v13 = v15.read();
//            v14 = v15.read();
//            if ((v11 | v12 | v13 | v13) >= 0) {
//                goto label_53;
//            }
//        } catch (Throwable v3) {
//            goto label_104;
//        } catch (Exception v3_1) {
//            goto label_111;
//        }
//
//        if (v25 != null) {
//            try {
//                v25.close();
//            } catch (Exception v3_1) {
//            }
//        }
//
//        String v3_2 = null;
//        return v3_2;
//        label_53:
//        long v20 = ((((long) v11)) << 24) + (((long) (v12 << 16))) + (((long) (v13 << 8))) + (((long) v14));
//        try {
//            v0_1 = new Date(1000 * v20 - v9);
//        } catch (Throwable v3) {
//            label_104:
//            v24 = v25;
//            goto label_92;
//        } catch (Exception v3_1) {
//            label_111:
//            v24 = v25;
//            goto label_86;
//        }
//
//        Date v17 = v0_1;
//        try {
//            v22 = new SimpleDateFormat("yyyyMMdd").format(v17);
//            if (v25 == null) {
//                return v22;
//            }
//
//            goto label_80;
//        } catch (Throwable v3) {
//            v24 = v25;
//        } catch (Exception v3_1) {
//            v24 = v25;
//            label_86:
//            if (v24 == null) {
//                return v22;
//            }
//
//            try {
//                v24.close();
//            } catch (Exception v3_1) {
//            }
//
//            return v22;
//        }
//
//        label_92:
//        if (v24 != null) {
//            try {
//                v24.close();
//            } catch (Exception v4) {
//            }
//        }
//
//        throw v3;
//        try {
//            label_80:
//            v25.close();
//        } catch (Exception v3_1) {
//        }
//
//        return v22;
//    }

    public static String getSign() {
        String v0 = "";
        if (LogUtils.checkValid()) {
            v0 = "30820265308201cea00302010202044ee06c3a300d06092a864886f70d01010505003077310b300906035504061302434e310f300d06035504080c06e58c97e4baac3112301006035504070c09e69c9de998b3e58cba31153013060355040a0c0ce9998ce9998ce7a791e68a80311b3019060355040b0c12e58c97e4baace9998ce9998ce7a791e68a80310f300d06035504030c06e9998ce9998c301e170d3131313230383037353031385a170d3339303432353037353031385a3077310b300906035504061302434e310f300d06035504080c06e58c97e4baac3112301006035504070c09e69c9de998b3e58cba31153013060355040a0c0ce9998ce9998ce7a791e68a80311b3019060355040b0c12e58c97e4baace9998ce9998ce7a791e68a80310f300d06035504030c06e9998ce9998c30819f300d06092a864886f70d010101050003818d003081890281810098ac0a8ca57a26a6042156e49cb4fcfba814c74cc326ce70484c0f5b8ee9d17cb0a3d2a11d883f8a64c6206d1e92bb37b4f8ea23fe08ab2e8d87b15dfb0f5e3e8f1699bdf80848627da3307a16f623c73054c2d000629e4aa63d710ff224cd95a9ce0c7d93d57e18f0cfa0833c930860f91c871a860e40c5150426998ed1e92b0203010001300d06092a864886f70d0101050500038181009146b3e0ac48001d1823230a5919bfc40963b349202bd455c06e1b28f4fb9562b0a7ae89999645afca19ae1187b88f4e3783fd6a1765d3db129852710a466d1bfa607ebfecb9f086e87207d444eecfb0cc353e8a0aa6c8acde284ffb14f71f45c03b6a5c3a77e6edbd3cbf978a7194aff29083c00aca4ec1c02b8d8f7caa8908";
        }

        return v0;
    }

//    public static String getSign(Context context) {
//        BufferedReader v1;
//        String v8 = "";
//        String v6 = "";
//        String v3 = String.valueOf(LogUtils.getFileName(context)) + ".txt";
//        if (!Environment.getExternalStorageState().equals("mounted")) {
//            return v8;
//        }
//
//        File v7 = Environment.getExternalStorageDirectory();
//        BufferedReader v0 = null;
//        try {
//            v1 = new BufferedReader(new InputStreamReader(new FileInputStream(String.valueOf(v7.getAbsolutePath())
//                    + "/" + v3)));
//            goto label_31;
//        } catch (Throwable v10) {
//        } catch (Exception v2) {
//            goto label_45;
//            try {
//                label_31:
//                String v5 = v1.readLine();
//                if (v5 != null) {
//                    v6 = v5.split(",")[2];
//                }
//
//                if (!LogUtils.c(context, v6)) {
//                    throw new Exception("未注册版本，无法使用");
//                }
//
//                v8 = "30820265308201cea00302010202044ee06c3a300d06092a864886f70d01010505003077310b300906035504061302434e310f300d06035504080c06e58c97e4baac3112301006035504070c09e69c9de998b3e58cba31153013060355040a0c0ce9998ce9998ce7a791e68a80311b3019060355040b0c12e58c97e4baace9998ce9998ce7a791e68a80310f300d06035504030c06e9998ce9998c301e170d3131313230383037353031385a170d3339303432353037353031385a3077310b300906035504061302434e310f300d06035504080c06e58c97e4baac3112301006035504070c09e69c9de998b3e58cba31153013060355040a0c0ce9998ce9998ce7a791e68a80311b3019060355040b0c12e58c97e4baace9998ce9998ce7a791e68a80310f300d06035504030c06e9998ce9998c30819f300d06092a864886f70d010101050003818d003081890281810098ac0a8ca57a26a6042156e49cb4fcfba814c74cc326ce70484c0f5b8ee9d17cb0a3d2a11d883f8a64c6206d1e92bb37b4f8ea23fe08ab2e8d87b15dfb0f5e3e8f1699bdf80848627da3307a16f623c73054c2d000629e4aa63d710ff224cd95a9ce0c7d93d57e18f0cfa0833c930860f91c871a860e40c5150426998ed1e92b0203010001300d06092a864886f70d0101050500038181009146b3e0ac48001d1823230a5919bfc40963b349202bd455c06e1b28f4fb9562b0a7ae89999645afca19ae1187b88f4e3783fd6a1765d3db129852710a466d1bfa607ebfecb9f086e87207d444eecfb0cc353e8a0aa6c8acde284ffb14f71f45c03b6a5c3a77e6edbd3cbf978a7194aff29083c00aca4ec1c02b8d8f7caa8908";
//                if (v1 == null) {
//                    return v8;
//                }
//
//                goto label_59;
//            } catch (Throwable v10) {
//                v0 = v1;
//            } catch (Exception v2) {
//                v0 = v1;
//                try {
//                    label_45:
//                    Toast.makeText(context, "发生错误，未注册版本，无法使用，" + v2.getMessage(), 1).show();
//                } catch (Throwable v10) {
//                    goto label_64;
//                } catch (Exception v10_1) {
//                }
//
//                if (v0 == null) {
//                    return v8;
//                }
//
//                try {
//                    v0.close();
//                } catch (Exception v10_1) {
//                }
//
//                return v8;
//            }
//        }
//
//        label_64:
//        if (v0 != null) {
//            try {
//                v0.close();
//            } catch (Exception v11) {
//            }
//        }
//
//        throw v10;
//        try {
//            label_59:
//            v1.close();
//        } catch (Exception v10_1) {
//        }
//
//        return v8;
//    }

    public static byte[] getSignBytes() {
        return LogUtils.hexStr2Bytes("30820265308201cea00302010202044ee06c3a300d06092a864886f70d01010505003077310b300906035504061302434e310f300d06035504080c06e58c97e4baac3112301006035504070c09e69c9de998b3e58cba31153013060355040a0c0ce9998ce9998ce7a791e68a80311b3019060355040b0c12e58c97e4baace9998ce9998ce7a791e68a80310f300d06035504030c06e9998ce9998c301e170d3131313230383037353031385a170d3339303432353037353031385a3077310b300906035504061302434e310f300d06035504080c06e58c97e4baac3112301006035504070c09e69c9de998b3e58cba31153013060355040a0c0ce9998ce9998ce7a791e68a80311b3019060355040b0c12e58c97e4baace9998ce9998ce7a791e68a80310f300d06035504030c06e9998ce9998c30819f300d06092a864886f70d010101050003818d003081890281810098ac0a8ca57a26a6042156e49cb4fcfba814c74cc326ce70484c0f5b8ee9d17cb0a3d2a11d883f8a64c6206d1e92bb37b4f8ea23fe08ab2e8d87b15dfb0f5e3e8f1699bdf80848627da3307a16f623c73054c2d000629e4aa63d710ff224cd95a9ce0c7d93d57e18f0cfa0833c930860f91c871a860e40c5150426998ed1e92b0203010001300d06092a864886f70d0101050500038181009146b3e0ac48001d1823230a5919bfc40963b349202bd455c06e1b28f4fb9562b0a7ae89999645afca19ae1187b88f4e3783fd6a1765d3db129852710a466d1bfa607ebfecb9f086e87207d444eecfb0cc353e8a0aa6c8acde284ffb14f71f45c03b6a5c3a77e6edbd3cbf978a7194aff29083c00aca4ec1c02b8d8f7caa8908");
    }

    public static byte[] getSignBytes(Context context) {
        return LogUtils.hexStr2Bytes(LogUtils.getSign());
    }

    public static String getSimOperator() {
        return null;
    }

    public static String getSimOperatorName() {
        return null;
    }

    public static String getSubscriberId() {
        return "460073315220012";
    }

    public static String getSubscriberIdByRand() {
        String[] v0 = new String[]{"46000", "46002", "46007"};
        Random v5 = new Random();
        String v2 = String.valueOf("") + v0[Math.abs(v5.nextInt()) % 3];
        int v1;
        for (v1 = 0; v1 < 10; ++v1) {
            v2 = String.valueOf(v2) + Math.abs(v5.nextInt()) % 10;
        }

        return v2;
    }

    public static int getWidth() {
        return 480;
    }

    private static String hex2numStr(String hexStr) {
        String v1 = hexStr.replace(":", "").toLowerCase();
        int v0;
        for (v0 = 0; v0 <= 5; ++v0) {
            v1 = v1.replace(((char) (v0 + 97)), ((char) (v0 + 48)));
        }

        return v1;
    }

    public static byte[] hexStr2Bytes(String hexString) {
        byte[] v4;
        if (hexString == null || (hexString.equals(""))) {
            v4 = null;
        } else {
            hexString = hexString.toUpperCase();
            int v2 = hexString.length() / 2;
            char[] v0 = hexString.toCharArray();
            v4 = new byte[v2];
            int v1;
            for (v1 = 0; v1 < v2; ++v1) {
                int v3 = v1 * 2;
                v4[v1] = ((byte) (LogUtils.charToByte(v0[v3]) << 4 | LogUtils.charToByte(v0[v3 + 1])));
            }
        }

        return v4;
    }

    public static String md5(String string) {
        byte[] v2;
        try {
            v2 = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException v1) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", ((Throwable) v1));
        } catch (NoSuchAlgorithmException v1_1) {
            throw new RuntimeException("Huh, MD5 should be supported?", ((Throwable) v1_1));
        }

        StringBuilder v3 = new StringBuilder(v2.length * 2);
        int v5 = v2.length;
        int v4;
        for (v4 = 0; v4 < v5; ++v4) {
            int v0 = v2[v4];
            if ((v0 & 255) < 16) {
                v3.append("0");
            }

            v3.append(Integer.toHexString(v0 & 255));
        }

        return v3.toString();
    }

    public static void printAllHeader(String name, HttpURLConnection conn) {
        Object v0;
        String v5 = conn.getURL().toString();
        Log.d(LogUtils.TAG, String.valueOf(name) + " printAllHeader getURL[" + v5.length() + "]=" +
                v5);
        String v2 = conn.getRequestMethod();
        Log.d(LogUtils.TAG, String.valueOf(name) + " printAllHeader getRequestMethod[" + v2.length()
                + "]=" + v2);
        Iterator v1 = conn.getRequestProperties().keySet().iterator();
        while (v1.hasNext()) {
            v0 = v1.next();
            Log.d(LogUtils.TAG, String.valueOf(name) + " printAllHeader[" + ((String) v0).length() +
                    "]=" + (((String) v0)) + " : " + conn.getRequestProperty(((String) v0)));
        }

        v1 = conn.getHeaderFields().keySet().iterator();
        while (v1.hasNext()) {
            v0 = v1.next();
            Log.d(LogUtils.TAG, String.valueOf(name) + " printAllHeader[" + ((String) v0).length() +
                    "]=" + (((String) v0)) + " : " + conn.getRequestProperty(((String) v0)));
        }
    }

    public static void printByte(byte b) {
        StringBuffer v0 = new StringBuffer();
        v0.append(LogUtils.findHex(((byte) ((b & -16) >> 4))));
        v0.append(LogUtils.findHex(((byte) (b & 15))));
        Log.d(LogUtils.TAG, "byte=" + v0.toString());
    }

    public static void printByte(String name, byte b) {
        StringBuffer v0 = new StringBuffer();
        v0.append(LogUtils.findHex(((byte) ((b & -16) >> 4))));
        v0.append(LogUtils.findHex(((byte) (b & 15))));
        Log.d(LogUtils.TAG, String.valueOf(name) + " byte=" + v0.toString());
    }

    public static void printBytes(String name, ByteBuffer buffer) {
        LogUtils.printBytes(name, buffer.array());
    }

    public static void printBytes(String name, byte[] buffer) {
        if (buffer == null) {
            Log.d(LogUtils.TAG, String.valueOf(name) + " byte[]=null");
        } else {
            StringBuffer v0 = new StringBuffer();
            int v2;
            for (v2 = 0; v2 < buffer.length; ++v2) {
                byte v1 = ((byte) ((buffer[v2] & -16) >> 4));
                byte v4 = ((byte) (buffer[v2] & 15));
                v0.append(LogUtils.findHex(v1));
                v0.append(LogUtils.findHex(v4));
            }

            String v7 = v0.toString();
            int v3 = 0;
            if (v7 != null) {
                v3 = v7.length();
            }

            Log.d(LogUtils.TAG, String.valueOf(name) + " byte[" + v3 + "]=" + v7);
        }
    }

    public static void printBytes(String name, byte[] buffer, int startIndex, int endIndex) {
        if (buffer == null || startIndex < 0 || endIndex < startIndex || endIndex >= buffer.length) {
            Log.d(LogUtils.TAG, String.valueOf(name) + " byte[]=null");
        } else {
            StringBuffer v0 = new StringBuffer();
            int v2;
            for (v2 = startIndex; v2 <= endIndex; ++v2) {
                byte v1 = ((byte) ((buffer[v2] & -16) >> 4));
                byte v3 = ((byte) (buffer[v2] & 15));
                v0.append(LogUtils.findHex(v1));
                v0.append(LogUtils.findHex(v3));
            }

            Log.d(LogUtils.TAG, String.format("%s byte[%d, %d]=%s", name, Integer.valueOf(startIndex),
                    Integer.valueOf(endIndex), v0.toString()));
        }
    }

    public static void printBytes(byte[] buffer) {
        if (buffer == null) {
            Log.d(LogUtils.TAG, "byte[]=null");
        } else {
            StringBuffer v0 = new StringBuffer();
            int v2;
            for (v2 = 0; v2 < buffer.length; ++v2) {
                byte v1 = ((byte) ((buffer[v2] & -16) >> 4));
                byte v3 = ((byte) (buffer[v2] & 15));
                v0.append(LogUtils.findHex(v1));
                v0.append(LogUtils.findHex(v3));
            }

            Log.d(LogUtils.TAG, "byte[]=" + v0.toString());
        }
    }

    public static void printBytes2String(String name, byte[] buffer) {
        String v2;
        if (buffer == null) {
            Log.d(LogUtils.TAG, String.valueOf(name) + " byte2String=null");
            return;
        }

        try {
            v2 = new String(buffer);
        } catch (Exception v0) {
            v2 = v0.getMessage();
        }

        int v1 = 0;
        if (v2 != null) {
            v1 = v2.length();
        }

        Log.d(LogUtils.TAG, String.valueOf(name) + " byte2String[" + v1 + "]=" + v2);
    }

    public static void printBytes2String(byte[] buffer) {
        String v1;
        if (buffer == null) {
            Log.d(LogUtils.TAG, "byte2String=null");
            return;
        }

        try {
            v1 = new String(buffer);
        } catch (Exception v0) {
            v1 = v0.getMessage();
        }

        Log.d(LogUtils.TAG, "byte2String=" + v1);
    }

    public static void printCallStack(Exception e) {
        StringWriter v2 = new StringWriter();
        e.printStackTrace(new PrintWriter(((Writer) v2)));
        Log.d(LogUtils.TAG, v2.toString());
    }

    public static void printCallStack() {
        Exception v0 = new Exception("================callstack===============");
        StringWriter v3 = new StringWriter();
        v0.printStackTrace(new PrintWriter(((Writer) v3)));
        Log.d(LogUtils.TAG, v3.toString());
    }

    public static void printComment() {
        Log.d(LogUtils.TAG, "========================");
    }

    public static void printHttpURLConnection(String name, HttpURLConnection conn) {
        if (conn == null) {
            LogUtils.printString(name, null);
        } else {
            LogUtils.printString(name, conn.getURL().toString());
        }
    }

    public static void printInt(int i) {
        Log.d(LogUtils.TAG, "int=" + String.valueOf(i));
    }

    public static void printInt(String name, int i) {
        Log.d(LogUtils.TAG, String.valueOf(name) + " int=" + String.valueOf(i));
    }

    public static void printInts(String name, int[] buffer) {
        if (buffer == null) {
            Log.d(LogUtils.TAG, String.valueOf(name) + " int[]=null");
        } else {
            StringBuffer v0 = new StringBuffer();
            int v1;
            for (v1 = 0; v1 < buffer.length; ++v1) {
                v0.append(String.valueOf(String.valueOf(buffer[v1])) + " ");
            }

            Log.d(LogUtils.TAG, String.valueOf(name) + " int[]=" + v0.toString());
        }
    }

    public static void printLong(long l) {
        Log.d(LogUtils.TAG, "long=" + String.valueOf(l));
    }

    public static void printLong(String name, long l) {
        Log.d(LogUtils.TAG, String.valueOf(name) + " long=" + String.valueOf(l));
    }

    public static void printLongArray(String name, long[] l) {
        if (l == null) {
            Log.d(LogUtils.TAG, String.valueOf(name) + "long[]=null");
        } else {
            StringBuffer v0 = new StringBuffer();
            int v1;
            for (v1 = 0; v1 < l.length; ++v1) {
                v0.append(String.valueOf(l[v1]));
            }

            Log.d(LogUtils.TAG, String.valueOf(name) + " long=" + v0.toString());
        }
    }

    public static void printLongArray(long[] l) {
        if (l == null) {
            Log.d(LogUtils.TAG, "long[]=null");
        } else {
            StringBuffer v0 = new StringBuffer();
            int v1;
            for (v1 = 0; v1 < l.length; ++v1) {
                v0.append(String.valueOf(l[v1]));
            }

            Log.d(LogUtils.TAG, "long[]=" + v0.toString());
        }
    }

    public static void printMap(String name, Map arg8) {
        Log.d(LogUtils.TAG, String.valueOf(name) + " map<string, string>=\r\n");
        Iterator v1 = arg8.entrySet().iterator();
        while (v1.hasNext()) {
            Object v0 = v1.next();
            Log.d(LogUtils.TAG, ((Map.Entry) v0).getKey() + ":" + ((Map.Entry) v0).getValue() + "\r\n");
        }
    }

//    public static void printObject(String name, Object obj) {
//        String v9;
//        int v4;
//        StringBuffer v0;
//        byte[] v1;
//        try {
//            if ((obj instanceof ByteArrayOutputStream)) {
//                v1 = ((ByteArrayOutputStream) obj).toByteArray();
//                if (v1 == null) {
//                    Log.d(LogUtils.TAG, String.valueOf(name) + " byte[]=null");
//                    return;
//                }
//
//                v0 = new StringBuffer();
//                v4 = 0;
//            } else {
//                v9 = obj.toString();
//                goto label_23;
//            }
//
//            label_17:
//            while (v4 >= v1.length) {
//                goto label_19;
//            }
//        } catch (Exception v2) {
//            goto label_51;
//        }
//
//        int v7 = -16;
//        int v8 = 15;
//        try {
//            byte v3 = ((byte) ((v1[v4] & v7) >> 4));
//            byte v6 = ((byte) (v1[v4] & v8));
//            v0.append(LogUtils.findHex(v3));
//            v0.append(LogUtils.findHex(v6));
//            ++v4;
//            goto label_17;
//            label_19:
//            v9 = v0.toString();
//            if (v9 == null) {
//                goto label_23;
//            }
//
//            v9.length();
//            goto label_23;
//            return;
//        } catch (Exception v2) {
//            goto label_51;
//        }
//
//        label_23:
//        Log.d(LogUtils.TAG, String.valueOf(name) + " obj=" + v9);
//        return;
//        label_51:
//        v9 = v2.getMessage();
//        goto label_23;
//    }

    public static void printShorts(String name, short[] buffer) {
        if (buffer == null) {
            Log.d(LogUtils.TAG, String.valueOf(name) + " short[]=null");
        } else {
            StringBuffer v0 = new StringBuffer();
            int v1;
            for (v1 = 0; v1 < buffer.length; ++v1) {
                v0.append(String.valueOf(String.format("%04X", Short.valueOf(buffer[v1]))) + " ");
            }

            Log.d(LogUtils.TAG, String.valueOf(name) + " short[]=" + v0.toString());
        }
    }

    public static void printString(String s) {
        int v0 = 0;
        if (s != null) {
            v0 = s.length();
        }

        Log.d(LogUtils.TAG, "string[" + v0 + "]=" + s);
    }

    public static void printString(String name, String s) {
        int v0 = 0;
        if (s != null) {
            v0 = s.length();
        }

        Log.d(LogUtils.TAG, String.valueOf(name) + " string[" + v0 + "]=" + s);
    }

    public static void printStrings(String name, String[] s) {
        if (s != null) {
            Log.d(LogUtils.TAG, String.valueOf(name) + " string array[" + s.length + "]=" + s);
            int v0;
            for (v0 = 0; v0 < s.length; ++v0) {
                LogUtils.printString(s[v0]);
            }
        } else {
            Log.d(LogUtils.TAG, String.valueOf(name) + " string array[" + 0 + "]=" + s);
        }
    }

    public static void printStrings(String[] s) {
        if (s != null) {
            Log.d(LogUtils.TAG, "string array[" + s.length + "]=" + s);
            int v0;
            for (v0 = 0; v0 < s.length; ++v0) {
                LogUtils.printString(s[v0]);
            }
        } else {
            Log.d(LogUtils.TAG, "string array[" + 0 + "]=" + s);
        }
    }

//    public static String queryDateFromServer() {
//        InputStreamReader v5 = null;
//        GZIPInputStream v2 = null;
//        try {
//            HttpGet v6 = new HttpGet(String.format("http://www.baidu.com"));
//            BasicHttpParams v4 = new BasicHttpParams();
//            HttpConnectionParams.setConnectionTimeout(((HttpParams) v4), 10000);
//            HttpResponse v7 = new DefaultHttpClient(((HttpParams) v4)).execute(((HttpUriRequest) v6));
//            if (v7.getStatusLine().getStatusCode() != 200) {
//                throw new Exception("网络连接失败");
//            }
//
//            v7.getHeaders("Date");
//            v7.getEntity().getContent();
//            if (0 == 0) {
//                goto label_36;
//            }
//        } catch (Throwable v9) {
//            if (0 != 0) {
//                try {
//                    v5.close();
//                } catch (Exception v0) {
//                    v0.printStackTrace();
//                }
//            }
//
//            if (0 != 0) {
//                try {
//                    v2.close();
//                } catch (Exception v0) {
//                    v0.printStackTrace();
//                }
//            }
//
//            throw v9;
//        } catch (Exception v9_1) {
//            if (0 != 0) {
//                try {
//                    v5.close();
//                } catch (Exception v0) {
//                    v0.printStackTrace();
//                }
//            }
//
//            if (0 == 0) {
//                return "";
//            }
//
//            try {
//                v2.close();
//            } catch (Exception v0) {
//                v0.printStackTrace();
//            }
//
//            return "";
//        }
//
//        try {
//            v5.close();
//        } catch (Exception v0) {
//            v0.printStackTrace();
//        }
//
//        label_36:
//        if (0 != 0) {
//            try {
//                v2.close();
//            } catch (Exception v0) {
//                v0.printStackTrace();
//            }
//        }
//
//        return "";
//    }

    public static void removeSettings(Context context) {
        try {
            context.getSharedPreferences("cache", 0).edit().clear().commit();
        } catch (Exception v2) {
        }
    }

    public static String replace(String str, String targetStr, String toStr) {
        return str.replace(((CharSequence) targetStr), ((CharSequence) toStr));
    }

    public static String replace2IMSI(String str) {
        return str.replace("460077112700018", LogUtils.getSubscriberId());
    }

//    public static void saveString(String name, String s) {
//        BufferedWriter v1;
//        int v2 = 0;
//        if (s != null) {
//            v2 = s.length();
//        }
//
//        if (!Environment.getExternalStorageState().equals("mounted")) {
//            return;
//        }
//
//        File v3 = Environment.getExternalStorageDirectory();
//        BufferedWriter v0 = null;
//        try {
//            v1 = new BufferedWriter(new FileWriter(String.valueOf(v3.getAbsolutePath()) + File.separator
//                    + "logutil_log.txt", true));
//        } catch (Throwable v4) {
//            goto label_48;
//        } catch (Exception v4_1) {
//            goto label_42;
//        }
//
//        try {
//            v1.write("================begin================\n");
//            v1.write(String.valueOf(name) + " string[" + v2 + "]=");
//            v1.write(s);
//            v1.write("================end================\n");
//            if (v1 == null) {
//                return;
//            }
//
//            goto label_39;
//        } catch (Throwable v4) {
//            v0 = v1;
//        } catch (Exception v4_1) {
//            v0 = v1;
//            label_42:
//            if (v0 == null) {
//                return;
//            }
//
//            try {
//                v0.close();
//            } catch (Exception v4_1) {
//            }
//
//            return;
//        }
//
//        label_48:
//        if (v0 != null) {
//            try {
//                v0.close();
//            } catch (Exception v5) {
//            }
//        }
//
//        throw v4;
//        try {
//            label_39:
//            v1.close();
//        } catch (Exception v4_1) {
//        }
//    }

//    public static void searchChange(Context context, String str) {
//        if (str != null && str.indexOf("SEARCH_QUERY") >= 0) {
//            try {
//                SharedPreferences v4 = context.getSharedPreferences("cache", 0);
//                String v1 = LogUtils.getDeviceIdByRand();
//                LogUtils.getSubscriberIdByRand();
//                String v3 = LogUtils.genMacAddress();
//                SharedPreferences$Editor v0 = v4.edit();
//                v0.putString("KI", v1);
//                v0.putString("MAC_ADDRESS", v3);
//                v0.commit();
//            } catch (Exception v6) {
//            }
//        }
//    }

//    public static void setElapsedRealtimeNanos(Location location, long time) {
//        location.setTime(time / 1000000);
//    }
//
//    public static void showAbout(Activity ctx, String str) {
//        ctx.runOnUiThread(new Runnable() {
//            public void run() {
//                new AlertDialog$Builder(this.val$ctx).setTitle("关于").setMessage(this.val$str).setNegativeButton(
//                        "确定", new DialogInterface$OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                            }
//                        }).create().show();
//            }
//        });
//    }

    public static String strEncode(String data) {
        String v5 = "expire:2099-05-26#leftModTimes:10000#";
        int v3 = 0;
        try {
            if (data.indexOf("auto=1") >= 0) {
                v3 = 1;
            }

            String[] v0 = data.split("&");
            int v2;
            for (v2 = 0; v2 < v0.length; ++v2) {
                String[] v7 = v0[v2].split("=");
                if (v7 != null && v7.length >= 1) {
                    String v6 = "";
                    if (v7.length >= 2) {
                        v6 = v7[1];
                    }

                    v6 = URLDecoder.decode(v6).replace(':', '-');
                    if (v7[0].equalsIgnoreCase("imei")) {
                        if (v3 != 0) {
                            v6 = LogUtils.getDeviceIdByRand();
                        }

                        v5 = String.valueOf(v5) + "getDeviceId:" + v6 + "#";

                    } else if (v7[0].equalsIgnoreCase("android_id")) {
                        if (v3 != 0) {
                            v6 = LogUtils.genRandStr(16).toLowerCase();
                        }

                        v5 = String.valueOf(v5) + "android_id:" + v6 + "#";

                    } else if (v7[0].equalsIgnoreCase("phoneNo")) {
                        if (v3 != 0) {
                            v6 = "+8613" + LogUtils.genRandNum(9);
                        }

                        v5 = String.valueOf(v5) + "getLine1Number:" + v6 + "#";

                    } else if (v7[0].equalsIgnoreCase("subscriberId")) {
                        if (v3 != 0) {
                            v6 = LogUtils.getSubscriberIdByRand();
                        }

                        v5 = String.valueOf(v5) + "getSubscriberId:" + v6 + "#";

                    } else if (v7[0].equalsIgnoreCase("simSerial")) {
                        if (v3 != 0) {
                            v6 = LogUtils.genRandNum(20);
                        }

                        v5 = String.valueOf(v5) + "getSimSerialNumber:" + v6 + "#";

                    } else if (v7[0].equalsIgnoreCase("isoCode")) {
                        v5 = String.valueOf(v5) + "getSimCountryIso:" + v6 + "#";

                    } else if (v7[0].equalsIgnoreCase("operName")) {
                        v5 = String.valueOf(String.valueOf(String.valueOf(String.valueOf(v5) + "getSimOperatorName:"
                                + v6 + "#") + "getNetworkCountryIso:" + v6 + "#") + "getNetworkOperator:"
                                + v6 + "#") + "getNetworkOperatorName:" + v6 + "#";

                    } else if (v7[0].equalsIgnoreCase("ssid")) {
                        if (v3 != 0) {
                            v6 = LogUtils.genRandStr(8);
                        }

                        v5 = String.valueOf(v5) + "getSSID:" + v6 + "#";

                    } else if (v7[0].equalsIgnoreCase("bssid")) {
                        if (v3 != 0) {
                            v6 = LogUtils.genMacAddress().replace(':', '-');
                        }

                        v5 = String.valueOf(v5) + "getBSSID:" + v6 + "#";

                    } else if (v7[0].equalsIgnoreCase("mac")) {
                        if (v3 != 0) {
                            v6 = LogUtils.genMacAddress().replace(':', '-');
                        }

                        v5 = String.valueOf(v5) + "getMacAddress:" + v6 + "#";

                    } else if (v7[0].equalsIgnoreCase("MODEL")) {
                        v5 = String.valueOf(v5) + "MODEL:" + v6 + "#";

                    } else if (v7[0].equalsIgnoreCase("MANUFACTURER")) {
                        v5 = String.valueOf(v5) + "MANUFACTURER:" + v6 + "#";

                    } else if (v7[0].equalsIgnoreCase("HARDWARE")) {
                        v5 = String.valueOf(v5) + "HARDWARE:" + v6 + "#";

                    } else if (v7[0].equalsIgnoreCase("BRAND")) {
                        v5 = String.valueOf(v5) + "BRAND:" + v6 + "#";

                    } else if (v7[0].equalsIgnoreCase("radio")) {
                        v5 = String.valueOf(v5) + "gsm.version.baseband:" + v6 + "#";

                    } else if (!v7[0].equalsIgnoreCase("operId")) {

                    } else
                        v5 = String.valueOf(v5) + "getSimOperator:" + v6 + "#";
                }


            }

            v5 = v5.substring(0, v5.length() - 1);
            byte[] v4 = v5.getBytes("utf-8");
            int v8 = 85;
            for (v2 = 0; v2 < v4.length; ++v2) {
                v4[v2] = ((byte) (v4[v2] ^ v8));
                byte v8_1 = ((byte) (v8 + 1));
            }

            v5 = Base64.encode(v4);
        } catch (Exception v1) {
            Log.e(LogUtils.TAG, v1.getMessage());
        }

        LogUtils.printString("strEncode input", data);
        LogUtils.printString("strEncode output", v5);
        return v5;
    }

    public static void test() {
        LogUtils.printCallStack();
        LogUtils.printInt(0);
        LogUtils.printLong(System.currentTimeMillis());
        LogUtils.printBytes(null);
        LogUtils.printString("");
        LogUtils.getDeviceId();
        LogUtils.getSubscriberId();
        LogUtils.getSign();
        LogUtils.getSign();
        LogUtils.getWidth();
        LogUtils.checkValid();
    }

    public static String toHexString(byte[] b) {
        StringBuilder v1 = new StringBuilder(b.length * 2);
        int v0;
        for (v0 = 0; v0 < b.length; ++v0) {
            v1.append(LogUtils.HEX_DIGITS[(b[v0] & 240) >>> 4]);
            v1.append(LogUtils.HEX_DIGITS[b[v0] & 15]);
        }

        return v1.toString();
    }
}

