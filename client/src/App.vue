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
import axios from 'axios'

export default {
  name: 'App',

  components: {
    NavBar,
    SideBar,
  },

  data: () => ({
    //
  }),

  methods: {
  },

  mounted() {
  }
}
</script>
