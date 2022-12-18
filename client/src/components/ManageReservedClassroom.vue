<template>
    <div class="text-center">
        <v-dialog v-model="classroomEditDialog">
            <template v-slot:activator="{ props }">
                <v-btn class="bg-orange" color="white" v-bind="props">
                    แก้ไข
                </v-btn>
            </template>

            <v-card class="ma-15 pa-5">
                <v-card-text class="text-h6 mx-10">
                    แก้ไข
                </v-card-text>

                <div class="mt-2 mx-10">
                    <v-select v-model="building" label="เลือกอาคาร" :items="building_item" item-title="name"
                        item-value="id" persistence-hint return-object>
                    </v-select>

                    <v-select v-model="room" :label="isLabelChange" :disabled="isDisableRoomSelect"
                        :items="classroom_items" item-title="name" item-value="id" return-object>
                    </v-select>

                    <Datepicker v-model="date" :enable-time-picker="false" placeholder="กรุณาเลือกวันที่" fixed-start
                        :clearable="false" :min-date=this.date />

                    <v-row class="mt-2">
                        <v-col>
                            <Datepicker v-model="startTime" time-picker minutes-increment="30"
                                minutes-grid-increment="30" :clearable="false" :start-time="{ hours: 8, minutes: 30 }"
                                placeholder="กรุณาเลือกเวลาเริ่มต้น" />
                        </v-col>

                        <v-col>
                            <Datepicker v-model="endTime" time-picker minutes-increment="30" minutes-grid-increment="30"
                                :clearable="false" :start-time="{ hours: 8, minutes: 30 }"
                                placeholder="กรุณาเลือกเวลาสื้นสุด" :min-time="startTime" />
                        </v-col>
                    </v-row>

                    <v-card class="mt-5 pa-1" :color="roomStatusColor">
                        <div class="text-center">
                            {{ roomStatus }}
                        </div>
                    </v-card>

                    <div>
                        <v-overlay v-model="loading" class="align-center justify-center">
                            <v-progress-circular size="70" color="orange" indeterminate>
                            </v-progress-circular>
                        </v-overlay>
                    </div>

                    <v-row class="mt-2 mx-5">
                        <v-row justify="center">
                        </v-row>
                    </v-row>
                </div>

                <v-row class="mx-15 my-5">
                    <v-col>
                        <v-btn color="green" block @click="classroomEditDialog = true, editConfirmDialog = true">
                            บันทึก
                        </v-btn>
                        <v-dialog v-model="editConfirmDialog">
                            <v-card class="ma-15">
                                <v-card-text class="d-flex align-center justify-center my-2">
                                    คุณต้องการยืนยันที่จะทำการจองห้องเรียนหรือไม่
                                </v-card-text>
                                <v-card-actions>
                                    <v-row class="mb-5 mx-2">
                                        <v-col>
                                            <v-btn class=" bg-green" color="white" block @click="editConfirmDialog = true, successDialog = true,
                                            classroomEditDialog = true, validateEdit">
                                                ใช่
                                            </v-btn>
                                            <v-dialog v-model="successDialog">
                                                <v-card class="d-flex align-center justify-center my-2 pa-5">
                                                    <div class="text-center" v-if="isReservationSuccess">
                                                        <h2 class="text-green"> สำเร็จ </h2>
                                                        สามารถตรวจสอบสถานะการจองห้องได้ที่หน้า ประวัติการจอง
                                                    </div>
                                                    <div class="text-center" v-else>
                                                        <h2 class="text-red"> ล้มเหลว </h2>
                                                        เกิดข้อผิดพลาด กรุณาลองใหม่อีกครั้งในภายหลัง
                                                    </div>
                                                    <v-btn color="red" class="mt-5 bg-white"
                                                        @click="confirmDialog = true, successDialog = false">
                                                        <h5> ปิด </h5>
                                                    </v-btn>
                                                </v-card>
                                            </v-dialog>
                                        </v-col>
                                        <v-col>
                                            <v-btn class="bg-red" color="white" block
                                                @click="editConfirmDialog = false, classroomEditDialog = true" validate>
                                                ยกเลิก
                                            </v-btn>
                                        </v-col>
                                    </v-row>
                                </v-card-actions>
                            </v-card>
                        </v-dialog>
                    </v-col>

                    <v-col>
                        <div class="text-center">
                            <v-btn color="red" block @click="classroomEditDialog = true,
                            editCancelDialog = true">
                                ยกเลิกการจอง
                            </v-btn>
                            <v-dialog v-model="editCancelDialog">
                                <v-card class="ma-15">
                                    <v-card-text class="d-flex align-center justify-center my-2">
                                        ยืนยันที่จะยกเลิกหรือไม่
                                    </v-card-text>
                                    <v-card-actions>
                                        <v-row class="mb-5 mx-2">
                                            <v-col>
                                                <v-btn class=" bg-green" color="white" block @click="editCancelDialog, failedDialog = true,
                                                classroomEditDialog = true, validateRemove">
                                                    ยืนยัน
                                                </v-btn>
                                                <v-dialog v-model="failedDialog">
                                                    <v-card class="d-flex align-center justify-center my-2 pa-5">
                                                        <div class="text-center" v-if="isReservationRemoved">
                                                            <h2 class="text-green"> สำเร็จ </h2>
                                                            สามารถตรวจสอบสถานะการจองห้องได้ที่หน้า ประวัติการจอง
                                                        </div>
                                                        <div class="text-center" v-else>
                                                            <h2 class="text-red"> ล้มเหลว </h2>
                                                            เกิดข้อผิดพลาด กรุณาลองใหม่อีกครั้งในภายหลัง
                                                        </div>
                                                        <v-btn color="red" class="mt-5 bg-white"
                                                            @click="confirmDialog = true, failedDialog = false">
                                                            <h5> ปิด </h5>
                                                        </v-btn>
                                                    </v-card>
                                                </v-dialog>
                                            </v-col>
                                            <v-col>
                                                <v-btn class="bg-red" color="white" block
                                                    @click="editCancelDialog = false, classroomEditDialog = true">
                                                    ยกเลิก
                                                </v-btn>
                                            </v-col>
                                        </v-row>
                                    </v-card-actions>
                                </v-card>
                            </v-dialog>
                        </div>


                    </v-col>
                </v-row>
                    <v-btn class="mx-16" color="grey" @click="classroomEditDialog = false">
                      ปิด  
                    </v-btn>

            </v-card>
        </v-dialog>
    </div>
