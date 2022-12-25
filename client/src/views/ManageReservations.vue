<template>
  <div class="mt-10 mx-10">
    <div class="text-h6 mb-1">
      จัดการคำขอจอง
    </div>
    <v-card class="overflow-y-auto" max-height="650" max-width="1200">
      <div v-for="i in userReservations" :key="i + '-classCard'" class="my-4">
        <ClassroomCard 
          :reservation="i"
          :width="700" class="my-5"
          showOwner managable showReserveNote
          @approve="approveReservation"
          @reject="rejectReservation"
          />
      </div>
    </v-card>
  </div>
</template>
  
<script>
import ClassroomCard from '@/components/ClassroomCard.vue'
import EventCalendar from '@/components/EventCalendar.vue'
import { defineComponent } from 'vue';
import { useReservationStore } from '@/stores/reservations.js'

// Components

export default defineComponent({
  name: 'ManageReservations',

  data: () => ({
    nReservation: 1,
    loading: false,
    userReservations: []
  }),

  methods: {

    fetchPendingReservation() {

      this.userReservations = []
      this.loading = true
      this.reservationStore.fetchAll({
        status: "pending"
      }).then(res => {
        console.log(res)
        this.userReservations = res
        this.loading = false
      }).catch(err => {
        console.error(err)
        this.userReservations = []
        this.loading = false
      })
    },

    approveReservation(reservationId, reason) {
      let token = localStorage.getItem('cookie')
      this.reservationStore.approve(token, reservationId, reason).then(res => {
        alert("อนุมัติการจองสำเร็จ")
        this.fetchPendingReservation()
      }).catch(() => {
        alert("อนุมัติการจองล้มเหลว")
      })
    },

    rejectReservation(reservationId, reason) {
      let token = localStorage.getItem('cookie')
      this.reservationStore.reject(token, reservationId, reason).then(res => {
        alert("ปฏิเสธการจองสำเร็จ")
        this.fetchPendingReservation()
      }).catch(() => {
        alert("ปฏิเสธการจองล้มเหลว")
      })
    }

  },

  setup() {
    return {
      reservationStore: useReservationStore()
    }
  },

  components: {
    ClassroomCard,
    EventCalendar,
  },

  mounted() {
    this.fetchPendingReservation()
  }
});
</script>
  