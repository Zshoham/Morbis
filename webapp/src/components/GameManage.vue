<template>
  <v-layout row wrap>
    <v-flex>
      <v-card raised class="mx-auto my-auto" max-width="500">
        <div align="center" dark class="primary mb-10 py-3 white--text">
          <h1>Game Management</h1>
        </div>

        <div align="center">
          <v-form ref="form" v-model="valid">
            <div v-if="false">
              <v-btn class="my-5" color="success" @click="connectToServer">Connect to Server</v-btn>
              <br />Connect to the server in order to add an event
            </div>
            <div v-else>
              <v-select v-model="event" :items="eventTypes" label="Event" class="mx-5" outlined></v-select>
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

<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.4.0/sockjs.js"></script>
<script>
import ShowGameEvents from "./ShowGameEvents.vue";
import SockJS from "sockjs-client";
import Stomp from "webstomp-client";

export default {
  components: {
    ShowGameEvents
  },
  name: "GameManage",
  mounted() {
    this.getOngoingGameEvents();
    this.getOngoingGame();
  },
  data: () => ({
    valid: true,
    connected: true,
    stompClient: null,
    game: null,
    gameEvents: null,
    eventTypes: [
      "GOAL",
      "OFFSIDE",
      "FOUL",
      "RED_CARD",
      "YELLOW_CARD",
      "SUBSTITUTION"
    ]
  }),
  methods: {
    connectToServer() {
      console.log("connecting to server");
      this.connected = true;
    },
    sendEventToServer() {
      if (!this.valid) {
        alert("Theres a problem with the form");
        return;
      }
      let data = {
        type: this.event,
        description: this.description
      };
      this.$root.client.send(
        "/api/live/" + this.$root.memberID + "/game-event",
        JSON.stringify(data)
      );
      setTimeout(() => {
        this.$refs.showGameEvents.clearEvents();
        this.getOngoingGameEvents();
      }, 500);
    },
    getOngoingGameEvents() {
      fetch(
        "http://localhost:8081/api/referee/" +
          this.$root.memberID +
          "/game-events-ongoing",
        {
          method: "GET",
          headers: {
            authorization: this.$root.userToken
          }
        }
      )
        .then(response => {
          if (response.ok) {
            response.json().then(json => {
              this.gameEvents = json;
              for (let i = 0; i < this.gameEvents.length; i++) {
                this.$refs.showGameEvents.addEvent(this.gameEvents[i]);
              }
            });
          } else {
            if (response.status == 404) {
              alert(
                "You're not in any game currently. You're being redirected to home page"
              );
              this.$router.push("/HomePage");
            } else {
              alert(
                "Server returned " +
                  response.status +
                  " : " +
                  response.statusText
              );
            }
          }
        })
        .catch(err => console.error(err));
    },
    getOngoingGame() {
      fetch(
        "http://localhost:8081/api/referee/" +
          this.$root.memberID +
          "/game-ongoing",
        {
          method: "GET",
          headers: {
            authorization: this.$root.userToken
          }
        }
      )
        .then(response => {
          if (response.ok) {
            response.json().then(json => {
              this.game = json;
            });
          } else {
            alert(
              "Server returned " + response.status + " : " + response.statusText
            );
          }
        })
        .catch(err => console.error(err));
    }
  }
};
</script>

<style scoped>
</style>