<template>
    <v-card raised class="mx-auto my-auto" max-width="500">
    <div align="center" class="primary mb-10 py-3 white--text">
      <h1 class="primary">New Team Request</h1>
    </div>

    <div align="center">
      To create a team, enter a team name<br><br>
      <v-form ref="form" width=80%>
        <v-text-field class="mx-5" outlined v-model="teamName" label="Team Name" required></v-text-field>
        <v-btn color="success" @click="createTeam" block>
          Send Team Name
        </v-btn>
      </v-form>
    </div>
    <v-spacer></v-spacer>
  </v-card>
</template>

<script>
    export default {
      name: 'SubmitTeamForm', 
      data: () => ({
            teamName: ""
      }),
      methods:{ 
        createTeam() { 
          fetch(this.$root.baseURL + '/api/team-owner/' + this.$root.memberID + '/create-team', {
          method: 'POST',
          headers:{
            'Content-Type': 'application/json', 
            'authorization': this.$root.userToken        
          },
          body: JSON.stringify(this.teamName)
        })
          .then(async response => {
            if (response.ok) {
              alert("Your team has been created !");
            } else {
              alert(
                "Server returned " + response.status + " : " + response.statusText+"\n please try again later"
              );
            }
          })
          .catch(err => console.error(err));
        }
      }
    };
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
