<template>
  <v-row align="center">
    <v-spacer></v-spacer>
    <v-form ref="form" v-model="valid" :lazy-validation="lazy">
      <v-text-field v-model="username" label="Username" required></v-text-field>
      <v-text-field v-model="password" :type="'password'" label="Password" required></v-text-field>

      <v-btn color="green" @click="Login">Login</v-btn>
    </v-form>
    <v-spacer></v-spacer>
  </v-row>
</template>

<script>
export default {
  name: "LoginForm",
  methods: {
    Login() {
        
    }
  }
};
</script>