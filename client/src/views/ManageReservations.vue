<template>
  <div class="mt-10 mx-10">
    <div class="text-h6 mb-1">
      จัดการคำขอจอง
    </div>

    <div v-if="userReservations.length == 0" class="text-caption text-center text-grey-darken-1 pt-8">( ยังไม่มีคำขอที่รอการอนุมัติในตอนนี้... )</div>

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
    <v-snackbar
      v-model="showApproveDialog"
      :color="approveSuccess ? 'green' : 'red'"
      :timeout="2000"
    >
      {{ approveMessage }}
    </v-snackbar>
  </div>
</template>
  
<script>
import ClassroomCard from '@/components/ClassroomCard.vue'
import EventCalendar from '@/components/EventCalendar.vue'
import { defineComponent } from 'vue';
import { useStores } from '../stores';

// Components

export default defineComponent({
  name: 'ManageReservations',

  data: () => ({
    nReservation: 1,
    loading: false,
    userReservations: [],
    approveMessage: "",
    approveSuccess: false,
    showApproveDialog: false
  }),

  methods: {

    fetchPendingReservation() {

      this.userReservations = []
      this.loading = true
      this.stores.reservation.fetchAll({
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

      this.showApproveDialog = false
      this.approveSuccess = false
      this.approveMessage = ""

      let token = localStorage.getItem('cookie')
      this.stores.reservation.approve(token, reservationId, reason).then(res => {
        this.approveMessage = "อนุมัติการจองสำเร็จ"
        this.approveSuccess = true
        this.showApproveDialog = true
        this.fetchPendingReservation()
      }).catch(() => {
        this.approveMessage = "อนุมัติการจองล้มเหลว"
        this.approveSuccess = false
        this.showApproveDialog = true
      })

    },

    rejectReservation(reservationId, reason) {

      this.showApproveDialog = false
      this.approveSuccess = false
      this.approveMessage = ""

      let token = localStorage.getItem('cookie')

      this.stores.reservation.reject(token, reservationId, reason).then(res => {
        this.approveMessage = "ปฏิเสธการจองสำเร็จ"
        this.approveSuccess = true
        this.showApproveDialog = true
        this.fetchPendingReservation()
      }).catch(() => {
        this.approveMessage = "ปฏิเสธการจองล้มเหลว"
        this.approveSuccess = false
        this.showApproveDialog = true
      })
    }

  },

  setup() {
    return {
      stores: useStores()
    }
  },

  components: {
    ClassroomCard,
    EventCalendar,
  },

  beforeMount() {
    // check if user is logged in and is a staff or not, if no then redirect to homepage
    this.stores.user.fetchCurrentUser(localStorage.getItem('cookie'))
    .then(userData => {
      if (userData.role != 'staff') {
      this.$router.replace("/")
      }
    })
    .catch(() => {
      this.$router.replace("/")
    })

  },

  mounted() {
    this.fetchPendingReservation()
  }
  
});
</script>
  