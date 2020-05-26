import Vue from 'vue'
import Router from 'vue-router'
import Home from '../views/Home.vue'
import RegisterPage from '../views/RegisterPage.vue'
import LoginPage from '../views/LoginPage.vue'
import GameManagePage from '../views/GameManagePage.vue'
import LeagueOptionsPage from '../views/LeagueOptionsPage.vue'

Vue.use(Router)

export default new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home
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
    {
      path: '/GameManagePage',
      name: 'GameManagePage',
      component: GameManagePage
    },
    {
      path: '/LeagueOptionsPage',
      name: 'LeagueOptionsPage',
      component: LeagueOptionsPage
    },
  ]
})