<template>
    <v-card raised class="mx-auto my-auto" max-width="500">
    <div align="center" class="primary mb-10 py-3 white--text">
      <h1 class="primary">New Team Request</h1>
    </div>

    <div align="center">
      Before creating a team, you must ask for a permission to be a Team Owner<br><br>
      <v-form ref="form" width=80%>
        <v-btn color="success" @click="createTeam" block>
          Send Request
        </v-btn>
      </v-form>
    </div>
    <v-spacer></v-spacer>
  </v-card>
</template>

<script>
    export default {
      name: 'NewTeamRequest', 
      data: () => ({

      }),
      methods:{ 
        createTeam() { 
          fetch('http://localhost:8081/api/fan/' + this.$root.memberID + '/requestRegisterAsTeamOwner', {
          method: 'POST',
          headers:{
            'Content-Type': 'application/json', 
            'authorization': this.$root.userToken        
          },
        })
          .then(async response => {
            alert(response.status);
            if (response.ok) {
              alert("request sent succesfully");
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
