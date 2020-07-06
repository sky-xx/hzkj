package com.hz.meetinghotel.meetinghotel.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hz.config.utils.RedisUtils;
import com.hz.general.utils.ResultVo;
import com.hz.general.utils.enums.ResultStateEnum;
import com.hz.meetinghotel.feignclient.MeetingFeignClient;
import com.hz.meetinghotel.feignclient.UserCenterFeignClient;
import com.hz.meetinghotel.meetinghotel.entity.MeetingHotel;
import com.hz.meetinghotel.meetinghotel.entity.MeetingHotelRoom;
import com.hz.meetinghotel.meetinghotel.entity.MeetingRoomUser;
import com.hz.meetinghotel.meetinghotel.entity.RoomUser;
import com.hz.meetinghotel.meetinghotel.repository.MeetingHotelRepository;
import com.hz.meetinghotel.meetinghotel.repository.MeetingHotelRoomRepository;
import com.hz.meetinghotel.meetinghotel.repository.MeetingRoomUserRepository;
import com.hz.meetinghotel.meetinghotel.service.MeetingHotelRoomService;
import com.hz.meetinghotel.meetinghotel.service.MeetingRoomUserService;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.beans.Transient;
import java.util.*;

@Service
public class MeetingRoomUserServiceImpl implements MeetingRoomUserService {

    @Resource
    MeetingRoomUserRepository meetingRoomUserRepository;

    @Resource
    MeetingHotelRoomRepository meetingRoomRepository;

    @Resource
    MeetingHotelRoomRepository meetingHotelRoomRepository ;

    @Autowired
    MeetingFeignClient meetingFeignClient;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    UserCenterFeignClient userCenterFeignClient;


    @Override
    public List<MeetingRoomUser> getRoomUserByRoomId(String meetingId, String roomId) {
        List<MeetingRoomUser> byMeetingIdAndRoomIds = meetingRoomUserRepository.findByMeetingIdAndRoomId(meetingId, roomId);
        if (!byMeetingIdAndRoomIds.isEmpty()){
            for (MeetingRoomUser byMeetingIdAndRoomId : byMeetingIdAndRoomIds) {
                String userId = byMeetingIdAndRoomId.getParticipantId();
                if (!StringUtils.isEmpty(userId)){
                    List<JSONObject>  users = meetingFeignClient.findMeetingInviteAndUserListByIds(Arrays.asList(String.join(",", userId).split(",")),"1").getData();
                    if (users!= null  && users.size() > 0){
                        byMeetingIdAndRoomId.setUserObject(users);
                    }
                }
            }
            return byMeetingIdAndRoomIds;
        }
        return null;
    }

