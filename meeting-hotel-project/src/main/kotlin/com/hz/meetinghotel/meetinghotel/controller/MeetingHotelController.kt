package com.hz.meetinghotel.meetinghotel.controller;

import com.hz.config.JWTUtils
import com.hz.config.PassToken
import com.hz.meetinghotel.meetinghotel.entity.MeetingHotel;
import com.hz.meetinghotel.meetinghotel.service.MeetingHotelService;
import com.hz.general.utils.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import com.hz.general.utils.enums.ResultStateEnum;
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.web.servlet.DispatcherServlet

@RestController
@RequestMapping("meetinghotel")
@Api("酒店管理")
class MeetingHotelController {

    @Autowired
    private val meetingHotelService: MeetingHotelService? = null


   /**
    * 新增
    * @param meetingHotel
    * @return 结果
    */
   @ApiOperation("新增/更新 酒店信息")
   @PostMapping("/save")
    fun saveMeetingHotel(@RequestBody meetingHotel: MeetingHotel,request: HttpServletRequest): ResultVo<Any?>? {
       val userId = JWTUtils.getUserId(request)
       val meetingHotel = meetingHotelService!!.save(meetingHotel,userId)
        return if (meetingHotel != "") {
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
    @ApiOperation("根据id删除酒店信息")
    @DeleteMapping("/delete/{id}")
    fun deleteMeetingHotel(@PathVariable("id") id: String, request: HttpServletRequest): ResultVo<Any?>? {
        val result = meetingHotelService!!.deleteById(id)
        return if (result == 0) {
            ResultVo(ResultStateEnum.SUE.code, "删除成功")
        } else {
            ResultVo(ResultStateEnum.ERR.code, "删除失败")
        }
    }

    /**
    * 根据id修改数据
    * @param meetingHotel
    * @return 结果
    */
    @ApiOperation("更新 酒店信息")
    @PutMapping("/update")
    fun updateMeetingHotel(@RequestBody meetingHotel: MeetingHotel): ResultVo<Any?>? {
        val result = meetingHotelService!!.updateById(meetingHotel)
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
    @ApiOperation("根据id查询酒店信息")
    @GetMapping("list/{id}")
    fun getMeetingHotelById(@PathVariable("id") id: String): ResultVo<Any?>? {
        var result = meetingHotelService!!.findById(id)
        return  ResultVo(ResultStateEnum.SUE.code, "",result)
    }



    /**
    * 查询分页信息
    * @param id  id
    * @return 结果
    */
    @ApiOperation("查询分页信息")
    @GetMapping("list/{page}/{pageSize}")
    fun getMeetingHotelList(@PathVariable("page") page: Int?, @PathVariable("pageSize")pageSize: Int?,request: HttpServletRequest): ResultVo<Any?>? {
        return meetingHotelService!!.findByPage(JWTUtils.getUserId(request),page,pageSize);
    }

}