package com.face.chargcabinetfactorytest.tools;

import com.face.chargcabinetfactorytest.entity.TempBean;

import java.util.ArrayList;
import java.util.List;

public class DataTools {
    //温度数量
    private static final int TEMP_SIZE=6;


    public static List<TempBean> getTempData(){
        List<TempBean> tempBeans=new ArrayList<>();
        for (int i = 0; i < TEMP_SIZE; i++) {
            tempBeans.add(new TempBean(i,0));
        }
        return tempBeans;
    }
}
