<template>
  <div>
    <div v-if="this.isLoggedIn">
      <v-btn height="80" color="white">
        <v-list-item :title="this.title" subtitle="Logout" />
        <v-avatar color="red">
          <span class="text-h6">
            {{ nameFirstChar }}
          </span>
        </v-avatar>
      </v-btn>
    </div>
    <div v-else>
      <v-btn height="80" color="white" to="/login">
        <v-list-item title="Login" />
      </v-btn>
    </div>


  </div>
</template>

<script>
import { onMounted } from 'vue';

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

  data: () => ({
    nameFirstChar: null,
  }),

  mounted() {
    if(this.isLoggedIn){
      let subname = this.title.split(" ")
      this.nameFirstChar = subname[0][0] + subname[1][0]
    }
  }
}
</script>