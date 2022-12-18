<template>
  <v-container>
    <v-row class="ma-0">
      <v-col cols="12" md="7">
        <EventCalendar width="860" class="mt-5" />
      </v-col>
      <v-col cols="12" md="5">
        <h2 class="mb-2">
          สนใจห้องใดอยู่หรือไม่ ?
        </h2>
        <v-card>
          <v-card color="orange" varient="text" class="pa-2 mb-5">
            <v-icon icon="mdi-filter"></v-icon>
            ตัวกรอง
          </v-card>

          <v-select v-model="building" label="เลือกอาคาร" :items="building_item" item-title="name" item-value="id"
            persistence-hint return-object class="mx-16">
          </v-select>

          <v-select v-model="room" label="เลือกห้อง" :disabled="isDisableRoomSelect" :items="classroom_items"
            item-title="name" item-value="id" return-object class="mx-16">
          </v-select>
        </v-card>
        <v-divider></v-divider>
        <v-card class="overflow-y-auto" max-height="485">
          <div v-for="i in nReservation" :key="i + '-classCard'" class="my-4">
            <ClassroomCard width="648.91" class="my-5" isOwnerShow="true" />
            <v-divider></v-divider>
          </div>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import ClassroomCard from '@/components/ClassroomCard.vue'
import EventCalendar from '@/components/EventCalendar.vue'

export default {
  name: 'Home',

  components: {
    EventCalendar,
    ClassroomCard,
  },

  data() {
    return {
      nReservation: 20,
      bannerPath: new URL("@/assets/images/home-banner-background.png", import.meta.url).href,
      tel: null,
    };
  }

}
</script>