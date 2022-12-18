import { ref } from 'vue'
import { defineStore } from 'pinia'
import { useUserStore } from './users.js'
import axios from '../plugins/axios.js'

export const useClassroomStore = defineStore('classrooms', () => {

	const classrooms = ref([])
	const userStore = useUserStore()

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

	async function updateClassroom(token, roomId, width, length, seats, isReady) {
		let headers = userStore.addBearerAuthHeader({}, token)
		let status = isReady === undefined ? undefined : isReady ? "ready" : "unready"
		await axios.post('/classrooms/' + roomId, { width, length, seats, status }, { headers })
	}

	return { classrooms, fetchAll, fetchClassroomInBuilding, checkIsRoomAvailable, updateClassroom }

  })