    @Override
    public ResultVo batchSave(String meetingId) {
        /**
         * 1.获取尚未分配的参会人员
         * 2.女性优先
         */
        ResultVo noRoomUser = this.getNoRoomUser(meetingId);
        List<JSONObject> data = (List<JSONObject>) noRoomUser.getData();
        if (data.size() > 0) {
            // 2.1  分出男性 女性
            List<String> ladyIds = new ArrayList<>();
            List<String> manIds = new ArrayList<>();
            data.forEach(jsonObject -> {
                if (jsonObject.getString("sex").equals("女")) {
                    ladyIds.add(jsonObject.getString("id"));
                } else {
                    manIds.add(jsonObject.getString("id"));
                }
            });
            // 2.2   找出尚未分配的房间
            List<MeetingHotelRoom> noUserRoom = meetingRoomRepository.getNoUserRoom(meetingId);
            // 2.3  分配房间
            if (noUserRoom.size() > 0) {
                List<MeetingRoomUser> meetingRoomUsers = new ArrayList<>();
                for (int i = 0; i < noUserRoom.size(); i++) {
                    MeetingHotelRoom room = noUserRoom.get(i);
                    MeetingRoomUser roomUser = new MeetingRoomUser();
                    roomUser.setCreateTime(new Date());
                    roomUser.setRoomId(room.getId());
                    roomUser.setMeetingId(meetingId);
                    List<String> list = null;
                    if (ladyIds.size() > 0) {
                        try {
                            list = ladyIds.subList(0, room.getCapacity());
                        } catch (Exception e) {
                            list = ladyIds.subList(0, ladyIds.size());
                        }
                        roomUser.setParticipantId(StringUtils.strip(list.toString(), "[]").replace(" ", ""));
                        ladyIds.removeAll(list);
                        meetingRoomUsers.add(roomUser);
                    } else {
                        if (manIds.size() > 0) {
                            try {
                                list = manIds.subList(0, room.getCapacity());
                            } catch (Exception e) {
                                list = manIds.subList(0, manIds.size());
                            }
                            roomUser.setParticipantId(StringUtils.strip(list.toString(),"[]").replace(" ", ""));
                            manIds.removeAll(list);
                            meetingRoomUsers.add(roomUser);
                        }
                    }
                    noUserRoom.remove(i);
                    --i;
                }
                if (meetingRoomUsers.size()>0){
                    //未解决循环体中id冲突的问题，设置一个集合来储存参会人id不同但其他信息相同的客房信息
                    List<MeetingRoomUser> roomUsers = new ArrayList<>();
                    for (MeetingRoomUser meetingRoomUser : meetingRoomUsers) {
                        String[] split = meetingRoomUser.getParticipantId().split(",");
                            //遍历参会人id，添加客房信息
                        for (String s : split) {
                            MeetingRoomUser roomUser = new MeetingRoomUser();
                            roomUser.setRoomId(meetingRoomUser.getRoomId());
                            roomUser.setMeetingId(meetingRoomUser.getMeetingId());
                            roomUser.setRemark(meetingRoomUser.getRemark());
                            roomUser.setParticipantId(s);
                            roomUser.setCreateUserId(meetingRoomUser.getCreateUserId());
                            roomUser.setCreateTime(meetingRoomUser.getCreateTime());
                            roomUsers.add(roomUser);
                        }
                    }
                    meetingRoomUserRepository.saveAll(roomUsers);
                }
            }
        }
        return new  ResultVo(ResultStateEnum.SUE.getCode(), "操作成功");
    }

    @Override
    @Transactional
    public ResultVo batchDelete(String meetingId) {
        meetingRoomUserRepository.deleteBatch(meetingId);
        return new  ResultVo(ResultStateEnum.SUE.getCode(), "操作成功");
    }

    @Override
    @Transactional
    public ResultVo batchDelete(List<String> ids) {
        for (String id : ids) {
            List<MeetingRoomUser> roomUsers = meetingRoomUserRepository.findByRoomId(id);
            meetingRoomUserRepository.deleteAll(roomUsers);
        }
        return new  ResultVo(ResultStateEnum.SUE.getCode(), "操作成功");
    }


    @Override
    public ResultVo getNoRoomUser(String meetingId) {
        // 1.根据会议id获取所有的参会人
        List<JSONObject> userList = this.getInviteUsers(meetingId);
        // 2.筛选出尚未分配客房的参会人员
        List<String> participantIds = meetingRoomUserRepository.findparticipantIdByMeetingId(meetingId);
        if (participantIds.size() > 0) {
            String join = String.join(",", participantIds);
            for (int i = 0; i < userList.size(); i++) {
                JSONObject jsonObject = userList.get(i);
                if (join.contains(jsonObject.getString("id"))) {
                    userList.remove(jsonObject);
                    --i;
                }
            }
        }
        return new ResultVo(ResultStateEnum.SUE.getCode(), "", userList);
    }



    public List<JSONObject> getInviteUsers(String meetingId) {
//        List<JSONObject> userList = (List<JSONObject>) redisUtils.lGetIndex("hotelUsers" + meetingId, 0);
//        if (userList == null) {
        List<JSONObject> userList = new ArrayList<>();
            ResultVo<JSONObject> meetingInviteUsers = meetingFeignClient.getMeetingInviteUsers(meetingId,null,1,99999);

            if (meetingInviteUsers != null){
                JSONObject data = meetingInviteUsers.getData();
                if (data != null && ResultStateEnum.SUE.getCode().equals(meetingInviteUsers.getCode())) {
                    List<Map> content = data.getObject("content", ArrayList.class);
                    if (content.size() > 0) {
                        List<JSONObject> finalUserList = userList;
                        for (Map map : content) {
                            JSONObject userObject = new JSONObject();
                            Map userData = (Map) map.get("userData");
                            //id为参会人id
                            userObject.put("id",map.get("id"));
                            userObject.put("userId", userData.get("id"));
                            userObject.put("userName", userData.get("userName"));
                            userObject.put("sex", userData.get("sex"));
                            finalUserList.add(userObject);
                        }
                    }
//                if (userList.size() > 0) {
//                    // 缓存180秒  不设置太长，怕有遗漏 再跑去添加参会人员
//                    redisUtils.lSet("hotelUsers" + meetingId, userList, 180);
//                }
                }
//        }
            }


        return userList;
    }


