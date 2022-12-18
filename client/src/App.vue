<template>
  <v-app>
    <v-main>
      <NavBar :title="title" :isLoggedIn="isLoggedIn" />
      <SideBar :title="title" :email="email" :isLoggedIn="isLoggedIn" />
      
      <router-view/>
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
    isLoggedIn: false,
  }),
  
  computed: {
  },

  setup() {
    const userStore = useUserStore()
    return {
      userStore
    }
  },

  methods: {
    setInfoFromStorage(){
      if (localStorage.fname && localStorage.lname) {
        this.title = localStorage.fname + " " + localStorage.lname;
      }
      if (localStorage.getItem("email")) {
        this.email = localStorage.getItem("email")
      }
      this.isLoggedIn = localStorage.getItem("cookie") != null ? true : false
    }
  },

  mounted() {
    this.setInfoFromStorage();
  }
}
</script>
