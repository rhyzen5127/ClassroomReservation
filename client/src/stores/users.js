import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import axios from '../plugins/axios'

export const useUserStore = defineStore('users', () => {

  const currentUser = ref(null)

  function addBearerAuthHeader(headers, token) {
    headers["Authorization"] = "Bearer " + token
    return headers
  }

  async function fetchCurrentUser(token) {
    let headers = addBearerAuthHeader({}, token)
    currentUser.value = null
    let res = await axios.get("/users/current", { headers } )
    currentUser.value = res.data
    return res.data
  }

  async function login(username, password) {
    let headers = {
        "Content-Type": "multipart/form-data"
    }
    let res = await axios.post("/login", { username, password }, { headers } )
    let token = res.data.token
    await fetchCurrentUser(token)
    return res.data.token

  }

  return { currentUser, login, fetchCurrentUser, addBearerAuthHeader }
})
