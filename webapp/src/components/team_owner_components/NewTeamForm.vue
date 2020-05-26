<template>
    <v-card raised class="mx-auto my-auto" max-width="500">
    <div align="center" class="primary mb-10 py-3 white--text">
      <h1 class="primary">New Team Request</h1>
    </div>

    <div align="center">
      If you want to create a team you should send a 'New Team Request'<br><br>
      <v-form ref="form" width=80%>
        <v-text-field class="mx-5" outlined :prepend-icon="'mdi-account-group'" label="Team Name:" required></v-text-field>
<!-- 
        <v-select
          v-model="e7"
          :items="players"
          label="Players"
          multiple
          chips
          hint="only the best players for your team!"
          persistent-hint
        ></v-select>
                <v-select
          v-model="e7"
          :items="owners"
          label="Owners"
          multiple
          chips
          hint="only the best owners for your team!"
          persistent-hint
        ></v-select>
                <v-select
          v-model="e7"
          :items="coaches"
          label="Coaches"
          multiple
          chips
          hint="only the best coaches for your team!"
          persistent-hint
        ></v-select>
          <v-select
          v-model="e7"
          :items="managers"
          label="Managers"
          multiple
          chips
          hint="only the best managers for your team!"
          persistent-hint
        ></v-select>
                  <v-select
          v-model="e7"
          :items="stadiums"
          label="Stadium"
          chips
          hint="only the best stadium for your team!"
          persistent-hint
        ></v-select>
         -->
         Pressing 'Send' will create a 'New Team Request'. after <br>
         its aprroval you will be able to set the New Team
         <!-- TODO: add click function -->
        <v-btn color="success" block>
          Send Request
        </v-btn>
      </v-form>
    </div>
    <v-spacer></v-spacer>
  </v-card>
</template>

<script>
    export default {
    name: 'NewTeamPage', 
    data: () => ({
      players: ['Messi', 'Benayun', 'Maradona', 'Bufun'],
      coaches: ['Guardiola', 'David Blatt', 'Pini Gershon', 'Mookie'],
      managers: ['shaike', 'polly', 'gavri'],
      stadiums: ['camp nou', 'menora', 'terner'],
      owners: ['haim', 'hod', 'shoam', 'yuval','ron']
    }),
    }
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
