<template>
  <div class="teamOwnerPage">
    <TeamOwner/>
  </div>
</template>

<script>
// @ is an alias to /src
import TeamOwner from '@/components/TeamOwnerComponent.vue'

export default {
  name: 'TeamOwnerPage',
  mounted(){
    if(this.$root.userToken == "" || this.$root.userToken == null){//he's not logged in so send him to welcome page
      this.$router.push("/WelcomePage");
    }
 },
  components: {
    TeamOwner
  }
}
</script>
