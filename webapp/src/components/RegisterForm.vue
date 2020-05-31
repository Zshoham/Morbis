<template>
  <v-card raised class="mx-auto my-auto" max-width="500">
    <div align="center" class="primary mb-10 py-3 white--text">
      <h1 class="primary">Register</h1>
    </div>

    <div align="center">
      <v-form ref="form" v-model="valid">
        <v-text-field
          class="mx-5"
          outlined
          v-model="username"
          :prepend-icon="'mdi-account'"
          :rules="usernameRules"
          label="Username"
          required
        ></v-text-field>
        <v-text-field
          class="mx-5"
          outlined
          v-model="password"
          :prepend-icon="'mdi-lock'"
          :append-icon="passwordVisable ? 'mdi-eye' : 'mdi-eye-off'"
          @click:append="passwordVisable = !passwordVisable"
          :type="passwordVisable ? 'text' : 'password'"
          :rules="passwordRules"
          label="Password"
          required
        ></v-text-field>
        <v-text-field
          class="mx-5"
          outlined
          v-model="name"
          :prepend-icon="'mdi-account'"
          :rules="nameRules"
          label="Name"
          required
        ></v-text-field>
        <v-text-field
          class="mx-5"
          outlined
          v-model="email"
          :prepend-icon="'mdi-email'"
          :type="'email'"
          :rules="emailRules"
          label="E-mail"
          required
        ></v-text-field>
        <v-btn color="success" block @click="register">
          Register
          <v-icon right>mdi-check-circle</v-icon>
        </v-btn>
      </v-form>
    </div>
    <v-spacer></v-spacer>
  </v-card>
</template>

<script>
export default {
  name: "RegisterForm",
  data: () => ({
    valid: false,
    username: "",
    password: "",
    name: "",
    email: "",
    usernameRules: [
      v =>
        !v ||
        /[a-zA-Z][a-zA-Z0-9_.@]{3,}/.test(v) ||
        "Username must be alphanumeric, at least 4 letters and start with a letter"
    ],
    passwordRules: [
      v =>
        !v ||
        (/(.*[a-z].*)/.test(v) &&
          /(.*[A-Z].*)/.test(v) &&
          /(.*[0-9].*)/.test(v) &&
          /(.{8,20})/.test(v)) ||
        "Password must contain: numbers, 8-20 letters long and big and small letters"
    ],
    nameRules: [
      v =>
        !v ||
        /( ?[a-zA-Z]{3,}$)+/.test(v) ||
        "Name must contain 3 letters and alphabet only"
    ],
    emailRules: [
      v =>
        !v ||
        /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,3}$/.test(v) ||
        "Invalid E-mail"
    ],
    date: null,
    menu: false,
    passwordVisable: false
  }),
  methods: {
    save(date) {
      this.$refs.menu.save(date);
    },
    register() {
      if(this.username == null || this.password == null || this.name == null || this.email == null) {
        alert("Please fill every cell before registering");
        return;
      }
      if(!this.valid) {
        alert("You have errors in the form");
        return;
      }
      fetch("http://localhost:8081/api/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          username: this.username,
          password: this.password,
          name: this.name,
          email: this.email
        })
      })
        .then(async response => {
          if (response.ok) {
            alert("Registered Successfully !");
              this.$router.push("/LoginPage");
          } else {
            response.json().then(json => {
                alert(response.status + ": " + json.message);
            });
          }
        })
        .catch(err => console.error(err));
    }
  }
};
</script>

<style scoped>
</style>