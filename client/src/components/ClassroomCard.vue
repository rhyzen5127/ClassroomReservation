<template>
	<div>
		<v-card :width="width" variant="">
			<v-row>
				<v-col class="pa-0" cols="12" md="4">
					<v-img src="https://storage.googleapis.com/inskru-optimized-image/-N2luZK8IiJoO1Qn1hfr:0.webp"
						:aspect-ratio="1" cover>
					</v-img>
				</v-col>
				<v-col cols="12" md="8">
					<div class="text-overline mb-1">
						{{ dateLabel }} : {{ timeRangeLabel }}
					</div>

					<div class="text-h6 mb-1">
						{{ name }}
					</div>

					<div class="text-caption">
						{{ building }}
					</div>

					<div class="text-caption">
						{{ sizeLabel }}
					</div>

					<div class="text-caption">
						{{ seatsLabel }}
					</div>

					<div v-if="isOwnerShow">
						<h6 class="mt-3">
							ผู้จอง:
						</h6>
						<h6 class="mx-5">
							{{ owner }}
						</h6>
						<h6 class="mx-5">
							{{ ownerEmail }}
						</h6>
					</div>

					<div v-if="isStatusShow" :class="'h6 text-caption mr-5 ' + statusNameColor">
						{{ statusName }}
					</div>


					<div v-if="editable" class="mx-auto justify-end">
						<ManageReservedClassroom />
					</div>

					<div v-if="deleteable && statusText=='rejected'" class="mt-7 justify-end">
						<v-btn class="bg-red" color="white" v-bind="props" @click="$emit('delete')">
							ลบ
						</v-btn>
					</div>

					<div v-if="managable" class="d-flex justify-end ms-16 mb-5 text-center">
						<v-btn color="white" class="bg-green mx-5" @click="$emit('approve')"> อนุมัติ </v-btn>
						<v-btn color="white" class="bg-red mx-5" @click="$emit('reject')"> ไม่อนุมัติ </v-btn>
					</div>
				</v-col>
			</v-row>
		</v-card>
	</div>
</template>

<!-- Parameter = Building, Name, Date, Time, Picture, Status -->
<script>
import ManageReservedClassroom from "./ManageReservedClassroom.vue"
import { useReservationStore } from "@/stores/reservations.js"

export default ({

	name: 'ClassroomCard',

	components: { ManageReservedClassroom },

	props: {
		building: {
			type: String,
			require: false,
			default: "<<Undefined Building>>",
		},

		width: {
			type: Number,
			require: false,
			default: "700",
		},

		name: {
			type: String,
			require: false,
			default: "<<Undefined Room>>",
		},

		dateStart: {
			type: Date,
			require: false,
			default: null,
		},

		dateEnd: {
			type: Date,
			require: false,
			default: null,
		},

		roomwidth: {
			type: Number,
			require: false,
			default: "<<Undefined Sizes>>",
		},

		roomlength: {
			type: Number,
			require: false,
			default: "<<Undefined Sizes>>",
		},

		seats: {
			type: Number,
			require: false
		},

		statusText: {
			type: String,
			require: false,
			default: "<<Undefined status>>",
		},

		editable: {
			type: Boolean,
			require: false,
			default: false
		},

		managable: {
			type: Boolean,
			require: false,
			default: false
		},

		owner: {
			type: String,
			require: false,
			default: "<<Undefined Owner>>"
		},

		ownerEmail: {
			type: String,
			require: false,
			default: "<<Undefined Email>>"
		},

		isOwnerShow: {
			type: Boolean,
			require: false,
			default: false
		},

		isStatusShow: {
			type: Boolean,
			require: false,
			dafault: true
		},

		deleteable: {
			type: Boolean,
			require: false,
			dafault: false
		}
	},

	emits: [
		"approve",
		"reject",
		"delete"
	],

	methods: {
		async deleteCard() {
			this.$emit("delete")
		}
	},

	data: () => ({
		status: "null",
		statusColor: "text-orange",  //orange,  green,  red
		dateFormat: {
			dateOptions: {
				year: "numeric",
				month: "short",
				weekday: "short",
				day: "numeric"
			},
			timeOptions: {
				hour: "numeric",
				minute: "numeric"
			},
			locale: 'th-TH'
		}
	}),

	computed: {

		dateLabel() {
			if (!this.dateStart) return ""
			let locale = this.dateFormat.locale
			let options = this.dateFormat.dateOptions
			return this.dateStart.toLocaleDateString(locale, options)
		},

		timeRangeLabel() {
			if (!this.dateStart || !this.dateEnd) return ""
			let locale = this.dateFormat.locale
			let options = this.dateFormat.timeOptions
			return this.dateStart.toLocaleTimeString(locale, options) + " น. - " + this.dateEnd.toLocaleTimeString(locale, options) + " น."
		},

		seatsLabel() {
			return this.seats ? (this.seats + " ที่นั่ง") : "(ไม่ระบุจำนวนที่นั่ง)"
		},

		sizeLabel() {
			return (this.roomwidth && this.roomlength) ? (this.roomwidth + " x " + this.roomlength + " เมตร") : "(ไม่ระบุขนาดห้อง)"
		},

		statusName() {
			if (this.statusText == 'pending') {
				return "รอการอนุมัติ"
			} else if (this.statusText == 'approved') {
				return "อนุมัติ"
			} else if (this.statusText == 'rejected') {
				return "ไม่อนุมัติ"
			} else {
				return "เกิดข้อผิดพลาด"
			}
		},

		statusNameColor() {
			if (this.statusText == 'pending') {
				return "text-orange"
			} else if (this.statusText == 'approved') {
				return "text-green"
			} else if (this.statusText == 'rejected') {
				return "text-red"
			} else {
				return "text-red"
			}
		}
	},

	setup() {
		return {
			reservationStore: useReservationStore()
		}
	},

	beforeMount() {

		// fix timezone
		let minuteOffset = new Date().getTimezoneOffset()
		if (this.dateStart) {
			console.log(this.dateStart)
			console.log(this.dateStart.toLocaleTimeString(this.dateFormat.locale, this.dateFormat.dateOptions))
			this.dateStart.setMinutes(this.dateStart.getMinutes() - minuteOffset)
			console.log(this.dateStart)
			console.log(this.dateStart.toLocaleTimeString(this.dateFormat.locale, this.dateFormat.dateOptions))
		} 
		if (this.dateEnd) this.dateEnd.setMinutes(this.dateEnd.getMinutes() - minuteOffset)
	},

	mounted() {
	
	}

})
</script>
