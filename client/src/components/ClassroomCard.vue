<template>
	<div>
		<v-card :width="width" variant="">
				<v-row>
					<v-col class="pa-0" cols="12" md="4">
						<v-img 
							src="https://storage.googleapis.com/inskru-optimized-image/-N2luZK8IiJoO1Qn1hfr:0.webp"
							:aspect-ratio="1" 
							cover>
						</v-img>
					</v-col>
					<v-col cols="12" md="8">
						<div class="text-overline mb-1">
							{{date}} : {{time}}
						</div>

						<div class="text-h6 mb-1">
							{{name}}
						</div>

						<div class="text-caption">
							{{building}}
						</div>

						<div class="text-caption">
							{{sizes}}
						</div>

						<div class="text-caption">
							{{seats}}
						</div>

						<h6 v-if="isOwnerShow" class="mt-3">
							ผู้จอง:
						</h6>
						<h6 v-if="isOwnerShow" class="mx-5">
							{{owner}}
						</h6>
						<h6 v-if="isOwnerShow" class="mx-5">
							{{ownerEmail}}
						</h6>

						<div v-if="editable" class="mx-auto justify-end">
							<div class="text-caption mr-1">
								สถานะ:
							</div>

							<div class="text-caption mr-5" :class="statusColor">
								<!-- {{ statusText }} -->
								{{ statusText }}
							</div>

							<ManageReservedClassroom/>
						</div>

						<div v-if="managable" class="mx-auto mb-5 text-center">
							<v-btn color="white" class="bg-green mx-5"> อนุมัติ </v-btn>
							<v-btn color="white" class="bg-red mx-5"> ไม่อนุมัติ </v-btn>
						</div>
					</v-col>
				</v-row>
		</v-card>
	</div>
</template>

<!-- Parameter = Building, Name, Date, Time, Picture, Status -->
<script>
import ManageReservedClassroom from "./ManageReservedClassroom.vue"
export default ({
	name: 'ClassroomCard',

	components: { ManageReservedClassroom },

	props: {
		building: {
			type: String,
			require: false,
			default: "<<Undefined Building>>",
		},

		name: {
			type: String,
			require: false,
			default: "<<Undefined Room>>",
		},

		date: {
			type: String,
			require: false,
			default: "<<Undefined Date>>",
		},

		time: {
			type: String,
			require: false,
			default: "<<Undefined Time>>",
		},

		sizes: [{
			type: Number,
			require: false,
			default: "<<Undefined Sizes>>",
		}, {
			type: Number,
			require: false,
			default: "<<Undefined Sizes>>",
		}],

		seats: {
			type: Number,
			require: false,
			default: "<<Undefined seats>>",
		},

		statusText: {
			type: String,
			require: false,
			default: "<<Undefined status>>",
		},

		width: {
			type: Number,
			require: false,
			default: -1,
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
		}
	},

	data: () => ({
		statusText: "รอการอนุมัติ", //รอการอนุมัติ,  อนุมัติแล้ว,  ไม่อนุมัติ: สาเหตุ
		statusColor: "text-orange",  //orange,  green,  red
	}),

	methods: {
		setStatus(status) {
			if (status == wait) {
				this.statusText = "รอการอนุมัติ"
				this.statusColor = "text-orange"
			} else if (status == accept) {
				this.statusText = "อนุมัติ"
				this.statusColor = "text-green"
			} else if (status == deny) {
				this.statusText = "ไม่อนุมัติ"
				this.statusColor = "text-red"
			} else {
				this.statusText = "เกิดข้อผิดพลาด"
				this.statusColor = "text-red"
			}
		}
	}
})
</script>
