<template>
  <div>
    <v-card :width="width" variant="">
      <v-row>
        <v-col class="pa-0" cols="12" md="4">
          <v-img
            src="https://storage.googleapis.com/inskru-optimized-image/-N2luZK8IiJoO1Qn1hfr:0.webp"
            :aspect-ratio="1"
            cover
          >
          </v-img>
        </v-col>
        <v-col cols="12" md="8">
          <div class="text-overline mb-1">
            {{ roomUseDate }} : {{ roomUseTime }}
          </div>

          <div class="text-h6 mb-1">
            {{ classroomName }}
          </div>

          <div class="text-caption">
            {{ buildingName }}
          </div>

          <div class="text-caption">
            {{ classroomSize }}
          </div>

          <div class="text-caption">
            {{ classroomSeats }}
          </div>

          <div v-if="showOwner">
            <h6 class="mt-3">ผู้จอง:</h6>
            <h6 class="mx-5">
              {{ ownerUserName }}
            </h6>
            <h6 class="mx-5">
              {{ ownerUserEmail }}
            </h6>
          </div>

          <div v-if="showReserveNote">
            <h6 class="mt-3">หมายเหตุในการจอง:</h6>
            <div class="text-caption mx-5">
              {{ reservationNote }}
            </div>
          </div>

          <div v-if="showStatus"
            :class="'mt-3 h6 text-caption mr-5 ' + reservationStatusColor"
          >
            {{ reservationStatus }}
            <div v-if="isRejected">
              สาเหตุ: {{ reservationStatusNote }}
            </div>
          </div>


          <div class="d-flex mx-auto justify-end">
            <div v-if="editable" class="mx-5">
              <ManageReservedClassroom />
            </div>

            <div v-if="deleteable && isRejected" class="mx-5">
              <v-btn class="bg-red" color="white" @click="$emit('delete')">
                ลบ
              </v-btn>
            </div>
          </div>

          <div
            v-if="managable"
            class="d-flex justify-end ms-16 mb-5 text-center"
          >
            <v-btn
              color="white"
              class="bg-green mx-5"
              @click="$emit('approve')"
            >
              อนุมัติ
            </v-btn>
            <v-btn color="white" class="bg-red mx-5" @click="$emit('reject')">
              ไม่อนุมัติ
            </v-btn>
          </div>
        </v-col>
      </v-row>
    </v-card>
  </div>
</template>

<!-- Parameter = Building, Name, Date, Time, Picture, Status -->
<script>
import ManageReservedClassroom from "./ManageReservedClassroom.vue";

