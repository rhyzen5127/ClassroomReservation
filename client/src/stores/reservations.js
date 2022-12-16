import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import axios from '../plugins/axios'

export const useReservationStore = defineStore('reservations', () => {
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

  return { count, doubleCount, increment }
})
