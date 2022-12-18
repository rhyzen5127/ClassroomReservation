<script setup>
import { ref, reactive, watch } from 'vue';
import '@fullcalendar/core/vdom'
import FullCalendar from '@fullcalendar/vue3'
import dayGridPlugin from '@fullcalendar/daygrid'
import timeGridPlugin from '@fullcalendar/timegrid'
import listPlugin from '@fullcalendar/list'
import interactionPlugin from '@fullcalendar/interaction'

</script>

<template>
    <v-card :width="width" class="">
        <FullCalendar v-bind:options="options" />
    </v-card>
</template>

<script>
export default {
    props: {
        width: {
            type: Number,
            required: true
        }
    },

    emits: [ 'dateSelected', 'eventSelected' ],

    data: () => {
        return {
            options:  {
                plugins: [ dayGridPlugin, timeGridPlugin, listPlugin, interactionPlugin ],
                initialView: 'dayGridMonth',
                headerToolbar: {
                    right: 'prev,next',
                    center: 'title',
                    // right: 'dayGridMonth,dayGridWeek,listDay'
                    left: 'today'
                },
                editable: true,
                selectable: true,
                weekends: true,
                events: [],
            }
        }
    },

    methods: {

        onDateSelected(arg) {
            this.$emit("dateSelected", arg.start, arg.end)
        },

        // eventClick(arg) {
        //     alert("select eventClick")
        // }

    },

    mounted() {
        this.options.select = this.onDateSelected
    }

}
</script>