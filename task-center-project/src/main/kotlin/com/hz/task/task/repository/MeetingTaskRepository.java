package com.hz.task.task.repository;

import com.hz.task.task.entity.MeetingTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MeetingTaskRepository extends JpaRepository<MeetingTask, String> {

//    @Query(value = "select task from MeetingTask task where leaderId in (?2) and taskStatus = ?1")
    Page<MeetingTask> findByTaskStatusAndAccepterIdIn(String taskStatus, List<String> accepterIds, Pageable pageable);

    //    @Query(value="select task from MeetingTask task where leaderId in (?2) and taskStatus in (?1)")
    Page<MeetingTask> findByTaskStatusInAndAccepterIdIn(List<String> taskStatus, List<String> accepterIds, Pageable pageable);

    Page<MeetingTask> findByAccepterId(String userId, Pageable pageable);

    Page<MeetingTask> findByCreatorId(String userId, Pageable pageable);

}
