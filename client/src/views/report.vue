<template>
  <v-row class="ma-0">
    <v-col cols="12" md="5">
      <EventCalendar @dateSelected="selectDate" width="650" class="mt-5" />
      <!-- <div class="text-center ma-5">
        <v-btn color="orange"> นำออกรายการ </v-btn>
      </div> -->
    </v-col>
    <v-col cols="12" md="7">
      <v-card>
        <v-card color="orange" class="pa-2 mb-2">
          <v-icon icon="mdi-filter"></v-icon>
          ตัวกรอง
        </v-card>

        <div class="text-h6 mx-5 mb-2">วันที่ {{ displayTime() }}</div>

        <v-select
          v-model="building"
          label="เลือกอาคาร"
          :items="building_item"
          item-title="name"
          item-value="id"
          persistence-hint
          return-object
          class="mx-5 my-1"
        >
        </v-select>

        <v-select
          v-model="room"
          :label="isLabelChange"
          :disabled="isDisableRoomSelect"
          :items="classroom_item"
          item-title="name"
          item-value="id"
          return-object
          class="mx-5 my-1"
        >
        </v-select>
      </v-card>
      <v-card class="overflow-y-auto" max-height="314">
        <div v-for="i in reservations" :key="i.id" class="my-1">
          <ClassroomCard
            class="my-5"
            :reservation="i"
            :width="648.91"
            showOwner
            selectable
            :cardSelected="isReservationSelected(i)"
            @click="onSelectReservation(i, !isReservationSelected(i))"
          />
        </div>
      </v-card>
    </v-col>
    <div>
      <v-card class="text-center mx-3" color="orange">
        รายการที่จะส่งออก
      </v-card>
      <v-card
        width="1600"
        class="mx-3 pa-2 overflow-y-auto"
        max-height="240"
        height="240"
        box-shadow="5"
      >
        <ClassroomReportTable :schedules="reservationToExport" @delete-entry="(r) => onSelectReservation(r, false)" />
      </v-card>
      <div class="d-flex justify-end mr-7 mt-2">

        <v-btn :disabled="reservationToExport.length == 0" color="green" class="mx-2" @click="exportReservationSchedules"> ส่งออก </v-btn>

        <v-btn :disabled="reservationToExport.length == 0" color="red" class="mx-2" @click="deselectAllPresentReservations()"> รีเซ็ต </v-btn>
      </div>
    </div>
  </v-row>
</template>
  
<script>
import ClassroomCard from "@/components/ClassroomCard.vue";
import EventCalendar from "@/components/EventCalendar.vue";
import ClassroomReportTable from "@/components/ClassroomReportTable.vue";
import { useStores } from '../stores';

export default {
  name: "Report",

  components: {
    EventCalendar,
    ClassroomCard,
    ClassroomReportTable,
  },

  data() {
    return {
      reservations: [],
      building_item: [],
      classroom_item: [],

      reservationToExport: [],
      selReservations: {},
      building: null,
      room: null,
      startTime: null,
      finishTime: null,

      bannerPath: new URL(
        "@/assets/images/home-banner-background.png",
        import.meta.url
      ).href,

      tel: null,
    };
  },

  methods: {
    selectDate(startTime, finishTime) {
      this.startTime = startTime;
      this.finishTime = finishTime;
      this.loadReservations();
    },

    displayTime() {
      if (!this.startTime || !this.finishTime) return "กรุณาเลือกวันที่";

      var startTime = new Date(this.startTime.toISOString());
      var finishTime = new Date(this.finishTime.toISOString());
      finishTime.setDate(finishTime.getDate() - 1);

      let locale = "th-TH";
      let format = {
        year: "numeric",
        month: "short",
        day: "numeric",
      };

      if (startTime.getDate() == finishTime.getDate())
        return startTime.toLocaleDateString(locale, format);
      else
        return (
          startTime.toLocaleDateString(locale, format) +
          " - " +
          finishTime.toLocaleDateString(locale, format)
        );
    },

    loadReservations() {
      let params = {
        minReserveTime: this.startTime,
        maxReserveTime: this.finishTime,
        status: "approved",
      };

      if (this.room) {
        this.stores.reservation
          .fetchFromClassroom(this.room.id, params)
          .then((res) => {
            this.reservations = res;
            this.sortReservationSelection()
          })
          .catch((err) => {
            console.error("Failed to fetch reservations: " + err);
          });
      } 
      else if (this.building) {
        this.stores.reservation
          .fetchFromBuilding(this.building.id, params)
          .then((res) => {
            this.reservations = res;
            this.sortReservationSelection()
          })
          .catch((err) => {
            console.error("Failed to fetch reservations: " + err);
          });
      } 
      else {
        this.stores.reservation
          .fetchAll(params)
          .then((res) => {
            this.reservations = res;
            this.sortReservationSelection()
          })
          .catch((err) => {
            console.error("Failed to fetch reservations: " + err);
          });
      }
    },

    selectAllPresentReservations() {
      this.reservations.forEach(r => this.onSelectReservation(r, true))
    },

    deselectAllPresentReservations() {
      this.reservationToExport = []
      //this.reservations.forEach(r => this.onSelectReservation(r, false))
    },

    onSelectReservation(reservation, selected) {
      if (selected) {
        this.reservationToExport.push({ id: reservation.id, reservation, note: "" })
       // this.selReservations[reservation.id] = undefined
      } else {
        this.reservationToExport = this.reservationToExport.filter(v => v.id != reservation.id)
        //this.selReservations[reservation.id] = reservation
      }
    },

    isReservationSelected(reservation) {
      return this.reservationToExport.find(v => v.id == reservation.id) ? true : false //this.selReservations[reservation.id] ? true : false
    },

    sortReservationSelection() {
        this.reservations.sort((r1, r2) => {
        let r1s = this.isReservationSelected(r1)
        let r2s = this.isReservationSelected(r2)
        if (r1s === r2s) {
          // sort normally
          return 0
        }
        if (r1s) {
          return -1
        }
        return 1
     })
    },

    exportReservationSchedules()
    {
      this.stores.reservation.exportPDF(this.reservationToExport.map(v => ({ id: parseInt(v.id), note: v.note })))
      .then(res => {
        var fileURL = window.URL.createObjectURL(new Blob([ res ], { type: "application/pdf"} ))
        var fileLink = document.createElement('a')
        fileLink.href = fileURL
        fileLink.setAttribute('download', 'schedules.pdf')
        document.body.appendChild(fileLink)
        fileLink.click()
      })
      .catch(err => {
        console.log(err)
      })
    }

  },

  watch: {

    building(newVal) {
      if (!newVal) return;

      this.classroom_item = [];
      this.room = null;

      this.stores.classroom
        .fetchClassroomInBuilding(newVal.id)
        .then((res) => {
          this.classroom_item = res;
        })
        .catch((err) => {
          console.error("Cannot fetch classrooms data: " + err.message);
        });

      this.loadReservations()
    },

    room(newVal) {
      this.loadReservations()
    }
  },

  computed: {
    // [Room select dropdown] Disable state True | False
    isDisableRoomSelect() {
      return this.building ? false : true;
    },

    // [Room select dropdown] Label
    isLabelChange() {
      return this.building
        ? "กรุณาเลือกห้อง"
        : "กรุณาเลือกห้อง (กรุณาเลือกอาคารก่อน)";
    },

  },

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

  mounted() {
    this.stores.building
      .fetchAll()
      .then((res) => {
        this.building_item = res;
      })
      .catch((err) => {
        console.log(err);
        this.building_item = [];
      });
  },
};
</script>