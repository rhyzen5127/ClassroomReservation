<template>
  <div class="mt-10 mx-10">
    <div class="text-h6 mb-1">
      กรุณาเลือกห้องเรียนที่จะแก้ไข
    </div>
    <v-select v-model="building" label="เลือกอาคาร" :items="building_item" item-title="name" item-value="id"
      persistence-hint return-object>
    </v-select>

    <v-select v-model="room" :label="isLabelChange" :disabled="isDisableRoomSelect" :items="classroom_items"
      item-title="name" item-value="id" return-object>
    </v-select>

    <v-card v-if="room" class="pa-10">
      <h3 class="mb-5"> รายละเอียดห้อง {{ room ? room.name : " " }} อาคาร {{ building ? building.name : " " }}</h3>

      <v-text-field v-model="seats" clearable label="ที่นั่ง (ตัว): " prepend-icon="mdi-seat"></v-text-field>
      <v-switch prepend-icon="mdi-wrench" v-model="status" :label="status ? 'พร้อมใช้งาน' : 'ไม่พร้อมใช้งาน'"
        color="success" hide-details></v-switch>

      <div class="d-flex justify-center"> {{ editStatus }} </div>

      <div class="d-flex justify-center">
        <v-btn color="green" class="mx-2" @click="submitUpdateClassroom(), loading = true">
          ยืนยัน
        </v-btn>
        <v-btn color="red" class="mx-2" @click="resetInput(), this.editStatus = 'ยกเลิกแล้ว'">
          ยกเลิก
        </v-btn>
      </div>
    </v-card>

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
import { useClassroomStore } from '@/stores/classrooms'
import { useBuildingStore } from '@/stores/buildings'

import { defineComponent } from 'vue';

export default defineComponent({
  name: 'Reservation',

  components: {},

  data: () => ({
    building: null,
    room: null,
    loading: true,
    isLoggedIn: false,

    confirmDialog: false,
    successDialog: false,

    editable: false,
    editStatus: null,

    building_item: [],
    classroom_items: [],

    seats: null,
    status: null,
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

      }
    },

    room(newVal) {
      if (newVal) {
        this.resetInput()
      }
    }
  },

  methods: {

    fetchClassroomData() {
      this.isLoggedIn = localStorage.cookie != undefined ? true : false

      this.loading = true
      this.buildingStore.fetchAll().then(res => {
        this.building_item = res
        this.loading = false
      }).catch(err => {
        console.error("Cannot fetch building data: " + err.message)
        this.loading = false
      })
    },

    resetInput() {
      this.seats = this.room != null ? this.room.seats : "Error"
      this.status = this.room != null ? this.room.ready : false
      this.editStatus = ""
    },

    submitUpdateClassroom() {
      let updatedSeats = this.room.seats != this.seats ? this.seats : undefined
      let updatedStatus = this.room.status != this.status ? this.status : undefined
      this.loading = true
      this.classroomStore.updateClassroom(localStorage.cookie, this.room.id, updatedSeats, updatedStatus).then(() => {
        this.editStatus = "แก้ไขสำเร็จ !"
        this.room.seats = updatedSeats
        this.room.status = updatedStatus
        this.loading = false
      }).catch(err => {
        console.error(err)
        this.editStatus = "เกิดข้อผิดพลาด กรุณาลองใหม่ในภายหลัง"
        this.loading = false
      })
    }

  },

  components: {
  },

  setup() {
    return {
      buildingStore: useBuildingStore(),
      classroomStore: useClassroomStore(),
    }
  },

  mounted() {
    this.fetchClassroomData()


  }
});
</script>