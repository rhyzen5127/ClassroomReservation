<template>
  <div class="mt-10 mx-10">
    <div class="text-h6 mb-1">กรุณาเลือกห้องเรียนที่จะแก้ไข</div>

    <ClassroomSelector v-model="room" compact />

    <!--     
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
      :label="isLabelChange"
      :disabled="isDisableRoomSelect"
      :items="classroom_items"
      item-title="name"
      item-value="id"
      return-object
    >
    </v-select> -->

    <v-card v-if="room" class="pa-10">
      <h3 class="mb-5">
        รายละเอียดห้อง {{ room.name }} อาคาร {{ room.building.name }}
      </h3>

      <v-form ref="form" v-model="valid" lazy-validation>
        <v-textarea
          v-model="roomDetail"
          clearable
          label="รายละเอียด: "
          prepend-icon="mdi-book"
        ></v-textarea>

        <div class="d-flex">
          <v-text-field
            v-model.number="seats"
            :rules="[ validateSeatInput ]"
            clearable
            label="ที่นั่ง: (ตัว)"
            prepend-icon="mdi-seat"
          ></v-text-field>

          <v-switch
            class="ms-16"
            prepend-icon="mdi-wrench"
            v-model="status"
            :label="status ? 'พร้อมใช้งาน' : 'ไม่พร้อมใช้งาน'"
            color="success"
            hide-details
          ></v-switch>
        </div>


        <div class="d-flex justify-center">
          <v-btn
            color="green"
            class="mx-2"
            @click="submitUpdateClassroom(), (loading = true)"
            :disabled="!isInputValid()"
          >
            ยืนยัน
          </v-btn>
          <v-btn
            color="red"
            class="mx-2"
            @click="resetInput(), (this.editStatus = 'ยกเลิกแล้ว')"
          >
            ยกเลิก
          </v-btn>
        </div>
      </v-form>
    </v-card>

    <div>
      <v-overlay v-model="loading" class="align-center justify-center">
        <v-progress-circular size="70" color="orange" indeterminate>
        </v-progress-circular>
      </v-overlay>
    </div>

    <v-row class="mt-2 mx-5">
      <v-row justify="center"> </v-row>
    </v-row>

    <v-snackbar
      v-model="showEditStatus"
      :color="editSuccess ? 'green' : 'red'"
      :timeout="2000"
    >
      {{ editStatus }}
    </v-snackbar>
  </div>
</template>
  
<script>

import { defineComponent } from "vue";
import ClassroomSelector from "../components/ClassroomSelector.vue";
import { useStores } from "../stores";

export default defineComponent({
  name: "Reservation",

  components: {},

  data: () => ({
    room: null,
    loading: false,
    isLoggedIn: false,

    confirmDialog: false,
    successDialog: false,

    editable: false,
    editStatus: null,
    showEditStatus: false,
    editSuccess: false,

    building_item: [],
    classroom_items: [],

    seats: null,
    status: null,
    roomDetail: null,
  }),

  computed: {},

  watch: {
    room() {
      this.resetInput();
    },
  },

  methods: {

    validateSeatInput(v) {
      if (new RegExp('^[0-9]*$').test(v)) {
        return true
      }
      return "กรุณากรอกข้อมูลเป็นจำนวนเต็มบวกเท่านั้น"
    },

    isInputChanged() {
      return  (this.seats != (this.room.seats || "")) || 
              (this.status != (this.room.ready || false)) || 
              (this.roomDetail != (this.room.note || ""))
    },

    isInputValid() {
      return (new RegExp('^[0-9]*$').test(this.seats)) && this.isInputChanged() 
    },

    resetInput() {
      if (this.room) {
        this.seats = this.room.seats || "";
        this.status = this.room.ready || false;
        this.roomDetail = this.room.note || "";
        this.editStatus = "";
      }
    },

    submitUpdateClassroom() {

      this.showEditStatus = false
      this.editSuccess = false
      this.loading = true;

      let updatedSeats = this.room.seats != this.seats ? this.seats : undefined;
      let updatedStatus = this.room.status != this.status ? this.status : undefined;

      this.stores.classroom
        .updateClassroom(
          localStorage.cookie,
          this.room.id,
          updatedSeats,
          updatedStatus,
          this.roomDetail
        )
        .then(() => {
          this.room.seats = updatedSeats;
          this.room.status = updatedStatus;
          this.room.note = this.roomDetail;
          this.loading = false;

          this.editStatus = "แก้ไขสำเร็จ !";
          this.showEditStatus = true
          this.editSuccess = true
        })
        .catch((err) => {
          console.error(err);
          this.loading = false;
          this.editStatus = "เกิดข้อผิดพลาด กรุณาลองใหม่ในภายหลัง";
          this.showEditStatus = true
          this.editSuccess = false
        });
    },
  },

  components: { ClassroomSelector },

  setup() {
    return {
      stores: useStores()
    };
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

  mounted() {},
});
</script>