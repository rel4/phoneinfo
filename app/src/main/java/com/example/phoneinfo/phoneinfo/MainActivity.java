package com.example.phoneinfo.phoneinfo;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    String pwd = "";
    String uName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parseAccInFile();
        setInput(R.id.name, this.uName);
        setInput(R.id.pwd, this.pwd);
        buildUI();

    }

    public void buildUI() {
        LinearLayout localLinearLayout = (LinearLayout) findViewById(R.id.linearLayoutInScroll);
        localLinearLayout.removeAllViews();
        addIMEI(localLinearLayout);
        addAdId(localLinearLayout);
        addWifiMac(localLinearLayout);
        addWifiSSID(localLinearLayout);
        addWifiBSSID(localLinearLayout);
        addPhoneNo(localLinearLayout);
        addPhoneSimSerail(localLinearLayout);
        addPhoneSimSub(localLinearLayout);
        addModel(localLinearLayout);
        addManualfactory(localLinearLayout);
        addHARDWARE(localLinearLayout);
        addBRAND(localLinearLayout);
        addRadio(localLinearLayout);
        bindClick(R.id.button1);
        bindClick(R.id.button2);
    }

    public void addRadio(LinearLayout paramLinearLayout) {
        TableRow localTableRow = new TableRow(this);
        addLabel(localTableRow, "基带版本radio");
        EditText localEditText = new EditText(this);
        localEditText.setText(Build.getRadioVersion());
        localEditText.setId(R.id.radio);
        localEditText.setLayoutParams(new TableRow.LayoutParams(-2, -2));
        localTableRow.addView(localEditText);
        paramLinearLayout.addView(localTableRow);
    }

    public void addBRAND(LinearLayout paramLinearLayout) {
        TableRow localTableRow = new TableRow(this);
        addLabel(localTableRow, "品牌  BRAND");
        EditText localEditText = new EditText(this);
        localEditText.setText(Build.BRAND);
        localEditText.setId(R.id.BRAND);
        localEditText.setLayoutParams(new TableRow.LayoutParams(-2, -2));
        localTableRow.addView(localEditText);
        paramLinearLayout.addView(localTableRow);
    }

    public void addHARDWARE(LinearLayout paramLinearLayout) {
        TableRow localTableRow = new TableRow(this);
        addLabel(localTableRow, "硬件  HARDWARE");
        EditText localEditText = new EditText(this);
        localEditText.setText(Build.HARDWARE);
        localEditText.setId(R.id.HARDWARE);
        localEditText.setLayoutParams(new TableRow.LayoutParams(-2, -2));
        localTableRow.addView(localEditText);
        paramLinearLayout.addView(localTableRow);
    }

    public void addManualfactory(LinearLayout paramLinearLayout) {
        TableRow localTableRow = new TableRow(this);
        addLabel(localTableRow, "厂商  MANUFACTURER");
        EditText localEditText = new EditText(this);
        localEditText.setText(Build.MANUFACTURER);
        localEditText.setId(R.id.MANUFACTURER);
        localEditText.setLayoutParams(new TableRow.LayoutParams(-2, -2));
        localTableRow.addView(localEditText);
        paramLinearLayout.addView(localTableRow);
    }

    public void addModel(LinearLayout paramLinearLayout) {
        TableRow localTableRow = new TableRow(this);
        addLabel(localTableRow, "机型 MODEL");
        EditText localEditText = new EditText(this);
        localEditText.setText(Build.MODEL);
        localEditText.setId(R.id.MODEL);
        localEditText.setLayoutParams(new TableRow.LayoutParams(-2, -2));
        localTableRow.addView(localEditText);
        paramLinearLayout.addView(localTableRow);
    }

    public void addPhoneSimSub(LinearLayout paramLinearLayout) {
        TelephonyManager localTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        TableRow localTableRow1 = new TableRow(this);
        addLabel(localTableRow1, "Sim订阅ID(IMSI)");
        EditText localEditText1 = new EditText(this);
        localEditText1.setId(R.id.subscriberId);
        localEditText1.setLayoutParams(new TableRow.LayoutParams(-2, -2));
        localTableRow1.addView(localEditText1);
        localEditText1.setText(localTelephonyManager.getSubscriberId());
        paramLinearLayout.addView(localTableRow1);
        TableRow localTableRow2 = new TableRow(this);
        addLabel(localTableRow2, "SIM卡状态(5=就绪)");
        EditText localEditText2 = new EditText(this);
        localEditText2.setId(R.id.simState);
        localEditText2.setLayoutParams(new TableRow.LayoutParams(-2, -2));
        localTableRow2.addView(localEditText2);
        localEditText2.setText(localTelephonyManager.getSimState() + "");
        paramLinearLayout.addView(localTableRow2);
        TableRow localTableRow3 = new TableRow(this);
        addLabel(localTableRow3, "运营商ID");
        EditText localEditText3 = new EditText(this);
        localEditText3.setId(R.id.operId);
        localEditText3.setLayoutParams(new TableRow.LayoutParams(-2, -2));
        localTableRow3.addView(localEditText3);
        localEditText3.setText(localTelephonyManager.getSimOperator());
        paramLinearLayout.addView(localTableRow3);
        TableRow localTableRow4 = new TableRow(this);
        addLabel(localTableRow4, "运营商名称");
        EditText localEditText4 = new EditText(this);
        localEditText4.setId(R.id.operName);
        localEditText4.setLayoutParams(new TableRow.LayoutParams(-2, -2));
        localTableRow4.addView(localEditText4);
        localEditText4.setText(localTelephonyManager.getSimOperatorName());
        paramLinearLayout.addView(localTableRow4);
        TableRow localTableRow5 = new TableRow(this);
        addLabel(localTableRow5, "国家码");
        EditText localEditText5 = new EditText(this);
        localEditText5.setId(R.id.isoCode);
        localEditText5.setLayoutParams(new TableRow.LayoutParams(-2, -2));
        localTableRow5.addView(localEditText5);
        localEditText5.setText(localTelephonyManager.getSimCountryIso());
        paramLinearLayout.addView(localTableRow5);
    }

    public void addPhoneSimSerail(LinearLayout paramLinearLayout) {
        TelephonyManager localTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        TableRow localTableRow = new TableRow(this);
        addLabel(localTableRow, "Sim序列号(ICCID)");
        EditText localEditText = new EditText(this);
        localEditText.setId(R.id.simSerial);
        localEditText.setLayoutParams(new TableRow.LayoutParams(-2, -2));
        localTableRow.addView(localEditText);
        localEditText.setText(localTelephonyManager.getSimSerialNumber());
        paramLinearLayout.addView(localTableRow);
    }

    public void addPhoneNo(LinearLayout paramLinearLayout) {
        TelephonyManager localTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        TableRow localTableRow = new TableRow(this);
        addLabel(localTableRow, "手机号");
        EditText localEditText = new EditText(this);
        localEditText.setId(R.id.phoneNo);
        localEditText.setLayoutParams(new TableRow.LayoutParams(-2, -2));
        localTableRow.addView(localEditText);
        localEditText.setText(localTelephonyManager.getLine1Number());
        paramLinearLayout.addView(localTableRow);
    }

    public void addWifiBSSID(LinearLayout paramLinearLayout) {
        WifiInfo localWifiInfo = ((WifiManager) getSystemService(Context.WIFI_SERVICE)).getConnectionInfo();
        TableRow localTableRow = new TableRow(this);
        addLabel(localTableRow, "Wifi B-SSID");
        EditText localEditText = new EditText(this);
        localEditText.setId(R.id.bssid);
        localEditText.setLayoutParams(new TableRow.LayoutParams(-2, -2));
        localTableRow.addView(localEditText);
        localEditText.setText(localWifiInfo.getBSSID());
        paramLinearLayout.addView(localTableRow);
    }

    public void addWifiSSID(LinearLayout paramLinearLayout) {
        WifiInfo localWifiInfo = ((WifiManager) getSystemService(Context.WIFI_SERVICE)).getConnectionInfo();
        TableRow localTableRow = new TableRow(this);
        addLabel(localTableRow, "Wifi SSID");
        EditText localEditText = new EditText(this);
        localEditText.setId(R.id.ssid);
        localEditText.setLayoutParams(new TableRow.LayoutParams(-2, -2));
        localTableRow.addView(localEditText);
        localEditText.setText(localWifiInfo.getSSID());
        paramLinearLayout.addView(localTableRow);
    }

    public void addWifiMac(LinearLayout paramLinearLayout) {
        WifiInfo localWifiInfo = ((WifiManager) getSystemService(Context.WIFI_SERVICE)).getConnectionInfo();
        TableRow localTableRow = new TableRow(this);
        addLabel(localTableRow, "Wifi Mac 地址");
        EditText localEditText = new EditText(this);
        localEditText.setId(R.id.mac);
        localEditText.setLayoutParams(new TableRow.LayoutParams(-2, -2));
        localTableRow.addView(localEditText);
        localEditText.setText(localWifiInfo.getMacAddress());
        paramLinearLayout.addView(localTableRow);
    }

    private void addAdId(LinearLayout paramLinearLayout) {
        TableRow localTableRow = new TableRow(this);
        addLabel(localTableRow, "安卓ID(android_id)");
        String str = Settings.Secure.getString(getContentResolver(), "android_id");
        EditText localEditText = new EditText(this);
        localEditText.setId(R.id.android_id);
        localEditText.setLayoutParams(new TableRow.LayoutParams(-2, -2));
        localTableRow.addView(localEditText);
        localEditText.setText(str);
        paramLinearLayout.addView(localTableRow);
    }


    private void addIMEI(LinearLayout paramLinearLayout) {
        TableRow localTableRow = new TableRow(this);
        addLabel(localTableRow, "机器码(IMEI)");
        String str = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        EditText localEditText = new EditText(this);
        localEditText.setText(str);
        localEditText.setId(R.id.imei);
        localEditText.setLayoutParams(new TableRow.LayoutParams(-2, -2));
        localTableRow.addView(localEditText);
        paramLinearLayout.addView(localTableRow);
    }

    private void bindClick(int paramInt) {
        findViewById(paramInt).setOnClickListener(this);
    }


    private void setInput(int paramInt, String paramString) {
        ((EditText) findViewById(paramInt)).setText(paramString);
    }

    public void parseAccInFile() {
        File localFile = new File("/mnt/sdcard/bsToolInfo");
        if (!localFile.exists())
            return;
        try {
            BufferedReader localBufferedReader = new BufferedReader(new FileReader(localFile));
            String str = localBufferedReader.readLine();
            System.out.println("accLine:" + str);
            if (str != null) {
                String[] arrayOfString = str.split(":");
                this.uName = arrayOfString[0];
                this.pwd = arrayOfString[1];
            }
            localBufferedReader.close();
            return;
        } catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
            return;
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    private void addLabel(TableRow paramTableRow, String paramString) {
        TextView localTextView = new TextView(this);
        localTextView.setText(paramString);
        localTextView.setLayoutParams(new TableRow.LayoutParams(150, -2));
        paramTableRow.addView(localTextView);
    }

    private String getInput(int paramInt) {
        return URLEncoder.encode(((EditText) findViewById(paramInt)).getText().toString());
    }

    @Override
    public void onClick(final View v) {
        this.uName = getInput(R.id.name);
        this.pwd = getInput(R.id.pwd);
        final StringBuffer localStringBuffer = new StringBuffer();
        localStringBuffer.append("&u=" + getInput(R.id.name));
        localStringBuffer.append("&p=" + getInput(R.id.pwd));
        localStringBuffer.append("&phoneNo=" + getInput(R.id.phoneNo));
        localStringBuffer.append("&imei=" + getInput(R.id.imei));
        localStringBuffer.append("&mac=" + getInput(R.id.mac));
        localStringBuffer.append("&android_id=" + getInput(R.id.android_id));
        localStringBuffer.append("&subscriberId=" + getInput(R.id.subscriberId));
        localStringBuffer.append("&simSerial=" + getInput(R.id.simSerial));
        localStringBuffer.append("&ssid=" + getInput(R.id.ssid));
        localStringBuffer.append("&MODEL=" + getInput(R.id.MODEL));
        localStringBuffer.append("&MANUFACTURER=" + getInput(R.id.MANUFACTURER));
        localStringBuffer.append("&HARDWARE=" + getInput(R.id.HARDWARE));
        localStringBuffer.append("&BRAND=" + getInput(R.id.BRAND));
        localStringBuffer.append("&radio=" + getInput(R.id.radio));
        localStringBuffer.append("&simSate=" + getInput(R.id.simState));
        localStringBuffer.append("&operId=" + getInput(R.id.operId));
        localStringBuffer.append("&operName=" + getInput(R.id.operName));
        localStringBuffer.append("&isoCode=" + getInput(R.id.isoCode));
        localStringBuffer.append("&bssid=" + getInput(R.id.bssid));
        getResources().getConfiguration();
        if (v.getId() == R.id.button2)
            localStringBuffer.append("&auto=1");
        new Thread(new Runnable() {
            public void run() {
                runIt(localStringBuffer.toString());
            }
        }).start();
    }

    private void runIt(String param) {
        String v3;
        Downloader v1 = new Downloader();
        try {
            String v0 = v1.getGen(param);
            if (v0.startsWith("Error:#")) {
                System.setProperty(TelephonyManager.class.getSimpleName(), v0);
                v3 = "出错了：" + v0;

            } else {
                v1.save("mbi");
                v3 = "保存成功";
                saveAccInfo();
            }


            v3 = "保存失败~";
        } catch (ConnectTimeoutException v2_1) {
            v3 = "保存失败:连接超时" + v2_1;
        } catch (Exception v2) {
            v3 = "保存失败:" + v2;
            v2.printStackTrace();
        }


        this.findViewById(R.id.button1).post(new Runnable() {
            public void run() {
//                Toast.makeText(this, v3, 0).show();
                buildUI();
            }
        });
    }

    void saveAccInfo() {
        File v1 = new File("/mnt/sdcard/bsToolInfo");
        try {
            BufferedWriter v2 = new BufferedWriter(new FileWriter(v1));
            v2.append(this.uName);
            v2.append(":");
            v2.append(this.pwd);
            v2.append(":");
            v2.close();
        } catch (Exception v0) {
            v0.printStackTrace();
        }
    }
//    private void runIt(String paramString)
//    {
//        Downloader localDownloader = new Downloader();
//        try
//        {
//            String str3 = localDownloader.getGen(paramString);
//            if (str3.startsWith("Error:#"))
//            {
//                System.setProperty(TelephonyManager.class.getSimpleName(), str3);
//                String str4 = "出错了：" + str3;
//                str1 = str4;
//            }
//            while (true)
//            {
//                String str2 = str1;
//                findViewById(2130968601).post(new Runnable(str2)
//                {
//                    public void run()
//                    {
//                        Toast.makeText(ParamActivity.this, this.val$s, 0).show();
//                        ParamActivity.this.buildUI();
//                    }
//                });
//                return;
//                localDownloader.save("mbi");
//                if (1 == 0)
//                    break;
//                str1 = "保存成功";
//                saveAccInfo();
//            }
//        }
//        catch (ConnectTimeoutException localConnectTimeoutException)
//        {
//            while (true)
//            {
//                str1 = "保存失败:连接超时" + localConnectTimeoutException;
//                continue;
//                str1 = "保存失败~";
//            }
//        }
//        catch (Exception localException)
//        {
//            while (true)
//            {
//                String str1 = "保存失败:" + localException;
//                localException.printStackTrace();
//            }
//        }
//    }
}
