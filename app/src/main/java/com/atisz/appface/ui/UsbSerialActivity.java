package com.atisz.appface.ui;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.atisz.appface.R;
import com.atisz.appface.eventbus.RxBusUtil;
import com.atisz.appface.ui.base.MyBaseActivity;
import com.atisz.appface.usbserial.driver.UsbSerialDriver;
import com.atisz.appface.usbserial.driver.UsbSerialPort;
import com.atisz.appface.usbserial.driver.UsbSerialProber;
import com.atisz.appface.usbserial.util.HexDump;
import com.atisz.appface.usbserial.util.SerialInputOutputManager;
import com.qmuiteam.qmui.widget.QMUITopBar;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.Subscription;

/**
 * @author wuwei
 * @date 2019/3/7
 */

public class UsbSerialActivity extends MyBaseActivity implements View.OnClickListener {
    private static final String ACTION_USB_SERIAL_PERMISSION = "com.atisz.appface.USB_SERIAL_PERMISSION";
    @BindView(R.id.usb_title)
    QMUITopBar mUsbTitle;
    @BindView(R.id.tv_open)
    TextView mTvOpen;
    @BindView(R.id.tv_close)
    TextView mTvClose;
    @BindView(R.id.tv_send)
    TextView mTvSend;
    @BindView(R.id.tv_status)
    TextView mTvStatus;
    @BindView(R.id.tv_rece)
    TextView mTvRece;
    @BindView(R.id.tv_scan)
    TextView mTvScan;


    private ScanThread mScanThread = null;
    private Subscription mObservable;
    private UsbManager mUsbManager;
    private UsbDevice mUsbSerialDevice;
    private ArrayList<UsbSerialPort> mUsbSerialPorts =  new ArrayList<>();
    private UsbSerialPort mUsbSerialPort;
    private boolean mUsbSerialState = false;
    private SerialInputOutputManager mSerialIoManager;


    private ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private SerialInputOutputManager.Listener mListener = new SerialInputOutputManager.Listener() {
        @Override
        public void onRunError(Exception e) {
            //此处是子线程，不能直接UI
//            alert("错误");
        }

        @Override
        public void onNewData(final byte[] data) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    alert("收到数据");
                    mTvRece.setText(HexDump.toHexString(data));
                }
            });
        }
    };

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("com.atisz.appface.USB_SERIAL_PERMISSION".equals(action)) {
                synchronized (this) {
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        alert("授权成功");
                        usbSerialOpen();
                    } else {
                        alert("授权失败");
                    }
                }
            }
        }
    };


    @Override
    protected void initView(Bundle saveInstanceState) {
        setContentView(R.layout.activity_usbserial);
    }

    @Override
    protected void initData(Bundle saveInstanceState) {
        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        mUsbTitle.setTitle("注册");
        mUsbTitle.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initUsbBroadcast();
        initRxBusUtil();
    }

    private void initUsbBroadcast() {
        IntentFilter filter = new IntentFilter(ACTION_USB_SERIAL_PERMISSION);
        filter.addAction(ACTION_USB_SERIAL_PERMISSION);
        this.registerReceiver(mUsbReceiver, filter);
    }

    @Override
    protected void initOperation(Bundle saveInstanceState) {
        mTvScan.setOnClickListener(this);
        mTvOpen.setOnClickListener(this);
        mTvClose.setOnClickListener(this);
        mTvSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_scan:
                usbSerialScan();
                break;
            case R.id.tv_open:
                usbSerialPreOpen();
                break;
            case R.id.tv_close:
                usbSerialClose();
                break;
            case R.id.tv_send:
                final byte[] bytes = new byte[2];
                bytes[0] = 0x05;
                bytes[1] = 0x06;
                usbSerialSend(bytes);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        usbSerialClose();
        mUsbSerialPorts.clear();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void initRxBusUtil() {
        mObservable = RxBusUtil.getInstance().getObservable(String.class)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
//                        alert("收到消息onCompleted");
                    }

                    @Override
                    public void onError(Throwable throwable) {
//                        alert("收到消息onError");
                    }

                    @Override
                    public void onNext(String s) {
//                        alert("收到消息onNext"+s);
                        mUsbSerialDevice = mUsbSerialPorts.get(0).getDriver().getDevice();
                        final String string1 = HexDump.toHexString((short) mUsbSerialDevice.getVendorId());
                        final String string2 = HexDump.toHexString((short) mUsbSerialDevice.getProductId());
                        mTvStatus.setText(string1 + "  " + string2);
                        dismissProgress();
                    }
                });
    }

    private void usbSerialScan() {
        if (mUsbSerialState == false) {
            showProgress("正在扫描");
            if (mUsbSerialPorts.size() != 0) {
                mUsbSerialPorts.clear();
            }
            if (mScanThread == null) {
                mScanThread = new ScanThread();
            }
            mScanThread.start();
            mTvScan.setEnabled(false);
        }
    }

    private boolean usbSerialCheckPermission(boolean autoRequest) {
        if (!mUsbManager.hasPermission(mUsbSerialDevice)) {
            if (autoRequest) {
                PendingIntent mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_SERIAL_PERMISSION), 0);
                mUsbManager.requestPermission(mUsbSerialDevice, mPermissionIntent);
            }
            return false;
        }
        return true;
    }

    // 准备打开串口
    private void usbSerialPreOpen() {
        if (usbSerialCheckPermission(true)) {
            usbSerialOpen();
        }
    }

    // 打开串口
    private void usbSerialOpen() {
        if (mUsbSerialPorts.size() != 0) {
            mUsbSerialPort = mUsbSerialPorts.get(0);
            final UsbDeviceConnection usbDeviceConnection = mUsbManager.openDevice(mUsbSerialPort.getDriver().getDevice());
            if (usbDeviceConnection == null) {
                alert("打开失败");
            } else {
                try {
                    mUsbSerialPort.open(usbDeviceConnection);
                    mUsbSerialPort.setParameters(9600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
                    alert("打开成功");
                    mTvStatus.setText("串口已打开");
                    mUsbSerialState = true;
                    usbSerialReceive();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            alert("没有串口");
        }
    }

    private void usbSerialClose() {
        if (mUsbSerialState == true) {
            try {
                mUsbSerialPort.close();
                mUsbSerialState = false;
                if (mScanThread != null){
                    mScanThread.interrupt();
                    mScanThread = null;
                }
                mTvStatus.setText("串口已关闭");
                alert("关闭成功");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void usbSerialSend(final byte[] b) {
        if (mUsbSerialState == true && mUsbSerialPort != null) {
            try {
                mUsbSerialPort.write(b, 100);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void usbSerialReceive() {
        if (mUsbSerialPort != null) {
            mSerialIoManager = new SerialInputOutputManager(mUsbSerialPort, mListener);
            mExecutor.submit(mSerialIoManager);
        }
    }


    private class ScanThread extends Thread {
        @Override
        public void run() {
            super.run();
            final List<UsbSerialDriver> drivers = UsbSerialProber.getDefaultProber().findAllDrivers(mUsbManager);
            for (final UsbSerialDriver driver : drivers) {
                final List<UsbSerialPort> ports = driver.getPorts();
                mUsbSerialPorts.addAll(ports);
            }
            RxBusUtil.getInstance().send("扫描完成");
        }
    }


}
