<template>
  <v-row align="center">
    <v-spacer></v-spacer>
    <v-form ref="form" v-model="valid" :lazy-validation="lazy">
      <v-text-field v-model="username" label="Username" required width="200px"></v-text-field>
      <v-text-field v-model="password" :type="'password'" label="Password" required></v-text-field>
      <v-text-field v-model="name" :rules="nameRules" label="Name" required></v-text-field>
      <v-text-field v-model="email" :type="'email'" :rules="emailRules" label="E-mail" required></v-text-field>

      <v-select
        v-model="roleSelected"
        :items="role"
        :rules="[v => !!v || 'Item is required']"
        label="Role"
        required
      ></v-select>

      <div v-if="roleSelected == 'Player'">
        <v-menu
          ref="menu"
          v-model="menu"
          :close-on-content-click="false"
          transition="scale-transition"
          offset-y
          min-width="290px"
        >
          <template v-slot:activator="{ on }">
            <v-text-field v-model="date" label="Birthday date" readonly v-on="on"></v-text-field>
          </template>
          <v-date-picker
            ref="picker"
            v-model="date"
            :max="new Date().toISOString().substr(0, 10)"
            min="1950-01-01"
            @change="save"
          ></v-date-picker>
        </v-menu>

        <v-select
          v-model="positionSelected"
          :items="position"
          :rules="[v => !!v || 'Item is required']"
          label="Position"
          required
        ></v-select>
      </div>

      <div v-if="roleSelected == 'Coach'">
        <v-textarea name="qualification" rows="1" counter="512" label="Qualification" auto-grow></v-textarea>
      </div>

      <v-btn color="success" class="mr-4" @click="validate">Register</v-btn>
    </v-form>
    <v-spacer></v-spacer>
  </v-row>
</template>

<script>
export default {
  name: "RegisterForm",
  data: () => ({
    emailRules: [
      v =>
        !v ||
        /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/.test(v) ||
        "Invalid E-mail"
    ],
    date: null,
    menu: false,

    roleSelected: {
      role: "Fan"
    },
    postionSelected: {
      position: "GoalKeeper"
    },
    role: ["Fan", "Player", "Coach", "Team Owner"],
    position: [
      "Goalkeeper",
      "Right Fullback",
      "Left Fullback",
      "Center Back",
      "Center Back/Sweeper",
      "Defending/Holding Midfielder",
      "Right Midfielder/Winger",
      "Central/Box-to-Box Midfielder",
      "Striker",
      "Attacking Midfielder/Playmaker",
      "Left Midfielder/Wingers"
    ]
  }),
  watch: {
    menu(val) {
      val && setTimeout(() => (this.$refs.picker.activePicker = "YEAR"));
    }
  },
  methods: {
    emailRules: [
      v =>
        !v ||
        /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/.test(v) ||
        "E-mail must be valid"
    ],
    save(date) {
      this.$refs.menu.save(date);
    },
    register() {
      
    }
  }
};
</script>

<style scoped>
.v-text-field {
  width: 300px;
}
</style>