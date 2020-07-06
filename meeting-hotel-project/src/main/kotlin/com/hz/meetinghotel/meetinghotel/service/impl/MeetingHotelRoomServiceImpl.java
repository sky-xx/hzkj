package com.hz.meetinghotel.meetinghotel.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hz.general.utils.ResultVo;
import com.hz.general.utils.enums.ResultStateEnum;
import com.hz.meetinghotel.config.PageResultVo;
import com.hz.meetinghotel.feignclient.MeetingFeignClient;
import com.hz.meetinghotel.feignclient.UserCenterFeignClient;
import com.hz.meetinghotel.meetinghotel.entity.MeetingHotel;
import com.hz.meetinghotel.meetinghotel.entity.MeetingHotelRoom;
import com.hz.meetinghotel.meetinghotel.entity.MeetingRoomUser;
import com.hz.meetinghotel.meetinghotel.repository.MeetingHotelRepository;
import com.hz.meetinghotel.meetinghotel.repository.MeetingHotelRoomRepository;
import com.hz.meetinghotel.meetinghotel.repository.MeetingRoomUserRepository;
import com.hz.meetinghotel.meetinghotel.service.MeetingHotelRoomService;
import com.hz.meetinghotel.meetinghotel.service.MeetingRoomUserService;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.support.HttpRequestHandlerServlet;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MeetingHotelRoomServiceImpl implements MeetingHotelRoomService {

    @Resource
    MeetingHotelRoomRepository meetingHotelRoomRepository;

    @Resource
    MeetingRoomUserRepository meetingRoomUserRepository;

    @Resource
    MeetingHotelRepository meetingHotelRepository;
    
    @Autowired
    MeetingFeignClient meetingFeignClient;

    HttpRequest request;

    @Override
    public ResultVo batchCreateRoom(JSONObject jsonObject) {
        Integer total = jsonObject.getInteger("total");
        //Integer roomNumber = jsonObject.getInteger("roomNumber");// 房间号
        Integer floor = jsonObject.getInteger("floor"); //楼层
        Integer capacity = jsonObject.getInteger("capacity"); // 可容纳人数
        String id = jsonObject.getString("hotelId");
        String meetingId = jsonObject.getString("meetingId");
        Integer roomNumber = floor * 100 + jsonObject.getInteger("startNumber") - 1; //房间号 起始编号
        Integer type = jsonObject.getInteger("type");
        Date date = new Date();
        List<MeetingHotelRoom> rooms = new ArrayList<>(roomNumber);

        for (int i = 0; i < total; i++) {
            MeetingHotelRoom room = new MeetingHotelRoom();
            room.setMeetingId(meetingId);
            room.setHotelId(id);
            room.setType(type);
            roomNumber++;
            room.setRoomNumber(roomNumber);
            room.setFloor(floor);
            room.setCapacity(capacity);
            room.setCreateTime(date);
            // todo createUserId
            rooms.add(room);
        }
        meetingHotelRoomRepository.saveAll(rooms);
        return new ResultVo(ResultStateEnum.SUE.getCode(),"创建成功");
    }

    @Override
    public ResultVo batchDelete(List<String> ids) {
        if (ids.size() > 0){
            for (String id : ids) {
                List<MeetingRoomUser> rooms = meetingRoomUserRepository.findByRoomId(id);
                meetingRoomUserRepository.deleteAll(rooms);
                meetingHotelRoomRepository.deleteById(id);
            }
        }
        return new ResultVo(ResultStateEnum.SUE.getCode(),"操作成功");
    }

    @Override
    public String save(@NotNull MeetingHotelRoom meetingHotelRoom) {
        try {
            Integer type = meetingHotelRoom.getType();
            // 0 是单人房
            if (type == 0){
                meetingHotelRoom.setCapacity(1);
            }else if (type == 1){
                meetingHotelRoom.setCapacity(2);
            }
            meetingHotelRoomRepository.save(meetingHotelRoom);
             return meetingHotelRoom.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public Integer deleteById(@NotNull String id) {
        try {
            List<MeetingRoomUser> rooms = meetingRoomUserRepository.findByRoomId(id);
            meetingRoomUserRepository.deleteAll(rooms);
            meetingHotelRoomRepository.deleteById(id);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    @Override
    public Integer updateById(@NotNull MeetingHotelRoom meetingHotelRoom) {
        try {
            meetingHotelRoomRepository.saveAndFlush(meetingHotelRoom);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
     }

    @Override
    public MeetingHotelRoom findById(@NotNull String id){
           Optional<MeetingHotelRoom> meetingHotelRoom = meetingHotelRoomRepository.findById(id);
           return meetingHotelRoom.get();
     }

    @Override
    public PageResultVo findByPage(String meetingId,Integer page,Integer size){
        try {
            //根据会议id分页查询所有对应的房间信息
            Page<MeetingHotelRoom> MeetingHotelRoomPage = meetingHotelRoomRepository.findByPage(meetingId, PageRequest.of(page - 1, size));

            // 获取会议客房
            List<MeetingHotelRoom> content = MeetingHotelRoomPage.getContent();

            if (content.size()>0){
                //获取住在客房的人的参会人员id
                List<String> collect = content.stream().map(MeetingHotelRoom::getParticipantId).collect(Collectors.toList());
                if (collect.size()>0){
                    //根据参会人员id，获取所有的参会人员的信息
                    List<JSONObject> users = meetingFeignClient.findMeetingInviteAndUserListByIds(collect, "1").getData();
                    //非空判断
                    if (users!= null  && users.size() > 0){
                        //去重人员id
                        Map<String,JSONObject> map = new HashMap();
                        users.forEach(user ->{
                            map.put(user.getString("id"),user);
                        });
                        //给当前房间赋值入住人员的信息
                        for (MeetingHotelRoom roomUser : content) {
                            String userId = roomUser.getParticipantId();
                            if (!StringUtils.isEmpty(userId)){
                                String[] split = userId.split(",");
                                List<JSONObject> jsonObjects = new ArrayList<>();
                                for (int g = 0; g < split.length; g++) {
                                    JSONObject jsonObject = map.get(split[g]);
                                    jsonObjects.add(jsonObject);
                                }
                                roomUser.setUserObject(jsonObjects);
                            }
                        }
                    }
                }

            }else {
                //如果当前会议下没有住宿信息，返回
                return new PageResultVo<>(ResultStateEnum.SUE.getCode(),ResultStateEnum.SUE.getMsg(),null);
            }
            /**
             * 要将除了参会人员id其他信息都相同的客房信息合并成一条客房信息，所以有了下面几十条，人菜笨方法，大体能用
             * 返回结果：一条客房信息，里面有个变量储存了多个参会人员的信息
             */
            List<MeetingHotelRoom> meetingHotelRoomss = new ArrayList<>();//最终合并的集合，其余定义的集合都是过渡用的
            ArrayList<MeetingHotelRoom> meetingHotelRooms = new ArrayList<>();
            meetingHotelRooms.addAll(MeetingHotelRoomPage.getContent());
            for (int i = 0; i < meetingHotelRooms.size(); i++) {
                MeetingHotelRoom r1 = meetingHotelRooms.get(i);
                for (int j = i+1; j < meetingHotelRooms.size(); j++) {
                    MeetingHotelRoom r2 = meetingHotelRooms.get(j);
                    if (r1.getId().equals(r2.getId())){
                        List<JSONObject> userObject = r1.getUserObject();
                        userObject.addAll(r2.getUserObject());
                        r1.setUserObject(userObject);
                        meetingHotelRoomss.add(r1);
                    }
                    break;
                }

            }
            //查询出两个集合中不同的元素，res是最终查询出的不同元素集合
            ArrayList<String> s1 = new ArrayList<>();
            ArrayList<String> s2 = new ArrayList<>();
            for (MeetingHotelRoom meetingHotelRoom : MeetingHotelRoomPage) {
                s1.add(meetingHotelRoom.getId());
            }
            for (MeetingHotelRoom hotelRoomss : meetingHotelRoomss) {
                s2.add(hotelRoomss.getId());
            }

            List<String> dif = new ArrayList<>();//交集
            List<String> res = new ArrayList<>();//不同的元素
            dif.addAll(s1);
            //先求出两个list的交集；
            dif.retainAll(s2);
            res.addAll(s1);
            res.addAll(s2);
            //用合集去掉交集，就是不同的元素；
            res.removeAll(dif);

            //根据不同的元素集合来查询出不同的哪个房间对象
            List<MeetingHotelRoom> hotelRooms = new ArrayList<>();
            for (MeetingHotelRoom meetingHotelRoom : MeetingHotelRoomPage.getContent()) {
                for (String re : res) {
                    if (meetingHotelRoom.getId().equals(re)){
                        hotelRooms.add(meetingHotelRoom);
                        break;
                    }
                }
            }
            //将不同的房间对象放到最终返回的集合中
            //为什么要找出不同的元素对象，因为合并的时候只拿到了除了参会人id其他信息都相同的元素对象
            meetingHotelRoomss.addAll(hotelRooms);
            //以下236-261行均为一个需求
            // 需求：有参会人入住的酒店信息排在前面，没有人入住的时候按照房间号来排序
            //就这样写了，能看懂就看，看不懂拉倒，你重新写吧，反正我看第二遍看不懂，就能用就行
            ArrayList<MeetingHotelRoom> roomshaveusers = new ArrayList<>();
            ArrayList<MeetingHotelRoom> roomsnohaveusers = new ArrayList<>();
            ArrayList<MeetingHotelRoom> endrooms = new ArrayList<>();
            for (MeetingHotelRoom roomss : meetingHotelRoomss) {
                if (roomss.getUserObject()!=null){
                    roomshaveusers.add(roomss);
                }else {
                    roomsnohaveusers.add(roomss);
                }
            }

            Collections.sort(roomshaveusers, new Comparator<MeetingHotelRoom>() {
                @Override
                public int compare(MeetingHotelRoom o1, MeetingHotelRoom o2) {
                    return o1.getUserObject().size()-o2.getUserObject().size();
                }
            });

            Collections.sort(roomsnohaveusers, new Comparator<MeetingHotelRoom>() {
                @Override
                public int compare(MeetingHotelRoom o1, MeetingHotelRoom o2) {
                    return o1.getRoomNumber()-o2.getRoomNumber();
                }
            });
            endrooms.addAll(roomshaveusers);
            endrooms.addAll(roomsnohaveusers);

            return new PageResultVo<>(ResultStateEnum.SUE.getCode(),"查询成功",endrooms,MeetingHotelRoomPage.getTotalElements());
        } catch (Exception e) {
            e.printStackTrace();
            return new PageResultVo<>(ResultStateEnum.ERR.getCode(),ResultStateEnum.ERR.getMsg(),null);
        }
    }


    /**
     * 根据会议id和参会人id查询对应的酒店客房信息
     * @param meetingId
     * @param participantId
     * @return
     */
    @Override
    public MeetingHotelRoom findByMeeringId(String meetingId,String participantId) {
        System.out.println(">>>>>>>>>"+participantId);

        //根据会议id和参会人id获取meetingRoomUser，并进行非空判断
        MeetingRoomUser roomUser = meetingRoomUserRepository.findByMeetingIdAndAndParticipantId(meetingId, participantId);
        if (roomUser==null){
            System.out.println("nnnnnnnnnnnnnnnnnnnnnnnnnnn");
            return null;
        }

        Optional<MeetingHotelRoom> meetingHotelRoom = meetingHotelRoomRepository.findById(roomUser.getRoomId());
        Optional<MeetingHotel> meetingHotel = meetingHotelRepository.findById(meetingHotelRoom.get().getHotelId());
        meetingHotelRoom.get().setHotelName(meetingHotel.get().getName());

        List<MeetingRoomUser> meetingRoomUsers = meetingRoomUserRepository.findByMeetingId(meetingId);
        ArrayList<String> participantIds = new ArrayList<>();
        for (MeetingRoomUser meetingRoomUser : meetingRoomUsers) {
            System.out.println("=====================================");
            System.out.println(meetingRoomUser.getParticipantId());
            if (meetingRoomUser.getRoomId().equals(roomUser.getRoomId())){
                System.out.println("ppppppppppppppppppppppppppppppppppppppp");
                System.out.println(meetingRoomUser.getParticipantId());
                participantIds.add(meetingRoomUser.getParticipantId());
            }
        }

        List<JSONObject> users = meetingFeignClient.findMeetingInviteAndUserListByIds(participantIds, "1").getData();
        if (users.size()>0){
            for (JSONObject user : users) {
                System.out.println("uuuuuuuuuuuuuuuuuuuuuuuuuuu");
                System.out.println(user);
            }
        }
        meetingHotelRoom.get().setUserObject(users);
        System.out.println("mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm");
        System.out.println(meetingHotelRoom.get());

        return meetingHotelRoom.get();
    }


}
