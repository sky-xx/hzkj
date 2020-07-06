package com.hz.meetinghotel.meetinghotel.repository;

import com.hz.meetinghotel.meetinghotel.entity.MeetingHotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MeetingHotelRepository extends JpaRepository<MeetingHotel, String> {

   Page findByCreateUserId(String userId, Pageable pageable);

}
