import { ref } from 'vue'
import { defineStore } from 'pinia'
import axios from '../plugins/axios'

export const useUserStore = defineStore('users', () => {

  const currentUser = ref(null)

  function addBearerAuth(token, options) {
    options = options || {}
    options.headers = options.headers || {}
    if (token) options.headers["Authorization"] = "Bearer " + token
    return options
  }

  async function fetchCurrentUser(token) {
    currentUser.value = null
    let options = addBearerAuth(token, {})
    let res = await axios.get("/users/current", options )
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

  return { currentUser, login, fetchCurrentUser, addBearerAuth }
})
