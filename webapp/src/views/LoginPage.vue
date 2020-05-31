<template>
  <div class="my-10">
    <LoginForm/>
  </div>
</template>

<script>
// @ is an alias to /src
import LoginForm from '@/components/LoginForm.vue'

export default {
  name: 'LoginPage',
  mounted(){
    if(!(this.$root.userToken == "" || this.$root.userToken == null)){//logged in so send him to home page
this.$router.push("/HomePage");    }
 },
  components: {
    LoginForm
  },
}
</script>