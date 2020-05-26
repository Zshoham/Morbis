<template>
  <v-card raised class="mx-auto my-auto" max-width="500">
    <div align="center" dark class="primary py-3 white--text">
      <h1>Game Events</h1>
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
                <img v-if="event.type == 'Red Card'"
                  height="20px"
                  width="20px"
                  src="https://upload.wikimedia.org/wikipedia/commons/thumb/e/e7/Red_card.svg/1200px-Red_card.svg.png"
                />
                <img v-if="event.type == 'Yellow Card'"
                  height="20px"
                  width="20px"
                  src="https://upload.wikimedia.org/wikipedia/commons/thumb/b/b1/Yellow_card.svg/1200px-Yellow_card.svg.png"
                />
                <img v-if="event.type == 'Goal'"
                  height="20px"
                  width="20px"
                  src="https://p7.hiclipart.com/preview/925/957/198/beach-ball-sport-american-football-ball.jpg"
                />
                <img v-if="event.type == 'Offside'"
                  height="20px"
                  width="20px"
                  src="https://image.flaticon.com/icons/svg/1540/1540536.svg"
                />
                <img v-if="event.type == 'Foul'"
                  height="20px"
                  width="20px"
                  src="https://image.flaticon.com/icons/png/512/26/26846.png"
                />
                <img v-if="event.type == 'Substitution'"
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
  name: "ShowGameEvents",
  data: () => ({
    list: [
      {
        type: "Goal",
        gameTime: "10",
        description: "Messi scored"
      },
      {
        type: "Red Card",
        gameTime: "20",
        description: "Pepe mehabel"
      },
      {
        type: "Yellow Card",
        gameTime: "30",
        description: "suarez nashah"
      },
      {
        type: "Offside",
        gameTime: "40",
        description: "Ronaldo mair miday"
      },
      {
        type: "Foul",
        gameTime: "50",
        description: "pepe hozer linkom"
      },
      {
        type: "Substitution",
        gameTime: "60",
        description: "meifim et pepe vesamim misheoo mi daesh"
      },
      {
        type: "Goal",
        gameTime: "70",
        description: "uvuvwevwevwe onyetenyevwe ugwemuhwem osas scored a goal while the other team tried to say his name"
      },
      {
        type: "Offside",
        gameTime: "80",
        description: "the player niv dal is always in offside"
      }
    ],
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
      addEvent(event) {
          this.list.push(event);
      }
  }
};
</script>

<style scoped>
</style>