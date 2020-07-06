package com.hz.task.task.service;

import com.hz.task.task.entity.MeetingTask;
import com.hz.general.utils.ResultVo;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MeetingTaskService {

    /**
     * 保存/更新
     * @param meetingTask 保存MeetingTask数据
     * @return 保存结果
     */
    void save(@NotNull MeetingTask meetingTask);

    Page findByTaskStatusAndLeaderIds(String taskStatus,Pageable pageable);


    /**
     * 根据用户id 获取该用户创建得任务
     * @param pageable
     * @return
     */
    Page  findByCreator(String userId,Pageable pageable);
    /**
     *  根据id删除
     * @param id  id
     * @return 删除结果
     */
    void deleteById(@NotNull String id);

    /**
     *  根据id查询
     * @param id
     * @return
     */
    MeetingTask findById(@NotNull String id);

    /**
     * 审核/接受任务
     */
//    ResultVo  acceptTask(@NotNull MeetingTask meetingTask,Integer check,Integer code);

    ResultVo isAccept(@NotNull MeetingTask meetingTask,String status);

    ResultVo isExecute(@NotNull MeetingTask meetingTask);

    ResultVo isSubmit(@NotNull MeetingTask meetingTask);

    ResultVo isCheck(@NotNull MeetingTask meetingTask,String check);

    ResultVo isCancel(@NotNull MeetingTask meetingTask);




    /**
     *  我发布的任务
     * @param userId
     * @param page
     * @param size
     * @return
     */
    ResultVo findByLeader(String userId,Integer page,Integer size);

}
