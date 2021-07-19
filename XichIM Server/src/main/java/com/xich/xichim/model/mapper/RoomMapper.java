package com.xich.xichim.model.mapper;

import com.xich.xichim.model.entity.Room;
import com.xich.xichim.model.entity.RoomMember;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoomMapper {
    @Select("SELECT id_room FROM xim_room_member WHERE username_user=#{username}")
    List<Integer> getRoomIdList(@Param("username") String username);

    @Select("SELECT * FROM xim_room_member JOIN xim_room ON xim_room.id = xim_room_member.id_room WHERE username_user=#{username}")
    List<Room> getRoomFullList(@Param("username") String username);

    @Insert("INSERT INTO xim_room (name) VALUES (#{name})")
    @Options(useGeneratedKeys=true, keyColumn = "id", keyProperty = "id")
    int createRoom(Room room);

    @Insert("INSERT INTO xim_room_member (username_user, id_room) VALUES (#{username_user}, #{id_room})")
    void joinRoom(@Param("username_user") String username, @Param("id_room") int roomId);

    @Update("UPDATE xim_room_member SET timestamp = #{timestamp} WHERE id_room=#{roomId} AND username_user=#{username}")
    void markReadRoom(@Param("timestamp") int timestamp, @Param("roomId") int roomId, @Param("username") String username);
}
