<template>
  <div class="mt-10 mx-10">
    <v-row>

      <v-col cols="12" md="6">
        <div class="text-h6 mb-1">
          เร็ว ๆ นี้
        </div>
        <div v-for="i in nReservation" :key="i + '-classCard'" class="my-4">
          <ClassroomCard></ClassroomCard>
        </div>
      </v-col>

      <v-col cols="11" md="5">
        <EventCalendar />
      </v-col>
      
    </v-row>
  </div>
</template>
  
<script>
import ClassroomCard from '@/components/ClassroomCard.vue'
import EventCalendar from '@/components/EventCalendar.vue'
import { defineComponent } from 'vue';

// Components

export default defineComponent({
  data: () => ({
    nReservation: 1,
  }),

  name: 'History',

  components: {
    ClassroomCard,
    EventCalendar,
  },
});
</script>
  