<template>
  <div class="mt-10 mx-10">
    <div class="text-h6 mb-1">
      กรุณากรอกข้อมูล
    </div>
    <v-select v-model="building" label="เลือกอาคาร" :items="building_item" item-title="name" item-value="id"
      persistence-hint return-object>
    </v-select>

    <v-select v-model="room" :label="isLabelChange" :disabled="isDisableRoomSelect" :items="classroom_items"
      item-title="name" item-value="id" return-object>
    </v-select>

    <Datepicker v-model="date" :enable-time-picker="false" placeholder="กรุณาเลือกวันที่" fixed-start :clearable="false"
      :min-date=this.date />

    <v-row class="mt-2">
      <v-col>
        <Datepicker v-model="startTime" time-picker minutes-increment="30" minutes-grid-increment="30"
          :clearable="false" :start-time="{ hours: 8, minutes: 30 }" placeholder="กรุณาเลือกเวลาเริ่มต้น" />
      </v-col>

      <v-col>
        <Datepicker v-model="endTime" time-picker minutes-increment="30" minutes-grid-increment="30" :clearable="false"
          :start-time="{ hours: 8, minutes: 30 }" placeholder="กรุณาเลือกเวลาสื้นสุด" :min-time="startTime" />
      </v-col>
    </v-row>

    <v-card class="mt-5" :color="roomStatusColor">
      <div class="text-center">
        {{ roomStatus }}
      </div>
    </v-card>



    <v-btn color="green" class="d-flex mt-4" block @click="confirmDialog = true" :disabled="!status || !isLoggedIn">
      <h2> จองห้อง </h2>
    </v-btn>
    
    <v-dialog v-model="confirmDialog">
      <v-card class="ma-15">
        <v-card-text class="d-flex align-center justify-center my-2">
          ยืนยันการบันทึกหรือไม่
        </v-card-text>
        <v-card-actions>
          <v-row class="mb-5 mx-2">
            <v-col>
              <v-btn class=" bg-green" color="white" block
                @click="confirmDialog = true, successDialog = true, addReservation()">
                ยืนยัน
              </v-btn>
              <v-dialog v-model="successDialog">
                <v-card class="d-flex align-center justify-center my-2 pa-5">
                  <div class="text-center" v-if="isReservationSuccess">
                    <h2 class="text-green"> สำเร็จ </h2>
                    สามารถตรวจสอบสถานะการจองห้องได้ที่หน้า ประวัติการจอง
                  </div>
                  <div class="text-center" v-else>
                    <h2 class="text-red"> ล้มเหลว </h2>
                    เกิดข้อผิดพลาด กรุณาลองใหม่อีกครั้งในภายหลัง
                  </div>
                  <v-btn color="black" class="mt-5" @click="confirmDialog = false, successDialog = false" to="/history">
                    <h5> ปิด </h5>
                  </v-btn>
                </v-card>
              </v-dialog>
            </v-col>
            <v-col>
              <v-btn class="bg-red" color="white" block @click="confirmDialog = false">
                ยกเลิก
              </v-btn>
            </v-col>
          </v-row>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <div class="ma-16 pa-5">
      <v-btn class="ma-5" color="red" @click="testDisplayStatus(false)">
        เสกให้ห้องว่าง
      </v-btn>
    </div>


    <div>
      <v-overlay v-model="loading" class="align-center justify-center">
        <v-progress-circular size="70" color="orange" indeterminate>
        </v-progress-circular>
      </v-overlay>
    </div>

    <v-row class="mt-2 mx-5">
      <v-row justify="center">
      </v-row>
    </v-row>
  </div>
</template>
  
<script>
import Datepicker from '@vuepic/vue-datepicker'
import '@vuepic/vue-datepicker/dist/main.css'
import axios from 'axios';
import { useClassroomStore } from '@/stores/classrooms'
import { useBuildingStore } from '@/stores/buildings'
import { useReservationStore } from '@/stores/reservations'

import { defineComponent } from 'vue';

