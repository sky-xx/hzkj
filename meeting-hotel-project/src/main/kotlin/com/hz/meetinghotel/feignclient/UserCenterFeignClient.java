package com.hz.meetinghotel.feignclient;

import com.alibaba.fastjson.JSONObject;
import com.hz.general.utils.ResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.Set;

@FeignClient(name = "userCenter",path = "user-center")
public interface UserCenterFeignClient {


        @GetMapping("user/user/{id}")
        ResultVo<JSONObject> findById(@PathVariable("id") String id);

        @GetMapping("department/manageableDepartmentIds")
        ResultVo<Set<String>> findManageableDepartmentIds();

        @PostMapping("user/users")
        ResultVo<List<JSONObject>> findAllByIds(@RequestBody Map<String, Set<String>> userIdsMap);

        @PostMapping("user1/users")
        ResultVo<List<JSONObject>> users(@RequestBody Map<String, Set<String>> userIdsMap);

        @PostMapping("/user/users/ids")
        ResultVo<List<JSONObject>> getUsers(@RequestBody List<String> ids);

}
