package com.face.chargcabinetfactorytest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.face.chargcabinetfactorytest.adapter.LockAdapter;
import com.face.chargcabinetfactorytest.adapter.ChargAdapter;
import com.face.chargcabinetfactorytest.adapter.TempAdapter;
import com.face.chargcabinetfactorytest.entity.TempBean;
import com.face.chargcabinetfactorytest.tools.ChargcabinetCallback;
import com.face.chargcabinetfactorytest.tools.DataTools;
import com.face.chargcabinetfactorytest.tools.MqttRulesTools;
import com.face_chtj.base_iotutils.KLog;
import com.face_chtj.base_iotutils.display.ToastUtils;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnItemClickListener, View.OnClickListener, ChargcabinetCallback {
    TextView tvNetStatus;
    TextView tvSn;
    TextView tvFan;
    TextView tvFwVersion;
    EditText etSn;
    RecyclerView rvLockView, rvCharge, rvTemp;
    ChargAdapter chargAdapter;
    LockAdapter lockAdapter;
    TempAdapter tempAdapter;
    Spinner spPortNum, spBaudr;
    Button btnTemp;
    Button btnOpenLock;
    Button btnWriteSn;
    Button btnGetFan;
    Button btnOCserial;
    boolean isSerialFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvFan = findViewById(R.id.tvFan);
        rvCharge = findViewById(R.id.rvCharge);
        tvFwVersion = findViewById(R.id.tvFwVersion);
        spPortNum = findViewById(R.id.spPortNum);
        spBaudr = findViewById(R.id.spBaudr);
        tvSn = findViewById(R.id.tvSn);
        tvNetStatus = findViewById(R.id.tvNetStatus);
        etSn = findViewById(R.id.etSn);
        rvTemp = findViewById(R.id.rvTemp);
        rvLockView = findViewById(R.id.rvLockView);
        btnWriteSn = findViewById(R.id.btnWriteSn);
        btnWriteSn.setOnClickListener(this);
        btnOpenLock = findViewById(R.id.btnOpenLock);
        btnOpenLock.setOnClickListener(this);
        btnOCserial = findViewById(R.id.btnOCserial);
        btnOCserial.setOnClickListener(this);
        btnTemp = findViewById(R.id.btnTemp);
        btnTemp.setOnClickListener(this);
        btnGetFan = findViewById(R.id.btnGetFan);
        btnGetFan.setOnClickListener(this);
        rvLockView.setLayoutManager(new GridLayoutManager(this, 6));
        rvCharge.setLayoutManager(new GridLayoutManager(this, 3));
        rvTemp.setLayoutManager(new GridLayoutManager(this, 3));


        lockAdapter = new LockAdapter(this, 12);
        lockAdapter.setOnItemClick(this);
        rvLockView.setAdapter(lockAdapter);


        chargAdapter = new ChargAdapter(this, 6);
        chargAdapter.setOnItemClick(this);
        rvCharge.setAdapter(chargAdapter);

        tempAdapter = new TempAdapter(this, DataTools.getTempData());
        tempAdapter.setOnItemClick(this);
        rvTemp.setAdapter(tempAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KLog.d("onDestroy() >> ");
    }

    @Override
    public void onItemClick(View view, int position) {
        ToastUtils.success("now click onItemClick="+position);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        ToastUtils.success("now click onItemLongClick="+position);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOCserial://??????????????????
                if (!isSerialFlag) {
                    String COM_PATH = spPortNum.getSelectedItem().toString();
                    int COM_BAUDRATE = Integer.parseInt(spBaudr.getSelectedItem().toString());
                    boolean isOpen = MqttRulesTools.instance().startSerialPort(COM_PATH, COM_BAUDRATE,this);
                    if (isOpen) {
                        ToastUtils.success("?????????????????????");
                    } else {
                        ToastUtils.error("??????????????????,????????????");
                    }
                    isSerialFlag = true;
                    btnOCserial.setText("????????????");
                } else {
                    MqttRulesTools.instance().closeSerial();
                    ToastUtils.info("??????????????????");
                    isSerialFlag = false;
                    btnOCserial.setText("????????????");
                }
                break;
            case R.id.btnOpenAllLock://??????????????????
                MqttRulesTools.instance().openAllLock();
                break;
            case R.id.btnOpenLock://??????
                MqttRulesTools.instance().openLock(0);
                break;
            case R.id.btnWriteSn://??????SN
                String writeSn=etSn.getText().toString();
                if(!TextUtils.isEmpty(writeSn)){
                    MqttRulesTools.instance().writeSn(writeSn);
                }else{
                    ToastUtils.error("??????????????????SN??????");
                }
                break;
            case R.id.btnGetSn://??????SN
                MqttRulesTools.instance().getSn();
                break;
            case R.id.btnGetFan://??????????????????
                MqttRulesTools.instance().getFan(0);
                break;
            case R.id.btnTemp://????????????
                MqttRulesTools.instance().getTemp(0);
                break;
        }
    }


    @Override
    public void switchMethod(String method, Map<String,Object> maps){
        switch (method){
            case MqttRulesTools.M_WRITE_SN:
                ToastUtils.success("???????????????");
                break;
            case MqttRulesTools.M_GET_SN:
                tvSn.setText("?????????(SN)???"+maps.toString());
                break;
            case MqttRulesTools.M_FAN_STATUS:
                tvFan.setText("???????????????"+maps.toString());
                break;
            case MqttRulesTools.M_TEMP_GET:
                int pos=0;
                int temp=0;
                tempAdapter.updatePos(pos,temp);
                break;
            case MqttRulesTools.M_OPEN_LOCK:
                ToastUtils.success("???????????????");
                break;
            case MqttRulesTools.M_OPEN_ALL_LOCK:
                ToastUtils.success("?????????????????????");
                break;
        }
    }
}