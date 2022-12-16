import { ref } from 'vue'
import { defineStore } from 'pinia'
import axios from '../plugins/axios.js'

export const useBuildingStore = defineStore('building', () => {

	const buildings = ref([])

	async function fetchAll() {
		let res = await axios.get('/buildings')
		buildings.value = res.data
		return buildings.value
	}
  
	return { buildings, fetchAll }

  })