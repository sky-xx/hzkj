package com.hz.meetinghotel.meetinghotel.entity;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "meeting_hotel_room")
@Data
public class MeetingHotelRoom {

    public MeetingHotelRoom(){}


    public MeetingHotelRoom(MeetingHotelRoom meetingHotelRoom,String participantId,String hotelName,String roomUserId){
        this.id = meetingHotelRoom.id;
        this.hotelId = meetingHotelRoom.hotelId;
        this.meetingId = meetingHotelRoom.meetingId;
        this.floor = meetingHotelRoom.floor;
        this.roomNumber = meetingHotelRoom.roomNumber;
        this.type = meetingHotelRoom.type;
        this.capacity = meetingHotelRoom.capacity;
        this.createUserId = meetingHotelRoom.createUserId;
        this.imageUrl = meetingHotelRoom.imageUrl;
        this.remark = meetingHotelRoom.remark;
        this.createTime = meetingHotelRoom.createTime;
        this.hotelName = hotelName;
        this.participantId = participantId;
        this.roomUserId=roomUserId;
    }



    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(columnDefinition = "varchar(50) COMMENT 'id'")
    private String id;

    @Column(columnDefinition = "varchar(50) COMMENT '经度'")
    private String  hotelId;

    @Column(columnDefinition = "varchar(50) COMMENT '会议id'")
    private String  meetingId;

    @Column(columnDefinition = "int(11) NULL DEFAULT 0 COMMENT '楼层'")
    private Integer  floor;

    @Column(columnDefinition = "int(11) COMMENT '房间号'")
    private Integer  roomNumber;

    @Column(columnDefinition = "varchar(50) COMMENT '房间类型( 单人房/双人房/三人房)'")
    private Integer  type;

    @Column(columnDefinition = "int(11) NULL DEFAULT 0 COMMENT '可容纳人数'")
    private Integer  capacity;

    @Column(columnDefinition = "varchar(50) COMMENT '创建者'")
    private String createUserId;

    @Column(columnDefinition = "varchar(150) COMMENT '图片'")
    private String imageUrl;

    @Column(columnDefinition = "varchar(200) COMMENT '描述说明'")
    private String  remark;

    @Column(columnDefinition = "timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP  COMMENT '创建时间'", updatable = false)
    private Date createTime;

    @Transient
    private String hotelName;

    @Transient
    private String participantId;


    @Transient
    private String roomUserId;

    @Transient
    private List<JSONObject> userObject;





}
