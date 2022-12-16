<template>
	<div>
		<v-card class="mx-auto" max-width="650" max-height="300">
			<v-card-item>
				<v-row>
					<v-col cols="12" md="4">
						<v-img src="https://cdn.vuetifyjs.com/images/cards/halcyon.png" max-width="512"
							max-height="512"></v-img>
					</v-col>

					<v-col cols="12" md="8">
						<div class="text-overline mb-1">
							<!-- วันที่ {{date}} เวลา {{time}} -->
							วันที่ 19 ธันวาคม 2565 เวลา 13:00 - 16:30 น.
						</div>

						<div class="text-h6 mb-1">
							<!-- {{name}} -->
							211
						</div>

						<div class="text-caption">
							<!-- {{building}} -->
							อาคารพระจอมเกล้าฯ SC-08
						</div>

						<div class="text-caption">
							<!-- {{sizes[0]}} * {{sizes[1]}} -->
							ขนาดห้อง: ใหญ่
						</div>

						<div class="text-caption">
							<!-- {{seats}} -->
							จำนวนที่นั่ง: ประมาณ 500 ที่นั่ง
						</div>

						<v-card-actions class="mx-auto justify-end">
							<div class="text-caption mr-1">
								<!-- {{status}} -->
								สถานะ:
							</div>

							<div class="text-caption mr-5" :class="statusColor">
								<!-- {{ statusText }} -->
								รอการอนุมัติ
							</div>

							<ManageReservedClassroom />
						</v-card-actions>
					</v-col>
				</v-row>

			</v-card-item>
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
			require: true,
			default: "อาคารพระจอมเกล้า SC-08",
		},

		name: {
			type: String,
			require: true,
			default: "211",
		},

		date: {
			type: String,
			require: true,
			default: "",
		},

		time: {
			type: String,
			require: true,
			default: "",
		},

		sizes: [{
			type: Number,
			require: true,
			default: "",
		}, {
			type: Number,
			require: true,
			default: "",
		}],

		seats: {
			type: Number,
			require: true,
			default: "",
		},

		statusText: {
			type: String,
			require: true,
			default: "",
		},
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
