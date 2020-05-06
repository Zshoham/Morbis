/*import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import About from '../views/About.vue'
import RegisterPage from '../views/RegisterPage.vue'

Vue.use(VueRouter)

const routerRoutes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/',
    name: 'RegisterPage',
    component: RegisterPage
  },
  {
    path: '/About',
    name: 'About',
    component: About
  }
]
const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: routerRoutes 
})

export default router
*/

import Vue from 'vue'
import Router from 'vue-router'
import Home from '../views/Home.vue'
import RegisterPage from '../views/RegisterPage.vue'
import LoginPage from '../views/LoginPage.vue'

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
      component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
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
  ]
})