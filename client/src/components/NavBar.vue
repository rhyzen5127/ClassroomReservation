<script setup>
import NavBarAccount from './icons/NavBarAccount.vue';
</script>

<template>
  <div>
    <v-app-bar color="#ff7a00" prominent>
      <v-toolbar-title>3
        <v-img :src="logoPath" max-width="250" max-height="48"/>
      </v-toolbar-title>

      <v-spacer>
      </v-spacer>

      <v-btn color="white" icon="mdi-calendar"/>

      <v-btn color="white" icon="mdi-bell"/>

      <v-btn color="white" to="/about" icon="mdi-exclamation-thick"/>

      <NavBarAccount title="Rhyzen MK"></NavBarAccount>

    </v-app-bar>

    <v-navigation-drawer v-model="drawer" location="bottom" temporary>
      <v-list :items="items"/>
    </v-navigation-drawer>
  </div>
</template>
<script>
export default {
  data: () => ({
    logoPath: new URL("@/assets/images/sci-kmitl-logo.png", import.meta.url).href,
  }),

  methods: {
    onClickRedirect2Home() {
      this.$router.push({ name: 'home' })
    }
  }
}
</script>

<style scoped>
.hover-logo {
  cursor: pointer;
}
</style>