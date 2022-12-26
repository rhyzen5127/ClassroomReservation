import { defineStore } from 'pinia'
import { useBuildingStore } from './buildings'
import { useClassroomStore } from './classrooms'
import { useReservationStore } from './reservations'
import { useUserStore } from './users'

export const useStores = defineStore('store', () => {
	
	const building = useBuildingStore()
	const classroom = useClassroomStore()
	const reservation = useReservationStore()
	const user = useUserStore()

	return {
		building, classroom, reservation, user
	}

})