export default defineComponent({
  name: 'Reservation',

  components: { Datepicker },

  data: () => ({
    building: null,
    room: null,
    startTime: null,
    date: new Date(),
    endTime: null,
    loading: true,
    isLoggedIn: false,

    status: null,
    roomStatus: "สถานะห้องเรียน", //ว่าง | ไม่ว่าง
    roomStatusColor: "grey", //เขียว | แดง

    isReservationSuccess: null,

    confirmDialog: false,
    successDialog: false,

    building_item: [],
    classroom_items: []
  }),

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

  watch: {

    date(newVal, oldVal) {
      if (newVal) {
        console.log(this.date);
        this.validateRoomAvailability()
      }
    },

    building(newVal, oldVal) {
      if (newVal) {

        // clear classroom options & input
        this.classroom_items = []
        this.room = null

        this.loading = true

        // fetch classroom options
        this.classroomStore.fetchClassroomInBuilding(newVal.id).then(res => {
          this.classroom_items = res
          this.loading = false
        }).catch(err => {
          console.error("Cannot fetch classrooms data: " + err.message)
          this.loading = false
        })

        this.validateRoomAvailability()

      }
    },

    startTime(newVal) {
      if (newVal) {
        this.validateRoomAvailability()
      }
    },

    endTime(newVal) {
      if (newVal) {
        this.validateRoomAvailability()
      }
    }

  },

  methods: {
    testDisplayStatus() {
      this.status = !this.status
      if (this.status) {
        this.status = true
        this.roomStatus = "ว่าง"
        this.roomStatusColor = "green"
      } else {
        this.status = false
        this.roomStatus = "ไม่ว่าง"
        this.roomStatusColor = "red"
      }
    },

    getStartDate() {
      var startDatetime = new Date()
      startDatetime.setFullYear(this.date.getFullYear())
      startDatetime.setMonth(this.date.getMonth())
      startDatetime.setDate(this.date.getDate())
      startDatetime.setHours(this.startTime.hours)
      startDatetime.setMinutes(this.startTime.minutes)
      startDatetime.setSeconds(this.startTime.seconds)

      return startDatetime.toISOString()


      // return this.date.getFullYear() + "-" + this.date.getMonth() + "-" + this.date.getDate() + "T"
      // + this.startTime.hours + ":" + this.startTime.minutes
    },

    getEndDate() {
      var finishDatetime = new Date()
      finishDatetime.setFullYear(this.date.getFullYear())
      finishDatetime.setMonth(this.date.getMonth())
      finishDatetime.setDate(this.date.getDate())
      finishDatetime.setHours(this.endTime.hours)
      finishDatetime.setMinutes(this.endTime.minutes)
      finishDatetime.setSeconds(this.endTime.seconds)

      return finishDatetime.toISOString()
      //return (this.date.getFullYear() + "-" + (this.date.getMonth() + 1) + "-" + this.date.getDate() + "T" + this.endTime.toLocaleTimeString()).toString()
    },

    // check if selected room & time range is available in the current time
    validateRoomAvailability() {
      // validate when all inputs are filled in
      if (this.room && this.date && this.startTime && this.endTime) {

        this.roomStatus = "สถานะห้องเรียน"
        this.roomStatusColor = "grey"

        this.loading = true

        // fetch classroom options
        this.classroomStore.checkIsRoomAvailable(this.room.id, this.getStartDate(), this.getEndDate()).then(res => {
          if (res) {
            this.status = true
            this.roomStatus = "ว่าง"
            this.roomStatusColor = "green"
          } else {
            this.status = false
            this.roomStatus = "ไม่ว่าง"
            this.roomStatusColor = "red"
          }
          this.loading = false
        }).catch(err => {
          console.error("Cannot check classroom availability: " + err.message)
          this.loading = false
        })

      }

    },

    addReservation() {
      console.log("addReservation")
      this.loading = true
      this.reservationStore.reserve(localStorage.cookie, this.room.id, this.getStartDate(), this.getEndDate()).then(res => {
        this.isReservationSuccess = true
        this.loading = false
      }).catch((err) => {
        console.error(err)
        this.isReservationSuccess = false
        this.loading = false
      })
    }
  },

  components: {
  },

  beforeMount() {
    this.date.setDate(this.date.getDate() + 1)
  },

  setup() {
    const buildingStore = useBuildingStore()
    const classroomStore = useClassroomStore()
    const reservationStore = useReservationStore()
    return {
      buildingStore,
      classroomStore,
      reservationStore
    }
  },

  mounted() {
    this.isLoggedIn = localStorage.cookie != undefined ? true : false

    this.loading = true
    this.buildingStore.fetchAll().then(res => {
      this.building_item = res
      this.loading = false
    }).catch(err => {
      console.error("Cannot fetch building data: " + err.message)
      this.loading = false
    })

    
  }
});
</script>