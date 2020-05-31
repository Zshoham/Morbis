<template>
  <div class="my-10">
    <TeamOwnerRequestsForm/>
  </div>
</template>

<script>
// @ is an alias to /src
import TeamOwnerRequestsForm from '@/components/TeamOwnerRequestsForm.vue'

export default {
  name: 'TeamOwnerRequestsPage',
  mounted(){
    /*if(!(this.$root.userToken == "" || this.$root.userToken == null)){//logged in so send him to home page
      this.$router.push("/HomePage");
    }*/
 },
  components: {
    TeamOwnerRequestsForm
  }
}
</script>