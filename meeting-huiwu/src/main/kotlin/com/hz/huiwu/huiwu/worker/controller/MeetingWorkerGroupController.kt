package com.hz.huiwu.huiwu.worker.controller;

import com.alibaba.fastjson.JSONObject
import com.hz.config.JWTUtils
import com.hz.general.utils.ResultVo
import com.hz.general.utils.enums.ResultStateEnum
import com.hz.huiwu.feignclient.UserCenterFeignClient
import com.hz.huiwu.huiwu.worker.entity.MeetingWorkerGroup
import com.hz.huiwu.huiwu.worker.service.MeetingWorkerGroupService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("meetingworkergroup")
@Api("会务组管理")
class MeetingWorkerGroupController {

    @Autowired
    private val meetingWorkerGroupService: MeetingWorkerGroupService? = null


    @Autowired
    private val userCenterFeignClient: UserCenterFeignClient? = null

    @ApiOperation("根据会议id获取会务组")
    @GetMapping("/getHasGroupMeeting")
    fun getByMeetingByUser(httpServletRequest: HttpServletRequest): ResultVo<Any?>? {
        return ResultVo(ResultStateEnum.SUE.code, "查询成功",meetingWorkerGroupService!!.byMeetingByUser)
    }

   @ApiOperation("新增或修改会务组信息")
   @PostMapping("/save")
    fun saveMeetingWorkerGroup(@RequestBody meetingWorkerGroup: MeetingWorkerGroup,httpServletRequest: HttpServletRequest): ResultVo<Any?>? {
       meetingWorkerGroup.createDate = Date()
       meetingWorkerGroup.createUser = JWTUtils.getUserId(httpServletRequest)
       val groupId = meetingWorkerGroupService!!.save(meetingWorkerGroup)
        return if (groupId != "") {
            ResultVo(ResultStateEnum.SUE.code, "保存成功",groupId)
        } else {
            ResultVo(ResultStateEnum.ERR.code, "保存失败")
        }
    }

    @ApiOperation("获取会务组成员")
    @PostMapping("/getAllCompanyUser/{groupId}")
    fun getAllCompanyUser(@PathVariable("groupId") groupId: String,@RequestBody jsonObjects: List<JSONObject>,@RequestParam("pageNum")pageNum: Int?,@RequestParam("pageSize")pageSize: Int? ): ResultVo<Any?>? {
        val byCondition = meetingWorkerGroupService!!.getGroupsUsers(groupId,"internal", "0", jsonObjects,pageNum, pageSize)
        return ResultVo(ResultStateEnum.SUE.code, "查询成功",byCondition);
    }



    /**
     * 根据id删除
     * @param id 需要删除的id
     * @return 结果
    */
    @DeleteMapping("/delete/{id}")
    fun deleteMeetingWorkerGroup(@PathVariable("id") id: String, request: HttpServletRequest): ResultVo<Any?>? {
        val result = meetingWorkerGroupService!!.deleteById(id)
        return if (result == 0) {
            ResultVo(ResultStateEnum.SUE.code, "删除成功")
        } else {
            ResultVo(ResultStateEnum.ERR.code, "删除失败")
        }
    }

    @PostMapping("/deleteByIds")
    fun deleteByIds(@RequestBody ids :List<String>): ResultVo<Any?>? {
        meetingWorkerGroupService!!.deleteByIds(ids);
        return ResultVo(ResultStateEnum.SUE.code, "删除成功")
    }


    @ApiOperation("根据会议id获取会务组")
    @GetMapping("/getByMeetingId/{meetingId}")
    fun getByMeetingId(@PathVariable("meetingId") id: String): ResultVo<Any?>? {
        return ResultVo(ResultStateEnum.SUE.code, "查询成功",meetingWorkerGroupService!!.getGroupByMeetingId(id))
    }


}