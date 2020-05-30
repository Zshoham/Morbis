<template>
  <v-card raised class="mx-auto my-auto" max-width="500">
    <div align="center" dark class="primary mb-10 py-3 white--text">
      <h1>Login</h1>
    </div>
    
    <div align="center">
      
      <v-form ref="form" v-model="valid">
        <v-text-field class="mx-5" outlined v-model="username" :prepend-icon="'mdi-account'" label="Username" required></v-text-field>
        <v-text-field class="mx-5" outlined v-model="password" :prepend-icon="'mdi-lock'" :append-icon="passwordVisable ? 'mdi-eye' : 'mdi-eye-off'" @click:append="passwordVisable = !passwordVisable"  :type="passwordVisable ? 'text' : 'password'" label="Password" required></v-text-field>

        <v-btn color="success" block @click="login">
          Login
          <v-icon right>mdi-check-circle</v-icon>
        </v-btn>  
      </v-form>
    </div>
    <v-spacer ></v-spacer>
  </v-card>
  
</template>
<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.4.0/sockjs.js"></script>
<script>
import SockJS from "sockjs-client";
import Stomp from "webstomp-client";
export default {
  name: "LoginForm",
  data: () => ({
    valid: false,
    username: "",
    password: "",
    passwordVisable: false,
    appData: this.$root.App
  }),    addNotification(){
      this.$root.notificationCount.value++;
    },
    clearNotifications(){
      this.$root.notificationCount.value=0;
    },
  methods: {
    connectToServer() {
      console.log("connecting to server");
      const url = "http://dev.morbis.xyz";
      let socket = new SockJS(url + "/api/websocket");
      this.stompClient = Stomp.over(socket);
      var tempClient = this.stompClient;
      this.$root.client=this.stompClient;
      let noteObj = this.$root.notificationCount;
      this.$root.client.connect({}, function(frame) {
        console.log("connected to: " + frame);
        //TODO: get subscribed Games
        tempClient.subscribe('/api/events/game-events/' + 15,(response) => {
          let data = JSON.parse(response.body);
          console.log('new nofitication:' + data.type);//TODO :replace with actual notification
          //noteObj.value++;
          AppData.notificationCount++;
        })
      })       
      this.connected = true;
    },
    login() {
      if(!this.valid) {
        alert("There's a problem");
        return;
      }
      fetch('http://dev.morbis.xyz/api/login', {
        method: 'POST',
        headers:{
          'Content-Type': 'application/json',          
        },
        body: JSON.stringify({
          username: this.username,
          password: this.password,
        })
      })
        .then(response => {
          if (response.ok) {
            response.json().then(json => {
              this.$root.userToken = json.token;
              this.$root.roles = json.roles;
              this.$root.memberID = json.memberID
              this.changeMenu(this.$root.roles);
              alert("Logged In successfully !")
              console.log(this.$root.userToken);
              this.$router.push("/HomePage");
              this.connectToServer();
            })
          } else {
              if(response.status == 401) {
                alert("The combination of username and password doesn't exist");
              }else{
                response.json().then(json => { alert(response.status + ": " + json.message); });
              }
          }
        })
        .catch(err => console.error(err));
    },
    changeMenu: function(roles){
     this.$root.$emit('loginChangeMenu',roles) //like this
    }
  }
};
</script>

<style scoped>
</style>