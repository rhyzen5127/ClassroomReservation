import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/Home.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/about',
      name: 'about',
      component: () => import('../views/About.vue')
    },
    {
      path: '/reservation',
      name: 'reservation',
      component: () => import('../views/Reservation.vue')
    },
    {
      path: '/history',
      name: 'history',
      component: () => import('../views/History.vue')
    },
    {
      path: '/map',
      name: 'map',
      component: () => import('../views/Map.vue')
    },
    {
      path: '/managereservations',
      name: 'managereservations',
      component: () => import('../views/ManageReservations.vue')
    },
    {
      path: '/manageclassroomdetails',
      name: 'manageclassroomdetails',
      component: () => import('../views/ManageClassroomDetails.vue')
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/Login.vue')
    }
  ]
})

export default router
