package com.hz.huiwu.huiwu.worker.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hz.general.utils.ResultVo;
import com.hz.huiwu.feignclient.MeetingFeignClient;
import com.hz.huiwu.feignclient.UserCenterFeignClient;
import com.hz.huiwu.huiwu.worker.entity.MeetingWorker;
import com.hz.huiwu.huiwu.worker.entity.MeetingWorkerGroup;
import com.hz.huiwu.huiwu.worker.repository.MeetingWorkerGroupRepository;
import com.hz.huiwu.huiwu.worker.repository.MeetingWorkerRepository;
import com.hz.huiwu.huiwu.worker.service.MeetingWorkerGroupService;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.sql.Driver;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MeetingWorkerGroupServiceImpl implements MeetingWorkerGroupService {

    @Resource
    MeetingWorkerGroupRepository meetingWorkerGroupRepository;

    @Resource
    MeetingWorkerRepository meetingWorkerRepository;

    @Autowired
    UserCenterFeignClient userCenterFeignClient;

    @Resource
    MeetingFeignClient meetingFeignClient;

    @Override
    public List<JSONObject> getByMeetingByUser() {
        // 根据token 查出所有的 会议
        ResultVo<JSONObject> meetings = meetingFeignClient.getMeetings(1, 9999);
        if (meetings.getCode().equals("001")){
            return  null;
        }
        List<Map> content = meetings.getData().getObject("content", List.class);
        List<JSONObject> returnList = new ArrayList<>();
        if (content.size() > 0){
            List<String> meetingIds = new ArrayList<>();
            Map<String,JSONObject> meetMap = new HashMap<>();
            content.forEach(object -> {
                JSONObject jsonObject = new JSONObject();
                String meetingId = (String) object.get("id");
                jsonObject.put("meetingId",meetingId);
                jsonObject.put("meetingName",object.get("meetingName"));
                meetingIds.add(meetingId);
                meetMap.put(meetingId,jsonObject);
            });
            // 根据会议id 查询是否有会务组
            List<MeetingWorkerGroup> groups = meetingWorkerGroupRepository.getGroupByMeetingId(meetingIds);
            if (groups.size() > 0){
                List<String> ids = new ArrayList<>();
                groups.forEach(group ->{
                    String meetingId = group.getMeetingId();
                    if (!ids.contains(meetingId)){
                        ids.add(meetingId);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("meetingId",meetingId);
                        JSONObject object = meetMap.get(meetingId);
                        jsonObject.put("meetingName",object.getString("meetingName"));
                        returnList.add(jsonObject);
                    }
                });
            }
        }
        return returnList;
    }

    @Transactional
    @Override
    public void deleteByIds(List<String> ids) {
        meetingWorkerGroupRepository.deleteByIds(ids);
    }

    @Override
    public String save(@NotNull MeetingWorkerGroup meetingWorkerGroup) {
        try {
            meetingWorkerGroupRepository.save(meetingWorkerGroup);
            return meetingWorkerGroup.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public Map getGroupsUsers(String groupId, String type, String searchFlag, List<JSONObject> queryConditions, Integer pageNum, Integer pageSize ) {
        // 1.获取group的user
        // 2.获取所有的user    循环判断user 最后返回
        // 3.封装返回
//        ResultVo<JSONObject> byCondition = userCenterFeignClient.getByCondition( type, "0",queryConditions, pageNum, pageSize);
//        JSONObject data = byCondition.getData();
//        Map<String,Object> returnData = new HashMap<>();
//        List<Map> content = data.getObject("content", List.class);
//        if (content.size()  != 0){
//            List<String> workerUserIds = meetingWorkerRepository.findByMeetingGroupId(groupId).stream().map(MeetingWorker::getUserId).collect(Collectors.toList());
//            List<JSONObject> allUser = new ArrayList<>();
//            List<JSONObject> workerUsers = new ArrayList<>();
//            content.forEach(jsonObject -> {
//                JSONObject userObject = new JSONObject();
//                String userId = (String) jsonObject.get("id");
//                userObject.put("userId",userId);
//                userObject.put("sex",jsonObject.get("sex"));
//                userObject.put("phone",jsonObject.get("phone"));
//                if (workerUserIds.contains(userId)){
//                    workerUsers.add(userObject);
//                }else {
//                    allUser.add(userObject);
//                }
//            });
//            returnData.put("allUser",allUser);
//            returnData.put("workerUsers",workerUsers);
//        }
//        return returnData;
        return null;
        // 如果返回的user size = 0 就直接返回
    }

    @Override
    public List<MeetingWorkerGroup>  getGroupByMeetingId(String meetingId) {

        List<MeetingWorkerGroup> byMeetingId = meetingWorkerGroupRepository.findByMeetingId(meetingId);
        if (byMeetingId.size() != 0) {
            List<MeetingWorkerGroup> parentGroup = new ArrayList<>();
            for (int i = 0; i < byMeetingId.size(); i++) {
                MeetingWorkerGroup group = byMeetingId.get(i);
                if (StringUtils.isEmpty(group.getParentId())) {
                    parentGroup.add(group);
                    byMeetingId.remove(group);
                    --i;
                }
            }
            if (parentGroup.size() > 0 && byMeetingId.size() > 0) {
                parentGroup.forEach(group -> {
                    group.setChildren(this.getGroupChild(group.getId(), byMeetingId));
                });
            }
            return parentGroup;
        }
        return byMeetingId;
    }

    private List<MeetingWorkerGroup> getGroupChild(String parentId, List<MeetingWorkerGroup> groups) {
        List<MeetingWorkerGroup> childList = new ArrayList<>();
        if (groups.size() == 0) {
            return null;
        }
        for (int i = 0; i < groups.size(); i++) {
            MeetingWorkerGroup group = groups.get(i);
            if (group.getParentId().equals(parentId)) {
                childList.add(group);
                groups.remove(group);
                --i;
            }
        }
        childList.forEach(child -> {
            child.setChildren( this.getGroupChild(child.getId(), groups));
        });
        return childList;
    }


    @Override
    public Integer deleteById(@NotNull String id) {
        try {
            meetingWorkerGroupRepository.deleteById(id);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    @Override
    public Integer updateById(@NotNull MeetingWorkerGroup meetingWorkerGroup) {
        try {
            meetingWorkerGroupRepository.saveAndFlush(meetingWorkerGroup);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    @Override
    public MeetingWorkerGroup findById(@NotNull String id) {
        Optional<MeetingWorkerGroup> meetingWorkerGroup = meetingWorkerGroupRepository.findById(id);
        return meetingWorkerGroup.get();
    }

    @Override
    public ResultVo findByPage(Integer page, Integer size) {
        try {
            Page<MeetingWorkerGroup> meetingWorkerGroups = meetingWorkerGroupRepository.findAll(PageRequest.of(page - 1, size));
            return new ResultVo<>("000", "查询成功", meetingWorkerGroups);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultVo<>("001", "查询失败", null);
        }
    }


}
