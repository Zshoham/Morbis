<template>
  <v-card raised class="mx-auto my-auto" max-width="500">
    <div align="center" dark class="primary mb-10 py-3 white--text">
      <h1>Login</h1>
    </div>
    
    <div align="center">
      
      <v-form ref="form" v-model="valid" :lazy-validation="lazy">
        <v-text-field class="mx-5" outlined v-model="username" :prepend-icon="'mdi-account'" label="Username" required></v-text-field>
        <v-text-field class="mx-5" outlined v-model="password" :prepend-icon="'mdi-lock'" :append-icon="passwordVisable ? 'mdi-eye' : 'mdi-eye-off'" @click:append="passwordVisable = !passwordVisable"  :type="passwordVisable ? 'text' : 'password'" label="Password" required></v-text-field>

        <v-btn color="success" block @click="validate">
          Login
          <v-icon right>mdi-check-circle</v-icon>
        </v-btn>  
      </v-form>
    </div>
    <v-spacer ></v-spacer>
  </v-card>
  
</template>

<script>
export default {
  name: "RegisterForm",
  data: () => ({
    
  }),
  methods: {
    login() {
      
    }
  }
};
</script>

<style scoped>
</style>