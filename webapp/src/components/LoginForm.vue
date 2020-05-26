<template>
  <v-card raised class="mx-auto my-auto" max-width="500">
    <div align="center" dark class="primary mb-10 py-3 white--text">
      <h1>Login</h1>
    </div>
    
    <div align="center">
      
      <v-form ref="form" v-model="valid" :lazy-validation="lazy">
        <v-text-field class="mx-5" outlined v-model="username" :prepend-icon="'mdi-account'" label="Username" required></v-text-field>
        <v-text-field class="mx-5" outlined v-model="password" :prepend-icon="'mdi-lock'" :append-icon="passwordVisable ? 'mdi-eye' : 'mdi-eye-off'" @click:append="passwordVisable = !passwordVisable"  :type="passwordVisable ? 'text' : 'password'" label="Password" required></v-text-field>

        <v-btn color="success" block @click="login">
          Login
          <v-icon right>mdi-check-circle</v-icon>
        </v-btn>  
      </v-form>
      {{info}}
    </div>
    <v-spacer ></v-spacer>
  </v-card>
  
</template>

<script>
export default {
  name: "LoginForm",
  data: () => ({
    
  }),
  methods: {
    login() {
      fetch('http://dev.morbis.xyz:8080/api/login', {
        method: 'POST',
        headers:{
          'Content-Type': 'application/json',          
        },
        body: JSON.stringify({
          username: this.username,
          password: this.password,
        })
      })
        .then(async response => {
          alert(response.status);
          if (response.ok) {
            response => (this.info = response);
          } else {
            alert(
              "Server returned " + response.status + " : " + response.statusText
            );
          }
        })
        .catch(err => console.error(err));
    }
  }
};
</script>

<style scoped>
</style>