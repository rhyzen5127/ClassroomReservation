<template>
  <v-table>
    <thead>
      <tr>
        <th class="text-middle">อาคาร</th>
        <th class="text-middle">ห้อง</th>
        <th class="text-middle">จองโดย</th>
        <th class="text-middle">อีเมล</th>
        <th class="text-middle">รายละเอียด</th>
        <th class="text-middle">วันที่</th>
        <th class="text-middle">เวลา</th>
        <th class="px-16 text-middle">หมายเหตุ</th>
        <th class="text-middle"></th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="i in schedules" :key="i.id">

        <td>{{ i.reservation.room.building.name }}</td>
        <td>{{ i.reservation.room.name }}</td>
        <td>{{ i.reservation.owner.firstName + " " + i.reservation.owner.lastName }}</td>
        <td>{{ i.reservation.owner.email }}</td>
        <td>{{ i.reservation.reserveNote || "-" }}</td>
        <td>{{ formatDate(i.reservation.startTime) }}</td>
        <td>{{ formatTimeRange(i.reservation.startTime, i.reservation.finishTime) }}</td>
        <td>
          <v-text-field v-model="i.note" />
        </td>
        <td><v-btn color="red" icon="mdi-delete-outline" @click="$emit('delete-entry', i.reservation)"> </v-btn></td>
      </tr>

    </tbody>
  </v-table>
</template>


  <script>
export default {

  props: {
    schedules: {
      type: Array,
      required: false,
      default: []
    }
  },

  emits: [ 'delete-entry' ],

  data() {
    return {
      // config
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
      // data
      classrooms: [
        {
          building: "อาคารพระจอมเกล้า",
          room: "211",
          floor: "2",
          owner: "Sahachai Plangrit",
          email: "63050197@kmitl.ac.th",
          detail: "Workshop CS Variety 2022",
          date: "24/12/2565",
          time: "08:30 - 16:30 น.",
          note: "แม่บ้านกรุณาล็อคห้องเวลา 17:00 น.",
        },
        {
          building: "จุฬาพร 1",
          room: "219",
          floor: "2",
          owner: "Voraphat Asawathongchai",
          email: "63050182@kmitl.ac.th",
          detail: "CS Media By MOMO",
          date: "30/2/2563",
          time: "08-:30 - 16:30 น.",
          note: "แม่บ้านอย่าไปยุ่งกับไอเด็กพวกนี้ มันเมากาว",
        },
      ],
    };
  },

  methods: {
    formatDate(date) {
      let d = new Date(date)
      let locale = this.dateFormat.locale;
      let options = this.dateFormat.dateOptions;
      let minuteOffset = new Date().getTimezoneOffset();
      d.setMinutes(d.getMinutes() - minuteOffset);

      return d.toLocaleDateString(locale, options);
    },

    formatTimeRange(time1, time2) {
      let d1 = new Date(time1)
      let d2 = new Date(time2)
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
      )
    }
  },

  mounted() {
  }
};
</script>