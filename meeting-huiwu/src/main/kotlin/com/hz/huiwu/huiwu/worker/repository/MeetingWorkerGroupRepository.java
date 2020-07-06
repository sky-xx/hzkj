package com.hz.huiwu.huiwu.worker.repository;

import com.hz.huiwu.huiwu.worker.entity.MeetingWorkerGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;


public interface MeetingWorkerGroupRepository extends JpaRepository<MeetingWorkerGroup, String> {



    List<MeetingWorkerGroup>  findByMeetingId(String meetingId);


    @Query(value="select id from MeetingWorkerGroup g where g.meetingId = ?1")
    List<String> findIdByMeetingId(String meetingId);



    @Query("select g from MeetingWorkerGroup g where g.meetingId in(?1)")
    List<MeetingWorkerGroup>  getGroupByMeetingId(java.util.List<String> ids);


    @Modifying
    @Query("delete from MeetingWorkerGroup s where s.id in (?1)")
    void  deleteByIds(List<String> ids);


}
