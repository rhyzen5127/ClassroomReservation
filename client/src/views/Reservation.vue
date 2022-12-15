<template>
  <div class="mt-10 mx-10">
    <div class="text-h6 mb-1">
      กรุณากรอกข้อมูล
    </div>
    <v-select v-model=building label="เลือกอาคาร"
      :items="['อาคารพระจอมเกล้าฯ', 'อาคารจุฬาพร 1', 'อาคารจุฬาพร 2', 'อาคารหอประชุมจุฬา']">
    </v-select>
    <v-select v-if="building == null"  v-model=room label="เลือกห้อง" :disabled="true"
      :items="[]">
    </v-select>
    <v-select v-else v-model=room label="เลือกห้อง (กรุณาเลือกอาคารก่อน)" :disabled="false"
      :items="['212', '224']">
    </v-select>
    
    <v-row class="mt-2 mx-5">
  <v-row justify="center">
  </v-row>
    </v-row>
  </div>
</template>
  
<script>
import { defineComponent } from 'vue';

export default defineComponent({
  name: 'Reservation',
  data: () => ({
    valid: true,
    building: [],
    room: [],
    building_item: [
      {
        title: "อาคารพระจอมเกล้าฯ",
      },
      {
        title: "อาคารจุฬาพรฯ 1",
      },
      {
        title: "อาคารจุฬาพรฯ 2",
      },
      {
        title: "หอประชุมจุฬาพรฯ",
      },
    ]
  }),
  methods: {
    isBuildingSelected () {
      console.log("isBuildingSelected worked");
      return building == null ?
      !false:
      !true;
    }
    
  },

  components: {
  },
});
</script>