<template>
  <v-app>
    <v-main>
      <NavBar/>
      <SideBar/>
      <router-view/>
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
    // import voraphat from '@/components/Voraphat.vue'
  }),

  methods: {
  },

  mounted() {
    // this.axios.get("www.google.com").then(res => console.log("axios ok")).catch(err => console.log("axios error"))
  }
}
</script>
