package com.hz.huiwu.huiwu.worker.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "meeting_worker_group")
public class MeetingWorkerGroup {


    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(columnDefinition = "varchar(50) comment '唯一id'")
    private String id;

    @Column(columnDefinition = "varchar(50) comment '会议id'")
    private String meetingId;

    @Column(columnDefinition = "varchar(50) comment '父类id(这个表的id)'")
    private String parentId;

    @Column(columnDefinition = "varchar(150) comment '会务组名称'")
    private String name;

    @Column(columnDefinition = "varchar(50) comment '创建人id'", updatable = false)
    private String createUser;

    @Column(columnDefinition = "timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'", updatable = false)
    private Date createDate;

    @Transient
    private List<MeetingWorkerGroup> children;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public List<MeetingWorkerGroup> getChildren() {
        return children;
    }

    public void setChildren(List<MeetingWorkerGroup> children) {
        this.children = children;
    }
}
