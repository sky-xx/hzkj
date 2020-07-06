package com.hz.huiwu.huiwu.worker.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;



@Entity
@Table(name = "meeting_worker")
@Data
public class MeetingWorker {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(columnDefinition = "varchar(50) comment '唯一id'")
    private String id;



    @Column(columnDefinition = "varchar(50) comment '会务组id'")
    private String workGroupId;

    @Column(columnDefinition = "varchar(50) comment '用户id'")
    private String userId;

    @Column( columnDefinition = "bit(1) default 1   comment '是否管理员'")
    private Boolean isAdmin;

    @Column(columnDefinition = "timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'", updatable = false)
    private Date createDate;
}
