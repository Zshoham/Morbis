<template>
  <v-card raised class="mx-auto my-auto" max-width="500">
    <div align="center" dark class="primary mb-10 py-3 white--text">
      <h1>League's Options</h1>
    </div>
    
    <div align="center">
        <v-select v-model="selectedLeague" :items="leagues" item-text="leagueName" label="League Name" class="mx-5" outlined @change="updateMethods"></v-select>
        <v-select v-model="selectedSchedulingMethod" :items="schedulingMethods" label="Scheduling Method" class="mx-5" outlined></v-select>
        <v-select v-model="selectedScoringMethod" :items="scoringMethods" label="Scoring Method" class="mx-5" outlined></v-select>
        <v-btn color="success" block @click="changeLeagueMethods">
          Apply Options
          <v-icon right>mdi-check-circle</v-icon>
        </v-btn>  

    </div>
    <v-spacer ></v-spacer>
  </v-card>
  
</template>

<script>
export default {
  name: "ChangeLeagueOptions",
  data: () => ({
    selectedLeague: "",
    selectedSchedulingMethod: "",
    selectedScoringMethod: "",
    test: ["A","B","C","D"],
    leagues: [
        {
            leagueName: "La Liga",
            schedulingMethod: "La Liga scheduling",
            scoringMethod: "La Liga scoring"
        },
        {
            leagueName: "Serie A",
            schedulingMethod: "Serie A scheduling",
            scoringMethod: "Serie A scoring"
        },
        {
            leagueName: "Ligat Haal",
            schedulingMethod: "Ligat Haal scheduling",
            scoringMethod: "Ligat Haal scoring"
        },
        {
            leagueName: "Premier League",
            schedulingMethod: "Premier scheduling",
            scoringMethod: "Premier scoring"
        }
        ],
        schedulingMethods: ["La Liga scheduling","Serie A scheduling","Ligat Haal scheduling","Premier scheduling" ],
        scoringMethods:["La Liga scoring","Serie A scoring","Ligat Haal scoring","Premier scoring"]
  }),
  methods: {
    changeLeagueMethods() {
      fetch('http://dev.morbis.xyz:8080/api/...', {
        method: 'POST',
        headers:{
          'Content-Type': 'application/json',          
        },
        body: JSON.stringify({
          league: this.league,
          schedulingMethod: this.selectedSchedulingMethod,
          scoringMethod: this.selectedScoringMethod,
        })
      })
        .then(async response => {
          alert(response.status);
          if (response.ok) {
            response => (this.info = response);
          } else {
            alert(
              "Server returned " + response.status + " : " + response.statusText
            );
          }
        })
        .catch(err => console.error(err));
    },
    updateMethods() {
      this.selectedSchedulingMethod = this.leagues.find(league => league.leagueName === this.selectedLeague).schedulingMethod
      this.selectedScoringMethod = this.leagues.find(league => league.leagueName === this.selectedLeague).scoringMethod
    }
  }
};
</script>

<style scoped>
</style>