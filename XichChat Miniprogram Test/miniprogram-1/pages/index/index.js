// index.js
// 获取应用实例
const app = getApp();

Page({
  data: {

    inputContent: '',
    username: "username",
    password: "password",
    receiver: "",
    roomId: "",
    messageCount: 0
  },

  onRoomId(e){
    var text = e.detail.value;
    this.setData({
      roomId: text
    })
  },

  onUsername(e){
    var text = e.detail.value;
    this.setData({
      username: text
    })
  },

  onPassword(e){
    var text = e.detail.value;
    this.setData({
      password: text
    })
  },

  onReceiver(e){
    var text = e.detail.value;
    this.setData({
      receiver: text
    })
  },

  onInput(e){
    var text = e.detail.value;
    this.setData({
      inputContent: text
    })
  },


  login() {
    app.chatLogin(this.data.username, this.data.password);
  },

  register(){
    app.chatRegister(this.data.username, this.data.password);
  },

  createDirectMessage(){
    var receiver = this.data.receiver;
    app.chatCreateDirectMessage(receiver);
  },

  sendMessage(){
    console.log("send")
    var message = this.data.inputContent;
    var roomId = this.data.roomId;
    this.data.messageCount += 1;
    app.chatSendMessage(this.data.username + ' ' + this.data.messageCount, message, roomId);
  },

  getRoomList(){
    app.chatGetRoomList()
  },

  loadHistory(){
    app.chatLoadHistory(this.data.roomId);
  },

  markRead(){
    app.chatMarkRead(this.data.roomId);
  }

})
