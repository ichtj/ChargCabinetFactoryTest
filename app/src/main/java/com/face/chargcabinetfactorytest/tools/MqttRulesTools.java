package com.face.chargcabinetfactorytest.tools;

import com.face_chtj.base_iotutils.KLog;
import com.face_chtj.base_iotutils.serialport.SerialPort;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MqttRulesTools {
    private static volatile MqttRulesTools sInstance;
    public static final String M_WRITE_SN      ="writesn";//
    public static final String M_GET_SN        ="getsn";//
    public static final String M_FAN_STATUS    ="fan";//
    public static final String M_TEMP_GET      ="temp";//
    public static final String M_OPEN_LOCK     ="openLock";//
    public static final String M_OPEN_ALL_LOCK ="openAllLock";//
    private SerialPort serialPort;
    private boolean isInitSerial;
    private Disposable disposable;
    private String COM_PATH;
    private int COM_BAUDRATE;
    private String serialNo;
    private ChargcabinetCallback callback;

    //单例模式
    public static MqttRulesTools instance() {
        if (sInstance == null) {
            synchronized (MqttRulesTools.class) {
                if (sInstance == null) {
                    sInstance = new MqttRulesTools();
                }
            }
        }
        return sInstance;
    }
    /**
     * 开启串口
     * @return 是否成功
     */
    public boolean startSerialPort(String COM_PATH, int COM_BAUDRATE){
        return startSerialPort(COM_PATH,COM_BAUDRATE,null);
    }

    /**
     * 开启串口
     * @return 是否成功
     */
    public boolean startSerialPort(String COM_PATH, int COM_BAUDRATE,ChargcabinetCallback callback) {
        try {
            instance().callback=callback;
            instance().COM_PATH=COM_PATH;
            instance().COM_BAUDRATE=COM_BAUDRATE;
            startSerialPort();
            KLog.d("startSerialPort() serialPort >> " + instance().serialPort);
            startMonitorSerial();
        } catch (Throwable e) {
            instance().isInitSerial = false;
        }
        return instance().isInitSerial;
    }


    /**
     * 开启串口
     * @return 是否成功
     */
    private boolean startSerialPort() {
        try {
            instance().serialPort = new SerialPort(new File(instance().COM_PATH), instance().COM_BAUDRATE, 0);
            instance().isInitSerial = true;
            KLog.d("startSerialPort() serialPort >> " + instance().serialPort);
        } catch (Throwable e) {
            instance().isInitSerial = false;
        }
        return instance().isInitSerial;
    }

    /**
     * 开始获取串口返回数据并回调
     */
    private void startMonitorSerial() {
        disposable = Observable
                .interval(3, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())//调用切换之前的线程。
                .observeOn(AndroidSchedulers.mainThread())//调用切换之后的线程。observeOn之后，不可再调用subscribeOn 切换线程
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (!isInitSerial) {
                            startSerialPort();
                        }
                        if (serialPort != null) {
                            int readSize = -1;
                            readSize = serialPort.getInputStream().available();
                            if (readSize > 0) {
                                byte[] bytes = new byte[readSize];
                                serialPort.read(bytes, bytes.length);
                                KLog.d(bytes.toString());
                            }

                            //这里为测试条件
                            String method="";
                            Map<String,Object> map=new HashMap<>();
                            if(callback!=null){
                                callback.switchMethod(method,map);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        KLog.d("startMonitorSerial throwable>>" + throwable.getMessage());
                        closeDisposable();
                        startMonitorSerial();
                    }
                });
    }


    public void closeSerial() {
        if (instance().serialPort != null) {
            instance().serialPort.close();
        }
        instance().isInitSerial = false;
    }

    public void closeDisposable() {
        KLog.d("closeDisposable");
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            disposable = null;
        }
    }


    /**
     * 发送串口指令
     * @param msg 指令内容
     */
    private static void senCmd(String msg) {
        instance().serialPort.write(msg.getBytes());
    }

    /**
     * 一键开启所有锁
     */
    public static void openAllLock() {
        Map map = new HashMap();
        map.put("p0", 1);
        senCmd(convertData(M_OPEN_ALL_LOCK, instance().serialNo, map));
    }


    /**
     * 开锁 1-6
     */
    public static void openLock(int lockPos) {
        Map map = new HashMap();
        map.put("p0", lockPos);
        senCmd(convertData(M_OPEN_LOCK, instance().serialNo, map));
    }


    /**
     * 写入SN
     * @param writeSn
     */
    public static void writeSn(String writeSn) {
        Map map = new HashMap();
        map.put("p0", writeSn);
        senCmd(convertData(M_WRITE_SN, instance().serialNo, map));
    }


    /**
     * 获取SN
     */
    public static void getSn() {
        Map map = new HashMap();
        senCmd(convertData(M_GET_SN, "", map));
    }

    /**
     * 获取风扇
     */
    public static void getFan(int pos) {
        Map map = new HashMap();
        map.put("p0", pos);
        senCmd(convertData(M_FAN_STATUS, instance().serialNo, map));
    }

    /**
     * 获取温度
     */
    public static void getTemp(int pos) {
        Map map = new HashMap();
        map.put("p0", pos);
        senCmd(convertData(M_TEMP_GET, instance().serialNo, map));
    }

    /**
     * 数据转换
     * @param action 属性| 方法
     * @param did 设备号
     * @param in 携带参数
     * @return 指令
     */
    public static String convertData(String action, String did, Map in) {
        String srcJson = "{\n" +
                "\t\"iid\": \"uuid\",\n" +
                "\t\"act\": \"cmd\",\n" +
                "\t\"ack\": \"app/" + did + "\",\n" +
                "\t\"inputs\": {\n" +
                "\t\t\"intent\": \"action\",\n" +
                "\t\t\"action\": [{\n" +
                "\t\t\t\"did\": \"" + did + "\",\n" +
                "\t\t\t\"method\": \"" + action + "\"\n" +
                "\t\t\t\"in\": " + GsonTools.toJsonFilterNullField(in) +
                "\t\t}]\n" +
                "\t}\n" +
                "}";
        return srcJson;
    }


}
