<template>
  <div class="mt-10 mx-10">
    <v-row>

      <v-col cols="12" md="6">
        <div class="text-h6 mb-1">
          เร็ว ๆ นี้
        </div>
        <v-card class="overflow-y-auto" max-height="650">
          <div v-for="i in userReservations" :key="i + '-classCard'" class="my-4">
            <v-card>
              <ClassroomCard 
                :building="i.room.building.name" 
                :room="i.room.name" 
                :roomwidth="i.room.width" 
                :roomlength="i.room.length" 
                :seats="i.room.seats" 
                :owner="i.owner.firstName + ' ' + i.owner.lastName" 
                :ownerEmail="i.owner.email" 
                :dateStart="new Date(i.startTime)"
                :dateEnd="new Date(i.finishTime)" 
                :statusText="i.status"
                :width="700" class="my-5"
                @delete="deleteReservation(i.id)" 
                isStatusShow deleteable 
                />
            </v-card>
          </div>
        </v-card>
      </v-col>

      <v-col cols="11" md="5" class="mt-10">
        <EventCalendar width="700" />
      </v-col>


    </v-row>
  </div>
</template>
  
<script>
import ClassroomCard from '@/components/ClassroomCard.vue'
import EventCalendar from '@/components/EventCalendar.vue'
import { useBuildingStore } from '@/stores/buildings.js'
import { useClassroomStore } from '@/stores/classrooms.js'
import { useReservationStore } from '@/stores/reservations.js'

// Components

export default {
  data: () => ({

    loading: false,

    building_item: [],
    classroom_item: [],
    userReservations: [],

    room: null,
    building: null,
    startTime: null,
    finishTime: null,

    bannerPath: new URL("@/assets/images/home-banner-background.png", import.meta.url).href,
    tel: null

  }),

  watch: {
    building(newVal) {

      if (!newVal) return

      this.classroom_item = []
      this.room = null

      this.classroomStore.fetchClassroomInBuilding(newVal.id).then(res => {
        this.classroom_item = res
      }).catch(err => {
        console.error("Cannot fetch classrooms data: " + err.message)
      })

      this.reservationStore.fetchFromBuilding(newVal.id, {
        minReserveTime: this.startTime,
        maxReserveTime: this.finishTime
      }).then(res => {
        this.reservations = res
      }).catch(err => {
        console.error("Cannot fetch reservations: " + err.message)
      })


    },

    room(newVal) {

      if (!newVal) return

      this.reservationStore.fetchFromClassroom(newVal.id, {
        minReserveTime: this.startTime,
        maxReserveTime: this.finishTime
      }).then(res => {
        this.reservations = res
      }).catch(err => {
        console.error("Cannot fetch reservations: " + err.message)
      })

    }
  },

  methods: {
    fetchUserReservation() {

      this.userReservations = []

      let token = localStorage.getItem("cookie")
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
    },

    selectDate(startTime, finishTime) {

      this.startTime = startTime
      this.finishTime = finishTime

      console.log(startTime, finishTime)

      let params = {
        minReserveTime: this.startTime,
        maxReserveTime: this.finishTime
      }

      if (this.room) {

        this.reservationStore.fetchFromClassroom(this.room.id, params).then(res => {
          this.reservations = res
        }).catch(err => {
          console.error("Failed to fetch reservations: " + err)
        })

      } else if (this.building) {

        this.reservationStore.fetchFromBuilding(this.building.id, params).then(res => {
          this.reservations = res
        }).catch(err => {
          console.error("Failed to fetch reservations: " + err)
        })

      } else {

        this.reservationStore.fetchAll(params).then(res => {
          this.reservations = res
          this.fetchUserReservation()
        }).catch(err => {
          console.error("Failed to fetch reservations: " + err)
        })

      }
    },

    displayTime() {
      if (!this.startTime || !this.finishTime) return "กรุณาเลือกวันที่"

      var startTime = new Date(this.startTime.toISOString())
      var finishTime = new Date(this.finishTime.toISOString())
      finishTime.setDate(finishTime.getDate() - 1)

      let locale = "th-TH"
      let format = {
        year: "numeric",
        month: "short",
        day: "numeric"
      }

      if (startTime.getDate() == finishTime.getDate())
        return startTime.toLocaleDateString(locale, format)
      else
        return startTime.toLocaleDateString(locale, format) + " - " + finishTime.toLocaleDateString(locale, format)
    },

    deleteReservation(reservationId) {
			let token = localStorage.getItem("cookie")
			this.reservationStore.deleteReservation(token, reservationId).then(() => {
				console.log("delete success")
        this.fetchUserReservation()
			}).catch(err => {
				console.log("delete failed")
				console.log(err)
			})
		}
  },

  setup() {
    return {
      reservationStore: useReservationStore(),
      buildingStore: useBuildingStore(),
      classroomStore: useClassroomStore()
    }
  },

  name: 'History',

  components: {
    ClassroomCard,
    EventCalendar,
  },

  mounted() {

    let token = localStorage.getItem("cookie")

    if (!token) return

    this.reservationStore.fetchUserReserved(token).then(res => {
        this.userReservations = res
        this.loading = false
      }).catch(err => {
        console.error(err)
        this.userReservations = []
        this.loading = false
      })

    this.buildingStore.fetchAll().then(res => {
      this.building_item = res
    }).catch(err => {
      console.error(err)
      this.building_item = []
    })
  }
};
</script>
  