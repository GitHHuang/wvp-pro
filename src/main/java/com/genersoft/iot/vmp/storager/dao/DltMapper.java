package com.genersoft.iot.vmp.storager.dao;

import com.genersoft.iot.vmp.gb28181.bean.Dlt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DltMapper {

    @Select( value = {" <script>" +
            " SELECT * FROM dlt " +
            " </script>"})
    List<Dlt> query();

}
