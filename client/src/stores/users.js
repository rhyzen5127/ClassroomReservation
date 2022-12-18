import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import axios from '../plugins/axios'

export const useUserStore = defineStore('users', () => {

  const currentUser = ref({})
  const currentUserId = computed(() => {
    return currentUser.value.id
  })

  function addBearerAuthHeader(headers, token) {
    headers["Authentication"] = "Bearer " + token
    return headers
  }

  async function fetchCurrentUser(token) {
    let headers = addBearerAuthHeader({}, token)
    let res = await axios.get("/users/current", { headers } )
    currentUser.value = res.data
    return res.data
  }

  async function login(username, password) {
    let headers = {
        "Content-Type": "multipart/form-data"
    }
    let res = await axios.post("/login", { username, password }, { headers } )
    currentUser.value = res.data
    return res.data

  }

  return { currentUser, currentUserId, login, fetchCurrentUser, addBearerAuthHeader }
})
