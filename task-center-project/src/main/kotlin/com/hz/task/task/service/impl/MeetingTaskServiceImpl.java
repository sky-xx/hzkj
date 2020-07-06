package com.hz.task.task.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hz.general.utils.ResultVo;
import com.hz.task.feignclient.MeetingFeignClient;
import com.hz.task.feignclient.UserCenterFeignClient;
import com.hz.task.task.entity.MeetingTask;
import com.hz.task.task.repository.MeetingTaskRepository;
import com.hz.task.task.service.MeetingTaskService;
import io.swagger.annotations.ApiModelProperty;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import com.hz.general.utils.enums.ResultStateEnum;


import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MeetingTaskServiceImpl implements MeetingTaskService {

    @Resource
    MeetingTaskRepository meetingTaskRepository;

    @Autowired
    MeetingFeignClient meetingFeignClient;

    @Autowired
    UserCenterFeignClient userCenterFeignClient;

    @Override
    public void save(@NotNull MeetingTask meetingTask) {
        meetingTaskRepository.save(meetingTask);
    }


    @Override
    public Page findByTaskStatusAndLeaderIds(String taskStatus,  Pageable pageable) {
        Page<MeetingTask> page = null;
        List<String> leaderIds =new ArrayList<>();
        ResultVo<JSONObject> userResultVo = userCenterFeignClient.findAllByQueryCondition("internal", "1", null, 1, 9999999);
        if (ResultStateEnum.SUE.getCode().equals(userResultVo.getCode()) && userResultVo.getData() != null){
            JSONObject userResultVoData = userResultVo.getData();
            JSONArray userlist =userResultVoData .getJSONArray("content");
            for (int i = 0; i < userlist.size(); i++) {
                JSONObject user = userlist.getJSONObject(i);
                String id = user.getString("id");
                leaderIds.add(id);
            }
        }

        System.out.println("1111111111111111111111");
        for (String leaderId : leaderIds) {
            System.out.println(leaderId);
        }

        if (taskStatus == null) {
            // 待办  0  1 5 都是我的任务页的待办状态
            String[] status = {"0","1","2"};
            page = meetingTaskRepository.findByTaskStatusInAndAccepterIdIn(Arrays.asList(status), leaderIds, pageable);
        } else {
            page = meetingTaskRepository.findByTaskStatusAndAccepterIdIn(taskStatus, leaderIds, pageable);
        }
        if (page.getTotalElements() > 0) {
            List<MeetingTask> content = page.getContent();
            Map<String, String> meetingNameMap = meetingFeignClient.getMeetingNameByIds(content.stream().map(MeetingTask::getMeetingId).collect(Collectors.toList()));
            content.forEach(meetingTask -> {
                meetingTask.setMeetingName(meetingNameMap.get(meetingTask.getMeetingId()));
            });
        }
        return page;
    }

    @Override
    public Page findByCreator(String userId, Pageable pageable) {
        Page<MeetingTask> page = meetingTaskRepository.findByCreatorId(userId, pageable);
        if (page.getTotalElements() > 0) {
            List<MeetingTask> content = page.getContent();
            Map<String, String> meetingNameMap = meetingFeignClient.getMeetingNameByIds(content.stream().map(MeetingTask::getMeetingId).collect(Collectors.toList()));
            content.forEach(meetingTask -> {
                meetingTask.setMeetingName(meetingNameMap.get(meetingTask.getMeetingId()));
            });
        }
        return page;
    }

    @Override
    public void deleteById(@NotNull String id) {
        meetingTaskRepository.deleteById(id);
    }

    @Override
    public MeetingTask findById(@NotNull String id) {
        Optional<MeetingTask> meetingTask = meetingTaskRepository.findById(id);
        return meetingTask.get();
    }

    @Override
    public ResultVo isAccept(@NotNull MeetingTask meetingTask,String status) {
        if (status.equals("0")){
            meetingTask.setTaskStatus("5");
        }
        if (status.equals("1")){
            meetingTask.setTaskStatus("1");
        }
        meetingTaskRepository.save(meetingTask);
        return new ResultVo(ResultStateEnum.SUE.getCode(),"成功");
    }

    @Override
    public ResultVo isExecute(@NotNull MeetingTask meetingTask) {
        meetingTask.setTaskStatus("2");
        meetingTaskRepository.save(meetingTask);

        return new ResultVo(ResultStateEnum.SUE.getCode(),"成功");
    }

    @Override
    public ResultVo isSubmit(@NotNull MeetingTask meetingTask) {
        meetingTask.setTaskStatus("3");
        meetingTaskRepository.save(meetingTask);
        return new ResultVo(ResultStateEnum.SUE.getCode(),"成功");
    }

    @Override
    public ResultVo isCheck(@NotNull MeetingTask meetingTask,String check) {
        if (check.equals("0")){
            meetingTask.setTaskStatus("2");
        }
        if (check.equals("1")){
            meetingTask.setTaskStatus("4");
        }
        meetingTaskRepository.save(meetingTask);
        return new ResultVo(ResultStateEnum.SUE.getCode(),"成功");
    }

    @Override
    public ResultVo isCancel(@NotNull MeetingTask meetingTask) {
        meetingTask.setAbolishCode("1");
        meetingTaskRepository.save(meetingTask);
        return new ResultVo(ResultStateEnum.SUE.getCode(),"成功");
    }


//    /**
//     * 接受任务：
//     * 审核任务：
//     * @param meetingTask
//     * @param check
//     * @param code
//     * @return
//     */
//    @Override
//    public ResultVo acceptTask(@NotNull MeetingTask meetingTask,Integer check,Integer code) {
//        //check=3 表示进入到判断任务是否接受的逻辑
//        if (check==3&&code!=3){
//            //判断任务状态是否是未开始的，进而确定是否进行接受的逻辑，TaskStatus = 0表示任务未开始
//            if(meetingTask.getTaskStatus()==0){
//                //若code=1表示任务没被接受，则任务状态变为待审核
//                if (code==1){
//                    meetingTask.setTaskCode(1);
//                    meetingTask.setTaskStatus(2);
//                }
//                //若code=0表示任务没被接受，则任务状态变为待审核
//                if (code==0){
//                    meetingTask.setTaskCode(0);
//                    meetingTask.setTaskStatus(2);
//                }
//            }
//        }
//        //code=3 表示进入到判断任务是否审核的逻辑
//        if (code ==3&&check!=3){
//            //判断任务状态是否是未开始的，进而确定是否进行审核的逻辑，TaskStatus = 2表示任务待审核
//            if (meetingTask.getTaskStatus()==2){
//                //若check=1表示任务审核通过
//                if (check==1){
//                    //若TaskCode=1表示任务已被接受并且任务审核通过，则任务状态变为已完成
//                    if (meetingTask.getTaskCode()==1){
//                        meetingTask.setTaskCheck(1);
//                        meetingTask.setTaskStatus(3);
//                    }
//                    //若TaskCode=0表示任务未被接受并且任务审核通过，则任务状态变为已取消
//                    if (meetingTask.getTaskCode()==0){
//                        meetingTask.setTaskCheck(1);
//                        meetingTask.setTaskStatus(4);
//                    }
//                }
//                //若check=0表示任务审核未通过
//                if (check==0){
//                    //若TaskCode=1表示任务已被接受并且任务审核未通过，则任务状态变为进行中
//                    if (meetingTask.getTaskCode()==1){
//                        meetingTask.setTaskCheck(0);
//                        meetingTask.setTaskStatus(1);
//                    }
//                    //若TaskCode=0表示任务未被接受并且任务审核未通过，则任务状态变为进行中
//                    if (meetingTask.getTaskCode()==0){
//                        meetingTask.setTaskCheck(0);
//                        meetingTask.setTaskStatus(1);
//                    }
//                }
//            }
//        }
//        meetingTaskRepository.save(meetingTask);
//        return null;
//    }


    @Override
    public ResultVo findByLeader(String userId, Integer page, Integer size) {
        try {
            Page<MeetingTask> meetingTasks = meetingTaskRepository.findByAccepterId(userId, PageRequest.of(page - 1, size));
            if (meetingTasks.getTotalElements() > 0) {
                List<MeetingTask> content = meetingTasks.getContent();
                Map<String, String> meetingNameMap = meetingFeignClient.getMeetingNameByIds(content.stream().map(MeetingTask::getMeetingId).collect(Collectors.toList()));
                content.forEach(meetingTask -> {
                    meetingTask.setMeetingName(meetingNameMap.get(meetingTask.getMeetingId()));
                });
            }
            return new ResultVo(ResultStateEnum.SUE.getCode(), "查询成功", meetingTasks);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultVo(ResultStateEnum.ERR.getCode(), "查询失败", null);
        }
    }




}
