import Vue from 'vue'
import Router from 'vue-router'
import HomePage from '../views/HomePage.vue'
import RegisterPage from '../views/RegisterPage.vue'
import LoginPage from '../views/LoginPage.vue'
import TeamOwnerPage from '../views/TeamOwnerPage.vue'
import NewTeamPage from '../views/NewTeamPage.vue'
import GameManagePage from '../views/GameManagePage.vue'
import LeagueOptionsPage from '../views/LeagueOptionsPage.vue'
import WelcomePage from '../views/WelcomePage.vue'
import notificationPage from '../views/notificationPage.vue'
import TeamOwnerRequestsPage from '../views/TeamOwnerRequestsPage.vue'

Vue.use(Router)

export default new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    {
      path: '/HomePage',
      name: 'HomePage',
      component: HomePage
    },
    {
      path: '/about',
      name: 'about',
      component: () => import('../views/About.vue')
    },
    {
      path: '/RegisterPage',
      name: 'RegisterPage',
      component: RegisterPage
    },
    {
      path: '/LoginPage',
      name: 'LoginPage',
      component: LoginPage
    },
    {//set path for team owner page
      path: '/TeamOwnerPage',
      name: 'TeamOwnerPage',
      component: TeamOwnerPage
    },
    {//set path for new team page
      path: '/NewTeamPage',
      name: 'NewTeamPage',
      component: NewTeamPage
    }, {//set path for team owner page
      path: '/GameManagePage',
      name: 'GameManagePage',
      component: GameManagePage
    },
    {//set path for new team page
      path: '/LeagueOptionsPage',
      name: 'LeagueOptionsPage',
      component: LeagueOptionsPage
    },
    {//set path for new team page
      path: '/WelcomePage',
      name: 'WelcomePage',
      component: WelcomePage
    },
    {//set path for new team page
      path: '/notificationPage',
      name: 'notificationPage',
      component: notificationPage
    },
    {//set path for new team page
      path: '/TeamOwnerRequestsPage',
      name: 'TeamOwnerRequestsPage',
      component: TeamOwnerRequestsPage
    },
  ]
})