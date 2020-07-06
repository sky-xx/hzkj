package com.hz.meetinghotel.meetinghotel.repository;

import com.hz.meetinghotel.meetinghotel.entity.MeetingRoomUser;
import com.hz.meetinghotel.meetinghotel.entity.RoomUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;


public interface MeetingRoomUserRepository extends JpaRepository<MeetingRoomUser, String> {



//    @Query(value = "select new com.hz.meetinghotel.meetinghotel.entity.RoomUser(ru.roomId,r.floor,r.capacity) from  MeetingRoomUser ru,MeetingRoom r where ru.roomId = r.id and r.id = ?1")
//    Page<MeetingRoomUser> findByRoomId(String roomId, Date date, Pageable pageable);


    @Query(value = "select new com.hz.meetinghotel.meetinghotel.entity.RoomUser(ru.roomId,ru.id,r.roomNumber,r.imageUrl,r.capacity,r.floor,r.type,ru.participantId,ru.meetingId,ru.remark " +
            ") from  MeetingRoomUser ru,MeetingHotelRoom r where ru.roomId = r.id and r.id = ?1 and ru.meetingId = ?2")
    Page<RoomUser> findByRoomIdAndDate(String roomId, String  meetingId, Pageable pageable);


    List<MeetingRoomUser> findByMeetingIdAndRoomId (String meeting,String roomId);

    @Query(value = "select participantId from MeetingRoomUser where meetingId = ?1")
    List<String> findparticipantIdByMeetingId(String  meetingId);



    @Modifying
    @Query("delete from MeetingRoomUser s where s.id in (?1)")
    void  deleteByIds(List<String> ids);



    @Modifying
    @Query("delete from MeetingRoomUser s where s.meetingId  = ?1 ")
    void  deleteBatch(String meetingId);


    MeetingRoomUser findByMeetingIdAndAndParticipantId(String meetingId,String participantId);

    List<MeetingRoomUser> findByMeetingId(String meetingId);

    List<MeetingRoomUser> findByRoomId(String roomId);

}
