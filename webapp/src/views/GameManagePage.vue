<template>
  <div class="my-10">
         <GameManage/>
  </div>
</template>

<script>
// @ is an alias to /src
import GameManage from '@/components/GameManage.vue'

export default {
  name: 'GameManagePage',
  mounted(){
    if(this.$root.userToken == "" || this.$root.userToken == null){//he's not logged in so send him to welcome page
      this.$router.push("/WelcomePage");
    }
 },
  components: {
    GameManage
  }
}
</script>