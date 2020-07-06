package com.hz.huiwu.feignclient;

import com.alibaba.fastjson.JSONObject;
import com.hz.general.utils.ResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@FeignClient(name = "userCenter", path = "user-center")
public interface UserCenterFeignClient {


        @GetMapping("user/user/{id}")
        ResultVo<JSONObject> findById(@PathVariable("id") String id);

        @GetMapping("department/manageableDepartmentIds")
        ResultVo<Set<String>> findManageableDepartmentIds();

        @PostMapping("user/users")
        ResultVo<List<JSONObject>> findAllByIds(@RequestBody Map<String, Set<String>> userIdsMap);

        @PostMapping("user1/users")
        ResultVo<List<JSONObject>> users(@RequestBody Map<String, Set<String>> userIdsMap);


        //      (分页)查询User: 根据条件查询    searchFlag传0
        @PostMapping("/user/users/{type}/{searchFlag}")
        ResultVo<JSONObject> getByCondition(@PathVariable("type") String type,@PathVariable("searchFlag") String searchFlag, @RequestBody List<JSONObject> jsonObjects,@RequestParam("pageNum") Integer pageNum,@RequestParam("pageSize") Integer pageSize);


        @PostMapping("/user/users/ids")
        ResultVo<List<JSONObject>> getUserByUserIds(@RequestBody List<String> userIds);

        @PostMapping("user/users/{type}/{searchFlag}")
        ResultVo<JSONObject> findAllByQueryCondition(@PathVariable("type")  String type,
                                                           @PathVariable("searchFlag")  String searchFlag,
                                                           @RequestParam("searchKey") String searchKey,
                                                           @RequestParam("pageNum") Integer pageNum,
                                                           @RequestParam("pageSize") Integer pageSize);
}
