import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import axios from '../plugins/axios'

export const useUserStore = defineStore('users', () => {

  function addBearerAuthHeader(headers, token) {
    headers["Authentication"] = "Bearer " + token
    return headers
  }

  async function fetchCurrentUser(token) {
    let headers = addBearerAuthHeader({}, token)
    let res = await axios.post("/users/current", { username, password }, { headers } )
    return res.data
  }

  async function login(username, password) {
    let headers = {
        "Content-Type": "multipart/form-data"
    }
    let res = await axios.post("/login", { username, password }, { headers } )
    return res.data

  }

  return { login, fetchCurrentUser }
})
