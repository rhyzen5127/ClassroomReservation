<template>
  <div>
    <v-navigation-drawer permanent>
      <SideBarAccount :title="this.title" :email="this.email" :isLoggedIn="this.isLoggedIn"/>
      

      <v-list density="compact" nav>
        <template v-for="item in nav_item" :key="item.title">
          <v-list-item link exact :color="item.color" :prepend-icon="item.icon" :title="item.title" :value="item.value"
            :to="item.to" />
        </template>

      </v-list>
    </v-navigation-drawer>
  </div>
</template>

<script>
import SideBarAccount from './icons/SideBarAccount.vue'

export default {
  props: {
    title: {
      type: String,
      require: true,
      default: "",
    },
    email: {
      type: String,
      require: true,
      default: "",
    },
    isLoggedIn: {
      type: Boolean,
      require: true,
      default: false,
    }
  },
  
  // Components
  components: {
    SideBarAccount,
  },

  // Variables
  data: () => ({
    nav_item: [
      {
        color: "orange",
        icon: "mdi-home",
        title: "หน้าหลัก",
        value: "home",
        to: "/"
      },
      {
        color: "orange",
        icon: "mdi-book",
        title: "ค้นหาหรือจองห้องเรียน",
        value: "reservation",
        to: "/reservation"
      },
      {
        color: "orange",
        icon: "mdi-calendar",
        title: "ประวัติการจอง",
        value: "history",
        to: "/history"
      },
      {
        color: "cyan",
        icon: "mdi-wrench",
        title: "อนุมัติคำขอจอง",
        value: "manageReservations",
        to: "/managereservations"
      },
      {
        color: "cyan",
        icon: "mdi-wrench",
        title: "จัดการรายละเอียดห้องเรียน",
        value: "manageClassroomDetails",
        to: "/manageclassroomdetails"
      },
    ]
  }),
  // Methods/Function
  methods: {
  },
}
</script>