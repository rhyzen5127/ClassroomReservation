<template>
  <v-app>
    <v-main>
      <NavBar :title="title" :isLoggedIn="isLoggedIn" />
      <SideBar :title="title" :email="email" :role="role" :isLoggedIn="isLoggedIn" />

      <router-view />
    </v-main>
  </v-app>
</template>

<script>
// Components
import NavBar from '@/components/NavBar.vue'
import SideBar from '@/components/SideBar.vue'
import { useUserStore } from '@/stores/users.js'
import { isThursday } from 'date-fns'

export default {
  name: 'App',

  components: {
    NavBar,
    SideBar,
  },

  data: () => ({
    title: "Guest Guest",
    email: "Guest@kmitl.ac.th",
    role: "null",
    isLoggedIn: false,
  }),

  computed: {
  },

  setup() {
    return {
      userStore: useUserStore()
    }
  },

  methods: {
    setInfoFromStorage() {
      this.isLoggedIn = false
      let userToken = localStorage.getItem("cookie")
      if (userToken) {
        this.userStore.fetchCurrentUser(userToken).then(res => {
          this.title = res.firstName + " " + res.lastName
          this.email = res.email
          this.role = res.role
          this.isLoggedIn = true
        }).catch(err => {
          localStorage.setItem("cookie", null)
          console.error(err)
        })
      }
    }
  },

  mounted() {
    this.setInfoFromStorage();
  }
}
</script>
