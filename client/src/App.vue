<template>
  <v-app>
    <v-main>
      <NavBar :title="title" :isLoggedIn="isLoggedIn" />
      <SideBar :title="title" :email="email" :isLoggedIn="isLoggedIn" />
      <router-view />
    </v-main>
  </v-app>
</template>

<script>
// Components
import NavBar from '@/components/NavBar.vue'
import SideBar from '@/components/SideBar.vue'

export default {
  name: 'App',

  components: {
    NavBar,
    SideBar,
  },

  data: () => ({
    title: "Sahachai Plangrit",
    email: "63050197@kmitl.ac.th",
    isLoggedIn: false,
    // import voraphat from '@/components/Voraphat.vue'
  }),

  methods: {
  },

  mounted() {
    // this.axios.get("www.google.com").then(res => console.log("axios ok")).catch(err => console.log("axios error"))
  }
}
</script>
