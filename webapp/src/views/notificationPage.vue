<template>
  <div class="my-10">
    <notificationDisplay />
  </div>
</template>

<script>
// @ is an alias to /src
import notificationDisplay from "@/components/notificationDisplay.vue";
export default {
  components: {
    notificationDisplay
  },
  name: "HomePage",
  mounted() {
    if (this.$root.userToken == "" || this.$root.userToken == null) {
      //not logged in so send him to welcome page
      this.$router.push("/WelcomePage");
    }
  }
};
</script>