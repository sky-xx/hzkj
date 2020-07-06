package com.hz.huiwu.huiwu.worker.service;

import com.alibaba.fastjson.JSONObject;
import com.hz.huiwu.huiwu.worker.entity.MeetingWorker;
import com.hz.general.utils.ResultVo;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MeetingWorkerService {


    /**
     * 根据会务组id 获取会务组成员
     * @param groupId
     * @param isAdmin  是否管理员
     * @return
     */
    ResultVo getUserByGroup(String meetingId,String groupId,Boolean isAdmin,Integer page,Integer pageSize);



    /**
     * 根据会议获取尚未分配会务组的人员（配置会务组成员）
     * @param meeting
     * @return
     */
    ResultVo getUserByMeetingId(String meeting);


    /**
     *  保存
     * @param userIds
     * @param groupId
     * @return
     */
    String save(@NotNull List<String> userIds,String groupId);

    /**
     *  根据id删除
     * @param id  id
     * @return 删除结果
     */
    Integer deleteById(@NotNull String id);

    /**
     *  根据id更新
     * @param meetingWorker
     * @return 更新结果
     */
    Integer updateById(@NotNull MeetingWorker meetingWorker);

    /**
     *  根据id查询
     * @param id
     * @return
     */
    MeetingWorker findById(@NotNull String id);


    /**
     *  分页查询
     * @param page 页码
     * @param size 页数
     * @return
     */
    ResultVo findByPage(Integer page,Integer size);


}
