<template>
  <div>
    <div v-if="this.isLoggedIn">
      <v-btn height="80" width="255" style="boxShadow: none;" class="d-flex ma-auto" @click="this.logout">
        <v-list>
          <v-row>
            <v-col cols="12" md="3">
              <v-avatar color="red" class="mt-1">
                <span class="text-h6">
                  {{ nameFirstChar }}
                </span>
              </v-avatar>
            </v-col>
            <v-col cols="12" md="9" class="justify-center">
              <v-card variant="">
                <h4 class="d-flex justify-center">
                  {{ splitTitle() }}
                </h4>
                <h5 class="d-flex justify-center">
                  {{ splitEmail() }}
                </h5>
                <h6 class="text-grey mt-1">
                  Logout
                </h6>
              </v-card>
            </v-col>
          </v-row>



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

  watch: {
    isLoggedIn() {
      let subname = this.title.split(" ")
      this.nameFirstChar = subname[0][0] + subname[1][0]
    }
  },

  methods: {
    logout() {
      localStorage.removeItem("cookie");
      localStorage.removeItem("fname");
      localStorage.removeItem("lname");
      localStorage.removeItem("email");
      localStorage.removeItem("role");
      this.loading = false
      window.location.href = '/'
    },

    splitTitle() {
      let title = this.title
      if(title.length > 10) return title.substring(0, 10) + "..."
      else return title
    },
    
    splitEmail() {
      let email = this.email
      if(email.length > 15) return email.substring(0, 15) + "..."
      else return email
    }
  },
}
</script>