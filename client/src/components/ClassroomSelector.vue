<template>
	<div v-if="compact" class="d-flex justify-space-between mb-6">
		<v-row>
			<v-col cols="12" md="6">
				<v-select
				class="mx-2"
				:items="building_item"
				v-model="building"
				label="เลือกอาคาร"
				item-title="name"
				item-value="id"
				return-object
				hide-details
				/>
			</v-col >
			<v-col cols="12" md="6">
				<v-select
				class="mx-2"
				:items="classroom_items"
				v-model="selectedRoom"
				:label="this.building ? 'เลือกห้องเรียน' : 'เลือกห้องเรียน (กรุณาเลือกอาคารก่อน)'"
				:disabled="!this.building"
				item-title="name"
				item-value="id"
				return-object
				hide-details
				/>
			</v-col>
		</v-row>

	</div>
	<div v-else>
		<v-select
			v-model="building"
			label="เลือกอาคาร"
			:items="building_item"
			item-title="name"
			item-value="id"
			return-object
			hide-details
		/>

		<v-select
			v-model="selectedRoom"
			:items="classroom_items"
			:label="this.building ? 'เลือกห้องเรียน' : 'เลือกห้องเรียน (กรุณาเลือกอาคารก่อน)'"
			:disabled="!this.building"
			item-title="name"
			item-value="id"
			return-object
			hide-details
		/>
	</div>
</template>

<script>
import { useClassroomStore } from "@/stores/classrooms.js"
import { useBuildingStore } from "@/stores/buildings.js"

export default {

	name: "SearchClassroom",
	
	props: {
		modelValue: { type: Object, required: true },
		compact: { type: Boolean, required: false, default: false },
		includeBuilding: { type: Boolean, required: false, default: false }
	},
	emits: [ "update:modelValue" ],

	data: () => ({

		building: null,

		building_item: [],
		classroom_items: [],

		loading: false

	}),

	computed: {

		selectedRoom: {
			
			get() {
				return this.modelValue
			},

			set(newVal) { 
				this.$emit('update:modelValue', newVal) 
			}
		}


	},

	methods: {
		
		fetchBuildingItems() {

			// clear classroom options & input
			this.building_items = [];
			this.building = null;
			this.loading = true;

			// fetch building options
			this.buildingStore
				.fetchAll()
				.then((res) => {
				this.building_item = res;
				this.building_item.sort((a, b) => a.name < b.name ? -1 : 1)
				this.loading = false;
				})
				.catch((err) => {
				console.error("Cannot fetch building data: " + err.message);
				this.loading = false;
				});

		},

		fetchClassroomItems() {

			// clear classroom options & input
			this.classroom_items = [];
			this.selectedRoom = null;

			if (!this.building) {
				return
			}

			this.loading = true;

			// fetch classroom options
			this.classroomStore
				.fetchClassroomInBuilding(this.building.id)
				.then((res) => {
				this.classroom_items = res;
				this.classroom_items.sort((a, b) => a.name < b.name ? -1 : 1)
				this.loading = false;
				})
				.catch((err) => {
				console.error("Cannot fetch classrooms data: " + err.message);
				this.loading = false;
				});

		}

	},

	watch: {
		building() {
			this.fetchClassroomItems()
		}
	},

	setup() {
		return {
			classroomStore: useClassroomStore(),
			buildingStore: useBuildingStore()
		}
	},

	mounted() {
		this.fetchBuildingItems()
	}

}

</script>