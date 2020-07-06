package com.hz.huiwu.huiwu.worker.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hz.general.utils.ResultVo;
import com.hz.general.utils.enums.ResultStateEnum;
import com.hz.huiwu.feignclient.MeetingFeignClient;
import com.hz.huiwu.feignclient.UserCenterFeignClient;
import com.hz.huiwu.huiwu.worker.entity.MeetingWorker;
import com.hz.huiwu.huiwu.worker.entity.MeetingWorkerGroup;
import com.hz.huiwu.huiwu.worker.repository.MeetingWorkerGroupRepository;
import com.hz.huiwu.huiwu.worker.repository.MeetingWorkerRepository;
import com.hz.huiwu.huiwu.worker.service.MeetingWorkerService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MeetingWorkerServiceImpl implements MeetingWorkerService {

    @Resource
    MeetingWorkerRepository meetingWorkerRepository;

    @Resource
    MeetingWorkerGroupRepository meetingWorkerGroupRepository;

    @Autowired
    UserCenterFeignClient userCenterFeignClient;

    @Autowired
    MeetingFeignClient meetingFeignClient;


    @Override
        public ResultVo getUserByGroup(String meetingId,String groupId, Boolean isAdmin, Integer page, Integer pageSize) {
        Page pageObj = null;
        if (isAdmin == null){
            isAdmin = false;
        }

        if (groupId == null){
            // 1.先获取该会议的所有分组id，然后在查询 worker（会务组人员）
            List<String> idByMeetingId = meetingWorkerGroupRepository.findIdByMeetingId(meetingId);
            if (idByMeetingId.size() > 0){
                pageObj = meetingWorkerRepository.getWorkerByGroupIds(idByMeetingId, isAdmin, PageRequest.of(page - 1, pageSize));
            }else {
                return  new ResultVo(ResultStateEnum.SUE.getCode(),"");
            }
        }else {
            pageObj = meetingWorkerRepository.findByWorkGroupIdAndIsAdmin(groupId, isAdmin, PageRequest.of(page - 1, pageSize));
        }
        List<MeetingWorker> workers = pageObj.getContent();
        if (workers.size() > 0){
            List<JSONObject>  returnObjs = new ArrayList<>(workers.size());
            List<MeetingWorkerGroup> groups = meetingWorkerGroupRepository.findByMeetingId(meetingId);
            Map<String,String> groupName = new HashMap();
            groups.forEach(workerGroup->{
                groupName.put(workerGroup.getId(),workerGroup.getName());
            });
            List<String> collect = workers.stream().map(MeetingWorker::getUserId).collect(Collectors.toList());
            List<JSONObject> userData = userCenterFeignClient.getUserByUserIds(collect).getData();
            Map<String,JSONObject> userObjects = new HashMap<>();
            userData.forEach(jsonObject -> {
                userObjects.put(jsonObject.getString("id"),jsonObject);
            });
            workers.forEach(meetingWorker -> {
                String workGroupId = meetingWorker.getWorkGroupId();
                JSONObject jsonObject = userObjects.get(meetingWorker.getUserId());
                if (jsonObject == null){
                    //理论上这个jsonObject不可能为null
                    jsonObject = new JSONObject();
                }
                jsonObject.put("groupName",groupName.get(workGroupId));
                jsonObject.put("workGroupId",workGroupId);
                jsonObject.put("id",meetingWorker.getId());
                jsonObject.put("userId",meetingWorker.getUserId());
                returnObjs.add(jsonObject);
            });
            Map<String,Object> map = new HashMap<>();
            map.put("data",returnObjs);
            map.put("total",pageObj.getTotalElements());
            return new ResultVo(ResultStateEnum.SUE.getCode(),"",map);
        }
        return  new ResultVo(ResultStateEnum.SUE.getCode(),"");
    }

    @Override
    public ResultVo getUserByMeetingId(String meeting) {
        // 获取该会议所有人员
        // 获取所有配置人员
        // 对比
        ResultVo<JSONObject> meetingInviteUsers = meetingFeignClient.getMeetingInviteUsers(meeting, null, 1, 9999);
        List<HashMap> content = meetingInviteUsers.getData().getObject("content", List.class);
        if (content.size() > 0){
            List<String> userIdByMeetingId = meetingWorkerRepository.getUserIdByMeetingId(meeting);
            List<JSONObject> users = new ArrayList<>();
            content.forEach(map ->{
                String  userId = (String) map.get("userId");
                if (!userIdByMeetingId.contains(userId)){
                    Map userData = (Map)map.get("userData");
                    if (userData != null){
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("userId",userId);
                        try {
                            jsonObject.put("sex",userData.get("sex"));
                        } catch (Exception e) {
//                        e.printStackTrace();
                        }
                        try {
                            jsonObject.put("userName",userData.get("userName"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        users.add(jsonObject);
                    }
                }
            });
            return new ResultVo(ResultStateEnum.SUE.getCode(),"",users);
        }
        return new ResultVo(ResultStateEnum.SUE.getCode(),"");
    }

    @Override
    public String save(@NotNull List<String> userIds, String groupId) {
        List<MeetingWorker> workers = new ArrayList<>(userIds.size());
        userIds.forEach(id ->{
            MeetingWorker worker =new MeetingWorker();
            worker.setCreateDate(new Date());
            worker.setIsAdmin(false);
            worker.setUserId(id);
            worker.setWorkGroupId(groupId);
            workers.add(worker);
        });
        meetingWorkerRepository.saveAll(workers);
        return null;
    }


    @Override
    public Integer deleteById(@NotNull String id) {
        try {
            meetingWorkerRepository.deleteById(id);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    @Override
    public Integer updateById(@NotNull MeetingWorker meetingWorker) {
        try {
            meetingWorkerRepository.saveAndFlush(meetingWorker);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
     }

    @Override
    public MeetingWorker findById(@NotNull String id){
           Optional<MeetingWorker> meetingWorker = meetingWorkerRepository.findById(id);
           return meetingWorker.get();
     }

    @Override
    public ResultVo findByPage(Integer page,Integer size){
        try {
            Page<MeetingWorker> meetingWorkers = meetingWorkerRepository.findAll(PageRequest.of(page-1, size));
            return new ResultVo<>("000","查询成功",meetingWorkers);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultVo<>("001","查询失败",null);
        }
    }


}
