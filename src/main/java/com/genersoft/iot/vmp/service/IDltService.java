package com.genersoft.iot.vmp.service;

import com.genersoft.iot.vmp.gb28181.bean.Dlt;

import java.util.List;

/**
 * 报警相关业务处理
 */
public interface IDltService {

    List<Dlt> getAllAlarm();

}
