<template>
  <v-app id="morbis">
    <v-navigation-drawer v-model="drawer" :clipped="$vuetify.breakpoint.lgAndUp" app>
      <v-list dense>
        <template v-for="item in items">
          <v-row v-if="item.heading" :key="item.heading" align="center">
            <v-col cols="6">
              <v-subheader v-if="item.heading">{{ item.heading }}</v-subheader>
            </v-col>
            <v-col cols="6" class="text-center">
              <a href="#!" class="body-2 black--text">EDIT</a>
            </v-col>
          </v-row>
          <v-list-group
            v-else-if="item.children"
            :key="item.text"
            v-model="item.model"
            :prepend-icon="item.model ? item.icon : item['icon-alt']"
            append-icon
            :hidden="item.hidden"
            :aria-pressed="false"
          >
            <template v-slot:activator>
              <v-list-item-content>
                <v-list-item-title>{{ item.text }}</v-list-item-title>
              </v-list-item-content>
            </template>
            <v-list-item v-for="(child, i) in item.children" :key="i" link :to="child.to"> 
              <v-list-item-content>
                <v-list-item-title :link="child.link">{{ child.text }}</v-list-item-title>
              </v-list-item-content>
              <v-list-item-action v-if="child.icon">
                <v-icon>{{ child.icon }}</v-icon>
              </v-list-item-action>
            </v-list-item>
          </v-list-group>
          <v-list-item v-else :key="item.text" :hidden="item.hidden" link :to="item.to">
              <v-icon>{{ item.icon }}</v-icon>
            <v-list-item-content>
              <v-list-item-title>{{ item.text }}</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </template>
      </v-list>
    </v-navigation-drawer>

    <v-app-bar :clipped-left="$vuetify.breakpoint.lgAndUp" app color="primary">
      <v-app-bar-nav-icon @click.stop="drawer = !drawer" />
      <v-toolbar-title style="width: 300px" class="ml-0 pl-4">
        <!-- <v-img :src="require('../src/assets/morbis-logo.svg')" contain height="50px" width="50px"></v-img> -->
        <span class="hidden-sm-and-down">Morbis</span>
      </v-toolbar-title>
      <v-text-field
        flat
        solo-inverted
        hide-details
        prepend-inner-icon="mdi-magnify"
        label="Search"
        class="hidden-sm-and-down"
      />
      <v-spacer />
      <v-btn color="black" class="white--text" style="margin:10px" to="/">Home</v-btn>
      <v-btn
        color="black"
        class="white--text"
        style="margin:10px"
        to="/about"
      >About</v-btn>
      <v-btn color="black" class="white--text" style="margin:10px" to="/RegisterPage">Register</v-btn>
      <v-btn color="black" class="white--text" style="margin:10px;" to="/LoginPage">Login</v-btn>
      <v-btn
        color="black"
        class="white--text"
        style="margin:10px;"
        @click="setMenu()"
        to="/TeamOwnerPage"
      >Team Owner</v-btn>
      <v-btn
        color="black"
        class="white--text"
        style="margin:10px"
        @click="setMenu(['TEAMOWNER','PLAYER'])"
      >test1</v-btn>
      <v-btn
        color="black"
        class="white--text"
        style="margin:10px"
        @click="setMenu()"
      >test2</v-btn>
      <v-btn
        color="black"
        class="white--text"
        style="margin:10px"
        @click="toggleAuthorisation()"
      >test3</v-btn>
      <v-spacer />
      <v-btn icon>
        <v-icon>mdi-apps</v-icon>
      </v-btn>
      <v-btn icon>
        <v-icon>mdi-bell</v-icon>
      </v-btn>
      <v-btn icon large>
        <v-avatar size="32px" item>
          <v-img src="https://cdn.vuetifyjs.com/images/logos/logo.svg" alt="Morbis" />
        </v-avatar>
      </v-btn>
    </v-app-bar>
    <v-content>
      <router-view />
      <!-- content here -->
    </v-content>
  </v-app>
</template>

