package com.hz.meetinghotel.meetinghotel.repository;

import com.hz.meetinghotel.meetinghotel.entity.MeetingHotelRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface MeetingHotelRoomRepository extends JpaRepository<MeetingHotelRoom, String> {

    @Query(value = "select mr from MeetingHotelRoom mr where  mr.meetingId = ?1 and  mr.id not in( select roomId from MeetingRoomUser where meetingId  = ?1  ) ")
    List<MeetingHotelRoom> getNoUserRoom(String meetingId);


    @Modifying
    @Query("delete from MeetingHotelRoom s where s.id in (?1)")
    void  deleteByIds(List<String> ids);

    //todo 为什么这里默认赋值会失败 要使用构造函数的方式
    @Query(value = "select new com.hz.meetinghotel.meetinghotel.entity.MeetingHotelRoom( mr, mru.participantId ,hotel.name,mru.id) " +
            "from MeetingHotelRoom mr left join MeetingRoomUser mru on  mr.id = mru.roomId" +
            " left join   MeetingHotel hotel  on mr.hotelId = hotel.id " +
            " where mr.meetingId  =  ?1 order by mr.roomNumber ")
    Page<MeetingHotelRoom> findByPage(String meetingId, Pageable pageable);



}