export default {
  name: "ClassroomCard",

  components: { ManageReservedClassroom },

  props: {
    // reservation data from backend API
    reservation: { type: Object, required: false, default: {} },

    // display options
    showOwner: { type: Boolean, require: false, default: false },
    showStatus: { type: Boolean, require: false, dafault: false },
    showReserveNote: { type: Boolean, require: false, dafault: true },

    // control options
    editable: { type: Boolean, require: false, default: false },
    managable: { type: Boolean, require: false, default: false },
    deleteable: { type: Boolean, require: false, dafault: false },

    // miscellaneous
    width: { type: Number, require: false, default: 700 },
  },

  emits: ["approve", "reject", "delete"],

  methods: {},

  data: () => ({
    status: "null",
    statusColor: "text-orange", //orange,  green,  red
    dateFormat: {
      dateOptions: {
        year: "numeric",
        month: "short",
        weekday: "short",
        day: "numeric",
      },
      timeOptions: {
        hour: "numeric",
        minute: "numeric",
      },
      locale: "th-TH",
    },
  }),

  computed: {
    // building info
    buildingName() {
      let defaultValue = "<<Undefined building>>";
      if (
        !this.reservation ||
        !this.reservation.room ||
        !this.reservation.room.building
      )
        return defaultValue;
      return this.reservation.room.building.name || defaultValue;
    },

    // classroom info
    classroomName() {
      let defaultValue = "<<Undefined room>>";
      if (!this.reservation || !this.reservation.room) return defaultValue;
      return this.reservation.room.name || defaultValue;
    },

    classroomSize() {
      let defaultValue = "(ไม่ระบุขนาดห้อง)";
      if (!this.reservation || !this.reservation.room) return defaultValue;
      let roomWidth = this.reservation.room.width;
      let roomLength = this.reservation.room.length;
      return roomWidth && roomLength
        ? roomWidth + " x " + roomLength + " เมตร"
        : defaultValue;
    },

    classroomSeats() {
      let defaultValue = "(ไม่ระบุจำนวนที่นั่ง)";
      if (!this.reservation || !this.reservation.room) return defaultValue;
      let seatCount = this.reservation.room.seats;
      return seatCount ? seatCount + " ที่นั่ง" : defaultValue;
    },

    // owner user info
    ownerUserName() {
      let defaultValue = "<<Undefined owner>>";
      if (!this.reservation.owner) return defaultValue;
      let fname = this.reservation.owner.firstName;
      let lname = this.reservation.owner.lastName;
      return fname && lname ? fname + " " + lname : defaultValue;
    },

    ownerUserEmail() {
      let defaultValue = "<<Undefined owner email>>";
      if (!this.reservation.owner) return defaultValue;
      return this.reservation.owner.email || defaultValue;
    },

    // reservation info
    roomUseDate() {
      let defaultValue = "<<Undefined use date>>";
      if (!this.reservation.startTime) return defaultValue;

      let date = new Date(this.reservation.startTime);
      let locale = this.dateFormat.locale;
      let options = this.dateFormat.dateOptions;
      let minuteOffset = new Date().getTimezoneOffset();
      date.setMinutes(date.getMinutes() - minuteOffset);

      return date.toLocaleDateString(locale, options);
    },

    roomUseTime() {
      let defaultValue = "<<Undefined use time>>";
      if (!this.reservation.startTime || !this.reservation.finishTime)
        return defaultValue;

      let d1 = new Date(this.reservation.startTime);
      let d2 = new Date(this.reservation.finishTime);
      let locale = this.dateFormat.locale;
      let options = this.dateFormat.timeOptions;
      let minuteOffset = new Date().getTimezoneOffset();

      d1.setMinutes(d1.getMinutes() - minuteOffset);
      d2.setMinutes(d2.getMinutes() - minuteOffset);

      return (
        d1.toLocaleTimeString(locale, options) +
        " น. - " +
        d2.toLocaleTimeString(locale, options) +
        " น."
      );
    },

    reservationStatus() {
      let defaultValue = "<<Undefined status>>";
      if (!this.reservation.status) return defaultValue;
      switch (this.reservation.status) {
        case "pending":
          return "รอการอนุมัติ";
        case "approved":
          return "อนุมัติ";
        case "rejected":
          return "ไม่อนุมัติ";
        default:
          return "<<Invalid status>>";
      }
    },

    reservationStatusColor() {
      let defaultValue = "text-red";
      if (!this.reservation.status) return defaultValue;
      switch (this.reservation.status) {
        case "pending":
          return "text-orange";
        case "approved":
          return "text-green";
        case "rejected":
          return "text-red";
        default:
          defaultValue;
      }
    },

    reservationNote() {
      let defaultValue = "(ไม่ระบุ)";
      if (!this.reservation) return defaultValue;
      return this.reservation.reserveNote || defaultValue
    },

    reservationStatusNote() {
      let defaultValue = "(ไม่ระบุ)";
      if (!this.reservation) return defaultValue;
      return this.reservation.approveNote || defaultValue
    },

    isRejected() {
      if (!this.reservation || !this.reservation.status) return false;
      return this.reservation.status == "rejected";
    },
  },

  setup() {},

  beforeMount() {},

  mounted() {},
};
</script>
