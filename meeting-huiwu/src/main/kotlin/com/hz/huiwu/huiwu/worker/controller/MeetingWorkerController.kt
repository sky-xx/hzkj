package com.hz.huiwu.huiwu.worker.controller;

import com.hz.general.utils.ResultVo
import com.hz.general.utils.enums.ResultStateEnum
import com.hz.huiwu.huiwu.worker.entity.MeetingWorker
import com.hz.huiwu.huiwu.worker.service.MeetingWorkerService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("meetingworker")
@Api("会务组人员管理")
class MeetingWorkerController {

    @Autowired
    private val meetingWorkerService: MeetingWorkerService? = null


    @ApiOperation("获取会务组的人员")
    @GetMapping("getUsers/{meetingId}/{pageSize}/{page}")
    fun getUser(@PathVariable("meetingId") meetingId: String,@PathVariable("page") page: Int?,
                @PathVariable("pageSize") pageSize: Int?,@RequestParam(required = false) groupId: String?,
                @RequestParam(required = false) isAdmin: Boolean?): ResultVo<Any?>? {

        return meetingWorkerService!!.getUserByGroup(meetingId, groupId, isAdmin, page, pageSize)
    }

    @ApiOperation("根据会议获取尚未分配会务组的人员（配置会务组成员）")
    @GetMapping("getUsers/{meetingId}")
    fun getUserByMeetingId(@PathVariable("meetingId") meetingId: String?): ResultVo<Any?>? {
        return meetingWorkerService!!.getUserByMeetingId(meetingId)
    }


    /**
    *
    * @param meetingWorker
    * @return 结果
    */
    @PostMapping("/save/{groupId}")
    @ApiOperation("添加会务组成员")
    fun saveMeetingWorker(@PathVariable("groupId") groupId: String,@RequestBody userIds: List<String> ): ResultVo<Any?>? {
        meetingWorkerService!!.save(userIds,groupId)
        return  ResultVo(ResultStateEnum.SUE.code, "保存成功")
    }

    /**
     * 根据id删除
     * @param id 需要删除的id
     * @return 结果
    */
    @DeleteMapping("/delete/{id}")
    fun deleteMeetingWorker(@PathVariable("id") id: String, request: HttpServletRequest): ResultVo<Any?>? {
        val result = meetingWorkerService!!.deleteById(id)
        return if (result == 0) {
            ResultVo(ResultStateEnum.SUE.code, "删除成功")
        } else {
            ResultVo(ResultStateEnum.ERR.code, "删除失败")
        }
    }

    /**
    * 根据id修改数据
    * @param meetingWorker
    * @return 结果
    */
    @ApiOperation("根据id修改数据")
    @PutMapping("/update")
    fun updateMeetingWorker(@RequestBody meetingWorker: MeetingWorker): ResultVo<Any?>? {
        val result = meetingWorkerService!!.updateById(meetingWorker)
        return if (result == 0) {
            ResultVo(ResultStateEnum.SUE.code, "修改成功")
        } else {
            ResultVo(ResultStateEnum.ERR.code, "修改失败")
        }
    }

    /**
    * 根据id查询数据
    * @param id  id
    * @return 结果
    */
    @GetMapping("list/{id}")
    fun getMeetingWorkerById(@PathVariable("id") id: String): ResultVo<Any?>? {
        var result = meetingWorkerService!!.findById(id)
        return  ResultVo(ResultStateEnum.SUE.code, "",result)
    }

    /**
    * 查询分页信息
    * @param id  id
    * @return 结果
    */
    @GetMapping("list/{page}/{pageSize}")
    fun getMeetingWorkerList(@PathVariable("page") page: Int?, @PathVariable("pageSize")pageSize: Int?): ResultVo<Any?>? {
        return meetingWorkerService!!.findByPage(page,pageSize);
    }

}