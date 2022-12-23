<template>
  
  <div>
    <v-app-bar color="#ff7a00" prominent>
      <v-toolbar-title>
        <v-img :src="logoPath" max-width="250" max-height="48"/>
      </v-toolbar-title>

      <v-spacer>
      </v-spacer>

      <!-- <v-btn color="white" icon="mdi-calendar" to="/history"/>

      <v-badge :content="69240" color="white"><v-btn color="white" icon="mdi-bell"/></v-badge> -->
      

      <v-btn color="white" to="/about" icon="mdi-exclamation-thick"/>

      <NavBarAccount :title="this.title" :isLoggedIn="this.isLoggedIn"></NavBarAccount>

    </v-app-bar>

    <!--
    <v-navigation-drawer v-model="drawer" location="bottom" temporary>
      <v-list :items="items"/>
    </v-navigation-drawer>
    -->
  </div>
</template>

<script>

import NavBarAccount from './icons/NavBarAccount.vue';

export default {
  props: {
    title: {
      type: String,
      require: true,
      default: "",
    },
    isLoggedIn: {
      type: Boolean,
      require: true,
      default: false,
    }
  },

  components: {
    NavBarAccount
  },

  data: () => ({
    logoPath: new URL("@/assets/images/sci-kmitl-logo.png", import.meta.url).href,
  }),

  methods: {
    onClickRedirect2Home() {
      window.location.href = '/'
    }
  }
}
</script>

<style scoped>
.hover-logo {
  cursor: pointer;
}
</style>