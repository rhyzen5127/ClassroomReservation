<template>
  <div>
    <div v-if="this.isLoggedIn">
      <v-btn height="80" color="white" @click="logout">
        <v-list-item :title="splitTitle()" subtitle="Logout"/>

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
  
  methods: {
    logout() {
      localStorage.removeItem("cookie");
      window.location.href = '/'
    },

    splitTitle() {
      let title = this.title
      if(title.length > 15) return title.substring(0, 15) + "..."
      else return title
    },
  }, 

  data: () => ({
    loading: false,
    nameFirstChar: null,
  }),

  watch: {
    isLoggedIn() {
      let subname = this.title.split(" ")
      this.nameFirstChar = subname[0][0] + subname[1][0]
    }
  },

  mounted() {
  }
}
</script>