<template>
  <div>
    <div v-if="this.isLoggedIn">
      <v-btn height="80" style="boxShadow: none;" class="d-flex ma-auto">
        <v-list class="d-flex">
              <v-avatar color="red">
                <span class="text-h6">
                  {{ nameFirstChar }}
                </span>
              </v-avatar>
              <v-list-item :title="this.title" :email="this.email" />

        </v-list>
      </v-btn>
    </div>
    <div v-else>
      <v-btn width="512" style="boxShadow: none;" color="white" to="/login">
        <v-list-item title="Login" />
      </v-btn>
    </div>
    <hr />
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
    email: {
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