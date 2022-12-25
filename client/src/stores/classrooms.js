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
  
	async function checkIsRoomAvailable(token, roomId, startTime, finishTime) {

		let options = { params: { startTime, finishTime } }

		try {
			// validate token
			await userStore.fetchCurrentUser(token)
			options = userStore.addBearerAuth(token, { params: { startTime, finishTime } })
		} catch (e) { }
		
		let res = await axios.get('/classrooms/' + roomId + "/availability", options)
		return res.data.available

	}

	async function updateClassroom(token, roomId, seats, isReady, note) {
		let options = userStore.addBearerAuth(token, {})
		let status = isReady === undefined ? undefined : isReady ? "ready" : "unready"
		await axios.post('/classrooms/' + roomId, { seats, status, note }, options)
	}

	return { classrooms, fetchAll, fetchClassroomInBuilding, checkIsRoomAvailable, updateClassroom }

  })