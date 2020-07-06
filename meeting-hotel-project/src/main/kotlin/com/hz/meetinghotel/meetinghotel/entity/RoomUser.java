package com.hz.meetinghotel.meetinghotel.entity;

import lombok.Data;

import java.util.Date;

/**
 * 页面展示的实体类
 */
@Data
public class RoomUser {

    public RoomUser(String roomId,String id,Integer roomNumber,String imageUrl,Integer capacity,Integer floor,Integer type,String participantId,String  meetingId,String remark){
        this.roomId = roomId;
        this.roomUserId = id;
        this.roomNumber = roomNumber;
        this.imageUrl = imageUrl;
        this.capacity = capacity;
        this.floor = floor;
        this.type = type;
        this.participantId = participantId;
        this.meetingId = meetingId;
        this.remark = remark;
    }

    private String roomId;
    private String roomUserId;  // roomUser表的id
    private Integer roomNumber;
    private String imageUrl;
    private Integer capacity;
    private Integer  floor;
    private Integer  type;
    private String participantId;
    private String userName;
    private String meetingId;
    private String remark;




}
