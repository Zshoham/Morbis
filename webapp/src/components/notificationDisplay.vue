<template>
  <v-card raised class="mx-auto my-auto" max-width="500">
    <div align="center" dark class="primary py-3 white--text">
      <h1>New Events</h1>
    </div>

    <div align="center">
      <v-list>
        <v-list-tile v-for="event in list" :key="event" avatar>
          <br/>
          <v-layout row wrap>
            <v-flex  class="text-left mx-5 ">
              <v-list-tile-content>
                <v-list-tile-title v-text="event.gameTime"></v-list-tile-title>'  
                <v-list-tile-title v-text="event.description"></v-list-tile-title>
              </v-list-tile-content>
            </v-flex>
            <v-flex class="text-right mx-5">
                <img v-if="event.type == 'RED_CARD'"
                  height="20px"
                  width="20px"
                  src="https://upload.wikimedia.org/wikipedia/commons/thumb/e/e7/Red_card.svg/1200px-Red_card.svg.png"
                />
                <img v-if="event.type == 'YELLOW_CARD'"
                  height="20px"
                  width="20px"
                  src="https://upload.wikimedia.org/wikipedia/commons/thumb/b/b1/Yellow_card.svg/1200px-Yellow_card.svg.png"
                />
                <img v-if="event.type == 'GOAL'"
                  height="20px"
                  width="20px"
                  src="https://p7.hiclipart.com/preview/925/957/198/beach-ball-sport-american-football-ball.jpg"
                />
                <img v-if="event.type == 'OFFSIDE'"
                  height="20px"
                  width="20px"
                  src="https://image.flaticon.com/icons/svg/1540/1540536.svg"
                />
                <img v-if="event.type == 'FOUL'"
                  height="20px"
                  width="20px"
                  src="https://image.flaticon.com/icons/png/512/26/26846.png"
                />
                <img v-if="event.type == 'SUBSTITUTION'"
                  height="20px"
                  width="20px"
                  src="https://upload.wikimedia.org/wikipedia/commons/thumb/0/0d/Substitution.svg/1280px-Substitution.svg.png"
                />
              
            </v-flex>
          </v-layout>

            <v-divider/>
        </v-list-tile>
      </v-list>
    </div>
    <v-spacer></v-spacer>
  </v-card>
</template>

<script>
export default {
    props: {
    parentData: Object,
  },
  name: "notificationDisplay",
  mounted() {
    this.getNotificationsFromServer();
    this.$root.$emit('SetNotification',0);
  },
  data: () => ({
    list: [],
  }),
  methods: {
      addEvent(event) {
          this.list.push(event);
      },
      getNotificationsFromServer() {
      fetch(
        this.$root.baseURL + "/api/fan/" +
          this.$root.memberID +
          "/events",
        {
          //mode: 'no-cors',
          method: "GET",
          headers: {
            // accept: "*/*",
            authorization: this.$root.userToken
          }
        }
      )
        .then(response => {
          if (response.ok) {
            response.json().then(json => {
              let notifications = json;
              for (let i = 0; i < notifications.length; i++) {
                this.addEvent(notifications[i]);
              }
            });
          } else {
            if (response.status == 404) {
              alert(
                "You don't have any notifications"
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
  }
};
</script>

<style scoped>
</style>