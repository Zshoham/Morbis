<template>
  <div>Welcome to home page of logined users</div>
</template>

<script>
// @ is an alias to /src

export default {
  name: "HomePage",
  mounted() {
    if (this.$root.userToken == "" || this.$root.userToken == null) {
        //not logged in so send him to welcome page
        this.$router.push("/WelcomePage");
      }
  },
};
</script>
