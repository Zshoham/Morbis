<template>
  <v-card raised class="mx-auto my-auto" max-width="500">
    <div align="center" dark class="primary mb-10 py-3 white--text">
      <h1>League's Options</h1>
    </div>

    <div align="center">
      <v-select
        v-model="selectedLeague"
        :items="leagues"
        item-text="leagueName"
        label="League Name"
        class="mx-5"
        outlined
        @change="updateMethods"
      ></v-select>
      <v-select
        v-model="selectedSchedulingMethod"
        :items="schedulingMethods"
        label="Scheduling Method"
        class="mx-5"
        outlined
      ></v-select>
      <v-select
        v-model="selectedScoringMethod"
        :items="scoringMethods"
        label="Scoring Method"
        class="mx-5"
        outlined
      ></v-select>
      <v-btn color="success" block @click="changeLeagueMethods">
        Apply Options
        <v-icon right>mdi-check-circle</v-icon>
      </v-btn>
    </div>
    <v-spacer></v-spacer>
  </v-card>
</template>

<script>
export default {
  name: "ChangeLeagueOptions",
  mounted() {
    this.getLeaguesFromServer();
    this.getSchedulingMethodsFromServer();
    this.getScoringMethodsFromServer();
  },
  data: () => ({
    selectedLeague: "",
    selectedSchedulingMethod: "",
    selectedScoringMethod: "",
    test: ["A", "B", "C", "D"],
    leagues: [],
    schedulingMethods: [],
    scoringMethods: []
  }),
  methods: {
    changeLeagueMethods() {
      let selectedLeague = this.leagues.find(league => league.leagueName == this.selectedLeague);
      selectedLeague.schedulingMethod = this.selectedSchedulingMethod;
      selectedLeague.scoringMethod = this.selectedScoringMethod;
      fetch("http://localhost:8081/api/association-rep/update-policy", {
        // mode: 'no-cors',
        method: "POST",
        headers: {
           "Content-Type": "application/json",
            'authorization': this.$root.userToken
        },
        body: JSON.stringify({
          leagueID: selectedLeague.leagueID,
          leagueName: selectedLeague.leagueName,
          schedulingMethod: selectedLeague.schedulingMethod,
          scoringMethod: selectedLeague.scoringMethod
        })
      })
        .then(response => {
          if (response.ok) {
          alert(response.status);            
          } else {
            response.json().then(json => {
                alert(response.status + ": " + json.message);
            });
          }
        })
        .catch(err => console.error(err));
    },
    updateMethods() {
      console.log(this.leagues);
      console.log("selected league: " + this.selectedLeague);
      console.log(
        "finding league: " +
          this.leagues.find(league => league.leagueName == this.selectedLeague)
            .scoringMethod
      );
      console.log("scoring method is:" + this.leagues[0].scoringMethod);
      this.selectedSchedulingMethod = this.leagues.find(
        league => league.leagueName == this.selectedLeague
      ).schedulingMethod;
      this.selectedScoringMethod = this.leagues.find(
        league => league.leagueName == this.selectedLeague
      ).scoringMethod;
    },
    getLeaguesFromServer() {
      fetch("http://localhost:8081/api/association-rep/leagues", {
        // mode: 'no-cors',
        method: "GET",
        headers: {
          // accept: "*/*",
          'authorization': this.$root.userToken
        }
      })
        .then(response => {
          if (response.ok) {
            response.json().then(json => {
              this.leagues = json;
            });
          } else {
            alert(
              "Server returned " + response.status + " : " + response.statusText
            );
          }
        })
        .catch(err => console.error(err));
    },
    getScoringMethodsFromServer() {
      fetch("http://localhost:8081/api/association-rep/scoring-methods", {
        // mode: 'no-cors',
        method: "GET",
        headers: {
          // accept: "*/*",
          // "Authorization":this.$root.userToken
        'authorization': this.$root.userToken
        }
      })
        .then(response => {
          if (response.ok) {
            response.json().then(json => {
              this.scoringMethods = json;
            });
          } else {
            alert(
              "Server returned " + response.status + " : " + response.statusText
            );
          }
        })
        .catch(err => console.error(err));
    },
    getSchedulingMethodsFromServer() {
      fetch("http://localhost:8081/api/association-rep/scheduling-methods", {
        //mode: 'no-cors',
        method: "GET",
        headers: {
          // accept: "*/*",
          'authorization': this.$root.userToken
        }
      })
        .then(response => {
          if (response.ok) {
            response.json().then(json => {
              this.schedulingMethods = json;
            });
          } else {
            alert(
              "Server returned " + response.status + " : " + response.statusText
            );
          }
        })
        .catch(err => console.error(err));
    },
  }
};
</script>

<style scoped>
</style>