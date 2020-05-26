<template>
  <v-layout row wrap>
    <v-flex>
      <v-card raised class="mx-auto my-auto" max-width="500">
        <div align="center" dark class="primary mb-10 py-3 white--text">
          <h1>Game Management</h1>
        </div>

        <div align="center">
          <v-form ref="form" v-model="valid" :lazy-validation="lazy">
            <div v-if="connected == false">
              <v-btn class="my-5" color="success" @click="connectToServer">Connect to Server</v-btn>
              <br />Connect to the server in order to add an event
            </div>
            <div v-else>
              <v-select v-model="event" :items="eventTypes" label="Event" class="mx-5" outlined></v-select>
              <v-text-field class="mx-5" outlined v-model="gameTime" label="Time" required></v-text-field>
              <v-textarea
                class="mx-5"
                outlined
                v-model="description"
                label="Description"
                rows="1"
                counter="512"
                auto-grow
                required
              ></v-textarea>

              <v-btn color="success" block @click="sendEventToServer">
                Add Event
                <v-icon right>mdi-check-circle</v-icon>
              </v-btn>
            </div>
          </v-form>
        </div>
        <v-spacer></v-spacer>
      </v-card>
    </v-flex>
    <v-flex>
      <ShowGameEvents ref="showGameEvents" />
    </v-flex>
  </v-layout>
</template>

<!-- <script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.4.0/sockjs.js"></script> -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
<script>
import ShowGameEvents from "./ShowGameEvents.vue";
import SockJS from "sockjs-client";
import Stomp from "webstomp-client";


export default {
  components: {
    ShowGameEvents
  },
  name: "GameManage",
  data: () => ({
    connected: false,
    eventTypes: [
      "Goal",
      "Offside",
      "Foul",
      "Red Card",
      "Yellow Card",
      "Substitution"
    ]
  }),
  methods: {
    connectToServer() {
      console.log("connecting to server");
      const url = "http://dev.morbis.xyz";
      let socket = new SockJS(url + "/api/websocket");
      var stompClient = Stomp.over(socket);
      stompClient.connect({}, function(frame) {
        console.log("connected to: " + frame);
      });
      this.connected = true;
    },
    sendEventToServer(refID, gameID, game) {
      let data = {
          game: game,
          type: this.event,
          date: "yo",
          gameTime: this.gameTime,
          description: this.description
      }
      this.$refs.showGameEvents.addEvent(data)
      stompClient.send(
        "/api/live/game-event/" + refID + "/" + gameID,
        {},
        JSON.stringify({
          data
        })
      );
    }
  }
};
</script>

<style scoped>
</style>