import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('../views/Home.vue')
    },
    {
      path: '/manual',
      name: 'manual',
      component: () => import('../views/Manual.vue')
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
    },
    {
      path: '/report',
      name: 'report',
      component: () => import('../views/Report.vue')
    },  
  ]
})

export default router
