package com.hz.task.task.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "meeting_task")
@Data
public class MeetingTask {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(columnDefinition = "varchar(50) comment '唯一id'")
    private String id;

    @Column(columnDefinition = "varchar(200) comment '标题'")
    private String taskTitle;

    @Column(columnDefinition = "varchar(1000) comment '任务描述'")
    private String taskDescription;

    @Column(columnDefinition = "varchar(5) comment '优先级'")
    private String taskPriority;

    @Column(columnDefinition = "varchar(5) comment '状态: 0-未接受 1-(已接收)未开始, 2-进行中, 3-(已提交)待审核, 4-(审核通过)已完成, 5-拒绝接受'")
    private String taskStatus;

    @Column(columnDefinition = "int(11) comment '任务类别 1.普通任务 2.会务组任务' ")
    private Integer type;

    @Column(columnDefinition = "timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'")
    private Date modifyDate;

    @Column(columnDefinition = "varchar(50) comment '创建人id'", updatable = false)
    private String creatorId;

    @Column(columnDefinition = "timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'", updatable = false)
    private Date createDate;

    @Column(columnDefinition = "datetime NULL DEFAULT NULL COMMENT '截止时间'")
    private Date deadline;

    @Column(columnDefinition = "datetime NULL DEFAULT NULL COMMENT '完成时间'")
    private Date completionTime;

    @Column(columnDefinition = "varchar(50) comment '会务组id'")
    private String meetingWorkGroupId;

    @Column(columnDefinition = "varchar(50) comment '会议id'")
    private String meetingId;

    @Column(columnDefinition = "varchar(500) comment '备注'")
    private String taskNotes;

    @Column(columnDefinition = "varchar(50) comment '负责人ID'")
    private String accepterId;

    @Column(columnDefinition = "varchar(5) comment '取消(废除)状态 :0-未取消, 1-已取消'")
    private String abolishCode;

    @Transient
    private String meetingName;

    @Transient
    private JSONObject creator;

    @Transient
    private JSONObject leader;

}
