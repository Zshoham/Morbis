<template>
  <div class="my-10">
    <RegisterForm/>
  </div>
</template>

<script>
// @ is an alias to /src
import RegisterForm from '@/components/RegisterForm.vue'

export default {
  name: 'RegisterPage',
  mounted(){
    if(!(this.$root.userToken == "" || this.$root.userToken == null)){//logged in so send him to home page
      this.$router.push("/HomePage");
    }
 },
  components: {
    RegisterForm
  }
}
</script>