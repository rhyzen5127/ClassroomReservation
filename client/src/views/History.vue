<template>
  <div class="text-h5 ma-10">ประวัติการจอง</div>
  <div class="mt-10 mx-10">
    <!-- <v-row> -->
    <!-- <v-col cols="12" md="6"> -->
    <div class="d-flex">
      <v-select
        v-model="recentlyFilter"
        class="mx-2"
        label="ตัวกรอง"
        :items="group_options"
        item-title="name"
        item-value="id"
      />
      <v-select
        v-model="statusFilter"
        class="mx-2"
        label="สถานะ"
        :items="status_options"
        item-title="name"
        item-value="id"
      />
    </div>
    <div class="overflow-y-auto my-5" max-height="650">
      <ClassroomCard
        v-for="i in userReservations"
        :key="i + '-classCard'"
        class="my-4"
        :reservation="i"
        :width="700"
        @cancel="cancelReservation(i.id)"
        showStatus
        deleteable
        showReserveNote
        showAutoDelete
      />
    </div>
    <!-- </v-col> -->

    <!-- <v-col cols="11" md="5" class="mt-10">
        <EventCalendar :width="700" @dateSelected="selectDate" />
      </v-col> -->
    <!-- </v-row> -->
  </div>
</template>
  
<script>
import ClassroomCard from "@/components/ClassroomCard.vue";
import EventCalendar from "@/components/EventCalendar.vue";
import { useBuildingStore } from "@/stores/buildings.js";
import { useClassroomStore } from "@/stores/classrooms.js";
import { useReservationStore } from "@/stores/reservations.js";
import { useUserStore } from "../stores/users";

export default {
  data: () => ({
    loading: false,

    building_item: [],
    classroom_item: [],
    userReservations: [],
    group_options: [
      { id: "now", name: "เร็ว ๆ นี้" },
      { id: "past", name: "ผ่านไปแล้ว" },
      { id: "all", name: "ทั้งหมด" },
    ],
    status_options: [
      { id: "all", name: "ทั้งหมด" },
      { id: "pending", name: "รอการอนุมัติ" },
      { id: "approved", name: "อนุมัติแล้ว" },
      { id: "rejected", name: "ไม่อนุมัติ" },
      { id: "canceled", name: "ที่ถูกยกเลิก" },
    ],

    room: null,
    building: null,
    startTime: null,
    finishTime: null,
    recentlyFilter: "now",
    statusFilter: "all",

    bannerPath: new URL(
      "@/assets/images/home-banner-background.png",
      import.meta.url
    ).href,
    tel: null,
  }),

  watch: {
    recentlyFilter() {
      this.fetchUserReservation();
    },

    statusFilter() {
      this.fetchUserReservation();
    },
  },

  methods: {
    fetchUserReservation() {
      let token = localStorage.getItem("cookie");

      if (!token) {
        window.location.href = "/";
      }

      this.loading = true;

      // check logged in user first
      this.userStore
        .fetchCurrentUser(token)
        .then(() => {
          this.userReservations = [];

          let now = new Date();
          let dateFilterMin = null;
          let dateFilterMax = null;

          switch (this.recentlyFilter) {
            case "now":
              dateFilterMin = now.toISOString();
              break;
            case "past":
              dateFilterMax = now.toISOString();
              break;
            case "all":
              break;
          }

          let fetchParams = {
            status: this.statusFilter != "all" ? this.statusFilter : null,
            minReserveTime: dateFilterMin,
            maxReserveTime: dateFilterMax,
          };

          // fetch all user reservation
          this.reservationStore
            .fetchUserReserved(token, fetchParams)
            .then((res) => {
              console.log(res);
              this.userReservations = res;
              this.userReservations.sort((a, b) => {
                if (a.startTime < b.startTime) return -1;
                if (a.startTime > b.startTime) return 1;
                return 0;
              });
              this.loading = false;
            })
            .catch((err) => {
              console.error(err);
              this.userReservations = [];
              this.loading = false;
            });
        })
        .catch(() => {
          window.location.href = "/";
        });
    },

    selectDate(startTime, finishTime) {
      this.startTime = startTime;
      this.finishTime = finishTime;
      this.fetchUserReservation();
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

    cancelReservation(reservationId) {
      let token = localStorage.getItem("cookie");
      this.reservationStore
        .cancel(token, reservationId)
        .then(() => {
          this.fetchUserReservation();
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },

  setup() {
    return {
      reservationStore: useReservationStore(),
      buildingStore: useBuildingStore(),
      classroomStore: useClassroomStore(),
      userStore: useUserStore(),
    };
  },

  name: "History",

  components: {
    ClassroomCard,
    EventCalendar,
  },

  mounted() {
    this.fetchUserReservation();
  },
};
</script>
  