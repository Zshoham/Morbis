<template>
  <div class="my-10">
    <ChangeLeagueOptions />
  </div>
</template>

<script>
// @ is an alias to /src
import ChangeLeagueOptions from "@/components/ChangeLeagueOptions.vue";

export default {
  name: "LeagueOptionsPage",
  mounted() {
    if (this.$root.userToken == "" || this.$root.userToken == null) {
      //he's not logged in
      this.$router.push("/WelcomePage");
    } else if (!this.$root.roles.includes("Association_Rep")) {
      //he doesn't have permissions
      this.$router.push("/HomePage");
    }
  },
  components: {
    ChangeLeagueOptions
  }
};
</script>
