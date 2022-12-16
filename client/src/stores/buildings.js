import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const buildingStore = defineStore('building', () => {

	const buildings = ref([])

	const doubleCount = computed(() => count.value * 2)

	async function fetch() {
		buildings.value = []
		// buildings.value = await axios.get('/buildings')
	}
  
	return { buildings, fetch }

  })