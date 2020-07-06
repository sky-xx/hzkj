package com.hz.huiwu.huiwu.worker.service;

import com.alibaba.fastjson.JSONObject;
import com.hz.huiwu.huiwu.worker.entity.MeetingWorkerGroup;
import com.hz.general.utils.ResultVo;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface MeetingWorkerGroupService {


    /**
     * 获取有配置会务组的会议
     * @return
     */
    List<JSONObject> getByMeetingByUser();


    /**
     *
     * @param ids
     */
    void  deleteByIds(List<String> ids);

    /**
     * 保存
     * @param meetingWorkerGroup 保存MeetingWorkerGroup数据
     * @return 保存结果
     */
    String save(@NotNull MeetingWorkerGroup meetingWorkerGroup);

    /**
     * 获取会务组成员
     * 获取该token下的所有成员以及获该组的所有成员
     * @param groupId
     */
    Map getGroupsUsers(String groupId, String type, String searchFlag, List<JSONObject> queryConditions, Integer pageNum, Integer pageSize );

    /**
     * 根据会议id 获取会务组
     * @param meetingId
     * @return
     */
    List<MeetingWorkerGroup>  getGroupByMeetingId(String meetingId);

    /**
     *  根据id删除
     * @param id  id
     * @return 删除结果
     */
    Integer deleteById(@NotNull String id);

    /**
     *  根据id更新
     * @param meetingWorkerGroup
     * @return 更新结果
     */
    Integer updateById(@NotNull MeetingWorkerGroup meetingWorkerGroup);

    /**
     *  根据id查询
     * @param id
     * @return
     */
    MeetingWorkerGroup findById(@NotNull String id);


    /**
     *  分页查询
     * @param page 页码
     * @param size 页数
     * @return
     */
    ResultVo findByPage(Integer page,Integer size);


}
