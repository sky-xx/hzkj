package com.hz.meetinghotel.meetinghotel.service;

import com.alibaba.fastjson.JSONObject;
import com.hz.meetinghotel.config.PageResultVo;
import com.hz.meetinghotel.meetinghotel.entity.MeetingHotelRoom;
import com.hz.general.utils.ResultVo;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface MeetingHotelRoomService {


    /**
     * 快速生成客房
     * @param jsonObject
     * @return
     */
    ResultVo batchCreateRoom(JSONObject jsonObject);

    /**
     * 批量删除客房
     * @param ids
     * @return
     */
    ResultVo batchDelete(List<String> ids);

    /**
     * 保存
     * @param meetingHotelRoom 保存MeetingHotelRoom数据
     * @return 保存结果
     */
    String save(@NotNull MeetingHotelRoom meetingHotelRoom);

    /**
     *  根据id删除
     * @param id  id
     * @return 删除结果
     */
    Integer deleteById(@NotNull String id);

    /**
     *  根据id更新
     * @param meetingHotelRoom
     * @return 更新结果
     */
    Integer updateById(@NotNull MeetingHotelRoom meetingHotelRoom);

    /**
     *  根据id查询
     * @param id
     * @return
     */
    MeetingHotelRoom findById(@NotNull String id);


    /**
     *  分页查询
     * @param page 页码
     * @param size 页数
     * @return
     */
    PageResultVo findByPage(String meetingId, Integer page, Integer size);


    /**
     * 根据会议id查找对应的MeetingRoomUser集合
     * @param meetingId
     * @return
     */
    MeetingHotelRoom findByMeeringId(String meetingId, String participantId);
}
