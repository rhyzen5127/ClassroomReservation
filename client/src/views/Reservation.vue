<template>
  <div class="mt-10 mx-10">
    <div class="text-h6 mb-1">
      กรุณากรอกข้อมูล
    </div>
    <v-select
    v-model="building"
    label="เลือกอาคาร"
    :items="building_item"
    item-title="name"
    item-value="id"
    persistence-hint
    return-object>
    </v-select>

    <v-select
    v-model="room"
    :label="isLabelChange"
    :disabled="isDisableRoomSelect"
    :items="classroom_items"
    item-title="name"
    item-value="id"
    return-object>
    </v-select>

    <Datepicker
    v-model="date"
    :enable-time-picker="false"
    placeholder="กรุณาเลือกวันที่"
    fixed-start
    :clearable="false"
    :min-date=this.date />

    <v-row class="mt-2">
      <v-col>
        <Datepicker
        v-model="startTime"
        time-picker
        minutes-increment="30"
        minutes-grid-increment="30"
        :clearable="false"
        :start-time="{ hours: 8, minutes: 30 }"
        placeholder="กรุณาเลือกเวลาเริ่มต้น" />
      </v-col>

      <v-col>
        <Datepicker
        v-model="endTime"
        time-picker
        minutes-increment="30"
        minutes-grid-increment="30"
        :clearable="false"
        :start-time="{ hours: 8, minutes: 30 }"
        placeholder="กรุณาเลือกเวลาสื้นสุด" 
        :min-time="startTime"/>
      </v-col>
    </v-row>

    <v-card class="mt-5 pa-1" :color="roomStatusColor">
      <div class="text-center">
        {{ roomStatus }}
      </div>
    </v-card>
    <div class="d-flex justify-end">
      <v-btn color="success" class="mt-4" @click="validate" :disabled="true">
        <h2> จองห้อง </h2>
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
import Datepicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css'
import axios from 'axios';

import { defineComponent } from 'vue';

const $axios = axios.create({
  baseURL: "http://localhost:8080",
})

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

    roomStatus: "สถานะห้องเรียน", //ว่าง | ไม่ว่าง
    roomStatusColor: "grey", //เขียว | แดง

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
        $axios.get("buildings/" + newVal.id + "/classrooms").then(res => {
          this.classroom_items = res.data
          this.loading = false
          console.log(this.classroom_items)
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
    setDisplayStatus(status) {
      if(status) {
        roomStatus = "ว่าง"
        roomStatusColor = "green"
      } else {
        roomStatus = "ไม่ว่าง"
        roomStatusColor = "red"
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

      return startDatetime.toISOString().split("Z")[0]


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

      return finishDatetime.toISOString().split("Z")[0]
      //return (this.date.getFullYear() + "-" + (this.date.getMonth() + 1) + "-" + this.date.getDate() + "T" + this.endTime.toLocaleTimeString()).toString()
    },

    // check if selected room & time range is available in the current time
    validateRoomAvailability() {

      console.log(this.room)
      console.log(this.date)
      console.log(this.startTime)
      console.log(this.endTime)

      // validate when all inputs are filled in
      if (this.room && this.date && this.startTime && this.endTime) {

        this.roomStatus = "สถานะห้องเรียน"
        this.roomStatusColor = "grey"

        this.loading = true

        // fetch classroom options
        $axios.get("classrooms/" + this.room.id + "/availability", {
          params: {
            startTime: this.getStartDate(),
            finishTime: this.getEndDate()
          }
        }).then(res => {

          if (res.data.available) {
            this.roomStatus = "ว่าง"
            this.roomStatusColor = "green"
          } else {
            this.roomStatus = "ไม่ว่าง"
            this.roomStatusColor = "red"
          }


          this.loading = false
        }).catch(err => {
          console.error("Cannot check classroom availability: " + err.message)
          this.loading = false
        })

      }

    }
  },

  components: {
  },

  beforeMount() {
    this.date.setDate(this.date.getDate() + 1)
  },

  mounted() {
    this.loading = true
    $axios.get("buildings").then(res => {
      this.building_item = res.data
      this.loading = false
      console.log(this.building_item)
    }).catch(err => {
      console.error("Cannot fetch building data: " + err.message)
      this.loading = false
    })
  }
});
</script>