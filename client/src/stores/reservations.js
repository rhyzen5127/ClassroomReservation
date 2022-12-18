import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { useUserStore } from './users.js'
import axios from '../plugins/axios'

export const useReservationStore = defineStore('reservations', () => {

  const userStore = useUserStore()
  const reservations = ref([])


  async function fetchById(id) {
    let res = await axios.get('/reservation/' + id)
    return res.data
  }

  /*
  params = {
    status: "pending" | "approved" | "rejected" | "canceled",
    minReserveTime: Date,
    maxReserveTime: Date,
    limit: Integer
  } 
  */
  async function fetchAll(params) {
    let res = await axios.get('/reservations', { params })
    reservations.value = res.data
    return reservations.value
  }

  async function fetchFromBuilding(buildingId, params) {
    let res = await axios.get(`/buildings/${buildingId}/reservations`, { params })
    return res.data
  }

  async function fetchFromClassroom(roomId, params) {
    let res = await axios.get(`/classrooms/${roomId}/reservations`, { params })
    return res.data
  }

  async function fetchUserReserved(token, params) {

    let user = await userStore.fetchCurrentUser(token)
    if (!user) return []

    let headers = userStore.addBearerAuthHeader({}, token)
    let res = await axios.get('/users/' + user.id + '/reservations', { headers, params })
    return res.data

  }

  async function reserve(token, roomId, startTime, finishTime) {
    let headers = userStore.addBearerAuthHeader({}, token)
    await axios.post('/reservations', { roomId, startTime, finishTime }, { headers })
  }

  async function approve(token, reservationId) {
    let headers = userStore.addBearerAuthHeader({}, token)
    await axios.post(`/reservations/${reservationId}/approve`, {}, { headers })
  }

  async function reject(token, reservationId) {
    let headers = userStore.addBearerAuthHeader({}, token)
    await axios.post(`/reservations/${reservationId}/reject`, {}, { headers })
  }

  async function deleteReservation(token, reservationId) {
    let headers = userStore.addBearerAuthHeader({}, token)
    await axios.delete(`/reservations/${reservationId}`, { headers })
  }

  return { fetchAll, fetchFromBuilding, fetchFromClassroom, fetchById, fetchUserReserved, reserve, approve, reject, deleteReservation }
})
