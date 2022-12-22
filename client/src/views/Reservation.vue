<template>
  <div class="mt-10 mx-10">
    <div class="text-h6 mb-1">กรุณากรอกข้อมูล</div>
    <v-select
      v-model="building"
      label="เลือกอาคาร"
      :items="building_item"
      item-title="name"
      item-value="id"
      persistence-hint
      return-object
    >
    </v-select>

    <v-select
      v-model="room"
      :label="roomSelectLabel"
      :disabled="isDisableRoomSelect"
      :items="classroom_items"
      item-title="name"
      item-value="id"
      return-object
    >
    </v-select>

    <Datepicker
      v-model="date"
      :enable-time-picker="false"
      placeholder="กรุณาเลือกวันที่"
      fixed-start
      :clearable="false"
      :min-date="minSelectDate"
    />

    <v-row class="mt-2">
      <v-col>
        <Datepicker
          v-model="startTime"
          time-picker
          minutes-increment="30"
          minutes-grid-increment="30"
          :clearable="false"
          :start-time="{ hours: 8, minutes: 30 }"
          :max-time="endTime || { hours: 21, minutes: 30 }"
          placeholder="กรุณาเลือกเวลาเริ่มต้น"
        />
      </v-col>

      <v-col>
        <Datepicker
          v-model="endTime"
          time-picker
          minutes-increment="30"
          minutes-grid-increment="30"
          :clearable="false"
          :start-time="startTime || { hours: 8, minutes: 30 }"
          placeholder="กรุณาเลือกเวลาสื้นสุด"
          :min-time="startTime"
        />
      </v-col>
    </v-row>

    <v-card class="mt-5" :color="roomStatusColor">
      <div class="text-center">
        {{ roomStatus }}
      </div>
    </v-card>

    <v-textarea
      v-if="isLoggedIn"
      clearable
      label="รายละเอียดการจอง"
      :value="detailField"
      @input="inputReserveNote"
      :color="detailField.length > 300 ? 'red' : 'green'"
      class="mt-10"
      :disabled="!isLoggedIn || !inputValid"
      prepend-icon="mdi-book"
      :hint="(300 - detailField.length).toString() + ' / 300'"
    />

    <v-btn
      v-if="isLoggedIn"
      color="green"
      class="d-flex mt-4"
      block
      @click="confirmDialog = true"
      :disabled="!isLoggedIn || !inputValid || detailField.length > 300"
    >
      <h2>จองห้อง</h2>
    </v-btn>

    <v-dialog v-model="confirmDialog">
      <v-card class="ma-15">
        <v-card-text class="d-flex align-center justify-center my-2">
          ยืนยันการบันทึกหรือไม่
        </v-card-text>
        <v-card-actions>
          <v-row class="mb-5 mx-2">
            <v-col>
              <v-btn
                class="bg-green"
                color="white"
                block
                @click="
                  (confirmDialog = true),
                    (successDialog = true),
                    addReservation()
                "
              >
                ยืนยัน
              </v-btn>
              <v-dialog v-model="successDialog">
                <v-card class="d-flex align-center justify-center my-2 pa-5">
                  <div class="text-center" v-if="isReservationSuccess">
                    <h2 class="text-green">สำเร็จ</h2>
                    สามารถตรวจสอบสถานะการจองห้องได้ที่หน้า ประวัติการจอง
                  </div>
                  <div class="text-center" v-else>
                    <h2 class="text-red">ล้มเหลว</h2>
                    เกิดข้อผิดพลาด กรุณาลองใหม่อีกครั้งในภายหลัง
                  </div>
                  <v-btn
                    color="black"
                    class="mt-5"
                    @click="(confirmDialog = false), (successDialog = false)"
                    to="/history"
                  >
                    <h5>ปิด</h5>
                  </v-btn>
                </v-card>
              </v-dialog>
            </v-col>
            <v-col>
              <v-btn
                class="bg-red"
                color="white"
                block
                @click="confirmDialog = false"
              >
                ยกเลิก
              </v-btn>
            </v-col>
          </v-row>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- <div class="ma-16 pa-5">
      <v-btn class="ma-5" color="red" @click="testDisplayStatus(false)">
        เสกให้ห้องว่าง
      </v-btn>
    </div> -->

    <div>
      <v-overlay v-model="loading" class="align-center justify-center">
        <v-progress-circular size="70" color="orange" indeterminate>
        </v-progress-circular>
      </v-overlay>
    </div>

    <v-row class="mt-2 mx-5">
      <v-row justify="center"> </v-row>
    </v-row>
  </div>
</template>
  
<script>
import Datepicker from "@vuepic/vue-datepicker";
import "@vuepic/vue-datepicker/dist/main.css";
import { useClassroomStore } from "@/stores/classrooms";
import { useBuildingStore } from "@/stores/buildings";
import { useReservationStore } from "@/stores/reservations";
import { useUserStore } from "@/stores/users";

import { defineComponent } from "vue";

