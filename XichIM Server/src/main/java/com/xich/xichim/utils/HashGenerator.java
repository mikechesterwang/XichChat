package com.xich.xichim.utils;

public class HashGenerator {

    private final static char[] characters = {'A', 'B', 'C', 'D', 'E', 'F', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};

    public static String generateRandomToken(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 16; ++i){
            sb.append(characters[(int)(Math.random() * (characters.length - 1))]);
        }
        return sb.toString();
    }

    public static String directMessageRoomNameHash(String id1, String id2){
        return id1.hashCode() > id2.hashCode() ? (id1 + " " + id2) : (id2 + " " + id1);
    }
}
