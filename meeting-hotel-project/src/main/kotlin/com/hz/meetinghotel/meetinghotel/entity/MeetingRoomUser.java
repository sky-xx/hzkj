package com.hz.meetinghotel.meetinghotel.entity;


import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "meeting_hotel_room_user")
@Data
@ToString
public class MeetingRoomUser {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(columnDefinition = "varchar(50) COMMENT 'id'")
    private String id;

    @Column(columnDefinition = "varchar(50) COMMENT '房间id'")
    private String  roomId;


    @Column(columnDefinition = "varchar(50) COMMENT '会议id'")
    private String  meetingId;

    @Column(columnDefinition = "varchar(200) COMMENT '描述说明'")
    private String  remark;


//    @JsonFormat(pattern = "yyyy-MM-dd")
//    @Temporal(TemporalType.DATE )
//    @Column(columnDefinition = "timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP  COMMENT '使用日期(入住时间)'", updatable = false)
//    private Date useDate;

    @Column(columnDefinition = "varchar(200) COMMENT '入住者id'")
    private String participantId;
    @Column(columnDefinition = "varchar(50) COMMENT '创建者'")
    private String createUserId;

    @Column(columnDefinition = "timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP  COMMENT '创建时间'", updatable = false)
    private Date createTime;

    @Transient
    private List<JSONObject> userObject;

    public MeetingRoomUser() {
    }

    public MeetingRoomUser(String id,String roomId, String meetingId, String remark, String participantId, String createUserId, Date createTime) {
        this.id=id;
        this.roomId = roomId;
        this.meetingId = meetingId;
        this.remark = remark;
        this.participantId = participantId;
        this.createUserId = createUserId;
        this.createTime = createTime;
    }


}
