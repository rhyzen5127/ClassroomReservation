<template>
  <div class="mt-10 mx-10">
    <div class="text-h6 mb-1">
      เร็ว ๆ นี้
    </div>
    <div v-for="i in 10" :key="i+'-classCard'" class="my-4">
      <ClassroomCard></ClassroomCard>
    </div>
  </div>
</template>

<script>
import ClassroomCard from '@/components/ClassroomCard.vue'

export default {
  name: 'Home',

  components: {
    ClassroomCard,
  },
}
</script>