</template>

<script>
import Datepicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css'
import axios from 'axios';
import { useBuildingStore } from '@/stores/buildings'
import { useClassroomStore } from '@/stores/classrooms'

import { defineComponent } from 'vue';

const $axios = axios.create({
    baseURL: "http://localhost:8080",
})

export default defineComponent({
    name: 'ManageReservedClassroom',

    components: { Datepicker },

    data: () => ({
        classroomEditDialog: false,
        editCancelDialog: false,
        editConfirmDialog: false,
        successDialog: false,
        failedDialog: false,
        building: null,
        room: null,
        startTime: null,
        date: new Date(),
        endTime: null,
        loading: true,

        isReservationSuccess: null,
        isReservationRemoved: null,

        roomStatus: "สถานะห้องเรียน", //ว่าง | ไม่ว่าง
        roomStatusColor: "grey", //เขียว | แดง

        building_item: [],
        classroom_items: []
    }),

    computed: {

        // [Room select dropdown] Disable state True | False
        isDisableRoomSelect() {
            return this.building ? false : true;
        },

        // [Room select dropdown] Label
        isLabelChange() {
            return this.building ? "กรุณาเลือกห้อง" : "กรุณาเลือกห้อง (กรุณาเลือกอาคารก่อน)";
        }
    },

    watch: {

        date(newVal, oldVal) {
            if (newVal) {
                console.log(this.date);
                this.validateRoomAvailability()
            }
        },

        building(newVal, oldVal) {
            if (newVal) {

                // clear classroom options & input
                this.classroom_items = []
                this.room = null

                this.loading = true

                // fetch classroom options
                this.classroomStore.fetchClassroomInBuilding(newVal.id).then(res => {
                    this.classroom_items = res
                    this.loading = false
                }).catch(err => {
                    console.error("Cannot fetch classrooms data: " + err.message)
                    this.loading = false
                })

                this.validateRoomAvailability()

            }
        },

        startTime(newVal) {
            if (newVal) {
                this.validateRoomAvailability()
            }
        },

        endTime(newVal) {
            if (newVal) {
                this.validateRoomAvailability()
            }
        }

    },

    methods: {
        setDisplayStatus(status) {
            if (status) {
                roomStatus = "ว่าง"
                roomStatusColor = "green"
            } else {
                roomStatus = "ไม่ว่าง"
                roomStatusColor = "red"
            }
        },

        getStartDate() {
            var startDatetime = new Date()
            startDatetime.setFullYear(this.date.getFullYear())
            startDatetime.setMonth(this.date.getMonth())
            startDatetime.setDate(this.date.getDate())
            startDatetime.setHours(this.startTime.hours)
            startDatetime.setMinutes(this.startTime.minutes)
            startDatetime.setSeconds(this.startTime.seconds)

            return startDatetime.toISOString()


            // return this.date.getFullYear() + "-" + this.date.getMonth() + "-" + this.date.getDate() + "T"
            // + this.startTime.hours + ":" + this.startTime.minutes
        },

        getEndDate() {
            var finishDatetime = new Date()
            finishDatetime.setFullYear(this.date.getFullYear())
            finishDatetime.setMonth(this.date.getMonth())
            finishDatetime.setDate(this.date.getDate())
            finishDatetime.setHours(this.endTime.hours)
            finishDatetime.setMinutes(this.endTime.minutes)
            finishDatetime.setSeconds(this.endTime.seconds)

            return finishDatetime.toISOString()
            //return (this.date.getFullYear() + "-" + (this.date.getMonth() + 1) + "-" + this.date.getDate() + "T" + this.endTime.toLocaleTimeString()).toString()
        },

        // check if selected room & time range is available in the current time
        validateRoomAvailability() {

            // validate when all inputs are filled in
            if (this.room && this.date && this.startTime && this.endTime) {

                this.roomStatus = "สถานะห้องเรียน"
                this.roomStatusColor = "grey"

                this.loading = true

                // check if classroom is available
                this.classroomStore.checkIsRoomAvailable(this.room.id, this.getStartDate(), this.getEndDate()).then(res => {
                    this.setDisplayStatus(res)
                    this.loading = false
                }).catch(err => {
                    console.error("Cannot check classroom availability: " + err.message)
                    this.loading = false
                })

            }

        }
    },

    components: {
    },

    beforeMount() {
        this.date.setDate(this.date.getDate() + 1)
    },

    setup() {
        return {
            buildingStore: useBuildingStore(),
            classroomStore: useClassroomStore()
        }
    },

    mounted() {
        this.loading = true
        this.buildingStore.fetchAll().then((res) => {
            this.building_item = res
            this.loading = false
        }).catch(err => {
            console.error("Cannot fetch building data: " + err.message)
            this.building_item = []
            this.loading = false
        })
    }

});
</script>