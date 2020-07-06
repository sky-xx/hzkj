package com.hz.meetinghotel.feignclient;

import com.alibaba.fastjson.JSONObject;
import com.hz.general.utils.ResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "meetingCenter",path = "/meeting-center")
public interface MeetingFeignClient {

    @GetMapping("meeting/meeting/{id}")
    ResultVo<JSONObject> findById(@PathVariable("id") String id);



    @PutMapping("meetingInvite/meetingInvite/state/{meetingId}/{userId}/{state}")
    ResultVo<JSONObject> updateInviteData(@PathVariable("meetingId") String meetingId,@PathVariable("userId") String id,@PathVariable("state") String state);



    @GetMapping("/meetingInvite/meetingInvites/{meetingId}")
    ResultVo<JSONObject>  getMeetingInviteUsers(@PathVariable("meetingId") String meetingId, @RequestParam(value = "confereeGroupId") String groupId,
                                                @RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize);


    @PostMapping("meetingInvite/list/meetingInviteAndUser/ids")
    ResultVo<List<JSONObject>>  findMeetingInviteAndUserListByIds(@RequestBody List<String> ids, @RequestParam(value = "containUser") String containUser);


}
