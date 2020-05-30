import Vue from 'vue'
import App from './App.vue'
import router from './router'
import vuetify from './plugins/vuetify';

//router.replace({ path: '/WelcomePage', redirect: '/' })
Vue.config.productionTip = false
Vue.userName = "";
Vue.userToken = "";
Vue.roles = [];
Vue.userID = -1;
Vue.client = {};
Vue.notificationCount={value:1};

new Vue({
  App,
  router,
  vuetify,
  render: h => h(App)
}).$mount('#app')
