<template>
    <NavBar>
    </NavBar>
    <SideBar>
    </SideBar>
  </template>
  
  <script>
  import { defineComponent } from 'vue';
  
  // Components
  import NavBar from '../components/NavBar.vue'
  import SideBar from '../components/SideBar.vue'
  
  export default defineComponent({
    name: 'Reservation',
  
    components: {
      NavBar,
      SideBar,
    },
  });
  </script>
  