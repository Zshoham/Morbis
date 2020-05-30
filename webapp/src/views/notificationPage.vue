<template>
  <notificationPage/>
</template>

<script>
// @ is an alias to /src
import notificationPage from '@/components/notificationDisplay.vue'
export default {
    components: {
    notificationPage
  },
  name: "HomePage",
  mounted() {
    if (this.$root.userToken == "" || this.$root.userToken == null) {
        //not logged in so send him to welcome page
        this.$router.push("/WelcomePage");
      }
  },
};
</script>