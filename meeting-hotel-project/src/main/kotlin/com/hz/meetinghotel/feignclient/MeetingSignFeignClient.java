package com.hz.meetinghotel.feignclient;

import com.alibaba.fastjson.JSONObject;
import com.hz.general.utils.ResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "meetingSignCenter",path = "meeting-sign")
public interface MeetingSignFeignClient {


    // 根据会议id 获取参会人信息
    @GetMapping("meetingsignup/getSignUp/{id}")
    ResultVo<JSONObject> getMeetingSignUp(@PathVariable("id") String meetingId);

    
    //  签到    需要token
    @GetMapping("meetingsign/sign/{meetingId}")
    ResultVo<JSONObject> meetingSign(@PathVariable("meetingId") String meetingId);

    // 获取已签到人信息
    @GetMapping("meetingsign/getSignTime")
    ResultVo<JSONObject> getSignTime(Map map);



}
