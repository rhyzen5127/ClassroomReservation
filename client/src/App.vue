<template>
  <v-app>
    <v-main>
      <NavBar :title="title" :isLoggedIn="isLoggedIn" />
      <SideBar :title="title" :email="email" :role="role" :isLoggedIn="isLoggedIn" />

      <router-view :key="$route.path" />
    </v-main>
  </v-app>
</template>

<script>
// Components
import NavBar from '@/components/NavBar.vue'
import SideBar from '@/components/SideBar.vue'
import { useUserStore } from '@/stores/users.js'

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

    fetchUserData() {

      this.isLoggedIn = false

      let userToken = localStorage.getItem("cookie")

      if (userToken) {

        this.userStore.fetchCurrentUser(userToken)
        .then(res => {
          this.title = res.firstName + " " + res.lastName
          this.email = res.email
          this.role = res.role
          this.isLoggedIn = true
        })
        .catch(() => {
          localStorage.removeItem("cookie")
          this.$router.replace("/")
        })

      }
      
    }

  },

  mounted() {
    this.fetchUserData()
  }
}
</script>
