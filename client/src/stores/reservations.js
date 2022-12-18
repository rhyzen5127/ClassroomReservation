import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { useUserStore } from './users.js'
import axios from '../plugins/axios'

export const useReservationStore = defineStore('reservations', () => {

  const userStore = useUserStore()
  const reservations = ref([])

  async function fetchAll() {
    let res = await axios.get('/reservations')
    reservations.value = res.data
    return reservations.value
  }

  async function fetchById(id) {
    let res = await axios.get('/reservation/' + id)
    return res.data
  }

  async function fetchUserReserved(token) {
    let user = await userStore.fetchCurrentUser(token)
    if (!user) return []
    let res = await axios.get('/users/' + user.id + '/reservations', userStore.addBearerAuthHeader({}, token))
    return res.data
  }

  async function reserve(token, roomId, startTime, finishTime) {
    await axios.post('/reservations', { roomId, startTime, finishTime }, userStore.addBearerAuthHeader({}, token))
  }

  return { fetchAll, fetchById, fetchUserReserved, reserve }
})
