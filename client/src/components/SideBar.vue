<template>
  <div>
    <v-navigation-drawer permanent>
      <SideBarAccount :title="this.title" :email="this.email" :isLoggedIn="this.isLoggedIn" />

      <v-list density="compact" nav>
        <template v-for="item in nav_item" :key="item.title">
          <v-list-item link exact :color="item.color" :prepend-icon="item.icon" :title="item.title" :value="item.value"
            :to="item.to" v-if="isShowMenu(item.checkRole)" />
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
    role: {
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
    icons: [
      'mdi-facebook',
      'mdi-twitter',
      'mdi-linkedin',
      'mdi-instagram',
    ],

    nav_item: [
      {
        color: "orange",
        icon: "mdi-home",
        title: "หน้าหลัก",
        value: "home",
        to: "/",
        checkRole: "null,user,staff,admin"
      },
      {
        color: "orange",
        icon: "mdi-book",
        title: "ค้นหาหรือจองห้องเรียน",
        value: "reservation",
        to: "/reservation",
        checkRole: "null,user,staff,admin"
      },
      {
        color: "orange",
        icon: "mdi-calendar",
        title: "ประวัติการจอง",
        value: "history",
        to: "/history",
        checkRole: "user,staff,admin"
      },
      {
        color: "cyan",
        icon: "mdi-wrench",
        title: "อนุมัติคำขอจอง",
        value: "manageReservations",
        to: "/managereservations",
        checkRole: "staff,admin"
      },
      {
        color: "cyan",
        icon: "mdi-wrench",
        title: "แก้ไขรายละเอียดห้องเรียน",
        value: "manageClassroomDetails",
        to: "/manageclassroomdetails",
        checkRole: "staff,admin"
      },
    ]
  }),
  // Methods/Function
  methods: {
    isShowMenu(acceptRole) {
      // console.log('CONFIG.ROLE: ', role);
      // console.log('USER.ROLE: ', localStorage.getItem("role"));
      return (acceptRole.includes(this.role + ""))
    }
  },
}
</script>