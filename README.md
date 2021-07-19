# XichChat

### Introduction

XichChat consist of two parts: 1) XichIM Server and 2) XichChat Miniprogram Test

XichChat Miniprogram Test is a frontend sample.

XichIM Server is an lightweight server for chatting or instant messaging (IM).

Based on Spring boot. The protocol is implemented by plain WebSocket. Mysql 5.7 is used for persistent storage.

 

### Realtime API

All API are implemented based on WebSocket. Before invoke the following method, connect the server using WebSocket at first.

##### register user

```json
{
    command: "register",
    parameters: ["username", "password"]
}
```

##### login

```json
{
    command: "login",
    parameters: ["username", "password"]
}
```

##### send message to a room

```json
{
    command: "send",
    parameters: ["message", "room-id"]
}
```

*If the user is not in this room, this request will be treated as an attack. The user will be put in the blacklist.*

##### load

```json
{
    command: "load",
    parameters: ["room-id", "endTimestamp", "count"]
}
```

Lod history message

##### create direct message

```JSON
{
    command: "createDirectMessage",
    parameters: ["username-receiver"]
}
```

##### get room list

```JSON
{
    command: "roomList",
    parameters: []
}
```

##### mark as read

```JSON
{
    command: "markRead",
    parameters: ["room-id"]
}
```





### Deployment

1. Set up the database: import `db.sql` to your mysql server (version 5.7+)
2. Config the database connection in `application.properties`
3. Run on JDK 11+



*TODO*: 

1. Use Redis or other in-memory mapping solution to manage the state. 
2. Create room, join room, leave room...