<template>
  <v-row class="ma-0">
    <v-col cols="12" md="7">
      <EventCalendar @dateSelected="selectDate" width="860" class="mt-5" />
    </v-col>
    <v-col cols="12" md="5">
      <h2 class="mb-2">
        เลือกดูห้องเรียนตามวันที่
      </h2>
      <v-card>
        <v-card color="orange" class="pa-2 mb-5">
          <v-icon icon="mdi-filter"></v-icon>
          ตัวกรอง
        </v-card>

        <div class="text-h6 mx-5 mb-3">
          วันที่ {{ displayTime() }}
        </div>

        <v-select v-model="building" label="เลือกอาคาร" :items="building_item" item-title="name" item-value="id"
          persistence-hint return-object class="mx-5">
        </v-select>

        <v-select v-model="room" :label="isLabelChange" :disabled="isDisableRoomSelect" :items="classroom_item"
          item-title="name" item-value="id" return-object class="mx-5">
        </v-select>

      </v-card>
      <v-divider></v-divider>
      <v-card class="overflow-y-auto my-4" max-height="485">
        <div v-for="i in reservations" :key="i.id">
          <ClassroomCard
            :reservation="i"
            :width="648.91" class="my-5"
            showOwner
            />
        </div>
      </v-card>
    </v-col>
  </v-row>
</template>

<script>
import ClassroomCard from '@/components/ClassroomCard.vue'
import EventCalendar from '@/components/EventCalendar.vue'
import { useReservationStore } from '@/stores/reservations.js'
import { useBuildingStore } from '@/stores/buildings.js'
import { useClassroomStore } from '@/stores/classrooms.js'

export default {
  name: 'Home',

  components: {
    EventCalendar,
    ClassroomCard,
  },

  data() {
    return {
      reservations: [],
      building: null,
      building_item: [],
      room: null,
      classroom_item: [],
      bannerPath: new URL("@/assets/images/home-banner-background.png", import.meta.url).href,
      tel: null,
      startTime: null,
      finishTime: null
    };

  },

  methods: {

    selectDate(startTime, finishTime) {
      this.startTime = startTime
      this.finishTime = finishTime
      this.loadReservations()
    },

    displayTime() {
      if (!this.startTime || !this.finishTime) return "กรุณาเลือกวันที่"

      var startTime = new Date(this.startTime.toISOString())
      var finishTime = new Date(this.finishTime.toISOString())
      finishTime.setDate(finishTime.getDate() - 1)

      let locale = "th-TH"
      let format = { 				
        year:"numeric", 
				month:"short",
				day:"numeric" 
      }

      if (startTime.getDate() == finishTime.getDate()) 
        return startTime.toLocaleDateString(locale, format)
      else
        return startTime.toLocaleDateString(locale, format) + " - " + finishTime.toLocaleDateString(locale, format)
    },

    loadReservations() {

      let params = {
        minReserveTime: this.startTime,
        maxReserveTime: this.finishTime,
        status: "approved"
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
        }).catch(err => {
          console.error("Failed to fetch reservations: " + err)
        })

      }

    }

  },

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




  computed: {

    // [Room select dropdown] Disable state True | False
    isDisableRoomSelect() {
      return this.building ? false : true;
    },

    // [Room select dropdown] Label
    isLabelChange() {
      return this.building ? "กรุณาเลือกห้อง" : "กรุณาเลือกห้อง (กรุณาเลือกอาคารก่อน)";
    }
  },

  setup() {
    return {
      reservationStore: useReservationStore(),
      buildingStore: useBuildingStore(),
      classroomStore: useClassroomStore()
    }
  },

  mounted() {
    this.buildingStore.fetchAll().then(res => {
      this.building_item = res
    }).catch(err => {
      console.log(err)
      this.building_item = []
    })
  }


}
</script>