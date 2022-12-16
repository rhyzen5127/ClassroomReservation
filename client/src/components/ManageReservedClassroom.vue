<template>
    <div class="text-center">
        <v-dialog v-model="dialog">
            <template v-slot:activator="{ props }">
                <v-btn color="black" v-bind="props">
                    จัดการ
                </v-btn>
            </template>

            <v-card class="ma-15 pa-3">
                <v-card-text>
                    จัดการ
                </v-card-text>

                <!-- คุณลืม git fetch ก่อน pushy ครับคุณเซน -->

                <v-row class="mx-15 my-5">
                    <v-col>
                            <v-btn color="green" block @click="dialog = false">บันทึก</v-btn>
                    </v-col>
                    <v-col>
                            <v-btn color="red" block @click="dialog = false">ยกเลิกการจอง</v-btn>
                    </v-col>
                </v-row>

            </v-card>
        </v-dialog>
    </div>
</template>


<script>
export default ({
    name: 'ManageReservedClassroom',

    components: {},



    data: () => ({
        dialog: false
    })
})
</script>
