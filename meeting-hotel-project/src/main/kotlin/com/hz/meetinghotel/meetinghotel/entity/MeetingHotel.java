package com.hz.meetinghotel.meetinghotel.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "meeting_hotel")
@Data
public class MeetingHotel {


    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(columnDefinition = "varchar(50) COMMENT 'id'")
    private String id;

    @Column(columnDefinition = "varchar(100) COMMENT '酒店名称'")
    private String  name;

    @Column(columnDefinition = "varchar(150) COMMENT '详细地址'")
    private String  address;

    @Column(columnDefinition = "varchar(20) COMMENT '纬度'")
    private String  gpsLat;

    @Column(columnDefinition = "varchar(20) COMMENT '经度'")
    private String  gpsLng;

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
}
