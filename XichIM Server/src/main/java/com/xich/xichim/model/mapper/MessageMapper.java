package com.xich.xichim.model.mapper;

import com.xich.xichim.model.entity.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MessageMapper {

    @Insert("INSERT INTO xim_message (username_sender, timestamp, content, id_room) VALUES (#{usernameSender}, #{timestamp}, #{content}, #{roomId})")
    @Options(useGeneratedKeys=true, keyColumn="id", keyProperty="id")
    long insertNewMessage(Message message);

    @Select("SELECT * FROM xim_message WHERE `id_room`=#{id_room} AND `timestamp` < #{end_timestamp} ORDER BY `timestamp` DESC LIMIT #{count};")
    List<Message> loadHistory(@Param("id_room") int roomId, @Param("end_timestamp") long endTimestamp, @Param("count") int count);
}
