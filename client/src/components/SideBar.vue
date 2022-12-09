<script setup>
import SideBarAccount from './icons/SideBarAccount.vue'
</script>

<template>
  <v-card>
    <v-layout>
      <v-navigation-drawer expand-on-hover rail>
        <SideBarAccount>
        </SideBarAccount>

        <v-divider></v-divider>

        <v-list density="compact" nav>
          <v-list-item color="orange" prepend-icon="mdi-home" title="หน้าหลัก" value="้home" to="/"></v-list-item>
          <v-list-item color="orange" prepend-icon="mdi-book" title="จองห้องเรียน" value="book"
            to="/reservation"></v-list-item>
          <v-list-item color="orange" prepend-icon="mdi-calendar" title="ประวัติการจอง" value="history" to="/history"></v-list-item>
          <v-list-item color="orange" prepend-icon="mdi-map" title="ดูแผนที่ห้องเรียน" value="map" to="/map"></v-list-item>
          <v-list-item color="cyan" prepend-icon="mdi-wrench" title="อนุมัติคำขอจอง"
            value="manageReservations" to="/managereservations"></v-list-item>
          <v-list-item color="cyan" prepend-icon="mdi-wrench" title="จัดการรายละเอียดห้องเรียน"
            value="manageClassroomDetails" to="/manageclassroomdetails"></v-list-item>
        </v-list>
      </v-navigation-drawer>

      <v-main style="height: 25000px"></v-main>
    </v-layout>
  </v-card>
</template>