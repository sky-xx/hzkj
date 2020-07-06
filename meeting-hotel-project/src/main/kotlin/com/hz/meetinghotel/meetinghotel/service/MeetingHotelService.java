package com.hz.meetinghotel.meetinghotel.service;

import com.alibaba.fastjson.JSONObject;
import com.hz.meetinghotel.meetinghotel.entity.MeetingHotel;
import com.hz.general.utils.ResultVo;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface MeetingHotelService {



    /**
     * 保存
     * @param meetingHotel 保存MeetingHotel数据
     * @return 保存结果
     */
    String save(@NotNull MeetingHotel meetingHotel,String userId);

    /**
     *  根据id删除
     * @param id  id
     * @return 删除结果
     */
    Integer deleteById(@NotNull String id);

    /**
     *  根据id更新
     * @param meetingHotel
     * @return 更新结果
     */
    Integer updateById(@NotNull MeetingHotel meetingHotel);

    /**
     *  根据id查询
     * @param id
     * @return
     */
    MeetingHotel findById(@NotNull String id);


    /**
     *  分页查询
     * @param page 页码
     * @param size 页数
     * @return
     */
    ResultVo findByPage(String userId,Integer page,Integer size);


}
