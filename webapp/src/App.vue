<template>
  <v-app>
    <!--<v-app-bar dark>-->
      <ourToolBar/>
  
      <v-spacer></v-spacer>
    <!--</v-app-bar>-->

    <v-content>
      <!--<HelloWorld />-->
    </v-content>
  </v-app>
</template>

<script>
//import HelloWorld from "./components/HelloWorld";
import ourToolBar from "./components/Toolbar";

export default {
  name: "App",

  components: {
    //HelloWorld,
    ourToolBar
  },

  data: () => ({
    //
  })
};
</script>
