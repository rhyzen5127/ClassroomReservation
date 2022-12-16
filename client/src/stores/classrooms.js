import { ref } from 'vue'
import { defineStore } from 'pinia'
import axios from '../plugins/axios.js'

export const useClassroomStore = defineStore('classrooms', () => {

	const classrooms = ref([])

	async function fetchAll() {
		let res = await axios.get('/classrooms')
		classrooms.value = res.data
		return classrooms.value
	}

	async function fetchClassroomInBuilding(buildingId) {
		let res = await axios.get('/buildings/' + buildingId + "/classrooms")
		return res.data
	}
  
	async function checkIsRoomAvailable(roomId, startTime, finishTime) {

		let res = await axios.get('/classrooms/' + roomId + "/availability", {
			params: {
				startTime, 
				finishTime
			}
		})

		console.log(res)

		return res.data.available
	}

	return { classrooms, fetchAll, fetchClassroomInBuilding, checkIsRoomAvailable }

  })