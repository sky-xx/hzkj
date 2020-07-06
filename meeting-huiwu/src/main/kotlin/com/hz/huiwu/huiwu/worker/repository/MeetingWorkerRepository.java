package com.hz.huiwu.huiwu.worker.repository;

import com.hz.huiwu.huiwu.worker.entity.MeetingWorker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MeetingWorkerRepository extends JpaRepository<MeetingWorker, String> {




    List<MeetingWorker> findByWorkGroupId(String groupId, Pageable pageable);

//    List<MeetingWorker> findByWorkGroupIdAndIsAdmin(String groupId, Boolean isAdmin,Pageable pageable);

    Page findByWorkGroupIdAndIsAdmin(String groupId, Boolean isAdmin,Pageable pageable);

    @Query("select w from MeetingWorker w where w.workGroupId in (?1) and w.isAdmin = ?2 ")
    Page getWorkerByGroupIds(List<String> groupIds, Boolean isAdmin, Pageable pageable);



    @Query(value = "select user_id from meeting_worker where work_group_id  in (select id from meeting_worker_group  where meeting_id = ?1)",nativeQuery = true)
    List<String > getUserIdByMeetingId(String id);

}
