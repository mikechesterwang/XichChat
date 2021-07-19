// app.js
App({
  globalData: {
    chatSocket: '',
  },

  chatSend(data, id, cb){
    console.log(data)
    data.id = id;
    this.globalData.chatSocket.send({
      data: JSON.stringify(data),
      success: cb,
      fail: err => console.error
    })
  },

  chatSendMessage(id, message, roomId){
    this.chatSend({
      command: "send",
      parameters: [message, roomId]
    }, id)
  },

  chatGetRoomList(){
    this.chatSend({
      command: "roomList",
      parameters: []
    }, 'get-room')
  },

  chatLogin(username, password){
    this.chatSend({
      command: "login",
      parameters: [username, password]
    }, 'login')
  },

  chatRegister(username, password){
    this.chatSend({
      command: "register",
      parameters: [username, password]
    }, 'register')
  },

  chatCreateDirectMessage(receiver){
    this.chatSend({
      command: "createDirectMessage",
      parameters: [receiver]
    }, 'createDirectMessage')
  },

  chatLoadHistory(roomId){
    this.chatSend({
      command: "load",
      
      parameters: [roomId, Math.round(new Date().getTime() / 1000), 20]
    })
  },

  chatMarkRead(roomId){
    this.chatSend({
      command: "markRead",
      parameters: [roomId]
    }, 'mark-read')
  },

  onLaunch() {
    console.log("与WebSocket服务器建立连接...")
    this.globalData.chatSocket = wx.connectSocket({
      url: 'ws://192.168.3.33:8080/registry',
      success: (e) => {
        console.log()
        wx.onSocketOpen((e) => {
          console.log("连接WebSocket成功。")
          wx.onSocketMessage((e) => {
            console.log("====服务器====")
            console.log(e)
            console.log("=============\n")
          })
        })
      },
      fail: err => console.error(err),
      timeout: 300
    });
  },

})
