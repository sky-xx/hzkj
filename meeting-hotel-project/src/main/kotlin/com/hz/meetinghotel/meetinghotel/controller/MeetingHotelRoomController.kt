package com.hz.meetinghotel.meetinghotel.controller;


import com.alibaba.fastjson.JSONObject
import com.hz.config.JWTUtils
import com.hz.config.PassToken
import com.hz.general.utils.ResultVo
import com.hz.general.utils.enums.ResultStateEnum
import com.hz.meetinghotel.config.PageResultVo

import com.hz.meetinghotel.meetinghotel.entity.MeetingHotelRoom
import com.hz.meetinghotel.meetinghotel.service.MeetingHotelRoomService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("meetinghotelroom")
@Api("酒店客房管理")
class MeetingHotelRoomController {

    @Autowired
    private val meetingRoomService: MeetingHotelRoomService? = null


    @ApiOperation("快速生成客房")
    @PostMapping("/batchSave")
    fun batchCreateRoom(@RequestBody  jsonObject: JSONObject): ResultVo<Any?>? {
        val meetingRoom = meetingRoomService!!.batchCreateRoom(jsonObject)
        return ResultVo(ResultStateEnum.SUE.code, "操作成功",meetingRoom)
    }

    @ApiOperation("批量删除客房")
    @PostMapping("/batchDelete")
    fun batchDelete(@RequestBody  ids: List<String>): ResultVo<Any?>? {
        val meetingRoom = meetingRoomService!!.batchDelete(ids)
        return ResultVo(ResultStateEnum.SUE.code, "操作成功")
    }


   /**
    * 新增
    * @param meetingRoom
    * @return 结果
    */
   @ApiOperation("新增/更新 客房信息")
   @PostMapping("/save")
    fun saveMeetingRoom(@RequestBody meetingRoom: MeetingHotelRoom): ResultVo<Any?>? {
        val meetingRoom = meetingRoomService!!.save(meetingRoom)
        return if (meetingRoom != "") {
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
    @ApiOperation("根据id删除一条客房信息")
    @DeleteMapping("/delete/{id}")
    fun deleteMeetingRoom(@PathVariable("id") id: String, request: HttpServletRequest): ResultVo<Any?>? {
        val result = meetingRoomService!!.deleteById(id)
        return if (result == 0) {
            ResultVo(ResultStateEnum.SUE.code, "删除成功")
        } else {
            ResultVo(ResultStateEnum.ERR.code, "删除失败")
        }
    }

    /**
    * 根据id修改数据
    * @param meetingRoom
    * @return 结果
    */
    @ApiOperation("更新 客房信息")
    @PutMapping("/update")
    fun updateMeetingRoom(@RequestBody meetingRoom: MeetingHotelRoom): ResultVo<Any?>? {
        val result = meetingRoomService!!.updateById(meetingRoom)
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
    @ApiOperation("根据id查询一条客房信息")
    @GetMapping("list/{id}")
    fun getMeetingRoomById(@PathVariable("id") id: String): ResultVo<Any?>? {
        var result = meetingRoomService!!.findById(id)
        return  ResultVo(ResultStateEnum.SUE.code, "",result)
    }

    /**
    * 查询分页信息
    * @param id  id
    * @return 结果
    */
    @ApiOperation("分页查询客房信息")
    @PassToken
    @GetMapping("list/{meetingId}/{page}/{pageSize}")
    fun getMeetingRoomList(@PathVariable("meetingId") meetingId: String?,@PathVariable("page") page: Int?, @PathVariable("pageSize")pageSize: Int?): PageResultVo<Any?>? {
        return meetingRoomService!!.findByPage(meetingId,page,pageSize);
    }

    /**
     * 根据会议id和参会人id查询对应的酒店客房信息
     */
    @ApiOperation("uni_app 根据会议id和参会人id查询对应的酒店客房信息")
    @GetMapping("MeetingHotelRoom/{meetingId}/{participantId}")
    fun getMeetingHotelRoom(@PathVariable("meetingId") meetingId: String?,@PathVariable("participantId") participantId: String?): ResultVo<Any?>?{
        val meetingHotelRoom = meetingRoomService!!.findByMeeringId(meetingId, participantId)
        return if (meetingHotelRoom!=null){
            ResultVo(ResultStateEnum.SUE.code,"成功",meetingHotelRoom)
        }else{
            ResultVo(ResultStateEnum.SUE.code,"当前会议没有住宿信息")
        }
    }


}