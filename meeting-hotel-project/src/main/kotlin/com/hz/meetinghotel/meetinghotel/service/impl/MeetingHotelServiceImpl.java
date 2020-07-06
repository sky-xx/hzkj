package com.hz.meetinghotel.meetinghotel.service.impl;

import com.hz.general.utils.ResultVo;
import com.hz.meetinghotel.meetinghotel.entity.MeetingHotel;
import com.hz.meetinghotel.meetinghotel.repository.MeetingHotelRepository;
import com.hz.meetinghotel.meetinghotel.service.MeetingHotelService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;

@Service
public class MeetingHotelServiceImpl implements MeetingHotelService {

    @Resource
    MeetingHotelRepository meetingHotelRepository;

    @Override
    public String save(@NotNull MeetingHotel meetingHotel,String userId) {
        meetingHotel.setCreateUserId(userId);
        try {
            Date date = new Date();
            meetingHotel.setCreateTime(date);
            meetingHotelRepository.save(meetingHotel);
             return meetingHotel.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public Integer deleteById(@NotNull String id) {
        try {
            meetingHotelRepository.deleteById(id);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    @Override
    public Integer updateById(@NotNull MeetingHotel meetingHotel) {
        try {
            meetingHotelRepository.saveAndFlush(meetingHotel);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
     }

    @Override
    public MeetingHotel findById(@NotNull String id){
           Optional<MeetingHotel> meetingHotelOptional = meetingHotelRepository.findById(id);
        return meetingHotelOptional.orElse(null);
    }

    @Override
    public ResultVo findByPage(String userId,Integer page,Integer size){
        try {
            Page<MeetingHotel> meetingHotels = meetingHotelRepository.findByCreateUserId(userId,PageRequest.of(page-1, size));
            return new ResultVo<>("000","查询成功",meetingHotels);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultVo<>("001","查询失败",null);
        }
    }


}
