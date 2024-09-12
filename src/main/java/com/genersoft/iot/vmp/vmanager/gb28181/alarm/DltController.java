package com.genersoft.iot.vmp.vmanager.gb28181.alarm;

import com.genersoft.iot.vmp.conf.security.JwtUtils;
import com.genersoft.iot.vmp.gb28181.bean.Dlt;
import com.genersoft.iot.vmp.service.IDltService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Tag(name = "查询抄表数据")

@RestController
@RequestMapping("/api/dlt")
public class DltController {

    @Autowired
    private IDltService dltService;

    /**
     *  分页查询报警
     */
    @Operation(summary = "分页查询报警", security = @SecurityRequirement(name = JwtUtils.HEADER))
    @GetMapping("/all")
    public List<Dlt> getAll() {
        return dltService.getAllAlarm();
    }

    public static void main(String[] args) {
        Object turnOnTime = "";
        String turnOffTime = "1724065800000";
        long  longtime = Integer.parseInt(turnOffTime);
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = timeFormat.format(new Date(longtime));
        System.out.println(longtime);
        System.out.println(format);
    }
}
