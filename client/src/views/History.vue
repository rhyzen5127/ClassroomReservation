<template>
  <div class="mt-10 mx-10">
    <v-row>

      <v-col cols="12" md="6">
        <div class="text-h6 mb-1">
          เร็ว ๆ นี้
        </div>
        <v-card class="overflow-y-auto" max-height="650">
        <div v-for="i in nReservation" :key="i + '-classCard'" class="my-4">
          <v-card >
            <ClassroomCard editable=true width="700"/>
          </v-card>
        </div>
      </v-card>
      </v-col>

      <v-col cols="11" md="5">
        <EventCalendar width="700"/>
      </v-col>
      
      
    </v-row>
  </div>
</template>
  
<script>
import ClassroomCard from '@/components/ClassroomCard.vue'
import EventCalendar from '@/components/EventCalendar.vue'
import { defineComponent } from 'vue';
import { useReservationStore } from '@/stores/reservations.js'

// Components

export default defineComponent({
  data: () => ({
    nReservation: 1,
    loading: false,
    userReservations: []
  }),

  methods: {
    fetchUserReservation() {

      let token = localStorage.cookie
      if (!token) return

      this.loading = true
      this.reservationStore.fetchUserReserved(token).then(res => {
        console.log(res)
        this.userReservations = res
        this.loading = false
      }).catch(err => {
        console.error(err)
        this.userReservations = []
        this.loading = false
      })
    }
  },

  setup() {
    return {
      reservationStore: useReservationStore()
    }
  },

  name: 'History',

  components: {
    ClassroomCard,
    EventCalendar,
  },

  mounted() {
    this.fetchUserReservation()
  }
});
</script>
  