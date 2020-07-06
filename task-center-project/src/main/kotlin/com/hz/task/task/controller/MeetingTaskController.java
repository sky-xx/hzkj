package com.hz.task.task.controller;

import com.hz.config.JWTUtils;
import com.hz.task.task.entity.MeetingTask;
import com.hz.task.task.service.MeetingTaskService;
import com.hz.general.utils.ResultVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

import com.hz.general.utils.enums.ResultStateEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Date;

@RestController
@RequestMapping("meetingtask")
@Api("任务中心")
public class MeetingTaskController {

    @Autowired
    private  MeetingTaskService meetingTaskService;

    @ApiOperation("新增/更新")
    @PostMapping("/saveOrUpdate")
    ResultVo saveMeetingTask(@RequestBody MeetingTask meetingTask,HttpServletRequest request){
        if (meetingTask.getId() == null){
            meetingTask.setTaskStatus("0");
            meetingTask.setAbolishCode("0");
            meetingTask.setCreateDate(new Date());
            meetingTask.setCreatorId(JWTUtils.Companion.getUserId(request));
        }else {
//            meetingTask.setTaskCode(1);
        }
        meetingTaskService.save(meetingTask);
        return new ResultVo(ResultStateEnum.SUE.getCode(),"保存成功",meetingTask.getId());
    }

    @ApiOperation("根据id删除实体类")
    @DeleteMapping("/delete/{id}")
    ResultVo deleteMeetingTask(@PathVariable("id") String id, HttpServletRequest request ){
        meetingTaskService.deleteById(id);
         return new ResultVo(ResultStateEnum.SUE.getCode(),"删除成功");
    }

    @ApiOperation("根据id查看实体类")
    @GetMapping("list/{id}")
    ResultVo getMeetingTaskById(@PathVariable("id") String id ){
        return new  ResultVo(ResultStateEnum.SUE.getCode(), "",meetingTaskService.findById(id));
    }

    @ApiOperation("根据状态查询")
    @ApiImplicitParam(name = "status",value = "任务状态：为1，查询审核页的代办任务；为4，查询已办任务；为空，查询我的任务页的待办任务")
    @GetMapping("getByStatus/{page}/{pageSize}")
        ResultVo getByStatus(@PathVariable("page")Integer page, @PathVariable("pageSize")Integer pageSize,@RequestParam(value = "status") String status,HttpServletRequest request){
        return  new ResultVo(ResultStateEnum.SUE.getCode(), "",meetingTaskService.findByTaskStatusAndLeaderIds(status,  PageRequest.of(page - 1, pageSize)));
    }


    @ApiOperation("查询我发布的任务")
    @GetMapping("listByCreate/{page}/{pageSize}")
    ResultVo getMeetingTaskById(@PathVariable("page")Integer page, @PathVariable("pageSize")Integer pageSize,HttpServletRequest request){
        String userId = JWTUtils.Companion.getUserId(request);
        return new  ResultVo(ResultStateEnum.SUE.getCode(), "",meetingTaskService.findByCreator(userId,PageRequest.of(page - 1, pageSize)));
    }
    @ApiOperation("是否接受任务")
    @ApiImplicitParam(name = "status",value = "任务状态：为0，拒绝接受任务；为1，接受任务了，任务状态为进行中")
    @PostMapping("/taskIsAccept/{status}")
    ResultVo taskIsAccept(@RequestBody MeetingTask meetingTask,@PathVariable(value = "status",required = true) String status){
        return meetingTaskService.isAccept(meetingTask,status);
    }
    @ApiOperation("任务接受后是否执行")
    @PostMapping("/taskIsExecute")
    ResultVo taskIsExecute(@RequestBody MeetingTask meetingTask){
        return meetingTaskService.isExecute(meetingTask);
    }
    @ApiOperation("任务执行后是否提交给管理员审核")
    @PostMapping("/taskIsSubmit")
    ResultVo taskIsSubmit(@RequestBody MeetingTask meetingTask){
        return meetingTaskService.isSubmit(meetingTask);
    }
    @ApiOperation("管理员审核任务")
    @ApiImplicitParam(name = "check",value = "审核码：为0，任务审核不通过，任务状态变为进行中；为1，任务通过，任务状态为已完成")
    @PostMapping("/taskIsCheck/{check}")
    ResultVo taskIsCheck(@RequestBody MeetingTask meetingTask,@PathVariable(value = "check",required = true) String check){
        return meetingTaskService.isCheck(meetingTask,check);
    }
    @ApiOperation("是否取消任务")
    @PostMapping("/taskIsCancel")
    ResultVo taskIsCancel(@RequestBody MeetingTask meetingTask){
        return meetingTaskService.isCancel(meetingTask);
    }



}