<script>
var authorised=true;
var fanMenu = {
  icon: "mdi-chevron-up",
  "icon-alt": "mdi-chevron-down",
  text: "My Account",
  model: false,
  hidden: false,
  children: [
    {text: "Players", icon:"mdi-run-fast"},
    {text: "Teams", icon:"mdi-account-group"},
    {text: "Games", icon:"mdi-soccer"},
    {text: "Seasons", icon:"mdi-medal-outline"},
    {text: "Leages", icon:"mdi-trophy"},
    {
      text: "New team",
      icon: "mdi-account-multiple-plus",
      to: "/NewTeamPage",
    }
  ]
};
var coachMenu = {
  icon: "mdi-chevron-up",
  "icon-alt": "mdi-chevron-down",
  text: "Coach",
  model: false,
  hidden: true,
  children: [
    {
      text: "New team",
      to: "/NewTeamPage",
    }
  ]
};
var refereeMenu = {
  icon: "mdi-chevron-up",
  "icon-alt": "mdi-chevron-down",
  text: "Referee",
  model: false,
  hidden: true,
  children: [
    {
      text: "New team",
      to: "/NewTeamPage",
    }
  ]
};
var playerMenu = {
  icon: "mdi-chevron-up",
  "icon-alt": "mdi-chevron-down",
  text: "Player",
  model: false,
  hidden: true,
  children: [
    {
      text: "New team",
      to: "/NewTeamPage",
    }
  ]
};
var assRepMenu = {
  icon: "mdi-chevron-up",
  "icon-alt": "mdi-chevron-down",
  text: "Association Representitive",
  model: false,
  hidden: true,
  children: [
    {
      text: "New team",
      to: "/NewTeamPage",
    }
  ]
};
var teamManagerMenu = {
  icon: "mdi-chevron-up",
  "icon-alt": "mdi-chevron-down",
  text: "Team Manager",
  model: false,
  hidden: true,
  children: [
    {
      text: "New team",
      to: "/NewTeamPage",
    }
  ]
};
var teamOwnerMenu = {
  icon: "mdi-chevron-up",
  "icon-alt": "mdi-chevron-down",
  text: "Team Owner",
  model: false,
  hidden: true,
  children: [
    { text: "Add/remove assets" },
    { text: "Edit assets" },
    { text: "Report transaction" },
    { text: "Appoint new owner" },
    { text: "Remove owner" },
    { text: "Appoint new manager" },
    { text: "Remove manager" },
    {
      text: "New team",
      to: "/NewTeamPage",
    },
    { text: "Reopen team" },
    { text: "Close team" }
  ]
};
var adminMenu = {
  icon: "mdi-chevron-up",
  "icon-alt": "mdi-chevron-down",
  text: "Admin",
  model: false,
  hidden: true,
  children: [
    { text: "Add/remove users" }
  ]
};
var menus = [];
  menus['FAN']= fanMenu;
  menus['PLAYER']= playerMenu;
  menus['COACH']= coachMenu;
  menus['REFEREE']= refereeMenu;
  menus['TEAMMANAGER']= teamManagerMenu;
  menus['TEAMOWNER']= teamOwnerMenu;
  menus['ASSOCIATIONREPRESENTITIVE']= assRepMenu;
  menus['ADMIN']= adminMenu;
var menu = [
  { icon: "mdi-home", text: "\t Home", to:"/Home"},
  // {
  //   icon: "mdi-chevron-up",
  //   "icon-alt": "mdi-chevron-down",
  //   text: "More",
  //   model: false,
  //   children: [
  //     { text: "Import" },
  //     { text: "Export" },
  //     { text: "Print" },
  //     { text: "Undo changes" },
  //     { text: "Other contacts" }
  //   ]
  // },
  fanMenu,
  playerMenu,
  refereeMenu,
  coachMenu,
  teamManagerMenu,
  teamOwnerMenu,
  assRepMenu,
  { icon: "mdi-cog", text: "\t Settings", to: "/Settings"},
  {
  text: "\t Switch Accounts",
  icon: "mdi-account-convert",
  to: "/LoginPage"
  },{
  text: "\t Logout",
  icon: "mdi-logout",
  to: "/logout"
  }
];
export default {
  props: {
    source: String
  },
  data: () => ({
    drawer: null,
    items: menu
  }),
  methods: {
    remove: function(arr) {
      var what,
        a = arguments,
        L = a.length,
        ax;
      while (L > 1 && arr.length) {
        what = a[--L];
        while ((ax = arr.indexOf(what)) !== -1) {
          arr.splice(ax, 1);
        }
      }
    },
    toggleAuthorisation: function(){authorised = !authorised; window.alert(authorised)},
    setMenu: function(roles){
      if(!authorised) return;
      //hide every menu
      menus['PLAYER'].hidden = true;
      menus['COACH'].hidden = true;
      menus['REFEREE'].hidden = true;
      menus['TEAMMANAGER'].hidden = true;
      menus['TEAMOWNER'].hidden = true;
      menus['ASSOCIATIONREPRESENTITIVE'].hidden = true;
      menus['ADMIN'].hidden = true;
     // while(allRoles!=null){alert(allRoles);menus[allRoles].hidden=true; allRoles.next()}
      //show only the relevent menus
      roles.forEach(role => {menus[role].hidden=false});
    }
  }
};
</script>