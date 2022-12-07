<template>
  <v-alert title="Alert title" text="..." type="success"></v-alert>
</template>

<script>
import { defineComponent } from 'vue';

// Components
import HelloWorld from '../components/HelloWorld.vue';
import NavBar from '../components/NavBar.vue'

export default defineComponent({
  name: 'HomeView',

  components: {
    HelloWorld,
    NavBar,
  },
});
</script>
