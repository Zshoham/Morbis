<template>
  <div>
    <h1>
      Hello and welcome to morbis' site !
      <br />Please Log in to continue
    </h1>
  </div>
</template>

<script>

export default {
  name: "WelcomePage",
  mounted(){
    if(!(this.$root.userToken == "" || this.$root.userToken == null)){//logged in so send him to home page
      this.$router.push("/HomePage");
    }
 },
};
</script>
