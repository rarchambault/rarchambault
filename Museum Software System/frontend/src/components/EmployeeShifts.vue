
<template>
  <v-row class="fill-height">
    <v-col>
      <v-sheet height="64">
        <v-toolbar flat>
          <v-btn outlined class="mr-4" color="grey darken-2" @click="setToday">
            Today
          </v-btn>
          <v-btn fab text small color="grey darken-2" @click="prev">
            <v-icon small>
              mdi-chevron-left
            </v-icon>
          </v-btn>
          <v-btn fab text small color="grey darken-2" @click="next">
            <v-icon small>
              mdi-chevron-right
            </v-icon>
          </v-btn>
          <v-toolbar-title v-if="$refs.calendar">
            {{ $refs.calendar.title }}
          </v-toolbar-title>
          <v-spacer></v-spacer>
          <v-btn @click="getShifts">Refresh</v-btn>
        </v-toolbar>
      </v-sheet>
      <v-sheet height="600">
        <v-calendar ref="calendar" v-model="focus" color="primary" :events="events"
          :type="type"></v-calendar>
      </v-sheet>
    </v-col>
  </v-row>
</template>

<script>
import axios from 'axios';

export default {
  name: 'EmployeeShifts',

  data: () => ({
    today: Date.today,
    events: [],

    focus: '',
    type: 'week',
    selectedEvent: {},
    selectedElement: null,
    selectedOpen: false,
    names: ['Meeting', 'Holiday', 'PTO', 'Travel', 'Event', 'Birthday', 'Conference', 'Party'],
  }),

  methods: {
    setToday() {
      this.focus = ''
    },
    prev() {
      this.$refs.calendar.prev()
    },
    next() {
      this.$refs.calendar.next()
    },
    getShifts() {
      this.events = [];

      let options = {
        method: 'GET',
        url: `http://localhost:8090/shift/employeeEmailAddress/` + sessionStorage.getItem("email"),
        headers: {
          'Content-Type': 'application/json',
        }
      };

      axios.request(options)
        .then(response => response.data)
        .then((shiftRes) => {
          if (shiftRes === "Shifts not found.") return;

          this.events = shiftRes.map(shift => {
            return {
              name: "Work Shift",
              start: `${shift.day.date} ${shift.startTime}`,
              end: `${shift.day.date} ${shift.endTime}`
            }
          });
        })
    }
  },

  mounted() {
    this.$refs.calendar.scrollToTime('08:00');
    this.getShifts();
  }
}
</script>

<style scoped>
.my-event {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  border-radius: 2px;
  background-color: #1867c0;
  color: #ffffff;
  border: 1px solid #1867c0;
  font-size: 12px;
  padding: 3px;
  cursor: pointer;
  margin-bottom: 1px;
  left: 4px;
  margin-right: 8px;
  position: relative;
}

.my-event.with-time {
  position: absolute;
  right: 4px;
  margin-right: 0px;
}
</style>