export default defineComponent({
  name: "Reservation",

  components: { Datepicker },

  data: () => ({
    // inputs
    building: null,
    room: null,
    startTime: null,
    date: new Date(),
    endTime: null,
    loading: true,
    detailField: "",

    // form state check
    isLoggedIn: false,
    inputValid: false,

    status: null,
    roomStatus: "สถานะห้องเรียน", //ว่าง | ไม่ว่าง | ไม่พร้อมใช้งาน
    roomStatusColor: "grey", //เขียว | แดง

    pickDate: new Date(),
    minSelectDate: new Date(),

    isReservationSuccess: null,

    confirmDialog: false,
    successDialog: false,
    reservationErrMsg: "",

    building_item: [],
    classroom_items: [],
  }),

  computed: {
    // [Room select dropdown] Disable state True | False
    isDisableRoomSelect() {
      return this.building ? false : true;
    },

    // [Room select dropdown] Label
    roomSelectLabel() {
      return this.building
        ? "กรุณาเลือกห้อง"
        : "กรุณาเลือกห้อง (กรุณาเลือกอาคารก่อน)";
    },

  },

  watch: {

    date() {
      this.validateInput();
    },

    building(newVal) {
      if (newVal) {
        // clear classroom options & input
        this.classroom_items = [];
        this.room = null;

        this.loading = true;

        // fetch classroom options
        this.classroomStore
          .fetchClassroomInBuilding(newVal.id)
          .then((res) => {
            this.classroom_items = res;
            this.loading = false;
          })
          .catch((err) => {
            console.error("Cannot fetch classrooms data: " + err.message);
            this.loading = false;
          });

        this.loading = false;
      }
      this.validateInput();
    },

    room() {
      this.validateInput();
    },

    date() {
      this.validateInput();
    },

    startTime() {
      this.validateInput();
    },

    endTime() {
      this.validateInput();
    },
  },

  methods: {

    inputReserveNote(event) {
      let newValue = event.target.value
      if (newValue && newValue.length <= 300) {
        this.detailField = event.target.value
        console.log(this.detailField)
      }
    },

    testDisplayStatus() {
      this.status = !this.status;
      if (this.status && this.isReady) {
        markRoomStatusAvailable(true);
      } else {
        markRoomStatusAvailable(false);
      }
    },

    markRoomStatusAvailable(available) {
      if (available) {
        this.inputValid = true;
        this.roomStatus = "ว่าง";
        this.roomStatusColor = "green";
      } else {
        this.inputValid = false;
        this.roomStatus = "ไม่ว่าง";
        this.roomStatusColor = "red";
      }
    },

    markRoomStatusReady(available) {
      if (available) {
        this.inputValid = false;
        this.roomStatus = "ห้องพร้อมใช้งาน";
        this.roomStatusColor = "cyan text-white";
      } else {
        this.inputValid = false;
        this.roomStatus = "ห้องไม่พร้อมใช้งาน";
        this.roomStatusColor = "red";
      }
    },

    resetRoomStatus() {
      this.inputValid = false;
      this.roomStatus = "สถานะห้องเรียน";
      this.roomStatusColor = "grey";
    },

    getStartDate() {
      var startDatetime = new Date(
        this.date.getFullYear(),
        this.date.getMonth(),
        this.date.getDate(),
        this.startTime.hours,
        this.startTime.minutes,
        this.startTime.seconds
      );
      return startDatetime.toISOString();
    },

    getEndDate() {
      var finishDatetime = new Date(
        this.date.getFullYear(),
        this.date.getMonth(),
        this.date.getDate(),
        this.endTime.hours,
        this.endTime.minutes,
        this.endTime.seconds
      );
      return finishDatetime.toISOString();
    },

    // check if selected room & time range is available in the current time
    async validateInput() {
      // validate room status
      if (this.room) {
        if (this.room.status != "ready") {
          this.markRoomStatusReady(false);
        }
      }

      // validate room availability when all inputs are filled in
      if (this.room && this.date && this.startTime && this.endTime) {
        this.resetRoomStatus();
        this.loading = true;

        let token = localStorage.getItem("cookie")

        // fetch classroom options
        this.classroomStore
          .checkIsRoomAvailable(
            token,
            this.room.id,
            this.getStartDate(),
            this.getEndDate(),
            this.detailField
          )
          .then((res) => {
            // console.log(res)
            this.markRoomStatusAvailable(res);
            this.loading = false;
          })
          .catch((err) => {
            console.error(
              "Cannot check classroom availability: " + err.message
            );
            this.loading = false;
          });
      }
    },

    async addReservation() {
      if (!this.inputValid || !this.isLoggedIn) {
        return;
      }

      this.loading = true;
      this.reservationStore
        .reserve(
          localStorage.getItem("cookie"),
          this.room.id,
          this.getStartDate(),
          this.getEndDate(),
          this.detailField
        )
        .then((res) => {
          this.isReservationSuccess = true;
          this.loading = false;
        })
        .catch((err) => {
          console.error(err);
          this.isReservationSuccess = false;
          this.loading = false;
        });
    },
  },

  components: {},

  // store setup
  setup() {
    return {
      buildingStore: useBuildingStore(),
      classroomStore: useClassroomStore(),
      reservationStore: useReservationStore(),
      userStore: useUserStore(),
    };
  },

  // date picker setup
  beforeMount() {
    this.minSelectDate = new Date();
    this.minSelectDate.setDate(this.minSelectDate.getDate() + 3);
    this.date = this.minSelectDate;
  },

  mounted() {
    this.isLoggedIn = this.userStore.currentUser ? true : false;

    this.loading = true;
    this.buildingStore
      .fetchAll()
      .then((res) => {
        this.building_item = res;
        this.loading = false;
      })
      .catch((err) => {
        console.error("Cannot fetch building data: " + err.message);
        this.loading = false;
      });
  }

});
</script>