package com.genersoft.iot.vmp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.genersoft.iot.vmp.gb28181.bean.Dlt;
import com.genersoft.iot.vmp.service.IDltService;
import com.genersoft.iot.vmp.storager.dao.DltMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("master")
public class DltServiceImpl implements IDltService {

    @Autowired
    private DltMapper dltMapper;

    @Override
    public List<Dlt> getAllAlarm() {
        List<Dlt> all = dltMapper.query();
        return all;
    }
}
