<template>
  <v-card
    :class="selectable && cardSelected ? 'bg-orange-lighten-4' : 'bg-white'"
  >
    <v-row>
      <v-col cols="12" md="3" class="d-flex">
        <v-img
          src="https://storage.googleapis.com/inskru-optimized-image/-N2luZK8IiJoO1Qn1hfr:0.webp"
          :aspect-ratio="1"
          cover
          height="200"
        />
      </v-col>
      <v-col cols="12" :md="managable ? 4 : 9">
        <div class="text-overline mb-1">
          {{ roomUseDate }} : {{ roomUseTime }}
        </div>
        <div class="text-h6 mb-1">{{ classroomName }} {{ buildingName }}</div>

        <div class="text-caption"></div>

        <div class="text-caption" v-if="showSize">
          {{ classroomSize }}
        </div>

        <div class="text-caption" v-if="showSeat">
          {{ classroomSeats }}
        </div>

        <div v-if="showOwner">
          <h6 class="mt-3">
            ผู้จอง: {{ ownerUserName }} ({{ ownerUserEmail }})
          </h6>
        </div>

        <div class="mt-3 d-flex" v-if="showReserveNote">
          <div class="text-caption">หมายเหตุ:</div>
          <div class="text-caption mx-5">
            {{ reservationNote }}
          </div>
        </div>

        <div v-if="showStatus">
          <v-chip :class="'h6 text-caption mr-5 ' + reservationStatusColor">
            {{ reservationStatus }}
          </v-chip>
          <div class="ms-3 mt-1 text-caption text-green" v-if="isApproved">
            หมายเหตุจากพนักงาน: {{ reservationStatusNote }}
          </div>
          <div class="ms-3 mt-1 text-caption text-red" v-if="isRejected">
            เหตุผล: {{ reservationStatusNote }}
          </div>
        </div>

        <div class="d-flex mx-auto justify-end">
          <div v-if="editable" class="mx-5">
            <ManageReservedClassroom />
          </div>

          <div v-if="showAutoDelete && isRejected" class="text-grey mx-5"> 
            **คำเตือน** ระบบจะลบคำขออัตโนมัติภายใน 1 สัปดาห์
          </div>

          <div v-if="deleteable && isPending" class="text-grey mx-5">
            <v-btn
              color="white"
              class="bg-red mx-5"
              @click="confirmDeleteDialog = true"
            >
              ยกเลิก
            </v-btn>

            <v-dialog v-model="confirmDeleteDialog" width="300">
              <v-card class="align-center pa-5">
                คุณต้องการที่จะยกเลิกหรือไม่
                <!-- ################################################################################################## -->
                <div>
                  <v-btn
                    color="white"
                    class="bg-red mt-2 mx-5"
                    @click="confirmDeleteDialog = false, $emit('cancel')"
                  >
                    ใช่
                  </v-btn>
                  <!-- ################################################################################################## -->
                  <v-btn
                    color="white"
                    class="bg-green mt-2 mx-5"
                    @click="confirmDeleteDialog = false"
                  >
                    ไม่ใช่
                  </v-btn>
                </div>
              </v-card>
            </v-dialog>
          </div>
        </div>
      </v-col>
      <v-divider vertical class="mb-2"> </v-divider>
      <v-col v-if="managable" cols="12" md="5">
        <div v-if="managable" class="mt-2 justify-end mb-5 text-center">
          <v-textarea
            v-model="manageDetail"
            label="หมายเหตุ"
            class="mx-5 mt-1"
          ></v-textarea>
          <div class="d-flex justify-end">
            <v-btn
              color="white"
              class="bg-green mx-5"
              @click="$emit('approve', reservation.id, manageDetail)"
            >
              อนุมัติ
            </v-btn>
            <v-btn
              color="white"
              class="bg-red mx-5"
              @click="$emit('reject', reservation.id, manageDetail)"
            >
              ไม่อนุมัติ
            </v-btn>
          </div>
        </div>
      </v-col>
    </v-row>
  </v-card>
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
    showSize: { type: Boolean, require: false, dafault: true },
    showSeat: { type: Boolean, require: false, default: true },
    showAutoDelete: { type: Boolean, requre: false, default: false },

    // control options
    editable: { type: Boolean, require: false, default: false },
    managable: { type: Boolean, require: false, default: false },
    deleteable: { type: Boolean, require: false, dafault: false },
    selectable: { type: Boolean, require: false, dafault: false },

    // miscellaneous
    width: { type: Number, require: false, default: 700 },
    cardSelected: { type: Boolean, require: false, default: false },
  },

  emits: ["approve", "reject", "cancel", "selected", "unselected"],

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
    manageDetail: null,
    confirmDeleteDialog: false,
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
        case "canceled":
          return "ยกเลิกแล้ว";
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
        case "canceled":
          return "text-red";
        default:
          defaultValue;
      }
    },

    reservationNote() {
      let defaultValue = "(ไม่ระบุ)";
      if (!this.reservation) return defaultValue;
      return this.reservation.reserveNote || defaultValue;
    },

    reservationStatusNote() {
      let defaultValue = "(ไม่ระบุ)";
      if (!this.reservation) return defaultValue;
      return this.reservation.approveNote || defaultValue;
    },

    isRejected() {
      if (!this.reservation || !this.reservation.status) return false;
      return this.reservation.status == "rejected";
    },

    isPending() {
      if (!this.reservation || !this.reservation.status) return false;
      return this.reservation.status == "pending";
    },

    isApproved() {
      if (!this.reservation || !this.reservation.status) return false;
      return this.reservation.status == "approved";
    },
  },

  setup() {},

  beforeMount() {},

  mounted() {
  },
};
</script>
