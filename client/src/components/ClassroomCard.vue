<template>
	<div>
		<v-card class="mx-auto" max-width="650" max-height="300" >
			<v-card-item>
				<v-row>
					<v-col cols="12" md="4">
						<v-img src="https://cdn.vuetifyjs.com/images/cards/halcyon.png" max-width="512"
							max-height="512"></v-img>
					</v-col>

					<v-col cols="12" md="8">
						<div class="text-overline mb-1">
							วันที่ 19 ธันวาคม 2565 เวลา 13:00 - 16:30 น.
						</div>

						<div class="text-h6 mb-1">
							211
						</div>

						<div class="text-caption">
							อาคารพระจอมเกล้าฯ SC-08
						</div>

						<div class="text-caption">
							ขนาดห้อง: ใหญ่
						</div>

						<div class="text-caption">
							จำนวนที่นั่ง: ประมาณ 500 ที่นั่ง
						</div>

						<v-card-actions class="mx-auto justify-end">
							<div class="text-caption mr-1">
								สถานะ:
							</div>

							<div class="text-caption mr-5" :class="statusColor">
								{{ status }}
							</div>

									<ManageReservedClassroom/>
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

	data: () => ({
		status: "รอการอนุมัติ", //รอการอนุมัติ,  อนุมัติแล้ว,  ไม่อนุมัติ: สาเหตุ
		statusColor: "text-orange",  //orange,  green,  red
	})
})
</script>
