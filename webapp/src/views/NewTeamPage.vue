<template>
  <div class="NewTeamPage">
    <NewTeamForm/>
  </div>
</template>

<script>
// @ is an alias to /src
import NewTeamForm from '@/components/team_owner_components/NewTeamForm.vue'

export default {
  name: 'NewTeamPage',
  mounted(){
    if(this.$root.userToken == "" || this.$root.userToken == null){//he's not logged in
this.$router.push("/WelcomePage");    }
    else if(this.$root.roles.includes('TeamOwner')) {//he doesn't have permissions
this.$router.push("/HomePage");    }
 },
  components: {
    NewTeamForm
  }
}
</script>
