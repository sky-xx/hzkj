package com.hz.meetinghotel.meetinghotel.service;

import com.alibaba.fastjson.JSONObject;
import com.hz.meetinghotel.meetinghotel.entity.MeetingHotelRoom;
import com.hz.meetinghotel.meetinghotel.entity.MeetingRoomUser;
import com.hz.general.utils.ResultVo;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface MeetingRoomUserService {



    /**
     * 获取全部参会人员
     *
     * @param meetingId
     * @return
     */
    List<JSONObject> getInviteUsers(String meetingId);

    /**
     * 根据房间id和会议id 获取相对应的人数
     * @param roomId
     * @param meetingId
     * @return
     */
    List<MeetingRoomUser> getRoomUserByRoomId(String meetingId,String roomId );


    /**
     * 快速入住
     * @return
     *   根据需求,直接分配,优先分配女性，不能指定酒店,按照查询结果，哪个先就先分配哪个
     */
    ResultVo batchSave(String meetingId);


    /**
     * 快速清空入住人员
     * @return
     *   根据需求,直接分配,优先分配女性，不能指定酒店,按照查询结果，哪个先就先分配哪个
     */
    ResultVo batchDelete(String meetingId);


    /**
     * 快速入住
     * @return
     *   根据需求,直接分配,优先分配女性，不能指定酒店,按照查询结果，哪个先就先分配哪个
     */
    ResultVo batchDelete(List<String> ids);

    /**
     * 获取还没有分配房间的参会人员
     * @return
     */
    ResultVo getNoRoomUser(String meetingId);


    /**
     * 保存
     * @param meetingRoomUser 保存MeetingRoomUser数据
     * @return 保存结果
     */
    String save(@NotNull MeetingRoomUser meetingRoomUser);



    /**
     *  根据id删除
     * @param id  id
     * @return 删除结果
     */
    Integer deleteById(@NotNull String id);

    /**
     *  根据id更新
     * @param meetingRoomUser
     * @return 更新结果
     */
    Integer updateById(@NotNull MeetingRoomUser meetingRoomUser);

    /**
     *  根据id查询
     * @param id
     * @return
     */
    MeetingRoomUser findById(@NotNull String id);


    /**
     *  分页查询
     * @param page 页码
     * @param size 页数
     * @return
     */
    ResultVo findByPage(String hotelId,Integer page,Integer size);



}