    @Override
    public String save(@NotNull MeetingRoomUser meetingRoomUser) {
        /**
         * 步骤:
         *  1. 传入数据
         *  2.判断房间是否重复安排(要判断这个实体类的userId是否有数据)
         *  3.判断用户是否重复(理论上不会有重复，被安排的人员不应该出现在前端配置)  --- 这里要给出一个接口,返回未安排人员的用户
         *  3.如果有前端提示是否覆盖  然后再传入
         */
        try {
            if (meetingRoomUser.getParticipantId().isEmpty()){
                return "";
            }
            String[] split = meetingRoomUser.getParticipantId().split(",");

            //根据会议id和房间id获取到对应的客房信息
            List<MeetingRoomUser> byMeetingIdAndRoomIds = meetingRoomUserRepository.findByMeetingIdAndRoomId(meetingRoomUser.getMeetingId(), meetingRoomUser.getRoomId());
            for (MeetingRoomUser roomUser : byMeetingIdAndRoomIds) {
                //根据客房信息获取到房间信息
                Optional<MeetingHotelRoom> hotelRoom = meetingHotelRoomRepository.findById(roomUser.getRoomId());
                //判断该房间是否已经住满人，住满了则不能添加，直接返回   cacity: 房间的可容纳人数
                if (hotelRoom.get().getCapacity()<split.length){
                    return "";
                }
            }
            //未解决循环体中id冲突的问题，设置一个集合来储存参会人id不同但其他信息相同的客房信息
            List<MeetingRoomUser> meetingRoomUsers = new ArrayList<>(split.length);
            //遍历参会人id，添加客房信息
            for (String s : split) {
                MeetingRoomUser roomUser = new MeetingRoomUser();
                roomUser.setRoomId(meetingRoomUser.getRoomId());
                roomUser.setMeetingId(meetingRoomUser.getMeetingId());
                roomUser.setRemark(meetingRoomUser.getRemark());
                roomUser.setParticipantId(s);
                roomUser.setCreateUserId(meetingRoomUser.getCreateUserId());
                roomUser.setCreateTime(meetingRoomUser.getCreateTime());
                meetingRoomUsers.add(roomUser);
            }
            meetingRoomUserRepository.saveAll(meetingRoomUsers);
            return meetingRoomUser.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public Integer deleteById(@NotNull String id) {
        try {
            List<MeetingRoomUser> roomUsers = meetingRoomUserRepository.findByRoomId(id);
            meetingRoomUserRepository.deleteAll(roomUsers);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    @Override
    public Integer updateById(@NotNull MeetingRoomUser meetingRoomUser) {
        try {
            meetingRoomUserRepository.saveAndFlush(meetingRoomUser);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    @Override
    public MeetingRoomUser findById(@NotNull String id) {
        Optional<MeetingRoomUser> meetingRoomUser = meetingRoomUserRepository.findById(id);
        return meetingRoomUser.get();
    }

    @Override
    public ResultVo findByPage(String hotelId, Integer page, Integer size) {
        try {
            // 这里要 room 表 + roomUser     + user  连表查询
            Page<RoomUser> byRoomIdAndDate = meetingRoomUserRepository.findByRoomIdAndDate("1", "", PageRequest.of(page - 1, size));
            // todo 查询userName
            if (byRoomIdAndDate.getContent().size() > 0) {
                byRoomIdAndDate.forEach(meetingRoomUser -> {

                });
            }
            Page<MeetingRoomUser> meetingRoomUsers = meetingRoomUserRepository.findAll(PageRequest.of(page - 1, size));
            return new ResultVo<>("000", "查询成功", meetingRoomUsers);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultVo<>("001", "查询失败", null);
        }
    }





}
