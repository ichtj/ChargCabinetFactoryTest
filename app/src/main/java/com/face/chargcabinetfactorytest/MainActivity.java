package com.face.chargcabinetfactorytest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.face.chargcabinetfactorytest.adapter.LockAdapter;
import com.face.chargcabinetfactorytest.adapter.ChargAdapter;
import com.face.chargcabinetfactorytest.adapter.TempAdapter;
import com.face_chtj.base_iotutils.KLog;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {
    TextView tvNetStatus;
    TextView tvSn;
    TextView tvFwVersion;
    EditText etSn;
    RecyclerView rvLockView,rvCharge,rvTemp;
    ChargAdapter chargAdapter;
    LockAdapter lockAdapter;
    TempAdapter tempAdapter;
    Spinner spPortNum,spBaudr;
    Button btnTemp;
    Button btnOpenLock;
    Button btnWriteSn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvCharge=findViewById(R.id.rvCharge);
        tvFwVersion=findViewById(R.id.tvFwVersion);
        spPortNum=findViewById(R.id.spPortNum);
        spBaudr=findViewById(R.id.spBaudr);
        tvSn=findViewById(R.id.tvSn);
        tvNetStatus=findViewById(R.id.tvNetStatus);
        etSn=findViewById(R.id.etSn);
        btnWriteSn=findViewById(R.id.btnWriteSn);
        rvTemp=findViewById(R.id.rvTemp);
        rvLockView=findViewById(R.id.rvLockView);
        btnOpenLock=findViewById(R.id.btnOpenLock);
        btnTemp=findViewById(R.id.btnTemp);
        rvLockView.setLayoutManager(new GridLayoutManager(this,6));
        rvCharge.setLayoutManager(new GridLayoutManager(this,3));
        rvTemp.setLayoutManager(new GridLayoutManager(this,3));


        lockAdapter =new LockAdapter(this,6);
        lockAdapter.setOnItemClick(this);
        rvLockView.setAdapter(lockAdapter);


        chargAdapter =new ChargAdapter(this,6);
        chargAdapter.setOnItemClick(this);
        rvCharge.setAdapter(chargAdapter);

        tempAdapter=new TempAdapter(this,6);
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

    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}