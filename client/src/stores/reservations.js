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
    let options = { params } 
    let res = await axios.get('/users/' + user.id + '/reservations', options)
    return res.data
  }

  async function reserve(token, roomId, startTime, finishTime, reserveNote) {
    let options = userStore.addBearerAuth(token, {}) 
    await axios.post('/reservations', { roomId, startTime, finishTime, reserveNote }, options)
  }

  async function approve(token, reservationId, reason) {
    let options = userStore.addBearerAuth(token, {}) 
    await axios.post(`/reservations/${reservationId}/approve`, { reason }, options)
  }

  async function reject(token, reservationId, reason) {
    let options = userStore.addBearerAuth(token, {}) 
    await axios.post(`/reservations/${reservationId}/reject`, { reason }, options)
  }

  async function deleteReservation(token, reservationId) {
    let options = userStore.addBearerAuth(token, {}) 
    await axios.delete(`/reservations/${reservationId}`, options)
  }

  return { fetchAll, fetchFromBuilding, fetchFromClassroom, fetchById, fetchUserReserved, reserve, approve, reject, deleteReservation }
})
