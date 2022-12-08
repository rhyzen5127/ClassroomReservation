<script setup>
import NavBarAccount from './icons/NavBarAccount.vue';
</script>

<template>
  <v-card >
    <v-layout>
      <v-app-bar
        color="#ff7a00"
        prominent
        >

        <v-toolbar-title > 
            <v-toolbar-title>
              <v-img
                src="../images/sci-kmitl-logo.png"
                max-width="250"
                max-height="48"
                >
                <v-btn height="64" width="256">
                </v-btn>
              </v-img>
            </v-toolbar-title>
        </v-toolbar-title>

        <v-spacer>
        </v-spacer>
        
        <v-btn color="white" icon="mdi-calendar"></v-btn>

        <v-btn color="white" icon="mdi-bell"></v-btn>

        <NavBarAccount></NavBarAccount>

      </v-app-bar>

      <v-navigation-drawer
        v-model="drawer"
        location="bottom"
        temporary
      >
        <v-list
          :items="items"
        ></v-list>
      </v-navigation-drawer>

      <v-main>
      </v-main>
    </v-layout>
  </v-card>
</template>
  <script>
    export default {
      //
    }
  </script>