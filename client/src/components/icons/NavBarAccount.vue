<template>
  <div>
    <v-btn height="80" color="white" to="/login">
      <v-list-item 
        title="Login"/>
    </v-btn>
    <!-- <v-btn height="80" color="white">
      <v-list-item 
        prepend-avatar="https://scontent.fbkk22-8.fna.fbcdn.net/v/t39.30808-6/319666732_871747604173254_5992482641832258112_n.jpg?_nc_cat=104&ccb=1-7&_nc_sid=09cbfe&_nc_eui2=AeG4C-Ud-71lDi6YjgDu_-a_B1ZB0DiUz5QHVkHQOJTPlCALVb6HSgzoaSktfNmyw09o_kDBzsZ5QDUQNIqepA1V&_nc_ohc=O196nff9f_wAX-364Q4&_nc_ht=scontent.fbkk22-8.fna&oh=00_AfBY0Ad6Ju9dHk10Hzhnktejd18KISa7f6c7XkWv-aQ__g&oe=639DA7AC"
        :title="title"
        subtitle="Logout"/>
    </v-btn> -->
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
  },

  data: () => ({

  })
}
</script>