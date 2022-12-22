<template>
  <div class="ma-16">
    Home page (for all)
    <v-card class="">
      <ClassroomCard
        :reservation="reservationApproved"
        :width="648.91"
        class="my-5"
        showOwner
      />
    </v-card>
    <hr class="ma-5" />
    History page approved (for user, staff)
    <v-card class="">
      <ClassroomCard
        :reservation="reservationApproved"
        :width="700"
        class="my-5"
        showStatus
      />
      <ClassroomCard
        :reservation="reservationPending"
        :width="700"
        class="my-5"
        showStatus
        deleteable
        editable
      />
      <ClassroomCard
        :reservation="reservationRejected"
        :width="700"
        class="my-5"
        showStatus
        deleteable="true"
        editable
      />
    </v-card>
  </div>
</template>
    
  <script>
import ClassroomCard from "@/components/ClassroomCard.vue";

export default {
  name: "cardtest",

  components: { ClassroomCard },

  data: () => ({
    reservationApproved: {
      id: 1,
      owner: {
        id: 1,
        email: "admin",
        firstName: "Volapst",
        lastName: "Aswathchai",
        role: "staff",
        enabled: true,
        staff: true,
      },
      room: {
        id: 1,
        name: "601/1",
        level: "6",
        status: "ready",
        width: 12,
        length: 12,
        seats: 30,
        note: null,
        building: {
          id: 1,
          name: "จุฬาภรณ์ 1",
          levels: 7,
        },
        ready: true,
      },
      bookingTime: "2022-11-12T15:32:16",
      startTime: "2022-11-13T12:30:00",
      finishTime: "2022-11-13T14:30:00",
      status: "approved",
      reserveNote: null,
      statusNote: null,
    },

    reservationPending: {
      id: 1,
      owner: {
        id: 1,
        email: "admin",
        firstName: "Volapst",
        lastName: "Aswathchai",
        role: "staff",
        enabled: true,
        staff: true,
      },
      room: {
        id: 1,
        name: "601/1",
        level: "6",
        status: "ready",
        width: 12,
        length: 12,
        seats: 30,
        note: null,
        building: {
          id: 1,
          name: "จุฬาภรณ์ 1",
          levels: 7,
        },
        ready: true,
      },
      bookingTime: "2022-11-12T15:32:16",
      startTime: "2022-11-13T12:30:00",
      finishTime: "2022-11-13T14:30:00",
      status: "pending",
      reserveNote: null,
      statusNote: null,
    },

    reservationRejected: {
      id: 1,
      owner: {
        id: 1,
        email: "admin",
        firstName: "Volapst",
        lastName: "Aswathchai",
        role: "staff",
        enabled: true,
        staff: true,
      },
      room: {
        id: 1,
        name: "601/1",
        level: "6",
        status: "ready",
        width: 12,
        length: 12,
        seats: 30,
        note: null,
        building: {
          id: 1,
          name: "จุฬาภรณ์ 1",
          levels: 7,
        },
        ready: true,
      },
      bookingTime: "2022-11-12T15:32:16",
      startTime: "2022-11-13T12:30:00",
      finishTime: "2022-11-13T14:30:00",
      status: "rejected",
      reserveNote: null,
      statusNote: null,
    },
  }),
};
</script>