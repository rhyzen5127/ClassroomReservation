<template>
  <v-table height="300px">
    <thead>
      <tr>
        <th class="text-middle">อาคาร</th>
        <th class="text-middle">ห้อง</th>
        <th class="text-middle">ชั้น</th>
        <th class="text-middle">จองโดย</th>
        <th class="text-middle">อีเมล</th>
        <th class="text-middle">รายละเอียด</th>
        <th class="text-middle">วันที่</th>
        <th class="text-middle">เวลา</th>
        <th class="text-middle">หมายเหตุ</th>
        <th class="text-middle"></th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="item in classrooms" :key="item.name">

        <td>{{ item.building }}</td>
        <td>{{ item.room }}</td>
        <td>{{ item.floor }}</td>
        <td>{{ item.owner }}</td>
        <td>{{ item.email }}</td>
        <td>{{ item.detail }}</td>
        <td>{{ item.date }}</td>
        <td>{{ item.time }}</td>
        <td>
          <v-btn icon="mdi-pencil" class="pa-0 mr-2" variant="" />
          {{ item.note }}
        </td>
        <td><v-btn color="red" icon="mdi-delete-outline"> </v-btn></td>
      </tr>

    </tbody>
  </v-table>
</template>


  <script>
export default {
  data() {
    return {
      // config


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
};
</script>