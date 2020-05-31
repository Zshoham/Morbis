<template>
  <div class="my-10">
    <SubmitTeamForm/>
  </div>
</template>

<script>
// @ is an alias to /src
import SubmitTeamForm from '@/components/team_owner_components/SubmitTeamForm.vue'

export default {
  name: 'SubmitTeamPage',
  mounted(){
    if(this.$root.userToken == "" || this.$root.userToken == null){//he's not logged in
        this.$router.push("/WelcomePage");    }
    else if(this.$root.roles.includes('TeamOwner')) {//he doesn't have permissions
        this.$router.push("/HomePage");    }
 },
  components: {
    SubmitTeamForm
  }
}
</script>
