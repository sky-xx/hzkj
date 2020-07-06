package com.hz.meetinghotel.meetinghotel.controller;

import com.hz.config.PassToken
import com.hz.general.utils.ResultVo
import com.hz.general.utils.enums.ResultStateEnum
import com.hz.meetinghotel.meetinghotel.entity.MeetingRoomUser
import com.hz.meetinghotel.meetinghotel.repository.MeetingRoomUserRepository
import com.hz.meetinghotel.meetinghotel.service.MeetingRoomUserService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("meetingroomuser")
@Api("安排入住")
class MeetingRoomUserController {

    @Autowired
    private val meetingRoomUserService: MeetingRoomUserService? = null


    @ApiOperation("根据会议id和房间id获取当前房间入住的用户信息")
    @PassToken
    @GetMapping("/getRoomUser/{meetingId}/{roomId}")
    fun getRoomUser(@PathVariable("meetingId") meetingId: String,@PathVariable("roomId") roomId: String): ResultVo<Any?>?{
        return ResultVo(ResultStateEnum.SUE.code, "查询入住人员成功",meetingRoomUserService!!.getRoomUserByRoomId(meetingId,roomId));
    }


    /**
     * 一键安排入住
     */
    @ApiOperation("一键安排入住")
    @GetMapping("/auto/{meetingId}")
    fun auto(@PathVariable("meetingId") meetingId: String): ResultVo<Any?>?{
        meetingRoomUserService!!.batchSave(meetingId)
        return ResultVo(ResultStateEnum.SUE.code, "保存成功");
    }


    @ApiOperation("获取尚未安排入住的人员")
    @GetMapping("/getNoRoomUser/{meetingId}")
    fun getNoRoomUser(@PathVariable("meetingId") meetingId: String): ResultVo<Any?>?{
        return meetingRoomUserService!!.getNoRoomUser(meetingId)
    }

    @ApiOperation("获取全部参会人员")
    @GetMapping("/getAllRoomUser/{meetingId}")
    fun getAllRoomUser(@PathVariable("meetingId") meetingId: String): ResultVo<Any?>?{
        return ResultVo(ResultStateEnum.SUE.code, "查询成功",meetingRoomUserService!!.getInviteUsers(meetingId))
    }


    /**
    * 新增
    * @param meetingRoomUser
    * @return 结果
    */
   @ApiOperation("安排入住")
   @PostMapping("/save")
    fun saveMeetingRoomUser(@RequestBody meetingRoomUser: MeetingRoomUser): ResultVo<Any?>? {
        val meetingRoomUser = meetingRoomUserService!!.save(meetingRoomUser)
        return if (meetingRoomUser != "") {
            ResultVo(ResultStateEnum.SUE.code, "保存成功")
        } else {
            ResultVo(ResultStateEnum.ERR.code, "保存失败")
        }
    }

    /**
     * 根据id删除
     * @param id 需要删除的id
     * @return 结果
    */
    @ApiOperation("根据id清除入住信息")
    @DeleteMapping("/deleteRoomUser/{id}")
    fun deleteMeetingRoomUser(@PathVariable("id") id: String, request: HttpServletRequest): ResultVo<Any?>? {
        val result = meetingRoomUserService!!.deleteById(id)
        return if (result == 0) {
            ResultVo(ResultStateEnum.SUE.code, "删除成功")
        } else {
            ResultVo(ResultStateEnum.ERR.code, "删除失败")
        }
    }

    @ApiOperation("一键清除入住信息")
    @DeleteMapping("/delete/{meetingId}")
    fun deleteBatch(@PathVariable("meetingId") meetingId: String, request: HttpServletRequest): ResultVo<Any?>? {
        return meetingRoomUserService!!.batchDelete(meetingId);
    }


    /**
     * 根据id删除
     * @param id 需要删除的id
     * @return 结果
     */
    @ApiOperation("批量删除入住信息")
    @PostMapping("/delete")
    fun deleteByIds(@RequestBody ids: List<String>): ResultVo<Any?>? {
       return meetingRoomUserService!!.batchDelete(ids)
    }


    /**
    * 根据id修改数据
    * @param meetingRoomUser
    * @return 结果
    */
    @PutMapping("/update")
    fun updateMeetingRoomUser(@RequestBody meetingRoomUser: MeetingRoomUser): ResultVo<Any?>? {
        val result = meetingRoomUserService!!.updateById(meetingRoomUser)
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
    fun getMeetingRoomUserById(@PathVariable("id") id: String): ResultVo<Any?>? {
        var result = meetingRoomUserService!!.findById(id)
        return  ResultVo(ResultStateEnum.SUE.code, "",result)
    }

    /**
    * 查询分页信息
    * @param id  id
    * @return 结果
    */
    @PassToken
    @GetMapping("list/{hotelId}/{page}/{pageSize}")
    fun getMeetingRoomUserList(@PathVariable("hotelId") hotelId: String?,@PathVariable("page") page: Int?, @PathVariable("pageSize")pageSize: Int?): ResultVo<Any?>? {
        return meetingRoomUserService!!.findByPage(hotelId,page,pageSize);
    }

}