<template>
  <div class="text-xs-center">
    <v-toolbar
      height="70px"
      dark
      prominent
      src="https://www.worcester.gov.uk/images/easyblog_shared/2019/b2ap3_amp_Football---carousel.jpg">

      <v-toolbar-icon><v-img :src="require('../assets/morbis-logo.svg')"  contain height="50px" width="50px"></v-img></v-toolbar-icon>
      <v-toolbar-title>Morbis</v-toolbar-title>
      <v-spacer></v-spacer>
      <div>
          <v-btn to="/">Home</v-btn>
          <v-btn to="/about">About</v-btn>
          <v-btn to="/RegisterPage">Register</v-btn>
          <v-btn to="/LoginPage">Login</v-btn>
      </div>
      <v-spacer></v-spacer>
    </v-toolbar>
    <router-view />
  </div>
</template>

<script>
export default {
  name: "ourToolBar"
};
</script>

<style scoped>
.v-btn {
  margin: 0 10px 0;
}
</style>